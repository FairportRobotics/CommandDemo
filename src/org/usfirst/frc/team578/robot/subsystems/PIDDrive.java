package org.usfirst.frc.team578.robot.subsystems;

import org.usfirst.frc.team578.robot.RobotMap;
import org.usfirst.frc.team578.robot.commands.PIDDriveCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * For use with PID / encoder testing. Not for production use.
 *
 */
@Deprecated
public class PIDDrive extends SubsystemBase {

	private CANTalon talon;

	public PIDDrive(boolean enable)
	{
		super(enable);

		if (!enable)
			return;

		talon = initializeTalon(RobotMap.BACK_RIGHT_TALON);
		//LiveWindow.addActuator("PIDDrive", "Talon", (LiveWindowSendable) talon);
	}

	public void drive(double val)
	{
		if (!enabled)
			return;
		
		talon.set(val * 500);
	}

	public void writeStatus()
	{
		if (!enabled)
			return;
		
		SmartDashboard.putString("TalonStatus: ", "P: " + talon.getP() 
				+ " I: " + talon.getI()
				+ " D: " + talon.getD()
				+ " Speed:" + talon.getSpeed()
				+ " EncVelocity:" + talon.getEncVelocity()
				+ " VVelocity:" + talon.getAnalogInVelocity()
				+ " Set: " + talon.getSetpoint());

	}

	@Override
	protected void initDefaultCommand() {
		//setDefaultCommand(new PIDDriveCommand());
	}

	private CANTalon initializeTalon(int channel) {
		CANTalon talon = new CANTalon(channel);
		talon.changeControlMode(ControlMode.Speed);
		talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		talon.setPID(1, 0.01, 0.85, 1.5, 0, 0, 0); //TUNED VALUES FOR FRONT RIGHT WHEEL
		//talon.setPID(1.23, 0, 0, 1.33, 0, 10, 0); EXACT VALUES FOR OLD TESTBOARD PID DO NOT CHANGE
		talon.enableControl();
		return talon;
	}

}
