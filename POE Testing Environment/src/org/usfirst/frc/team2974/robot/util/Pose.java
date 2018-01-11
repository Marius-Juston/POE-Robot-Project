package org.usfirst.frc.team2974.robot.util;

public class Pose {

	public final Point2D point;
	public final double angle;

	public Pose(Point2D point, double angle) {
		this.point = point;
		this.angle = angle;
	}

	/**
	 * Interpolates pose0 and pose1.
	 *
	 * @param pose0 initial pose
	 * @param p how far the robot has gone
	 * @param pose1 next pose
	 * @param q how far it has yet to go
	 * @return the interpolated pose
	 */
	public static Pose interpolate(Pose pose0, double p, Pose pose1, double q) {
		return new Pose(Point2D.interpolate(pose0.point, p, pose1.point, q),
			(p * pose0.angle) + (q * pose1.angle));
	}

	/**
	 * Offsets point with {@code l} along {@code angle}.
	 *
	 * @param l distance to offset by
	 * @return offset point
	 */
	public final Point2D offsetPoint(double l) {
		return new Point2D(point.getX() + (l * Math.cos(angle)),
			point.getY() + (l * Math.sin(angle)));
	}

	@Override
	public String toString() {
		return String.format("Pose{x=%f, y=%f, angle=%f}", point.getX(), point.getY(), angle);
	}

}
