/**
 * 
 */
package SupportLibraries;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;



/**
 * Responsible to store and retrieve session data during execution.
 * 
 * Typical use case is to store data like AppNum or CaseNum after the application registration
 * and use the same AppNum or CaseNum throughout the driver flow.
 * 
 * @author skandacharam
 *
 */
final public class DriverSession {

	private static final DriverSession session = new DriverSession();
	
	private static final Logger logger = Logger.getLogger(DriverSession.class.getName());
	
	private Map<String, Object> cache = new HashMap<String, Object>();
	
	public static DriverSession getInstance(){
		return session;
	}
	
	/*
	 * Store value to the session with the given key
	 */
	public void add(String key, Object value){
		cache.put(key, value);
		logger.info("Stored key:"+key+" value:"+value+" into session");
	}
	
	/*
	 * Read session data for the given key
	 */
	public Object get(String key){
		logger.info("Reading key:"+key+" from session");
		return cache.get(key);
	}
	
	/*
	 * Remove the session data stored with the given key
	 */
	public void remove(String key){
		cache.remove(key);
		logger.info("Removed key:"+key+" from session");
	}
	
	/*
	 * Clear the session data
	 */
	public void clear(){
		logger.info("Clearing the session");
		cache.clear();
	}
	
}
