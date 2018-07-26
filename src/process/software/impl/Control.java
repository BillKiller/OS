package process.software.impl;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.management.Query;

import device.DMT;
import memory.hardware.PCB;
import process.hardware.Register;
import process.software.Process;

public class Control {
	//编译器
	public static compiler.Compiler compiler = new compiler.Compiler();
	//系统时间
	public static int systemTime = 0;
	public static int runTime = 1;
	public static int equipmentTime = 1;
	public static int timeSloct = 6;
	//系统寄存器
	public static Register register = new Register();
	//PCB队列，就绪，阻塞，空白
	public static Queue<PCB> readyProcess = new LinkedList<>();
	public static Queue<PCB> blockProcess = new LinkedList<>();
	public static Queue<PCB> blankProcess = new LinkedList<>();
	//设备管理
	public static Queue<DMT> waitProcess = new LinkedList<>();
	//已存在过的线程，用于线程id
	public static int existProcess = 0;
	//正在运行的进程
	public static PCB runningProcess = null;
	
	public Control() {
		
	}
	//轮转调度线程，6秒一轮转
	public void CPU() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				systemTime++;
				if(runningProcess==null) {
					if(!readyProcess.isEmpty())
						runningProcess = readyProcess.poll();
				}else {
					if(timeSloct!=0) {
						timeSloct--;
						//读取进程所在内存的信息
						//register.ir = memory[register.getPc()]*Math.pow(2, 8)+memory[register.getPc()+1];
						//register.setPc(register.getPc()+2);
						//执行指令
						executeIr(register.getIr());
						//判断是否中断
						if(register.getPsw()==1) {
							//请求使用设备，进程发生中断
							Control.blockProcess.add(runningProcess);
							Process.block(runningProcess);
							runningProcess = Control.readyProcess.poll();
							timeSloct = 6;
						}else if(register.getPsw()==3) {
							//进程完成发生中断
							
							//显示结果
							
							
							runningProcess = Control.readyProcess.poll();
							timeSloct = 6;
						}
					}else {
						register.setPsw(2);
						if (register.getPsw()==2) {
							//时钟中断
							Control.blockProcess.add(runningProcess);
							Process.block(runningProcess);
							runningProcess = Control.readyProcess.poll();
						}
					}
				}
				
			}
		},0,1000);
	}
	//执行ir语句
	public void executeIr(int ir) {
		int op = (int) (ir/Math.pow(2, 13));
		if(op==0b0) {
			register.setAx((int) (ir%Math.pow(2, 13)));
		}else if(op == 0b001) {
			
		}else if(op==0b010) {
			
		}else if(op==0b011) {
			register.setPsw(1);
		}else if(op==0b100) {
			register.setPsw(3);
		}
		
	}
	
	public static void main(String[] args) {
		Control control = new Control();
		control.CPU();
	}
	
}
