package org.usfirst.frc.team578.robot.subsystems;

import org.usfirst.frc.team578.robot.RobotMap;
//import org.usfirst.frc.team578.robot.commands.PotTestCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * For testing a potentiometer with a CANTalon. Not for production use.
 *
 */
@Deprecated
public class POTTest extends SubsystemBase {

	CANTalon talon;
	
	public POTTest(boolean enable) {
		super(enable);

		if (!enable)
			return;
		
		talon = initializeTalon(RobotMap.ELEVATOR_TALON);
	}

	@Override
	protected void initDefaultCommand() {
		//setDefaultCommand(new PotTestCommand());
	}

	public void writeStatus()
	{
		if (!enabled)
			return;
		
		SmartDashboard.putString("TalonStatus POT: ", "P: " + talon.getP() 
				+ " I: " + talon.getI()
				+ " D: " + talon.getD()
				+ " Speed:" + talon.getSpeed()
				+ " EncVelocity:" + talon.getEncVelocity()
				+ " VVelocity:" + talon.getAnalogInVelocity()
				+ " Position: " + talon.getPosition()
				+ " Set: " + talon.getSetpoint());

	}
	
	private CANTalon initializeTalon(int channel) {
		CANTalon talon = new CANTalon(channel);
		talon.changeControlMode(CANTalon.TalonControlMode.Position);
		talon.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
		//talon.setPID(1, 0.01, 0.85, 1.5, 0, 0, 0); //TUNED VALUES FOR FRONT RIGHT WHEEL
		//talon.setPID(1.23, 0, 0, 1.33, 0, 10, 0); EXACT VALUES FOR OLD TESTBOARD PID DO NOT CHANGE
		talon.enableControl();
		return talon;
	}

}
