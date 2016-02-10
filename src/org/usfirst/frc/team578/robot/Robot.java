package org.usfirst.frc.team578.robot;
import java.lang.reflect.Field;
import java.util.logging.Level;

import org.usfirst.frc.team578.robot.commands.autonomous.AutonomousNothing;
import org.usfirst.frc.team578.robot.commands.autonomous.AutonomousDriveStraightGroup;
import org.usfirst.frc.team578.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team578.robot.subsystems.ElevatorPIDSpeedSubsystem;
import org.usfirst.frc.team578.robot.subsystems.FibinacciSubsystem;
import org.usfirst.frc.team578.robot.subsystems.IntakeSubsystem;
import org.usfirst.frc.team578.robot.subsystems.LoggingSubsystem;
import org.usfirst.frc.team578.robot.subsystems.PDPSubystem;
import org.usfirst.frc.team578.robot.subsystems.SubsystemBase;
import org.usfirst.frc.team578.robot.subsystems.ToteDetectionSubsystem;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	//Read about disabling a subsystem in the robotInit() method! You	
	//likely do not need to comment out a subsystem here.
	public static DriveSubsystem driveSubsystem;
	public static ElevatorPIDSpeedSubsystem elevatorSubsystem;
	public static IntakeSubsystem intakeSubsystem;
	public static FibinacciSubsystem fibinacciSubsystem;
	public static LoggingSubsystem log;
	public static ToteDetectionSubsystem toteDetectionSubsystem;
	public static PDPSubystem pdpSubystem;
	public static AHRS navx;
	//public static PIDDrive pid;
	//public static POTTest pot;


	public static OI oi;
	private static long startTime;

	private Command autonomousCommand;
	private SendableChooser autonomousChooser;
	private SendableChooser loggingLevelChooser;
	private boolean initial = true;
	//private boolean calibrated;

	public static long getElapsedTime()
	{
		return (System.currentTimeMillis() - startTime) / 1000;
	}

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code. 
	 * 
	 * Initializes subsystems and SmartDashboard SendableChoosers.
	 */
	public void robotInit() {
		//Log has no robot references, no need to disable currently. (As of 2/11/15)
		log = new LoggingSubsystem();
		log.write(Level.INFO, "ROBOT INITIALIZING...");
		
		navx = new AHRS(SPI.Port.kMXP);

		//INIT ROBOT SUBSYSTEMS

		//
		// **** READ: IMPORTANT NOTICE! ****
		// 
		// TO DISABLE A SUBSYSTEM, SET  false  IN THE CONSTRUCTOR.
		// YOU NO LONGER NEED TO COMMENT OUT ALL USAGES!
		// A DISABLED SUBSYSTEM WILL NOT INITIALIZE COMPONENTS.
		// SUBSYSTEM METHODS WILL RETURN EARLY IF NOT ENABLED.
		// SEE SUBSYSTEM METHOD JAVADOCS FOR SPECIFIC INSTANCE IMPLICATIONS.
		//	
		//

		driveSubsystem = new DriveSubsystem(true); //ensure pidDrive disabled
		elevatorSubsystem = new ElevatorPIDSpeedSubsystem(true);
		intakeSubsystem = new IntakeSubsystem(true);
		fibinacciSubsystem = new FibinacciSubsystem(true);
		toteDetectionSubsystem = new ToteDetectionSubsystem(false);
		pdpSubystem = new PDPSubystem(true);

		//pot = new POTTest(false);

		//pid = new PIDDrive(false); //do not enable unless drive disabled

		//END SUBSYSTEMS
		//OTHER INIT

		oi = new OI();
		initializeAutonomousChooser();
		initializeDebugChooser();

		startTime = System.currentTimeMillis();

		logSubsystems();
		log.write(Level.INFO, "ROBOT INITIALIZATION COMPLETE!");
		//log.closeStream();
		//Use for USB camera (maybe)
		//CameraServer server = CameraServer.getInstance();
		//		if (server != null)
		//		{
		//			server.setQuality(50);
		//			server.startAutomaticCapture("cam0");
		//		}
	}

	/**
	 * Writes the status of all the subsystems to the log
	 */
	private void logSubsystems() {
		for (Field f : getClass().getDeclaredFields())
		{
			if (f.getType().getGenericSuperclass() == SubsystemBase.class)
			{
				try {
					Object get = f.get(this);

					log.write(Level.INFO, f.getType().getSimpleName() + " initialized as: " 
							+ (get == null ? "Null"
									: (((SubsystemBase) f.get(this)).enabled) ? "Enabled" : "Disabled"));

				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initializeDebugChooser() {
		loggingLevelChooser = new SendableChooser();
		loggingLevelChooser.addDefault(Level.SEVERE.getName(), Level.SEVERE);
		loggingLevelChooser.addObject(Level.WARNING.getName(), Level.WARNING);
		loggingLevelChooser.addObject(Level.INFO.getName(), Level.INFO);
		loggingLevelChooser.addObject(Level.CONFIG.getName(), Level.CONFIG);
		SmartDashboard.putData("Logging Level", loggingLevelChooser);
	}

	private void initializeAutonomousChooser() {
		autonomousChooser = new SendableChooser();
		autonomousChooser.addObject("Do Nothing", new AutonomousNothing());
		autonomousChooser.addDefault("Drive Straight", new AutonomousDriveStraightGroup());
		SmartDashboard.putData("Autonomous Strategy", autonomousChooser);
	}

	public Level getLoggingLevel()
	{
		return (Level) loggingLevelChooser.getSelected();
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		// schedule the autonomous command (example)
		autonomousCommand = (Command) autonomousChooser.getSelected();
		log.openStream();
		Robot.log.write(Level.INFO, "Initializing Autonomous mode: " + autonomousCommand.getName());

		if (autonomousCommand != null) autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to 
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null) autonomousCommand.cancel();

		Robot.log.write(Level.INFO, "Beginning Teleop!");
	}

	/**
	 * This function is called when the disabled button is hit.
	 * You can use it to reset subsystems before shutting down.
	 */
	public void disabledInit(){
		if (!initial)
		{
			initial = false;
			Robot.log.write(Level.INFO, "Robot disabled");
			Robot.log.closeStream();
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}
	//popeye loves kale

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		Scheduler.getInstance().run();
		LiveWindow.run();
	}
	
	@Override
	public void testInit() {
	}
}
