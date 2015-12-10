package edu.nr.robotics;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	public static final int TALON_LEFT_A = 16;
	public static final int TALON_LEFT_B = 4;
	public static final int TALON_RIGHT_A = 14;
	public static final int TALON_RIGHT_B = 15;
	
	public static final int ENCODER_LEFT_A = 9;
	public static final int ENCODER_LEFT_B = 8;
	public static final int ENCODER_RIGHT_A = 7;
	public static final int ENCODER_RIGHT_B = 6;
	public static final double MAX_ENCODER_RATE = 20; //Rotations/second
}
