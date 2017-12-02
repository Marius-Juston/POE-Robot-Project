package org.usfirst.frc.team2974.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.usfirst.frc.team2974.robot.Input;
import org.usfirst.frc.team2974.robot.RobotConfiguration;
import org.usfirst.frc.team2974.robot.exception.RobotRuntimeException;
import org.usfirst.frc.team2974.robot.io.Driver;
import org.usfirst.frc.team2974.robot.manager.SubsystemManager;
import org.usfirst.frc.team2974.robot.subsystem.DriveTrain;

public class DriveCommand extends Command {

  private final Set<Driver> drivers;
  private final DriveTrain driveTrain;
  private Driver currentDriver;

  public DriveCommand(final Driver initialDriver) {
    super("DriveCommand");

    this.drivers = new HashSet<>(4);

    this.setCurrentDriver(initialDriver);

    this.driveTrain = SubsystemManager.getSubsystem(DriveTrain.class);

    this.requires(this.driveTrain);
  }

  public DriveCommand() {
    this(Driver.DEFAULT_DRIVER);
  }

  public static double getLeftThrottle() {
    final double leftY = -Input.leftJoystick.getY();
    if (Math.abs(leftY) < RobotConfiguration.LEFT_JOYSTICK_THRESHOLD) {
      return 0;
    }

    return leftY;
  }

  public static double getRightThrottle() {
    final double rightY = -Input.rightJoystick.getY();
    if (Math.abs(rightY) < RobotConfiguration.RIGHT_JOYSTICK_THRESHOLD) {
      return 0;
    }

    return rightY;
  }

  /**
   * Returns current driver
   *
   * @return Returns current driver
   */
  public final Driver getCurrentDriver() {
    return this.currentDriver;
  }

  /**
   * Changes the current driver all the while reinitializing the buttons
   *
   * @param driver the new current driver
   */
  public final void setCurrentDriver(final Driver driver) {
    if ((driver != null) && !driver.equals(this.currentDriver)) {
      this.currentDriver = driver;

      driver.initButtons();
    } else {
      throw new RobotRuntimeException("Driver to be set is null.");
    }
  }

  /**
   * Adds the driver to the list of drivers if he does not exists
   *
   * @param driver the driver to add
   */
  public final void addDriver(final Driver driver) {
    if (this.drivers.stream()
        .anyMatch(driver1 -> driver1.getDriverName().equals(driver.getDriverName()))) {
      throw new RobotRuntimeException("Driver named " + driver.getDriverName() + " already exists");
    }

    this.drivers.add(driver);
  }

  /**
   * Return the driver given his name
   *
   * @param driverName the name to search for
   * @return the driver who has the driverName
   */
  public final Driver getDriver(final String driverName) {
    final Optional<Driver> driverOptional = this.drivers.stream()
        .filter(driver -> driver.getDriverName().equals(driverName)).findFirst();

    if (driverOptional.isPresent()) {
      return driverOptional.get();
    }

    throw new RobotRuntimeException(
        "Driver named " + driverName + " does not exist. Did you add him to the drivers lists");
  }

  @Override
  protected void initialize() {

  }

  public final void tankDrive() {
    this.driveTrain.setPowers(DriveCommand.getLeftThrottle(), DriveCommand.getRightThrottle());
  }

  @Override
  protected final void execute() {
    // if() shifter code

    this.tankDrive();
  }

  @Override
  protected final boolean isFinished() {
    return false;
  }

  @Override
  public final String toString() {
    return "DriveCommand{" +
        "currentDriver=" + currentDriver +
        '}';
  }
}
