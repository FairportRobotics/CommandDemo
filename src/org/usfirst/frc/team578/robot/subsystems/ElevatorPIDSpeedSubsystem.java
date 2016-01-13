package org.usfirst.frc.team578.robot.subsystems;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;

import org.usfirst.frc.team578.robot.Robot;
import org.usfirst.frc.team578.robot.RobotMap;
//import org.usfirst.frc.team578.robot.commands.ElevatorMonitorCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Elevator control system using PID control to reach levels 2, 3, and for maintaining positions
 * 2,3, and 4 when under load of a tote (prevents slipping).
 * <br><br>
 * Switches to PercentVBus mode to reach levels 1, 4 (at limit switches) at a desired rate.
 *
 */
public class ElevatorPIDSpeedSubsystem extends SubsystemBase {

	//Reverse limit switch is the top
	//Forward limit switch is the bottom

	//Talons normally open

	//	private static final double LEVEL_ONE_MIN = 40;
	//	private static final double LEVEL_ONE_MAX = 50;
	//	private static final double LEVEL_TWO_MIN = 110;
	//	private static final double LEVEL_TWO_MAX = 120;
	//	private static final double LEVEL_THREE_MIN = 185;
	//	private static final double LEVEL_THREE_MAX = 195;
	//	private static final double LEVEL_FOUR_MIN = 450;
	//	private static final double LEVEL_FOUR_MAX = 460;

	//	private static final double LEVEL_TWO_OFFSET = 70;
	//	private static final double LEVEL_THREE_OFFSET = 75;
	//	private static final double LEVEL_FOUR_OFFSET = 256;


	//CONSTANTS
	private static final double[] OFFSETS = {0, 0, 30, 115, 256};

	private static final long TALON_STALL_TIMEOUT = 1000; //milliseconds

	private static final double TALON_SPEED_UP=-1.0; //negative and positive swapped?
	private static final double TALON_SPEED_DOWN = 0.75;
	private static final double TALON_SPEED_REVERSE = 0.25;
	////

	private static int levelOnePosition = 309; //This changes as the elevator belts shift

	private static double[] positions = new double[5];

	private CANTalon elevatorTalon;

	private int currLevel = 1;
	private int desiredLevel = 1;

	private long lastPositionChangeTime;
	private int lastPosition;

	private long fineControlEndTime;

	private enum ElevatorState {
		/**
		 * Moving to a specified position
		 */
		MOVING,
		/**
		 * Moving due to computer control. Not user interruptible.
		 */
		FINE_CONTROL,
		/**
		 * Moving to the bottom limit switch during auto-homing calibration.
		 */
		HOMING,
		/**
		 * Either stationary or maintaining a vertical position with PID control to prevent slippage
		 */
		IDLE,
		/**
		 * Stopped due to velocity timeout
		 */
		TIMEDOUT;
	}

	private ElevatorState state = ElevatorState.IDLE;

	private static File levelOnePositionFile = new File("levelOnePosition.txt");

	/**
	 * Constructs the subsystem, including 2 digital inputs for switches,
	 * and the Talon for the elevator. 
	 */
	public ElevatorPIDSpeedSubsystem(boolean enable) 
	{
		super(enable);

		if (!enabled)
			return;

		elevatorTalon = initializeTalon(RobotMap.ELEVATOR_TALON);

		readLevelOnePosition();
		generatePositions();
	}

	private void generatePositions() {
		positions[0] = 1; //definite bottom

		positions[1] = levelOnePosition;
		positions[2] = positions[1] + OFFSETS[2];
		positions[3] = positions[2] + OFFSETS[3];
		positions[4] = positions[3] + OFFSETS[4];
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

		switch (getCurrentLevel())
		{
		case 1: SmartDashboard.putString("Elevator Level", "1: BOTTOM"); break;
		case 2: SmartDashboard.putString("Elevator Level", "2: LOW"); break;
		case 3: SmartDashboard.putString("Elevator Level", "3: HIGH"); break;
		case 4: SmartDashboard.putString("Elevator Level", "4: TOP"); break;
		}
	}

	@Override
	protected void initDefaultCommand() {
		//setDefaultCommand(new ElevatorMonitorCommand());
	}

	/**
	 * Gets the current position of the elevator. Returns -1 if not at a discernible level. (or disabled)
	 * <br><br>
	 * READ: May not be current, as elevator positions are not updated after the last elevator
	 * command has terminated. Not really a good method to call in its current state.
	 * 
	 * @return
	 */
	public int getCurrentLevel(){
		if (!enabled)
			return -1;

		for (int x = 1; x <= 4; x++)
		{
			if (isAtLevel(x))
				return x;
		}

		return -1;
	}

