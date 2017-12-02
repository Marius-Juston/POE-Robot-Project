package org.usfirst.frc.team2974.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2974.robot.command.auton.DriveForwardCommand;
import org.usfirst.frc.team2974.robot.manager.SmartDashboardManager;
import org.usfirst.frc.team2974.robot.manager.SubsystemManager;
import org.usfirst.frc.team2974.robot.subsystem.DriveTrain;
import org.usfirst.frc.team2974.robot.subsystem.GearIntake;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the IterativeRobot documentation. If you change the name of this class
 * or the package after creating this project, you must also update the manifest file in the
 * resource directory.
 */
public class Robot extends IterativeRobot {

  private final String defaultAuto = "Default";
  private final String customAuto = "My Auto";
  private final SendableChooser<String> autonChooser = new SendableChooser<>();
  private String autoSelected;

  private static void update() {
    SmartDashboardManager.update();
  }

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public final void robotInit() {
    this.autonChooser.addDefault("Default Auto", this.defaultAuto);
    this.autonChooser.addObject("My Auto", this.customAuto);
    SmartDashboardManager.addBind("Auto choices", this.defaultAuto, () -> this.autonChooser);

    SubsystemManager.addSubsystem(new DriveTrain());
    SubsystemManager.addSubsystem(new GearIntake());

    SmartDashboard.putData("Drive forward 5 meters", new DriveForwardCommand(5, 3, 0.5));
    SmartDashboardManager.addBind("Drive forward 5 meters", new DriveForwardCommand(5, 3, 0.5));

    SmartDashboardManager.addBind("Left Encoder Dist", 0, RobotMap.leftEncoder::getDistance);
    SmartDashboardManager.addBind("Right Encoder Dist", 0, RobotMap.rightEncoder::getDistance);

    SmartDashboardManager.addBind("Left Encoder Rate", 0, RobotMap.leftEncoder::getRate);
    SmartDashboardManager.addBind("Right Encoder Rate", 0, RobotMap.rightEncoder::getRate);

    SmartDashboardManager.addBind("Gear Sensor", 0, RobotMap.gearSensor::get);
  }

  /**
   * This autonomous (along with the autonChooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable autonChooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the autonChooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * autonChooser code above as well.
   */
  @Override
  public final void autonomousInit() {
    this.autoSelected = this.autonChooser.getSelected();
  }

  /**
   * This function is called periodically during autonomous
   */
  @Override
  public final void autonomousPeriodic() {
    Robot.update();

    switch (this.autoSelected) {
      case customAuto:
        // Put custom auto code here
        break;
      case defaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  @Override
  public void teleopInit() {
    //InputManager.bind
  }

  /**
   * This function is called periodically during operator control
   */
  @Override
  public final void teleopPeriodic() {
    Robot.update();
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode
   */
  @Override
  public final void testPeriodic() {
    Robot.update();
  }
}

