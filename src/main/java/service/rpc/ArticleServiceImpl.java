package service.rpc;

import java.util.List;

import org.apache.thrift.TException;

import database.JDBCService;
import starter.Starter;

/** ����� ������ ��� ���������� ���������� ������ � RPC ����� ����������
 * 
 * @author Parfenenko Artem
 * @version 1.0
 *
 */
public class ArticleServiceImpl implements ArticleService.Iface {

	/** ���� - ��������� ������ ��� ������ � JDBC
	 * @see database.JDBCService
	 */
	private JDBCService jdbcService;
	
	/** ����������� ��� ������������� jdbcService � �������� ���������� ������
	 * @param jdbcService ��������� ������ JDBCService
	 */
	public ArticleServiceImpl(JDBCService jdbcService) {
		this.jdbcService = jdbcService;
	}

	/** ����� ��� ��������� ������ ���������� ������ �����������
	 * @return ������ ���������� ������
	 */
	public List<String> getTitles() throws TException {
		Starter.getLogger().info("Reading titles from the database");
		List<String> titles = jdbcService.getTitleContent();
		return titles;
	}

	/** ����� ��� �������������� ������
	 * @param oldName ������� ��� ������� ������
	 * @param newName ����� ��� ������
	 * @param value ����� ���������� ������
	 */
	public void editArticle(String oldName, String newName, String value) throws ArticleNotFoundException, TException {
		Starter.getLogger().info("Editing article with name '" + oldName + "'");
		String curValue = jdbcService.getContent(oldName);
		if(curValue == null) {
			Starter.getLogger().error("There is now article with name '" + oldName + "'");
			throw new ArticleNotFoundException();
		}
		jdbcService.editContent(oldName, newName, value);
	}

	/** ����� ��� �������� ������
	 * @param name ��� ��������� ������
	 */
	public void deleteArticle(String name) throws ArticleNotFoundException, TException {
		Starter.getLogger().info("Deleting article with name '" + name + "'");
		String curValue = jdbcService.getContent(name);
		if(curValue == null) {
			Starter.getLogger().error("There is now article with name '" + name + "'");
			throw new ArticleNotFoundException();
		}
		jdbcService.deleteContent(name);
	}
	
	/** ����� ��� �������� ���� ������ � �����������
	 */
	public void deleteArticles() throws TException {
		Starter.getLogger().info("Deleting all articles from the database");
		jdbcService.deleteContent();
	}

	/** ����� ��� �������� ������
	 * @param name ��� ����������� ������
	 * @param value ���������� ����������� ������ 
	 */
	public void createArticle(String name, String value) throws TException {
		Starter.getLogger().info("Creating a new article");
		jdbcService.addContent(name, value);
	}

	/** ����� ��� ��������� ����������� ������
	 * @param name ��� ������� ������
	 * @return ���������� ������� ������
	 */
	public String getContent(String name) throws ArticleNotFoundException, TException {
		if(name.length() > 0)
			Starter.getLogger().info("Reading article with name '" + name + "'");
		String curValue = jdbcService.getContent(name);
		if(curValue == null) {
			if(name.length() > 0)
				Starter.getLogger().error("There is now article with name '" + name + "'");
			throw new ArticleNotFoundException();
		}
		return curValue;
	}

}
