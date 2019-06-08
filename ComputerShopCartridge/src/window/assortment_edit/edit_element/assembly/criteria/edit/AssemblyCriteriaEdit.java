package window.assortment_edit.edit_element.assembly.criteria.edit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import database.ConnectWrap;

import window.Application;
import window.assortment_edit.edit_element.assembly.criteria.IGetValue;

public class AssemblyCriteriaEdit extends Panel implements IGetValue<String>{
	private final static long serialVersionUID=1L;
	/** ������ ��� ���� ����� �������� */
	private Model<String> modelEdit=new Model<String>("");
	
	/** ���������� ��� ������������ ��� �������������� */
	private int assortmentId;
	
	/** ���������� ��� �������� ��� ������� ������������ */
	private int assortmentDescriptionKod;
	
	
	/** ������ �������������� Edit - �� ����  
	 * @param wicketId - ���������� ������������� ��� �������� Wicket
	 * @param assortmentId - ��� ������������ 
	 * @param assortmentDescriptionKod - ��� �������� �� ������� ASSORTMENT_TYPE_DESCRIPTION.KOD
	 * @param assortmentDescriptionKod - ��� ���� �������� �� ������� ASSORTMENT_TYPE_DESCRIPTION.KOD_ASSORTMENT_TYPE
	 */
	public AssemblyCriteriaEdit(String wicketId, 
								int assortmentId, 
								int assortmentDescriptionKod){
		super(wicketId);
		this.assortmentId=assortmentId;
		this.assortmentDescriptionKod=assortmentDescriptionKod;
		initComponents();
	}
	
	/** �������������� ������������� �����������  */
	private void initComponents(){
		TextField<String> criteriaEdit=new TextField<String>("criteria_edit", modelEdit);
		criteriaEdit.setRequired(false);
		this.add(criteriaEdit);
		
		// �������� ��� ���������� ������������ ������� ��� ��������������
		modelEdit.setObject(this.getEditValue());
	}
	
	/** �������� �������� �� ���� ������ �� ��������� ������� �������� */
	private String getEditValue(){
		String returnValue=null;
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			Connection connection=connector.getConnection();
			PreparedStatement ps=connection.prepareStatement(queryGetRealValue);
			// System.out.println("AssortmentId:"+this.assortmentId);
			ps.setInt(1, this.assortmentId);
			// System.out.println("AssortmentDescriptionKodType:"+this.assortmentDescriptionKod);
			ps.setInt(2, this.assortmentDescriptionKod);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				returnValue=rs.getString("NAME");
			}
		}catch(Exception ex){
			System.err.println("AssemblyCriteriaEdit#getEditValue Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		// System.out.println(">>>"+returnValue);
		return returnValue;
	}
	
	/** ������ �� ��������� �������� �� ���������� ������������ � ���� ����� ������������ 
	 * <table border="1">
	 * 	<tr>
	 * 		<td> <b>1</b></td> 
	 * 		<td> �SSORTMENT_KOD </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td> <b>2</b></td> 
	 * 		<td> �SSORTMENT_DESCRIPTION.KOD_ASSORTMENT_TYPE_DESCRIPTION </td>
	 * 	</tr>
	 * </table> 
	 * */
	private static String queryGetRealValue;
	static{
		StringBuffer query=new StringBuffer();
		  query.append("	select  assortment.kod	\n");
		  query.append("	        ,assortment_description.kod_assortment_type_description	\n");
		  query.append("	        ,assortment_description.name NAME	\n");
		  query.append("	from assortment	\n");
		  query.append("	inner join assortment_description on assortment_description.kod_assortment=assortment.kod	\n");
		  query.append("	    and assortment.kod=?	\n");
		  query.append("	    and assortment_description.kod_assortment_type_description=?	\n");
		  queryGetRealValue=query.toString();
	}

	/**  �������� ��������� ��� ������� ���� */
	private String getTitle(int assortmentTypeDescriptionKod){
		String returnValue="";
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select name from assortment_type_description where kod="+assortmentTypeDescriptionKod);
			if(rs.next()){
				returnValue=rs.getString(1);
			}
		}catch(Exception ex){
			System.err.println("AssemblyCriteriaList#GetTitle Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	@Override
	public String getValue() {
		return this.modelEdit.getObject();
	}

	@Override
	public int getAssortmentTypeDescriptionKod() {
		return this.assortmentDescriptionKod;
	}

	@Override
	public String getAssortmentTypeDescriptionTitle() {
		return this.getTitle(this.assortmentDescriptionKod);
	}
	
}
