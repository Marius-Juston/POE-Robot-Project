package org.usfirst.frc.team2974.robot.kinematic;

import static org.usfirst.frc.team2974.robot.RobotConfiguration.ROBOT_WIDTH;

import org.usfirst.frc.team2974.robot.util.MotionProvider;
import org.usfirst.frc.team2974.robot.util.Pose;
import org.usfirst.frc.team2974.robot.util.RobotPair;

public class Kinematics {

    private final MotionProvider motion;
    private final double v0;
    private final double v1;
    private final int nPoints;
    private KinematicPose lastPose;
    private KinematicPose nextPose;
    private double s; // percent done
    private double l0;

    /**
     * Constructs Kinematics.
     *
     * @param motion the motion provider being used
     * @param wheelPosition the wheel position at time t
     * @param t time
     * @param v0 the initial velocity for the motion
     * @param v1 the final velocity for the motion
     * @param nPoints number of points to put on curve, FIXME: (I think) i.e. greater is more accurate
     */
    public Kinematics(MotionProvider motion, RobotPair wheelPosition, double t, double v0,
        double v1, int nPoints) {
        this.motion = motion;
        s = 0.0;
        this.v0 = v0;
        this.v1 = v1;
        this.nPoints = nPoints;

        evaluateFirstPose(wheelPosition, t);
        evaluateNextPose(1.0 / nPoints);
    }

    public Kinematics(MotionProvider motion, RobotPair wheelPosition, double t, double v0,
        double v1) {
        this(motion, wheelPosition, t, v0, v1, motion.getNumberOfPoints());
    }

    /**
     * Constructs a static kinematic pose. i.e. KinematicPose with only distance
     *
     * @param pose pose to make into kinematic pose
     * @param wheelPosition the wheel positions of the robot
     * @param t time
     * @return static KinematicPose
     */
    public static KinematicPose staticPose(Pose pose, RobotPair wheelPosition, double t) {
        KinematicState left = new KinematicState(wheelPosition.left, 0, 0);
        KinematicState right = new KinematicState(wheelPosition.right, 0, 0);

        return new KinematicPose(pose, left, right, t, false);
    }

    /**
     * Evaluates the first pose of this motion.
     *
     * @param wheelPosition the wheel positions of the robot
     * @param t time
     */
    private void evaluateFirstPose(RobotPair wheelPosition, double t) {
        Pose pose = motion.evaluatePose(0);
        KinematicState left = new KinematicState(wheelPosition.left, v0, 0);
        KinematicState right = new KinematicState(wheelPosition.right, v0, 0);

        lastPose = nextPose = new KinematicPose(pose, left, right, t, false);
        l0 = lastPose.getCenterLength();
    }

    /**
     * Evaluates the last pose of this motion.
     */
    private void evaluateLastPose() {
        Pose pose = motion.evaluatePose(1.0);
        KinematicState left = new KinematicState(lastPose.left.length, v1, 0);
        KinematicState right = new KinematicState(lastPose.right.length, v1, 0);

        lastPose = nextPose = new KinematicPose(pose, left, right, lastPose.t, true);
    }

