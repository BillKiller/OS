package disk.software;

import disk.software.impl.DiskManagerImpl;

/**
 * 利用接口避免耦合
 * 
 * @author Zhanbiao_Zhu
 *
 */
public interface FileOS {
	/**
	 * 创建文件的路径+名字
	 * 还有文件的属性
	 * @param filename
	 * @param attribute
	 * @return
	 */
	public boolean create_file(int bnum,String filename,String type,int attribute);
	
	/**
	 * 打开文件的名字和打开的方式（读或者写）
	 * @param name
	 * @param kind
	 * @return
	 */
	public boolean open_file(int bnum,String name,int kind);
	/**
	 * 读取length个字节
	 * （其实不知道有什么作用）
	 * @param name
	 * @param length
	 * @return
	 */
	public byte[] read_file(int bnum,String name,int length);
	/**
	 * 写入length个字节
	 * （其实不知道有什么作用）
	 * @param name
	 * @param bnum 代表磁盘块位置
	 * @param length
	 * @return
	 */
	public boolean write_file(int bnum,String name,byte [] data);
	public boolean close_file(int bnum,String name);
	public boolean delete_file(int bnum,String name);
	/**
	 * 显示文件的内容
	 * @param name
	 * @return
	 */
	public boolean typef_file(int bnum,String name);
	public boolean change(int bnum,String name);
	/**
	 * 运行可执行文件
	 * @param name
	 * @return
	 */
	public boolean execute(int bnum,String name);

}
