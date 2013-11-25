/* ----------------------------------------------------------------------------------
 * Package Name : com.net.server.protocol
 * Class Name   : InvokeProtocol.java
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
 * 2002/10/12	Igor Androsov	Sample protocol class demonstarting use of socket
 * 				
 * ---------------------------------------------------------------------------------
 */
package com.net.server.protocol;

import java.io.IOException;
import java.io.*;
import java.util.*;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

/**
 * @author Igor Androsov
 *
 * The InvokeProtocol class implementation of the protocol class
 * to invoke any IS service synchronos mode, send data and get
 * response data back from the service to return it to client.
 * 
 * This protocol format provides service name to invoke as part
 * of the data send to socket. It allows client to control what
 * service to invoke on IS based on protocol
 * 
 * Sample data:
 * "service=SimpleSocket:test;reqData=data:DATA ELEMENTS GO HERE"
 */
public class InvokeProtocol extends ContentHandler
{
	/**
	 * Constructs a new InvokeProtocol default constructor
	 */
	public InvokeProtocol()
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
		BufferedReader in = null;
		PrintWriter out   = null;
		
		try
		{
			// Get the socket input stream
			is = getInputStream();
			os = getOutputStream();

			out = new PrintWriter(os, true);
			in = new BufferedReader(new InputStreamReader(getInputStream()));

			//String inputLine;
			StringBuffer inputLine = new StringBuffer(64000);
			
			// Loop to read lines of data while the socket is opened
			while((inputLine.append(in.readLine()).length()) != 0)
			{
				// Tokenize and get service name and data to call the service
				StringTokenizer st = new StringTokenizer(inputLine.toString(), ";");

				//Jump first token because its service name
				st.nextElement();

				// Create the IData input object
				IData input = IDataFactory.create();
				IDataCursor inputCursor = input.getCursor();

				while(st.hasMoreElements())
				{
					String element = (String)st.nextElement();

					String[] data = element.split("=");
					IDataUtil.put( inputCursor, data[0], data[1]);

				}
				inputCursor.destroy();

				//System.out.println("Read Data");

				String serviceFullName = getServiceName(inputLine.toString());
				String[] serviceData = serviceFullName.split(":");

				IData 	output = IDataFactory.create();
				try {

					// Invoke service on IS
					//System.out.println("Invoke - "+serviceData[0]+":"+serviceData[1]);
					output = executeServiceSynch(serviceData[0], serviceData[1], input);

					// Send output
					out.write("### Data returned: "+ getResult(output) + "\r\n");
					out.flush();
					
					//System.out.println("Close ALL");
					inputLine.setLength(0); // Clear string buffer
					is.close();
					out.close();
					os.close();
					in.close();
            		
					//Close socket from server
					if (!isClosed())
						close();
					//System.out.println("Close Socket");
					break;

				}
				catch( Exception e)
				{
					e.printStackTrace();
				}

			} // End of WHILE LOOP
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
				if (in != null)
					in.close();
				
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

	private String getServiceName(String inputString)
	{
		// Service Name should be the first token
		StringTokenizer st = new StringTokenizer(inputString, ";");
		String firstElement = (String)st.nextElement();

		if(firstElement == null)
			return null;

		// Check it the first token is the service name
		StringTokenizer st2 = new StringTokenizer(firstElement, "=");

		if(st2.nextElement().equals("service"))
			return (String)st2.nextElement();
		else
			return null;


	}
	
}
