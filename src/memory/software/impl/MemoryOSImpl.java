package memory.software.impl;

import java.util.LinkedList;

import org.junit.Test;

import memory.hardware.MemoryTable;
import memory.hardware.MyMemory;
import memory.hardware.OpenFileItem;
import memory.hardware.PCB;
import memory.hardware.Zone;
import memory.software.MemoryOS;
import myUtil.Number;

/**
 * 软件实现内存管理类
 * 
 * @author suisui
 *
 */
public class MemoryOSImpl implements MemoryOS {

	private MyMemory memory;
	private MemoryTable memoryTable;// 内存分配表
	private PCB[] pcbs;
	private LinkedList<Zone> freeZones;// 空闲区链表

	public MemoryOSImpl() {
		// 初始化
		this.memory = new MyMemory();
		this.memoryTable = memory.getSystemArea().getMemoryTable();
		this.pcbs = memory.getSystemArea().getPcb();
		this.freeZones = new LinkedList<>();

		freeZones.add(new Zone(0, Number.sizeOfUserArea, true));
	}

	@Override
	public boolean allocationForFile(byte[] data, OpenFileItem openFileItem) {
		int size = data.length;
		if (firstFit(size) != -1) {// 默认首次适配
			
			memory.getSystemArea().getOpenFileTable().getOpenFileTable().add(openFileItem);
			return true;
		}
		return false;
	}

	@Override
	public boolean allocationForProcess(byte[] data) {
		int size = data.length;
		if (firstFit(size) != -1) {// 默认首次适配
			return true;
		}
		return false;
	}

	@Override
	public boolean collection(long pid) {
		PCB tempPcb;

		for (PCB pcb : pcbs) {
			if (pcb.getPid() == pid) {
				tempPcb = pcb;
				break;
			}
		}
		for (Zone zone : memoryTable) {

		}
		return false;
	}

	@Override
	public boolean collection(OpenFileItem openFileItem) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PCB[] getPCB() {
		return pcbs;
	}

	@Override
	public byte[] getUserArea() {
		return memory.getUserArea().getMemData();
	}

	/**
	 * 显示当前空闲块链表，测试用
	 */
	public void showFreeZone() {
		System.out.println("showFreeZone:");
		for (int j = 0; j < freeZones.size(); j++) {
			System.out.println("j=" + j + " " + freeZones.get(j));
		}
		System.out.println();
	}

	/**
	 * 显示当前内存分配表，测试用
	 */
	public void showMemoryTable() {
		System.out.println("showMemoryTable:");
		for (int j = 0; j < memoryTable.size(); j++) {
			System.out.println("j=" + j + " " + memoryTable.get(j));
		}
		System.out.println();
	}

	private void saveData(byte[] data) {
		int head=firstFit(data.length);
		for(int i=head;i<data.length;i++) {
			
		}
	}
	/**
	 * 首次适配
	 * 
	 * @param size 申请内存大小
	 * @return 分配成功返回分配的内存块起址，失败返回-1
	 */
	private int firstFit(int size) {
		int i = 0;
		for (Zone zone : freeZones) {

			if (size <= zone.getZoneSize()) {
//				System.out.println("Zone:"+zone);
				int remainSize = zone.getZoneSize() - size;// 剩余空闲区大小
				int newHead = zone.getHead() + size;// 空闲区的新起址
				Zone busyZone = new Zone(zone.getHead(), size, false);// 分配忙内存空间
				
				if (remainSize != 0) {
					Zone freeZone = new Zone(newHead, remainSize, true);// 新空闲块
					// 更新空闲区链表
					freeZones.remove(zone);// 删除旧空闲区
					freeZones.add(i, freeZone);// 增加新空闲区

					// 更新内存分配表
					memoryTable.remove(zone);// 删除旧内存块
					memoryTable.add(busyZone);// 增加忙内存块
					memoryTable.add(freeZone);// 增加空闲内存块
					showMemoryTable();
				}else {//remainSize == 0
					// 更新空闲区链表
					freeZones.remove(zone);// 删除旧空闲区
					memoryTable.remove(zone);// 删除旧内存块
					memoryTable.add(busyZone);// 增加忙内存块
					showMemoryTable();
				}
				return zone.getHead();
			} else {
				i++;
			}
		}
		return -1;
	}

	/**
	 * 下次适配
	 * 
	 * @param size
	 */
	private void nextFit(int size) {

	}

	/**
	 * 最佳适配
	 * 
	 * @param size
	 */
	private void bestFit(int size) {

	}

	/**
	 * 最差适配
	 * 
	 * @param size
	 */
	private void worstFit(int size) {

	}
}
