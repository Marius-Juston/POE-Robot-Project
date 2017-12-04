package org.usfirst.frc.team2974.robot.controllers;

/**
 * This is the pair of the left and right wheels. DOUBLE VALUES
 */
public class RobotPair {

    public final double left; // location of left side
    public final double right; // ^^ right ^

    public RobotPair(double left, double right) {
        this.left = left;
        this.right = right;
    }

    /**
     * @return location of center
     */
    public final double mean() {
        return (left + right) / 2f;
    }

    @Override
    public final String toString() {
        return String.format("RobotPair{left=%f, right=%f}", left, right);
    }

}
