package org.usfirst.frc.team2974.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2974.robot.subsystems.DriveTrain;
import java.util.ArrayList;
import java.util.List;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the IterativeRobot documentation. If you change the name of this class
 * or the package after creating this project, you must also update the manifest file in the
 * resource directory.
 */
public class Robot extends IterativeRobot {

  // used for static methods only
  private static Robot _instance;

  final String defaultAuto = "Default";
  final String customAuto = "My Auto";
  String autoSelected;
  SendableChooser<String> chooser = new SendableChooser<>();

  private final List<Subsystem> subsystems;

  public Robot() {
    _instance = this;

    subsystems = new ArrayList<>();
  }

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    chooser.addDefault("Default Auto", defaultAuto);
    chooser.addObject("My Auto", customAuto);
    SmartDashboard.putData("Auto choices", chooser);

    subsystems.add(new DriveTrain());
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    autoSelected = chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    System.out.println("Auto selected: " + autoSelected);
  }

  /**
   * This function is called periodically during autonomous
   */
  @Override
  public void autonomousPeriodic() {
    switch (autoSelected) {
      case customAuto:
        // Put custom auto code here
        break;
      case defaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control
   */
  @Override
  public void teleopPeriodic() {}

  /**
   * This function is called periodically during test mode
   */
  @Override
  public void testPeriodic() {}

  /**
   * Finds the specified Subsystem on the current robot.
   *
   * Method usage example: getSubsystem(DriveTrain.class) -> DriveTrain on Robot
   *
   * @param type the Class.class type of subsystem. ex -> DriveTrain.class
   * @param <T> The type of subsystem to get, unused in <> sense.
   * @return The specified subsystem of type type, <b>null</b> otherwise.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Subsystem> T getSubsystem(Class<T> type) {
    T subsystem = _instance.subsystems.stream().filter(type::isInstance).findFirst().map(type::cast)
        .orElse(null); // returns null if subsystem doesn't exist

    if (subsystem == null) // FIXME? should this be like this, or throw Exception
      System.err.println("Subsystem of type " + type.getTypeName()
          + " does not exist. \nDid you forget to add it?");

    return subsystem;
  }
}

