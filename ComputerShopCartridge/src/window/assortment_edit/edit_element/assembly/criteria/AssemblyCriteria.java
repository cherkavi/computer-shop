package window.assortment_edit.edit_element.assembly.criteria;

import java.sql.ResultSet;

import org.apache.wicket.ajax.AjaxEventBehavior;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

import database.ConnectWrap;
import database.bind.EAssemblyEditType;

import wicket_extension.action.IAjaxActionExecutor;
import window.Application;
import window.assortment_edit.edit_element.assembly.criteria.edit.AssemblyCriteriaEdit;
import window.assortment_edit.edit_element.assembly.criteria.list.AssemblyCriteriaList;
import window.assortment_edit.edit_element.assembly.criteria.select.AssemblyCriteriaSelect;

/** �������� ������ */
public class AssemblyCriteria extends Panel{
	private final static long serialVersionUID=1L;
	/** ������� �� ������ �������������  */
	public final static String actionRemoveFromList="remove_from_list";
	
	/** ��� ������ ��� ������� ��������  
	 * <table>
	 * 	<tr>
	 * 		<th> ��� </th> <th> �������� </th>
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 0 </td> <td> Edit </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 1 </td> <td> DropDown </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 2 </td> <td> Select List </td>
	 * 	</tr>
	 * </table>
	 * */
	private int assemblyEditType;
	
	/** ������������ ������, �������� ������� ���������� ��������� ���������� � ����������� */
	private IAjaxActionExecutor actionExecutor;
	
	/** ������ ��� �������������� ������ */
	private Panel panelEdit;

	/** ��� ������������, ������� � ������ ������ ������������� */
	private int assortmentKod;
	
	/** ��� ������������� �� ������� ASSORTMENT_TYPE_DESCRIPTION.KOD */
	private int assortmentTypeDescriptionKod;
	
	/** �������� ������ (ASSORTMENT_TYPE_DESCRIPTION)
	 * @param id - ���������� ������������� �� �������� Wicket
	 * @param ajaxAction - ����������� ������������� ���� � ������������� ���������� ��������� ��������
	 * @param title - ������������ ������� �������� ������ 
	 * @param assortmentKod - ���������� ��� ������������
	 * @param assortmentTypeDescriptionKod - ���������� ����� �� ������������ ������
	 * @param assemblyEditType
	 * <table>
	 * 	<tr>
	 * 		<th> ��� </th> <th> �������� </th>
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 0 </td> <td> Edit </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 1 </td> <td> DropDown </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 2 </td> <td> Select List </td>
	 * 	</tr>
	 * </table>
	 * */
	public AssemblyCriteria(String wicketId,
							IAjaxActionExecutor ajaxAction,
							String title, 
							int assortmentKod, 
							int assortmentTypeDescriptionKod,
							int assemblyEditType){
		super(wicketId);
		this.actionExecutor=ajaxAction;
		this.assortmentKod=assortmentKod;
		this.assortmentTypeDescriptionKod=assortmentTypeDescriptionKod;
		this.assemblyEditType=assemblyEditType;
		this.initComponents(title);
	}
	
	/** ��� �� �������: ASSORTMENT.KOD */
	public int getAssortmentKod(){
		return this.assortmentKod;
	}
	
	/** @return assortmentTypeDescriptionKod - ���������� ����� �� ������������ ������ */
	public int getAssortmentTypeDescriptionKod(){
		return this.assortmentTypeDescriptionKod;
	}
	
	/**
	 * @return
	 * <table border="1">
	 * 	<tr>
	 * 		<th> ��� </th> <th> �������� </th>
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 0 </td> <td> Edit </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 1 </td> <td> DropDown </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 2 </td> <td> Select List </td>
	 * 	</tr>
	 * </table>
	 */
	public int getAssemblyEditType(){
		return this.assemblyEditType;
	}
	
	private void initComponents(String title){
		this.add(new Label("title",title));
		Button buttonEdit=new Button("button_edit_criteria");
		this.add(buttonEdit);
		buttonEdit.add(new SimpleAttributeModifier("value",this.getString("caption_button_edit")));
		buttonEdit.add(new AjaxEventBehavior("onclick") {
			private final static long serialVersionUID=1L;
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonEdit(target);
			}
		});
		
		/*Button buttonRemove=new Button("button_remove");
		this.add(buttonRemove);
		buttonRemove.add(new SimpleAttributeModifier("value",this.getString("caption_button_remove")));
		buttonRemove.add(new AjaxEventBehavior("onclick") {
			private final static long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonRemove(target);
			}
		});*/
		
