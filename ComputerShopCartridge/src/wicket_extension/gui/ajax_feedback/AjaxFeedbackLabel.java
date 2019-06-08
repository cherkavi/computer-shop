package wicket_extension.gui.ajax_feedback;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/** Label, ������� ������������ ��� ����������� ������������, � �������� �������� ������ ComponentFeedbackPanel */
public class AjaxFeedbackLabel extends Panel{
	private static final long serialVersionUID = 1L;
	private Model<String> model;
	
	/** Label, ������� ������������ ��� ����������� ������������, � �������� �������� ������ ComponentFeedbackPanel */
	public AjaxFeedbackLabel(String id){
		super(id);
		this.model=new Model<String>(null);
		this.setOutputMarkupId(true);
		this.add(new Label("label",this.model));
	}
	
	/** ���������� ����� ��� ������ */
	public void setText(String text){
		this.model.setObject(text);
	}
	
	@Override
	protected void detachModel() {
		super.detachModel();
		this.model.setObject(null);
	}
}
