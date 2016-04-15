package connection;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.oracle.solaris.rad.client.*;
import com.oracle.solaris.rad.connect.*;
import com.oracle.solaris.rad.connect.Connection;
import com.oracle.solaris.rad.transports.RadTrustManager;
import java.io.*;
import java.net.*;
import java.security.cert.*;
import java.security.KeyStore;
import java.util.*;

public class TLSConnection implements ConnectionFactory  {
	private static String CERT_FILENAME = "/etc/rad/cert.pem";
	private static String KEY_FILENAME = "/etc/rad/key.pem";
	private String TRUSTSTORE_FILENAME = "/export/truststore";
	private String TRUSTSTORE_PASSWORD = "l1admin";


	String rootPassword = null;
	private Connection privateConn;

	
	/**
	 * If the client requires authentication the root password can be
	 *  set with this constructor.
	 * @return 
	 */
	public TLSConnection(String rootPassIn) throws Exception {
		rootPassword = rootPassIn;
	}

	public Connection createConnection(String hostIP) throws Exception {

		int port = LocalTCPConnection.getFreePort();
		List<String> auxargs = Arrays.asList("-t",
				"tls:port=" +
				Integer.toString(port) +
				",localonly=false," +
				"certificate=" +  CERT_FILENAME +
				",privatekey=" + KEY_FILENAME);
		privateConn = Connection.connectPrivate("/",
							modules, auxargs);
		InetAddress addr = Inet4Address.getByName("127.0.0.1");
		Connection conn = createConnection("localhost", addr, port);
		if (rootPassword != null) {
			RadPamHandler hdl = new RadPamHandler(conn);
			hdl.login("C", "root", rootPassword);
		}
		return conn;
		
	}


	/**
	 * Connects to localhost using the default port, key, and cert
	 */
	public Connection createConnection() throws Exception {
		// Create a private instance so we can make a
		// connection
		int port = LocalTCPConnection.getFreePort();
		List<String> auxargs = Arrays.asList("-t",
				"tls:port=" +
				Integer.toString(port) +
				",localonly=false," +
				"certificate=" +  CERT_FILENAME +
				",privatekey=" + KEY_FILENAME);
		privateConn = Connection.connectPrivate("/",
							modules, auxargs);
		InetAddress addr = Inet4Address.getByName("127.0.0.1");
		Connection conn = createConnection("localhost", addr, port);
		if (rootPassword != null) {
			RadPamHandler hdl = new RadPamHandler(conn);
			hdl.login("C", "root", rootPassword);
		}
		return conn;

	}

	/**
	 * Connects to host using the default key, and cert
	 */
	public Connection createConnection(String hostname, InetAddress addr,
					   int port) throws Exception {
		Connection conn = null;
		KeyStore keystore = createKeyStore();
		conn = Connection.connectTLS(addr, hostname, port,
						keystore, null);
		if (rootPassword != null) {
			RadPamHandler hdl = new RadPamHandler(conn);
			hdl.login("C", "root", rootPassword);
		}
		return conn;
	}

	public Connection createConnection(String hostname, InetAddress addr,
					   int port, String keyFile,
					   String certFile) throws Exception {
		Connection conn = null;

		CERT_FILENAME = certFile;
		KEY_FILENAME = keyFile;

		KeyStore keystore = createKeyStore();
		conn = Connection.connectTLS(addr, hostname, port,
						keystore, null);
		if (rootPassword != null) {
			RadPamHandler hdl = new RadPamHandler(conn);
			hdl.login("C", "root", rootPassword);
		}
		return conn;
	}

	private KeyStore createKeyStore() throws Exception {
		File trustStore = new File(TRUSTSTORE_FILENAME);
		File cert = new File(CERT_FILENAME);

		setUpTrustStore(trustStore);
		setUpCertificate(trustStore, cert);

		FileInputStream fis = new FileInputStream(TRUSTSTORE_FILENAME);
		KeyStore keyStore = KeyStore.getInstance(
						KeyStore.getDefaultType());
		keyStore.load(fis, TRUSTSTORE_PASSWORD.toCharArray());

		return keyStore;
	}
	private void setUpTrustStore(File trustStoreFile) throws Exception {
		if (trustStoreFile.exists()) {
			return;
		}

		KeyStore keyStore = KeyStore.getInstance(
						KeyStore.getDefaultType());
		char[] password = TRUSTSTORE_PASSWORD.toCharArray();

		// Create empty keystore
		keyStore.load(null, password);

		FileOutputStream fos = new FileOutputStream(trustStoreFile);
		keyStore.store(fos, password);
		fos.close();
	}

	private void setUpCertificate(File trustStoreFile, File certificateFile)
		throws Exception {

		KeyStore keyStore = KeyStore.getInstance(
						KeyStore.getDefaultType());
		char[] password = TRUSTSTORE_PASSWORD.toCharArray();

		// Load trust store
		FileInputStream fis = new FileInputStream(trustStoreFile);
		keyStore.load(fis, password);
		fis.close();

		FileInputStream certificateFis = new
			FileInputStream(certificateFile);
		Certificate certificate = CertificateFactory.
						getInstance("X.509").
			generateCertificate(certificateFis);
		certificateFis.close();

		// Add certificate
		String alias = ((X509Certificate) certificate).
						getIssuerDN().toString();
		KeyStore.Entry entry = new KeyStore.
			TrustedCertificateEntry(certificate);
		keyStore.setEntry(alias, entry, null);

		FileOutputStream fos = new FileOutputStream(trustStoreFile);
		keyStore.store(fos, password);
		fos.close();
	}


	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
