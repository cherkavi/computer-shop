package window.assortment_edit.edit_element.assembly.criteria.modal_choice;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import wicket_extension.action.IAjaxActionExecutor;
import window.assortment_edit.edit_element.assembly.criteria.AssemblyCriteria;

/** панель, помещенная на форму для выбора варианта использования одного критерия, 
 * то есть данный вариант может быть типа Edit или типа Select  */
public class PanelChoice extends Panel{
	private static final long serialVersionUID=1L;
	private Model<String> modelEdit=new Model<String>(idEdit);
	private Model<String> modelSelect=new Model<String>(idSelect);
	private Model<String> modelList=new Model<String>(idList);
	
	public static final String idEdit="EDIT";
	public static final String idSelect="SELECT";
	public static final String idList="LIST";
	
	private Model<String> modelGroup=new Model<String>(null);
	private Model<String> modelInit=new Model<String>(null);
	
	private IAjaxActionExecutor executor;
	private AssemblyCriteria criteria;
	private ModalWindow modalWindow;
	
	/** панель, помещенная на форму для выбора варианта использования одного критерия, 
	 * то есть данный вариант может быть типа Edit или типа Select  */
	public PanelChoice(String id,
					   IAjaxActionExecutor executor,
					   AssemblyCriteria criteria,
					   ModalWindow modalWindow){
		super(id);
		this.executor=executor;
		this.criteria=criteria;
		this.modalWindow=modalWindow;
		if(criteria.isEdit()){
			modelGroup.setObject(idEdit);
			modelInit.setObject(idEdit);
		}else if(criteria.isSelect()){
			modelGroup.setObject(idSelect);
			modelInit.setObject(idSelect);
		}else if(criteria.isList()){
			modelGroup.setObject(idList);
			modelInit.setObject(idList);
		}else{
			assert false: "PanelChoice#constructor   Check your AssemblyCriteria Element";
		}
		initComponents();
	}
	
	private void initComponents(){

		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		RadioGroup<String> radioGroup=new RadioGroup<String>("radio_group", modelGroup);
		formMain.add(radioGroup);
		
		// radio Edit
		Radio<String> choiceEdit=new Radio<String>("choice_edit", modelEdit, radioGroup);
		radioGroup.add(choiceEdit);
		radioGroup.add(new Label("label_edit", "Edit"));
		
		// radio Select 
		Radio<String> choiceSelect=new Radio<String>("choice_select", modelSelect, radioGroup);
		radioGroup.add(choiceSelect);
		radioGroup.add(new Label("label_select", "Select"));

		// radio List
		Radio<String> choiceList=new Radio<String>("choice_list", modelList, radioGroup);
		radioGroup.add(choiceList);
		radioGroup.add(new Label("label_list", "List"));
		
		AjaxButton buttonOk=new AjaxButton("button_ok"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, 
									Form<?> form) {
				onButtonOk(target);
			}
		};
		buttonOk.add(new SimpleAttributeModifier("value","Ok"));
		formMain.add(buttonOk);
		
		AjaxButton buttonCancel=new AjaxButton("button_cancel"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, 
									Form<?> form) {
				onButtonCancel(target);
			}
		};
		buttonCancel.add(new SimpleAttributeModifier("value","Cancel"));
		formMain.add(buttonCancel);
	}
	
	/** нажатие на кнопку Ok */
	private void onButtonOk(AjaxRequestTarget target){
		if(!modelInit.getObject().equals(modelGroup.getObject())){
			System.out.println("is change ");
			this.executor.action(target, "CHANGE", new Object[]{this.criteria, modelGroup.getObject()});
			this.modalWindow.close(target);
		}else{
			System.out.println("no change");
			this.modalWindow.close(target);
		}
	}
	
	/** нажатие на кнопку Cancel  */
	private void onButtonCancel(AjaxRequestTarget target){
		this.modalWindow.close(target);
	}
}
