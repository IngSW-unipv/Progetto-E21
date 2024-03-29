package server;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.rythmengine.Rythm;

/**
 * This class is the main Application Server
 */
public class ApplicationServer {
	 private int port;
	    private Servlet servlet;
	    private Server server;

	    public ApplicationServer(int port, Servlet servlet) {
	        this.port = port;
	        this.servlet = servlet;
	    }

	/**
	 * This method starts the server
	 * @throws Exception
	 */
	public void start() throws Exception {
	    	initTemplateEngine();
	        server = new Server(port);
	        ServletContextHandler handler = new ServletContextHandler();
	        handler.addServlet(new ServletHolder(servlet), "/*");
	        addStaticFileServing(handler);
	        server.setHandler(handler);
	        server.start();
	    }

	/**
	 * This method stops the server
	 * @throws Exception
	 */
	    public void stop() throws Exception {
	        server.stop();
	    }

	    private void addStaticFileServing(ServletContextHandler handler) {
	        ServletHolder holderPwd = new ServletHolder("default", new DefaultServlet());
	        holderPwd.setInitParameter("resourceBase", "./src/main/resources/statics");
	        holderPwd.setInitParameter("dirAllowed","false");
	        holderPwd.setInitParameter("pathInfoOnly","true");
	        handler.addServlet(holderPwd, "/statics/*");
	    }

	    private void initTemplateEngine() {
	        Map<String, Object> conf = new HashMap<>();
	        conf.put("home.template", "templates");
	        Rythm.init(conf);
	    }
}

