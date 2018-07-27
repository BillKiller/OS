package memory.software;

import memory.software.impl.MemoryOSImpl;

public class memTest {
	public static void main(String[] args) {
		MemoryOSImpl main=new MemoryOSImpl();
		byte[] data1="23456".getBytes();
		main.allocationForProcess(data1);
		byte[] data2="123456789".getBytes();
		main.allocationForProcess(data2);
		byte[] data3="12345678910".getBytes();
		main.allocationForProcess(data3);
	}
}
