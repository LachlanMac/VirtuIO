package com.frick.virtuio.main.utilities;

public class Timer extends Thread {
	private int interval;
	Timed t;

	public Timer(int interval, Timed t) {
		this.t = t;
		this.interval = interval;
		this.start();
	}

	public void run() {

		while (true) {

			while (t != null) {

				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				t.tick();

			}

		}

	}
}
