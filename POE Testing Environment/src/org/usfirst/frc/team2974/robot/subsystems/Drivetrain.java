package org.usfirst.frc.team2974.robot.subsystems;

import org.usfirst.frc.team2974.robot.comands.Drive;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Drivetrain extends Subsystem {

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new Drive());
  }
}
