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


public class SongList 
{
	private SongNode head;
	
	public SongList()
	{
		head = null;
	}
	
	/*Inserts new nodes with the three parameters in alphabetical order based on the song title -- all comparisons are
	 * explained inside the method 
	 */
	
	public void insertInPlace(String SongTitle, String Artist, String SongPath)
	{
		SongNode insert = new SongNode(SongTitle, Artist, SongPath);
		SongNode curr = getHead();
		
		// checks if curr is null -- if so, inserts new node at curr, which is currently head at this juncture
		
		if(curr == null) 
		{
			head = insert;
		}
		
		/* compares new node to the first node -- second time through the method will be the first time this elif 
		 * statement will be executed
		 */
		
		else if (insert.compareTo(curr) <= 0)  
		{
			insert.setNext(getHead());
			head = insert;
		}
		/* if neither of the previous if statements are the case, this elif will be entered -- the first comparison will
		 * make sure that the next position isn't null; if it is, it will set the next position to the new node -- if it's not,
		 * it will keep comparing the new node to the curr node, and once it finds a node that it is smaller than, it will insert
		 * the node... again, if it comes to a point where the next position is null, it will set the new node to the next position
		 */
		else if (curr != null)
		{
			while(curr.next() != null)
			{
				if(insert.compareTo(curr.next()) == 0) // checks if the song already exits in the database -- update 4
				{
					return;
				}
				
				else if(insert.compareTo(curr.next()) <= 0)
				{
					insert.setNext(curr.next());
					curr.setNext(insert);
					break;
				}
				
				else
				{
					curr = curr.next();
				}
			}
			curr.setNext(insert);
		}
	}
	
	public void insertSearch(String SongTitle, String Artist, String SongPath)
	{
		/* simply adds songs from the search parameter into the search SongList. It is unnecessary to compare the nodes again
		 * to one another, since they are already ordered... less comparisons, faster run time! :)
		 */
		SongNode insert = new SongNode(SongTitle, Artist, SongPath);
		SongNode curr = getHead();
		if(curr == null)
		{
			head = insert;
		}
		else
		{
			while(curr.next() != null)
			{
				curr = curr.next();
			}
			curr.setNext(insert);
		}
	}
	
	/*method that searches for the song based on the given input from the textbox in the MPlayerPanel
	 * Songs are then inserted into a new SongList based on whether or not they fit the search parameter
	 */
	
	public void searchForSongs(String LookUp)
	{
		SongNode curr = getHead();
		if(curr.getSongTitle().contains(LookUp))
		{
			insertSearch(curr.getSongTitle(), curr.getArtist(), curr.getSongPath());
		}
	}
	
	
	
	
	/*just a method that helped me do some debugging. Not really necessary to the assignment, but useful for me
	 */
	
	public void printNodes() 
	{
		SongNode curr = getHead();
		while (curr != null) 
		{
			System.out.println(curr.getSongTitle() + " " + curr.getArtist() + " " + curr.getSongPath());
			curr = curr.next();
		}
		System.out.println();
	}
	

	public SongNode getHead() 
	{
		return head;
	}

	public void setHead(SongNode head) 
	{
		this.head = head;
	}
}
