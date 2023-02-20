import java.io.*;

/**
 * Program to provide information on a GPS track stored in a file.
 *
 * @author Louis Bishop
 */
public class TrackInfo 
{
    public static void main(String[] args) throws IOException
    {
        if(args.length == 0)
        {
            System.err.println("Please provide a valid filename");
            System.exit(0);
        }

        try 
        {
            Track track = new Track(args[0]);
            printTrackInfo(track);
        }   catch   (IOException e) 
        {
            System.err.println("Error reading file. File either does not exist or is in the wrong format.");
            System.exit(0);
        }   catch   (GPSException e)
        {
            System.err.println("Error performing GPS operations. Data may be inadequate or corrupted.");
            System.exit(0);
        }
        
    }

    public static void printTrackInfo(Track track)
    {
        System.out.printf("%d points in track\n",track.size());
        System.out.print("Lowest point is ");
        System.out.println(track.lowestPoint().toString());
        System.out.print("Highest point is ");
        System.out.println(track.highestPoint().toString());
        System.out.printf("Total distance = %.3f\n",track.totalDistance());
        System.out.printf("Average speed = %.3f\n",track.averageSpeed());
    }
}
