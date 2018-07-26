package device;

public class DMT {
	
	//设备占用信息
	//占用设备的进程id
	private int proceeID = 0;
	//设备占用时间
	private int useTime = 0;
	//设备占用状态 等待0，运行1，完成3
	private int status = 0;
	
	
	public int getProceeID() {
		return proceeID;
	}
	public void setProceeID(int proceeID) {
		this.proceeID = proceeID;
	}
	public int getUseTime() {
		return useTime;
	}
	public void setUseTime(int useTime) {
		this.useTime = useTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
