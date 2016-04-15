/*
 * Copyright (c) 2013, 2016, Oracle and/or its affiliates. All rights reserved.
 */

package connection;

import com.oracle.solaris.rad.connect.Connection;
import java.util.*;

/*
 * Base Factory Pattern for the various types of RAD connections.
 */

public interface ConnectionFactory {

	String testdir = System.getenv("SERVER_MODULES_DIR");
	int timeout = 139;
	List<String> modules = Arrays.asList(
/*			testdir + "/mod_test.so",
			testdir + "/mod_test_dict.so",*/
			"/usr/lib/rad/module/mod_pam.so",
/*			testdir + "/mod_test_enum.so",
			testdir + "/mod_test_list.so",*/
			"/usr/lib/rad/protocol/mod_proto_rad.so",
			"/usr/lib/rad/transport/mod_tls.so",
			"/usr/lib/rad/transport/mod_tcp.so");

	public Connection createConnection() throws Exception;
	public String getDescription();
}
