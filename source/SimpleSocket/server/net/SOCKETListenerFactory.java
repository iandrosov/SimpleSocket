/* ----------------------------------------------------------------------------------
 * Package Name : SimpleSocket.server.net
 * Class Name   : SOCKETListenerFactory.java
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
 * 2002/10/12	Igor Androsov	Socet Listener factory
 *
 * ---------------------------------------------------------------------------------
 */
package SimpleSocket.server.net;


import com.wm.app.b2b.server.*;
import com.wm.util.JournalLogger;
import com.wm.util.Values;
import SimpleSocket.server.net.resources.ServerListenerExceptionBundle;

/**
 * @author Igor Androsov
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SOCKETListenerFactory
	implements ListenerFactory
{

    public static String provider = "Generic";
    public static String name = "SOCKET Listener";
    public static String protocol = "SOCKET";


	public SOCKETListenerFactory()
	{
	}

	public ServerListenerIf createListener(Values props)
		throws ServerListenerException
	{

        if(!props.containsKey("port"))
            throw new ServerListenerException(wm.server.net.resources.ServerListenerExceptionBundle.class, ServerListenerExceptionBundle.PORT_REQUIRED, "");

        int port = props.getInt("port");

        if(port == -1)
            throw new ServerListenerException(wm.server.net.resources.ServerListenerExceptionBundle.class, ServerListenerExceptionBundle.PORT_NUMBER_REQUIRED, "");

        if(port <= 0 || port > 65535)
        {
            JournalLogger.logError(1, 46, Integer.toString(port));
            throw new ServerListenerException(SimpleSocket.server.net.resources.ServerListenerExceptionBundle.class, ServerListenerExceptionBundle.INVALID_PORT, "");
        } else
        {
            return new SOCKETListener(props);
        }

	}

    public String getConfigURL()
    {
		return ("/SimpleSocket/configSOCKET.dsp");
	}

    public String getKey()
    {
		 return provider + "/" + protocol;
	}

    public String getName()
	{
		 return provider + " " + protocol + " Listener";
	}





}
