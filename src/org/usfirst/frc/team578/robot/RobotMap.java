package org.usfirst.frc.team578.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	
	//DriveTrain talon ID's
	public static final int FRONT_LEFT_TALON = 16;
	public static final int BACK_LEFT_TALON = 13;
	public static final int BACK_RIGHT_TALON = 1;
	public static final int FRONT_RIGHT_TALON = 3;
	
	//Driver Input ID's
	
	//Joysticks
	public static final int LEFT_JOYSTICK = 1;
	public static final int RIGHT_JOYSTICK = 2;
	public static final int JOYSTICK_STOP_ELEVATOR = 7;

	//Gamepad
	public static final int CONTROLLER_B6 = 6;
	public static final int CONTROLLER_B8 = 8;
	public static final int CONTROLLER_B9 = 9;
	public static final int CONTROLLER_B10 = 10;
	public static final int GAMEPAD = 0;
	
	//Elevator ID's
	public static final int ELEVATOR_TALON = 15;
	public static final int ELEVATOR_LEVEL_TWO_SWITCH = 8;
	public static final int ELEVATOR_LEVEL_THREE_SWITCH = 9;
	
	//Fibinacci Wheel ID's
	public static final int FIBINACCI_WHEEL = 0;
	
	public static final int FIBINACCI_SWITCH = 1;

	//Intake ID's
	public static final int LEFT_INTAKE_TALON = 14;
	public static final int RIGHT_INTAKE_TALON = 2;	
	
	//Misc
	public static final int TOTE_SENSOR = 0;
	
	// camera related
	public static final int btCamCenter = 1;
	public static final int btCamRight = 2;

	public static final String camNameCenter = "cam0";
	public static final String camNameRight = "cam1";
	public static final int imgQuality = 60;
	
}