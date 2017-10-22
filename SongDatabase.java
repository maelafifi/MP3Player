/*UPDATE 5 --- removed an array method that was not necessary -- removed static variable that kept track of the
 * amount of songs that have been loaded into the variable -- 
 * 
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

import java.io.File;
import java.io.IOException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class SongDatabase 
{
	private static SongList all = new SongList();
	private static SongList search = new SongList();
	
	/*adds songs from a selected directory -- checks to see if the file is a directory, and if it is, 
	 * it will recursively call the method again; if not, it will iterate through each file and grab
	 * all the files that end with ".mp3". Once the .mp3 file is found, it will grab the title, 
	 * artist, and path and "insertInPlace" which is adding the songs to a songlist based on alphabetical
	 * ordering of the song title
	 */
	
	public SongList addSongsFromDirectory(File file) throws 
	CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
	{
		for(File g : file.listFiles())
		{
			String artist;
			String title;
			if(g.isDirectory())
			{
				addSongsFromDirectory(g);
			}
			else
			{
				String path = g.getPath();
				if(path.endsWith(".mp3"))
				{
					try
					{
						AudioFile f = AudioFileIO.read(new File(path));
						Tag tag = f.getTag();
						artist = tag.getFirst(FieldKey.ARTIST);
						title = tag.getFirst(FieldKey.TITLE);
						if(artist == "" || title == "")
						{
							System.out.println(path + " omitted -- no title and/or artist");
						}
						else
						{
							all.insertInPlace(title, artist, path);
						}
					}
					catch(Exception e)
					{
						System.out.println("No available Artist or Title; ommited.");
						
					}
					
				}
			}
		}
		return all;
	}
	
	/* Takes in a given search string and looks through the songlist of all songs in the
	 * database. If a song matches the search string, it will be added to the search songlist. 
	 * For each search, head is reset to null, making the search songlist null and refreshing
	 * search so that each new search will only return the new search parameters
	 */
	
	public String[][] searchForSongs(String LookUp)
	{
		SongNode x = all.getHead();
		search.setHead(null);
		String LookUp2 = LookUp.toLowerCase();
		String LookUp3 = LookUp.toUpperCase();
		String LookUp4 = LookUp.substring(0, 1).toUpperCase() + LookUp.substring(1); 
		while(x != null)
		{
			if(x.getSongTitle().startsWith(LookUp) || x.getSongTitle().startsWith(LookUp2) || x.getSongTitle().startsWith(LookUp3) ||x.getSongTitle().startsWith(LookUp4))
			{
				search.insertSearch(x.getSongTitle(), x.getArtist(), x.getSongPath());
			}
			x = x.next();
		}
		String [][] searched = CreateArray(search);
		return searched;
		
	}
	
	/* creates an array for the table in the main player panel; the array is initialized with the
	 * static variable "counter" which is increased each time a song is added to the songlist "all"
	 * -- since the songlist is already in alphabetical order, the method simply adds songs to the 
	 * array in the order of the songlist and returns the newly created array
	 */
	
	public String[][] CreateArray(SongList Songs)
	{
		SongNode curr = Songs.getHead();
		int counts = 0;
		while(curr!=null)
		{
			counts++;
			curr = curr.next();
		}
		
		curr = Songs.getHead();
		
		String TitlesArtist[][] = new String[counts][2];
		int count = 0;
		while(curr != null)
		{
			String SongTitle = curr.getSongTitle();
			String Artist = curr.getArtist();
			if(count <= counts)
			{
				int i = 0;
				TitlesArtist[count][i] = SongTitle;
				i++;
				TitlesArtist[count][i] = Artist;
				count++;
			}
			curr = curr.next();
		}
		return TitlesArtist;
	}
	
	/* simple find method for the player thread that looks through the database based
	 * on the selected song from the mplayer panel -- once the song is found in the 
	 * database, the songs path is returned and passed to the player to be played
	 */
	public String find(String SelectedSong)
	{
		String selectedSong;
		SongNode curr = all.getHead();
		while(curr != null)
		{
			if(curr.getSongTitle().equalsIgnoreCase(SelectedSong))
			{
				selectedSong = curr.getSongPath();
				return selectedSong;
			}
			else
			{
				curr = curr.next();
			}
		}
		return null;
	}
}
