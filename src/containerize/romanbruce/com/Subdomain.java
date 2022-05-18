package containerize.romanbruce.com;

import java.util.UUID;

public class Subdomain {
	public static String getURL(String projName) {
		return UUID.randomUUID().toString().substring(0, 9)+".freeapphosting.co";
	}
}
