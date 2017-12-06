package org.usfirst.frc.team2974.robot.motion;

import org.usfirst.frc.team2974.robot.util.MotionProvider;
import org.usfirst.frc.team2974.robot.util.Pose;

/**
 * JUST ANGLES, TURNING
 */
public class MotionPathTurn extends MotionProvider {

    private final Pose pose0;
    private final Pose pose1;

    /**
     * Constructs MotionPathTurn.
     *
     * @param pose0 initial pose
     * @param dAngle amount to change angle by, in radians
     * @param vCruise cruise velocity
     * @param rotationMaxAcceleration max rotational acceleration/deceleration
     */
    public MotionPathTurn(Pose pose0, double dAngle, double vCruise,
        double rotationMaxAcceleration) {
        super(vCruise, rotationMaxAcceleration);

        this.pose0 = pose0;
        pose1 = new Pose(pose0.point, pose0.angle + MotionProvider.boundAngle(dAngle));
    }

    @Override
    public final Pose evaluatePose(final double s) {
        final double r = 1.0 - s;
        return new Pose(this.pose0.point, (r * this.pose0.angle) + (s * this.pose1.angle));
    }

    @Override
    public final MotionProvider.LimitMode getLimitMode() {
        return MotionProvider.LimitMode.LimitRotationalAcceleration;
    }

    @Override
    public final double getLength() {
        return 0;
    }

    @Override
    public final double getInitialTheta() {
        return this.pose0.angle;
    }

    @Override
    public final double getFinalTheta() {
        return this.pose1.angle;
    }


}
