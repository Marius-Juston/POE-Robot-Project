package org.usfirst.frc.team2974.robot.controllers;

/**
 * This is the pair of the left and right wheels. DOUBLE VALUES
 */
public class RobotPair {
	
	public final double left; // location of left side
	public final double right; // ^^ right ^
	
	public RobotPair(double left, double right){
		this.left = left;
		this.right = right;
	}
	
	// location of center
	public double mean(){
		return (this.left + this.right) / 2;
	}
	
	@Override
	public String toString() {
		return String.format("%f, %f", left, right);
	}

}
