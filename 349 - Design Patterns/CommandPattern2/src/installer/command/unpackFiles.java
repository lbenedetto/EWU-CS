package installer.command;

import installer.HardDrive;

public class unpackFiles implements Command {
	private HardDrive hardDrive;

	public unpackFiles(HardDrive hardDrive) {
		this.hardDrive = hardDrive;
	}

	@Override
	public void execute() {
		try {
			Thread.sleep(estimateTime() * 100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		hardDrive.setFilesUnpacked();
	}

	@Override
	public int estimateTime() {
		return 15;
	}
}
