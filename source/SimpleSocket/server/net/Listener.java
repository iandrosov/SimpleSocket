/* ----------------------------------------------------------------------------------
 * Package Name : SimpleSocket.server.net
 * Class Name   : Listener.java
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
import com.wm.util.*;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Hashtable;
import SimpleSocket.server.net.resources.ServerListenerExceptionBundle;

public abstract class Listener
 implements Runnable, ServerListenerIf
{

 static final String DEF_PACKAGE = "SimpleSocket";


 protected Thread _thread;
 protected boolean _running;
 protected boolean _enabled;
 protected String _protocol;
 protected int _port;
 protected InetAddress _bindAddress;
 protected String _package;
 protected Hashtable _allowList;
 protected Hashtable _denyList;
 protected int _ipAccessMode;
 protected boolean _isAccessModeSet;

 public Listener()
 {
     _allowList = new Hashtable(11);
     _denyList = new Hashtable(11);
     _isAccessModeSet = false;
     _thread = null;
     _running = false;
     _enabled = false;
     _package = "SimpleSocket";
     _ipAccessMode = 0;
 }

 public Listener(Values props)
 {
     this();
     if(props != null)
     {
         String mode = (String)props.get("hostAccessMode");
         String allowlist[] = (String[])props.get("hostAllow");
         String denylist[] = (String[])props.get("hostDeny");
         if(mode != null)
         {
             boolean allowByDefault = !"exclude".equalsIgnoreCase(mode);
             if(allowByDefault)
             {
                 if(denylist != null)
                     _ipAccessMode = 1;
             } else
             if(allowlist != null)
                 _ipAccessMode = 2;
         }
         if(_ipAccessMode != 0)
             if(_ipAccessMode == 1)
             {
                 if(denylist != null)
                     setAccessList(denylist, false);
             } else
             if(_ipAccessMode == 2 && allowlist != null)
                 setAccessList(allowlist, true);
         String bindaddr = (String)props.get("bindAddress");
         try
         {
             if(bindaddr != null && bindaddr.length() > 0)
                 _bindAddress = InetAddress.getByName(bindaddr);
         }
         catch(Exception exception) { }
     }
 }

 public synchronized boolean isRunning()
 {
     return _running;
 }

 public boolean isListening()
 {
     return _running;
 }

 public boolean isEnabled()
 {
     return _enabled;
 }

 public boolean isPrimary()
 {
     return ListenerAdmin.isPrimaryListener(this);
 }

 public int getPort()
 {
     return _port;
 }

 public InetAddress getBindAddress()
 {
     return _bindAddress;
 }

 public String getPackage()
 {
     return _package;
 }

 public String getProtocol()
 {
     return _protocol;
 }

 public Thread getThread()
 {
     return _thread;
 }

 public synchronized void setRunning(boolean running)
 {
     _running = running;
 }

 public void setEnabled(boolean enable)
 {
     _enabled = enable;
 }

 public void setThread(Thread thread)
 {
     _thread = thread;
 }

 public void setPort(int port)
 {
     _port = port;
 }

 public void setBindAddress(InetAddress bindAddr)
 {
     _bindAddress = bindAddr;
 }

 public void setPackage(String pkg)
 {
     _package = pkg;
     if(_package == null)
         _package = "SimpleSocket";

 }

 public abstract void setup()
     throws ServerListenerException;

 public abstract void cleanup()
     throws ServerListenerException;

 public void startListening()
     throws ServerListenerException
 {
     if(isRunning())
         return;
     if(!isEnabled())
         throw new ServerListenerException(SimpleSocket.server.net.resources.ServerListenerExceptionBundle.class, ServerListenerExceptionBundle.LISTENER_NOT_ENABLED, "");
     try
     {
		 //System.out.println("Listener - startListening call.");
		
         Thread t = new Thread(this);
         setThread(t);
         setup();
         t.setName(getKey());
         t.start();
         setRunning(true);
         JournalLogger.logDebugPlus(1, 23, 70, getKey());
     }
     catch(ServerListenerException sle)
     {
         cleanup();
         throw sle;
     }
 }

 public void stopListening()
     throws ServerListenerException
 {
     try
     {
		//System.out.println("##### TEST");
		//System.out.println("Listener - stopListening call.");
		
         JournalLogger.logDebugPlus(1, 24, 70, getKey());
         setRunning(false);
         cleanup();
         Thread t = getThread();
         if(t != null)
             t.stop();
         setThread(null);
		 //System.out.println("Listener - thread stopped and set - null.");
     }
     catch(ServerListenerException serverlistenerexception) { }
 }

 public void finish()
     throws ServerListenerException
 {
     stopListening();
 }

 public abstract void loop();

 public void run()
 {
     try
     {
         setRunning(true);
         JournalLogger.logDebugPlus(1, 17, 70, getKey());
         PortManager.addListener(getKey());
         loop();
         try
         {
             cleanup();
         }
         catch(ServerListenerException sle)
         {
             sle.printStackTrace();
         }
         setRunning(false);
         setThread(null);
     }
     finally
     {
         JournalLogger.logDebugPlus(1, 18, 70, getKey());
         PortManager.removeListener(getKey());
     }
 }

 public Values getProperties()
 {
     Object o[][] = {
         {
             "port", new Integer(getPort())
         }, {
             "protocol", getProtocol()
         }, {
             "pkg", getPackage()
         }, {
             "enabled", (new Boolean(isEnabled())).toString()
         }, {
             "key", getKey()
         }
     };
     Values v = new Values(o);
     if(_ipAccessMode != 0)
     {
         v.put("hostAccessMode", _ipAccessMode != 1 ? "exclude" : "include");
         v.put("hostAllow", getAccessList(true));
         v.put("hostDeny", getAccessList(false));
     }
     if(_bindAddress != null)
         v.put("bindAddress", _bindAddress.getHostAddress());
     return v;
 }

 public boolean isAccessAllowed(InetAddress iAddress)
 {
     String address = iAddress.getHostAddress();
     boolean allowed;
     switch(_ipAccessMode)
     {
     case 1: // '\001'
         if(_denyList.size() > 0)
         {
             String host = iAddress.getHostName();
             for(Enumeration e = _denyList.keys(); e.hasMoreElements();)
             {
                 StringMatcher sm = new StringMatcher((String)e.nextElement());
                 if(sm.match(host.toLowerCase()) || sm.match(address))
                 {
                     JournalLogger.logDebug(25, 70, new Object[] {
                         getKey(), iAddress.getHostAddress()
                     });
                     return false;
                 }
             }

         }
         return true;

     case 2: // '\002'
         if(_allowList.size() > 0)
         {
             String host = iAddress.getHostName();
             for(Enumeration e = _allowList.keys(); e.hasMoreElements();)
             {
                 StringMatcher sm = new StringMatcher((String)e.nextElement());
                 if(sm.match(host.toLowerCase()) || sm.match(address))
                     return true;
             }

         }
         JournalLogger.logDebug(25, 70, new Object[] {
             getKey(), iAddress.getHostAddress()
         });
         return false;

     case 0: // '\0'
         allowed = Server.connectAllowed(iAddress);
         if(!allowed)
             JournalLogger.logDebug(25, 70, new Object[] {
                 getKey(), iAddress.getHostAddress()
             });
         return allowed;
     }
     allowed = Server.connectAllowed(iAddress);
     if(!allowed)
         JournalLogger.logDebug(25, 70, new Object[] {
             getKey(), iAddress.getHostAddress()
         });
     return allowed;
 }

 public String[] getAccessList(boolean allowedList)
 {
     String accessList[] = null;
     if(allowedList)
     {
         Enumeration e = _allowList.keys();
         accessList = new String[_allowList.size()];
         int i = 0;
         while(e.hasMoreElements()) 
             accessList[i++] = (String)e.nextElement();
     } else
     {
         Enumeration e = _denyList.keys();
         accessList = new String[_denyList.size()];
         int i = 0;
         while(e.hasMoreElements()) 
             accessList[i++] = (String)e.nextElement();
     }
     return accessList;
 }

 public void setAccessList(String hostList[], boolean allowedList)
 {
     if(hostList == null)
         return;
     for(int i = 0; i < hostList.length; i++)
     {
         String host = hostList[i];
         if(host != null)
             if(allowedList)
                 _allowList.put(hostList[i].toLowerCase(), "");
             else
                 _denyList.put(hostList[i].toLowerCase(), "");
     }

 }

 public void deleteFromAccessList(String hostName, boolean allowedList)
 {
     if(allowedList)
         _allowList.remove(hostName.toLowerCase());
     else
         _denyList.remove(hostName.toLowerCase());
 }

 public synchronized void setIPAccessMode(int accessMode)
 {
     _ipAccessMode = accessMode;
 }

 public synchronized int getIPAccessMode()
 {
     return _ipAccessMode;
 }

 public abstract String getKey();

 public abstract String getStatus();
}