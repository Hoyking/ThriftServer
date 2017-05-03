package service.soap;

/** ����� - ����������, ���������� � ������ ������� ����������� � �������������� ������ (��� ����������-��������������� ������ ����������� web-�������)
 * 
 * @author Parfenenko Artem
 * @version 1.0
 *
 */
public class ArticleNotFoundFault extends Exception {

	private static final long serialVersionUID = -5147677238858547706L;

	/** ����������� ��� �������� ���������� ������
	 * 
	 * @param message ��������� ����������
	 */
	public ArticleNotFoundFault(String message) {
		super(message);
	}
	
}
