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

  private Solenoid piston;

  private DigitalInput gearSensor;

  public GearIntake() {
    super("Gear Intake");

    piston = RobotMap.gearIntakeSolenoid;
    gearSensor = RobotMap.gearSensor;
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new GearIntakeCommand());
  }

  public void setDeployed(boolean deployed) {
    piston.set(deployed);
  }

  public void toggleDeployed() {
    piston.set(!piston.get());
  }

  public boolean hasGear() {
    return !gearSensor.get();
  }
}
