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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 
 * @author gkhanna
 */
@SpringBootApplication

public class App {
    private final static Logger LOGGER = LoggerFactory.getLogger(App.class);

    @Autowired
    private AppUtil appUtil;

    public static void main(String[] args) {
	@SuppressWarnings("unused")
	ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
    }

    @PostConstruct
    public void post() {
	LOGGER.debug("Hello World!");

	List<Future<String>> futuresList = new ArrayList<>();
	ExecutorService executor = Executors
		.newFixedThreadPool(Integer.parseInt(appUtil.getAppConfig().getExecPoolSize()));
	try (BufferedReader bR = new BufferedReader(
		new FileReader(Paths.get(appUtil.getAppConfig().getTopSitesPath()).toFile()))) {
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

    private Task getTask(String line) {
	try {
	    String hostName = line.substring(line.lastIndexOf(",") + 1);
	    int rank = Integer.parseInt(line.substring(0, line.indexOf(",")));
	    return new Task(rank, hostName, 443, appUtil);
	}
	catch (Exception e) {
	    LOGGER.warn("Exception creating Task for line - {}: ", line, e);
	    return null;
	}

    }
}
