package edu.nr.robotics;

import edu.nr.lib.CancelAllCommand;
import edu.nr.lib.Periodic;
import edu.nr.lib.SmartDashboardSource;
import edu.nr.lib.path.OneDimensionalPath;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveAnglePIDCommand;
import edu.nr.robotics.subsystems.drive.DriveComplexDistanceCommand;
import edu.nr.robotics.subsystems.drive.DriveConstantCommand;
import edu.nr.robotics.subsystems.drive.ResetEncodersCommand;
import edu.nr.robotics.subsystems.lights.LightsBlinkCommand;
import edu.nr.robotics.subsystems.lights.LightsOffCommand;
import edu.nr.robotics.subsystems.lights.LightsOnCommand;
import edu.nr.robotics.subsystems.shooter.ShooterOnCommand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI implements SmartDashboardSource, Periodic {
	/** Used Buttons:
	 * Drive Left: (0)
	 * -> 1:  Cancel all commands
	 * -> 2:  Reverse drive direction
	 * 
	 * Drive Right: (1)
	 * -> 1:  Slow Turn
	 * -> 10: Reset encoders
	 * 
	 * Operator Left: (2)
	 * -> 1:  Cancel all commands
	 * -> 9:  Drive PID enable switch
	 * 
	 * Operator Right: (3)
	 * -> 1:   Drive Angle Command
	 * -> 4:   Drive Constant Command, left only, no PID, full reverse throttle
	 * -> 6:   Lights On Command
	 * -> 7:   Lights Off Command
	 * -> 8:   Lights Blink Command, every 2000 milliseconds
	 * -> 10:  Drive Constant Command, left only, no PID, full throttle
	 */
	
	public SendableChooser drivingModeChooser;

	public double speedMultiplier = 1;

	private final double JOYSTICK_DEAD_ZONE = 0.15;

	public double gyroValueforPlayerStation = 0;

	private static OI singleton;

	Joystick driveLeft;
	Joystick driveRight;
	Joystick operatorLeft, operatorRight;

	JoystickButton fighter;

	private OI() {
		SmartDashboard.putNumber("Speed Multiplier", speedMultiplier);

		driveLeft = new Joystick(0);
		driveRight = new Joystick(1);

		operatorLeft = new Joystick(2);
		operatorRight = new Joystick(3);

		new JoystickButton(operatorRight, 10).whileHeld(new DriveConstantCommand(false, true, false,0.9));
		new JoystickButton(operatorRight, 4).whileHeld(new DriveConstantCommand(false, true, false,-0.9));
		new JoystickButton(operatorRight, 1).whenPressed(new DriveAnglePIDCommand(0.65));
		
		new JoystickButton(operatorRight, 2).whenPressed(new DriveComplexDistanceCommand(
				new OneDimensionalPath(6.096,RobotMap.MAX_SPEED, RobotMap.MAX_ACCELERATION), 1/RobotMap.MAX_SPEED,0,0,0));

		new JoystickButton(operatorRight, 6).whenPressed(new LightsOnCommand());
		new JoystickButton(operatorRight, 7).whenPressed(new LightsOffCommand());
		new JoystickButton(operatorRight, 8).whenPressed(new LightsBlinkCommand(100));

		
		fighter = new JoystickButton(operatorLeft, 9);

		new JoystickButton(operatorLeft, 1).whenPressed(new CancelAllCommand());
		new JoystickButton(driveLeft, 1).whenPressed(new CancelAllCommand());
		new JoystickButton(driveRight, 10).whenPressed(new ResetEncodersCommand());

	}

	public static OI getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new OI();
		}
	}

	public double getArcadeMoveValue() {
		return snapDriveJoysticks(driveLeft.getY()) * (driveLeft.getRawButton(2) ? -1 : 1);
	}

	public double getArcadeTurnValue() {
		return -snapDriveJoysticks(driveRight.getX());
	}

	public double getTankLeftValue() {
		return -snapDriveJoysticks(driveLeft.getY());
	}

	public double getTankRightValue() {
		return snapDriveJoysticks(driveRight.getY());
	}

	private double snapDriveJoysticks(double value) {
		if (Math.abs(value) < JOYSTICK_DEAD_ZONE) {
			value = 0;
		} else if (value > 0) {
			value -= JOYSTICK_DEAD_ZONE;
		} else {
			value += JOYSTICK_DEAD_ZONE;
		}
		value /= 1 - JOYSTICK_DEAD_ZONE;

		return value;
	}

	public double getRawMove() {
		return -driveLeft.getY();
	}

	public double getRawTurn() {
		return -driveRight.getX();
	}

	public double getTurnAdjust() {
		return driveRight.getRawButton(1) ? 0.5 : 1;
	}

	@Override
	public void smartDashboardInfo() {
		speedMultiplier = SmartDashboard.getNumber("Speed Multiplier");
	}
	
	public boolean isTankNonZero() {
		return getTankLeftValue() != 0 || getTankRightValue() != 0;
	}
	
	public boolean isArcadeNonZero() {
		return getArcadeMoveValue() != 0 || getArcadeTurnValue() != 0;
	}

	public boolean isCurrentModeNonZero() throws DrivingModeException {
		if(drivingModeChooser.getSelected().equals("arcade")) {
			return isArcadeNonZero();
		} else if(drivingModeChooser.getSelected().equals("tank")) {
			return isTankNonZero();
		} else {
			throw new DrivingModeException((DrivingMode) drivingModeChooser.getSelected());
		}
	}
		
	@Override
	public void periodic() {
		try {
			if(isCurrentModeNonZero()) {
				if(Drive.getInstance().getCurrentCommand().getName() != "DriveJoystickCommand") {
					Drive.getInstance().getCurrentCommand().cancel();
					//TODO: Test this cancel functionality
				}
			}
		} catch (DrivingModeException e) {
			System.out.println("Driving Mode " + e.getMode().toString() + " is not supported by OI drive subsystem cancel");
		}
	}
}
