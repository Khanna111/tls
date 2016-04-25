package com.khanna111.tls;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

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
 */
public class App {
    private final static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
	LOGGER.debug("Hello World!");
	App app = new App();
	app.start();
    }

    private void start() {
	try {
	    SSLSocketFactory factory = createSSLSocketFactory();
	    SSLSocket socket = (SSLSocket) factory.createSocket("symantec.com", 443);
	    socket.addHandshakeCompletedListener(getHandshakeCompletedListener());
	    socket.startHandshake();

	}
	catch (Exception e) {
	    LOGGER.info("Exception raised at start() : ", e);
	}

    }

    private static SSLSocketFactory createSSLSocketFactory() throws Exception {
	TrustManager[] getCertTM = new TrustManager[] { new X509TrustManager() {
	    public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	    }

	    public void checkClientTrusted(X509Certificate[] chain, String authType) {
	    }

	    public void checkServerTrusted(X509Certificate[] chain, String authType) {
		for (X509Certificate cert : chain) {
		    LOGGER.trace("-------------\n" + cert.toString() + "\n-------------\n" );
		    StringBuilder strB = new StringBuilder();
		    strB.append("\n******************\n");
		    strB.append("DN: " +cert.getSubjectX500Principal().getName() + "\n");
		    strB.append("SN: " +cert.getSerialNumber() + "\n");
		    strB.append("Issuer DN: " +cert.getIssuerX500Principal().getName() + "\n");
		    strB.append("BasicConstraints: " +cert.getBasicConstraints() + "\n");
		    strB.append("\n******************\n");
		    
		    LOGGER.debug(strB.toString());
		}

	    }
	} };

	SSLContext sslContext = SSLContext.getInstance("TLS");
	sslContext.init(null, getCertTM, new SecureRandom());
	return sslContext.getSocketFactory();
    }

    private HandshakeCompletedListener getHandshakeCompletedListener() {
	return hCL; 
    }
    
    private static final HandshakeCompletedListener hCL = (e) -> {
	    LOGGER.debug(e.getCipherSuite());
	};

}
