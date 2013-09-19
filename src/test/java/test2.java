import java.io.IOException;

import org.github.dx88968.monitor.restlet.JHttpAgent;


public class test2 {

	public static void main(String[] args) throws IOException {
		JHttpAgent.securePort(8080);
		System.out.println("port is secured");
	}
}
