package process.software.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.management.Query;

import device.DMT;
import device.EquipmentOS;
import memory.hardware.PCB;
import memory.software.impl.MemoryOSImpl;
import myUtil.Number;
import process.hardware.Register;
import process.software.Process;

public class Controller {
	//编译器
	public static compiler.Compiler compiler = new compiler.Compiler();
	//系统时间
	public static int systemTime = 0;
	public static int timeSloct = 6;
	//系统寄存器
	public static Register register = new Register();
	//内存管理
	public static MemoryOSImpl memory = new MemoryOSImpl();
	byte[] memoryByte;
	//进程管理
	public static Process processController = new Process();
	//设备管理
	public static EquipmentOS equipmentController = new EquipmentOS();
	//PCB队列，就绪，阻塞，空白
	public static Queue<PCB> readyProcess = new LinkedList<>();
	public static Queue<PCB> blockProcess = new LinkedList<>();
	public static Queue<PCB> blankProcess = new LinkedList<>();
	//已存在过的线程，用于线程id
	public static int existProcess = 0;
	//正在运行的进程
	public static PCB runningProcess = null;
	//保存中间结果
	private Map<Integer, Integer> intermediaResult;
	
	//初始化controller
	public Controller() {
		ArrayList<PCB> pcbs = memory.getPCB();
		for(PCB pcb : pcbs) {
			readyProcess.add(pcb);
		}
		memoryByte = memory.getUserArea();
	}
	
	//轮转调度线程，6秒一轮转
	public void CPU() {
		//定时器
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				systemTime++;
				System.out.println("系统时间为：" + systemTime);
				if(runningProcess == null) {
					if(!readyProcess.isEmpty())
						runningProcess = readyProcess.poll();
				}else {
					if(timeSloct!=0) {
						timeSloct--;
						//读取进程所在内存的信息
						register.setIr((int) (memoryByte[register.getPc()]*Math.pow(2, 8) + memoryByte[register.getPc()+1]));
						register.setPc(register.getPc()+2);
						//执行指令
						executeIr(register.getIr());
						//判断是否中断
						if(register.getPsw() == Number.deviceInterrupt) {
							//请求使用设备，进程发生中断
							Controller.blockProcess.add(runningProcess);
							processController.block(runningProcess);
							runningProcess = Controller.readyProcess.poll();
							timeSloct = 6;
							register.setPsw(Number.noInterrupt);
						}else if(register.getPsw() == Number.finishedInterrupt) {
							//进程完成发生中断
							
							//显示结果 map
							for(Map.Entry<Integer, Integer> entry : intermediaResult.entrySet()) {
								System.out.println("结果："+(char)(entry.getKey()+48)+"--"+entry.getValue());
							}
							
							runningProcess = Controller.readyProcess.poll();
							timeSloct = 6;
							register.setPsw(Number.noInterrupt);
						}
					}else {
						register.setPsw(Number.timeInterrupt);
						if (register.getPsw()==Number.timeInterrupt) {
							//时钟中断
							Controller.readyProcess.add(runningProcess);
							runningProcess = Controller.readyProcess.poll();
							register.setPsw(Number.noInterrupt);
						}
					}
					//显示中间结果，示例调用
					for(Map.Entry<Integer, Integer> entry : intermediaResult.entrySet()) {
						System.out.println("结果："+(char)(entry.getKey()+48)+"--"+entry.getValue());
					}
				}
				
			}
		},0,1000);
	}
	//执行ir语句
	public void executeIr(int ir) {
		intermediaResult = runningProcess.getIntermediaResult();
		int op = (int) (ir/Math.pow(2, 13));
		if(op==0b0) {
			//赋值操作
			register.setAx((int) (ir%Math.pow(2, 13)));
			boolean find = false;
			int number = (int) (register.getAx()%Math.pow(2, 8));
			int character = (int) (register.getAx()/Math.pow(2, 8));
			for(Map.Entry<Integer, Integer> entry : intermediaResult.entrySet()) {
				if(character==entry.getValue()) {
					entry.setValue(number);
					find = !find;
				}
			}
			if(!find) {
				intermediaResult.put(character, number);
			}
		}else if(op == 0b001) {
			//自增
			register.setAx((int) (ir%Math.pow(2, 13)));
			boolean find = false;
			int character = (int) (register.getAx()/Math.pow(2, 8));
			for(Map.Entry<Integer, Integer> entry : intermediaResult.entrySet()) {
				if(character==entry.getValue()) {
					entry.setValue(entry.getValue()+1);
					find = !find;
				}
			}
			if(!find) {
				System.out.println("无该变量");
			}

		}else if(op==0b010) {
			//自减
			register.setAx((int) (ir%Math.pow(2, 13)));
			boolean find = false;
			int character = (int) (register.getAx()/Math.pow(2, 8));
			for(Map.Entry<Integer, Integer> entry : intermediaResult.entrySet()) {
				if(character==entry.getValue()) {
					entry.setValue(entry.getValue()-1);
					find = !find;
				}
			}
			if(!find) {
				System.out.println("无该变量");
			}
		}else if(op==0b011) {
			//占用设备,未完成
			register.setPsw(Number.deviceInterrupt);
		}else if(op==0b100) {
			//完成
			register.setPsw(Number.finishedInterrupt);
		}
		
	}
	
	public Map<Integer, Integer> getIntermediaResult() {
		return intermediaResult;
	}
	public void setIntermediaResult(Map<Integer, Integer> intermediaResult) {
		this.intermediaResult = intermediaResult;
	}
	public static void main(String[] args) {
		Controller Controller = new Controller();
		Controller.CPU();
	}
	
}
