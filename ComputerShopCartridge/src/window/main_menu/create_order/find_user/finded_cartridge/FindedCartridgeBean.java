package window.main_menu.create_order.find_user.finded_cartridge;

import java.io.Serializable;

public class FindedCartridgeBean implements Serializable{
	private final static long serialVersionUID=1L;
	/** уникальный идентификатор картриджа */
	private Integer cartridgeId;
	/** имя произовдителя */
	private String cartridgeVendor;
	/** имя модели */
	private String cartridgeModel;
	/** является ли данный картридж выделенным */
	private boolean selected;

	/** Данные для строки
	 * @param cartridgeId - уникальный идентификатор картриджа 
	 * @param cartridgeVendor - имя Производителя
	 * @param cartridgeModel - имя Картриджа
	 */
	public FindedCartridgeBean(Integer cartridgeId, 
							   String cartridgeVendor, 
							   String cartridgeModel){
		this.cartridgeId=cartridgeId;
		this.cartridgeVendor=cartridgeVendor;
		this.cartridgeModel=cartridgeModel;
		this.selected=false;
	}

	/** Данные для строки
	 * @param cartridgeId - уникальный идентификатор картриджа 
	 * @param cartridgeVendor - имя Производителя
	 * @param cartridgeModel - имя Картриджа
	 * @param selected - является ли данный картридж выделенным 
	 */
	public FindedCartridgeBean(Integer cartridgeId, 
							   String cartridgeVendor, 
							   String cartridgeModel,
							   boolean selected){
		this.cartridgeId=cartridgeId;
		this.cartridgeVendor=cartridgeVendor;
		this.cartridgeModel=cartridgeModel;
		this.selected=selected;
	}
	
	public Integer getCartridgeId() {
		return cartridgeId;
	}

	public void setCartridgeId(Integer cartridgeId) {
		this.cartridgeId = cartridgeId;
	}

	public String getCartridgeVendor() {
		return cartridgeVendor;
	}

	public void setCartridgeVendor(String cartridgeVendor) {
		this.cartridgeVendor = cartridgeVendor;
	}

	public String getCartridgeModel() {
		return cartridgeModel;
	}

	public void setCartridgeModel(String cartridgeModel) {
		this.cartridgeModel = cartridgeModel;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
