package org.usfirst.frc.team2974.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.usfirst.frc.team2974.robot.Input;
import org.usfirst.frc.team2974.robot.manager.SubsystemManager;
import org.usfirst.frc.team2974.robot.io.Driver;
import org.usfirst.frc.team2974.robot.exception.RobotRuntimeException;
import org.usfirst.frc.team2974.robot.subsystem.DriveTrain;

public class DriveCommand extends Command {

  private final Set<Driver> drivers;
  private Driver currentDriver;

  private DriveTrain driveTrain;

  public DriveCommand(Driver initialDriver) {
    super("DriveCommand");

    drivers = new HashSet<>();

    setCurrentDriver(initialDriver);

    driveTrain = SubsystemManager.getSubsystem(DriveTrain.class);

    requires(driveTrain);
  }

  public DriveCommand() {
    this(Driver.DEFAULT_DRIVER);
  }

  /**
   * Returns current driver
   *
   * @return Returns current driver
   */
  public Driver getCurrentDriver() {
    return currentDriver;
  }

  /**
   * Changes the current driver all the while reinitializing the buttons
   *
   * @param driver the new current driver
   */
  public void setCurrentDriver(Driver driver) {
    if (driver != null && !driver.equals(currentDriver)) {
      currentDriver = driver;

      driver.initButtons();
    }else {
      throw new RobotRuntimeException("Driver to be set is null.");
    }
  }

  /**
   * Adds the driver to the list of drivers if he does not exists
   *
   * @param driver the driver to add
   */
  public void addDriver(Driver driver) {
    if (drivers.stream()
        .anyMatch(driver1 -> driver1.getDriverName().equals(driver.getDriverName()))) {
      throw new RobotRuntimeException("Driver named " + driver.getDriverName() + " already exists");
    }

    drivers.add(driver);
  }

  /**
   * Return the driver given his name
   *
   * @param driverName the name to search for
   * @return the driver who has the driverName
   */
  public Driver getDriver(String driverName) {
    Optional<Driver> driverOptional = drivers.stream()
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

  public double getLeftThrottle() {
    double leftY = -Input.leftJoystick.getY();
    if(Math.abs(leftY) < .3) {
      return 0;
    }

    return leftY;
  }

  public double getRightThrottle() {
    double rightY = -Input.rightJoystick.getY();
    if(Math.abs(rightY) < .3) {
      return 0;
    }

    return rightY;
  }

  public void tankDrive() {
    driveTrain.setPowers(getLeftThrottle(), getRightThrottle());
  }

  @Override
  protected void execute() {
    // if() shifter code

    tankDrive();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

}
