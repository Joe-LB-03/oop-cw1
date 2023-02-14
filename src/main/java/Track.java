import java.util.*;
import java.io.*;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Represents a point in space and time, recorded by a GPS sensor.
 *
 * @author Louis Bishop
 */
public class Track 
{
    //Member variables

    private ArrayList<Point> points;

    //Methods
    
    public Track()
    {
        points = new ArrayList<Point>();
        
    }

    public void readFile(String fileName) throws IOException
    {
        this.points= new ArrayList<Point>();

        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        scanner.nextLine();
        while(scanner.hasNextLine())
        {
            String[] line = scanner.nextLine().split(",");
            if(line.length != 4)
            {
                scanner.close();
                throw new GPSException("Invalid CSV arguments.");
            }
            else
            {
                Point point = new Point(ZonedDateTime.parse(line[0]),Double.parseDouble(line[1]),Double.parseDouble(line[2]),Double.parseDouble(line[3]));
                points.add(point);
            }
        }
        scanner.close();
        
    }

    public void add(Point point)
    {
        points.add(point);
    }

    public Point get(int index)
    {
        if(index < 0 || index >= points.size())
        {
            throw new GPSException("Invalid index.");
        }
        else
        {
            return points.get(index);
        }
    }

    public int size()
    {
        return points.size();
    }

    public Point lowestPoint() throws GPSException
    {
        if(points.size() != 0)
        {
            return Collections.min(points, Comparator.comparing(Point::getElevation));
        }
        else
        {
            throw new GPSException("Insufficient number of points");
        }
    }

    public Point highestPoint() throws GPSException
    {
        if(points.size() != 0)
        {
            return Collections.max(points, Comparator.comparing(Point::getElevation));
        }
        else
        {
            throw new GPSException("Insufficient number of points");
        }
    }

    public double totalDistance() throws GPSException
    {
        if(points.size() >= 2)
        {
            double total = 0;
            for(int i = 1; i < points.size(); i++)
            {
                total = total + (Point.greatCircleDistance(points.get(i-1), points.get(i)));
            }
            return total;
        }
        else
        {
            throw new GPSException("Insufficient number of points");
        }
    }

    public double averageSpeed() throws GPSException
    {
        double totalDistance = totalDistance();
        double totalTime = ChronoUnit.SECONDS.between(points.get(0).getTime(),points.get(points.size() - 1).getTime());
        return totalDistance/totalTime;
    }
}
