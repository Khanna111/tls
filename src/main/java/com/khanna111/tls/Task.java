package com.khanna111.tls;

import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.concurrent.Callable;

import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author gkhanna
 *
 */
public class Task implements Callable<String> {

    private final static Logger LOGGER = LoggerFactory.getLogger(Task.class);

    private static ThreadLocal<StringBuilder> printStr = new ThreadLocal<>();

    private String hostName;
    private int port;
    private int rank;

    private AppUtil appUtil;

    public Task(int rank, String hostName, int port, AppUtil appUtil) {
	this.rank = rank;
	this.hostName = hostName;
	this.port = port;
	this.appUtil = appUtil;
    }

    public String call() {
	try {
	    printStr.set(new StringBuilder());
	    printStr.get().append("**rank:" + rank);
	    printStr.get().append("**HostName:" + hostName);
	    start();
	    return printStr.get().toString();
	}
	catch (Throwable t) {
	    LOGGER.warn("Throwable at the task top - {} - {} - {} ", printStr.get().toString(), t.getMessage(),
		    t.getClass().getName());
	    LOGGER.debug("", t);
	    return printStr.get().append("**").append("Exception: " + t.getMessage() + " -- " + t.getClass().getName())
		    .toString();
	}
	finally {
	    printStr.remove();
	}
    }

    private void start() throws Exception {
	SSLSocketFactory factory = createSSLSocketFactory();
	SSLSocket socket = (SSLSocket) factory.createSocket();
	socket.connect(new InetSocketAddress(hostName, port),
		Integer.parseInt(appUtil.getAppConfig().getConnectTimeOutMs()));
	socket.setSoTimeout(Integer.parseInt(appUtil.getAppConfig().getReadTimeOutMs()));

	socket.addHandshakeCompletedListener(getHandshakeCompletedListener());
	socket.startHandshake();
	String cipherSuite = socket.getSession().getCipherSuite();
	String protocol = socket.getSession().getProtocol();
	Certificate[] peerCerts = socket.getSession().getPeerCertificates();
	printStr.get().append("===").append("SN confirm: " + ((X509Certificate) peerCerts[0]).getSerialNumber());
	printStr.get().append("**")
		.append("DN confirm: " + ((X509Certificate) peerCerts[0]).getSubjectX500Principal().getName());
	printStr.get().append("**")
		.append("Issuer confirm: " + ((X509Certificate) peerCerts[0]).getIssuerX500Principal().getName());
	printStr.get().append("**").append("CipSui: " + cipherSuite);
	printStr.get().append("**").append("ProVer: " + protocol);

    }

    private static SSLSocketFactory createSSLSocketFactory() throws Exception {

	SSLContext sslContext = SSLContext.getInstance("TLS");
	sslContext.init(null, CERT_TM, new SecureRandom());
	return sslContext.getSocketFactory();
    }

    private HandshakeCompletedListener getHandshakeCompletedListener() {
	return hCL;
    }

    private static final HandshakeCompletedListener hCL = (e) -> {
	// printStr.get().append("**").append(e.getCipherSuite());
	// printStr.get().append("**").append(e.getSession().getProtocol());

    };

    private static final TrustManager[] CERT_TM = new TrustManager[] { new X509TrustManager() {
	public X509Certificate[] getAcceptedIssuers() {
	    return new X509Certificate[0];
	}

	public void checkClientTrusted(X509Certificate[] chain, String authType) {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) {
	    for (X509Certificate cert : chain) {
		LOGGER.trace("-------------\n" + cert.toString() + "\n-------------\n");
		StringBuilder strB = new StringBuilder();
		strB.append("\n******************\n");
		strB.append("DN: " + cert.getSubjectX500Principal().getName() + "\n");
		printStr.get().append("===").append("DN: " + cert.getSubjectX500Principal().getName());

		strB.append("SN: " + cert.getSerialNumber() + "\n");
		printStr.get().append("**").append("SN: " + cert.getSerialNumber());

		strB.append("PK Algo: " + cert.getPublicKey().getAlgorithm() + "\n");
		printStr.get().append("**").append("PK Algo: " + cert.getPublicKey().getAlgorithm());

		strB.append("Issuer DN: " + cert.getIssuerX500Principal().getName() + "\n");
		printStr.get().append("**").append("Issuer DN: " + cert.getIssuerX500Principal().getName());

		strB.append("BasicConstraints: " + cert.getBasicConstraints() + "\n");
		printStr.get().append("**").append("BasicConstraints: " + cert.getBasicConstraints());

		strB.append("\n******************\n");

		LOGGER.debug(strB.toString());
	    }

	}
    } };

}
