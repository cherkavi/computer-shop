package com.cherkashin.vitaliy.computer_shop.client.view.main_menu.rediscount.trade_point;

import java.util.ArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherkashin.vitaliy.computer_shop.client.utility.RootComposite;
import com.cherkashin.vitaliy.computer_shop.client.view.main_menu.rediscount.ChoiceTradePoint;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

/** Переучет  */
public class Rediscount extends LayoutContainer{
	private int pointKod=0;
	private MessageBox waitMessageBox=null;
	private IRediscountAsync manager=GWT.create(IRediscount.class);
	private CRediscount constants=GWT.create(CRediscount.class);
	private TextField<String> textBarCode=new TextField<String>();
	/** имя стиля, который подразумевает отображение ошибки ввода в текстовое поле*/
	private static final String styleTextError="text_error";
	/** имя стиля, который подразумевает валдный вод в текстовое поле ( либо же нейтральное состояние ) */
	private static final String styleTextClear="text_clear";
	/** length of log */
	private static int logSize=10;
	
	private ListStore<RediscountElementModel> store=new ListStore<RediscountElementModel>();
	
	/** Переучет
	 * @param pointKod - код точки, по которой создается переучет
	 */
	public Rediscount(int pointKod){
		this.pointKod=pointKod;
		this.initComponents();
		onButtonShowLast();		
	}
	
	
	/** инициализация компонентов при ошибке обмена с сервером - только кнопка закрыть */
	@SuppressWarnings("unused")
	private void initComponentsError(){
		VBoxLayout layout=new VBoxLayout();
		layout.setVBoxLayoutAlign(VBoxLayoutAlign.CENTER);
		this.setLayout(layout);
		this.setWidth(RootComposite.getWindowWidth());
		this.setHeight(RootComposite.getWindowHeight());
		
		Button buttonClose=new Button(this.constants.buttonClose());
		this.add(buttonClose);
		buttonClose.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				onButtonClose();
			}
		});
		this.add(new Label("temp"));
	}
	
	/** инициализация визуальных компонентов */
	private void initComponents(){
		// fill panel Left
		ContentPanel panelLeft=new ContentPanel();
		VBoxLayout leftLayout=new VBoxLayout();
		leftLayout.setVBoxLayoutAlign(VBoxLayoutAlign.CENTER);
		panelLeft.setWidth(RootComposite.getWindowWidth()/2);
		panelLeft.setHeight(75);
		panelLeft.setLayout(leftLayout);
		panelLeft.setTitle(constants.titleInputPanel());
		
		
		textBarCode.setTitle(constants.barcodeTitle());
		textBarCode.addKeyListener(new KeyListener(){
			@Override
			public void componentKeyUp(ComponentEvent event) {
				if(event.getKeyCode()==13){
					onButtonFixed();
				}
			}
		});
		panelLeft.add(textBarCode);
		
		Button buttonFixed=new Button(constants.barcodeButton());
		buttonFixed.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				onButtonFixed();
			}
		});
		buttonFixed.setWidth(((int)RootComposite.getWindowWidth()/2)+"px");
		panelLeft.add(buttonFixed);
		
		// fill panel right
		ContentPanel panelRight=new ContentPanel();
		panelRight.setHeaderVisible(false);
		VBoxLayout rightLayout=new VBoxLayout();
		rightLayout.setVBoxLayoutAlign(VBoxLayoutAlign.CENTER);
		panelRight.setWidth(RootComposite.getWindowWidth());
		panelRight.setHeight(450);
		panelRight.setLayout(rightLayout);
		
		Button buttonShowLast=new Button(constants.buttonLastValues());
		buttonShowLast.setWidth(((int)RootComposite.getWindowWidth())+"px");
		panelRight.add(buttonShowLast);
		buttonShowLast.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				onButtonShowLast();
			}
		});
		
		// create grid
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column=new ColumnConfig("assortmentName",constants.columnAssortmentName(),340);
		column.setAlignment(HorizontalAlignment.LEFT);
		configs.add(column);

		column=new ColumnConfig("serialNumber",constants.columnSerialNumber(),70);
		column.setAlignment(HorizontalAlignment.LEFT);
		configs.add(column);
		
		column=new ColumnConfig("serialNumberSeller",constants.columnSerialNumberSeller(),160);
		column.setAlignment(HorizontalAlignment.LEFT);
		configs.add(column);

		column=new ColumnConfig("assortmentBarCode",constants.columnAssortmentBarCode(),100);
		column.setAlignment(HorizontalAlignment.LEFT);
		configs.add(column);

		column=new ColumnConfig("assortmentBarCodeCompany",constants.columnAssortmentBarCodeCompany(),100);
		column.setAlignment(HorizontalAlignment.LEFT);
		configs.add(column);
		
		Grid<RediscountElementModel> grid = new Grid<RediscountElementModel>(store, new ColumnModel(configs));  
	    grid.setStyleAttribute("borderTop", "none");  
	    // grid.setAutoExpandColumn("assortmentName");  
	    grid.setBorders(false);  
	    grid.setStripeRows(true);
	    grid.setWidth(RootComposite.getWindowWidth());
	    grid.setHeight(270);
	    panelRight.add(grid);
	    // grid.setColumnLines(true);  
	    // grid.setColumnReordering(true);  
	    // grid.getAriaSupport().setLabelledBy(cp.getHeader().getId() + "-label");  
	    
	    
	    Button buttonClose=new Button(constants.buttonClose());
		buttonClose.setWidth(((int)RootComposite.getWindowWidth())+"px");
		panelRight.add(buttonClose);
		buttonClose.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				onButtonClose();
			}
		});

		VBoxLayout layout=new VBoxLayout();
		layout.setVBoxLayoutAlign(VBoxLayoutAlign.CENTER);
		this.setLayout(layout);
		this.setWidth(RootComposite.getWindowWidth());
		this.setHeight(RootComposite.getWindowHeight());
		this.add(panelLeft);
		this.add(panelRight);
	}
	
	private AsyncCallback<RediscountElement[]> saveBarCode=new AsyncCallback<RediscountElement[]>(){
		@Override
		public void onFailure(Throwable caught) {
			saveBarCodeFailure();
		}
		@Override
		public void onSuccess(RediscountElement[] result) {
			saveBarCodeSuccess(result);
		}
	};
	
	/** отправка кода на сервер не удалась  */
	private void saveBarCodeFailure(){
		this.waitMessageBox.close();
		MessageBox.alert("Error", "Server does not response", null);
	}
	
	private void saveBarCodeSuccess(RediscountElement[] result){
		this.waitMessageBox.close();
		if(result==null){
			MessageBox.alert("", this.constants.checkBarCodeError(), null);
			this.textBarCode.setStyleName(styleTextError);
		}else{
			// место записи в лог последний записанных элементов
			// writeToHistoryLog(this.textBarCode.getValue());
			updateLogModel(result);
			// this.store.removeAll();
			// this.store.add(getListFromArray(result));
			this.textBarCode.setValue(null);
			if(this.textBarCode.getStyleName().equals(styleTextError))
			this.textBarCode.setStyleName(styleTextClear);
		}
	}
	
	/** получить список моделей на основании элементов */
	@SuppressWarnings("unused")
	private ArrayList<RediscountElementModel> getListFromArray(RediscountElement[] elements){
		ArrayList<RediscountElementModel> returnValue=new ArrayList<RediscountElementModel>();
		if((elements!=null)&&(elements.length>0)){
			for(int counter=0;counter<elements.length;counter++){
				returnValue.add(new RediscountElementModel(elements[counter]));
			}
		}
		return returnValue;
	}
	
	/** реакция нажатия на кнопку "зафиксировать товар" */
	private void onButtonFixed(){
		String value=this.textBarCode.getValue();
		if((value==null)||(value.trim().equals(""))){
			// nothing
		}else{
			this.waitMessageBox=MessageBox.wait("", "Server exchange", "wait...");
			// проверить
			manager.saveBarCode(this.pointKod, this.removeFirstZero(value), logSize, saveBarCode);
			// where.append("rupper(SERIAL.NUMBER) like '%"+barCode.toUpperCase()+"%'\n ");
		}
	}
	
	/** убрать впереди стоящий 0, если он есть */
	private String removeFirstZero(String barCode){
		String returnValue=barCode;
		if(returnValue!=null){
			if(returnValue.length()>0){
				if(returnValue.substring(0, 1).equals("0")){
					returnValue=returnValue.substring(1);
				}
			}
		}
		return returnValue;
	}
	
	private void onButtonShowLast(){
		this.waitMessageBox=MessageBox.wait("", "Server exchange", "wait...");
		this.manager.getLastRediscountValue(pointKod, logSize, new AsyncCallback<RediscountElement[]>() {
			@Override
			public void onFailure(Throwable caught) {
				waitMessageBox.close();
				MessageBox.alert("Error", "server exchange Error", null);
			}

			@Override
			public void onSuccess(RediscountElement[] result) {
				waitMessageBox.close();
				if(result!=null){
					updateLogModel(result);
				}else{
					// data does not exists
				}
			}
		});
		System.out.println("ShowLast");
	}

	private void updateLogModel(RediscountElement[] values){
		this.store.removeAll();
		for(int counter=0;counter<values.length;counter++){
			this.store.add(new RediscountElementModel(values[counter]));
		}
	}
	
	/* записать значение в лог  
	private void writeToHistoryLog(String value){
		String insertValue=removeFirstZero(value);
		if(valueInLog(insertValue)==false){
			this.store.set
			modelList.addFirst(insertValue);
			while(modelList.size()>logSize){
				modelList.removeLast();
			}
			updateLogByModel();
		}
	}

	// значение находится в логе ? 
	private boolean valueInLog(String value){
		return this.modelList.indexOf(value)>=0;
	}
	
	// обновить визуальные компоненты согласно модели 
	private void updateLogByModel(){
		for(int counter=0;counter<Rediscount.logSize;counter++){
			try{
				this.logList[counter].setText(modelList.get(counter));
			}catch(Exception ex){
				this.logList[counter].setText("");
			}
		}
	}
	*/
	
	/** нажатие на кнопку "закрыть" */
	private void onButtonClose(){
		RootComposite.showView(new ChoiceTradePoint());
	}
}

