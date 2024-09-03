package org.Mastermindz;

import org.apache.commons.cli.*;

import java.util.*;
import java.io.*;
import java.awt.geom.Point2D;

public class Problem2 {
    private static void align(String alignIn, String alignOut) {
        ArrayList<Point2D.Double> poles = new ArrayList<>();
        Point2D.Double position = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(alignIn))) {
            String[] params = reader.readLine().trim().split(" ");
            int poleCount = Integer.parseInt(params[0]);

            for (int i = 0; i < poleCount; i++) {
                String[] newPole = reader.readLine().trim().split(" ");
                double x = Double.parseDouble(newPole[0]);
                double y = Double.parseDouble(newPole[1]);
                poles.add(new Point2D.Double(x, y));
            }

            String[] pos = reader.readLine().trim().split(" ");
            double posX = Double.parseDouble(pos[0]);
            double posY = Double.parseDouble(pos[1]);
            position = new Point2D.Double(posX, posY);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int minPoleIndex = Utils.closestPoleIndex(poles, position);
        Point2D.Double closestPole = poles.get(minPoleIndex);
        double angle = Utils.calculateAngle(position, closestPole);
        double dist = position.distance(closestPole);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(alignOut))) {
            writer.write(Math.round(angle * 10.0) / 10.0 + " " + Math.round(dist * 10.0) / 10.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("i", "input-data", true, "Input data path, default is src/main/resources/align.in");
        options.addOption("o", "output-data", true, "Output data path, default is /tmp/align.out");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            // Parse command line arguments
            cmd = parser.parse(options, args);
            String inputPath = cmd.getOptionValue("i", "src/main/resources/align.in");
            String outputPath = cmd.getOptionValue("o", "/tmp/align.out");

            align(inputPath, outputPath);
        } catch (org.apache.commons.cli.ParseException e) {
            System.out.println("Error parsing command line arguments");
            formatter.printHelp("Problem2", options);
            System.exit(1);
        }
    }
}
