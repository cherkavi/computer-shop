package window.title;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import wicket_extension.action.IActionExecutor;

/** панель-заголовок для базовой страницы, содержит ссылки на Action: 
	<li>"CATALOG"</li>
	<li>"ASSEMBLY"</li>
	<li>"FILL"</li> 
	<li>"QUESTION"</li> 
	<li>"JOBBER"</li> 
	<li>"CONTAKT"</li> 
 * */
public class Title extends Panel{
	private final static long serialVersionUID=1L;
	private IActionExecutor actionExecutor;
	
	public Title(String id,IActionExecutor actionExecutor){
		super(id);
		this.actionExecutor=actionExecutor;
		initComponents();
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		Label labelCaptionWellcom=new Label("caption_wellcom",this.getString("caption_wellcom"));
		labelCaptionWellcom.setEscapeModelStrings(false);
		this.add(labelCaptionWellcom);
		
		WebComponent component=new WebComponent("caption_logo");
		component.add(new SimpleAttributeModifier("src","images/Logo.png"));
		this.add(component);
		//this.add(new Label("caption_logo",this.getString("caption_logo")));
		
		Label labelCaptionContakt=new Label("caption_contakt",this.getString("caption_contakt"));
		labelCaptionContakt.setEscapeModelStrings(false);
		this.add(labelCaptionContakt);
		
		Link<?> linkCatalog=new Link<Object>("link_catalog"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onLinkCatalog();
			}
		};
		this.add(linkCatalog);
		
		Link<?> linkAssembly=new Link<Object>("link_assembly"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onLinkAssembly();
			}
		};
		this.add(linkAssembly);
		
		Link<?> linkFill=new Link<Object>("link_fill"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onLinkFill();
			}
		};
		this.add(linkFill);
		
		Link<?> linkQuestion=new Link<Object>("link_question"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onLinkQuestion();
			}
		};
		this.add(linkQuestion);

		 
		ExternalLink linkJobber=new ExternalLink("link_jobber","/ComputerShopJobberResource/jobber.jnlp");
		this.add(linkJobber);
		
		Link<?> linkContakt=new Link<Object>("link_contakt"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onLinkContakt();
			}
		};
		this.add(linkContakt);
	}
	
	/** реакция на нажатие ссылки Каталог */
	private void onLinkCatalog(){
		this.actionExecutor.action("CATALOG", null);
	}
	/** реакция на нажатие ссылки Сборка */
	private void onLinkAssembly(){
		this.actionExecutor.action("ASSEMBLY", null); 
	}
	/** реакция на нажатие ссылки Наполнение */
	private void onLinkFill(){
		this.actionExecutor.action("FILL", null); 
	}
	/** реакция на нажатие ссылки Вопрос */
	private void onLinkQuestion(){
		this.actionExecutor.action("QUESTION", null); 
	}
	/** реакция на нажатие ссылки Оптовикам */
	private void onLinkJobber(){
		this.actionExecutor.action("JOBBER", null); 
	}
	/** реакция на нажатие ссылки Контакт */
	private void onLinkContakt(){
		this.actionExecutor.action("CONTAKT", null); 
	}
	
}
