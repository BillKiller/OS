package disk.software;

import disk.hardware.FileStruct;

/**
 * 
 * @author Zhanbiao_Zhu
 *
 */
public interface DirOS {
	/**
	 * 新建目录
	 * @param path
	 * @return
	 * @return 如果-1代表是错误，如果是其他代表是新目录得块
	 */
	public int md(int bnum,String path,String name,int attribute);
	/**
	 * 显示目录下的文件和目录
	 * 想当于dir命令
	 * @param path
	 * @return
	 */
	
	public FileStruct[] dir(int bnum,String path);
	
	/**
	 * 删除一个空目录
	 * -->我们要实现一个删除非空目录的扩张功能
	 * @param path
	 * @return
	 */
	public boolean rd(int bnum,String path);
	/**
	 * 格式化磁盘
	 * @return
	 */
	public boolean format();
	
	public boolean isDirectory(FileStruct fileStruct);
}
