package containerize.romanbruce.com.logging;

import containerize.romanbruce.com.DateAndTime;

public class RuntimeLogger implements Logger {

	@Override
	public void write(String m) {
		System.out.println("["+DateAndTime.now()+"/Runtime] "+m);
	}

}
