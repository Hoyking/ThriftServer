package service.soap;

import java.util.List;

import database.JDBCService;

public class AxisDirectory {

	private JDBCService jdbcService;
	
	public AxisDirectory() {
		this.jdbcService = JDBCService.getInstance();
	}
	
	public List<String> getTitles() {
		List<String> titles = jdbcService.getTitleContent();
		return titles;
	}

	public void editArticle(String name, String value) {
		jdbcService.editContent(name, value);
	}

	public void deleteArticle(String name) {
		jdbcService.deleteContent(name);
	}
	
	public void deleteArticles() {
		jdbcService.deleteContent();
	}

	public void createArticle(String name, String value) {
		jdbcService.addContent(name, value);
	}

	public String getContent(String name) {
		System.out.println("getting content");
		String curValue = jdbcService.getContent(name);
		return curValue;
	}
	
}
