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
    gearIntake = SubsystemManager.getSubsystem(GearIntake.class);

    requires(gearIntake);
  }

  @Override
  protected final void execute() {
    if (Input.gamepad.buttonPressed(GamepadButton._1)) {
      gearIntake.toggleDeployed();
    }
  }

  @Override
  protected final boolean isFinished() {
    return false;
  }
}
