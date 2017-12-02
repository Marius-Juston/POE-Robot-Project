package org.waltonrobotics.curvedrawer.util;

import javafx.scene.paint.Color;

public class Config {

  public static class PointSetting {

    public static final double RADIUS = 3;
    public static final Color COLOR = Color.BLUE;
  }

  public static class LineSetting {

    public static final int STROKE_WIDTH = 3;
    public static final Color STROKE_COLOR = Color.RED;
  }

  public static final class ROBOT {

    public static final double ROBOT_WIDTH = 0.7; // in meters
  }

  public static class Curve {

    public static final int RESOLUTION = 50;
  }

  public static class Resources {

    private final static String packagePath = "/org/waltonrobotics/curvedrawer/";

    public static final String MENU = packagePath + "menu.fxml";
    public static final String PATH_SELECTOR = packagePath + "pathSelector.fxml";
    public static final String TAB = packagePath + "tab.fxml";
  }
}
