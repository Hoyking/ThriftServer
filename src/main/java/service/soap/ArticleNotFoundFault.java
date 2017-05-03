package service.soap;

/** Класс - исключение, вызываемое в случае попытки допуститься к несуществующей статье (при документно-ориентированном методе организации web-сервиса)
 * 
 * @author Parfenenko Artem
 * @version 1.0
 *
 */
public class ArticleNotFoundFault extends Exception {

	private static final long serialVersionUID = -5147677238858547706L;

	/** Конструктор для создания экземпляра класса
	 * 
	 * @param message сообщение исключения
	 */
	public ArticleNotFoundFault(String message) {
		super(message);
	}
	
}
