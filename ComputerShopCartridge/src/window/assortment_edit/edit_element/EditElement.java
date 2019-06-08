package window.assortment_edit.edit_element;

import java.io.FileOutputStream;

import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.wicket.Resource;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.resource.IResourceStream;

import database.ConnectWrap;

// import wicket_extension.FileResourceStream;
import org.apache.wicket.util.resource.FileResourceStream;
import wicket_extension.action.IAjaxActionExecutor;
import wicket_extension.gui.modal_window.ModalWindowConfirm;
import window.Application;
import window.assortment_edit.BaseAssortmentEdit;
import window.assortment_edit.choice_element.ChoiceElement;
import window.assortment_edit.edit_element.assembly.AssemblyPanel;
import window.main_menu.utility.point_choice.ChoicePoint;

/** редактирование элемента подгрузка фото, ввод критериев  */
public class EditElement extends BaseAssortmentEdit implements IAjaxActionExecutor{
	/** элемент, который в данный момент редактируется */
	private AssortmentElement element;
	/** обертка для отображения состояния элемента  */
	private WebMarkupContainer assemblyStateWrap;
	/** модель, которая отображает состояние элемента */
	private Model<String> assemblyStateElement=new Model<String>();
	/** модель надписи на кнопке "удалить из сборки" */
	private Model<String> modelRemoveFromAssembly=new Model<String>();
	/** кнопка удаления из сборки */
	private Button buttonRemoveFromAssembly;
	/** модальное окно для подтверждения удаления/добавления данных  */
	private ModalWindowConfirm modalWindow;
	/** меню поиска */
	private Button buttonFindMenu;
	/** код ассортимента, по которому происходит редактирование */
	private int assortmentKod;
	
	/** редактирование элемента 
	 * @param assortmentId - уникальный идентификатор Assortment.KOD
	 * */
	public EditElement(int assortmentId) throws Exception{
		this.assortmentKod=assortmentId;
		this.element=this.getElement(assortmentId);
		initComponents();
	}
	
	private void initComponents(){
		// инициализация элементов 
		this.add(new Label("caption_class",element.getClassName()));
		this.add(new Label("caption_name",element.getName()));
		this.add(new Label("caption_note",element.getNote()));
		this.add(new Label("caption_price",Float.toString(element.getPrice())));
		if(element.getPhotoFile()==null){
			Image image=new Image("photo");
			image.add(new SimpleAttributeModifier("src","images/no_image.png"));
			this.add(image);
		}else{
			this.updateImage();
		}
		
		Form<?> uploadForm=new Form<Object>("upload_form");
		this.add(uploadForm);
		
		final FileUploadField uploadFile=new FileUploadField("upload_file");
		uploadForm.add(uploadFile);
		// загрузить фотографию
		Button uploadButton=new Button("upload_button"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				saveFileByCurrentAssortment(uploadFile.getFileUpload());
				updateAssortment(assortmentKod, 1);
				EditElement.this.updateImage();
			}
		};
		uploadButton.add(new SimpleAttributeModifier("value",this.getString("label_button_upload")));
		uploadForm.add(uploadButton);
		
