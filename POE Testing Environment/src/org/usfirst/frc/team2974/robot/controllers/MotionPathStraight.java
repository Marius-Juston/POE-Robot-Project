package org.usfirst.frc.team2974.robot.controllers;

/**
 * JUST DISTANCE, FORWARD/BACKWARDS
 */
public class MotionPathStraight extends MotionProvider{
	private Pose pose0;
	private Pose pose1;
	private double length;
	
	public MotionPathStraight(Pose pose0, double distance, double vCruise, double aMax){
		super(vCruise, aMax);
		synchronized (this) {
			this.pose0 = pose0;
			this.pose1 = new Pose(pose0.offsetPoint(distance), pose0.angle);
			this.length = distance;
		}
	}

	@Override
	public Pose evaluatePose(double s) {
		Point2D X = Point2D.interpolate(pose0.point, 1.0-s, pose1.point, s);
		double angle = pose0.angle;
		return new Pose(X, angle) ; 
	}
	
	@Override
	public LimitMode getLimitMode() {
		return LimitMode.LimitLinearAcceleration;
	}
	@Override
	public double getLength() {
		return length;
	}
	@Override
	public double getInitialTheta() {
		return pose0.angle;
	}
	@Override
	public double getFinalTheta() {
		return pose1.angle;
	}

}
