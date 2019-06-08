package window.assembly;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import wicket_extension.action.IActionExecutor;
import window.BasePage;
import window.assembly.chooser.Chooser;
import window.assembly.element.Element;
import window.assembly.section.Section;

/** ������-��������� ��� ������� �������� */
public class Assembly extends BasePage implements IActionExecutor{
	private final static long serialVersionUID=1L;
	private ArrayList<String> tables=new ArrayList<String>();
	private ArrayList<String> tablesTitle=new ArrayList<String>();
	{
		tables.add("mb");
		tablesTitle.add("����������� �����");
		
		tables.add("processor");
		tablesTitle.add("���������");
		
		tables.add("video");
		tablesTitle.add("�����");

		tables.add("memory");
		tablesTitle.add("������");

		tables.add("hdd");
		tablesTitle.add("������� ����");
	}
	private final Model<ArrayList<Element>> listOfElement=new Model<ArrayList<Element>>(new ArrayList<Element>());
	
	public Assembly(){
		listOfElement.getObject().add(new Element("element",this, this.tablesTitle.get(0),this.tables.get(0),null,false));
		listOfElement.getObject().add(new Element("element",this, this.tablesTitle.get(1),this.tables.get(1),null,false));
		initComponents();
	}
	
	/** �������������� ������������� ����������� */
	private void initComponents(){
		this.add(new ListView<Element>("list_of_element",this.listOfElement){
			private final static long serialVersionUID=1L;
			
			@Override
			protected void populateItem(ListItem<Element> item) {
				item.add(item.getModelObject());
			}
		});
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		Button buttonAdd=new Button("button_add"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonAdd();
			}
			
			@Override
			public boolean isVisible() {
				return Assembly.this.isElementForAddExists();
			}
		};
		buttonAdd.add(new SimpleAttributeModifier("value",this.getString("caption_button_add")));
		formMain.add(buttonAdd);
	}

	/** ���������� �� ��� �������� ��� ���������� */
	private boolean isElementForAddExists(){
		return this.listOfElement.getObject().size()<this.tables.size();
	}
	
	private void onButtonAdd(){
		// �������� ����, � ���������� �� ���� ���� ��� ����������, �� ������� �������� ������
		this.setResponsePage(new Section(this,this.listOfElement.getObject(),this.tables,this.tablesTitle,this));
	}
	
	@Override
	public void action(String actionName, Serializable argument){
		super.action(actionName, argument);
		// �����, ���� �������� ��� ����������
			// ������� ������� 
		if(actionName.equals(AssemblyCommands.REMOVE.toString())){
			// remove element
			int removeIndex=this.listOfElement.getObject().indexOf(argument);
			if(removeIndex>=0){
				int tableIndex=this.tables.indexOf(this.listOfElement.getObject().get(removeIndex).getTableName());
				if(tableIndex<=1){
					// �� ����� ���� ������ - ���� ����������� �����, ���� ��������� 
				}else{
					this.listOfElement.getObject().remove(removeIndex);
				}
			}else{
				System.err.println("Index was not found");
			}
		}
			// ������� ��� ��������� ������ ���������� ����� �� ������� 
		if(actionName.equals(AssemblyCommands.CHOOSE_ID.toString())){
			Element currentElement=(Element)argument;
			int index=this.tables.indexOf(currentElement.getTableName());
			this.setResponsePage(new Chooser("������� �������: "+this.tablesTitle.get(index),
											 currentElement,
											 this.listOfElement.getObject(),
											 this));
		}
		
		if(actionName.equals(AssemblyCommands.ADD_SECTION.toString())){
			int tableIndex=this.tables.indexOf((String)argument);
			if(tableIndex>=0){
				this.listOfElement.getObject().add(new Element("element",this, this.tablesTitle.get(tableIndex),this.tables.get(tableIndex),null,true));
			}else{
				System.out.println("ADD_SECTION not found:"+argument);
			}
		}
	}
}
