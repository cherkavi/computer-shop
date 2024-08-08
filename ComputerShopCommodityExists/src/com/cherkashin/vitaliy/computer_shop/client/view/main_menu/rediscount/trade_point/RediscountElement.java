package com.cherkashin.vitaliy.computer_shop.client.view.main_menu.rediscount.trade_point;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RediscountElement implements IsSerializable{
	private String assortmentName;
	private String serialNumber;
	private String serialNumberSeller;
	private String assortmentBarCode;
	private String assortmentBarCodeCompany;
	
	public RediscountElement(){
	}
	
	/** 
	 * @param assortmentName - NAME (ASSORTMEMT)
	 * @param serialNumber - NUMBER (SERIAL)
	 * @param serialNumberSeller - NUMBER_SELLER (SERIAL)
	 * @param assortmentBarCode - BAR_CODE (SERIAL)
	 * @param assortmentBarCodeCompany BAR_CODE_COMPANY (ASSORTMEMT)
	 */
	public RediscountElement(String assortmentName, String serialNumber, String serialNumberSeller, String assortmentBarCode, String assortmentBarCodeCompany){
		this.assortmentName=assortmentName;
		this.serialNumber=serialNumber;
		this.serialNumberSeller=serialNumberSeller;
		this.assortmentBarCode=assortmentBarCode;
		this.assortmentBarCodeCompany=assortmentBarCodeCompany;
	}

	/** получить ASSORTMENT.NAME */
	public String getAssortmentName() {
		return assortmentName;
	}

	/** получить SERIAL.NUMBER */
	public String getSerialNumber() {
		return serialNumber;
	}

	/** получить SERIAL.NUMBER_SELLER */
	public String getSerialNumberSeller() {
		return serialNumberSeller;
	}

	/** получить ASSORTMENT.BAR_CODE */
	public String getAssortmentBarCode() {
		return assortmentBarCode;
	}

	/** получить ASSORTMENT.BAR_CODE_COMPANY */
	public String getAssortmentBarCodeCompany() {
		return assortmentBarCodeCompany;
	}

	public void setAssortmentName(String assortmentName) {
		this.assortmentName = assortmentName;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setSerialNumberSeller(String serialNumberSeller) {
		this.serialNumberSeller = serialNumberSeller;
	}

	public void setAssortmentBarCode(String assortmentBarCode) {
		this.assortmentBarCode = assortmentBarCode;
	}

	public void setAssortmentBarCodeCompany(String assortmentBarCodeCompany) {
		this.assortmentBarCodeCompany = assortmentBarCodeCompany;
	}
	
}

