package wicket_extension.panel.label;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/** ������ � ��������� ������ */
public class TextPanel extends Panel{
	private final static long serialVersionUID=1L;
	
	/** ������ � ��������� ������ 
	 * @param id - ���������� ������������� ������
	 * @param text - ��������� ����� �� ������ 
	 * */
	public TextPanel(String id, String text){
		super(id);
		this.add(new Label("label",text));
	}
	
}
