package org.waltonrobotics.curvedrawer.util;

import javafx.scene.paint.Color;

public class Helper {

  public static String colorToString(Color color) {
    return String.format("rgb(%f, %f, %f)", color.getRed(), color.getGreen(), color.getBlue());
  }
}
