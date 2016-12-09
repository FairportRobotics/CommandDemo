package org.usfirst.frc.team578.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team578.robot.*;


public class DriverMechatronSubsystem extends Subsystem {
	
	private CANTalon frontLeftTalon;
	private CANTalon frontRightTalon;
	private CANTalon backLeftTalon;
	private CANTalon backRightTalon;
	
	private static final double HOW_FAST_THE_WHEELS_ARE_GOING=0.6;
	
	public DriverMechatronSubsystem(){
		
		frontLeftTalon = initializeTalon(RobotMap.FRONT_LEFT_TALON);
		frontRightTalon = initializeTalon(RobotMap.FRONT_RIGHT_TALON);
		backLeftTalon = initializeTalon(RobotMap.BACK_LEFT_TALON);
		backRightTalon = initializeTalon(RobotMap.BACK_RIGHT_TALON);
	}
	
	private CANTalon initializeTalon(int channel) {
		CANTalon talon = new CANTalon(channel);
		talon.changeControlMode(CANTalon.ControlMode.PercentVbus);
		//talon.changeControlMode(ControlMode.Speed);
		//talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		//talon.setPID(1, 0.01, 0.85, 1.5, 0, 0, 0);
		talon.enableControl();
		return talon;
	}

	public void whereWeActuallyTellItToDoStuff(int direction){
		
		switch(direction)
		{
		case 0:
			
			break;
		case 1:
			
			break;
		case 2:
			
			break;
		case 3:
			
			break;
		}
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

}












