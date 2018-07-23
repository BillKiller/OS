package disk.software.impl;

import org.junit.Test;

import disk.hardware.Disk;
import disk.hardware.DiskBlock;
import disk.hardware.FAT;
import disk.hardware.FileStruct;
import disk.software.DiskManager;
import myUtil.Number;

public class DiskManagerImpl implements DiskManager{


	private DiskOS diskOS;
	public DiskOS getDiskOS() {
		return diskOS;
	}

	public void setDiskOS(DiskOS diskOS) {
		this.diskOS = diskOS;
	}

	@Override
	public int getFreeBlockPos() {
		// TODO Auto-generated method stub
		FAT fat=diskOS.getFatTable();
		byte [] fatItem=fat.getFatItem();
		for(int i =4;i<fatItem.length;i++) {
			if(Number.byteToInt(fatItem[i])==0) {
					return i;
			}
		}
		return -1;
	}

	@Override
	public int getFreeBlockNum() {
		// TODO Auto-generated method stub
		FAT fat=diskOS.getFatTable();
		byte [] fatItem=fat.getFatItem();
		int ans=0;
		for(int i =0;i<fatItem.length;i++) {
			if(Number.byteToInt(fatItem[i])==0) {
					ans++;
			}
		}
		return ans;
	}
	@Override
	public boolean isEnoughBlock(byte[] file) {
		int len=file.length/64+1;
		if(len<=getFreeBlockNum()) {
			return true;
		}else
		return false;
	}
	@Override
	public void storeFile(int start,byte[] file) {
		// TODO Auto-generated method stub
		/**
		 * 存取数据的时候去fat表查找适合的空间将数据放进磁盘
		 * 无论如何一定会占用一个块磁盘
		 * 
		 */
		byte [] fatItem=diskOS.getFatTable().getFatItem();
		if(file == null) {
			fatItem[start]=-1;
			return;
		}
		int last;
		int pos;
		int wp=0; //写指针
		int remain = file.length;
		int len = file.length;
		Disk disk = diskOS.getDisk();
		DiskBlock diskBlock[] =disk.getDisk();
		//无论如何都需要占用一块内存然后初分配
		//初分配如果不够需要更多的块构成链表
		last = pos = start;
		fatItem[pos]=-1; //假设只有一块
		int times=remain;
		for(int i=0;i<Math.min(times,64);i++) {
			diskBlock[pos].getDiskblock()[i] = file[wp];
			wp++;
			remain--;
		}
		//再分配
		for(int i=0;i<len/64;i++) {
			pos = getFreeBlockPos();
			fatItem[last] =Number.intToByte(pos);
			last = pos;
			times=remain;
			for(int j =0;j<Math.min(times,64);j++) {
				diskBlock[pos].getDiskblock()[j] = file[wp];
				wp++;
				remain--;
			}
		}
		fatItem[pos]=-1;
	}

	/**
	 * 给定FAT的起点和文件的长度
	 * 将文件读取出来成流式文件
	 */
	@Override
	public byte[] getFile(int start,int len) {
		// TODO Auto-generated method stub
		
		int pos=start;
		int remain = len;
		int rp=0;//读取指针用于定位
		byte []ans = new byte[len];
		byte []fat = diskOS.getFatTable().getFatItem();
		DiskBlock[] diskBlock = diskOS.getDisk().getDisk();
		while(pos!=255) {
			int time=remain;
			for(int j=0;j<Math.min(64,time);j++) {
				ans[rp] =diskBlock[pos].getDiskblock()[j]; 
				rp++;
				remain--;
			}
			pos = Number.byteToInt(fat[pos]);
		}
		return ans;
		
	}

