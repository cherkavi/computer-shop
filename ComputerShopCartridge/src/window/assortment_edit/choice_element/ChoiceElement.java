package window.assortment_edit.choice_element;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.Model;

import window.assortment_edit.BaseAssortmentEdit;
import window.assortment_edit.choice_element.panel_table.PanelTableAssortment;
import window.main_menu.utility.point_choice.ChoicePoint;

/** �������� ��� ������ ������������ � ����� ��� ��������������  */
public class ChoiceElement extends BaseAssortmentEdit{
	private Model<String> modelName;
	private Model<String> modelBarCode;
	private static String panelResultId="panel_result";
	
	private Model<Boolean> modelCheckboxFilter=new Model<Boolean>(true);
	/** �������� ��� ������ ������������ � ����� ��� ��������������  
	 * @param filterName - ���� �������
	 * @param filterBarCode - ���� BarCode 
	 */
	public ChoiceElement(String filterName, String filterBarCode){
		modelName=new Model<String>(filterName);
		modelBarCode=new Model<String>(filterBarCode);
		
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		TextField<String> name=new TextField<String>("text_name",modelName);
		name.setRequired(false);
		formMain.add(name);
		
		TextField<String> bar_code=new TextField<String>("text_bar_code",modelBarCode);
		name.setRequired(false);
		formMain.add(bar_code);
		
		Button buttonFilter=new Button("button_filter"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonFilter();
			};
		};
		buttonFilter.add(new SimpleAttributeModifier("value",this.getString("label_button_filter")));
		formMain.add(buttonFilter);
		
		Button buttonExit=new Button("button_exit"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonExit();
			}
		};
		buttonExit.add(new SimpleAttributeModifier("value",this.getString("label_button_exit")));
		formMain.add(buttonExit);
		
		CheckBox checkboxFilter=new CheckBox("checkbox_filter_assembly",modelCheckboxFilter);
		formMain.add(checkboxFilter);
		formMain.add(new Label("label_filter","������ ��, � ������� ��� ���������� � ������"));
		
		if(this.isFilterEmpty(filterName, filterBarCode)){
			// ��� ��������
			this.add(new EmptyPanel(panelResultId));
		}else{
			this.add(new PanelTableAssortment(panelResultId,
											  modelName.getObject(), 
											  modelBarCode.getObject(),
											  modelCheckboxFilter.getObject()
											  )
					 );
		}
	}
	
	/** ������� �� ����� �� ������� ������  */
	private void onButtonExit(){
		this.setResponsePage(new ChoicePoint());
	}
	
	/** �������� �������� �� ��������� ��������  */
	private boolean isFilterEmpty(String filterName, String filterBarCode){
		if (((filterName==null)||(filterName.equals("")))
		   &&((filterBarCode==null)||(filterBarCode.equals("")))){
			return true;
		}else{
			return false;
		}
	}

	/** ������� �� ������� ������ ������ */
	private void onButtonFilter(){
		// ������� ������ ������
		if(isFilterEmpty(this.modelName.getObject(), this.modelBarCode.getObject())){
			// ������ �� ��������
			if(this.get(panelResultId) instanceof EmptyPanel){
				// ����������� ������ ������
			}else{
				// ����������� �� ������ ������ - �������� 
				this.remove(panelResultId);
				this.add(new EmptyPanel(panelResultId));
			}
		}else{
			// ������ ��������
			if(this.get(panelResultId) instanceof EmptyPanel){
				// ����������� ������ ������
				this.remove(panelResultId);
				this.add(new PanelTableAssortment(panelResultId,
												  this.modelName.getObject(), 
												  this.modelBarCode.getObject(),
												  this.modelCheckboxFilter.getObject()
												  )
						);
			}else{
				// ����������� ������ � ��������, ���������� ����� ������ 
				((PanelTableAssortment)this.get(panelResultId)).setFilter(this.modelName.getObject(), this.modelBarCode.getObject(),this.modelCheckboxFilter.getObject());
			}
		}
	}
	
}