    /**
     * Evaluates the next pose of this motion.
     *
     * @param ds change in percent
     */
    private void evaluateNextPose(final double ds) {
        lastPose = nextPose;

        if (lastPose.isFinished) {
            evaluateLastPose();
            return;
        }

        boolean isFinished = false;
        if ((s + ds) > 1.0) {
            s = 1.0;
            isFinished = true;
        } else {
            s += ds;
        }

        //calculate the next pose from given motion
        final Pose pose = motion.evaluatePose(s);
        final double direction = Math.signum(motion.getLength());

        //estimate angle to turn s
        final double dl = lastPose.point.distance(pose.point) * direction;
        final double dAngle = MotionProvider.boundAngle(pose.angle - lastPose.angle);

        //estimate lengths each wheel will turn
        final double dlLeft = (dl - ((dAngle * ROBOT_WIDTH) / 2));
        final double dlRight = (dl + ((dAngle * ROBOT_WIDTH) / 2));

        //assuming one of the wheels will limit motion, calculate time this step will take
        double dt = Math.max(Math.abs(dlLeft), Math.abs(dlRight)) / motion.getVCruise();
        double a = 0.0; //acceleration doesn't matter if following steady motion

        //bound time steps for initial/final acceleration
        switch (motion.getLimitMode()) {
            case LimitLinearAcceleration:
                //assuming constant linear acceleration from initial/to final speeds
                final double v = Math.abs(dl) / dt;
                final double lMidpoint = (lastPose.getCenterLength() + (0.5 * dl)) - l0;

                final double vAcceleration = Math.sqrt(
                    (v0 * v0) + (motion.getAMax() * Math.abs(lMidpoint)));
                final double vDeceleration = Math
                    .sqrt((v1 * v1) + (motion.getAMax() * (Math
                        .abs(motion.getLength() - lMidpoint))));

                if ((vAcceleration < v) && (vAcceleration < vDeceleration)) {
                    a = motion.getAMax();
                    dt = Math.abs(dl) / vAcceleration;
                }

                if ((vDeceleration < v) && (vDeceleration < vAcceleration)) {
                    a = -motion.getAMax();
                    dt = Math.abs(dl) / vDeceleration;
                }

                //System.out.println(String.format("v=%f, vaccel=%f, vdecel=%f", v, vAcceleration, vDeceleration));
                break;
            case LimitRotationalAcceleration:
                //assuming constant angular acceleration from/to zero angular speed
                final double omega = Math.abs(dlRight - dlLeft) / dt / ROBOT_WIDTH;
                final double thetaMidpoint = lastPose.angle + (0.5 * dAngle);

                final double omegaAcceleration = Math.sqrt(motion.getAMax() * Math
                    .abs(MotionProvider.boundAngle(thetaMidpoint - motion.getInitialTheta())));
                final double omegaDeceleration = Math.sqrt(motion.getAMax() * Math
                    .abs(MotionProvider.boundAngle(thetaMidpoint - motion.getFinalTheta())));

                if ((omegaAcceleration < omega) && (omegaAcceleration < omegaDeceleration)) {
                    dt = Math.abs(dlRight - dlLeft) / omegaAcceleration / ROBOT_WIDTH;
                }

                if ((omegaDeceleration < omega) && (omegaDeceleration < omegaAcceleration)) {
                    dt = Math.abs(dlRight - dlLeft) / omegaDeceleration / ROBOT_WIDTH;
                }
                break;
        }

        //create new kinematic state. Old state is retained to interpolate positions, new state contains estimate for speed and accel
        KinematicState left = new KinematicState(
            lastPose.left.length + dlLeft,
            dlLeft / dt,
            a * direction
        );
        KinematicState right = new KinematicState(
            lastPose.right.length + dlRight,
            dlRight / dt,
            a * direction
        );

        nextPose = new KinematicPose(pose, left, right, lastPose.t + dt, isFinished);
    }

    /**
     * Calculates the path to follow within a particular distance.
     *
     * @param t time
     * @return the interpolated pose from {@code lastPose} and {@code nextPose}
     */
    public final KinematicPose interpolatePose(double t) {

        if (t <= lastPose.t) {
            return lastPose;
        }

        if (lastPose.isFinished) {
            return lastPose;
        }

        while (t > nextPose.t) {
            evaluateNextPose(1.0 / nPoints);
            if (lastPose.isFinished) {
                return lastPose;
            }
        }

        double dt = nextPose.t - lastPose.t;
        double p = (nextPose.t - t) / dt;
        double q = (t - lastPose.t) / dt;

        return KinematicPose.interpolate(lastPose, p, nextPose, q);
    }

    /**
     * @return the motion provider
     */
    public final MotionProvider getMotion() {
        return motion;
    }

    /**
     * @return the next pose
     */
    public final Pose getPose() {
        return new Pose(nextPose.point, nextPose.angle);
    }

    /**
     * @return the wheel positions of the next pose
     */
    public final RobotPair getWheelPositions() {
        return new RobotPair(nextPose.left.length, nextPose.right.length);
    }

    /**
     * @return the time at which next pose should occur
     */
    public final double getTime() {
        return nextPose.t;
    }

    @Override
    public final String toString() {
        return String.format("Kinematics{lastPose=%s, nextPose=%s}", lastPose, nextPose);
    }
}
