package service.soap;

import java.util.List;

import database.JDBCService;
import starter.Starter;

/** Класс служит для реализации документно-ориентированнного типа соединения
 * 
 * @author Parfenenko Artem
 * @version 1.0
 *
 */
public class AxisDirectory {

	/** Поле - экземпляр класса для работы с JDBC
	 * @see database.JDBCService
	 */
	private JDBCService jdbcService;
	
	/** Конструктор для инициализации jdbcService и создания экземпляра класса
	 * @param jdbcService экземпляр класса JDBCService
	 */
	public AxisDirectory() {
		this.jdbcService = JDBCService.getInstance();
	}
	
	/** Метод для получения списка заголовков статей справочника
	 * @return список заголовков статей
	 */
	public List<String> getTitles() {
		Starter.getLogger().info("Reading titles from the database");
		List<String> titles = jdbcService.getTitleContent();
		return titles;
	}

	/** Метод для редактирования статьи
	 * @param oldName текущее имя искомой статьи
	 * @param newName новое имя статьи
	 * @param value новое содержимое статьи
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

	/** Метод для удаления статьи
	 * @param name имя удаляемой статьи
	 */
	public void deleteArticle(String name) throws ArticleNotFoundFault {
		Starter.getLogger().info("Deleting article with name '" + name + "'");
		String curValue = jdbcService.getContent(name);
		if(curValue == null)
			throw new ArticleNotFoundFault("There is no article with name '" + name + "'");
		jdbcService.deleteContent(name);
	}
	
	/** Метод для удаления всех статей в справочнике
	 */
	public void deleteArticles() {
		jdbcService.deleteContent();
	}

	/** Метод для создания статьи
	 * @param name имя создаваемой статьи
	 * @param value содержание создаваемой статьи 
	 */
	public void createArticle(String name, String value) {
		jdbcService.addContent(name, value);
	}

	/** Метод для получения содержимого статьи
	 * @param name имя искомой статьи
	 * @return содержимое искомой статьи
	 */
	public String getContent(String name) throws ArticleNotFoundFault {
		String curValue = jdbcService.getContent(name);
		if(curValue == null) {
			throw new ArticleNotFoundFault("There is no article with name '" + name + "'");
		}
		return curValue;
	}
	
}
