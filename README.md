# POE Robot Project

This is an POE project to further enrich our skills with using the allowable sensors.

The task chosen is (drumroll please): Use the Bezier curve program to send a spline to the robot and the robot will execute/follow the spline.
#### Deliverables
  - Test plan (how we are going to show the consumer that it works and how to use it)
  - Code library useable
  - Bezier curve program that can send values to the robot
  - Autonomous robot with teleop functionalities
#### Constraints
  - Due by Dec 13
  - Parts available below
  - Code is commented and well documented (jdocs)
  
Challenge: Make the robot follow a curve with any number of control points.

### Test Plan
1. Test that it can go forwards 10 meters in a straight line (margin of error of +/-10cm) (CHECK)
2. Test that it can rotate a full 180 degrees (margin of error +/-5 degrees)
3. Test that it can rotate any number of degrees.
4. Test it with a two point curve, three point curve, and four point curve. 
  - Challenge: Make it work with x# of points.

## Robot
### Parts available to us

 Parts               | Description
 ------------------- | ---
 4 Motors            | Two motors are connected to the left wheels, and two motors are connected to the right wheels 
 2 Encoders          | Connected to the right and left motors
 2 Logitech Cameras  | Can be connected through usb on Pi
 1 Raspberry Pi      | Can analyse images and has 1 Raspberry Camera
 Gyro on RoboRIO     | The gyro on the Robo RIO does not work correctly

## Task List to Pick

1. Make the robot follow a person continously. The person will face the cameras in front of the robot, the robot will then follow the person when he/she moves around while avoiding the surroundings.
2. Make the robot solve a maze. 
3. Make the robot follow a line. <- the chosen one
4. Track target (usually reflective).

## Notes

- Solenoid connected to gear intake can be used to tilt camera.
- Optical sensor
- Create a 3D representation of the area in front of the robot find the target and then create a A* algorithm path.
- We can reuse code from the robot from last year

## More

Go to [this](https://guides.github.com/features/mastering-markdown/) and [this](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet) to read on how to format and add images, lists, code snippet, headers & quotes to a README.md file

To use the Google java formatter in eclipse open eclipse naviagte to Windows -> Preferences -> Java -> Code Style -> Formatter then press import and navigate to the eclipse-java-google-style.xml file included in this repository. 

To format a file in eclipse you can press the Ctrl + Shift + F.

To use IntelliJ you can just add the wpilib [right here](./POE%20Testing%20Environment/build/jars) in the project settings (top right button) as a library named lib.
