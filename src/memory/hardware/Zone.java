package memory.hardware;

/**
 * 内存可用空间块节点类
 * 
 * @author suisui
 */
public class Zone {
	private int zoneSize;// 内存块大小
	private int head;// 内存块起址
	private boolean isFree;// 内存块是否空闲

	public Zone(int head, int zoneSize) {
		this.head = head;
		this.zoneSize = zoneSize;
		this.isFree = true;
	}

	public int getZoneSize() {
		return zoneSize;
	}

	public void setZoneSize(int zoneSize) {
		this.zoneSize = zoneSize;
	}

	public int getHead() {
		return head;
	}

	public void setHead(int head) {
		this.head = head;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
}