	/**
	 * Stops the elevator at its current position. Enters IDLE mode.
	 */
	public void stop()
	{
		if (!enabled)
			return;

		Robot.log.write(Level.WARNING, "Elevator stopped!");
		state = ElevatorState.IDLE;

		elevatorTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
		elevatorTalon.enableControl();
		elevatorTalon.set(0);
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

		//Robot.log.write(Level.SEVERE, "IN SETLEVEL");

		SmartDashboard.putNumber("Desired Level: ", desiredLevel);

		if ((!isAtLevel(level)) && (state != ElevatorState.FINE_CONTROL))
		{
			this.desiredLevel = level;

			state = ElevatorState.MOVING;

			//Changes Pot to speed mode for level 1 and 4.
			if((desiredLevel == 4||desiredLevel == 1))
			{
				elevatorTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
				elevatorTalon.enableControl();

				if(desiredLevel == 1)
				{
					elevatorTalon.set(TALON_SPEED_DOWN);
				}
				else if (desiredLevel == 4)
				{
					elevatorTalon.set(TALON_SPEED_UP);
				}
			}
			else
			{
				elevatorTalon.changeControlMode(CANTalon.ControlMode.Position);
				elevatorTalon.enableControl();

				elevatorTalon.set(positions[desiredLevel]);
			}

			Robot.log.write(Level.INFO, "Elevator Level Set: " + desiredLevel);
		}
		else
		{
			Robot.log.write(Level.WARNING, "Failed attempt to set desired level to: " + level);
			Robot.log.write(Level.WARNING, "Current state: " + state + " Current pos: " 
			+ elevatorTalon.getPosition());

		}
	}

	/**
	 * Whether or not the elevator is essentially stationary
	 * @return
	 */
	public boolean isStationary()
	{
		return state == ElevatorState.IDLE || state == ElevatorState.TIMEDOUT;
	}

	public void calibrateElevator()
	{
		Robot.log.write(Level.WARNING, "Calibrating Elevator!");
		state = ElevatorState.HOMING;
		elevatorTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
		elevatorTalon.enableControl();
		elevatorTalon.set(0.5); //drive to bottom limit
	}

	public boolean isAtLevel(int level)
	{
		if (!enabled)
			return false;

		double position = elevatorTalon.getPosition();

		if (!elevatorTalon.isFwdLimitSwitchClosed())
		{
			currLevel = 1;
		}
		else if (position > positions[2] - 20 && position < positions[2] + 50)
		{
			currLevel = 2;
		}
		else if (position > positions[3] - 20 && position < positions[3] + 60)
		{
			currLevel = 3;
		}
		else if (!elevatorTalon.isRevLimitSwitchClosed())
		{
			currLevel = 4;
		}

		return currLevel == level;
	}

	private void saveLevelOnePosition()
	{
		try {
			PrintWriter pw = new PrintWriter(levelOnePositionFile);
			pw.write(levelOnePosition + "");
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			Robot.log.write(Level.SEVERE, "Failed to write levelOnePosition to file!");
			e.printStackTrace();
		}
	}

	private void readLevelOnePosition()
	{
		try {
			Scanner sc = new Scanner(levelOnePositionFile);
			levelOnePosition = Integer.valueOf(sc.nextLine());
			sc.close();
		} catch (FileNotFoundException e) {
			Robot.log.write(Level.SEVERE, "Failed to read levelOnePosition from file!");
			e.printStackTrace();
		}
	}

	public void update() {
		if (!enabled)
			return;


		//Robot.log.write(Level.INFO, "State: " + state);

		//HOMING DETECTION
		if (state == ElevatorState.HOMING)
		{
			if (isAtLevel(1)) //If we are on the bottom limit switch
			{
				state = ElevatorState.IDLE;
				levelOnePosition = (int) elevatorTalon.getPosition();
				saveLevelOnePosition();
				SmartDashboard.putNumber("LEVEL1POS", levelOnePosition);
				generatePositions();
			}
		}

		if (state == ElevatorState.FINE_CONTROL)
		{
			if (System.currentTimeMillis() >= fineControlEndTime)
			{
				Robot.log.write(Level.INFO, "Fine control completed! Stopping...");
				stop();
			}
		}

		updateCurrentPosition();
		checkTimeout();
	}

