package academicLeague;

import java.util.Timer;

public class Alarm implements Runnable {
	Speak speak;
	Timer timer;
	boolean bool;
	
	public Alarm(Speak speak, Timer timer) {
		this.speak = speak;
		this.timer = timer;
	}

	@Override
	public void run() {
		while (speak.clip.isActive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				bool = true;
			}
		}
		//	timer.schedule(task, 5000);
		
	}
	public boolean getValue() {
		return bool;
	}

}
