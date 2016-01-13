package org.usfirst.frc.team578.robot.subsystems;

import java.util.logging.Level;

import org.usfirst.frc.team578.robot.Robot;
import org.usfirst.frc.team578.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Original elevator control system, using physical digital input switches to establish
 * elevator positions.
 * @author Brian
 *
 */
@Deprecated
public class ElevatorSwitchSubsystem extends SubsystemBase {

	//	DigitalInput levelOne
	private DigitalInput levelTwo;
	//DigitalInput levelThree
	private DigitalInput levelThree;
	//	DigitalInput levelFour

	//Reverse limit switch is the top
	//Forward limit switch is the bottom

	private CANTalon elevatorTalon;

	private int currLevel = 1;
	private int desiredLevel = 1;

	private long talonStartTime;

	private boolean timedOut = false;
	
	private static final double ELEVATOR_SPEED = 0.5;

	/**
	 * Constructs the subsystem, including 2 digital inputs for switches,
	 * and the Talon for the elevator. 
	 */
	public ElevatorSwitchSubsystem(boolean enable) 
	{
		super(enable);
		
		if (!enabled)
			return;

		levelTwo = new DigitalInput(RobotMap.ELEVATOR_LEVEL_TWO_SWITCH);
		levelThree = new DigitalInput(RobotMap.ELEVATOR_LEVEL_THREE_SWITCH);

		elevatorTalon = new CANTalon(RobotMap.ELEVATOR_TALON);

		//elevatorTalon.ConfigFwdLimitSwitchNormallyOpen(false);
		//elevatorTalon.ConfigRevLimitSwitchNormallyOpen(false);
		elevatorTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
	}

	/**
	 * Writes the position of the elevator to the SmartDashboard
	 */
	public void writeStatus()
	{
		if (!enabled)
			return;
		
		//		SmartDashboard.putBoolean("Fwd Closed", elevatorTalon.isFwdLimitSwitchClosed());
		//		SmartDashboard.putBoolean("Rev Closed", elevatorTalon.isRevLimitSwitchClosed());
		//		SmartDashboard.putNumber("curr level", currLevel);

		SmartDashboard.putString("Limit", "Three:" + levelThree.get() + " Two:" + levelTwo.get());

		switch (currLevel)
		{
		case 1: SmartDashboard.putString("Elevator Level", "1: BOTTOM"); break;
		case 2: SmartDashboard.putString("Elevator Level", "2: LOW"); break;
		case 3: SmartDashboard.putString("Elevator Level", "3: HIGH"); break;
		case 4: SmartDashboard.putString("Elevator Level", "4: TOP"); break;
		}
	}

	@Override
	protected void initDefaultCommand() {

	}

	/**
	 * Gets the current position of the elevator (At switches)
	 * @return
	 */
	public int getCurrentLevel(){
		return currLevel;
	}

	public void setLevelTest(int level)
	{
		if (!enabled)
			return;

		desiredLevel = level;
		
		if (level == 4)
		{
			elevatorTalon.set(ELEVATOR_SPEED);
		}
		else if (level == 1)
		{
			elevatorTalon.set(-ELEVATOR_SPEED);
		}
		else if (level == 2)
		{
			elevatorTalon.set(0);
		}
	}
	
	public void stop()
	{
		if (!enabled)
			return;
		
		elevatorTalon.set(0);
		Robot.log.write(Level.WARNING, "Elevator manually stopped!");
	}

	/**
	 * Sets the level of the elevator to a specific position (1-4)
	 * @param level
	 */
	public void setLevel(int level) 
	{
		if (!enabled)
			return;

		//DRIVING THE TALON WITH A NEGATIVE VALUE
		//MAKES IT GO UP! POSITIVE GOES DOWN!
		
		SmartDashboard.putNumber("Desired Level: ", level);

		int offset = currLevel - level;

		if (!timedOut)
		{
			if (offset > 0) {
				elevatorTalon.set(ELEVATOR_SPEED);
			} else if (offset < 0) {
				elevatorTalon.set(-ELEVATOR_SPEED);
			}

			if(level==4)
			{
				elevatorTalon.set(-ELEVATOR_SPEED);
			}
			else if(level==1)
			{
				elevatorTalon.set(ELEVATOR_SPEED);
			}
		}

		if (!elevatorTalon.isFwdLimitSwitchClosed())
		{
			currLevel = 1;
			Robot.log.write(Level.INFO, "Elevator detected at level #1");
		}
		
		if (levelTwo.get())
		{
			currLevel = 2;
			Robot.log.write(Level.INFO, "Elevator detected at level #2");
		}
		
		if (levelThree.get())
		{
			currLevel = 3;
			Robot.log.write(Level.INFO, "Elevator detected at level #3");
		}
		
		if (!elevatorTalon.isRevLimitSwitchClosed())
		{
			currLevel = 4;
			Robot.log.write(Level.INFO, "Elevator detected at level #4");
		}
		
		if(level == 1){
			if(!elevatorTalon.isFwdLimitSwitchClosed()){
				elevatorTalon.set(0);
			}
		}else if(level == 2){
			if(levelTwo.get()){
				elevatorTalon.set(0);
			}else if(currLevel == level)
			{
				elevatorTalon.set(-ELEVATOR_SPEED);
			}
		}else if(level == 3){
			if(levelThree.get()){
				elevatorTalon.set(0);
			}else if(currLevel == level)
			{
				elevatorTalon.set(-ELEVATOR_SPEED);
			}
		}else if(level == 4){
			if(!elevatorTalon.isRevLimitSwitchClosed()){
				elevatorTalon.set(0);
			}else if(currLevel == level)
			{
				elevatorTalon.set(-ELEVATOR_SPEED);
			}
		}
	}

	public void update() {
		if (!enabled)
			return;
		
		if(System.currentTimeMillis()-talonStartTime>5000)
		{
			timedOut = true;
			elevatorTalon.set(0);
			Robot.log.write(Level.SEVERE, "Elevator talon has ran for >5 seconds and has timed out");
		}
	}

	public void start() {
		if (!enabled)
			return;
		
		timedOut = false;
		talonStartTime = System.currentTimeMillis();
	}

	public int getDesiredLevel() {
		return desiredLevel;
	}
}
