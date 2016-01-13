package org.usfirst.frc.team578.robot;



import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
	// Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
	
	//Joysticks
	private Joystick leftJoystick;
	private Joystick rightJoystick;
	private Button leftJoystickButtonSEVEN;
	private Button rightJoystickButtonSEVEN;
	
	//Gamepad
	private Joystick gamePad;
	private Button buttonONE;
	private Button buttonTWO;
	private Button buttonTHREE;
	private Button buttonFOUR;
	private Button buttonFIVE;
	private Button buttonSEVEN;
	private Button buttonSIX;
	private Button buttonEIGHT;
	private Button buttonNINE;
	private Button buttonTEN;
    
    public OI()
    {
    	
    	leftJoystick = new Joystick(RobotMap.LEFT_JOYSTICK);
    	rightJoystick = new Joystick(RobotMap.RIGHT_JOYSTICK);
    	gamePad = new Joystick(RobotMap.GAMEPAD);
    	buttonONE = new JoystickButton(gamePad, 1);
    	buttonTWO = new JoystickButton(gamePad, 2);
    	buttonTHREE = new JoystickButton(gamePad, 3);
    	buttonFOUR = new JoystickButton(gamePad, 4);
    	buttonFIVE = new JoystickButton(gamePad, 5);
    	buttonSEVEN = new JoystickButton(gamePad, 7);
    	buttonSIX = new JoystickButton(gamePad, RobotMap.CONTROLLER_B6);
    	buttonEIGHT = new JoystickButton(gamePad, RobotMap.CONTROLLER_B8);
    	buttonNINE = new JoystickButton(gamePad, RobotMap.CONTROLLER_B9);
    	buttonTEN = new JoystickButton(gamePad, RobotMap.CONTROLLER_B10);
    	
    	leftJoystickButtonSEVEN = new JoystickButton(leftJoystick, RobotMap.JOYSTICK_STOP_ELEVATOR);
    	rightJoystickButtonSEVEN = new JoystickButton(rightJoystick, RobotMap.JOYSTICK_STOP_ELEVATOR);
    	


    	//SmartDashboard.putString("ADA", "AODUA");
    }
	
	public double getLeftStickY()
	{
		return leftJoystick.getY();
	}
	
	public double getRightStickY()
	{
		return rightJoystick.getY();
	}
	
	public double getLeftStickX()
	{
		return leftJoystick.getX();
	}
	
	public double getRightStickX()
	{
		return rightJoystick.getX();
	}
	
	public boolean getButtonSix()
	{
		return buttonSIX.get();
	}
	
	public boolean getButtonEight()
	{
		return buttonEIGHT.get();
	}
	
	public boolean getButtonNine()
	{
		return buttonNINE.get();
	}
	
//	public boolean getLeftTrigger()
//	{
//		return leftTrigger.get();
//	}
//	
//	public boolean getRightTrigger()
//	{
//		return rightTrigger.get();
//	}
}