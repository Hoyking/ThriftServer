package starter;

import org.apache.log4j.Logger;
import server.ServerHandler;

/** ����� ������ ��� �������� ���������� ������� � ����� ����� � ���������
 *
 */
public class Starter {

	/** ��������� ������� */
	private static final Logger log = Logger.getLogger(Starter.class);
	
	public static Logger getLogger() {
		return log;
	}
	
	/** ����� main */
	public static void main(String[] args) {
		ServerHandler serverHandler = new ServerHandler();
		serverHandler.handle();
	}
	
}
