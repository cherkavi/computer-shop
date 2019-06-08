package window;

import java.io.Serializable;

import org.apache.wicket.MarkupContainer;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.Panel;

import wicket_extension.action.IActionExecutor;
import window.assembly.Assembly;
import window.catalog.Catalog;
import window.contakt.Contakt;
import window.fill_cartridge.FillCartridge;
import window.question.Question;
import window.title.Title;

public abstract class BasePage extends WebPage implements IActionExecutor{
	
	public BasePage(){
		MarkupContainer container=new MarkupContainer("css_type"){
			private final static long serialVersionUID=1L;
		};
		container.add(new SimpleAttributeModifier("href","astronomy.css"));
		this.add(container);
		this.add(getTitle());
	}
	
	/** получить панель, которая является заголовком для */
	public Panel getTitle(){
		return new Title("title",
						 this);
	}

	public void action(String actionName, Serializable argument) {
		if(actionName.equals("CATALOG")){
			Catalog catalog=new Catalog();
			this.setResponsePage(catalog);
		}
		if(actionName.equals("ASSEMBLY")){
			Assembly assembly=new Assembly();
			this.setResponsePage(assembly);
		}
		if(actionName.equals("FILL")){
			FillCartridge fillCartrigde=new FillCartridge();
			this.setResponsePage(fillCartrigde);
		}
		if(actionName.equals("QUESTION")){
			Question question=new Question();
			this.setResponsePage(question);
		}
		if(actionName.equals("CONTAKT")){
			Contakt contakt=new Contakt();
			this.setResponsePage(contakt);
		}
	}
	
}
