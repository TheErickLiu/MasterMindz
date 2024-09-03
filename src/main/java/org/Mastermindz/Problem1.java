package org.Mastermindz;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.apache.commons.cli.*;

class Problem1 {

    // https://github.com/openpnp/opencv to load the opencv native libraries
    static {
        nu.pattern.OpenCV.loadLocally();
    }

    private static void processImage(String inputPath, String outputPath) {
        Mat inputImage = Imgcodecs.imread(inputPath);
        if (inputImage.empty()) {
            throw new IllegalArgumentException("No arguments provided.");
        }

        Mat image = new Mat();
        Size newSize = new Size(600, 800);
        Imgproc.resize(inputImage, image, newSize, 0, 0, Imgproc.INTER_LINEAR);

        Mat hsvImage = new Mat();
        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);
        // https://stackoverflow.com/questions/10948589/choosing-the-correct-upper-and-lower-hsv-boundaries-for-color-detection-withcv
        Scalar lower = new Scalar(90, 170, 20);
        Scalar upper = new Scalar(150, 255, 255);
        Mat mask = new Mat();
        Core.inRange(hsvImage, lower, upper, mask);

        Mat kernel = Mat.ones(2, 2, CvType.CV_8U);
        Mat erodedImage = new Mat();
        Imgproc.erode(mask, erodedImage, kernel);
        Mat dilatedImage = new Mat();
        Imgproc.dilate(erodedImage, dilatedImage, kernel);
        //Imgproc.morphologyEx(mask, mask, Imgproc.MORPH_OPEN, kernel);
        //Imgproc.morphologyEx(mask, mask, Imgproc.MORPH_CLOSE, kernel);

        Imgcodecs.imwrite(outputPath, dilatedImage);
    }

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("i", "input-image", true, "Input image path, default is src/main/resources/cone.png");
        options.addOption("o", "output-image", true, "Output image path, default is /tmp/masked_cone.png");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            // Parse command line arguments
            cmd = parser.parse(options, args);
            String inputPath = cmd.getOptionValue("i", "src/main/resources/cone.png");
            String outputPath = cmd.getOptionValue("o", "/tmp/masked_cone.png");

            processImage(inputPath, outputPath);
        } catch (org.apache.commons.cli.ParseException e) {
            System.out.println("Error parsing command line arguments");
            formatter.printHelp("Problem1", options);
            System.exit(1);
        }
    }
}