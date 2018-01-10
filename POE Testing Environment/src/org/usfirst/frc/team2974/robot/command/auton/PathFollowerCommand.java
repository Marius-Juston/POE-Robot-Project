package org.usfirst.frc.team2974.robot.command.auton;

import static org.usfirst.frc.team2974.robot.Robot.driveTrain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.usfirst.frc.team2974.robot.motion.MotionPathFollower;
import org.usfirst.frc.team2974.robot.util.MotionProvider;
import org.usfirst.frc.team2974.robot.util.PointRetriever;
import org.usfirst.frc.team2974.robot.util.Pose;

import edu.wpi.first.wpilibj.command.Command;

public class PathFollowerCommand extends Command {

    private MotionProvider pathFollower; // the motion that will be executed
    private double vCruise; // the velocity of the robot to go at
    private double aMax; // the constant acceleration/deceleration to use
    private boolean isForwards; // whether the robot is facing forwards or not when executing the spline
  private final String[] smartDashboardPaths;
  private Pose[] poses; // the robot path points that the robot has to pass through
    private final boolean usesSmartDashboard;

    /**
     * Instantiates the instance variables
     *
     * @param vCruise the velocity to try to reach and stay at the longest
     * @param aMax the acceleration/deceleration of the robot
     * @param isForwards whatever the path is executed facing forwards or backwards
     * @param smartDashboardPaths the SmartDashboard string keys where the paths should be
     * retrieved from. If there is more than one path (more than one key)
     * if will append all the poses together.
     */
    public PathFollowerCommand(double vCruise, double aMax, boolean isForwards,
        String... smartDashboardPaths) {
    	this.vCruise = vCruise;
    	this.aMax = aMax;
    	this.isForwards = isForwards;
      this.smartDashboardPaths = smartDashboardPaths;

      this.usesSmartDashboard = true;
    
    	
    	
    	requires(driveTrain);
    }

    /**
     * Instantiates the instance variables
     *
     * @param vCruise the velocity to try to reach and stay at the longest
     * @param aMax the acceleration/deceleration of the robot
     * @param isForwards whetever the path is executed facing forwards or backwards
     * @param poses the path points the robot has to go through
     */
    public PathFollowerCommand(double vCruise, double aMax, boolean isForwards, Pose... poses) {
        this.vCruise = vCruise;
        this.aMax = aMax;
        this.isForwards = isForwards;
        this.poses = poses;
        
        usesSmartDashboard = false;
        smartDashboardPaths = new String[0];

        requires(driveTrain); // tells the command that it will use the drivetrain subsystem
    }

    /**
     * creates and starts the motion
     */
    @Override
    public void start() {
    	if(usesSmartDashboard) {
    		List<Pose> poses = new ArrayList<>();
    		
    		for(String key: smartDashboardPaths)
    		{
    			try {
    		  Pose[] pathPoses = PointRetriever.retrievePoses(key);

          Collections.addAll(poses, pathPoses);
    			}catch(NumberFormatException e)
    			{
    				e.printStackTrace();
    			}
    		}

        System.out.println(poses);

        this.pathFollower = new MotionPathFollower(vCruise, aMax, isForwards, poses.toArray(new Pose[0]));

        driveTrain.addControllerMotion(pathFollower);
        driveTrain.startMotion();
    	}else {
        this.pathFollower = new MotionPathFollower(vCruise, aMax, isForwards, poses);

        driveTrain.addControllerMotion(pathFollower);
        driveTrain.startMotion();
    	}

      System.out.println("hellooooooooooooooooooooo");
    }

    /**
     * Checks if the motion is finished if the motion is finished then the command will end
     *
     * @return true if motion has finished false otherwise
     */
    @Override
    protected boolean isFinished() {
        return driveTrain.isControllerFinished() || (
            driveTrain.getCurrentMotion().equals(pathFollower) && driveTrain
                .isCurrentMotionFinished());
    }

    @Override
    public String toString() {
        return String.format(
            "PathFollowerCommand{pathFollower=%s, vCruise=%f, aMax=%f, isForwards=%s, poses=%s}"
            , pathFollower
            , vCruise
            , aMax
            , isForwards
            , Arrays.toString(poses)
        );
    }
}
