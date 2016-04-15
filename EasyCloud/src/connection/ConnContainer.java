package connection;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.oracle.solaris.rad.client.*;
import com.oracle.solaris.rad.connect.*;
import com.oracle.solaris.rad.zonemgr.*;

public class ConnContainer {
	private static String cert_file_prefix="/etc/rad/cert.pem.";
	private static String user="root";
	private static String passwd="l1admin";

	private static Map<String, Connection> container=new HashMap<String, Connection>();
	public static Connection getConnection(String hostname) {

		Set<String> certs = new HashSet<String>();
		String cert_file = cert_file_prefix + hostname;
		certs.add(cert_file);
		
		if (container.containsKey(hostname))
			return  container.get(hostname);
		
		try {
			Connection con = Connection.connectTLS(hostname, certs);
			RadPamHandler hdl = new RadPamHandler(con);
			hdl.login("C", user, passwd);
			
			container.put(hostname, con);
			return con;

		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
			return null;
		}
		
	}
	public static void closeConnection(String hostname){
		if (container.containsKey(hostname)) {
			container.remove(hostname);
		}
	}

}
