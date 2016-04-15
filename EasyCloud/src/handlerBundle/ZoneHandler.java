package handlerBundle;
import com.oracle.solaris.rad.client.*;
import com.oracle.solaris.rad.connect.*;
import com.oracle.solaris.rad.zonemgr.*;

import connection.ConnContainer;

import java.util.*;
import java.io.*;

public class ZoneHandler {
	public static void main(String[] args) throws Exception {
		System.out.println("hello");
		try {
			
			Connection con = ConnContainer.getConnection("sct-x42-18");

			List<ADRName> zoneList = con.listObjects(new Zone());
			for (ADRName name: zoneList) {
				Zone zone = con.getObject(name);
				String zonepath = "";
				String ip = "exclusive";

				Resource global_filter = new Resource("global", null, null);
				List<Property> props = zone.getResourceProperties(global_filter, null);

				for (Property prop: props) {
					if ( prop.getName().equals("zonepath")) {
						zonepath = prop.getValue();
					}
					if (prop.getName().equals("ip-type")) {
						ip = prop.getValue();
					}
				}
				System.out.format("%-16s %-11s %-16s %-10s %-10s\n",
					zone.getName(), zone.getstate(), zonepath, zone.getbrand(), ip);

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
