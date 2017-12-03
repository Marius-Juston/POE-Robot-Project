package org.usfirst.frc.team2974.robot.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team2974.robot.RobotMap;
import org.usfirst.frc.team2974.robot.command.GearIntakeCommand;

/**
 * Gear Intake
 */
public class GearIntake extends Subsystem {

  private final Solenoid piston;

  private final DigitalInput gearSensor;

  public GearIntake() {
    piston = RobotMap.gearIntakeSolenoid;
    gearSensor = RobotMap.gearSensor;
  }

  @Override
  protected final void initDefaultCommand() {
    setDefaultCommand(new GearIntakeCommand());
  }

  public final void setDeployed(boolean deployed) {
    piston.set(deployed);
  }

  public final void toggleDeployed() {
    piston.set(!piston.get());
  }

  public final boolean hasGear() {
    return !gearSensor.get();
  }
}
