package memory.hardware;

import disk.hardware.FileStruct;

/**
 * 
 * @author Zhanbiao_Zhu
 *
 */
public class OpenFileItem {
	
	private FileStruct fileStruct;
	private Pointer read;
	private Pointer write;
	public FileStruct getFileStruct() {
		return fileStruct;
	}
	public void setFileStruct(FileStruct fileStruct) {
		this.fileStruct = fileStruct;
	}
	public Pointer getRead() {
		return read;
	}
	public void setRead(Pointer read) {
		this.read = read;
	}
	public Pointer getWrite() {
		return write;
	}
	public void setWrite(Pointer write) {
		this.write = write;
	}
}
