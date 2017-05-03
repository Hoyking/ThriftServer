package service.soap;

import java.util.List;

import database.JDBCService;
import starter.Starter;

/** ����� ������ ��� ���������� ����������-����������������� ���� ����������
 * 
 * @author Parfenenko Artem
 * @version 1.0
 *
 */
public class AxisDirectory {

	/** ���� - ��������� ������ ��� ������ � JDBC
	 * @see database.JDBCService
	 */
	private JDBCService jdbcService;
	
	/** ����������� ��� ������������� jdbcService � �������� ���������� ������
	 * @param jdbcService ��������� ������ JDBCService
	 */
	public AxisDirectory() {
		this.jdbcService = JDBCService.getInstance();
	}
	
	/** ����� ��� ��������� ������ ���������� ������ �����������
	 * @return ������ ���������� ������
	 */
	public List<String> getTitles() {
		Starter.getLogger().info("Reading titles from the database");
		List<String> titles = jdbcService.getTitleContent();
		return titles;
	}

	/** ����� ��� �������������� ������
	 * @param oldName ������� ��� ������� ������
	 * @param newName ����� ��� ������
	 * @param value ����� ���������� ������
	 */
	public void editArticle(String oldName, String newName, String value) throws ArticleNotFoundFault {
		Starter.getLogger().info("Editing article with name '" + oldName + "'");
		String curValue = jdbcService.getContent(oldName);
		if(curValue == null) {
			Starter.getLogger().error("There is now article with name '" + oldName + "'");
			throw new ArticleNotFoundFault("There is no article with name '" + oldName + "'");
		}
		jdbcService.editContent(oldName, newName, value);
	}

	/** ����� ��� �������� ������
	 * @param name ��� ��������� ������
	 */
	public void deleteArticle(String name) throws ArticleNotFoundFault {
		Starter.getLogger().info("Deleting article with name '" + name + "'");
		String curValue = jdbcService.getContent(name);
		if(curValue == null)
			throw new ArticleNotFoundFault("There is no article with name '" + name + "'");
		jdbcService.deleteContent(name);
	}
	
	/** ����� ��� �������� ���� ������ � �����������
	 */
	public void deleteArticles() {
		jdbcService.deleteContent();
	}

	/** ����� ��� �������� ������
	 * @param name ��� ����������� ������
	 * @param value ���������� ����������� ������ 
	 */
	public void createArticle(String name, String value) {
		jdbcService.addContent(name, value);
	}

	/** ����� ��� ��������� ����������� ������
	 * @param name ��� ������� ������
	 * @return ���������� ������� ������
	 */
	public String getContent(String name) throws ArticleNotFoundFault {
		String curValue = jdbcService.getContent(name);
		if(curValue == null) {
			throw new ArticleNotFoundFault("There is no article with name '" + name + "'");
		}
		return curValue;
	}
	
}
