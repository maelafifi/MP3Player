/*UPDATE 5 --- removed an array method that was not necessary -- removed static variable that kept track of the
 * amount of songs that have been loaded into the variable
 */

/* UPDATE 4 --- handles multiple directories loaded into database.
 * adds songs to already existing database, without deleting other songs that were previously loaded.
 * handles if a song already exists in the database -- does not add it
 * handles search by setting a string variable to equal the search string and sets the first character
 * in the string to upper case and the rest to lower case
 */

/* UPDATE 3 --- with comments and handles a search of a search
 * handles lower case and upper case cases for search
 */

// UPDATE 2 --- added some comments

import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

public class MPlayerPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private SongDatabase songDatabase; 
	private String columnNames[] = { "Title", "Artist"};
	private String [][] songs;
	private String [][] songs2;
	private static int searchCount = 0;

	JPanel topPanel, bottomPanel;
	JScrollPane centerPanel;
	Thread currThread = null;

	JButton playButton, stopButton, exitButton, loadMp3Button;
	JTextField searchBox;
	JButton searchButton;

	int selectedSong = -1;
	JTable table = null;
	private final JFileChooser fc = new JFileChooser();

	MPlayerPanel(SongDatabase songCol) 
	{
		this.songDatabase = songCol;
		this.setLayout(new BorderLayout());
		
		topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 4));

		loadMp3Button = new JButton("Load mp3");
		searchBox = new JTextField(5);
		searchButton = new JButton("Search");
		exitButton = new JButton("Exit");
		playButton = new JButton("Play");
		stopButton = new JButton("Stop");

		loadMp3Button.addActionListener(new ButtonListener());
		exitButton.addActionListener(new ButtonListener());
		playButton.addActionListener(new ButtonListener());
		stopButton.addActionListener(new ButtonListener());
		searchButton.addActionListener(new ButtonListener());

		topPanel.add(loadMp3Button);
		topPanel.add(searchBox);
		topPanel.add(searchButton);

		this.add(topPanel, BorderLayout.NORTH);

		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 3));
		bottomPanel.add(playButton);
		bottomPanel.add(stopButton);
		bottomPanel.add(exitButton);

		this.add(bottomPanel, BorderLayout.SOUTH);

		centerPanel = new JScrollPane();
		this.add(centerPanel, BorderLayout.CENTER);

		fc.setCurrentDirectory(new File("."));

	}

	class ButtonListener implements ActionListener 
	{
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource() == loadMp3Button) 
			{
				System.out.println("Load mp3 button");

				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setDialogTitle("Select a directory with mp3 songs");

				int returnVal = fc.showOpenDialog(MPlayerPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) 
				{
					File dir = fc.getSelectedFile();
					try 
					{
						songs = songDatabase.CreateArray(songDatabase.addSongsFromDirectory(dir));
						remove(centerPanel);
						table = new JTable(songs, columnNames);
						centerPanel = new JScrollPane(table);
						add(centerPanel, BorderLayout.CENTER);
					} 
					catch (CannotReadException | IOException | TagException | ReadOnlyFileException
							| InvalidAudioFrameException e1) 
					{
						e1.printStackTrace();
					}

					updateUI();

				}
			}

			else if (e.getSource() == playButton) 
			{

				if (table == null) // if there's not table of listed songs, return
					 return;
				else if(currThread == null) // if no song is currently playing, play the selected song
				{
					selectedSong = table.getSelectedRow();
					System.out.println("selected Song = " + selectedSong + " ");
					String songToBePlayed = songs[selectedSong][0];
					String SelectedSongPath = songDatabase.find(songToBePlayed);
					currThread = new PlayerThread(SelectedSongPath);
					currThread.start();
				}
				else // if there is a song currently playing, stop it and play the selected song
				{
					currThread.stop();
					selectedSong = table.getSelectedRow();
					System.out.println("selected Song = " + selectedSong + " ");
					String songToBePlayed = songs[selectedSong][0];
					String SelectedSongPath = songDatabase.find(songToBePlayed);
					currThread = new PlayerThread(SelectedSongPath);
					currThread.start();
				}
			} 
			
			else if (e.getSource() == stopButton)
			{
				if(currThread == null) // if no song is playing, return
				{
					return;
				}
				else // if a song is playing, stop the song
				{
					currThread.stop();
				}
			} 
			
			else if (e.getSource() == exitButton) 
			{
			
				System.exit(0);
			}

			
			/* part I) checks the value in the searchBox... If it is null, it will return if the array is also null
			 * and if the array isn't null, it will return the original songlist
			 * 
			 * part II) if neither of those are the case, an array list for the songs in the database that fit the value 
			 * of the search box is created, and per Alark's suggestions, all the values of the table are reset
			 * to empty and then new values are added based on the new arraylist returned
			 */
			else if (e.getSource() == searchButton)
			{
				String x = searchBox.getText();
				if(x.equals("")) // part I
				{
					if(searchCount % 2 > 0)
					{
						searchCount--;
						if(songs2 == null)
						{
							return;
						}
						else
						{
							int rowCount = table.getRowCount();
							int i = 0;
							while(i <= rowCount-1)
							{
								int j = 0;
								table.setValueAt("", i, j);
								j++;
								table.setValueAt("", i, j);
								i++;
							}
							for(int y = 0; y <= songs.length-1; y++)
							{
								table.setValueAt(songs2[y][0], y, 0);
								table.setValueAt(songs2[y][1], y, 1);
							}
						}
					}
				}
				else if(table != null)// part II
				{
					if(searchCount %2 == 0)
					{
						searchCount++;
						int rowCount = table.getRowCount();
						int i = 0;
						songs2 = new String[rowCount][2];
						while(i <= rowCount-1)
						{
							int j = 0;
							songs2[i][j] = songs[i][j];
							table.setValueAt("", i, j);
							j++;
							songs2[i][j] = songs[i][j];
							table.setValueAt("", i, j);
							i++;
						}
						String [][] searched = songDatabase.searchForSongs(x);
						for(int y = 0; y <= searched.length-1; y++)
						{
							table.setValueAt(searched[y][0], y, 0);
							table.setValueAt(searched[y][1], y, 1);
						}
					
					}
					else
					{
						System.out.println("cannot search a searched list. Please search any empty string and try again");
					}
				}
				else
				{
					System.out.println("No values in the table");
					return;
				}
			}
		}
	}
}