	private void checkTimeout() {
		//TODO: Fix timeout

		if (state == ElevatorState.MOVING)
		{
			int currentPos = (int) elevatorTalon.getPosition();

			if (lastPosition != currentPos)
			{
				lastPositionChangeTime = System.currentTimeMillis();
			}
			else
			{
				if (System.currentTimeMillis() - lastPositionChangeTime > TALON_STALL_TIMEOUT)
				{
					Robot.log.write(Level.SEVERE, "Elevator stopping due to stall timeout!");
					stop();
				}
			}
		}

		//		if (timing && System.currentTimeMillis() - talonStallTime > TALON_STALL_TIMEOUT)
		//		{
		//			Robot.log.write(Level.SEVERE, "Elevator has stalled longer than the "
		//					+ "timeout! Stopping the elevator.");
		//			stop();
		//		}
	}

	private void updateCurrentPosition() {
		//POSITION CALCULATION
		//		if (isAtLevel(1))
		//		{
		//			if(desiredLevel == 1)
		//			{
		//				//Changes the mode to position mode if the elevator is at level one.
		//				elevatorTalon.changeControlMode(ControlMode.Position);
		//				elevatorTalon.enableControl();
		//				elevatorTalon.set(positions[desiredLevel]);
		//			}
		//			currLevel = 1;
		//			Robot.log.write(Level.INFO, "Elevator detected at level #1");
		//		}
		//
		//		if (isAtLevel(2))
		//		{
		//			currLevel = 2;
		//			Robot.log.write(Level.INFO, "Elevator detected at level #2");
		//		}
		//
		//		if (isAtLevel(3))
		//		{
		//			currLevel = 3;
		//			Robot.log.write(Level.INFO, "Elevator detected at level #3");
		//		}
		//
		//		if (isAtLevel(4))
		//		{
		//			if(desiredLevel == 4)
		//			{
		//				//Changes the mode to position mode when the elevator is at level four.
		//				elevatorTalon.changeControlMode(ControlMode.Position);
		//				elevatorTalon.enableControl();
		//				elevatorTalon.set(positions[desiredLevel]);
		//			}
		//			currLevel = 4;
		//			Robot.log.write(Level.INFO, "Elevator detected at level #4");
		//		}


		if (state == ElevatorState.MOVING && isAtLevel(desiredLevel)) //Stay where we are and mark IDLE
		{
			Robot.log.write(Level.WARNING, "Reached desired level: " + desiredLevel 
					+ " Current position: " + elevatorTalon.getPosition());
			elevatorTalon.changeControlMode(CANTalon.ControlMode.Position);
			elevatorTalon.enableControl();
			//			if (desiredLevel == 1 || desiredLevel == 4){
			//				elevatorTalon.set(elevatorTalon.getPosition());
			//			}
			//			else
			//			{
			//				elevatorTalon.set(positions[desiredLevel]);
			//			}

			if (desiredLevel == 4){
				elevatorTalon.set(elevatorTalon.getPosition());
			}
			else if (desiredLevel != 1)
			{
				elevatorTalon.set(positions[desiredLevel]);
			}

			state = ElevatorState.IDLE;
		}
	}

	public void start() {
		if (!enabled)
			return;
	}

	public int getDesiredLevel() {
		if (!enabled)
			return -1;

		return desiredLevel;
	}

	private CANTalon initializeTalon(int channel) {
		CANTalon talon = new CANTalon(channel);
		talon.reverseOutput(true);
		talon.ConfigRevLimitSwitchNormallyOpen(false);
		talon.changeControlMode(CANTalon.ControlMode.Position);
		talon.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
		talon.setPID(6, .0001, 0.85, 0.75, 0, 0, 0);
		//talon.setPID(1, 0.01, 0.85, 1.5, 0, 0, 0); //TUNED VALUES FOR FRONT RIGHT WHEEL
		//talon.setPID(1.23, 0, 0, 1.33, 0, 10, 0); EXACT VALUES FOR OLD TESTBOARD PID DO NOT CHANGE
		talon.enableControl();
		return talon;
	}

	/**
	 * Runs motor in reverse indefinitely. Call stop() when done!
	 * @param runTime 
	 */
	public void reverse(long runTime) 
	{
		if (!enabled)
			return;

		fineControlEndTime = System.currentTimeMillis() + runTime;

		Robot.log.write(Level.WARNING, "Elevator reversing!");

		state = ElevatorState.FINE_CONTROL;

		elevatorTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
		elevatorTalon.enableControl();
		elevatorTalon.set(TALON_SPEED_REVERSE); //TODO: is backwards?
	}
}
