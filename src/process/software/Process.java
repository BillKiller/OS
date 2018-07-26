package process.software;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import memory.hardware.PCB;
import process.software.impl.Control;

public class Process {
	public Process() {
	}
	
	//创建目前不完善，要按照磁盘和内存写
	public static PCB create(String filePath) {
		//为进程定义一个pcb
		PCB pcb =  new PCB(Control.existProcess++, 0, 0, 0, 0, 0, 0, 4);
		//将文件内容从磁盘读入内存
		File file = new File(filePath);
		try {
			String string;
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			while((string = bufferedReader.readLine())!=null) {
				
			}
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		Control.readyProcess.add(pcb);
		return pcb;
	}
	
	//销毁进程
	public static void destroy(PCB pcb) {
		if(Control.readyProcess.contains(pcb)) {
			Control.readyProcess.remove(pcb);
		}else if(Control.blockProcess.contains(pcb)) {
			Control.blockProcess.remove(pcb);
		}
	}
	

	//阻塞进程
	public static void block(PCB pcb) {
		pcb.setAx(Control.register.getAx());
		pcb.setIr(Control.register.getIr());
		pcb.setPc(Control.register.getPc());
		pcb.setPsw(Control.register.getPsw());
		pcb.setState(2);
		Control.blockProcess.add(pcb);
	}
	
	//唤醒进程
	public static void awake(PCB pcb) {
		Control.blockProcess.remove(pcb);
		Control.readyProcess.add(pcb);
	}

}
