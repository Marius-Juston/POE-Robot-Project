package org.curvedrawer.path;

import org.curvedrawer.util.LimitMode;
import org.curvedrawer.util.Pose;

public abstract class Path {
    private final double cruiseVelocity;
    private final double acceleration;

    /**
     * @param cruiseVelocity - cruise velocity
     * @param aMax           - max acceleration
     */
    Path(double cruiseVelocity, double aMax) {
        if (cruiseVelocity == 0)
            throw new IllegalArgumentException("cruiseVelocity cannot be 0");
        this.cruiseVelocity = cruiseVelocity;

        if (aMax == 0)
            throw new IllegalArgumentException("acceleration cannot be 0");
        this.acceleration = aMax;
    }

    private static Pose[] offsetPosesPerpendicularly(Pose[] poses, double offsetDistance) {
        Pose[] offsetPoses = new Pose[poses.length];

        for (int i = 0; i < offsetPoses.length; i++) {
            offsetPoses[i] = poses[i].offsetPerpendicular(offsetDistance);
        }

        return offsetPoses;
    }

    double getCruiseVelocity() {
        return cruiseVelocity;
    }

    double getAcceleration() {
        return acceleration;
    }

    /**
     * Finds the points that define the path the robot follows
     *
     * @return an array of points that holds the points along the path
     */
    public abstract Pose[] createPathPoints();

    public abstract LimitMode getLimitMode();
}
