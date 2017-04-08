package service.rpc;

import java.util.List;

import org.apache.thrift.TException;

import database.JDBCService;

public class ArticleServiceImpl implements ArticleService.Iface {

	private JDBCService jdbcService;
	
	public ArticleServiceImpl(JDBCService jdbcService) {
		this.jdbcService = jdbcService;
	}

	public List<String> getTitles() throws TException {
		List<String> titles = jdbcService.getTitleContent();
		return titles;
	}

	public void editArticle(String name, String value) throws ArticleNotFoundException, TException {
		String curValue = jdbcService.getContent(name);
		if(curValue == null)
			throw new ArticleNotFoundException();
		jdbcService.editContent(name, value);
	}

	public void deleteArticle(String name) throws ArticleNotFoundException, TException {
		String curValue = jdbcService.getContent(name);
		if(curValue == null)
			throw new ArticleNotFoundException();
		jdbcService.deleteContent(name);
	}
	
	public void deleteArticles() throws TException {
		jdbcService.deleteContent();
	}

	public void createArticle(String name, String value) throws TException {
		jdbcService.addContent(name, value);
	}

	public String getContent(String name) throws ArticleNotFoundException, TException {
		String curValue = jdbcService.getContent(name);
		if(curValue == null)
			throw new ArticleNotFoundException();
		return curValue;
	}

}
