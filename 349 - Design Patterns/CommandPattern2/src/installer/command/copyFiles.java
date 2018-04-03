package installer.command;

import installer.HardDrive;

public class copyFiles implements Command {
	private HardDrive hardDrive;

	public copyFiles(HardDrive hardDrive) {
		this.hardDrive = hardDrive;
	}

	@Override
	public void execute() {
		try {
			Thread.sleep(estimateTime() * 100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		hardDrive.setFilesCopied();
	}

	@Override
	public int estimateTime() {
		return 30;
	}
}
