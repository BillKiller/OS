package disk.software.impl;

import org.junit.Test;

import disk.hardware.FAT;
import disk.hardware.FileStruct;
import disk.software.DiskManager;
import disk.software.FileOS;
import myUtil.Number;

public class FileOSImpl implements FileOS{

	/**
	 * 创建一个新的文件
	 * @param diskManagerImpl 管理者 用于查询磁盘空间是不是足够 以及 目录项是不是还有空间
	 * @param 其他是文件属性
	 * 如果存在目录项并且存在空的磁盘块那么可以创建新的文件
	 * 
	 */
	private DiskManager diskManagerImpl;
	@Override
	public boolean create_file(int bnum,String filename,String type,int attribute) {
		// TODO Auto-generated method stub
		if(diskManagerImpl.getFreeStructPos(bnum)==-1)return false;
		if(diskManagerImpl.getFreeBlockNum()==0)return false;
		//满足磁盘块和目录项都足够的条件
		
		int pos=diskManagerImpl.getFreeBlockPos();
		
		FileStruct fileStruct = new FileStruct();
		fileStruct.setName(filename);
		fileStruct.setType(type);
		fileStruct.setFileAttribute(Number.intToByte(attribute));
		fileStruct.setStartPos(Number.intToByte(pos));
		fileStruct.setFileLength((short)0);
		
		int pnum = diskManagerImpl.getFreeStructPos(bnum);
		
		diskManagerImpl.addStruct(bnum, pnum, fileStruct);
		diskManagerImpl.storeFile(pos,null);
		return true;
	}
	@Override
	public boolean open_file(int bnum, String name, int kind) {
		// TODO Auto-generated method stub
		
		return false;
	}
	/**
	 * @param bnum 磁盘块位置
	 * @param name 文件的名字
	 * @param length 文件的长度 也可以不通过长度而是通过文件末尾结束符
	 * 功能：读取所在磁盘块的下name文件length长度的数据
	 */
	@Override
	public byte[] read_file(int bnum, String name, int length) {
		// TODO Auto-generated method stub
		int pos=diskManagerImpl.getStructPos(bnum, name);
		if(pos!=-1) {
			return diskManagerImpl.getFile(pos,length);
		}
		return null;
	}
	public byte[] read_file(FileStruct fileStruct) {
		// TODO Auto-generated method stub
			return diskManagerImpl.getFile(fileStruct.getStartPos(),fileStruct.getFileLength());
	}
	/**
	 * 将数据写进磁盘内
	 * 同时更新文件目录项
	 */
	@Override
	public boolean write_file(int bnum, String name, byte[]data) {
		// TODO Auto-generated method stub
		FileStruct fileStruct=diskManagerImpl.getFileStructByName(bnum, name);
		if(fileStruct==null)return false;
		int pos=fileStruct.getStartPos();
		if(diskManagerImpl.isEnoughBlock(data)) {
			diskManagerImpl.storeFile(pos,data);
			fileStruct.setFileLength((short)data.length);
			diskManagerImpl.updateFileStruct(bnum, fileStruct);
			return true;
		}else {
			return false;
		}	
	}
	/*
	public void test() {
		DiskOS diskOS = new DiskOS();//开机
		diskManagerImpl = new DiskManagerImpl();
		diskManagerImpl.setDiskOS(diskOS);
		String context="i am heeeeeee";
		diskOS.getFatTable().getFatItem()[4]=1;
		byte[]data=context.getBytes();
		create_file(4, "abc", "c", 0);
	
		if(write_file(4,"abc",data)) {
			System.out.println("w ok");
		}else {
			System.out.println("w err");
		}
		byte[]disk =diskOS.getDisk().getDisk()[4].getDiskblock();
		for(int i=0;i<16;i++) {
			System.out.println(disk[i]);
		}
		FileStruct temp = diskManagerImpl.getFileStructByName(4, "abc");
		System.out.println(temp.getFileLength());
		byte[] read=read_file(temp);
		String s=new String(read);
		System.out.println(s);
	}
	*/
	@Override
	public boolean close_file(int bnum, String name) {
		// TODO Auto-generated method stub
		
		return false;
	}
	@Override
	public boolean delete_file(int bnum, String name) {
		// TODO Auto-generated method stub
		int pnum = diskManagerImpl.getStructPos(bnum, name);
		FileStruct fileStruct = diskManagerImpl.getFileStructByName(bnum, name);
		if(pnum ==-1)return false;
		diskManagerImpl.removeFile(fileStruct.getStartPos());
		diskManagerImpl.delStruct(bnum, pnum, fileStruct);
		return true;
	}
	@Override
	public boolean typef_file(int bnum, String name) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean change(int bnum, String name) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean execute(int bnum, String name) {
		// TODO Auto-generated method stub
		return false;
	}

}