		this.panelEditWrap=new WebMarkupContainer("panel_edit_wrap");
		this.add(panelEditWrap);
		
		this.reInit(EAssemblyEditType.getByInt(this.assemblyEditType));
	}
	/** ��������� ��� ������ ��������������, ������� ����� ���� ���� Edit ���� Select ���� ����� ���� �������� � ��������  */
	private WebMarkupContainer panelEditWrap;
	
	/* �������� �� ������ ������ ������� ��� ������ �� ������ ��������� ���������� 
	private boolean isEditSelect(){
		boolean returnValue=false;
		ConnectWrap connector=((Application)this.getApplication()).getConnector();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select assembly_edit_type from assortment_type_description where kod="+this.assortmentTypeDescription);
			if(rs.next()){
				Integer value=rs.getInt(1);
				if(rs.wasNull()){
					value=null;
				};
				if((value==null)||
				   (value.intValue()==0)){
					// �������� null ��� 0 
					returnValue=false;
				}else {
					// �������� �������� �� null, 0 - ����� �� ������ 
					returnValue=true;
				}
			}else{
				assert false: "AssemblyCriteria#isEditSelect, element is not found:"+this.assortmentTypeDescription;
			}
		}catch(Exception ex){
			System.err.println("AssemblyCriteria#idEditSelect: "+ex.getMessage());
		}finally{
			try{
				connector.close();
			}catch(NullPointerException ex){}
		}
		return returnValue;
	}
	 */	
	
	private void onButtonEdit(AjaxRequestTarget target){
		this.actionExecutor.action(target, "EDIT",this);
	}

	/** �������� �� ������ ��� �������������� ����� ��� �����  */
	public boolean isEdit(){
		return (this.panelEdit instanceof AssemblyCriteriaEdit);
	}
	
	/** �������� �� ������ ��� �������������� ����������� ������ */
	public boolean isSelect(){
		return (this.panelEdit instanceof AssemblyCriteriaSelect);
	}
	
	/** �������� �� ������ ��� �������������� ����������� �������������� ������ */
	public boolean isList(){
		return (this.panelEdit instanceof AssemblyCriteriaList);
	}

	/** ������������������ ������, ������ ������ ���� ������ - PanelEdit �� ������� ���������� ������ */
	public void reInit() {
		// �������� �� ���� ������ ����� ��� ��� ������� ��������
		this.assemblyEditType=getCurrentAssemblyEditType(this.assortmentTypeDescriptionKod);
		reInit(EAssemblyEditType.getByInt(this.assemblyEditType));
	}
	
	/** �������� �������� AssemblyEditType �� ��������� ����������� ASSORTMENT_TYPE_DESCRIPTION.KOD */
	private int getCurrentAssemblyEditType(int kod){
		int returnValue=0;
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from assortment_type_description where kod="+kod);
			if(rs.next()){
				returnValue=rs.getInt("ASSEMBLY_EDIT_TYPE");
			}
		}catch(Exception ex){ 
			System.err.println("AssemblyCriteria#getCurrentAssemblyEditType:");
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	private void reInit(EAssemblyEditType assemblyEditType){
		panelEditWrap.removeAll();
		// ������ ����������������� �������������� ������
		if(assemblyEditType.equals(EAssemblyEditType.EDIT)){
			// EDIT
			panelEdit=new AssemblyCriteriaEdit("panel_edit",
												this.assortmentKod,
												this.assortmentTypeDescriptionKod);
		}else if(assemblyEditType.equals(EAssemblyEditType.SELECT)){
			// SELECT
			panelEdit=new AssemblyCriteriaSelect("panel_edit", 
											     this.assortmentTypeDescriptionKod, 
											     this.assortmentKod);
		}else if(assemblyEditType.equals(EAssemblyEditType.LIST)){
			// LIST
			panelEdit=new AssemblyCriteriaList("panel_edit", 
											   this.assortmentTypeDescriptionKod, 
											   this.assortmentKod);
		}else {
			panelEdit=new EmptyPanel("panel_edit");
			assert false:"AssemblyCriteria#initComponents assemblyEditType does not found:"+this.assemblyEditType;
		}
		panelEditWrap.add(panelEdit);
	}
	
	/**
	 * @return
	 * �������� ������ � ���������:
	 * <ul> 
	 *	<li> {@link AssemblyCriteriaEdit} </li> 
	 *	<li> {@link AssemblyCriteriaSelect} </li> 
	 *	<li> {@link AssemblyCriteriaList} </li> 
	 * </ul>
	 * 
	 */
	public Object getCriteriaPanel(){
		return this.panelEdit;
	}
	
}
