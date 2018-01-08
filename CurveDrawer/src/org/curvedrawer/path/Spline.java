package org.curvedrawer.path;

import org.curvedrawer.util.Point;
import org.curvedrawer.util.Pose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creates splines that travel through set points, or "knots", and all
 * information needed to make the robot travel along the path.
 *
 * @author Russell Newton, Walton Robotics
 */

public class Spline extends Path {
    /**
     * Create a new spline
     *
     * @param numberOfSteps - the amount of points generated for the path, the resolution of the spline
     * @param knots         - the fixed points the spline will travel through
     */
    public Spline(int numberOfSteps, Point... knots) {
        super(numberOfSteps, knots);
    }

    /**
     * Creates the control points required to make cubic bezier curves that
     * transition between knots.
     * <p>
     * https://www.particleincell.com/2012/bezier-splines/
     * https://en.wikipedia.org/wiki/Tridiagonal_matrix_algorithm
     *
     * @return A list of lists that hold the control points for the segments in the
     * spline
     */
    private List<List<Point>> computeControlPoints() {
        int degree = getPoints().size() - 1;

        if (degree > 0) {

            Point[] points1 = new Point[degree];
            Point[] points2 = new Point[degree];

            /* constants for Thomas Algorithm */
            double[] a = new double[degree];
            double[] b = new double[degree];
            double[] c = new double[degree];
            double[] r_x = new double[degree];
            double[] r_y = new double[degree];

            /* left most segment */
            a[0] = 0;
            b[0] = 2.0;
            c[0] = 1.0;
            r_x[0] = getPoints().get(0).getX() + (2.0 * getPoints().get(1).getX());
            r_y[0] = getPoints().get(0).getY() + (2.0 * getPoints().get(1).getY());

            /* internal segments */
            for (int i = 1; i < (degree - 1); i++) {
                a[i] = 1.0;
                b[i] = 4.0;
                c[i] = 1.0;
                r_x[i] = (4.0 * getPoints().get(i).getX()) + (2.0 * getPoints().get(i + 1).getX());
                r_y[i] = (4.0 * getPoints().get(i).getY()) + (2.0 * getPoints().get(i + 1).getY());
            }

            /* right segment */
            a[degree - 1] = 2.0;
            b[degree - 1] = 7.0;
            c[degree - 1] = 0;
            r_x[degree - 1] = (8.0 * getPoints().get(degree - 1).getX()) + getPoints().get(degree).getX();
            r_y[degree - 1] = (8.0 * getPoints().get(degree - 1).getY()) + getPoints().get(degree).getY();

            /* solves Ax=b with the Thomas algorithm */
            for (int i = 1; i < degree; i++) {
                double m = a[i] / b[i - 1]; // temporary variable
                b[i] -= m * c[i - 1];
                r_x[i] -= m * r_x[i - 1];
                r_y[i] -= m * r_y[i - 1];
            }
            points1[degree - 1] = new Point(r_x[degree - 1] / b[degree - 1], r_y[degree - 1] / b[degree - 1]);
            for (int i = degree - 2; i >= 0; --i) {
                points1[i] = new Point((r_x[i] - (c[i] * points1[i + 1].getX())) / b[i],
                        (r_y[i] - (c[i] * points1[i + 1].getY())) / b[i]);
            }

            /* we have p1, now compute p2 */
            for (int i = 0; i < (degree - 1); i++) {
                points2[i] = new Point((2.0 * getPoints().get(i + 1).getX()) - points1[i + 1].getX(),
                        (2.0 * getPoints().get(i + 1).getY()) - points1[i + 1].getY());
            }

            points2[degree - 1] = new Point(0.5 * (getPoints().get(degree).getX() + points1[degree - 1].getX()),
                    0.5 * (getPoints().get(degree).getY() + points1[degree - 1].getY()));

            List<List<Point>> controlPoints = new ArrayList<>(degree);

            for (int i = 0; i < degree; i++) {
                List<Point> segmentControlPoints = new ArrayList<>(4);
                Collections.addAll(segmentControlPoints, getPoints().get(i), points1[i], points2[i], getPoints().get(i + 1));
                Collections.addAll(controlPoints, segmentControlPoints);
            }

            return controlPoints;
        } else {
            return new ArrayList<>(0);
        }
    }

    /**
     * Joins the bezier curves defining the spline into individual arrays of Points
     *
     * @param pathControlPoints - a List of Lists of control points for each curve that make up the spline
     */
    private Pose[] joinBezierCurves(List<List<Point>> pathControlPoints) {
        List<Pose> pathPointsAdd = new ArrayList<>(getNumberOfSteps());

        for (int i = 0; i < pathControlPoints.size(); i++) { //FIXME make it so that the spline returns exactly the correct number of points
            List<Point> curveControlPoints = pathControlPoints.get(i);
            Point[] controlPoints = curveControlPoints.toArray(new Point[curveControlPoints.size()]);
            BezierCurve curve = new BezierCurve(Math.toIntExact(Math.round((double) getNumberOfSteps() / pathControlPoints.size())), controlPoints);


            Pose[] pathPoints = curve.createPathPoses();

            Pose[] poses = new Pose[pathPoints.length - ((i == (pathControlPoints.size() - 1)) ? 1 : 0)]; //We do not want to repeat having the same last and first points for consecutive bezier curve so we need to trim off that point if it is not the last bezier curve
            System.arraycopy(pathPoints, 0, poses, 0, poses.length);

            Collections.addAll(pathPointsAdd, poses);
        }

        return pathPointsAdd.toArray(new Pose[pathPointsAdd.size()]);
    }

    @Override
    public final Pose[] createPathPoses() {
        return joinBezierCurves(computeControlPoints());
    }

}
