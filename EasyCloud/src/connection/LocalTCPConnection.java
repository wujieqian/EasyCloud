/*
 * Copyright (c) 2013, 2014, Oracle and/or its affiliates. All rights reserved.
 */

package connection;

import com.oracle.solaris.rad.client.RadPamHandler;
import com.oracle.solaris.rad.connect.Connection;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Connection factory for Local TCP RAD connections using loopback interface.
 */

public class LocalTCPConnection implements ConnectionFactory {

	private Connection privateConn;
	private int port;
	private String rootPassword = null;

	public LocalTCPConnection() throws IOException {
		// Create a private instance so we can make a
		// connection
		port = getFreePort();
		List<String> auxargs = Arrays.asList("-t",
			"tcp:localonly=false,port=" + port);
		privateConn = Connection.connectPrivate("/",
							modules, auxargs);
	}

	/**
	 * If the client requires authentication the root password can be
	 *  set with this constructor.
	 */
	public LocalTCPConnection(String rootPassIn) throws IOException {
		this();
		rootPassword = rootPassIn;
	}

	/* Default TCP inet4 connection */
	public Connection createConnection() throws IOException  {
		InetAddress addr = Inet4Address.getByName("127.0.0.1");
		Connection conn = Connection.connectTCP(addr, port);
		if (rootPassword != null) {
			RadPamHandler hdl = new RadPamHandler(conn);
			// First argument is locale
			hdl.login("C", "root", rootPassword);
		}
		return conn;
	}

	/* Option to allow TCP inet6 connection */
	public Connection createConnection(boolean ipv6) throws IOException  {
		if (!ipv6) {
			return this.createConnection();
		}
		InetAddress addr = Inet6Address.getByName("[::1]");
		Connection conn = Connection.connectTCP(addr, port);
		if (rootPassword != null) {
			RadPamHandler hdl = new RadPamHandler(conn);
			// First argument is locale
			hdl.login("C", "root", rootPassword);
		}
		return conn;
	}

	public String getDescription() {
		return "loopback_tcp_connection";
	}

	public void cleanup() {
		try {
			if (privateConn != null && privateConn.isOpen()) {
				privateConn.close();
			}
		} catch (Exception e) {
			// Do nothing
		}
	}

	public static int getFreePort() throws IOException {
		ServerSocket server = new ServerSocket(0);
		int port = server.getLocalPort();
		server.close();
		return port;
	}

	// Probably not needed, just in case someone forgets to
	// run cleanup we'll take care of it at GC time.
	protected void finalize() throws Throwable {
		cleanup();
		super.finalize();
	}
}
