/* ----------------------------------------------------------------------------------
 * Package Name : SimpleSocket.server.net
 * Class Name   : SOCKETListener.java
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
package SimpleSocket.server.net;

import com.wm.app.b2b.server.*;
import com.wm.util.JournalLogger;
import com.wm.util.Values;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.*;
import com.net.server.*;

import SimpleSocket.server.net.resources.ServerListenerExceptionBundle;


/**
 * @author Igor Androsov
 *
 */
public class SOCKETListener
	extends Listener
{
    static final int DEFAULT_PORT = 4444;
    public static final int MIN_PORT = 0;
    public static final int MAX_PORT = 65535;
    public static int MAX_QUEUE = 65535;
    public static final String VALUE_KEYS[] = {
        "factory", "className", "provider", "port", "package", "factoryKey"
    };

    protected String provider;
    protected String protocol;
    protected ServerSocket sockServer;
    protected String classid;
    
    String plugin;
    int so_timeout; //Server Socket timeout value on accept.
	String service_name = "SimpleSocket:receive"; // Name of default service to execute

    public String getKey()
    {
        return classid + '@' + getPort();
    }

    public Values getProperties()
    {
        Values props = super.getProperties();
        props.put("factoryKey", getFactoryKey());
        props.put("configURL", ListenerAdmin.getFactory(getFactoryKey()).getConfigURL());
        props.put("provider", "Generic");
        
        props.put("plugin",plugin);
        //props.put("maxConn","10");
        
		props.put("so_timeout",Integer.toString(so_timeout));
        props.put("service_name", service_name);
        
        
        return props;
    }

    public SOCKETListener(Values properties)
    {
        super(properties);
        classid = "SOCKETListener";
   		int port = properties.getInt("port");
        if(port == -1)
            port = 4444;
        setPort(port);
        sockServer = null;
        setPackage(properties.getString("pkg"));
        setEnabled(properties.getBoolean("enabled"));
        
        // Set plugin
        plugin = properties.getString("plugin");
        
        // Set server socket timeout value
		so_timeout = properties.getInt("so_timeout");
		if (so_timeout == -1)
			so_timeout = 20000; // Set default timeout value to 20000 milliseconds

		// Set service name to execute
		service_name = properties.getString("service_name");

		if (service_name == null )
			service_name = "SimpleSocket:receive"; // Default service name
    }


    public SOCKETListener(int port)
    {
        //super(null);
        //classid = "SOCKETListener";
        if(port == -1)
            port = 4444;
        setPort(port);
        sockServer = null;
        //setEnabled(false);
	}

    protected static SOCKETListener createISSocketServerListener(String port)
    {
               
        try
        {
            SOCKETListener l = null;
            int p = Integer.parseInt(port.trim());
            if(p > 0 && p <= 65535)
                l = new SOCKETListener(p);
            return l;
        }
        catch(Exception exception)
        {
            //System.out.println("Error Creating SOCKET LISTENER...");
            JournalLogger.logError(1, 46, new Integer(port));
        }
        return null;
    }



    public String getProtocol()
    {
        return "SOCKET";
    }

    public String getFactoryKey()
    {
        return "Generic/SOCKET";
    }

    public String getStatus()
    {
        return isRunning() ? "Active" : "Inactive";
    }

    public void setup() 
    	throws ServerListenerException
    {
        try
        {
        	System.out.println("SOCKETListener - setup call.");
        	
            // Check if the plugin exists
            // If it throws error is caught by the exception
            Class.forName(plugin);
            
            // Bind 
            InetAddress bindAddr = getBindAddress();
            if(bindAddr != null)
                sockServer = new ServerSocket(getPort(), MAX_QUEUE, bindAddr);
            else
                sockServer = Server.getServerSocket(getPort(), MAX_QUEUE);
            // Set socket server accept timeout value    
            sockServer.setSoTimeout(so_timeout);
        }
        catch (ClassNotFoundException e)
        {
            throw new ServerListenerException(SimpleSocket.server.net.resources.ServerListenerExceptionBundle.class, ServerListenerExceptionBundle.ERROR_LOAD_PLUGIN, "", e.getMessage());
        }
        catch(Exception ioe)
        {
            throw new ServerListenerException(SimpleSocket.server.net.resources.ServerListenerExceptionBundle.class, ServerListenerExceptionBundle.ERROR_START_LISTEN, "", ioe.getMessage());
        }
    }

    public void cleanup() 
    	throws ServerListenerException
    {
		//System.out.println("SOCKETListener - cleanup call.");
        JournalLogger.logDebugPlus(1, 4, 46, Integer.toString(getPort()));
        if(sockServer != null)
            try
            {
				//System.out.println("SOCKETListener - close socket.");
                sockServer.close();
            }
            catch(IOException ioexception) { }
        sockServer = null;
    }

    public void loop()
    {
        if(sockServer == null)
            JournalLogger.logDebugPlus(1, 6, 46, Integer.toString(getPort()));
        else
            while(isRunning())
                try
                {
                    Socket conn = sockServer.accept();
                    if(conn != null)
                        if(isAccessAllowed(conn.getInetAddress()))
                        {

							///////////////////////////////
							// This code is to be changed for
							// custom socket communication

                            new ISSocketServerImpl(conn, plugin, service_name);
                            
                            // END OF MODS
                            /////////////////////////////////


                        } else
                        {
                            JournalLogger.logWarning(7, 46, conn.getInetAddress().toString());
                            conn.close();
                        }
                }
                catch(InterruptedIOException interruptedioexception) 
                { 
                    
                }
                catch(SocketException e)
                {
                    JournalLogger.logError(9998, 46, e);
                    JournalLogger.logError(27, 70, getKey());
                }
                catch(IOException e)
                {
                    JournalLogger.logError(9998, 46, e);
                    JournalLogger.logError(27, 70, getKey());
                }
                catch(Exception e)
                {
                    JournalLogger.logError(9998, 46, e);
                }
    }

}
