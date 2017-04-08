package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//import service.rpc.Article;

public class JDBCService {
	
	private Connection connection;
	private Statement statement;
	private static JDBCService jdbcservice;
	
	private JDBCService() {
		try {
			System.out.println("Creating JDBC");
			Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:E:/programming/aipos_server/ThriftServer/database//article_db.db");
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			System.out.println("Database activated");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        createTable();
	}
	
	public static JDBCService getInstance() {
		if(jdbcservice == null) {
			System.out.println("New instance of JDBC");
			jdbcservice = new JDBCService();
		}
		return jdbcservice;
	}
	
	private void executeOperation(String sql) {
		try {
			statement.execute(sql);
			System.out.println("EXECUTED: " + sql);
			connection.commit();
		} catch (SQLException e) {
			try {
				System.out.println("FAILED: " + sql);
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private ResultSet queryOperation(String sql) {
		ResultSet rs;
		try {
			rs = statement.executeQuery(sql);
			System.out.println("EXECUTED: " + sql);
			connection.commit();
			return rs;
		} catch (SQLException e) {
			try {
				System.out.println("FAILED: " + sql);
				connection.rollback();
				return null;
			} catch (SQLException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}
	
	private void createTable() {
		executeOperation("CREATE TABLE articles(name varchar(100) NOT NULL UNIQUE, value varchar(100));");
	}
	
	public void addContent(String name, String value) {
		executeOperation("INSERT INTO articles(name, value) VALUES('" + name + "', '" + value + "');");
	}
	
	/*public void addContent(List<Article> articles) {
		for(Article article: articles) {
			addContent(article);
		}
	}*/
	
	public void deleteContent(String name) {
		executeOperation("DELETE FROM articles WHERE articles.name = '" + name + "';");
	}
	
	public void deleteContent() {
		executeOperation("DELETE * FROM articles;");
	}
	
	public void editContent(String name, String value) {
		executeOperation("UPDATE articles SET value = '" + value + "' WHERE (name = '" + name + "');");
	}
	
	public String getContent(String name) {
		System.out.println("getting content from DB");
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
			e.printStackTrace();
			return null;
		}  finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<String> getTitleContent() {
		List<String> titles = new ArrayList<String>();
		ResultSet rs = queryOperation("SELECT articles.name FROM articles;");
		try {
			while(rs.next()) {
				titles.add(new String(rs.getString("name")));
			}
			return titles;
		} catch (SQLException e) {
			e.printStackTrace();
			return titles;
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
