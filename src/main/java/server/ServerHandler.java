package server;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import database.JDBCService;
import service.rpc.ArticleService;
import service.rpc.ArticleServiceImpl;
import service.soap.AxisDirectory;

public class ServerHandler {

	public void handle() {
		try {
			TServerTransport serverTransport = new TServerSocket(9090);
		    TServer server = new TThreadPoolServer(new
		    TThreadPoolServer.Args(serverTransport).processor(new ArticleService.Processor<ArticleServiceImpl>
		    													(new ArticleServiceImpl(JDBCService.getInstance()))));
		    System.out.println("Server started");
		    server.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
