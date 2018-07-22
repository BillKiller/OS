package disk.software;

import disk.hardware.FileStruct;
import disk.software.impl.DiskOS;

/**
 * 磁盘空间管理
 * 连接文件和磁盘
 * 将文件存储到磁盘
 * 会调用磁盘的存取
 * 将文件byte分别存放到block中
 * @author Zhanbiao_Zhu
 *
 */
public interface DiskManager {
	
	//磁盘空间管理
	public int getFreeBlockPos();
	public int getFreeBlockNum();
	public boolean isEnoughBlock(byte[]file);
	public void storeFile(int start,byte[]file); //返回存储的startPos的位置
	public byte[] getFile(int start,int len);
	public void removeFile(int start);
	
	public void setDiskOS(DiskOS diskOS);
	
	//处理文件结构
	public int getStructPos(int bnum,String name);
	public int getFreeStructPos(int bnum);
	public void addStruct(int bnum,int pnum,FileStruct fileStruct);
	public void delStruct(int bnum,int pnum,FileStruct fileStruct);
	public FileStruct getFileStruct(int bnum,int pnum);
	public FileStruct getFileStructByName(int bnum,String name);
	public boolean updateFileStruct(int bnum,FileStruct fileStruct);
}
