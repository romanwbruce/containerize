package containerize.romanbruce.com.logging;

import containerize.romanbruce.com.DateAndTime;

public class DebugLogger implements Logger {

	@Override
	public void write(String m) {
		System.out.println("["+DateAndTime.now()+"/debug] "+m);
	}

}
