package com.woniu.base.web;

import org.apache.commons.cli.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;

public class Main {
	private static final int DEFAULT_HTTP_PORT = 8080;
	private static final int DEFAULT_THREAD_SIZE = 20;

	public static void main(String[] args) throws Exception {
		int port = DEFAULT_HTTP_PORT;
		int threadSize = DEFAULT_THREAD_SIZE;
		String war = "src/main/webapp";
		if (new File("webapp").exists()) {
			war = "webapp";
		}
		String contextPath = "/";
		int gracefulShutdownTimeout = 30000;

		Options options = new Options();
		options.addOption("p", "port", true, "server port, default is "
				+ DEFAULT_HTTP_PORT);
		options.addOption("m", "max-threads", true, "max threads, default is "
				+ DEFAULT_THREAD_SIZE);
		options.addOption("w", "war", true, "war directory");
		options.addOption("c", "context-path", true, "context path");
		options.addOption("g", "graceful-shutdown-timeout", true,
				"set graceful shutdown timeout(ms), default is 3000ms");
		options.addOption("h", "help", false, "show help message");
		CommandLineParser parser = new GnuParser();
		try {
			CommandLine commandLine = parser.parse(options, args);
			if (commandLine.hasOption("p")) {
				port = Integer.parseInt(commandLine.getOptionValue("p"));
			}
			if (commandLine.hasOption("m")) {
				threadSize = Integer.parseInt(commandLine.getOptionValue("m"));
			}
			if (commandLine.hasOption("w")) {
				war = commandLine.getOptionValue("w");
			}
			if (commandLine.hasOption("c")) {
				contextPath = commandLine.getOptionValue("c");
			}
			if (commandLine.hasOption("g")) {
				gracefulShutdownTimeout = Integer.parseInt(commandLine
						.getOptionValue("g"));
			}
			if (commandLine.hasOption("h")) {
				usage(options);
			}
		} catch (Exception e) {
			usage(options);
		}

		Server server = new Server(new QueuedThreadPool(threadSize));
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
		connector.setIdleTimeout(gracefulShutdownTimeout);
		server.addConnector(connector);

		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath(contextPath);
		webapp.setWar(war);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{
                new StatisticsHandler(),
                webapp
        });
        server.setHandler(handlers);

		server.setStopAtShutdown(true);
		server.setStopTimeout(gracefulShutdownTimeout);
		server.start();
		server.join();
	}

	private static void usage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("Main", options);
		System.exit(1);
	}

}
