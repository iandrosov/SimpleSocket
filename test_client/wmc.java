import java.io.*;
import java.net.*;
import java.util.*;

public class wmc {


public static void main(String[] args) throws IOException
{
		//Usage: <thread count> <total time to run sec> <host> <port> <name> <time interval log>

		String host_name = "localhost";

		String thd_count = args[0];
		int thrd = Integer.parseInt(thd_count);
		String my = args[1];
		String host = args[2];
		if (host != null)
			host_name = host;
		String s_port = args[3];
		int port = Integer.parseInt(s_port); //7777

		String test_name = args[4];
		int stat_interval = Integer.parseInt(args[5]);
		stat_interval = stat_interval * 1000;
	//int total = 0;
	int cnt = Integer.parseInt(my);//10000;
	cnt = cnt * 1000; //convert from sec to milliseconds
	//long total_byte = 0;

    Date currentTime = new Date();
	String startTime = currentTime.toString();
	//System.out.println("Timestamp: " + startTime + "\n\n");

	main_log("Timestamp: " + startTime + "\r\n", test_name);

	try
	{
		Hashtable ht = new Hashtable();

		// Start Threads
		for (int i = 0; i < thrd; i++)
		{
			wmcClientThread p = new wmcClientThread(cnt,host_name,port,test_name);
			p.start();

			ht.put(p.getName(),p);

		}

		// Start monitor
		wmcMonitorThread mon = new wmcMonitorThread(ht, cnt);
		mon.start();

		wmcStatThread stat = new wmcStatThread(ht, stat_interval, test_name);
		stat.start();

		wmcClientThread val = null;
		Enumeration enum = ht.keys();
		while (enum.hasMoreElements())
		{
			String key = (String)enum.nextElement();
			val = (wmcClientThread)ht.get(key);
			val.join();
		}
		enum = null;
		ht.clear();

		//stop monitor
		stat.setStop(true);
		//System.out.println("### Got END stop stats thread wait for threads to end");
		mon.join();
		stat.join();
		//System.out.println("### thraeds endnded EXIT");
		//System.exit(0);
	}
	catch(Exception e) {e.printStackTrace();}


	//System.out.println("Total calls - " + Integer.toString(total) + " Total Bytes - " + Long.toString(total_byte));
    currentTime = new Date();
  	startTime = currentTime.toString();
  	//System.out.println("Timestamp: " + startTime + "\n\n");
  	main_log("Timestamp: " + startTime + "\r\n", test_name);

}

	public static void main_log(String str, String file)
	{
		String file_path = "log" + File.separator + "MAIN_" + file + ".txt";
		try
		{
			FileWriter fw = new FileWriter(file_path, true);
			fw.write(str+"\r\n");
			fw.close();
			fw = null;
		}
		catch (Exception e){e.printStackTrace();}
	}


}

