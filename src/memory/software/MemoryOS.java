package memory.software;

import disk.hardware.FileStruct;
import memory.hardware.OpenFileItem;
import memory.hardware.PCB;

public interface MemoryOS {
	//kind = 0 代表是可运行文件，到时候入驻pcb表
	//kind = 1 代表是文本文件， 入驻文件打开表
	public boolean allocation(byte[]data,int kind,OpenFileItem openFileItem);
	//后面这个两个函数本质和allocation(byte[]data,int kind,OpenFileItem openFileItem 一样
	//你喜欢哪个就留下哪个，或者都留下也可以我觉得最还是都留下来吧
	public boolean allocationForFile(byte[]data,OpenFileItem openFileItem);
	public boolean allocationForProcess(byte[]data,OpenFileItem openFileItem);
	//用于cpu调度
	public void collection(long pid);//关闭进程后释放空间
	public void collection(OpenFileItem openFileItem);//通过打开文件目录项查找文件打开目录表然后回收内存
	public PCB[] getPCB();
	public byte[] getUserArea();
}
