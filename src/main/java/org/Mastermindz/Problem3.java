package org.Mastermindz;
import org.apache.commons.cli.*;

import java.util.*;
import java.io.*;

public class Problem3 {
    public static Set<String> findObstacles(List<List<int[]>> polygons) {
        Set<String> obstacles = new HashSet<>();

        for (List<int[]> polygon : polygons) {
            Map<Integer, List<Integer>> yXList = new HashMap<>();
            for (int[] point : polygon) {
                yXList.computeIfAbsent(point[1], k -> new ArrayList<>()).add(point[0]);
            }

            for (int y = Collections.min(yXList.keySet()); y <= Collections.max(yXList.keySet()); y++) {
                if (yXList.containsKey(y)) {
                    int xMin = Collections.min(yXList.get(y));
                    int xMax = Collections.max(yXList.get(y));
                    for (int x = xMin; x <= xMax; x++) {
                        obstacles.add(x + "," + y);
                    }
                }
            }
        }
        return obstacles;
    }

    public static List<int[]> neighbors(Point p, Set<String> obstacles, int N, int M) {
        List<int[]> goodNeighbors = new ArrayList<>();
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};

        for (int[] d : directions) {
            int x = p.getX() + d[0];
            int y = p.getY() + d[1];
            if (y > 0 && x > 0 && y < N && x < M && !obstacles.contains(x + "," + y)) {
                goodNeighbors.add(new int[]{x, y});
            }
        }

        return goodNeighbors;
    }

    public static List<int[]> navigate(int[] p1, int[] p2, Set<String> obstacles, int N, int M) {
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

            for (int[] n : neighbors(current, obstacles, N, M)) {
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
        Set<String> obstacles = findObstacles(polygons);
        List<int[]> finalPath = new ArrayList<>();

        for (int i = 0; i < waypoints.size() - 1; i++) {
            List<int[]> pathSegment = navigate(waypoints.get(i), waypoints.get(i + 1), obstacles, N, M);
            finalPath.addAll(i == 0 ? pathSegment : pathSegment.subList(1, pathSegment.size()));
        }

        return finalPath;
    }

    public static void navigatePath(String input, String output) {
        int N = 0, M = 0;
        List<int[]> waypoints = new ArrayList<>();
        List<List<int[]>> polygons = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(input)))) {
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
            // Parse command line arguments
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