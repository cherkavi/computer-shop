package wicket_extension.action;

/** ������ ��������� ������������ ��� �������������� ���������, 
 * ������� ��������� ���������� � ���������, � �������� ��� ����� ���� ��������(WebPage),
 * ������� �������� ������, � ��� ������ ��������� � ���������� �����-���� ��������   
 */
public interface IActionExecutor {
	/** ���������� � ������������� ���������� ���������� ��������:
	 * @param actionName - ��� ��������
	 * @param argument - ��������, ������� ���������� ��� ���������� ��������  
	 * */
	public void action(String actionName, Object argument);
}
