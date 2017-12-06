package org.usfirst.frc.team2974.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2974.robot.Input;
import org.usfirst.frc.team2974.robot.Robot;
import org.usfirst.frc.team2974.robot.io.logitech.GamepadButton;
import org.usfirst.frc.team2974.robot.subsystem.GearIntake;

import static org.usfirst.frc.team2974.robot.Robot.gearIntake;

/**
 * Gear Intake Command
 */
public class GearIntakeCommand extends Command {

    public GearIntakeCommand() {
        requires(gearIntake);
    }

    @Override
    protected final void execute() {
        if (Input.gamepad.buttonPressed(GamepadButton._1)) {
            gearIntake.setDeployed(true);
        } else if (Input.gamepad.buttonPressed(GamepadButton._2)) {
            gearIntake.setDeployed(false);
        }
    }

    @Override
    protected final boolean isFinished() {
        return false;
    }
}
