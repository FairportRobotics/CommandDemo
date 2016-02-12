package org.usfirst.frc.team578.robot.commands.autonomous;

import org.usfirst.frc.team578.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class AutonomousTurnToZero extends Command{
	
	public AutonomousTurnToZero() {
		requires(Robot.driveSubsystem);
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		if(Robot.navx.getFusedHeading()>358||Robot.navx.getFusedHeading()<2)
		{
			Robot.driveSubsystem.driveMotors(0, 0, 0, 0);
		}
		else
		{
			Robot.driveSubsystem.driveMotors(.2, -.2, .2, -.2);
		}
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}

}
