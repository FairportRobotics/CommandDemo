package org.usfirst.frc.team578.robot.subsystems;

import org.usfirst.frc.team578.robot.commands.LogPDPStatusCommand;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * Subsystem to obtain values from the Power Distribution Panel (PDP).
 * Automatically starts a non-finishing {@link LogPDPStatusCommand}
 *
 */
public class PDPSubystem extends SubsystemBase {

	private PowerDistributionPanel pdp;

	public PDPSubystem(boolean enable) {
		super(enable);

		if (!enabled)
			return;

		pdp = new PowerDistributionPanel();
	}

	public double getInputVoltage()
	{
		if (!enabled)
			return -1;
		
		return pdp.getVoltage();
	}

	public double getTotalCurrent()
	{
		if (!enabled)
			return -1;
		
		return pdp.getTotalCurrent();
	}

	public double getTemperature()
	{
		if (!enabled)
			return -1;
		
		return pdp.getTemperature();
	}

	@Override
	protected void initDefaultCommand() {
		//setDefaultCommand(new LogPDPStatusCommand());
	}

}
