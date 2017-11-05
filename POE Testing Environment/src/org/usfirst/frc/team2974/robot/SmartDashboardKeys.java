package org.usfirst.frc.team2974.robot;

import org.usfirst.frc.team2974.robot.subsystems.DriveTrain;

public class SmartDashboardKeys {
  
// Here we want to work in a way so that when the SubsystemManager.getSubsystem(DriveTrain.class).getLeftMotorPower() value changes the smartdashboard value also changes
  public final SmartDashboardKey<Number> leftEncoder = SmartDashboardManager.<Number>createBindKey("Left Motor Power", SubsystemManager.getSubsystem(DriveTrain.class).getLeftMotorPower(), 0);
}
