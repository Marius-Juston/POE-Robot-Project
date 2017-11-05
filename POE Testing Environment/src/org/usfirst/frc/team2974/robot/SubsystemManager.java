package org.usfirst.frc.team2974.robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import edu.wpi.first.wpilibj.command.Subsystem;

public final class SubsystemManager {

  private static final List<Subsystem> SUBSYSTEM_LIST = new ArrayList<>();

  /**
   * Finds the specified Subsystem on the current robot.
   *
   * Method usage example: getSubsystem(DriveTrain.class) -> DriveTrain on Robot
   *
   * @param type the Class.class type of subsystem. ex -> DriveTrain.class
   * @param <T> The type of subsystem to get, unused in <> sense.
   * @return The specified subsystem of type type, <b>null</b> otherwise.
   */
  public synchronized static <T extends Subsystem> T getSubsystem(Class<T> type) {
    T subsystem = SUBSYSTEM_LIST.stream().filter(type::isInstance) // filters out the subsystems
                                                                   // that are not instances of the
                                                                   // class that type comes from
        .findFirst() // finds the first instance of class that we are searching for
        .map(type::cast) // casts the subsystem instance to the more specific sub class of Subsystem
                         // to the desired cast
        .orElse(null); // returns null if subsystem doesn't exist

    if (subsystem == null) // FIXME? should this be like this, or throw Exception
      System.err.println("Subsystem of type " + type.getTypeName()
          + " does not exist. \nDid you forget to add it?");

    return subsystem;
  }


  /**
   * Adds the subsystems to the list of subsystems making it available for retrieval using the
   * getSubsystem method
   * 
   * @param subsystems to add to the subsystem list
   */
  public static void addSubsystem(Subsystem... subsystems) {
    Collections.addAll(SUBSYSTEM_LIST, subsystems);
  }

}
