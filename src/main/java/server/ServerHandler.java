package server;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import database.JDBCService;
import service.rpc.ArticleService;
import service.rpc.ArticleServiceImpl;

/** ����� ������ ��� ������������� RPC �������
 * 
 * @author Parfenenko Artem
 * @version 1.0
 *
 */
public class ServerHandler {

	/** ����� ������ ��� ������������� RPC ������� */
	public void handle() {
		try {
			TServerTransport serverTransport = new TServerSocket(9090);
		    TServer server = new TThreadPoolServer(new
		    TThreadPoolServer.Args(serverTransport).processor(new ArticleService.Processor<ArticleServiceImpl>
		    													(new ArticleServiceImpl(JDBCService.getInstance()))));
		    server.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
