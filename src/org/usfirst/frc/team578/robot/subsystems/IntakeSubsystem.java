package org.usfirst.frc.team578.robot.subsystems;
import java.util.logging.Level;

import org.usfirst.frc.team578.robot.Robot;
import org.usfirst.frc.team578.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;

/**
 * Subsystem to run the intake wheels for consuming and ejecting totes
 *
 */
public class IntakeSubsystem extends SubsystemBase {
	
	private CANTalon leftIntakeTalon;
	private CANTalon rightIntakeTalon;
	
	public IntakeSubsystem(boolean enable)
	{
		super(enable);
		
		if(!enabled)
			return;
		
		leftIntakeTalon = new CANTalon(RobotMap.LEFT_INTAKE_TALON);
		rightIntakeTalon = new CANTalon(RobotMap.RIGHT_INTAKE_TALON);
	}

	
	/**
	 * Nothing Happening Here!
	 */
	@Override
	protected void initDefaultCommand() {
		
	}
	
	/**
	 * Gets the speed setting of the left intake Talon. Returns 0 if not enabled.
	 * @return the speed setting 0 if not enabled.
	 */
	public double getSpeed()
	{
		if (!enabled)
			return 0;
		
		return leftIntakeTalon.get();
	}
	
	/**
	 * The Speed of the intake Talons / wheels is being set!
	 */
	public void spinIntake() {
		if (!enabled)
			return;
		
		Robot.log.write(Level.INFO, "Spinning the wheels for intake");
		leftIntakeTalon.set(1);
		rightIntakeTalon.set(-1);
	}
	
	/**
	 * The Speed of the output Talons / wheels is being set!
	 */
	public void spinOutput() {
		if (!enabled)
			return;
		
		Robot.log.write(Level.INFO, "Spinning the wheels for output");
		leftIntakeTalon.set(-1);
		rightIntakeTalon.set(1);
	}
	
	/**
	 * Stops the Talons / wheels from spinning!
	 */
	public void spinStop() {
		if (!enabled)
			return;
		
		Robot.log.write(Level.INFO, "Stopping the wheels");
		leftIntakeTalon.set(0);
		rightIntakeTalon.set(0);
	}
}