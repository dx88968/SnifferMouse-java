package org.github.dx88968.monitor.restlet;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.github.dx88968.monitor.restlet.resources.ControllerResource;
import org.github.dx88968.monitor.restlet.resources.PipelineResource;
import org.github.dx88968.monitor.restlet.resources.TraceResource;
import org.github.dx88968.monitor.utils.Constants;
import org.json.simple.JSONObject;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.routing.Route;
import org.restlet.util.RouteList;



@SuppressWarnings("deprecation")
public class Auditor {

	static Auditor instance;
	ConcurrentHashMap<String, Traceable> resources;
	Component component;
	
	private Auditor(int port) throws Exception{
		
		// Disable annoying console logging of requests..
	    Logger logger = Logger.getLogger("org.restlet");
	    for (Handler handler : logger.getParent().getHandlers()) {
	        // Find the console handler
	        if (handler.getClass().equals(java.util.logging.ConsoleHandler.class)) {
	            // set level to SEVERE. We could disable it completely with 
	            // a custom filter but this is good enough.
	            handler.setLevel(Level.SEVERE);
	        }
	    }
	    JHttpAgent.securePort(port);
		resources=new ConcurrentHashMap<>();
		component = new Component();
        component.getServers().add(Protocol.HTTP, port);
        component.start();
	}
	
	public static Auditor getInstance(int port) throws Exception{
		if(instance==null){
			instance=new Auditor(port);
			instance.startService("/Control", ControllerResource.class);
			instance.startService("/Trace", TraceResource.class);
		}
		return instance;
	}
	
	public static Auditor getInstance() throws Exception{
		if(instance==null){
			throw new Exception("No active instance by now!");
		}
		return instance;
	}
	
	public Traceable getResource(String id){
		return resources.get(id);
	}
	
	public void register(Traceable resourse){
		String id=getAvailibleID(resourse.getName(), resourse.getType());
		resourse.setAccessibleID(id);
		resources.put(id, resourse);
		instance.startService(resourse.getAccessibleID(), PipelineResource.class);
	}
	
	public void unregister(Traceable resourse){
		if(!resources.containsKey(resourse.getAccessibleID())){
			return;
		}
		resources.remove(resourse.getAccessibleID());
		stopService(resourse.getAccessibleID());
	}
	
	public ArrayList<Traceable> filter(ResourceType type){
		ArrayList<Traceable> results=new ArrayList<>();
		Enumeration<String> keys = resources.keys();
		while(keys.hasMoreElements()){
			String id=keys.nextElement();
			if (extractTypeFromID(id).equals(type)) {
				results.add(resources.get(id));
			}
		}
		return results;
	}
	
	private ResourceType extractTypeFromID(String id){
		try{
			String type_str=id.split(Constants.IDInterval)[0];
			ResourceType type=ResourceType.valueOf(type_str.substring(1));
			return type;
		}catch(Exception ex){
			System.out.println("Unknown type or illegel id!");
			return  ResourceType.Unknown;
		}
	}
	
	private String getAvailibleID(String name,ResourceType type){
		String expectedName =Constants.IDHEAD+ type.toString()+Constants.IDInterval+name+Constants.IDInterval+System.currentTimeMillis();
		if(resources.contains(expectedName)){
			expectedName=getAvailibleID(name, type);
		}
		return expectedName;
	}
	
	public JSONObject getInfo(String id){
		if(resources.contains(id)){
			return resources.get(id).getInfo();
		}
		return null;
	}
	
	public void stop(String id){
		if(resources.contains(id)){
			resources.get(id).stop();
		}
		synchronized (instance) {
			instance.notifyAll();
		}
	}
	
	public void join() throws InterruptedException{
		synchronized (instance) {
			instance.wait();
		}
	}
	
	
	public void startService(String address,Class<?> targetClass){
		component.getDefaultHost().attach(address, targetClass);
	}
	
	public void stopService(String address){
		RouteList a = component.getDefaultHost().getRoutes();
        for(Route r:a){
        	if(r.getTemplate().getPattern().equals(address)){
        		a.remove(r);
        	}
        }
	}
	
	public void terminate() throws Exception{
		component.stop();
	}
	
}
