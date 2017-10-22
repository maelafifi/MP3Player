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


public class SongNode 
{
	private SongNode next;
	private String Artist;
	private String SongTitle;
	private String SongPath;
	
	// constructor for nodes
	public SongNode(String SongTitle, String Artist, String SongPath, SongNode next)
	{
		this.SongTitle = SongTitle;
		this.Artist = Artist;
		this.SongPath = SongPath;
		this.next = next;
	}
	
	// default constructor for nodes without a next songnode parameter
	public SongNode(String SongTitle, String Artist, String SongPath)
	{
		this.SongTitle = SongTitle;
		this.Artist = Artist;
		this.SongPath = SongPath;
		next = null;
	}

	/*getters and setters for all parameters of SongNode*/
	public String getArtist()
	{
		return Artist;
	}

	public void setArtist(String Artist)
	{
		this.Artist = Artist;
	}

	public String getSongTitle()
	{
		return SongTitle;
	}

	public void setSongTitle(String SongTitle)
	{
		this.SongTitle = SongTitle;
	}

	public String getSongPath()
	{
		return SongPath;
	}

	public void setSongPath(String SongPath)
	{
		this.SongPath = SongPath;
	}
	
	public SongNode next()
	{
		return next;
	}
	
	public void setNext(SongNode other)
	{
		next = other;
	}
	
	/*compareTo method that is used in the songList class to add songs to the songlist based on the alphabetic order
	 * of the song title
	 */
	public int compareTo(SongNode other)
	{
		return this.getSongTitle().compareTo(other.getSongTitle());
	}
}
