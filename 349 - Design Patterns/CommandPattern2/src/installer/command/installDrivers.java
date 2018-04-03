package installer.command;

import installer.HardDrive;

public class installDrivers  implements Command {
	private HardDrive hardDrive;

	public installDrivers(HardDrive hardDrive) {
		this.hardDrive = hardDrive;
	}

	@Override
	public void execute() {
		try {
			Thread.sleep(estimateTime() * 100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		hardDrive.setDriversInstalled();
	}

	@Override
	public int estimateTime() {
		return 15;
	}
}
