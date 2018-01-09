package org.usfirst.frc.team2974.robot.command.auton;

import static org.usfirst.frc.team2974.robot.Robot.driveTrain;

import edu.wpi.first.wpilibj.command.Command;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.usfirst.frc.team2974.robot.motion.MotionPathFollower;
import org.usfirst.frc.team2974.robot.util.MotionProvider;
import org.usfirst.frc.team2974.robot.util.PointRetriever;
import org.usfirst.frc.team2974.robot.util.Pose;

public class PathFollowerCommand extends Command {

    private String[] smartDashboardPaths;
    private MotionProvider pathFollower; // the motion that will be executed
    private double vCruise; // the velocity of the robot to go at
    private double aMax; // the constant acceleration/deceleration to use
    private boolean isForwards; // whether the robot is facing forwards or not when executing the spline
    private Pose[] poses; // the robot path points that the robot has to pass through


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
        this(vCruise, aMax, isForwards, new Pose[0]);                   // converts the stream into an array
        this.smartDashboardPaths = smartDashboardPaths;
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


        requires(driveTrain); // tells the command that it will use the drivetrain subsystem
    }


    private boolean run = false;

    /**
     * creates and starts the motion
     */
    @Override
    protected void initialize() {
        findAndRun();
    }

    private void findAndRun()
    {

        try {
            poses =
                    Arrays.stream(smartDashboardPaths)              // loops through smartdashboard keys
                            .map(
                                    PointRetriever::retrievePoses)         // uses the retrievePoses to convert the key into a Pose[]
                            .reduce(
                                    new ArrayList<>(), (poses23, poses2) -> {   //
                                        Collections
                                                .addAll(poses23, poses2);    // adds elements of one array to a list
                                        return poses23;
                                    },
                                    (poses22, poses2) -> {                   // joins the lists created by the stream into one
                                        poses22.addAll(poses2);
                                        return poses22;
                                    }).toArray(new Pose[0]);
            this.pathFollower = new MotionPathFollower(vCruise, aMax, isForwards, poses);

            driveTrain.addControllerMotion(pathFollower);
            driveTrain.startMotion();


            run = true;
        }catch (Exception e)
        {
          e.printStackTrace();
            run = false;
        }
    }

    @Override
    protected void execute() {
      System.out.println(run);
        if (!run)
        findAndRun();
    }

    /**
     * Checks if the motion is finished if the motion is finished then the command will end
     *
     * @return true if motion has finished false otherwise
     */
    @Override
    protected boolean isFinished() {
        return run && (driveTrain.isControllerFinished() || (
            driveTrain.getCurrentMotion().equals(pathFollower) && driveTrain
                .isCurrentMotionFinished()));
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
