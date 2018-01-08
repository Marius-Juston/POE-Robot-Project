package org.curvedrawer.util;

import java.util.ArrayList;
import java.util.List;

public enum Converter {
    ;

    /**
     * Converts a String in the following format: "x y angle x y angle ..." to an array of poses
     *
     * @param stringPoses string to convert to an array of poses
     * @return the convert String into the array of poses
     */
    public static Pose[] stringToPoses(String stringPoses) {
        String[] information = stringPoses.split(" ");

        assert (information.length % 3) == 0;

        List<Pose> poses = new ArrayList<>(information.length / 3);

        for (int i = information.length - 1; i >= 0; i -= 3) {
            double x = Double.parseDouble(information[i]);
            double y = Double.parseDouble(information[i + 1]);
            double angle = Double.parseDouble(information[i + 2]);

            poses.add(new Pose(x, y, angle));
        }

        return poses.toArray(new Pose[poses.size()]);
    }

    /**
     * Converts an array of poses to a String in the following format: "x y angle x y angle ..."
     *
     * @param poses poses to convert to string
     * @return a string of poses in the format above
     */
    public static String posesToString(Pose[] poses) {
        StringBuilder stringBuilder = new StringBuilder(poses.length * 6);

        for (Pose pose : poses) {
            stringBuilder.append(pose.getScaledX());
            stringBuilder.append(' ');
            stringBuilder.append(pose.getScaledY());
            stringBuilder.append(' ');
            stringBuilder.append(pose.getAngle());
            stringBuilder.append(' ');
        }

        return stringBuilder.toString();
    }
}
