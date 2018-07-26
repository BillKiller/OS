package process.hardware;

public class Register {
	private int ax;//保存x的值ֵ
	private int psw;//程序状态字״̬
	private int pc;//指向指令所在位置
	private int ir;//ָ保存执行指令
	
	
	public int getAx() {
		return ax;
	}
	public void setAx(int ax) {
		this.ax = ax;
	}
	public int getPsw() {
		return psw;
	}
	public void setPsw(int psw) {
		this.psw = psw;
	}
	public int getPc() {
		return pc;
	}
	public void setPc(int pc) {
		this.pc = pc;
	}
	public int getIr() {
		return ir;
	}
	public void setIr(int ir) {
		this.ir = ir;
	}
	
	
}
