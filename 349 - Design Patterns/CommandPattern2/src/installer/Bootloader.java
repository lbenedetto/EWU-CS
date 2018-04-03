package installer;

import installer.command.*;

public class Bootloader {
	public static void main(String args[]){
		HardDrive h = new HardDrive();
		Command[] commands = {new format(h), new copyFiles(h), new unpackFiles(h), new installDrivers(h), new defrag(h)};
		Installer installer = new Installer(commands);
		installer.install();
		if(h.isBootable()) System.out.println("Successfully installed");
	}
}
