package org.curvedrawer.path;

public enum PathType {
    BEZIER_CURVE(BezierCurve.class), SPLINE(Spline.class);

    private Class associatedClass;

    PathType(Class associatedClass) {

        this.associatedClass = associatedClass;
    }

    public Class getAssociatedClass() {
        return associatedClass;
    }
}
