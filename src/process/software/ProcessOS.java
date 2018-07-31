package process.software;

import memory.hardware.PCB;

public interface ProcessOS {
	/**
	 * 创建新的进程
	 * @return boolean
	 */
	public boolean create(byte[] data);
	
	/**
	 *销毁一个进程
	 *@param pcb 
	 */
	public void destory(PCB pcb);
	
	/**
	 * 阻塞一个正在运行的进程
	 * @param pcb
	 */
	public void block(PCB pcb);
	
	/**
	 * 唤醒一个阻塞的进程
	 * 进程由阻塞态变成就绪态
	 * @param pcb
	 */
	public void awake(PCB pcb);
}