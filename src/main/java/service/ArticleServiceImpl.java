package service;

import java.util.List;

import org.apache.thrift.TException;

import database.JDBCService;

public class ArticleServiceImpl implements ArticleService.Iface {

	private JDBCService jdbcService;
	
	public ArticleServiceImpl(JDBCService jdbcService) {
		this.jdbcService = jdbcService;
	}
	
	public Article getArticle(String name) throws ArticleNotFoundException, TException {
		Article article = jdbcService.getContent(name);
		if(article == null)
			throw new ArticleNotFoundException("There is now article with such name");
		return article;
	}

	public List<String> getTitles() throws TException {
		List<String> titles = jdbcService.getTitleContent();
		return titles;
	}

	public void editArticle(String name, String value) throws ArticleNotFoundException, TException {
		Article article = jdbcService.getContent(name);
		if(article == null)
			throw new ArticleNotFoundException("There is now article with such name");
		jdbcService.editContent(name, value);
	}

	public void deleteArticle(String name) throws ArticleNotFoundException, TException {
		Article article = jdbcService.getContent(name);
		if(article == null)
			throw new ArticleNotFoundException("There is now article with such name");
		jdbcService.deleteContent(name);
	}
	
	public void deleteArticles() throws TException {
		jdbcService.deleteContent();
	}

	public void createArticle(String name, String value) throws TException {
		Article article = new Article();
		article.setName(name);
		article.setValue(value);
		jdbcService.addContent(article);
	}

}
