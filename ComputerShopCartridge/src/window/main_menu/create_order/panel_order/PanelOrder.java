package window.main_menu.create_order.panel_order;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import wicket_extension.UserApplication;
import wicket_extension.action.IActionExecutor;
import database.ConnectWrap;

/** панель, которая отображает заказ */
public class PanelOrder extends Panel{
	private final static long serialVersionUID=1L;
	private OrderPanelElement element;
	private DropDownChoice<String> dropDownVendor;
	private DropDownChoice<String> dropDownModel;
	private Model<String> modelPrice=new Model<String>();
	private IActionExecutor executor;
	private WebMarkupContainer componentMain;
	
	/** панель, которая отображает элемент заказа*/
	public PanelOrder(String id, OrderPanelElement element, IActionExecutor executor) {
		super(id);
		this.element=element;
		this.executor=executor;
		initComponents();
	}

	/** установить цену в Caption */
	public void setPriceToModel(Float value){
		this.modelPrice.setObject(value.toString());
	}
	
	private void initComponents(){
		/** элемент, который содержит в себе все визуальные элемент */
		this.componentMain=new WebMarkupContainer("component_main");
		this.componentMain.setOutputMarkupId(true);
		this.add(this.componentMain);
		// Select Vendor
		Form<Object> formVendor=new Form<Object>("form_vendor");
		ArrayList<String> listVendor=this.getListFromTable("select * from cartridge_vendor where (select count(*) from cartridge_model where cartridge_vendor.id=cartridge_model.id_vendor)>0", "name");
		this.dropDownVendor=new DropDownChoice<String>("select_vendor",listVendor);
		int vendorIndex=listVendor.indexOf(this.element.getVendor());
		this.dropDownVendor.setDefaultModel(new Model<String>(listVendor.get(vendorIndex)));
		this.dropDownVendor.setOutputMarkupId(true);
		this.dropDownVendor.add(new AjaxFormSubmitBehavior(formVendor,"onchange") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				onVendorChange(target);
			}
			@Override
			protected void onError(AjaxRequestTarget arg0) {
			}
		});
		formVendor.add(this.dropDownVendor);
		this.componentMain.add(formVendor);

		// Select Model
		Form<Object> formModel=new Form<Object>("form_model");
		ArrayList<String> listModel=this.getListFromTable("select * from cartridge_model where cartridge_model.id_vendor=(select cartridge_vendor.id from cartridge_vendor where cartridge_vendor.name='"+this.element.getVendor().replaceAll("'", "''")+"')", "name");
		this.dropDownModel=new DropDownChoice<String>("select_model",listModel);
		int modelIndex=listModel.indexOf(this.element.getModel());
		this.dropDownModel.setDefaultModel(new Model<String>(listModel.get(modelIndex)));
		this.dropDownModel.setOutputMarkupId(true);
		this.dropDownModel.add(new AjaxFormSubmitBehavior(formModel,"onchange") {
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				onModelChange(target);
			}
			@Override
			protected void onError(AjaxRequestTarget arg0) {
			}
		});
		formModel.add(this.dropDownModel);
		this.componentMain.add(formModel);

		// Price
		this.modelPrice.setObject(this.getPrice(listVendor.get(vendorIndex), listModel.get(modelIndex)));
		Label labelPrice=new Label("label_price",this.modelPrice);
		labelPrice.setOutputMarkupId(true);
		this.componentMain.add(labelPrice);
		
		// Button Remove
		WebMarkupContainer buttonRemove=new WebMarkupContainer("button_remove");
		buttonRemove.add(new AjaxEventBehavior("onclick") {
			private final static long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonRemove(target);
			}
		});
		buttonRemove.add(new SimpleAttributeModifier("value","Удалить"));
		this.componentMain.add(buttonRemove);
		
		WebMarkupContainer buttonEdit=new WebMarkupContainer("button_edit");
		buttonEdit.add(new AjaxEventBehavior("onclick"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonEdit(target);
			}
		});
		buttonEdit.add(new SimpleAttributeModifier("value","Редактировать/Добавить"));
		this.componentMain.add(buttonEdit);
	}
	
	private String getPrice(String vendor, String model){
		String returnValue=null;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		ResultSet rs=null;
		try{
			rs=connector.getConnection().createStatement().executeQuery("select * from get_cartridge_model('"+vendor.replaceAll("'", "''")+"','"+model.replaceAll("'", "''")+"')");
			rs.next();
			returnValue=rs.getString("price");
		}catch(Exception ex){
			System.err.println("PanelOrder#getPrice Exception: "+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
		}
		connector.close();
		return returnValue;
	}
	
	/** получить ArrayList на основании запроса к базе данных и имени колонки из этого запроса 
	 * @param sql - запрос к базе данных, на основании которого будут отобраны данные
	 * @param column - имя колонки, из которой будут "вытащены" данные 
	 * @return ArrayList с пустой строкой в начале 
	 */
	private ArrayList<String> getListFromTable(String sql, String column){
		ArrayList<String> returnValue=new ArrayList<String>();
		ConnectWrap connector=null;
		ResultSet rs=null;
		try{
			connector=((UserApplication)this.getApplication()).getConnector();
			rs=connector.getConnection().createStatement().executeQuery(sql);
			while(rs.next()){
				returnValue.add(rs.getString(column));
			}
		}catch(Exception ex){
			System.out.println("PanelOrder#getListFromTable: "+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	/** обновить список моделей по текущему производителю */
	public ArrayList<String> refreshModelList(){
		ArrayList<String> listModel=this.getListFromTable("select * from cartridge_model where cartridge_model.id_vendor=(select cartridge_vendor.id from cartridge_vendor where cartridge_vendor.name='"+this.element.getVendor().replaceAll("'", "''")+"')", "name");
		this.dropDownModel.setChoices(listModel);
		return listModel;
	}
	
	/** установить модель по её имени */
	public void setModel(String modelName){
		int index=this.dropDownModel.getChoices().indexOf(modelName);
		if(index>=0){
			this.dropDownModel.setDefaultModel(new Model<String>(this.dropDownModel.getChoices().get(index)));
			this.modelPrice.setObject(this.getPrice(this.dropDownVendor.getModelObject(), this.dropDownModel.getModelObject()));
			ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
			this.element.setModel(this.dropDownModel.getModelObject(),connector);
			connector.close();
		}
	}
	
	private void onVendorChange(AjaxRequestTarget target){
		// загрузить данные в модель
		this.element.setVendor(this.dropDownVendor.getModelObject());
		ArrayList<String> listModel=this.refreshModelList();
		int modelIndex=0;
		this.dropDownModel.setDefaultModel(new Model<String>(listModel.get(modelIndex)));
		// обновить цену
		this.modelPrice.setObject(this.getPrice(this.dropDownVendor.getModelObject(), this.dropDownModel.getModelObject()));
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		this.element.setModel(this.dropDownModel.getModelObject(),connector);
		connector.close();
		target.addComponent(this.componentMain);
	}
	
	private void onModelChange(AjaxRequestTarget target){
		this.modelPrice.setObject(this.getPrice(this.dropDownVendor.getModelObject(), 
								  this.dropDownModel.getModelObject()));
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		this.element.setModel(this.dropDownModel.getModelObject(),connector);
		connector.close();
		target.addComponent(this.componentMain);
	}
	
	private void onButtonRemove(AjaxRequestTarget target){
		this.executor.action("REMOVE", new Object[]{target, this});
	}
	
	private void onButtonEdit(AjaxRequestTarget target){
		this.executor.action("EDIT",new Object[]{target,this});
	}
	
	public OrderPanelElement getElement(){
		return this.element;
	}
}
