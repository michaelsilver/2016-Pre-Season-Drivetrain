package edu.nr;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI 
{	
    public SendableChooser drivingModeChooser;
	
	public double speedMultiplier = 1;
	
	private final double JOYSTICK_DEAD_ZONE = 0.05;
	
	public double gyroValueforPlayerStation = 0;
	
	private static OI singleton;
	
	Joystick driveLeft;
	Joystick driveRight;
	Joystick operatorLeft, operatorRight;
	
	public static int H_DRIVE_BUTTON = 2;

	private OI()
	{
		driveLeft = new Joystick(0);
		driveRight = new Joystick(1);

		operatorLeft = new Joystick(2);
		operatorRight = new Joystick(3);
		
		//new JoystickButton(operatorRight, 9).whenPressed(new FrontArmsToggleCommand());
		//new JoystickButton(operatorRight, 8).whenPressed(new ToteTwoToWaitWithOpeningArmsGroup());
		//new JoystickButton(operatorRight, 7).whenPressed(new WhipDeployGroup());
		//new JoystickButton(operatorRight, 6).whenPressed(new WhipUndeployGroup());
		
		//new JoystickButton(operatorRight, 5).whenPressed(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_WAITING));
		//new JoystickButton(operatorRight, 4).whenPressed(new AdjustRecycleGroup());
		
		//new JoystickButton(operatorRight, 10).whenPressed(new ToteTwoToWaitGroup());
		
		//new JoystickButton(operatorRight, 2).whenPressed(new ToteOneToScoreGroup());
		//new JoystickButton(operatorRight, 1).whenPressed(new ScoreGroup());
		//new JoystickButton(operatorLeft, 8).whenPressed(new CloseBinGrabberAndRaiseGroup());
		
		//new JoystickButton(operatorLeft, 5).whenPressed(new ToggleBinGrabberCommand());
		
		//new JoystickButton(operatorLeft, 7).whenPressed(adjust); (Adjust used to be a variable, it was removed)
		
		//new JoystickButton(operatorLeft, 6).whenPressed(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BEFORE_TOTE_ADJUST));
		
		//new JoystickButton(operatorLeft, 1).whenPressed(new CancelAllCommand());
		
		//new JoystickButton(operatorLeft, 3).whenPressed(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_SCORING));
        
		//new JoystickButton(operatorLeft, 2).whenPressed(new ToteTwoToScoreGroup());
		
		//new JoystickButton(operatorLeft, 4).whenPressed(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BOTTOM));
		
		JoystickButton fighter = new JoystickButton(operatorLeft, 9);
		//fighter.whenPressed(new ActivateDumbDriveCommand());
		//fighter.whenReleased(new ActivateSmartDriveCommand());
		//new JoystickButton(driveLeft, 1).whenPressed(new CancelAllCommand());
	}
	
	public static OI getInstance()
	{
		init();
		return singleton;
	}
	
	public static void init()
	{
		if(singleton == null)
            singleton = new OI();
	}
	
	public double getArcadeMoveValue()
	{
		return -snapDriveJoysticks(driveLeft.getY());
	}
	
	public double getArcadeTurnValue()
	{
		return -snapDriveJoysticks(driveRight.getX());
	}
	
	public double getHDriveValue()
	{
		if(driveRight.getRawButton(2))
		{
			return -driveRight.getX();
		}
		else
		{
			return snapCoffinJoysticks(-operatorLeft.getRawAxis(0));
		}
	}
	
	//Reversing drive direction makes it easy to maneuver in reverse
	public boolean reverseDriveDirection()
	{
		return driveRight.getRawButton(1);
	}
	
	public double getTankLeftValue()
	{
		return -snapDriveJoysticks(driveLeft.getY());
	}

	public double getTankRightValue()
	{
		return snapDriveJoysticks(driveRight.getY());
	}
	
	private double snapDriveJoysticks(double value)
	{
		if(Math.abs(value) < JOYSTICK_DEAD_ZONE)
    	{
			value = 0;
    	}
    	else if(value > 0)
    	{
    		value -= JOYSTICK_DEAD_ZONE;
    	}
    	else
    	{
    		value += JOYSTICK_DEAD_ZONE;
    	}
		value /=  (1 - JOYSTICK_DEAD_ZONE);
		
		return value;
	}
	
	private double snapCoffinJoysticks(double value)
	{
		if(value > -0.1 && value < 0.1)
			return 0;
		
		return (value-0.1) / 0.9;
	}
	
	/**
	 * @return true if the DriveJoystickCommand should ignore joystick Z value and use the gyro to drive straight instead.
	 */
	public boolean useGyroCorrection()
	{
		return driveRight.getRawButton(H_DRIVE_BUTTON);
	}
	
	public double getRawMove()
	{
		return -driveLeft.getY();
	}
	
	public double getRawTurn()
	{
		return -driveRight.getX();
	}
}
