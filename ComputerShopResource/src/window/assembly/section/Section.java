package window.assembly.section;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import wicket_extension.action.IActionExecutor;
import wicket_extension.panel_link_action.PanelLinkAction;
import window.BasePage;
import window.assembly.AssemblyCommands;
import window.assembly.element.Element;

/** страница, которая предоставляет право выбора  */
public class Section extends BasePage implements IActionExecutor{
	private ArrayList<Element> listOfElement;
	private ArrayList<String> tables;
	private Model<ArrayList<String>> elementsTitle=new Model<ArrayList<String>>();
	private IActionExecutor executor;
	private ArrayList<String> tablesTitle;
	private WebPage pageCancel;
	/** 
	 * @param indexElement - индекс выделенного элемента, по которому нужно выбрать запись из базы данных
	 * @param listOfElements - перечень всех элементов, которые уже добавлены 
	 * @param tables - список таблиц 
	 * @param tablesTitle - заголовки для таблиц
	 * @param pageCancel - страница для отмены выбора 
	 */
	public Section(IActionExecutor executor, ArrayList<Element> listOfElement, ArrayList<String> tables, ArrayList<String> tablesTitle, WebPage pageCancel){
		this.listOfElement=listOfElement;
		this.tables=tables;
		this.executor=executor;
		this.tablesTitle=tablesTitle;
		this.pageCancel=pageCancel;
		initComponents();
	}

	/** первоначальная инициализация компонентов */
	private void initComponents(){
		// получить список тех, которых нет в наличии 
		this.elementsTitle.setObject(this.getElements());
		this.add(new ListView<String>("list_of_element",this.elementsTitle){
			private final static long serialVersionUID=1L;
			
			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(new PanelLinkAction("element", 
											 Section.this, 
											 "LINK", 
											 item.getModelObject(), 
											 item.getModelObject(),
											 "link_class", 
											 "link_class_mouseover")
						);
			}
		});
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonCancel();
			}
		};
		buttonCancel.add(new SimpleAttributeModifier("value",this.getString("caption_button_cancel")));
		formMain.add(buttonCancel);
	}
	
	private void onButtonCancel(){
		this.setResponsePage(this.pageCancel);
	}
	

	private ArrayList<String> getElements(){
		ArrayList<String> returnValue=new ArrayList<String>();
		
		for(int counter=0;counter<this.tables.size();counter++){
			String currentTableName=this.tables.get(counter);
			for(int index=0;index<this.listOfElement.size();index++){
				if(currentTableName.equals(this.listOfElement.get(index).getTableName())){
					currentTableName=null;
					break;
				}
			}
			if(currentTableName!=null){
				returnValue.add(this.tablesTitle.get(counter));
			}
		}
		return returnValue;
	}
	
	@Override
	public void action(String actionName, Serializable argument) {
		if(actionName.equals("LINK")){
			String value=(String)argument;
			int index=this.tablesTitle.indexOf(value);
			this.executor.action(AssemblyCommands.ADD_SECTION.name(),this.tables.get(index));
			this.setResponsePage(pageCancel);
		}
	}
}
