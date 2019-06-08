package wicket_extension.panel.label;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/** панель с текстовой меткой */
public class TextPanel extends Panel{
	private final static long serialVersionUID=1L;
	
	/** панель с текстовой меткой 
	 * @param id - уникальный идентификатор панели
	 * @param text - текстовая метка на панели 
	 * */
	public TextPanel(String id, String text){
		super(id);
		this.add(new Label("label",text));
	}
	
}
