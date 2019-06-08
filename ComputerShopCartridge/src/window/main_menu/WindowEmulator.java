package window.main_menu;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import window.BasePage;

public class WindowEmulator extends BasePage{
	private Label labelTitle;

	public WindowEmulator(){
		initComponents(null);
	}

	public WindowEmulator(String title){
		initComponents(title);
	}
	
	private void initComponents(String title){
		this.labelTitle=new Label("caption_main",new Model<String>(title));
		this.add(this.labelTitle);
	}
	
	public void setTitle(String title){
		this.labelTitle.setDefaultModelObject(title);
	}
}
