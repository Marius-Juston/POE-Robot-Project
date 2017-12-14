package org.curvedrawer.util;

import java.util.ArrayList;
import java.util.List;

public final class Converter {
    private Converter() {
    }

    public static Pose[] stringToPoses(String stringPoses) {
        String[] information = stringPoses.split(" ");

        assert (information.length % 3) == 0;

        List<Pose> poses = new ArrayList<>(information.length / 3);

        for (int i = 0; i < information.length; i += 3) {
            double x = Double.parseDouble(information[i]);
            double y = Double.parseDouble(information[i + 1]);
            double angle = Double.parseDouble(information[i + 2]);

            poses.add(new Pose(x, y, angle));
        }

        return poses.toArray(new Pose[poses.size()]);
    }

    public static String posesToString(Pose[] poses) {
        StringBuilder stringBuilder = new StringBuilder(poses.length * 6);

        for (Pose pose : poses) {
            stringBuilder.append(pose.getX());
            stringBuilder.append(' ');
            stringBuilder.append(pose.getY());
            stringBuilder.append(' ');
            stringBuilder.append(pose.getAngle());
            stringBuilder.append(' ');
        }

        return stringBuilder.toString();
    }
}
