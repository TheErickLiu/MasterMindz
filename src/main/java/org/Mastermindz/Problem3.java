package org.Mastermindz;
import org.apache.commons.cli.*;

import java.util.*;
import java.io.*;

public class Problem3 {
    public static boolean checkIntersection (double x, double y, int[] p1, int[] p2) {

        // get the min and max of the polygon side on x axis and y axis
        double x_min = Math.min(p1[0], p2[0]);
        double x_max = Math.max(p1[0], p2[0]);
        double y_min = Math.min(p1[1], p2[1]);
        double y_max = Math.max(p1[1], p2[1]);

        // special case: (p1, p2) is a horizontal line
        if (y_min == y_max) {
            // true if share same y and between p1.x and p2.x
            return y == y_min && x >= x_min && x <= x_max;
        }

        // special case handling: the point (x, y) shares the same y as one of endpoints,
        // which means (x, y) may intersect with two sides (with the same endpoint),
        // then an offset will make sure it's only counted once.
        if (y == y_min || y == y_max) {
            y += 0.01;
        }

        // case 1: out of range thus no intersection
        if (y < y_min || y > y_max || x > x_max) {
            return false;
        }

        // case 2: to the left of both endpoints, there is intersection
        if (x < x_min) {
            return true;
        }

        // case 3: calculate the x-coordinate of the intersection
        double xIntersection = p1[0] + (y - p1[1]) * (p2[0] - p1[0]) / (p2[1] - p1[1]);
        return x < xIntersection;
    }

    public static boolean pointInPolygon(int x, int y, List<int[]> polygon) {
        int intersections = 0;

        // quick check if it is one of the polygon endpoints
        for (int[] p : polygon) {
            if (x == p[0] && y == p[1]) {
                return true;
            }
        }

        // check by ray-casting
        for (int i = 0; i < polygon.size(); i++) {
            int[] p1 = polygon.get(i);
            int[] p2 = polygon.get((i+1) % polygon.size());
            if (checkIntersection(x, y, p1, p2)) {
                intersections++;
            }
        }

        return ((intersections % 2) != 0);
    }

    public static List<int[]> neighbors(Point p, List<List<int[]>> polygons, int N, int M) {
        List<int[]> goodNeighbors = new ArrayList<>();
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};

        for (int[] d : directions) {
            int x = p.getX() + d[0];
            int y = p.getY() + d[1];
            if (y > 0 && x > 0 && y < N && x < M) {
                boolean isGoodNeighbor = true;
                for (List<int[]> polygon : polygons) {
                    if (pointInPolygon(x, y, polygon)) {
                        isGoodNeighbor = false;
                        break;
                    }
                }
                if (isGoodNeighbor) {
                    goodNeighbors.add(new int[]{x, y});
                }
            }
        }
        return goodNeighbors;
    }

    public static List<int[]> navigate(int[] p1, int[] p2, List<List<int[]>> polygons, int N, int M) {
        PriorityQueue<Point> openPoints = new PriorityQueue<>();
        Map<String, Point> visited = new HashMap<>();
        Point start = new Point(p1[0], p1[1]);
        openPoints.add(start);
        visited.put(p1[0] + "," + p1[1], start);

        while (!openPoints.isEmpty()) {
            Point current = openPoints.poll();
            current.setState(1);

            if (current.getX() == p2[0] && current.getY() == p2[1]) {
                return retraceSteps(current);
            }

            for (int[] n : neighbors(current, polygons, N, M)) {
                String key = n[0] + "," + n[1];
                if (visited.containsKey(key)) {
                    Point p = visited.get(key);
                    if (p.getState() == 1) continue;

                    double offeredGScore = current.getGScore() + Math.hypot(n[0] - current.getX(), n[1] - current.getY());
                    if (offeredGScore < p.getGScore()) {
                        p.setGScore((int) offeredGScore);
                        p.setFScore((int) (offeredGScore + Math.hypot(n[0] - p2[0], n[1] - p2[1])));
                        p.setParent(current);
                        openPoints.remove(p);
                        openPoints.add(p);
                    }
                } else {
                    double gScore = current.getGScore() + Math.hypot(current.getX() - n[0], current.getY() - n[1]);
                    double fScore = gScore + Math.hypot(n[0] - p2[0], n[1] - p2[1]);
                    Point p = new Point(n[0], n[1], (int) fScore, (int) gScore, 0, current);
                    visited.put(key, p);
                    openPoints.add(p);
                }
            }
        }

        return new ArrayList<>();
    }

    public static List<int[]> retraceSteps(Point p) {
        List<int[]> path = new ArrayList<>();
        for (Point current = p; current != null; current = current.getParent()) {
            path.add(new int[]{current.getX(), current.getY()});
        }
        Collections.reverse(path);
        return path;
    }

    public static List<int[]> findPath(List<int[]> waypoints, List<List<int[]>> polygons, int N, int M) {
        List<int[]> finalPath = new ArrayList<>();

        for (int i = 0; i < waypoints.size() - 1; i++) {
            List<int[]> pathSegment = navigate(waypoints.get(i), waypoints.get(i + 1), polygons, N, M);
            finalPath.addAll(i == 0 ? pathSegment : pathSegment.subList(1, pathSegment.size()));
        }

        return finalPath;
    }

    public static void navigatePath(String input, String output) {
        int N = 0, M = 0;
        List<int[]> waypoints = new ArrayList<>();
        List<List<int[]>> polygons = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String[] params = reader.readLine().trim().split(" ");
            N = Integer.parseInt(params[0]);
            M = Integer.parseInt(params[1]);

            int numWaypoints = Integer.parseInt(params[2]);
            if (numWaypoints == 0){
                System.out.println("No waypoints given");
                return;
            }

            if (numWaypoints == 1) {
                System.out.println("Only one waypoint, already at destination");
                return;
            }

            for (int i = 0; i < numWaypoints; i++) {
                waypoints.add(Arrays.stream(reader.readLine().trim().split(" "))
                        .mapToInt(Integer::parseInt).toArray());
            }

            for (int i = 0, numPolygons = Integer.parseInt(params[3]); i < numPolygons; i++) {
                List<int[]> vertices = new ArrayList<>();
                for (int j = 0, numVertices = Integer.parseInt(reader.readLine()); j < numVertices; j++) {
                    vertices.add(Arrays.stream(reader.readLine().trim().split(" "))
                            .mapToInt(Integer::parseInt).toArray());
                }
                polygons.add(vertices);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<int[]> path = findPath(waypoints, polygons, N, M);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
            for (int[] point : path) {
                writer.write(point[0] + " " + point[1]);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("i", "input", true, "Input path, default is src/main/resources/navigate.in");
        options.addOption("o", "output", true, "Output path, default is /tmp/navigate.out");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            String inputPath = cmd.getOptionValue("i", "src/main/resources/navigate.in");
            String outputPath = cmd.getOptionValue("o", "/tmp/navigate.out");

            navigatePath(inputPath, outputPath);
        } catch (org.apache.commons.cli.ParseException e) {
            System.out.println("Error parsing command line arguments");
            formatter.printHelp("Problem3", options);
            System.exit(1);
        }
    }
}