package org.usfirst.frc.team2974.robot;

import edu.wpi.first.wpilibj.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import org.usfirst.frc.team2974.robot.kinematic.KinematicPose;
import org.usfirst.frc.team2974.robot.kinematic.Kinematics;
import org.usfirst.frc.team2974.robot.util.MotionProvider;
import org.usfirst.frc.team2974.robot.util.PoseProvider;
import org.usfirst.frc.team2974.robot.util.RobotPair;

public class MotionProfileController {

	private final BlockingDeque<MotionProvider> motions = new LinkedBlockingDeque<>(); // motions that we want to do
	private final PoseProvider poseProvider; // wheel positions and pose
	private final java.util.Timer controller; // schedules all of the motions to be run, TIMER
	// PID values
	private double kV; // what we want to change the voltage by to get the motion we want
	private double kK; // up/down translation
	private double kA; // FIXME?? WHAT IS THIS
	private double kP; // what we want to change the power by to get the motion we want
	private Kinematics currentKinematics; // does all the math
	private KinematicPose staticKinematicPose; // has the vectors that we use in the calculations
	private volatile boolean isEnabled; // yeah, this

	/**
	 * Constructs MotionProfileController.
	 *
	 * @param poseProvider provides current pose
	 * @param period rate to update PID loop, in seconds
	 */
	public MotionProfileController(PoseProvider poseProvider, double period) {
		this.poseProvider = poseProvider;

		controller = new java.util.Timer();
		controller.schedule(new MPCTask(), 0L, (long) (period * 1000.0));

		staticKinematicPose = Kinematics
			.staticPose(poseProvider.getPose(), poseProvider.getWheelPositions(),
				Timer.getFPGATimestamp());
	}

	/**
	 * Stops the motion
	 */
	public final void free() {
		this.controller.cancel();
	}

	/**
	 * Adds a motion to execute after all others before it.
	 *
	 * @param motion motion to execute
	 */
	public final synchronized void addMotion(MotionProvider motion) {
		motions.addLast(motion);
	}

	/**
	 * Enables the use of the controller.
	 */
	public final synchronized void enable() {
		MotionProvider newMotion = motions.poll();

		if (newMotion != null) {
			currentKinematics = new Kinematics(newMotion, poseProvider.getWheelPositions(),
				Timer.getFPGATimestamp(), 0, 0);

			isEnabled = true;
		}
	}

	/**
	 * Hard stop
	 */
	public final synchronized void cancel() {
		isEnabled = false;
		currentKinematics = null;
		motions.clear();

		Robot.driveTrain.setPowers(0, 0);
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public final synchronized double getKV() {
		return kV;
	}

	public final synchronized void setKV(double kV) {
		this.kV = kV;
	}

	public final synchronized double getKK() {
		return kK;
	}

	public final synchronized void setKK(double kK) {
		this.kK = kK;
	}

	public final synchronized double getKA() {
		return kA;
	}

	public final synchronized void setKA(double kA) {
		this.kA = kA;
	}

	public final synchronized double getKP() {
		return kP;
	}

	public final synchronized void setKP(double kP) {
		this.kP = kP;
	}

	/**
	 * This is the PID loop, accounts for distance error.
	 */
	private void calculate() {

		double leftPower = 0;
		double rightPower = 0;
		final boolean enabled;

		synchronized (this) {
			enabled = isEnabled;
		}

		if (enabled) {

			double time = Timer.getFPGATimestamp();

			final RobotPair wheelPositions = poseProvider.getWheelPositions();

			final KinematicPose kinematicPose =
				currentKinematics != null ? currentKinematics.interpolatePose(time)
					: staticKinematicPose;

			synchronized (this) {
				//feed forward
				leftPower += ((kV * kinematicPose.left.velocity) + kK)
					+ (kA * kinematicPose.left.acceleration);
				rightPower +=
					((kV * kinematicPose.right.velocity) + kK)
						+ (kA * kinematicPose.right.acceleration);
				//feed back
				leftPower += kP * (kinematicPose.left.length - wheelPositions.left);
				rightPower += kP * (kinematicPose.right.length - wheelPositions.right);

			}

			leftPower = Math.max(-1.0, Math.min(1.0, leftPower));
			rightPower = Math.max(-1.0, Math.min(1.0, rightPower));

			Robot.driveTrain.setPowers(leftPower, rightPower);

			if (kinematicPose.isFinished) {
				final MotionProvider newMotion = motions.pollFirst();
				if (newMotion != null) {
					currentKinematics = new Kinematics(newMotion,
						currentKinematics.getWheelPositions(),
						currentKinematics.getTime(), 0, 0);
				} else {
					staticKinematicPose = Kinematics
						.staticPose(currentKinematics.getPose(),
							currentKinematics.getWheelPositions(),
							currentKinematics.getTime());
					currentKinematics = null;
				}
			}
		}
	}

	/**
	 * @param time time
	 * @return if the current motion is finished
	 */
	public final boolean isCurrentMotionFinished(double time) {
		final KinematicPose kinematicPose =
			currentKinematics != null ? currentKinematics.interpolatePose(time)
				: staticKinematicPose;

		return kinematicPose.isFinished;
	}

	/**
	 * @return if the kinematic is finished
	 */
	public final boolean isFinished() {
		return currentKinematics == null;
	}

	public final MotionProvider getCurrentMotion() {
		if (currentKinematics == null) {
			return null;
		}

		return currentKinematics.getMotion();
	}

	@Override
	public final String toString() {
		return String.format(
			"MotionProfileController{motions=%s, kV=%f, kK=%f, kA=%f, kP=%f, currentKinematics=%s, isEnabled=%s}",
			motions, kV, kK, kA, kP, currentKinematics, isEnabled);
	}

	private class MPCTask extends TimerTask {

		@Override
		public final void run() {
			calculate();
		}
	}
}