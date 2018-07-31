package process.software;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import memory.hardware.PCB;
import myUtil.Number;
import process.software.impl.Controller;

public class Process implements ProcessOS{
	public Process() {
	}
	
	//新建进程，进程初始化
	@Override
	public boolean create(byte[] data) {
		// TODO Auto-generated method stub
		//为进程定义一个pcb
		
		//当有新进程新建的时候更新controller的进程队列  
		//Controller.readyProcess.add(pcb);
		
		return Controller.memory.allocationForProcess(data);
	}

	//销毁进程
	@Override
	public void destory(PCB pcb) {
		if(Controller.readyProcess.contains(pcb)) {
			Controller.readyProcess.remove(pcb);
		}else if(Controller.blockProcess.contains(pcb)) {
			Controller.blockProcess.remove(pcb);
		}
	}

	//阻塞进程
	@Override
	public void block(PCB pcb) {
		pcb.setAx(Controller.register.getAx());
		pcb.setIr(Controller.register.getIr());
		pcb.setPc(Controller.register.getPc());
		pcb.setPsw(Controller.register.getPsw());
		pcb.setState(Number.stateBlocking);
		Controller.blockProcess.add(pcb);
	}

	//唤醒进程
	@Override
	public void awake(PCB pcb) {
		Controller.blockProcess.remove(pcb);
		Controller.readyProcess.add(pcb);
	}

}
