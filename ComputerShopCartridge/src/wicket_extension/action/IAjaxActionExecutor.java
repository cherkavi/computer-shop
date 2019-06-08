package wicket_extension.action;

import org.apache.wicket.ajax.AjaxRequestTarget;
/** ����������� ��� Ajax ��������  */
public interface IAjaxActionExecutor {
	public final static int RETURN_ERROR=(-1);
	public final static int RETURN_OK=0;

	/** ��������� ���� ������� ������������� ��������� */
	public final static String actionModalOk="modal_ok";
	
	/** ��������� ���� ������� ������������� ��������� */
	public final static String actionModalCancel="modal_cancel";
	
	/**  �����, � ������� ���������� ������ �� ������ ������� 
	 * @param target - AjaxRequestTarget
	 * @param actionName - ������������ 
	 * @param argument - �������� ��� �������� ������ 
	 * @return (��������� ���������� � ������� ������)
	 * <ul>
	 * 	<li><b>{@link #RETURN_OK} </b> ������� ���������� </li>
	 * 	<li><b>{@link #RETURN_ERROR} </b> ������ � ��������� </li>
	 * </ul>
	 */
	public int action(AjaxRequestTarget target, String actionName, Object argument);
}
