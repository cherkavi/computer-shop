package wicket_extension.action;

import org.apache.wicket.ajax.AjaxRequestTarget;
/** исполнитель для Ajax запросов  */
public interface IAjaxActionExecutor {
	public final static int RETURN_ERROR=(-1);
	public final static int RETURN_OK=0;

	/** модальное окно вернуло положительный результат */
	public final static String actionModalOk="modal_ok";
	
	/** модальное окно вернуло отрицательный результат */
	public final static String actionModalCancel="modal_cancel";
	
	/**  метод, в который передаются вызовы от других объетов 
	 * @param target - AjaxRequestTarget
	 * @param actionName - наименование 
	 * @param argument - аргумент для передачи данных 
	 * @return (возможное оповещение о решении задачи)
	 * <ul>
	 * 	<li><b>{@link #RETURN_OK} </b> успешно отработана </li>
	 * 	<li><b>{@link #RETURN_ERROR} </b> ошибка в обработке </li>
	 * </ul>
	 */
	public int action(AjaxRequestTarget target, String actionName, Object argument);
}
