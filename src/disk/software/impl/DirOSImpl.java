package disk.software.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import disk.hardware.FileStruct;
import disk.software.DirOS;
import disk.software.DiskManager;
import myUtil.Number;

public class DirOSImpl implements DirOS {
	private DiskManager diskManagerImpl;
	@Override
	public int md(int bnum,String path,int attribute) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				if(diskManagerImpl.getFreeStructPos(bnum)==-1)return -1;
				if(diskManagerImpl.getFreeBlockNum()==0)return -1;
				//满足磁盘块和目录项都足够的条件
				
				int pos=diskManagerImpl.getFreeBlockPos();
				
				FileStruct fileStruct = new FileStruct();
				fileStruct.setName(path);
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
		List<FileStruct>list=new ArrayList<FileStruct>();
		for(int i=0;i<8;i+=8) {
			FileStruct temp = diskManagerImpl.getFileStruct(bnum, i);
			if(temp==null)break;
			list.add(temp);
		}
		return (FileStruct[])list.toArray();
	}
		
	@Override
	public boolean rd(int bnum,String path) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean format() {
		// TODO Auto-generated method stub
		return false;
	}

}
