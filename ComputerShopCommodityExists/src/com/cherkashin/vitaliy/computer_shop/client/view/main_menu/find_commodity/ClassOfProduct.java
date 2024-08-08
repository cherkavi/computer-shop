package com.cherkashin.vitaliy.computer_shop.client.view.main_menu.find_commodity;

import com.google.gwt.user.client.rpc.IsSerializable;


/** Элемент для отображения одного класса продукта из списка  */
public class ClassOfProduct implements IsSerializable{
	private int kod;
	private String name;
	
	public ClassOfProduct(){
	}
	
	public ClassOfProduct(int kod, String name){
		this.kod=kod;
		this.name=name;
	}
	
	public int getKod() {
		return kod;
	}
	public void setKod(int kod) {
		this.kod = kod;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + kod;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassOfProduct other = (ClassOfProduct) obj;
		if (kod != other.kod)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
