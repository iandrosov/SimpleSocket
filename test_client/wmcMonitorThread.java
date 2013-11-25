import java.io.*;
import java.util.*;


public class wmcMonitorThread extends Thread {

	private Hashtable m_ht = null;
	boolean m_stop = false;
	int m_interval = 0;

	public wmcMonitorThread(Hashtable ht, int interval)
	{
		m_ht = ht;
		if (interval == 0)
			m_interval = 30000;
		else
			m_interval = interval;
    }

	public void setStop(boolean s)
	{
		m_stop = s;
	}

    public void run()
    {
		try
		{
			// Wait untill time
			Thread.sleep(m_interval);

			// Signal all threads to stop its time to finish
			wmcClientThread val = null;
			Enumeration enum = m_ht.keys();
			while (enum.hasMoreElements())
			{
				String key = (String)enum.nextElement();
				val = (wmcClientThread)m_ht.get(key);

				val.Stop(true);
			}
			enum = null;
		}
		catch(Exception e){e.printStackTrace();}
	}

}
