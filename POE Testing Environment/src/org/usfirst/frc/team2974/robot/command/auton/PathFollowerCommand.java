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

    private MotionProvider pathFollower;
    private double vCruise;
    private double aMax;
    private boolean isForwards;
    private Pose[] poses;

    public PathFollowerCommand(double vCruise, double aMax, boolean isForwards,
        String... smartDashboardPaths) {
        this(vCruise, aMax, isForwards,
            Arrays.stream(smartDashboardPaths)
                .map(PointRetriever::retrievePoses).reduce(
                new ArrayList<>(), (poses23, poses2) -> {
                    Collections.addAll(poses23, poses2);
                    return poses23;
                }, (poses22, poses2) -> {
                    poses22.addAll(poses2);
                    return poses22;
                }).toArray(new Pose[0]));
    }

    public PathFollowerCommand(double vCruise, double aMax, boolean isForwards, Pose... poses) {
        this.vCruise = vCruise;
        this.aMax = aMax;
        this.isForwards = isForwards;
        this.poses = poses;

        requires(driveTrain); // tells the command that it will use the drivetrain subsystem
    }

    @Override
    protected void initialize() {
        this.pathFollower = new MotionPathFollower(vCruise, aMax, isForwards, poses);

        driveTrain.addControllerMotion(pathFollower);
        driveTrain.startMotion();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
