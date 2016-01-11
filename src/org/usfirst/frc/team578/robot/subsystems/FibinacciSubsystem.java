package org.usfirst.frc.team578.robot.subsystems;
import java.util.logging.Level;

import org.usfirst.frc.team578.robot.Robot;
import org.usfirst.frc.team578.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;

//Known to the general public as the FibonacciSubsystem
public class FibinacciSubsystem extends SubsystemBase {

	private DigitalInput fibinacciSwitch;
	private Relay fibinacciRelay;
	//private boolean extending = false;

	/**
	 * Constructor for the subsystem.
	 */
	public FibinacciSubsystem(boolean enable) {
		super(enable);
		
		if (!enabled)
			return;

		fibinacciSwitch = new DigitalInput(RobotMap.FIBINACCI_SWITCH);
		fibinacciRelay = new Relay(RobotMap.FIBINACCI_WHEEL);

		setDirection();
	}

	@Override
	protected void initDefaultCommand() {

	}

	/**
	 * Extends the fibinacci. Sets the relay value to on and sets the value of 
	 * extending to true.
	 */
	public void extendFibinacci() {
		if (!enabled)
			return;
		
		Robot.log.write(Level.WARNING, "Extending Fibinacci");
		fibinacciRelay.set(Relay.Value.kOn);
	}

	/**
	 * Gets the state of the fibinacci relay. May be
	 * null if subsystem is not enabled!
	 * @return current state or null if not enabled.
	 */
	public Value getFibinacciStatus()
	{
		if (!enabled)
			return null;
		
		return fibinacciRelay.get();
	}

	/**
	 * Returns the fibinacciSwitch state. False if not enabled.
	 * @return
	 */
	public boolean readSwitch() {
		if (!enabled)
			return false;
		
		return fibinacciSwitch.get();
	}

	/**
	 * Has the direction set to forward.
	 */
	public void setDirection() {
		if (!enabled)
			return;
		
		fibinacciRelay.setDirection(Relay.Direction.kForward);
	}

	/**
	 * Sets the fibinacci to off.
	 */
	public void stopFibinacci() {
		if (!enabled)
			return;
		
		Robot.log.write(Level.WARNING, "Stopping Fibinacci");
		//MAY STOP IMMEDIATELY BE PREPARED TO ADD A DELAY
		fibinacciRelay.set(Relay.Value.kOff);
	}
}