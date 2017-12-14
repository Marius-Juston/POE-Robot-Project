package org.curvedrawer.path;

import javafx.beans.property.SimpleListProperty;
import org.curvedrawer.Main;
import org.curvedrawer.util.Converter;
import org.curvedrawer.util.Pose;

public class RobotPathViewer {

    private final String pathKey;
    private final Pose[] pathPoses;
    private final SimpleListProperty<Pose> robotPoses;

    private final String robotPathKey;

    public RobotPathViewer(String pathKey, Pose[] pathPoses) {
        this.pathPoses = pathPoses;
        robotPoses = new SimpleListProperty<>();
        this.pathKey = pathKey;
        robotPathKey = pathKey + "_robot";

        Main.networkTable.addTableListener(robotPathKey, (iTable, s, o, b) -> robotPoses.setAll(Converter.stringToPoses((String) o)), true);
    }

}
