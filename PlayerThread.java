import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

class PlayerThread extends Thread 
{
	Player pl;
	PlayerThread(String filename)
	{
		FileInputStream file;
		try
		{
			file = new FileInputStream(filename);
			pl = new Player(file);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("No such file exists");
		}
		catch (JavaLayerException e)
		{
			e.getMessage();
		}
	}
	public void run()
	{
		try
		{
			pl.play();
		}
		catch(Exception e)
		{
		System.out.println("Cannot play song");
		}
	}
}
