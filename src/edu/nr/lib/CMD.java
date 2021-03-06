package edu.nr.lib;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A modified version of the WPILib Command class that provides additional
 * lifecycle methods.
 *
 */
public abstract class CMD extends Command {
	public CMD() {
		super();
	}

	/**
	 * Constructor used to set this commands visible name in SmartDashboard
	 * 
	 * @param name
	 */
	public CMD(String name) {
		super(name);
	}

	public CMD(String name, double timeout) {
		super(name, timeout);
	}

	public CMD(double timeout) {
		super(timeout);
	}

	private boolean reset;

	/**
	 * Called every time the command starts
	 */
	protected abstract void onStart();

	/**
	 * Called every loop while the command is active
	 */
	protected abstract void onExecute();

	/**
	 * Called when the command ends
	 * 
	 * @param interrupted
	 *            True if the command was interrupted
	 */
	protected abstract void onEnd(boolean interrupted);

	@Override
	protected void initialize() {
		onStart();
		reset = false;
	}

	@Override
	protected final void execute() {
		if (reset) {
			onStart();
			reset = false;
		}

		onExecute();
	}

	@Override
	protected final void end() {
		reset = true;
		onEnd(false);
	}

	@Override
	protected final void interrupted() {
		reset = true;
		onEnd(true);
	}
}