		// меню поиска ассортимента
		Form<?> formBack=new Form<Object>("form_back");
		this.add(formBack);
		buttonFindMenu=new Button("button_back"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonBack();
			};
		};
		buttonFindMenu.add(new SimpleAttributeModifier("value",this.getString("label_button_back")));
		buttonFindMenu.setOutputMarkupId(true);
		formBack.add(buttonFindMenu);
		
		// меню выхода из режима редактирования
		Form<?> formExit=new Form<Object>("form_exit");
		this.add(formExit);
		Button buttonExit=new Button("button_exit"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonExit();
			};
		};
		buttonExit.add(new SimpleAttributeModifier("value",this.getString("label_button_exit")));
		buttonExit.setOutputMarkupId(true);
		formExit.add(buttonExit);
		
		assemblyStateWrap=new WebMarkupContainer("assembly_state_wrap");
		this.add(assemblyStateWrap);
		assemblyStateWrap.setOutputMarkupId(true);
		
		Label assemblyState=new Label("assembly_state", 
									  assemblyStateElement);
		assemblyStateWrap.add(assemblyState);
		// кнопка удаления данной позиции из ассортимента 
		buttonRemoveFromAssembly=new Button("remove_from_assembly");
		buttonRemoveFromAssembly.add(new AjaxEventBehavior("onclick"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				showConfirmWindow(target);
			}
		});
		Form<?> formRemove=new Form<Object>("form_remove");
		formRemove.add(buttonRemoveFromAssembly);
		
		assemblyStateWrap.add(formRemove);
		
			// задать для кнопки удаления из ассортимента текст на кнопке
		if(element.getNotAssembly()==null){
			assemblyStateElement.setObject(this.getString("position_state_in_assembly"));
			
			buttonRemoveFromAssembly.add(new SimpleAttributeModifier("value", 
																	 this.getString("remove_from_assembly")));
		}else{
			assemblyStateElement.setObject(this.getString("position_state_out_assembly"));
			
			buttonRemoveFromAssembly.add(new SimpleAttributeModifier("value", 
																	 this.getString("add_to_assembly")));
		}
		// showConfirmWindow(target);
		modalWindow=new ModalWindowConfirm("modal_window");
		this.add(modalWindow);
		
		// заполнить поле создания ссылки на ассортимент
		this.assemblyPanel=new AssemblyPanel("assembly_panel", 
											 this.element.getId(),
											 buttonFindMenu.getMarkupId(),
											 buttonExit.getMarkupId());
		this.add(this.assemblyPanel);
	}
	
	/** обновить ассортимент, установив флаг обновления данного ассортимента:
	 * @param connection - соединение с базой
	 * @param assortmentKod - код ассортимента 
	 * @param flag - флаг, который устанавливается для данной единицы 
	 *  */
	private boolean updateAssortment(int assortmentKod, int flag){
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		Connection connection=connector.getConnection();
		boolean returnValue=false;
		try{
			connection.createStatement().executeUpdate("update assortment set update_in_server="+flag+" where kod="+assortmentKod);
			connection.commit();
			returnValue=true;
		}catch(Exception ex){
			System.err.println("AssemlbyPanel#updateAssortment Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	
	private AssemblyPanel assemblyPanel;
	
	/** отобразить окно подтверждения  */
	private void showConfirmWindow(AjaxRequestTarget target){
		// target.appendJavascript("alert('ok');");
		modalWindow.init(
						 this,
						 this.getString("remove_from_assembly_title"),
						 this.getString("remove_from_assembly_message"),
						 300,
						 85
						);
		modalWindow.show(target);
	}
	

	private void onButtonBack(){
		this.setResponsePage(new ChoiceElement(null, null));
	}
	
	private void onButtonExit(){
		this.setResponsePage(new ChoicePoint());
	}
	
	private void updateImage(){
		if(this.get("photo")!=null){
			this.remove("photo");
		}
		this.add(new Image("photo",new Resource(){
			private final static long serialVersionUID=1L;
			@Override
			public IResourceStream getResourceStream() {
				try{
					return new FileResourceStream(new File(getImagePath()+EditElement.this.element.getPhotoFile()));
					// return new FileResourceStream(getImagePath()+EditElement.this.element.getPhotoFile());
				}catch(Exception ex){
					return null;
				}
			}
		}));
	}
	
	private void saveFileByCurrentAssortment(FileUpload file){
		ConnectWrap connector=this.getConnector();
		try{
			// сохранить файл
			String fileName="cs_"+file.getClientFileName();
			String pathToFile=this.getImagePath()+fileName;
			OutputStream out=new FileOutputStream(pathToFile);
			out.write(file.getBytes());
			out.close();
			int idPhoto=this.getPhotoId(connector,fileName);
			this.updateAssortmentSetPhoto(connector, this.element.getId(), idPhoto);
			connector.getConnection().commit();
			this.element=getElement(this.element.getId());
		}catch(Exception ex){
			System.err.println("Ошибка сохранения файла ");
		}finally{
			connector.close();
		}
		
		System.out.println(""+file.getClientFileName());
	}
	
	/** записать имя файла в таблицу Photo и получить номер записи */
	private int getPhotoId(ConnectWrap connector,String fileName) throws SQLException{
		ResultSet rs=connector.getConnection().createStatement().executeQuery("select GEN_ID(GEN_PHOTO_ID,1) from rdb$database");
		if(rs.next()){
			int idPhoto=rs.getInt(1);
			PreparedStatement ps=connector.getConnection().prepareStatement("insert into photo(id,filename) values(?,?)");
			ps.setInt(1, idPhoto);
			ps.setString(2, fileName);
			ps.executeUpdate();
			return idPhoto;
		}else{
			return 0;
		}
	}
	
	private void updateAssortmentSetPhoto(ConnectWrap connector, int assortmentId, int photoId) throws SQLException{
		connector.getConnection().createStatement().executeUpdate(" update assortment set kod_photo="+photoId+"  where kod="+assortmentId);
	}
	
	String query="\n select assortment.*, class.name class_name, price.price price, photo.filename photo_filename from assortment"+
				 "\n inner join class on class.kod=assortment.class_kod"+
				 "\n inner join price on price.kod=assortment.price_kod"+
				 "\n left join photo on photo.id=assortment.kod_photo"+
				 "\n where assortment.kod=?";
	
	/** получить элемент, который будет редактироваться */
	private AssortmentElement getElement(int assortmentKod) throws Exception{
		AssortmentElement returnValue=null;
		ConnectWrap connector=this.getConnector();
		try{
			PreparedStatement ps=connector.getConnection().prepareStatement(query);
			ps.setInt(1, assortmentKod);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				Integer notAssembly=rs.getInt("NOT_ASSEMBLY");
				if(notAssembly==0){
					if(rs.wasNull()){
						notAssembly=null;
					}
				}
				
				returnValue=new AssortmentElement(
						 rs.getInt("KOD"),
						 rs.getString("NAME"),
						 rs.getString("NOTE"),
						 rs.getInt("CLASS_KOD"),
						 rs.getString("CLASS_NAME"),
						 rs.getFloat("PRICE"),
						 rs.getString("PHOTO_FILENAME"),
						 notAssembly
						);
			}else{
				throw new Exception("Assortment does not found: "+assortmentKod);
			}
				
		}finally{
			connector.close();
		}
		return returnValue;
	}

	/** получить полный путь к изображению  */
	private String getImagePath(){
		return (String)((Application)this.getApplication()).getPropertis("path_to_local_image");
	}
	
	private ConnectWrap getConnector(){
		return ((Application)this.getApplication()).getConnectorToServer();
	}

	@Override
	public int action(AjaxRequestTarget target, 
					  String actionName,
					  Object argument) {
		if(actionName.equals(IAjaxActionExecutor.actionModalOk)){
			// target.appendJavascript("document.getElementById('"+buttonFindMenu.getMarkupId()+"').disabled=false");
			//System.out.println("button>>> "+this.get("assembly_state_wrap/remove_from_assembly").getMarkupId());
			// target.appendJavascript("document.getElementById('"+this.get("assembly_state_wrap.remove_from_assembly").getMarkupId()+"').enable=false");
			// target.appendJavascript("document.getElementById('"+buttonRemoveFromAssembly.getMarkupId()+"').disabled=true");
			target.addComponent(this.assemblyStateWrap);
			System.out.println("Удалить из/в сборку : ");
			// FIXME - добавить возможность удаления/добавления  из/в сборку 
			return IAjaxActionExecutor.RETURN_OK;
		}else if(actionName.equals(IAjaxActionExecutor.actionModalCancel)){
			return IAjaxActionExecutor.RETURN_OK;
		}else{
			return IAjaxActionExecutor.RETURN_ERROR;
		}
	}
}

/** элемент, который редактируется */
class AssortmentElement implements Serializable{
	private final static long serialVersionUID=1L;
	
	private int id;
	private String name;
	private String note;
	private int classKod;
	private String className;
	private float price;
	private String photoFile;
	private Integer notAssembly;
	
	/**
	 * @param kod - FK в базе
	 * @param name - наименование
	 * @param note - примечание 
	 * @param classKod - код класса 
	 * @param className - наименование класса
	 * @param price - цена в USD
	 * @param photoFile - имя прикрепленного файла ( как изображения )
	 * @param notAssembly - 
	 * 	<ul>
	 * 		<li> <b>null</b> - входит в сборку и в ассортимент для сайта</li>
	 * 		<li> <b> not null </b> - не входит в сборку и в ассортимент для сайта </li>
	 * 	</ul>
	 */
	public AssortmentElement(int kod,
							 String name,
							 String note,
							 int classKod,
							 String className,
							 float price,
							 String photoFile,
							 Integer notAssembly){
		this.id=kod;
		this.name=name;
		this.note=note;
		this.classKod=classKod;
		this.className=className;
		this.price=price;
		this.photoFile=photoFile;
		this.notAssembly=notAssembly;
	}

	/** ASSORTMENT.ID */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getClassKod() {
		return classKod;
	}

	public void setClassKod(int classKod) {
		this.classKod = classKod;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile(String photoFile) {
		this.photoFile = photoFile;
	}

	/**
	 * 	<ul>
	 * 		<li> <b>null</b> - входит в сборку и в ассортимент для сайта</li>
	 * 		<li> <b> not null </b> - не входит в сборку и в ассортимент для сайта </li>
	 * 	</ul>
	 */
	public Integer getNotAssembly(){
		return this.notAssembly;
	}

	/**
	 * @param notAssembly
	 * 	<ul>
	 * 		<li> <b>null</b> - входит в сборку и в ассортимент для сайта</li>
	 * 		<li> <b> not null </b> - не входит в сборку и в ассортимент для сайта </li>
	 * 	</ul>
	 */
	public void setNotAssembly(Integer notAssembly){
		this.notAssembly=notAssembly;
	}
	
}
