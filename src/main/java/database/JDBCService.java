package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import starter.Starter;

/** Класс служит для реализации взаимодействия с базой данных, используя SQLite JDBC
 * 
 * @author Parfenenko Artem
 * @version 1.0
 *
 */
public class JDBCService {	 
	
	private Connection connection;
	private Statement statement;
	/** Поле - экземпляр класса JDBCService */
	private static JDBCService jdbcservice;
	
	/** Приватный конструктор для инициализации соединения с базой данных */
	private JDBCService() {
		try {
			Starter.getLogger().info("Connection to the database");
			Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:E:/programming/aipos_server/ThriftServer/database//article_db.db");
			connection.setAutoCommit(false);
			statement = connection.createStatement();
		} catch (ClassNotFoundException e) {
			Starter.getLogger().error("The thrown ClassNotFoundException accompained by StackTrace: ", e);
		} catch (SQLException e) {
			Starter.getLogger().error("The thrown SQLException accompained by StackTrace: ", e);
		}
        createTable();
	}
	
	/** Метод возвращает экземпляр класса
	 * 
	 * @return экземпляр JDBCService
	 */
	public static JDBCService getInstance() {
		if(jdbcservice == null) {
			jdbcservice = new JDBCService();
		}
		return jdbcservice;
	}
	
	/** Метод посылает запрос к БД с целью изменения хранящейся информации
	 * 
	 * @param sql строка запроса к БД
	 */
	private void executeOperation(String sql) {
		try {
			statement.execute(sql);
			connection.commit();
		} catch (SQLException e) {
			Starter.getLogger().error("The thrown SQLException accompained by StackTrace: ", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				Starter.getLogger().error("The thrown SQLException accompained by StackTrace: ", e1);
			}
		}
	}
	
	/** Метод посылает запрос к БД с целью получения информации из БД
	 * 
	 * @param sql строка запроса к БД
	 * @return запрашиваемая информация
	 */
	private ResultSet queryOperation(String sql) {
		ResultSet rs;
		try {
			rs = statement.executeQuery(sql);
			connection.commit();
			return rs;
		} catch (SQLException e) {
			Starter.getLogger().error("The thrown SQLException accompained by StackTrace: ", e);
			try {
				connection.rollback();
				return null;
			} catch (SQLException e1) {
				Starter.getLogger().error("The thrown SQLException accompained by StackTrace: ", e1);
				return null;
			}
		}
	}
	
	/** Метод служит для создания таблицы articles в БД */
	private void createTable() {
		try {
			statement.execute("CREATE TABLE articles(name varchar(100) NOT NULL UNIQUE, value varchar(100));");
			Starter.getLogger().info("Creating new table articles");
			connection.commit();
		} catch (SQLException e) {
			Starter.getLogger().info("Table articles already exists");
			try {
				connection.rollback();
			} catch (SQLException e1) {
				Starter.getLogger().error("The thrown SQLException accompained by StackTrace: ", e1);
			}
		}
	}
	
	/** Метод посылает запрос к БД с целью добавления новой статьи
	 * 
	 * @param name имя новой статьи
	 * @param value содержание новой статьи
	 */
	public void addContent(String name, String value) {
		executeOperation("INSERT INTO articles(name, value) VALUES('" + name + "', '" + value + "');");
	}
	
	/** Метод посылает запрос к БД с целью удаления статьи
	 * 
	 * @param name имя удаляемой статьи
	 */
	public void deleteContent(String name) {
		executeOperation("DELETE FROM articles WHERE articles.name = '" + name + "';");
	}
	
	/** Метод посылает запрос к БД с целью удаления всех имеющихся в БД статей */
	public void deleteContent() {
		executeOperation("DELETE * FROM articles;");
	}
	
	/** Метод посылает запрос к БД с целью редактирования имеющейся статьи
	 * 
	 * @param oldName имя имеющейся статьи
	 * @param newName новое имя статьи
	 * @param value новое содержимое статьи
	 */
	public void editContent(String oldName, String newName, String value) {
		executeOperation("UPDATE articles SET name = '" + newName + "', value = '" + value + "' WHERE (name = '" + oldName + "');");
	}
	
	/** Метод посылает запрос к БД с целью получения содержимого имеющейся статьи
	 * 
	 * @param name имя искомой статьи
	 * @return содержимое искомой статьи
	 */
	public String getContent(String name) {
		ResultSet rs = queryOperation("SELECT articles.value FROM articles WHERE name = '" + name + "';");
		String content;
		try {
			if(rs.next()) {
				content = new String(rs.getString("value"));
				return content;
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			Starter.getLogger().error("The thrown SQLException accompained by StackTrace: ", e);
			return null;
		}  finally {
			try {
				rs.close();
			} catch (SQLException e) {
				Starter.getLogger().error("The thrown SQLException accompained by StackTrace: ", e);
			}
		}
	}
	
	/** Метод посылает запрос к БД с целью получения списка заголовков имеющихся в БД статей
	 * 
	 * @return список заголовков статей
	 */
	public List<String> getTitleContent() {
		List<String> titles = new ArrayList<String>();
		ResultSet rs = queryOperation("SELECT articles.name FROM articles;");
		try {
			while(rs.next()) {
				titles.add(new String(rs.getString("name")));
			}
			return titles;
		} catch (SQLException e) {
			Starter.getLogger().error("The thrown SQLException accompained by StackTrace: ", e);
			return titles;
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				Starter.getLogger().error("The thrown SQLException accompained by StackTrace: ", e);
			}
		}
	}
	
}
