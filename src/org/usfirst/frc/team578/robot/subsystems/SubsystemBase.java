package org.usfirst.frc.team578.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * <p>
 * A custom Subsystem that can be disabled.
 * </p>
 * 
 * <p>
 * Any class that extends this one MUST not use or initialize any robot objects
 * if the subsystem is initialized as disabled.
 * </p>
 *
 */
public abstract class SubsystemBase extends Subsystem {

	public final boolean enabled;

	/**
	 * Constructs the subsystem. Sets the enabled constant.
	 * @param enable
	 */
	public SubsystemBase(boolean enable)
	{
		this.enabled = enable;
	}
}
