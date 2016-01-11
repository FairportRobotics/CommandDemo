package org.usfirst.frc.team578.robot.subsystems;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem to continuously log information about the robot
 *
 */
public class LoggingSubsystem extends Subsystem {

	private FileWriter p;

	public void openStream()
	{
		if (p == null)
		{
			File logDir = new File("/578-logs/");

			logDir.mkdirs();

			try {
				p = new FileWriter(new File(logDir + "/" + getTimestamp() + ".log"), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeStream()
	{
		if (p != null)
		{
			try {
				p.flush();
				p.close();
				p = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void initDefaultCommand() {

	}

	public void write(Level logLevel, String message) {
		String timeStamp = getTimestamp();

		try {
			
			openStream();
			
			p.write("\n[" + logLevel.getName() + "] "+ "[" + timeStamp + "]: " + message);
			p.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getTimestamp() {
		Calendar c = Calendar.getInstance();

		c.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
		int h = c.get(Calendar.HOUR);
		int m = c.get(Calendar.MINUTE);
		int s = c.get(Calendar.SECOND);
		int p = c.get(Calendar.AM_PM);

		String timeStamp = h + ":" + (m < 10 ? "0" + m : m) + ":"
				+ (s < 10 ? "0" + s : s) + " " + (p == 1 ? "PM" : "AM");
		return timeStamp;
	}
}
