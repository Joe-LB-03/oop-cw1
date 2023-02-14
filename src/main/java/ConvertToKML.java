import java.io.*;

/**
 * Program to general a KML file from GPS track data stored in a file,
 * for the Advanced task of COMP1721 Coursework 1.
 *
 * Louis Bishop
 */
public class ConvertToKML 
{
    public static void main(String[] args) 
    {
        if(args.length != 2)
        {
            System.err.println("Please provide a valid filename");
            System.exit(0);
        }

        Track track = new Track();
        try 
        {
            track.readFile(args[0]);
        }   catch   (IOException e) 
        {
            System.err.println("Error reading file. File either does not exist or is in the wrong format.");
            System.exit(0);
        }   catch   (GPSException e)
        {
            System.err.println("Error performing GPS operations. Data may be inadequate or corrupted.");
            System.exit(0);
        }

        track.writeKML(args[1]);
    }
}
