package wicket_extension.ajax;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.IAjaxIndicatorAware;

/** Ajax behavior with Indicator */
public abstract class AjaxEventIndicatorBehavior extends AjaxEventBehavior implements IAjaxIndicatorAware{
	private final static long serialVersionUID=1L;
	
	private String ajaxIndicatorHtmlId;
	
	/** Ajax behavior with Indicator 
	 * @param event - ������������ �������
	 * @param ajaxIndicatorHtmlId - id ��������, ������� ������� ���������� ��� Ajax �������� 
	 */
	public AjaxEventIndicatorBehavior(String event, String ajaxIndicatorHtmlId){
		super(event);
		this.ajaxIndicatorHtmlId=ajaxIndicatorHtmlId;
	}
	
	@Override
	public String getAjaxIndicatorMarkupId() {
		return this.ajaxIndicatorHtmlId;
	}

	
}
