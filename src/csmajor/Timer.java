package csmajor;

class Timer implements Runnable{
	int startTime;
	int runningTime;
	int currentSecond;
	boolean running;
	Timer(){
		startTime = (int) System.currentTimeMillis();
		running = false;
	}
	public void run() {
	    while (running) {
	    	this.runningTime = (int) System.currentTimeMillis();
	        this.currentSecond = (int)(this.runningTime - this.startTime) / 1000;
	    }
	}
	public void enable() {
		startTime = (int) System.currentTimeMillis();
		running = true;
	}
	public void disable() {
		running = false;
	}
	public int getSeconds() {
		return currentSecond % 60;
	}
	public int getMin() {
		return currentSecond/60;
	}
	public int getFullSeconds() {
		return currentSecond;
	}
}