package installer;

public class HardDrive {
	//This is the receiver
	private boolean formatted = false;
	private boolean filesCopied = false;
	private boolean filesUnpacked = false;
	private boolean driversInstalled = false;
	private boolean defragged = false;

	public void setFormatted() {
		this.formatted = true;
	}

	public void setFilesCopied() {
		this.filesCopied = true;
	}

	public void setFilesUnpacked() {
		this.filesUnpacked = true;
	}

	public void setDriversInstalled() {
		this.driversInstalled = true;
	}

	public void setDefragged() {
		this.defragged = true;
	}

	boolean isBootable(){
		return formatted && filesCopied && filesUnpacked && driversInstalled && defragged;
	}
}
