package org.callas.vm.udp;
import java.util.Vector;
import org.callas.vm.Call;

public interface InputInterface extends Runnable {
	void setConnection(String str);
	Call popCall();
	void close();
}
