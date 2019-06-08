package wicket_extension.panel_link_action;

import java.io.Serializable;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import wicket_extension.action.IActionExecutor;

/** панель со ссылкой, которая передает управление на IActionExecutor и является ссылкой */
public class PanelLinkAction extends Panel{
	private final static long serialVersionUID=1L;
	
	/** панель со ссылкой, которая передает управление на IActionExecutor и является ссылкой 
	 * @param id - уникальный идентификатор панели 
	 * @param executor - исполнитель, которому передается управление
	 * @param actionName - имя Action, 
	 * @param argument - аргумент, который должен передваться
	 * @param caption - текст, который отображается на панели 
	 * @param htmlClassName - nullable HTML класс, который отображается на панели
	 */
	public PanelLinkAction(String id, 
						   final IActionExecutor executor,
						   final String actionName,
						   final Serializable argument, 
						   String caption, 
						   String htmlClassName){
		super(id);
		this.add(this.getLink(executor, actionName, argument, caption, htmlClassName,null));
	}

	/** панель со ссылкой, которая передает управление на IActionExecutor и является ссылкой 
	 * @param id - уникальный идентификатор панели 
	 * @param executor - исполнитель, которому передается управление
	 * @param actionName - имя Action, 
	 * @param argument - аргумент, который должен передваться
	 * @param caption - текст, который отображается на панели 
	 * @param htmlClassName - nullable HTML класс, который отображается на панели
	 * @param htmlClassNameOver - nullable HTML класс, который назначается элементу при наведении курсора 
	 */
	public PanelLinkAction(String id, 
						   final IActionExecutor executor,
						   final String actionName,
						   final Serializable argument, 
						   String caption, 
						   String htmlClassName,
						   String htmlClassNameOver){
		super(id);
		this.add(this.getLink(executor, actionName, argument, caption, htmlClassName,htmlClassNameOver));
	}
	
	
	private Link<?> getLink(final IActionExecutor executor,
			   final String actionName,
			   final Serializable argument, 
			   String caption, 
			   String htmlClassName,
			   String htmlOverClass){
		
		Link<?> link=new Link<Object>("link"){
			private final static long serialVersionUID=1L;
			public void onClick() {
				executor.action(actionName, argument);
			};
		};
		link.add(new Label("caption",caption));
		
		if(htmlClassName!=null){
			link.add(new SimpleAttributeModifier("class",htmlClassName));
		}
		if(htmlOverClass!=null){
			link.add(new SimpleAttributeModifier("onmouseover","this.className='"+htmlOverClass+"'"));
			link.add(new SimpleAttributeModifier("onmouseout","this.className='"+htmlClassName+"'"));
		}
		return link;
	}
}
