package wicket_extension.action;

import java.io.Serializable;

/** ������ ��������� ������������ ��� �������������� ���������, 
 * ������� ��������� ���������� � ���������, � �������� ��� ����� ���� ��������(WebPage),
 * ������� �������� ������, � ��� ������ ��������� � ���������� �����-���� ��������   
 */
public interface IActionExecutor {
	/** ���������� � ������������� ���������� ���������� ��������:
	 * @param actionName - ��� ��������
	 * @param argument - ��������, ������� ���������� ��� ���������� ��������  
	 * */
	public void action(String actionName, Serializable argument);
}
