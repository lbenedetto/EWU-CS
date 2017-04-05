package installer.command;

import installer.HardDrive;

public class defrag implements Command {
	private HardDrive hardDrive;

	public defrag(HardDrive hardDrive) {
		this.hardDrive = hardDrive;
	}

	@Override
	public void execute() {
		try {
			Thread.sleep(estimateTime() * 100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		hardDrive.setDefragged();
	}

	@Override
	public int estimateTime() {
		return 30;
	}
}
