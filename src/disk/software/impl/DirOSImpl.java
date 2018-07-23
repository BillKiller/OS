package disk.software.impl;

import java.util.ArrayList;

import org.junit.Test;

import disk.hardware.FileStruct;
import disk.software.DirOS;
import disk.software.DiskManager;
import myUtil.Number;

public class DirOSImpl implements DirOS {
	private DiskManager diskManagerImpl;
	@Override
	public int md(int bnum,String path,String name,int attribute) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				if(diskManagerImpl.getFreeStructPos(bnum)==-1)return -1;
				if(diskManagerImpl.getFreeBlockNum()==0)return -1;
				//满足磁盘块和目录项都足够的条件
				
				int pos=diskManagerImpl.getFreeBlockPos();
				
				FileStruct fileStruct = new FileStruct();
				fileStruct.setName(name);
				fileStruct.setType(" ");
				fileStruct.setFileAttribute(Number.intToByte(attribute));
				fileStruct.setStartPos(Number.intToByte(pos));
				fileStruct.setFileLength((short)0);
				
				int pnum = diskManagerImpl.getFreeStructPos(bnum);
				
				diskManagerImpl.addStruct(bnum, pnum, fileStruct);
				diskManagerImpl.storeFile(pos,null);
				return pos;
	}

	@Override
	public FileStruct[] dir(int bnum,String path) {
		// TODO Auto-generated method stub
		ArrayList<FileStruct> list = new ArrayList<>();
		for(int i=0;i<64;i+=8) {
			FileStruct temp = diskManagerImpl.getFileStruct(bnum, i);
			if(diskManagerImpl.isFileStructExist(temp)==false)continue;
			list.add(temp);
		}
		return list.toArray(new FileStruct[list.size()]);
	}
	/*
	 * test unit for dir() result ok
	 * test unit for rd() result ok
	 */
	/*
	@Test(timeout=1000)
	public void test() {
			DiskOS diskOS = new DiskOS();
			diskManagerImpl = new DiskManagerImpl();
			diskManagerImpl.setDiskOS(diskOS);
			FileOSImpl fileOS = new FileOSImpl();
			fileOS.setDiskManagerImpl(diskManagerImpl);
			
			fileOS.create_file(4, "abc", "e", 0);
			fileOS.create_file(4, "abc", "e", 0);
			fileOS.create_file(4, "abc", "e", 0);
			fileOS.create_file(4, "abc", "e", 0);
			md(4, "", "fff",0);
			
			
			
			list(4);
			
			rd(4,"fff");

			System.out.println("------");
			list(4);
	}
	*/
	public void list(int bnum) {
		FileStruct[] fileStruct = dir(bnum, "");
		for(int i =0;i<fileStruct.length;i++) {
			if(isDirectory(fileStruct[i]))
				System.out.println("dir:"+fileStruct[i].getName());
			else
				System.out.println("file:"+fileStruct[i].getName()+"."+fileStruct[i].getType());
		}
	}
	@Override
	public boolean rd(int bnum,String path) {
		// TODO Auto-generated method stub
		int pnum=diskManagerImpl.getStructPos(bnum, path);
		if(pnum==-1)return false;
		FileStruct fileStruct=diskManagerImpl.getFileStructByName(bnum, path);
		diskManagerImpl.removeFile(fileStruct.getStartPos());
		diskManagerImpl.delStruct(bnum, pnum,null);
		return true;
	}

	@Override
	public boolean format() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDirectory(FileStruct fileStruct) {
		if(fileStruct == null )return false;
		if(fileStruct.getType().equals(" ")) {
			return true;
		}
		return false;
	}
}
