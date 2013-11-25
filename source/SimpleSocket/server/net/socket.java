/* ----------------------------------------------------------------------------------
 * Package Name : SimpleSocket.server.net
 * Class Name   : socket.java
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
 * 2002/10/12	Igor Androsov	SOcket server startup.shutdown service utilities
 *
 * ---------------------------------------------------------------------------------
 */
package SimpleSocket.server.net;

import com.wm.app.b2b.server.*;
import com.wm.util.Values;

public final class socket
{

    public static final String QUEUE_PROPERTY = "watt.server.portQueue";
    public static final String MAIN_PROPERTY = "watt.server.port";
    public static final String LIST_PROPERTY = "watt.server.portList";
    public static final int SOCKETLS0008 = 8;

    public socket()
    {
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }



    public static Values init(Values in)
    	throws ServiceException
    {
        Values out = in;
        try
        {
            ListenerAdmin.registerFactory(new SOCKETListenerFactory(), "SimpleSocket");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        String mq = System.getProperty("watt.server.portQueue", "65534");
        try
        {
            SOCKETListener.MAX_QUEUE = Integer.parseInt(mq);
        }
        catch(Exception e)
        {
            SOCKETListener.MAX_QUEUE = 65534;
            Server.logError(e);
        }
        return out;
    }

    public static Values shutdown(Values in)
    	throws ServiceException
    {
        Values out = in;
        try
        {
            ListenerAdmin.unregisterFactory("Generic/SOCKET");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return out;
    }
}