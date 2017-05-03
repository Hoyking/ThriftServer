package test;

import static org.junit.Assert.assertEquals;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.Before;
import org.junit.Test;

import database.JDBCService;
import service.rpc.ArticleNotFoundException;
import service.rpc.ArticleService;
import service.rpc.ArticleServiceImpl;
import starter.Starter;

public class ServerTest implements Runnable{

	private ArticleService.Client client;
	private TServer server;
	
	@Before
	public void testPreparation() {		
		Thread thread = new Thread(this);
		thread.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		TTransport transport = new TSocket("localhost", 9090);
		try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		TProtocol protocol = new TBinaryProtocol(transport);
		client = new ArticleService.Client(protocol);
	}

	@Test
	public void testCreatReadDelete() {
		try {
			client.createArticle("Java test", "Java test content");
		} catch (TException e) {
			e.printStackTrace();
		}
		
		String expected1 = "Java test content";
		String result1 = "";
		try {
			result1 = client.getContent("Java test");
		} catch (ArticleNotFoundException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		}
		
		try {
			client.deleteArticle("Java test");
		} catch (ArticleNotFoundException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		}
		
		String expected2 = null;
		String result2 = null;
		try {
			result2 = client.getContent("Java test");
		} catch (ArticleNotFoundException e) {
			Starter.getLogger().info("The article is already deleted");
		} 
		catch (TException e) {
			e.printStackTrace();
		}
		
		server.stop();
		
		assertEquals(result1, expected1);
		assertEquals(result2, expected2);
	}
	
	@Test
	public void testCreateEditDelete() {
		try {
			client.createArticle("Java test", "Java test content");
		} catch (TException e) {
			e.printStackTrace();
		}
		
		String expectedContent1 = "Java test content updated";
		String resultContent1 = "";
		
		try {
			client.editArticle("Java test",	"Java test updated", "Java test content updated");
		} catch (ArticleNotFoundException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		}
		
		try {
			resultContent1 = client.getContent("Java test updated");
		} catch (ArticleNotFoundException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		}
		
		try {
			client.deleteArticle("Java test updated");
		} catch (ArticleNotFoundException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		}
		
		String expectedContent2 = null;
		String resultContent2 = null;
		try {
			resultContent2 = client.getContent("Java test updated");
		} catch (ArticleNotFoundException e) {
			Starter.getLogger().info("The article is already deleted");
		} 
		catch (TException e) {
			e.printStackTrace();
		}
		
		server.stop();
		
		assertEquals(resultContent1, expectedContent1);
		assertEquals(resultContent2, expectedContent2);
	}
	
	@Override
	public void run() {
		try {
			TServerTransport serverTransport = new TServerSocket(9090);
		    server = new TThreadPoolServer(new
		    TThreadPoolServer.Args(serverTransport).processor(new ArticleService.Processor<ArticleServiceImpl>
		    													(new ArticleServiceImpl(JDBCService.getInstance()))));
		    server.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
