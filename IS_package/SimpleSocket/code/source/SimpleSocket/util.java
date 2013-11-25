package SimpleSocket;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2005-01-13 17:36:17 JST
// -----( ON-HOST: xiandros-c640

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
import java.util.*;
import com.wm.util.ByteOutputBuffer;
// --- <<IS-END-IMPORTS>> ---

public final class util

{
	// ---( internal utility methods )---

	final static util _instance = new util();

	static util _newInstance() { return new util(); }

	static util _cast(Object o) { return (util)o; }

	// ---( server methods )---




	public static final void test (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(test)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:required stream
		// [o] object:0:required bytes
		
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			InputStream	stream = (InputStream)IDataUtil.get( pipelineCursor, "stream" );
		pipelineCursor.destroy();
		
		        byte b[] = new byte[8192];
		        ByteOutputBuffer out = new ByteOutputBuffer();
		        int i;
		        try
		        {
		            while((i = stream .read(b)) > 0) 
		                out.write(b, 0, i);
		        }
		        catch(EOFException eofexception) { }
			catch(IOException ioe) {}
			try
		        {
		        byte bytes[] = out.toByteArray();
		pipelineCursor = pipeline.getCursor();
		        pipelineCursor.last();
		        pipelineCursor.insertAfter("bytes", bytes);
		pipelineCursor.destroy();
		        stream.close();
			}catch(Exception exception) { }
		// --- <<IS-END>> ---

                
	}
}

