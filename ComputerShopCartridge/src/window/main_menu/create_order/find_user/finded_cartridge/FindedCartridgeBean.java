package window.main_menu.create_order.find_user.finded_cartridge;

import java.io.Serializable;

public class FindedCartridgeBean implements Serializable{
	private final static long serialVersionUID=1L;
	/** ���������� ������������� ��������� */
	private Integer cartridgeId;
	/** ��� ������������� */
	private String cartridgeVendor;
	/** ��� ������ */
	private String cartridgeModel;
	/** �������� �� ������ �������� ���������� */
	private boolean selected;

	/** ������ ��� ������
	 * @param cartridgeId - ���������� ������������� ��������� 
	 * @param cartridgeVendor - ��� �������������
	 * @param cartridgeModel - ��� ���������
	 */
	public FindedCartridgeBean(Integer cartridgeId, 
							   String cartridgeVendor, 
							   String cartridgeModel){
		this.cartridgeId=cartridgeId;
		this.cartridgeVendor=cartridgeVendor;
		this.cartridgeModel=cartridgeModel;
		this.selected=false;
	}

	/** ������ ��� ������
	 * @param cartridgeId - ���������� ������������� ��������� 
	 * @param cartridgeVendor - ��� �������������
	 * @param cartridgeModel - ��� ���������
	 * @param selected - �������� �� ������ �������� ���������� 
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
