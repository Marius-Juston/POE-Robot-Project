package org.usfirst.frc.team2974.robot.motion;

import org.usfirst.frc.team2974.robot.util.MotionProvider;
import org.usfirst.frc.team2974.robot.util.Point2D;
import org.usfirst.frc.team2974.robot.util.Pose;

/**
 * JUST DISTANCE, FORWARD/BACKWARDS
 */
public class MotionPathStraight extends MotionProvider {

	private final Pose pose0;
	private final Pose pose1;

	/**
	 * Constructs MotionPathStraight.
	 *
	 * @param pose0 initial pose
	 * @param distance distance to go straight
	 * @param vCruise cruise velocity
	 * @param aMax max acceleration/deceleration
	 */
	public MotionPathStraight(Pose pose0, double distance, double vCruise, double aMax) {
		super(vCruise, aMax);

		// FIXME? why is this synchronized
		synchronized (this) {
			this.pose0 = pose0;
			pose1 = new Pose(pose0.offsetPoint(distance), pose0.angle);
			setLength(distance);
		}
	}

	@Override
	public final Pose evaluatePose(double s) {
		Point2D X = Point2D.interpolate(pose0.point, 1.0 - s, pose1.point, s);
		double angle = pose0.angle;
		return new Pose(X, angle);
	}

	@Override
	public final MotionProvider.LimitMode getLimitMode() {
		return MotionProvider.LimitMode.LimitLinearAcceleration;
	}

	@Override
	public final double getInitialTheta() {
		return pose0.angle;
	}

	@Override
	public final double getFinalTheta() {
		return pose1.angle;
	}


	@Override
	public String toString() {
		return String.format(
			"MotionPathStraight{pose0=%s, pose1=%s, length=%f, vCruise=%f, aMax=%f}"
			, pose0
			, pose1
			, getLength()
			, getVCruise()
			, getAMax()
		);
	}


}