	/**
	 * 删除FAT中的分配信息
	 */
	@Override
	public void removeFile(int start) {
		// TODO Auto-generated method stub
		int pos=start;
		byte []fat = diskOS.getFatTable().getFatItem();
		int last = pos;
		while(pos!=255) {
			pos = Number.byteToInt(fat[pos]);
			fat[last]=0;
			last = pos;
		}
	}
	/**
	 * 获取b磁盘下的空余目录项下标
	 */
	@Override
	public int getFreeStructPos(int bnum) {
		// TODO Auto-generated method stub
		byte []disk = diskOS.getDisk().getDisk()[bnum].getDiskblock();
		for(int i=0;i<64;i+=8) {
			if(disk[i]=="$".getBytes()[0]) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 通过名字查询文件目录项位置
	 */
 	@Override
	public int getStructPos(int bnum, String name) {
		// TODO Auto-generated method stub
		for(int i=0;i<64;i+=8) {
			FileStruct fileStruct=getFileStruct(bnum, i);
			if(fileStruct.getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}
 	/**
 	 * 通过名字查询得到文件目录项
 	 */
	@Override
	public FileStruct getFileStructByName(int bnum, String name) {
		// TODO Auto-generated method stub
		for(int i=0;i<64;i+=8) {
			FileStruct fileStruct=getFileStruct(bnum, i);
			if(fileStruct.getName().equals(name)) {
				return fileStruct;
			}
		}
		return null;
	}

	@Override
	public void addStruct(int bnum, int pnum, FileStruct fileStruct) {
		// TODO Auto-generated method stub
		//文件结构是8个字节的
		
		/**
		 * 格式如下:
		 * 名字[0]
		 * 名字[1]
		 * 名字[2]
		 * 类型[0]
		 * 属性[0]
		 * 起点[0]
		 * 长度[0] 低位
		 * 长度[1] 高位
		 */
		if(fileStruct.getName().length()==1) {
			fileStruct.setName("  "+fileStruct.getName());
		}
		if(fileStruct.getName().length()==2) {
			fileStruct.setName(" "+fileStruct.getName());
		}
		byte[] fileByte=new byte[]{
			fileStruct.getName().getBytes()[0],
			fileStruct.getName().getBytes()[1],
			fileStruct.getName().getBytes()[2],
			fileStruct.getType().getBytes()[0],
			fileStruct.getFileAttribute(),
			fileStruct.getStartPos(),
			(byte)(fileStruct.getFileLength()),
			(byte)(fileStruct.getFileLength()>>8)
		};
		for(int i =0;i<8;i++) {
			diskOS.getDisk().getDisk()[bnum].getDiskblock()[pnum+i]=fileByte[i];
		}
	}
	@Test
	public void Test() {
		FileStruct fileStruct = new FileStruct();
		fileStruct.setName("abc");
		fileStruct.setType("c");
		fileStruct.setFileAttribute((byte)10);
		fileStruct.setStartPos((byte)10);
		addStruct(4, 0, fileStruct);
		byte[]disk=diskOS.getDisk().getDisk()[4].getDiskblock();
		for(int i=0;i<16;i++) {
			System.out.println("i:"+i+" "+Number.byteToInt(disk[i]));
		}
		FileStruct file2=getFileStruct(4, 0);
		System.out.println("hello"+file2.getName()+" "+file2.getType()+" "+file2.getFileAttribute());
	}
	
	/**
	 * 删除文件目录项
	 */
	@Override
	public void delStruct(int bnum, int pnum, FileStruct fileStruct) {
		// TODO Auto-generated method stub
		for(int i =0;i<8;i++) {
			diskOS.getDisk().getDisk()[bnum].getDiskblock()[pnum+i]="$".getBytes()[0];
		}
	}
	/**
	 * 通过位置得到文件目录项
	 */
	@Override
	public FileStruct getFileStruct(int bnum, int pnum) {
		// TODO Auto-generated method stub
		FileStruct fileStruct  = new FileStruct();
		byte []disk = diskOS.getDisk().getDisk()[bnum].getDiskblock();
		fileStruct.setName(new String(new byte[] {
				disk[pnum],disk[pnum+1],disk[pnum+2]
		}));
		fileStruct.setType(new String(new byte[] {
				disk[pnum+3]
		}));
		fileStruct.setFileAttribute(disk[pnum+4]);
		fileStruct.setStartPos(disk[pnum+5]);
		short x =(short)(disk[pnum+7]*256+disk[pnum+6]);
		fileStruct.setFileLength(x);
		fileStruct.setName(fileStruct.getName().trim());
		return fileStruct;
	}
	public boolean isFileStructExist(FileStruct fileStruct) {
		if(fileStruct == null) {
			return false;
		}else {
			if(fileStruct.getName().startsWith("$")) {
				return false;
			}else {
				return true;
			}
		}
	}
	/**
	 * 更新文件目录项内容
	 */
	@Override
	public boolean updateFileStruct(int bnum,FileStruct fileStruct) {
		// TODO Auto-generated method stub
		int pnum=getStructPos(bnum, fileStruct.getName());
		if(pnum==-1)return false;
		byte[] fileByte=new byte[]{
				fileStruct.getName().getBytes()[0],
				fileStruct.getName().getBytes()[1],
				fileStruct.getName().getBytes()[2],
				fileStruct.getType().getBytes()[0],
				fileStruct.getFileAttribute(),
				fileStruct.getStartPos(),
				(byte)(fileStruct.getFileLength()),
				(byte)(fileStruct.getFileLength()>>8)
		};
		for(int i =0;i<8;i++) {
				diskOS.getDisk().getDisk()[bnum].getDiskblock()[pnum+i]=fileByte[i];
		}
		return true;
	}

}
