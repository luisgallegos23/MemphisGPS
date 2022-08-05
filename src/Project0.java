import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class Project0 {
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a filename: ");
        String filename = scan.nextLine();
        RoadNetwork graph = readGraph(filename);
        scan.close();

        while (true) {
            System.out.print("\nEnter a location ID or 0 to quit: ");
            long locId = Integer.parseInt(scan.nextLine());

            if (locId == 0) break;

            Location loc = graph.getLocationForId(locId);
            List<Road> roads = graph.getAdjacentRoads(locId);
            for (Road road : roads) {
                Location loc2 = graph.getLocationForId(road.endId());
                double dist = Geometry.getDistanceInMiles(road, graph);
                double timeSec = Geometry.getDriveTimeInSeconds(road, graph);
                System.out.println("    Location " + road.endId() + ", " + road.speedLimit() + " mph, "
                    + road.name() + ", " + timeSec + " seconds");
            }
        }

    }

    public static RoadNetwork readGraph(String filename)
    {
        InputStream is = Project0.class.getResourceAsStream(filename);
        if (is == null) {
            System.err.println("Bad filename: " + filename);
            System.exit(1);
        }
        Scanner scan = new Scanner(is);

        RoadNetwork graph = new RoadNetwork();

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] pieces = line.split("\\|");

            if (pieces[0].equals("location")) {
                long id = Long.parseLong(pieces[1]);
                double lat = Double.parseDouble(pieces[2]);
                double longi = Double.parseDouble(pieces[3]);
                Location loc = new Location(id, lat, longi);
                graph.addLocation(loc);
            } else if (pieces[0].equals("road")) {
                long startId = Long.parseLong(pieces[1]);
                long endId = Long.parseLong(pieces[2]);
                int speed = Integer.parseInt(pieces[3]);
                String name = pieces[4];
                Road r1 = new Road(startId, endId, speed, name);
                Road r2 = new Road(endId, startId, speed, name);
                graph.addRoad(r1);
                graph.addRoad(r2);
            }
        }
        scan.close();

        return graph;
    }
}
