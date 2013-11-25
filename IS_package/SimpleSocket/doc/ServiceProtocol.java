/* ----------------------------------------------------------------------------------
 * Package Name : com.net.server.protocol
 * Class Name   : ServiceProtocol.java
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

import java.io.IOException;
import java.io.*;
//import java.util.*;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

/**
 * @author Igor Androsov
 *
 * ServiceProtocol a default class implementation of the protocol class
 * to invoke handler IS service in synchronos mode, send stream 
 * data to flow service to be parsed and processed and get
 * response data back from the service to return it to client.
 * 
 * This protocol format provides only data. The service name to invoke 
 * is found in listener configuration. For all data sent to this port
 * same listener defined service will be invoked.
 * It allows server to control what service to invoke on IS. Client just
 * sends data to the socket.
 * 
 */
public class ServiceProtocol extends ContentHandler
{
	public ServiceProtocol()
	{
	}

	/**
	 * Mandatory to implement this method to retrieve data from the socket.
	 * If it is not implemented, the parent class empty method will be ran and the
	 * socket is closed without further notice.
	 */
	public void receiveData()
	{
		InputStream is    = null;
		OutputStream os   = null;
		//BufferedReader in = null;
		PrintWriter out   = null;
		
		try
		{
			// Get the socket input stream
			is = getInputStream();
			os = getOutputStream();

			out = new PrintWriter(os, true);
			//in = new BufferedReader(new InputStreamReader(getInputStream()));

			//String inputLine;
			//StringBuffer inputLine = new StringBuffer(64000);
			
			// Loop to read lines of data while the socket is opened
			//while((inputLine = in.readLine()) != null)
			//while((inputLine.append(in.readLine()).length()) != 0)
			//{
				// Create the IData input object
				IData input = IDataFactory.create();
				IDataCursor inputCursor = input.getCursor();

				IDataUtil.put( inputCursor, "stream", is);

				inputCursor.destroy();

				//System.out.println("Read Data");

				String serviceFolder = getServiceFolder();
				String serviceName = getServiceName();
				System.out.println("####### ServiceProtocol:receiveData - "+serviceName);
				
				IData 	output = IDataFactory.create();
				try{

					System.out.println("Invoke - "+serviceFolder+":"+serviceName);
					output = executeServiceSynch(serviceFolder, serviceName, input);

					System.out.println("Invoke FINISH");

					// Send output
					out.write("### Data returned: "+ getResult(output) + "\r\n");
					out.flush();
					
					System.out.println("flush data and Close ALL");
					//inputLine.setLength(0); // Clear string buffer
					//is.close();
					//out.close();
					//os.close();
					//in.close();
            		
					//Close socket from server
					//if (!isClosed())
					//	close();
					//System.out.println("Close Socket");
					//break;
					System.out.println("END Server stuff!!!");

				}
				catch( Exception e)
				{
					e.printStackTrace();
				}

			//if (inputLine.length() > 0)
			//  System.out.println("Socket DATA size - " + Integer.toString(inputLine.length()));

			//} // End of WHILE LOOP
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			System.out.println("Protocol error ");

			// break the connection
			try
			{
				close();
			}
			catch(IOException ex2)
			{}
		}
		finally {
			// break the connection
			try {
				//System.out.println("Call finally block");
				//if (in != null)
				//	in.close();
				
				if (out != null) 
					out.close();
				
				if (is != null)
					is.close();
				if (os != null)
					os.close();

				if (!isClosed())
					close();
				 
				
			}
			catch (Exception e) {
			}
			
		}
	}

	private String getResult(IData out)
	{

		IDataCursor idc = out.getCursor();

		String outputLine = IDataUtil.getString( idc, "reqData" );

		idc.destroy();

		return outputLine;
	}

}
