/* ----------------------------------------------------------------------------------
 * Package Name : com.net.server.protocol
 * Class Name   : ContentHandler.java
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
 * 2002/10/12	Igor Androsov	
 * 
 * ---------------------------------------------------------------------------------
 */
package com.net.server.protocol;

import java.net.*;
import java.io.*;

import com.wm.app.b2b.server.*;
import com.wm.data.*;


/**
 * @author Igor Androsov
 *
 * ContentHandler implementation. This class must be extended to implement 
 * the socket protocol
 */
public class ContentHandler extends Thread
{
	// Private Variables. 
	private Socket s;
    private String service_name;
	private String service_folder;
	
    
	/**
	 * Empty constructor. When using this constructor, it is necessary to call the 
	 * setSocket(Socket s) method to set the accpeted socket connection into the class.
	 * It is also necessary call the setSocketListener(SocketListener sl) to set the 
	 * listener object, as this object contains thread configurations.
	 *
	 */
	public ContentHandler()
	{
	}
    
	/**
	 * Constructor.
	 * @param s Accepted socket connection
	 */
	public ContentHandler(Socket s)
	{
		this.s = s;    
	}
    
	/**
	 * Set the accepted connection soket object
	 * @param s Accpeted socket
	 */
	public void setSocket(Socket s)
	{
		this.s = s;
	}
    
	/**
	 * Return status of connection soket object
	 * 
	 */    
	public boolean isClosed()
	{
		return this.s.isClosed(); 
	}
    
	public Socket getSocket()
	{
		return s;
	}
	public void setService(String svc)
	{
		//System.out.println("####### ContentHandler:setService - "+svc);
		String[] serviceData = svc.split(":");
		service_folder = serviceData[0];
		service_name = serviceData[1];
	}	
	public String getServiceName()
	{
		return service_name;
	}
	public String getServiceFolder()
	{
		return service_folder;
	}

	/**
	 * Return a Socket Input Stream. It must be used in the protocol implementation class
	 * extended from the DataHandler class.
	 */
	public InputStream getInputStream()
		throws IOException
	{
		return s.getInputStream();
	}
 
	/**
	 * Return a Socket Output Stream. It must be used in the protocol implementation class
	 * extended from the DataHandler class.
	 */    
	public OutputStream getOutputStream()
		throws IOException
	{
		return s.getOutputStream();
	}
    
	/**
	 * Close the socket connection.
	 */
	public void close()
		throws IOException
	{
		s.close();
		//sl.decrementThread();
	}
    
	/**
	 * Thread run method.
	 */
	public void run()
	{
 
		// Recreate the session in this thread
		InvokeState stt = new InvokeState();
		stt.setCheckAccess(false);
		InvokeState.setCurrentState(stt);       
        
		receiveData();   
	}
    
	/**
	 * Execute the IS service in synchronous mode. It is possible ro receive data 
	 * from the IS service.
	 * 
	 * @param folder Folder where the service is
	 * @param service Service name
	 * @param input Input IData object
	 * @return Output IData object
	 * @throws Exception Thrown if any error occours
	 */
	public IData executeServiceSynch(String folder, String service, IData input)
		throws Exception
	{
		IData output = Service.doInvoke(folder, service, input); 
		return output;
	}
    
    
	/**
	 * Execute the IS service in asynchronous mode. Used when it is not necessary 
	 * to receive any data from the invoked service.
	 * 
	 * @param folder Folder where the service is
	 * @param service Service name
	 * @param input Input IData object
	 * @throws ISRuntimeException
	 */
	public void executeServiceAsynch(String folder, String service, IData input)
		throws ISRuntimeException
	{
		Service.doThreadInvoke(folder, service, input);
	}
    
    
	/**
	 * To be implement by the DataHandler extension classes
	 *
	 */
	public void receiveData()
	{
		// Close the socket silently
		try
		{
			close();
		}
		catch(IOException e)
		{
			// Silent. Exception thrown if the socket is already closed
		}
	}
    
        
	/**
	 * Ran when this object is being destroyed.
	 */
	public void finalize()
	{
		try
		{
			s.close();
		}
		catch(Exception e)
		{
			// Do not do anything closing this socket in case of error
		}       
	}

}
