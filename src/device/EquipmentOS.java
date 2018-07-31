package device;
import memory.hardware.PCB;
import process.hardware.*;
import process.hardware.Process;
import process.software.impl.*;
public class EquipmentOS {

	public static void apply(PCB pcb,Equipment num,int time){
		//申请设备
		WaitProcess value = new WaitProcess(pcb, time);
		if(num.getNumOfDevice()==0 || num.waitList.isEmpty()==false){
			num.waitList.add(value);
		}else if(num.getNumOfDevice()>0){
			for(int i=0;i<num.getDmts().length;i++){
				if(num.dmts[i].getStatus()==0){
					num.dmts[i].setStatus(1);
					num.setNumOfDevice(num.getNumOfDevice()-1);
					num.dmts[i].useDevice(pcb, time);
					/*
					 * 使用
					 */
					num.dmts[i].setStatus(0);
					num.setNumOfDevice(num.getNumOfDevice()+1);
					finish(num);

				}
			}
		}
	}

	public static void finish(Equipment value){
		if(!value.waitList.isEmpty()){
			WaitProcess mode = value.waitList.remove();
			PCB pcb = mode.getPcb();
			int time = mode.getTime();
			apply(pcb,value,time);
		}
	}
}
