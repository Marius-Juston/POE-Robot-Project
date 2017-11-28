package org.usfirst.frc.team2974.robot.controllers;

import java.util.TimerTask;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.usfirst.frc.team2974.robot.manager.SubsystemManager;
import org.usfirst.frc.team2974.robot.subsystem.DriveTrain;

import edu.wpi.first.wpilibj.Timer;

public class MotionProfileController{
	// PID values
	private double kV=0, // what we want to change the voltage by to get the motion we want
			kK=0, // up/down translation
			kA=0, // FIXME?? WHAT IS THIS
			kP=0; // what we want to change the power by to get the moton we want
	
	private Kinematics currentKinematics = null; // does all the math
	private KinematicPose staticKinematicPose; // has the vectors that we use in the calculations
	private BlockingDeque<MotionProvider> motions = new LinkedBlockingDeque<MotionProvider>(); // motions that we want to do
	private PoseProvider poseProvider; // wheel positions and pose
	private java.util.Timer controller; // schedules all of the motions to be run, TIMER
	private double period; // in ms, time we want the motion to take
	private boolean isEnabled; // yeah, this
	private final int nPoints = 50; // number of motions
	
	private class MPCTask extends TimerTask{
		
		@Override
		public void run() {
			calculate();
		}
		
	}
	
	public MotionProfileController(PoseProvider poseProvider, double period){
		
		this.poseProvider = poseProvider;
		this.period = period; // in ms, time we want the motion to take
		
		controller = new java.util.Timer();
		
		controller.schedule(new MPCTask(), 0L, (long)(period*1000));

		staticKinematicPose = Kinematics.staticPose(poseProvider.getPose(), poseProvider.getWheelPositions(), Timer.getFPGATimestamp());
	}
	
	/**
	 * Stops the motion
	 */
	public void free() {
		controller.cancel();
		//RobotLoggerManager.setFileHandlerInstance("robot.controller").info("MPCTask is destroyed.");
	}
	
	public synchronized void addMotion(MotionProvider motion) {
		motions.addLast(motion);
		System.out.println("added motion" + motion.toString());
	}
	
	public synchronized void enable() {
		System.out.println(String.format("kK=%f, kV=%f, kA=%f, kP=%f", kK, kV, kA, kP));
		MotionProvider newMotion = motions.poll();
		if (newMotion != null) {
			currentKinematics = new Kinematics(newMotion, poseProvider.getWheelPositions(), Timer.getFPGATimestamp(), 0, 0, nPoints);
//			System.out.println("starting new motion:" + currentKinematics.toString());
			isEnabled = true;
		}
	}
	
	/**
	 * Hard stop
	 */
	public synchronized void cancel() {
		isEnabled = false;
		currentKinematics = null;
		motions.clear();
		SubsystemManager.getSubsystem(DriveTrain.class).setPowers(0, 0);
	}
	
	public boolean getEnabled() {
		return isEnabled;
	}
	
	public synchronized double getKV() {
		return kV;
	}
	
	public synchronized void setKV(double kV) {
		this.kV = kV;
	}	

	public synchronized double getKK() {
		return kK;
	}
	
	public synchronized void setKK(double kK) {
		this.kK = kK;
	}
	
	public synchronized double getKA() {
		return kA;
	}
	
	public synchronized void setKA(double kA) {
		this.kA = kA;
	}
		
	public synchronized double getKP() {
		return kP;
	}
	
	public synchronized void setKP(double kP) {
		this.kP = kP;
	}
	
	/**
	 * This is the PID loop, accounts for distance error.
	 */
	private void calculate() {
		
		double leftPower = 0;
		double rightPower = 0;
		boolean enabled;
		
		synchronized (this) {
			enabled = this.isEnabled;
		}
		
		if(enabled) {
			
			double time = Timer.getFPGATimestamp();
		
			RobotPair wheelPositions = poseProvider.getWheelPositions();
			
			KinematicPose kinematicPose;
			if (currentKinematics != null) {
				kinematicPose = currentKinematics.interpolatePose(time);
			} else {
				kinematicPose = staticKinematicPose;
			}
			
//			System.out.println("time:" + time+ " " + wheelPositions + " " + kinematicPose);
			synchronized (this) {
				//feed forward
				leftPower += (kV * kinematicPose.left.velocity + kK) + kA * kinematicPose.left.acceleration;
				rightPower += (kV * kinematicPose.right.velocity + kK) + kA * kinematicPose.right.acceleration;
				//feed back		
				leftPower += kP * (kinematicPose.left.length - wheelPositions.left);
				rightPower += kP * (kinematicPose.right.length - wheelPositions.right);
				
			}
			
			leftPower = Math.max(-1, Math.min(1, leftPower));
			rightPower = Math.max(-1, Math.min(1, rightPower));
			
//			System.out.println(String.format("LP=%f,RP=%f, err_l=%f, err_r=%f", leftPower,rightPower, 
//					kinematicPose.left.l - wheelPositions.left,
//					kinematicPose.right.l - wheelPositions.right));

			SubsystemManager.getSubsystem(DriveTrain.class).setPowers(leftPower, rightPower);
			
			if(kinematicPose.isFinished) {
				MotionProvider newMotion = motions.pollFirst();
				if (newMotion != null) {
					currentKinematics = new Kinematics(newMotion, currentKinematics.getWheelPositions(), 
							                           currentKinematics.getTime(), 0, 0, nPoints);
//					System.out.println("starting new motion:" + currentKinematics.toString());
				}
				else {
					staticKinematicPose = Kinematics.staticPose(currentKinematics.getPose(), currentKinematics.getWheelPositions(), 
							                                    currentKinematics.getTime());
					currentKinematics = null;
				}
			}			
		}
	}
	public synchronized boolean isFinished(){
		return currentKinematics == null;
	}
}