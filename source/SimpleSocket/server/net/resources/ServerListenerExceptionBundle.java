/* ----------------------------------------------------------------------------------
 * Package Name : SimpleSocket.server.net.resources
 * Class Name   : ServerListenerExceptionBundle.java
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
 * 2002/10/12	Igor Androsov	Resource bundle for Exception handling
 *
 * ---------------------------------------------------------------------------------
 */
package SimpleSocket.server.net.resources;

import com.wm.util.B2BListResourceBundle;

public class ServerListenerExceptionBundle extends B2BListResourceBundle
{

    public static final String INVALID_GLOBAL_SERVICE = String.valueOf(9001);
    public static final String INVALID_DEFAULT_SERVICE = String.valueOf(9002);
    public static final String EMAIL_LOGGED_IN = String.valueOf(9003);
    public static final String EMAIL_ENABLE_FAILED = String.valueOf(9004);
    public static final String HOST_REQUIRED = String.valueOf(9005);
    public static final String TYPE_REQUIRED = String.valueOf(9006);
    public static final String USERNAME_REQUIRED = String.valueOf(9007);
    public static final String PASSWORD_REQUIRED = String.valueOf(9008);
    public static final String ERROR_START_LISTEN = String.valueOf(9009);
    public static final String PORT_NUMBER_REQUIRED = String.valueOf(9010);
    public static final String INVALID_PORT = String.valueOf(9011);
    public static final String PORT_REQUIRED = String.valueOf(9012);
    public static final String PRIVKEY_NOT_SPECIFIED = String.valueOf(9013);
    public static final String PRIVKEY_FILE_NOT_EXIST = String.valueOf(9014);
    public static final String BAD_CERTIFICATE = String.valueOf(9017);
    public static final String LISTENER_NOT_ENABLED = String.valueOf(9015);
    public static final String LISTENER_REQUIRED = String.valueOf(9016);
    public static final String ERROR_LOAD_PLUGIN = String.valueOf(9018);
    static final Object contents[][] = {
        {
            INVALID_GLOBAL_SERVICE, "Invalid global service {0}"
        }, {
            INVALID_DEFAULT_SERVICE, "Invalid default service {0}"
        }, {
            EMAIL_LOGGED_IN, "Enable failed: Could not log into account {0}@{1}"
        }, {
            EMAIL_ENABLE_FAILED, "Enable failed: {0}"
        }, {
            HOST_REQUIRED, "Must provide Server Host"
        }, {
            TYPE_REQUIRED, "Must provide Server Type"
        }, {
            USERNAME_REQUIRED, "Must provide User Name"
        }, {
            PASSWORD_REQUIRED, "Must provide Password"
        }, {
            ERROR_START_LISTEN, "Error starting to listen: {0}"
        }, {
            PORT_NUMBER_REQUIRED, "A number must be provided for the port"
        }, {
            INVALID_PORT, "An invalid port number was provided"
        }, {
            PORT_REQUIRED, "A port number must be provided"
        }, {
            PRIVKEY_NOT_SPECIFIED, "Private key not specified"
        }, {
            PRIVKEY_FILE_NOT_EXIST, "Private key file does not exist"
        }, {
            LISTENER_NOT_ENABLED, "Listener is not enabled"
        }, {
            LISTENER_REQUIRED, "A listener must be provided"
        }, {
            BAD_CERTIFICATE, "Could not process certificate {0}"
        }, {
            ERROR_LOAD_PLUGIN, "Could not load plugin: {0}"
        }
    };

    public ServerListenerExceptionBundle()
    {
    }

    public Object[][] getContents()
    {
        return contents;
    }

    public String getProdComponent()
    {
        return "S";
    }

    public int getFacility()
    {
        return 70;
    }

}