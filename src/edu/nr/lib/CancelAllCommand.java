package edu.nr.lib;

import edu.nr.robotics.Robot;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CancelAllCommand extends EmptyCommand {

	public CancelAllCommand() {
		for (Subsystem subsystem : Robot.subsystems) {
			subsystem.getCurrentCommand().cancel();
			requires(subsystem);
		}
	}
	
	public void onExecute() {
		System.out.println("Cancelling all commands");
	}

	@Override
	protected boolean isFinished() {
		return true;
	}
}
