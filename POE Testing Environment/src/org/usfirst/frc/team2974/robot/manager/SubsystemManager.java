package org.usfirst.frc.team2974.robot.manager;

import edu.wpi.first.wpilibj.command.Subsystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.usfirst.frc.team2974.robot.exception.RobotRuntimeException;

/**
 * Class meant to manage the subsystem for easy access and retrieval.
 */
public final class SubsystemManager {

  private static final List<Subsystem> SUBSYSTEM_LIST = new ArrayList<>();

  /**
   * Finds the first specified Subsystem on the current robot.
   *
   * Method usage example: getSubsystem(DriveTrain.class) -> DriveTrain on Robot
   *
   * @param type the Class.class type of subsystem. ex -> DriveTrain.class
   * @param <T> The type of subsystem to get, unused in <> sense.
   * @return The specified subsystem of type type, <b>null</b> otherwise.
   * @throws RobotRuntimeException throws runtime exception if the subsystem does not exist
   */
  public static synchronized <T extends Subsystem> T getSubsystem(Class<T> type) {
    T subsystem = SubsystemManager.SUBSYSTEM_LIST.stream()
        .filter(type::isInstance) /* filters out the subsystem that are not instances of the class that type comes from */
        .findFirst() // finds the first instance of class that we are searching for
        .map(type::cast) /* casts the subsystem instance to the more specific sub class of Subsystem */
        .orElse(null); // returns null if subsystem doesn't exist

    if (subsystem == null) {
      throw new RobotRuntimeException("Subsystem of type " + type.getName()
          + " does not exist inside the list of available subsystem.\nDid you forget to add it?");
    }

    return subsystem;
  }


  /**
   * Adds the subsystem to the list of subsystem making it available for retrieval using the
   * getSubsystem method
   *
   * @param subsystems to add to the subsystem list
   */
  public static void addSubsystem(Subsystem... subsystems) {
    Collections.addAll(SubsystemManager.SUBSYSTEM_LIST, subsystems);
  }

}
