package window.main_menu.return_to_user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import database.ConnectWrap;
import wicket_extension.UserApplication;
import window.main_menu.MainMenu;
import window.main_menu.WindowEmulator;
import window.main_menu.return_to_user.marker.ReturnToUserElement;

public class ReturnToUser extends WindowEmulator{
	private TextField<String> findNumber;
	
	public ReturnToUser(){
		super("Выдать картридж пользователю");
		initComponents();
	}
	
	@SuppressWarnings("unchecked")
	private void initComponents(){
		// панель поиска
		MarkupContainer fieldset=new MarkupContainer("panel_find") {
			private final static long serialVersionUID=1L;
		};
		fieldset.add(new SimpleAttributeModifier("title","Панель поиска"));
		this.add(fieldset);
		fieldset.add(new Label("panel_find_legend","Панель поиска"));

		final Form<Object> formFind=new Form<Object>("form_find"){
			private final static long serialVersionUID=1L;
			protected void onSubmit() {
				onFindForm();
			};
		};
		formFind.setOutputMarkupId(true);
		
		formFind.add(new Label("label_find_number","Поиск по номеру"));

		this.findNumber=new TextField<String>("find_number",new Model<String>("")){
			private final static long serialVersionUID=1L;
			protected void onComponentTag(org.apache.wicket.markup.ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("onkeypress", "if(event.keyCode==13){"+formFind.getMarkupId()+".submit()}");
				tag.put("onclick", ReturnToUser.this.findNumber.getMarkupId()+".value=''");
			};
		};
		this.findNumber.setOutputMarkupId(true);
		formFind.add(this.findNumber);
		fieldset.add(formFind);
		
		Button buttonFindSubmit=new Button("button_find_submit");
		buttonFindSubmit.add(new SimpleAttributeModifier("value","Поиск"));
		formFind.add(buttonFindSubmit);
		
		Form<Object> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		Button buttonMainMenu=new Button("button_main_menu"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonMainMenu();
			}
		};
		buttonMainMenu.add(new SimpleAttributeModifier("value","В главное меню"));
		formMain.add(buttonMainMenu);
		
		// Таблица с данными:
		SortableDataProvider<TableElement> provider=this.getSortableDataProvider();
		IColumn[] columns=this.getColumns();
		DefaultDataTable<TableElement> table=new DefaultDataTable<TableElement>("table_main",columns,provider,5);
		this.add(table);
	}

	/** получить колонки для таблицы */
	@SuppressWarnings("unchecked")
	private IColumn[] getColumns(){
		//
		return new IColumn[]{
			new AbstractColumn<TableElement>(new Model("Номер заказа")){
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item<ICellPopulator<TableElement>> cellItem, 
										 String id,
										 IModel<TableElement> model) {
					cellItem.add(new PanelLink(id, Integer.toString(model.getObject().getUniqueNumber()),model.getObject().getId()));
				}
			},
			new AbstractColumn<TableElement>(new Model("Производитель")){
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item<ICellPopulator<TableElement>> cellItem, 
										 String id,
										 IModel<TableElement> model) {
					cellItem.add(new PanelLink(id, model.getObject().getVendorName(),model.getObject().getId()));
				}
			},
			new AbstractColumn<TableElement>(new Model("Модель")){
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item<ICellPopulator<TableElement>> cellItem, 
										 String id,
										 IModel<TableElement> model) {
					cellItem.add(new PanelLink(id, model.getObject().getModelName(),model.getObject().getId()));
				}
			},
			new AbstractColumn<TableElement>(new Model("Время размещения")){
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item<ICellPopulator<TableElement>> cellItem, 
										 String id,
										 IModel<TableElement> model) {
					cellItem.add(new PanelLink(id, model.getObject().getTimeCreate(),model.getObject().getId()));
				}
			},
			new AbstractColumn<TableElement>(new Model("Взятие в работу ")){
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item<ICellPopulator<TableElement>> cellItem, 
										 String id,
										 IModel<TableElement> model) {
					cellItem.add(new PanelLink(id, model.getObject().getTimeGetToProcess(),model.getObject().getId()));
				}
			},
			new AbstractColumn<TableElement>(new Model("Заправленный на складе")){
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item<ICellPopulator<TableElement>> cellItem, 
										 String id,
										 IModel<TableElement> model) {
					cellItem.add(new PanelLink(id, model.getObject().getTimeReturnFromProcess(),model.getObject().getId()));
				}
			},
			new AbstractColumn<TableElement>(new Model("Фамилия")){
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item<ICellPopulator<TableElement>> cellItem, 
										 String id,
										 IModel<TableElement> model) {
					cellItem.add(new PanelLink(id, model.getObject().getCustomerSurname(),model.getObject().getId() ));
				}
			},
			new AbstractColumn<TableElement>(new Model("Имя")){
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item<ICellPopulator<TableElement>> cellItem, 
										 String id,
										 IModel<TableElement> model) {
					cellItem.add(new PanelLink(id, model.getObject().getCustomerName(),model.getObject().getId()));
				}
			}
		};
	}
	
	
	/** получить данные для таблицы */
	private SortableDataProvider<TableElement> getSortableDataProvider(){
		return new SortableDataProvider<TableElement>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Iterator<? extends TableElement> iterator(int first,
															 int count) {
				String sql="select first "+count+" skip "+first+" * from get_cartridge_for_customer where time_return_to_customer is null";
				//System.out.println(sql);
				ConnectWrap connector=((UserApplication)ReturnToUser.this.getApplication()).getConnector();
				ArrayList<TableElement> list=new ArrayList<TableElement>();
				try{
					Connection connection=connector.getConnection();
					ResultSet rs=connection.createStatement().executeQuery(sql);
					while(rs.next()){
						list.add(new TableElement(rs));
					}
				}catch(Exception ex){
					System.out.println("ReturnToUser#getSortableDataProvider Exception: "+ex.getMessage());
				}finally{
					connector.close();
				}
				return list.iterator();
			}

			@Override
			public IModel<TableElement> model(TableElement object) {
				return new Model<TableElement>(object);
			}

			@Override
			public int size() {
				int returnValue=0;
				ConnectWrap connector=((UserApplication)ReturnToUser.this.getApplication()).getConnector();
				try{
					Connection connection=connector.getConnection();
					ResultSet rs=connection.createStatement().executeQuery(" select count(*) from get_cartridge_for_customer where time_return_to_customer is null");
					rs.next();
					returnValue=rs.getInt(1);
				}catch(Exception ex){
					System.out.println("ReturnToUser#getSortableDataProvider Exception: "+ex.getMessage());
				}finally{
					connector.close();
				}
				return returnValue;
			}
		};
		
	}
	
	
	private void onButtonMainMenu(){
		this.setResponsePage(MainMenu.class);
	}

	
	private void onFindForm(){
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Connection connection=connector.getConnection();
			String sql="select * from get_cartridge_for_customer where unique_number="+Integer.parseInt(this.findNumber.getModelObject());
			ResultSet rs=connection.createStatement().executeQuery(sql);
			if(rs.next()){
				ReturnToUserElement page=new ReturnToUserElement(rs.getInt(1));
				this.setResponsePage(page);
			}
		}catch(Exception ex){
			System.out.println("TakeOrder#getSortableDataProvider Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
	}
}

