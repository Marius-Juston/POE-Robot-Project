package org.usfirst.frc.team2974.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2974.robot.Input;
import org.usfirst.frc.team2974.robot.io.logitech.GamepadButton;
import org.usfirst.frc.team2974.robot.manager.SubsystemManager;
import org.usfirst.frc.team2974.robot.subsystem.GearIntake;

/**
 * Gear Intake Command
 */
public class GearIntakeCommand extends Command {

  private final GearIntake gearIntake;

  public GearIntakeCommand() {
    super("Gear Intake");
    this.gearIntake = SubsystemManager.getSubsystem(GearIntake.class);

    this.requires(this.gearIntake);
  }

  @Override
  protected void execute() {
    if (Input.gamepad.buttonPressed(GamepadButton._1)) {
      this.gearIntake.toggleDeployed();
    }
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
