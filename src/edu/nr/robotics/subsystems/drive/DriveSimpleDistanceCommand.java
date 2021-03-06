package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.CMD;
import edu.nr.lib.FieldCentric;

/**
 *
 */
public class DriveSimpleDistanceCommand extends CMD {

	double distance; // in meters
	double speed; // from 0 to 1

	public DriveSimpleDistanceCommand(double distance, double speed) {
		this.speed = speed;
		this.distance = distance;
		requires(Drive.getInstance());
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return FieldCentric.getInstance().getDistance() > distance;
	}

	@Override
	protected void onStart() {
		FieldCentric.getInstance().reset();
	}

	@Override
	protected void onExecute() {
		Drive.getInstance().arcadeDrive(speed, 0);
	}

	@Override
	protected void onEnd(boolean interrupted) {
	}
}
