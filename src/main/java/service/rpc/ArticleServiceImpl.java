package service.rpc;

import java.util.List;

import org.apache.thrift.TException;

import database.JDBCService;
import starter.Starter;

/** Класс служит для реализации интерфейса работы с RPC типом соединения
 * 
 * @author Parfenenko Artem
 * @version 1.0
 *
 */
public class ArticleServiceImpl implements ArticleService.Iface {

	/** Поле - экземпляр класса для работы с JDBC
	 * @see database.JDBCService
	 */
	private JDBCService jdbcService;
	
	/** Конструктор для инициализации jdbcService и создания экземпляра класса
	 * @param jdbcService экземпляр класса JDBCService
	 */
	public ArticleServiceImpl(JDBCService jdbcService) {
		this.jdbcService = jdbcService;
	}

	/** Метод для получения списка заголовков статей справочника
	 * @return список заголовков статей
	 */
	public List<String> getTitles() throws TException {
		Starter.getLogger().info("Reading titles from the database");
		List<String> titles = jdbcService.getTitleContent();
		return titles;
	}

	/** Метод для редактирования статьи
	 * @param oldName текущее имя искомой статьи
	 * @param newName новое имя статьи
	 * @param value новое содержимое статьи
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

	/** Метод для удаления статьи
	 * @param name имя удаляемой статьи
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
	
	/** Метод для удаления всех статей в справочнике
	 */
	public void deleteArticles() throws TException {
		Starter.getLogger().info("Deleting all articles from the database");
		jdbcService.deleteContent();
	}

	/** Метод для создания статьи
	 * @param name имя создаваемой статьи
	 * @param value содержание создаваемой статьи 
	 */
	public void createArticle(String name, String value) throws TException {
		Starter.getLogger().info("Creating a new article");
		jdbcService.addContent(name, value);
	}

	/** Метод для получения содержимого статьи
	 * @param name имя искомой статьи
	 * @return содержимое искомой статьи
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
