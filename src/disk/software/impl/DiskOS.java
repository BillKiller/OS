package disk.software.impl;

import disk.hardware.Disk;
import disk.hardware.FAT;

public class DiskOS {
	private FAT fatTable = new FAT();
	private Disk disk = new Disk();
	public FAT getFatTable() {
		return fatTable;
	}
	public void setFatTable(FAT fatTable) {
		this.fatTable = fatTable;
	}
	public Disk getDisk() {
		return disk;
	}
	public void setDisk(Disk disk) {
		this.disk = disk;
	}
}
