import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.io.*;

/**
 * JavaFX application to plot elevations of a GPS track, for
 * the Advanced task of COMP1721 Coursework 1.
 *
 * @author Louis
 */
public class PlotApplication extends Application 
{
    protected static Track track;

    @Override public void start(Stage stage) 
    {
        stage.setTitle("Elevation Plot");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Point");
        yAxis.setLabel("Elevation");
        //creating the chart
        final LineChart<Number,Number> lineChart =  new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setTitle("Elevation as a function of distance.");
        //defining a series
        XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
        series.setName("Datapoints");
        //populating the series with data
        double distance = 0;
        for(int i = 0; i < track.size(); i++)
        {
            series.getData().add(new XYChart.Data<Number,Number>(distance, track.get(i).getElevation()));
            if(i != track.size() - 1)
            {
                distance = distance + Point.greatCircleDistance(track.get(i), track.get(i+1));
            }
        }
        
        
        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);
       
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) 
    {
        if(args.length == 0)
        {
            System.err.println("Please provide a valid filename");
            System.exit(0);
        }

        track = new Track();

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

        PlotApplication.launch();
    }
}
