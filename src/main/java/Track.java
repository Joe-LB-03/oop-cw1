import java.util.*;
import java.io.*;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Represents a seqeuence of points in space and time, recorded by a GPS sensor.
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

    //Method to parse the dataset as a KML file for use with google maps/earth
    public void writeKML(String fileName)
    {
        try 
        {
            File myFile = new File(fileName);
            if (myFile.createNewFile()) 
            {
                System.out.println("File created: " + myFile.getName());
            }  
            else 
            {
                System.out.println("File already exists.");
            }
        }   catch   (IOException e) 
        {
            System.err.println("An error occurred creating the file.");
            System.exit(0);
        }

        try
        {
            FileWriter myWriter = new FileWriter(fileName);

            myWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            myWriter.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
            myWriter.write("  <Document>\n");
            myWriter.write("    <Style id=\"yellowLineGreenPoly\">\n");
            myWriter.write("      <LineStyle>\n");
            myWriter.write("        <color>7f00ffff</color>\n");
            myWriter.write("        <width>4</width>\n");
            myWriter.write("      </LineStyle>\n");
            myWriter.write("      <PolyStyle>\n");
            myWriter.write("        <color>7f00ff00</color>\n");
            myWriter.write("      </PolyStyle>\n");
            myWriter.write("    </Style>\n");
            myWriter.write("    <Placemark>\n");
            myWriter.write("      <styleUrl>#yellowLineGreenPoly</styleUrl>\n");
            myWriter.write("      <LineString>\n");
            myWriter.write("        <altitudeMode>absolute</altitudeMode>\n");
            myWriter.write("        <coordinates> ");
            for(int i = 0; i < points.size(); i++)
            {
                myWriter.write(String.format("%f,%f,%f\n",points.get(i).getLongitude(),points.get(i).getLatitude(),points.get(i).getElevation()));
            }
            myWriter.write("        </coordinates>\n");
            myWriter.write("      </LineString>\n");
            myWriter.write("    </Placemark>\n");
            myWriter.write("  </Document>\n");
            myWriter.write("</kml>");
            myWriter.close();

        }   catch   (IOException e) 
        {
            System.out.println("An error occurred writing to the file.");
            System.exit(0);
        }
    }
}
