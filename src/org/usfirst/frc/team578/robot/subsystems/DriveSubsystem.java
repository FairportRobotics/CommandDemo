package org.usfirst.frc.team578.robot.subsystems;

import org.usfirst.frc.team578.robot.RobotMap;
//import org.usfirst.frc.team578.robot.commands.DriveCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveSubsystem extends SubsystemBase {

	private CANTalon frontLeftTalon;
	private CANTalon frontRightTalon;
	private CANTalon backLeftTalon;
	private CANTalon backRightTalon;
	
	private static final int DRIVE_SCALING_FACTOR = 1;
	
	public DriveSubsystem(boolean enable)
	{
		super(enable);
		
		if (!enabled)
			return;

		frontLeftTalon = initializeTalon(RobotMap.FRONT_LEFT_TALON);
		frontRightTalon = initializeTalon(RobotMap.FRONT_RIGHT_TALON);
		backLeftTalon = initializeTalon(RobotMap.BACK_LEFT_TALON);
		backRightTalon = initializeTalon(RobotMap.BACK_RIGHT_TALON);
	}

	/**
	 *  Sets the default command to the DriveCommand, so that it will respond
	 *  to joystick input at any time during Teleop.
	 */
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		//setDefaultCommand(new DriveCommand());
	}

	public void writeStatus()
	{
		if (!enabled)
			return;

		SmartDashboard.putString("BackLeft", backLeftTalon.getSpeed() + " " + backLeftTalon.getEncVelocity());
		SmartDashboard.putString("BackRight", backRightTalon.getSpeed() + " " + backRightTalon.getEncVelocity());
		SmartDashboard.putString("FrontLeft", frontLeftTalon.getSpeed() + " " + frontLeftTalon.getEncVelocity());
		SmartDashboard.putString("FrontRight", frontRightTalon.getSpeed() + " " + frontRightTalon.getEncVelocity());
	}

	/**
	 * <p>Deprecated as the robot no longer has a mecanum drive.</p>
	 * 
	 * <p>
	 * Drives the robot using joystick input values, and is designed for mecanum drive.
	 *  Values will be scaled before being assigned as motor speeds. Use 
	 * {@link #driveMotors(double, double, double, double)}
	 * to set actual motor values. 
	 * </p>
	 * 
	 * <p>
	 * Accepts double values -1 to 1.
	 * </p>
	 * 
	 * @param leftX		left joystick's x value
	 * @param leftY 	left joystick's y value
	 * @param rightX 	right joystick's x value
	 * @param rightY 	right joystick's y value
	 */
	@Deprecated
	public void driveMecanumJoysticks(double leftX, double leftY, double rightX, double rightY) 
	{
		if (!enabled)
			return;
		
		if (leftX < 0.3 && leftX > -0.3) {
			frontLeftTalon.set(-leftY * DRIVE_SCALING_FACTOR);
			backLeftTalon.set(-leftY * DRIVE_SCALING_FACTOR);
		} else {
			frontLeftTalon.set(-leftX * DRIVE_SCALING_FACTOR);
			backLeftTalon.set(leftX * DRIVE_SCALING_FACTOR);
		}

		if (rightX < 0.3 && rightX > -0.3) {
			frontRightTalon.set(rightY * DRIVE_SCALING_FACTOR);
			backRightTalon.set(rightY * DRIVE_SCALING_FACTOR);
		} else {
			frontRightTalon.set(-rightX * DRIVE_SCALING_FACTOR);
			backRightTalon.set(rightX * DRIVE_SCALING_FACTOR);
		}
	}
	
	/**
	 * <p>
	 * Drives the robot using joystick input values, and is designed for tank drive.
	 *  Values will be scaled before being assigned as motor speeds. Use 
	 * {@link #driveMotors(double, double, double, double)}
	 * to set actual motor values. 
	 * </p>
	 * 
	 * <p>
	 * Accepts double values -1 to 1.
	 * </p>
	 * 
	 * @param leftX		left joystick's x value
	 * @param leftY 	left joystick's y value
	 * @param rightX 	right joystick's x value
	 * @param rightY 	right joystick's y value
	 */
	public void driveTankJoysticks(double leftX, double leftY, double rightX, double rightY)
	{
		if(!enabled)
			return;
		
		frontLeftTalon.set(-leftY * DRIVE_SCALING_FACTOR);
		backLeftTalon.set(-leftY * DRIVE_SCALING_FACTOR);
		frontRightTalon.set(rightY * DRIVE_SCALING_FACTOR);
		backRightTalon.set(rightY * DRIVE_SCALING_FACTOR);
	}

	/**
	 * 
	 * @param channel 	The channel that the Talon will be running on.
	 * @return 		Returns the newly created Talon object.
	 */
	private CANTalon initializeTalon(int channel) {
		CANTalon talon = new CANTalon(channel);
		talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		//talon.changeControlMode(ControlMode.Speed);
		//talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		//talon.setPID(1, 0.01, 0.85, 1.5, 0, 0, 0);
		talon.enableControl();
		return talon;
	}


	/**
	 * <p>
	 * Sets drive train motor speeds after scaling the values. Use
	 * {@link #driveTankJoysticks(double, double, double, double)} for joysticks.
	 * </p>
	 * <p>
	 * Accepts double values from -1 to 1.
	 * </p>
	 * 
	 * @param fr
	 * @param fl
	 * @param br
	 * @param bl
	 */
	public void driveMotors(double fr, double fl, double br, double bl) {

		if (!enabled)
			return;

		backLeftTalon.set(bl * DRIVE_SCALING_FACTOR);
		backRightTalon.set(-br * DRIVE_SCALING_FACTOR);
		frontLeftTalon.set(fl * DRIVE_SCALING_FACTOR);
		frontRightTalon.set(-fr * DRIVE_SCALING_FACTOR);
	}
}