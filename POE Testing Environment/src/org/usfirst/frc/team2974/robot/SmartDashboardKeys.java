package org.usfirst.frc.team2974.robot;

import org.usfirst.frc.team2974.robot.subsystems.DriveTrain;

// TODO: this should probably go in the manager
public class SmartDashboardKeys {
  
// Here we want to work in a way so that when the SubsystemManager.getSubsystem(DriveTrain.class).getLeftMotorPower() value changes the smartdashboard value also changes
  public final SmartDashboardProperty<Number> leftEncoder = SmartDashboardManager.createBindKey("Left Motor Power", SubsystemManager.getSubsystem(DriveTrain.class).getLeftMotorPower(), 0);
}