class RediscountElementModel implements com.extjs.gxt.ui.client.data.ModelData{
	private RediscountElement element; 
	
	public RediscountElementModel(RediscountElement element){
		this.element=element;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <X> X get(String property) {
		if(property!=null){
			if(property.equals("assortmentName")){
				return (X)this.element.getAssortmentName();
			}else if(property.equals("serialNumber")){
				return (X)this.element.getSerialNumber();
			}else if(property.equals("serialNumberSeller")){
				return (X)this.element.getSerialNumberSeller();
			}else if(property.equals("assortmentBarCode")){
				return (X)this.element.getAssortmentBarCode();
			}else if(property.equals("assortmentBarCodeCompany")){
				return (X)new Float(this.element.getAssortmentBarCodeCompany());
			}else{
				return null;
			}
		}else{
			return null;
		}
	}

	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
	    for(String property : getPropertyNames()){
	        properties.put(property, get(property));
	    }
	    return properties;	
	}

	@Override
	public Collection<String> getPropertyNames() {
		Collection<String> names=new ArrayList<String>();
		names.add("assortmentName");
		names.add("serialNumber");
		names.add("serialNumberSeller");
		names.add("assortmentBarCode");
		names.add("assortmentBarCodeCompany");
		return names;
	}

	@Override
	public <X> X remove(String property) {
		return this.set(property, null);
	}

	@Override
	public <X> X set(String property, X value) {
		try{
			if(property==null)return null;

			if(property.equals("assortmentName")){
				this.element.setAssortmentName((String)value);
			}
			if(property.equals("serialNumber")){
				this.element.setSerialNumber((String)value);
			}
			if(property.equals("serialNumberSeller")){
				this.element.setSerialNumberSeller((String)value);
			}
			if(property.equals("assortmentBarCode")){
				this.element.setAssortmentBarCode((String)value);
			}
			if(property.equals("assortmentBarCodeCompany")){
				this.element.setAssortmentBarCodeCompany((String)value);
			}
			return value;
		}catch(Exception ex){
			return null;
		}
	}

}