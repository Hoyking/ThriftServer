package starter;

import org.apache.log4j.Logger;
import server.ServerHandler;

/** Класс служит для создания экземпляра логгера и точки входа в программу
 *
 */
public class Starter {

	/** Экземпляр логгера */
	private static final Logger log = Logger.getLogger(Starter.class);
	
	public static Logger getLogger() {
		return log;
	}
	
	/** Метод main */
	public static void main(String[] args) {
		ServerHandler serverHandler = new ServerHandler();
		serverHandler.handle();
	}
	
}
