# Erick Liu - MasterMindz Software Entrance Assignment

## Part One - Research

### What is PIDF? And what is its purpose for robotics? And how does it differ from the PID control loop?

In [FTClib](https://github.com/FTCLib/FTCLib), the [PIDFController](https://github.com/FTCLib/FTCLib/blob/master/core/src/main/java/com/arcrobotics/ftclib/controller/PIDFController.java) is a control loop mechanism that uses feedback and feedforward to control motors for accurate and smooth movements. The [PIDController](https://github.com/FTCLib/FTCLib/blob/master/core/src/main/java/com/arcrobotics/ftclib/controller/PIDController.java) only reacts to error and adjusts the motor control based on the P, I and D terms. In fact, the `PIDController` is a subclass of the `PIDFController` with `kf` being 0. The feedforward (F) term proactively applies a predicted amount of control without waiting for the error to develop, thereby reducing the error before it occurs. The `PIDFController` compensate variables to maintain a desired output, It is expected to improve system response time, precision and stable control.

#### How can we define a PIDF controller in FTCLib?
As shown in the [FTClib controllers documentation](https://docs.ftclib.org/ftclib/features/controllers), a `PIDFController` object can created with the [PIDFController](https://github.com/FTCLib/FTCLib/blob/master/core/src/main/java/com/arcrobotics/ftclib/controller/PIDFController.java) class and used control loop.
```
PIDFController pidf = new PIDFController(kP, kI, kD, kF);
while (!pidf.atSetPoint()) {
    double output = pidf.calculate(motor.getCurrentPosition());
    // Updating motor velocity is one example
    // motor.setVelocity(output);
}
```
A customized PIDF controller, for [example](https://www.youtube.com/watch?v=E6H6Nqe6qJo), can be built on top of the [PIDController](https://github.com/FTCLib/FTCLib/blob/master/core/src/main/java/com/arcrobotics/ftclib/controller/PIDController.java) by adding the feedforward amount to the calculated PID control.

It is important to use the FTCDashboard and [telemetry](https://acmerobotics.github.io/ftc-dashboard/javadoc/com/acmerobotics/dashboard/telemetry/MultipleTelemetry.html) to fine tune the P, I, D and F component. 

- **What does `kP` represent?**

Proportional. It produces an output proportional to the current error (the difference between the desired and actual output). It reacts to how far the system is from the desired state.

- **What does `kI` represent?**

Integral. It integrates all the past errors and compensates for any leftover error that the proportional term alone cannot fix.

- **What does `kD` represent?**

Derivative. It predicts the future error by considering the derivative of change. This creates a damping effect that helps to reduce overshoot.

- **What does `kF` represent?**
Feedforward. It adds an immediate offset to the control needed to reach the desired state, without waiting for feedback (i.e. error) from the system to occur.

- **And how does changing these values change the control loop?** 
In each iteration of the control loop, the PIDF controller calculates the control output to reduce the error between the current and target state. Proper tuning of the PIDF components ensures the error decreases with each iteration, bringing the system closer to the target state.

### What are finite state machines? And what is its purpose for robotics?
Both [gm0](https://gm0.org/en/latest/docs/software/concepts/finite-state-machines.html) and [wikipedia](https://en.wikipedia.org/wiki/Finite-state_machine) have a good explanation of FSM that consists of a set of predefined states (including a start state), a set of possible inputs to the FSM and a transition function that maps one state to another state.

FSMs are a powerful tool to structure and manage the action of robots in FTC to help them perform complex tasks in a organized manner.
* FSMs help manage the sequence of actions by defining states, actions and the conditions under which the robot should transit from one state to another, resulting a predictable control flow.
* FSMs separates complex robot behaviors into states and actions. It makes development, debugging and maintenance easier.

### What is Roadrunner? And what is its purpose for robotics?
[RoadRunner](https://github.com/acmerobotics/road-runner) is a Kotlin library for planning 2D mobile robot paths and trajectories for FTC.

#### What are dead wheels? And what is its purpose?
From [RoadRunner FAQ](https://learnroadrunner.com/introduction.html#what-are-dead-wheels-odometry): Dead wheels are unpowered [omni](https://gm0.org/en/latest/docs/common-mechanisms/drivetrains/index.html#term-Omni-Wheel) (directional) wheel that tracks the distance the robot has traveled through the encoder attached to the wheel’s axle. Dead wheels experience very little slip compared to [mecanum](https://gm0.org/en/latest/docs/common-mechanisms/drivetrains/holonomic.html#term-Mecanum-Wheel) wheels, which improves accuracy during high acceleration.

#### How can we create a trajectory using Roadrunner?
https://learnroadrunner.com/trajectories.html presents an example to create a trajectory based on RoadRunner v0.5.6. The [repo](https://github.com/FTCLib/RoadRunner-FTCLib-Quickstart) was archived and RoadRunner has been updated to v1.0.0. But the way to [create](https://github.com/FTCLib/RoadRunner-FTCLib-Quickstart/blob/main/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/drive/SampleMecanumDrive.java#L172) a `TrajectoryBuilder` and use `TrajectoryBuilder` [methods](https://rr.brott.dev/docs/v1-0-0/core/javadoc/com/acmerobotics/roadrunner/TrajectoryBuilder.html) to configure the trajectory do not change, for example
```
new TrajectoryBuilder(new Pose2d())
  .lineToLinearHeading(new Pose2d(40, 40, Math.toRadians(90)))
  .build()
```

#### For all possible trajectory builder functions, what do they take as parameters and how do they influence the trajectory?
https://learnroadrunner.com/trajectorybuilder-functions.html has an excellent animation of `TrajectoryBuilder` function parameters and motions.

### What is OpenCV? And how is it used in Java programming
OpenCV is an open source computer vision / machine learning library used for image processing & video analysis. It can be used for tasks such as object detection, facial recognition, motion tracking, and image manipulation.

I have tried two ways for opencv in Java:
* [build opencv](https://docs.opencv.org/4.x/d9/d52/tutorial_java_dev_intro.html), add `build/bin/opencv-4110.jar` to Intellij as a dependency and finally add `build/lib/libopencv_java4100.dylib` to `-Djava.library.path`.
* It is more convenient to use the prebuilt [openpnp/opencv](https://github.com/openpnp/opencv) or [JavaCV](https://github.com/bytedeco/javacv) by including the dependency in maven. One caveat is that `System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME)` needs to be replaced by `nu.pattern.OpenCV.loadShared()` with `openpnp/opencv` to load the native library.

#### What are some common image-processing techniques that can be implemented using OpenCV in Java? And what are their purposes in robotics?
Some common image-processing techniques include
* Contour detection: find and analyze shapes, `Imgproc.findContours`
* Edge detection: navigate and obstacle avoidance, `Imgproc.Canny`
* Image thresholding: simply image data for further processing, `Imgproc.threshold`
* Object detection: use pretrained classifiers to detect objects, `CascadeClassifier`

#### What is ImgProc for OpenCV Java?
The [ImgProc](https://docs.opencv.org/4.x/javadoc/org/opencv/imgproc/Imgproc.html) class provides a comprehensive set of functions for image processing, including filtering, thresholding, edge detection, contour detection, image transformation. These functions are essential for analyzing and manipulating images in various CV applications, including robotics.

## Part Two - Pseudocode

### Problem #1
```
1. Load image
2. Resize image
3. Convert RGB color to HSV
4. Define lower and upper values for blue
5. Make mask / filter out other colors
6. Init kernel
7. Erode then dilate the image to remove noise
```

### Problem #2
```
1. Iterate through all poles and find the one with min distance to position (pythagorean theorm)
2. Calculate the align angle
2.1. Find angle between pole and position relative to the positive x-axis.
2.2. Flip the angle to relative to the positive y-axis.
2.3. Normalize angle to between -180 and 180.
3. return angle and distance
```

### Problem #3
```
1. Split up path to waypoint-to-waypoint segments
2. Use A* algorithm (greedy) on each segment
2.1. Create min heap and a map to track visited points
2.2. Add the starting point to the heap and mark as visited
2.3. Loop:
2.3.1. Pop the point with the lowest estimated distance to end from the heap
2.3.2. If the current point is the destination, return path
2.3.3. For each good neighbor (found with the raycast algorithm) of the current point, If the neighbor is already visited, check if a shorter path exists and update
If not visited, calculate its distance to end and distance from start, then add it to the queue and mark it as visited.
2.4 If the destination is reached, find the parent of each point to construct the path; otherwise, return an empty list if no path is found.
3. Combine segments into the final path
```
## Part 3 - Code

### Problem 1
```
usage: Problem1
 -i,--input-image <arg>    Input image path, default is src/main/resources/cone.png
 -o,--output-image <arg>   Output image path, default is /tmp/masked_cone.png
```

### Problem 2
```
usage: Problem2
 -i,--input-data <arg>    Input data path, default is src/main/resources/align_in
 -o,--output-data <arg>   Output data path, default is /tmp/align_out
```
`calculateAngle` is unit tested with eight cases in `org.Mastermindz.Problem2Test`.

### Problem 3
```
usage: Problem3
-i,--input <arg>    Input path, default is src/main/resources/navigate.in
-o,--output <arg>   Output path, default is /tmp/navigate.out
```
Core of the implementation is the ray-casting algorithm and the A* algorithm.
`checkIntersection` is unit tested with four cases in `org.Mastermindz.Problem3Test`.