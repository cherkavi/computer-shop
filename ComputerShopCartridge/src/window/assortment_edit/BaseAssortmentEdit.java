package window.assortment_edit;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebPage;

public abstract class BaseAssortmentEdit extends WebPage{

	public BaseAssortmentEdit(){
		MarkupContainer container=new MarkupContainer("css_type"){
			private final static long serialVersionUID=1L;
		};
		
		container.add(new SimpleAttributeModifier("href","cartridge_page.css"));
		this.add(container);
	}
}
