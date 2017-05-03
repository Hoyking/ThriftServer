package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import starter.Starter;

/** ����� ������ ��� ���������� �������������� � ����� ������, ��������� SQLite JDBC
 * 
 * @author Parfenenko Artem
 * @version 1.0
 *
 */
public class JDBCService {	 
	
	private Connection connection;
	private Statement statement;
	/** ���� - ��������� ������ JDBCService */
	private static JDBCService jdbcservice;
	
	/** ��������� ����������� ��� ������������� ���������� � ����� ������ */
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
	
	/** ����� ���������� ��������� ������
	 * 
	 * @return ��������� JDBCService
	 */
	public static JDBCService getInstance() {
		if(jdbcservice == null) {
			jdbcservice = new JDBCService();
		}
		return jdbcservice;
	}
	
	/** ����� �������� ������ � �� � ����� ��������� ���������� ����������
	 * 
	 * @param sql ������ ������� � ��
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
	
	/** ����� �������� ������ � �� � ����� ��������� ���������� �� ��
	 * 
	 * @param sql ������ ������� � ��
	 * @return ������������� ����������
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
	
	/** ����� ������ ��� �������� ������� articles � �� */
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
	
	/** ����� �������� ������ � �� � ����� ���������� ����� ������
	 * 
	 * @param name ��� ����� ������
	 * @param value ���������� ����� ������
	 */
	public void addContent(String name, String value) {
		executeOperation("INSERT INTO articles(name, value) VALUES('" + name + "', '" + value + "');");
	}
	
	/** ����� �������� ������ � �� � ����� �������� ������
	 * 
	 * @param name ��� ��������� ������
	 */
	public void deleteContent(String name) {
		executeOperation("DELETE FROM articles WHERE articles.name = '" + name + "';");
	}
	
	/** ����� �������� ������ � �� � ����� �������� ���� ��������� � �� ������ */
	public void deleteContent() {
		executeOperation("DELETE * FROM articles;");
	}
	
	/** ����� �������� ������ � �� � ����� �������������� ��������� ������
	 * 
	 * @param oldName ��� ��������� ������
	 * @param newName ����� ��� ������
	 * @param value ����� ���������� ������
	 */
	public void editContent(String oldName, String newName, String value) {
		executeOperation("UPDATE articles SET name = '" + newName + "', value = '" + value + "' WHERE (name = '" + oldName + "');");
	}
	
	/** ����� �������� ������ � �� � ����� ��������� ����������� ��������� ������
	 * 
	 * @param name ��� ������� ������
	 * @return ���������� ������� ������
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
	
	/** ����� �������� ������ � �� � ����� ��������� ������ ���������� ��������� � �� ������
	 * 
	 * @return ������ ���������� ������
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
