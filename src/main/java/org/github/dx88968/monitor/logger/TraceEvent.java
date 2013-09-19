package org.github.dx88968.monitor.logger;

import java.util.ArrayList;

public class TraceEvent {
	static boolean enabled=true;
	private static ArrayList<Event> events=new ArrayList<>();
	
	public static Event SessionCreated=new Event();
	public static Event SessionIdle=new Event();
	
	public static class Event{
		
		public Event(){
			events.add(this);
		}
		
		public void waitForEmit(){
			if (enabled) {
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {}
				}
			}
			
		}
		
		public void emit(){
			if (enabled) {
				synchronized (this) {
					this.notifyAll();
				}	
			}
		}
	}
	
	public static void disable() {
		triggerAll();
		enabled=false;
	}
	
	private static void triggerAll(){
		for (int i = 0; i < events.size(); i++) {
			events.get(i).emit();
		}
	}

}
