package com.khanna111.tls;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
	ExecutorService e = Executors.newFixedThreadPool(2);
	Future<String> f1 = e.submit(new Task("symantec.com", 443));
	Future<String> f2 = e.submit(new Task("google.com", 443));
	e.shutdown();

	try {
	    LOGGER.info(f1.get());
	    LOGGER.info(f2.get());
	}
	catch (InterruptedException | ExecutionException e1) {
	 LOGGER.warn("Excepton at top : ", e);
	}

    }

}
