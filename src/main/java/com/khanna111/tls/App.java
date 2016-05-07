package com.khanna111.tls;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * @author gkhanna
 */
@SpringBootApplication
public class App {
    private final static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
	LOGGER.debug("Hello World!");

	List<Future<String>> futuresList = new ArrayList<>();
	ExecutorService executor = Executors.newFixedThreadPool(2);
	try (BufferedReader bR = new BufferedReader(
		new FileReader(Paths.get("/app/workspace/eclipse-mars/tls/resources/top-10.csv").toFile()))) {
	    String line = null;
	    while ((line = bR.readLine()) != null) {
		if (line.trim().length() == 0) {
		    LOGGER.warn("Empty String");
		    continue;
		}
		Task task = getTask(line);
		if (task != null) {
		    futuresList.add(executor.submit(task));
		}
	    }
	    executor.shutdown();

	    for (Future<String> f : futuresList) {
		try {
		    LOGGER.info(f.get());
		}
		catch (InterruptedException | ExecutionException e1) {
		    LOGGER.warn("Excepton at top : ", e1);
		}
	    }

	}
	catch (Exception e) {
	    LOGGER.warn("Unable to operate in file data", e);
	}

    }

    private static Task getTask(String line) {
	try {
	    String hostName = line.substring(line.lastIndexOf(",") + 1);
	    int rank = Integer.parseInt(line.substring(0, line.indexOf(",")));
	    return new Task(rank, hostName, 443);
	}
	catch (Exception e) {
	    LOGGER.warn("Exception creating Task for line - {}: ", line, e);
	    return null;
	}

    }
}
