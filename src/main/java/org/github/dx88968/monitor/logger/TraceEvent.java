package org.github.dx88968.monitor.logger;

public class TraceEvent {
	
	public static Event SessionCreated=new Event();
	public static Event SessionIdle=new Event();
	
	public static class Event{
		
		public void emit(){
			synchronized (this) {
				this.notifyAll();
			}
		}
	}

}
