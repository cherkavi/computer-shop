package window.assembly.chooser;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import database.ConnectWrap;

import wicket_extension.UserApplication;
import wicket_extension.action.IActionExecutor;
import wicket_extension.panel_link_action.PanelLinkAction;
import wicket_utility.IConnectorAware;
import window.BasePage;
import window.assembly.element.Element;

/** страница, котора€ предоставл€ет право выбора  */
public class Chooser extends BasePage implements IActionExecutor, IConnectorAware{
	private Element element;
	private ElementDataProvider dataProvider;
	private WebPage pageReturn;
	/**
	 * @param title - заголовок дл€ страницы
	 * @param selectedElement - элемент, который инициирует выборку
	 * @param listOfElement - список элементов, которые уже выделены 
	 * @param returnPage - страница дл€ передачи управлени€ 
	 */
	public Chooser(String title, 
				   Element selectedElement, 
				   ArrayList<Element> listOfElement, 
				   WebPage returnPage){
		this.element=selectedElement;
		this.pageReturn=returnPage;
		initComponents(title,listOfElement);
	}

	/** первоначальна€ инициализаци€ компонентов */
	private void initComponents(String title, ArrayList<Element> listOfElement){
		this.add(new Label("label_element",title));
		dataProvider=new ElementDataProvider(this, 
											 element.getTableName(),
											 listOfElement);
		DefaultDataTable<Serializable> table=new DefaultDataTable<Serializable>("table", // уникальный идентификатор формаы 
																	this.getColumns(), // колонки, которые нужно отображать
																	dataProvider, // данные 
																	20);// кол-во на странице
		this.add(table);
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonCancel();
			}
		};
		buttonCancel.add(new SimpleAttributeModifier("value",this.getString("caption_button_cancel")));
		formMain.add(buttonCancel);
	}

	private void onButtonCancel(){
		this.setResponsePage(this.pageReturn);
	}
	
	@Override
	public ConnectWrap getConnector() {
		return ((UserApplication)this.getApplication()).getConnector();
	}
	
	@SuppressWarnings("unchecked")
	private IColumn<Serializable>[] getColumns(){
		return new IColumn[]{
			new AbstractColumn<Serializable>(new Model<String>("Ќаименование")){
				private final static long serialVersionUID=1L;
				@Override
				public void populateItem(Item<ICellPopulator<Serializable>> item,
										 String componentId, 
										 IModel<Serializable> model) {
					item.add(new PanelLinkAction(componentId,
												 Chooser.this,
												 "LINK",
												 (Integer)(((Object[])model.getObject())[0]),
												 (String)(((Object[])model.getObject())[1]), 
												 "link_class",
												 "link_class_mouseover"));
				}
				
			}
		};
	}
	
    @Override
    public void action(String actionName, Serializable argument) {
    	if(actionName.equals("LINK")){
    		this.element.setTableId((Integer)argument);
    		this.setResponsePage(this.pageReturn);
    	}
    }
	
}

