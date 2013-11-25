import java.io.*;
import java.util.*;


public class wmcStatThread extends Thread {

	private Hashtable m_ht = null;
	boolean m_stop = false;
	int m_interval = 0;
	String m_name = "";

	public wmcStatThread(Hashtable ht, int interval, String name)
	{
		m_ht = ht;
		if (interval == 0)
			m_interval = 30000;
		else
			m_interval = interval;

		m_name = name;
    }

	public void setStop(boolean s)
	{
		m_stop = s;
	}

    public void run()
    {
		try
		{
			double dblTPS = 0.0;
			long total_exec = 0;
			log_header(m_name);
			while(true)
			{
				long start = System.currentTimeMillis();

				Thread.sleep(m_interval);

				long end = System.currentTimeMillis();

				// Count TPS and time here
				wmcClientThread val = null;
				Enumeration enum = m_ht.keys();
				while (enum.hasMoreElements())
				{
					String key = (String)enum.nextElement();
					val = (wmcClientThread)m_ht.get(key);
					total_exec += val.get_total();
					val.resetTotal();
				}
				enum = null;

				long mil_time = end - start;
				double time_sec = mil_time / 1000;
				if (time_sec > 0)
					dblTPS = total_exec / time_sec;

				//System.out.println("Total - "+Long.toString(total_exec)+" Time mil - "+Long.toString(mil_time));
				//System.out.println("Time second - "+ Double.toString(time_sec) + " TPS - "+Double.toString(dblTPS));
				logTPS(time_sec, dblTPS, start, end, total_exec, m_name);

				total_exec = 0;
				mil_time = 0;
				time_sec = 0;

				if (m_stop == true)
					break;
			}
		}
		//catch(IOException ioe){ioe.printStackTrace();}
		catch(Exception e){e.printStackTrace();}
	}

	public void log_header(String file)
	{
		String file_path = "log" + File.separator + file + ".txt";
		try
		{
			String str_data = "Time,TPS,Start,End,TXN";
			FileWriter fw = new FileWriter(file_path, false);
			fw.write(str_data+"\r\n");
			fw.close();
			fw = null;
		}
		catch (Exception e){e.printStackTrace();}

	}

	public void logTPS(double t, double tps, long start, long end, long txn, String file)
	{
		String file_path = "log" + File.separator + file + ".txt";
		try
		{
			FileWriter fw = new FileWriter(file_path, true);
			//System.out.println("Time second - "+ Double.toString(t) + " TPS - "+Double.toString(tps));

			String str_data = Double.toString(t) + "," + Double.toString(tps)+","+Long.toString(start)+","+Long.toString(end)+","+Long.toString(txn);
			fw.write(str_data+"\r\n");
			fw.close();
			fw = null;
		}
		catch (Exception e){e.printStackTrace();}
	}

}
