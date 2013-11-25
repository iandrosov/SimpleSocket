/* ----------------------------------------------------------------------------------
 * Package Name : com.net.ISSocketServer
 * Class Name   : ISSocketServerImpl.java
 * 
 * ---------------------------
 * Created by    : Igor Androsov
 * Creation date : 2002/10/12
 * ---------------------------
 * 
 * Change log:
 * 
 * Date			Author			Description
 * --------------------------------------------------------------------------------
 * 2002/10/12	Igor Androsov	Implementation of Socket server
 * 						
 * 
 * ---------------------------------------------------------------------------------
 */
package com.net.server;

import java.net.Socket;

import com.net.server.protocol.*;

/**
 * @author iandrosov
 *
 * Port Listener Thread
 */
public class ISSocketServerImpl 
{
	private Socket s = null;
	private String plugin;
    private String svc;
    
	private ContentHandler ch = null;
  
	int count = 0;

    
    
	public ISSocketServerImpl(Socket s, String plugin)
	{
		this.s = s;
		this.plugin = plugin;
        
		// Run
		spanThread();
	}

	public ISSocketServerImpl(Socket s, String plugin, String svc_name)
	{
		this.s = s;
		this.plugin = plugin;
        this.svc = svc_name;
        
		// Run
		spanThread();
	}
    
	public void setSocket(Socket s)
	{
		this.s = s;
	}
    
	public void setPlugin(String plugin)
	{
		this.plugin = plugin;
	}
    
	public synchronized void decrementThread()
	{
		count--;
	}
    
    
	public void spanThread()
	{              
		try
		{
			Class cls = Class.forName(plugin);                
			ch = (ContentHandler)cls.newInstance();

		}
		catch(ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch(Exception ex)
		{
			// Handle the instantiations exceptions
                    
		}
		ch.setSocket(s);
		ch.setService(svc);
		ch.start();
	}

}
