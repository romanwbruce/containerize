package containerize.romanbruce.com;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Random;

public class OpenPort {

	private static Random random = new Random();
	
	public static int getNextPort() {
		int port = 1025 + random.nextInt(50_000);
		if(available(port)) {
			return port;
		}else {
			return getNextPort();
		}
	}
	
	public static boolean available(int port) {
	    if (port < 1024 || port > 65500) {
	        throw new IllegalArgumentException("Invalid start port: " + port);
	    }

	    ServerSocket ss = null;
	    DatagramSocket ds = null;
	    try {
	        ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        ds = new DatagramSocket(port);
	        ds.setReuseAddress(true);
	        return true;
	    } catch (IOException e) {
	    } finally {
	        if (ds != null) {
	            ds.close();
	        }

	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (IOException e) {
	                /* should not be thrown */
	            }
	        }
	    }

	    return false;
	}
}
