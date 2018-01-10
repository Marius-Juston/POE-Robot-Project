package org.usfirst.frc.team2974.robot.command.auton;

import static org.usfirst.frc.team2974.robot.Robot.driveTrain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import edu.wpi.first.wpilibj.command.Command;

import java.util.Arrays;

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


    private boolean run = false;

    /**
     * creates and starts the motion
     */
    @Override
    public void start() {
      if (usesSmartDashboard) {
        List<Pose> poses = new ArrayList<>();

        for (String key : smartDashboardPaths) {
          try {
            Pose[] pathPoses = PointRetriever.retrievePoses(key);

            Collections.addAll(poses, pathPoses);
          } catch (NumberFormatException e) {
            e.printStackTrace();
          }
        }

        System.out.println(poses);

        this.pathFollower = new MotionPathFollower(vCruise, aMax, isForwards,
            poses.toArray(new Pose[0]));

        driveTrain.addControllerMotion(pathFollower);
        driveTrain.startMotion();
      } else {
        this.pathFollower = new MotionPathFollower(vCruise, aMax, isForwards, poses);

        driveTrain.addControllerMotion(pathFollower);
        driveTrain.startMotion();
      }
    }

    protected void initialize() {
        findAndRun();
    }

    private void findAndRun()
    {

        try {
          if(poses == null)
            poses =
//                    Arrays.stream(smartDashboardPaths)              // loops through smartdashboard keys
//                            .map(
//                                    PointRetriever::retrievePoses)         // uses the retrievePoses to convert the key into a Pose[]
//                            .reduce(
//                                    new ArrayList<>(), (poses23, poses2) -> {   //
//                                        Collections
//                                                .addAll(poses23, poses2);    // adds elements of one array to a list
//                                        return poses23;
//                                    },
//                                    (poses22, poses2) -> {                   // joins the lists created by the stream into one
//                                        poses22.addAll(poses2);
//                                        return poses22;
//                                    }).toArray(new Pose[0]);

                PointRetriever.retrievePoses("0.0 0.0 0.0 0.04039983340274885 4.1649312786339016E-4 0.041666666666666664 0.07996668054977092 0.0016659725114535606 0.08510638297872342 0.11870054144106623 0.003748438150770512 0.13043478260869565 0.15660141607663472 0.006663890045814243 0.17777777777777776 0.19366930445647645 0.010412328196584757 0.2272727272727273 0.2299042065805914 0.014993752603082049 0.2790697674418605 0.2653061224489796 0.020408163265306124 0.3333333333333332 0.299875052061641 0.02665556018325697 0.3902439024390243 0.33361099541857564 0.03373594335693461 0.45 0.3665139525197834 0.04164931278633903 0.5128205128205129 0.39858392336526444 0.05039566847147023 0.5789473684210527 0.42982090795501876 0.059975010412328195 0.6486486486486487 0.4602249062890463 0.07038733860891296 0.7222222222222224 0.4897959183673469 0.0816326530612245 0.7999999999999999 0.5185339441899208 0.0937109537692628 0.8823529411764707 0.546438983756768 0.10662224073302788 0.9696969696969697 0.5735110370678884 0.12036651395251978 1.0625 0.599750104123282 0.13494377342773844 1.161290322580645 0.6251561849229488 0.15035401915868388 1.2666666666666664 0.6497292794668889 0.16659725114535612 1.3793103448275865 0.673469387755102 0.18367346938775508 1.5 0.6963765097875885 0.20158267388588091 1.6296296296296298 0.718450645564348 0.22032486463973344 1.7692307692307698 0.7396917950853811 0.23990004164931278 1.92 0.7600999583506872 0.2603082049146189 2.0833333333333335 0.7796751353602666 0.2815493544356518 2.2608695652173916 0.798417326114119 0.3036234902124114 2.4545454545454533 0.8163265306122449 0.326530612244898 2.6666666666666656 0.8334027488546439 0.35027072053311126 2.9 0.849645980841316 0.3748438150770512 3.1578947368421044 0.8650562265722616 0.40024989587671805 3.444444444444445 0.87963348604748 0.4264889629321115 3.7647058823529402 0.893377759266972 0.453561016243232 4.125 0.9062890462307371 0.4814660558100791 4.533333333333333 0.9183673469387756 0.5102040816326531 5.0 0.929612661391087 0.5397750937109538 5.53846153846154 0.9400249895876718 0.5701790920449812 6.166666666666665 0.9496043315285297 0.6014160766347355 6.909090909090907 0.958350687213661 0.6334860474802165 7.8 0.9662640566430654 0.6663890045814245 8.888888888888891 0.9733444398167431 0.7001249479383591 10.250000000000004 0.979591836734694 0.7346938775510203 11.999999999999995 0.9850062473969179 0.7700957934194085 14.333333333333332 0.9895876718034153 0.8063306955435237 17.599999999999998 0.9933361099541859 0.8433985839233654 22.500000000000004 0.9962515618492295 0.8812994585589338 30.66666666666669 0.9983340274885465 0.920033319450229 46.99999999999995 0.9995835068721367 0.9596001665972511 95.9999999999999 1.0 1.0 1.8 ");
            
          System.out.println(Arrays.toString(poses));
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
//      System.out.println(run);
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
