package org.github.dx88968.monitor.logger;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.github.dx88968.monitor.logger.TraceEvent.Event;
import org.github.dx88968.monitor.restlet.Auditor;
import org.github.dx88968.monitor.restlet.ResourceStates;
import org.github.dx88968.monitor.restlet.ResourceType;
import org.github.dx88968.monitor.restlet.Traceable;
import org.github.dx88968.monitor.utils.CollectionUtils;
import org.github.dx88968.monitor.utils.Constants;
import org.github.dx88968.monitor.utils.StackUtils;


public class DirectOutputTracker extends Traceable{
	
	ConcurrentHashMap<Condition, Pipeline> activePipelines;
	long serial=0;// 0 is reserved value indicating serial is not set
	public static DirectOutputTracker instance=new NotInitTarcer();
	
	private DirectOutputTracker(){
		activePipelines=new ConcurrentHashMap<>();
	}
	
	public static void init() throws Exception{
		if (instance==null || instance instanceof NotInitTarcer||instance instanceof DisabledTracer) {
			Constants.loadConstants();
			Auditor.getInstance(Constants.DEFAULTSERVERPORT);
			instance=new DirectOutputTracker();
		}
	}
	
	/*
	 * 0 is reserved value indicating serial is not set
	 */
	public long getSerial() {
		return serial;
	}

	public List<Pipeline> getPipelines() {
		Enumeration<Pipeline> pipelines = activePipelines.elements();
		List<Pipeline> returnValue=new ArrayList<>();
		while (pipelines.hasMoreElements()) {
			Pipeline pipeline = (Pipeline) pipelines.nextElement();
			returnValue.add(pipeline);
		}
		return returnValue;
	}
	
	public List<Condition> getConditions(){
		Enumeration<Condition> keys =activePipelines.keys();
		List<Condition> returnValue=new ArrayList<>();
		while (keys.hasMoreElements()) {
			Condition condition = (Condition) keys.nextElement();
			returnValue.add(condition);
		}
		return returnValue;
	}
	
	public void waitfor(Event e){
		e.waitForEmit();
	}
		
	/*
	 * get a pipeline satisfying certain condition, build one if none is found
	 */
	public Pipeline getPipeline(String traceMark,String className,String methodRep,TraceLevel level) throws IOException{
		Condition condition=new Condition(traceMark, className, methodRep,level);
		if (activePipelines.containsKey(condition)) {
			return activePipelines.get(condition);
		}
		String name=Integer.toString(condition.hashCode());
		Pipeline pipeline=new PipelineBuilder().setName(name).setMaxBufferSize(0).build();
		try {
			Auditor auditor=Auditor.getInstance();
			auditor.register(pipeline);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		activePipelines.put(condition, pipeline);
		return pipeline;
	}
	
	/*
	 * get a pipeline with a hash of a condition, none if no match is found
	 */
	public Pipeline getPipeline(String hashCodeOfKey){
		int hashcode=Integer.parseInt(hashCodeOfKey);
		return CollectionUtils.getHashMapElementByHash(activePipelines, hashcode);
	}
	
	public void stopPipeline(String traceMark,String className,String methodRep,TraceLevel level){
		Condition Condition=new Condition(traceMark, className, methodRep,level);
		if (activePipelines.containsKey(Condition)) {
			activePipelines.remove(Condition);
		}
	}
	
	public void stopPipeline(String hashCodeOfKey){
		int hashcode=Integer.parseInt(hashCodeOfKey);
		CollectionUtils.removeHashMapElementByHash(activePipelines, hashcode);
	}
	
	public void print(String traceMark,TraceLevel level,String content){
		if (activePipelines==null || activePipelines.size()<=0) {
			return;
		}
		String className=null;
		String methodRep=null;
		StackTraceElement[] stacks = new Throwable().getStackTrace();  
		className=stacks[1].getClassName();
		try {
			methodRep=StackUtils.parseMethod(stacks[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String attach = StackUtils.summerize(className, methodRep);
		Condition tarCondition=new Condition(traceMark, className, methodRep,level);
		Set<Condition> conditions = activePipelines.keySet();
		Iterator<Condition> iter = conditions.iterator();
		Condition tempCondition=null;
		while(iter.hasNext()){
			tempCondition=iter.next();
			if (tarCondition.satisfy(tempCondition)) {
				activePipelines.get(tempCondition).writeline(attach+content);
			}
		}
	}
	
	public void join(){
		waitfor(TraceEvent.SessionIdle);
		stop();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		try {
			Auditor.getInstance().terminate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		instance=new DisabledTracer();
		TraceEvent.disable();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setState(ResourceStates state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ResourceType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAccessibleID(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAccessibleID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceStates getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getStartAt() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSourceID() {
		// TODO Auto-generated method stub
		return null;
	}
	
	static class NotInitTarcer extends DirectOutputTracker{
		
		
		@Override
		public void waitfor(Event e){
			try {
				init();
				e.waitForEmit();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		
		@Override
		public void print(String traceMark,TraceLevel level,String content){
			try {
				init();
				instance.print(traceMark, level, content);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.err.println("Snipper Mouse can not be initialized for"+e.getMessage());
				instance=new DisabledTracer();
			}
		}
		
	}
	
	static class DisabledTracer extends DirectOutputTracker{
		
		@Override
		public void print(String traceMark,TraceLevel level,String content){
		}
	}

}
