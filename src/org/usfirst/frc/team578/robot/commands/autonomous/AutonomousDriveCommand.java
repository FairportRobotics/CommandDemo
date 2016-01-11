package org.usfirst.frc.team578.robot.commands.autonomous;

import org.usfirst.frc.team578.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class AutonomousDriveCommand extends Command {
	
	public final double fr,fl,br,bl;
	private final double time;
	
	/**
	 * Drives with the specified values for the specified period of time.
	 * Note that the drive values are not altered after the timeout, this is
	 * simply the amount of time before the command finishes.
	 * @param fr
	 * @param fl
	 * @param br
	 * @param bl
	 * @param time drive time in seconds
	 */
	public AutonomousDriveCommand(double fr, double fl, double br, double bl, double time)
	{
		this.fr = fr;
		this.fl = fl;
		this.br = br;
		this.bl = bl;
		this.time = time;
	}

	/**
	 * Drives with the specified values. Ends immediately after execution.
	 * @param fr
	 * @param fl
	 * @param br
	 * @param bl
	 */
	public AutonomousDriveCommand(double fr, double fl, double br, double bl)
	{
		this.fr = fr;
		this.fl = fl;
		this.br = br;
		this.bl = bl;
		this.time = 0;
	}

	@Override
	protected void initialize() {
		

	}

	@Override
	protected void execute() {
		
		Robot.driveSubsystem.driveMotors(fr,fl,br,bl);
	}

	/**
	 * Is finished once the time elapsed since command initialization has exceeded the 
	 * specified time setting in the constructor
	 */
	@Override
	protected boolean isFinished() {
		return (timeSinceInitialized() >= time || time == 0);
	}

	@Override
	protected void end() {

	}

	@Override
	protected void interrupted() {
		

	}

}
