package installer.command;

import installer.HardDrive;

public class format implements Command {
	private HardDrive hardDrive;

	public format(HardDrive hardDrive) {
		this.hardDrive = hardDrive;
	}

	@Override
	public void execute() {
		try {
			Thread.sleep(estimateTime() * 100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		hardDrive.setFormatted();
	}

	@Override
	public int estimateTime() {
		return 10;
	}
}
