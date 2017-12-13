package org.usfirst.frc.team2974.robot.motion;

import java.util.Arrays;
import org.usfirst.frc.team2974.robot.util.MotionProvider;
import org.usfirst.frc.team2974.robot.util.Pose;

/**
 * Class meant to be used with the BezierCurve Program
 */
public class MotionPathFollower extends MotionProvider {

    private final double initialTheta; // start angle
    private final double finalTheta; // end angle
    private final boolean isForwards;
    private final Pose[] robotPoses; // the positions the robot has to go through


    /**
     * Constructs MotionPathSpline.
     *
     * @param vCruise velocity to cruise at (velocity to try to reach and stay at as long as possible)
     * @param aMax acceleration/deceleration
     * @param isForwards if the robot is facing forwards or not
     * @param robotPoses the positions the robot has to go through
     */
    public MotionPathFollower(double vCruise, double aMax, boolean isForwards, Pose... robotPoses) {
        super(vCruise, aMax, robotPoses.length);

        this.robotPoses = robotPoses;
        this.isForwards = isForwards;

        double length = 0;
        if (robotPoses.length > 1) {
            for (int i = 0; i < robotPoses.length - 1; i++) {
                length += robotPoses[i].point.distance(robotPoses[i + 1].point);
            }

            initialTheta = robotPoses[0].angle;
            finalTheta = robotPoses[robotPoses.length - 1].angle;
        } else {
            initialTheta = 0;
            finalTheta = 0;
        }

        setLength(isForwards ? length : -length);
    }


    @Override
    public final Pose evaluatePose(double s) {
        // interpolate between poses s% and s% + 1
        int index = (int) (s * (robotPoses.length - 1));
        if(index + 1 >= robotPoses.length)
            return robotPoses[index];

        Pose last = robotPoses[index];
        Pose next = robotPoses[index + 1];

        double localPercent = (s - index / (robotPoses.length - 1)) / (1 / robotPoses.length);

        return Pose.interpolate(last, 1 - localPercent, next, localPercent);
    }

    @Override
    public final LimitMode getLimitMode() {
        return LimitMode.LimitLinearAcceleration;
    }

    @Override
    public final double getInitialTheta() {
        return initialTheta;
    }

    @Override
    public final double getFinalTheta() {
        return finalTheta;
    }

    @Override
    public String toString() {
        return String.format(
            "MotionPathFollower{initialTheta=%f, finalTheta=%f, isForwards=%s, robotPoses=%s}"
            , initialTheta
            , finalTheta
            , isForwards
            , Arrays.toString(robotPoses)
        );
    }
}
