package org.usfirst.frc.team2974.robot.controllers;


import java.util.Arrays;
import org.usfirst.frc.team2974.robot.exception.RobotException;

/**
 * TODO: super coder (heh.) needed
 */
public class MotionPath {

    /**
     * These are the motions it will take. The end of motions[i] HAVE to be the same as motions[i + 1].
     */
    private MotionProvider[] motions;

    public MotionPath(MotionProvider... motions) {
        try {
            MotionPath.checkMotions(motions);
            this.motions = motions;
        } catch (RobotException r) {
            r.printStackTrace();
            System.err.println("(This means that " + this + " won't run properly!!!)");
            this.motions = new MotionProvider[0];
        }
    }

    public static void checkMotions(MotionProvider... motions) throws RobotException {
        for (int i = 1; i < motions.length; i++) {
            if (!motions[i].getInitialPose().equals(motions[i - 1].getFinalPose())) {
                throw new RobotException(
                    "Motion " + i + " initial pose does not equal Motion " + (i + 1)
                        + " final pose.");
            }
        }
    }

    @Override
    public final String toString() {
        return "MotionPath{" +
            "motions=" + Arrays.toString(this.motions) +
            '}';
    }
}
