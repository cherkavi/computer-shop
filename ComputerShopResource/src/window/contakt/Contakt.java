package window.contakt;

import org.apache.wicket.markup.html.basic.Label;

import window.BasePage;

/** панель-заголовок для базовой страницы */
public class Contakt extends BasePage{
	private final static long serialVersionUID=1L;
	
	public Contakt(){
		initComponents();
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		Label labelContaktNumber=new Label("contakt_number",this.getString("contakt_number"));
		labelContaktNumber.setEscapeModelStrings(false);
		this.add(labelContaktNumber);
	}

}
