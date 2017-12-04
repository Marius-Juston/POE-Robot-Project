package org.usfirst.frc.team2974.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.usfirst.frc.team2974.robot.Input;
import org.usfirst.frc.team2974.robot.Robot;
import org.usfirst.frc.team2974.robot.RobotConfiguration;
import org.usfirst.frc.team2974.robot.exception.RobotRuntimeException;
import org.usfirst.frc.team2974.robot.io.Driver;
import org.usfirst.frc.team2974.robot.subsystem.DriveTrain;

public class DriveCommand extends Command {

    private final Set<Driver> drivers; // TODO: actually use
    private final DriveTrain driveTrain;
    private Driver currentDriver;

    public DriveCommand(final Driver initialDriver) {
        super("DriveCommand");

        drivers = new HashSet<>(4);

        setCurrentDriver(initialDriver);

        driveTrain = Robot.driveTrain;

        requires(driveTrain);
    }

    public DriveCommand() {
        this(Driver.DEFAULT_DRIVER);
    }

    public static double getLeftThrottle() {
        double leftY = -Input.leftJoystick.getY();
        if (Math.abs(leftY) < RobotConfiguration.LEFT_JOYSTICK_THRESHOLD) {
            return 0;
        }

        return leftY;
    }

    public static double getRightThrottle() {
        double rightY = -Input.rightJoystick.getY();
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
        return currentDriver;
    }

    /**
     * Changes the current driver all the while reinitializing the buttons
     *
     * @param driver the new current driver
     */
    public final void setCurrentDriver(Driver driver) {
        if (driver != null) {
            if (!driver.equals(currentDriver)) {
                currentDriver = driver;
                driver.initButtons();
            }
        } else {
            throw new RobotRuntimeException("Driver to be set is null.");
        }
    }

    /**
     * Adds the driver to the list of drivers if he does not exists
     *
     * @param driver the driver to add
     */
    public final void addDriver(Driver driver) {
        if (drivers.stream()
            .anyMatch(driver1 -> driver1.getDriverName().equals(driver.getDriverName()))) {
            throw new RobotRuntimeException(
                "Driver named " + driver.getDriverName() + " already exists");
        }

        drivers.add(driver);
    }

    /**
     * Return the driver given his name
     *
     * @param driverName the name to search for
     * @return the driver who has the driverName
     */
    public final Driver getDriver(String driverName) {
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

    public final void tankDrive() {
        driveTrain.setPowers(DriveCommand.getLeftThrottle(), DriveCommand.getRightThrottle());
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
    protected void end() {
        driveTrain.setPowers(0, 0);
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    public final String toString() {
        return "DriveCommand{" +
            "currentDriver=" + currentDriver +
            '}';
    }
}
