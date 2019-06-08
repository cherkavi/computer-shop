package window.question.answer;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import window.BasePage;
import window.catalog.Catalog;

/** вопрос от клиента */
public class Answer extends BasePage{
	private final static long serialVersionUID=1L;
	
	/** вопрос от клиента */
	public Answer(){
		initComponents();
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		Form<?> formMain=new Form<Object>("form");
		this.add(formMain);
		
		Button buttonOk=new Button("button"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonOk();
			};
		};
		buttonOk.add(new SimpleAttributeModifier("value",this.getString("caption_button")));
		formMain.add(buttonOk);
	}

	/** реакция на нажатие клавиши OK */
	private void onButtonOk(){
		this.setResponsePage(Catalog.class);
	}
	
}
