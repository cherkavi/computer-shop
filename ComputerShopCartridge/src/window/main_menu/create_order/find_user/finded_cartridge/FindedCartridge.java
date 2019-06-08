package window.main_menu.create_order.find_user.finded_cartridge;

import org.apache.wicket.ajax.AjaxRequestTarget;

import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/** панель, отображающая найденного пользователя */
public class FindedCartridge extends Panel{
	private final static long serialVersionUID=1L;
	private FindedCartridgeBean bean;
	
	public FindedCartridge(String id, FindedCartridgeBean bean){
		super(id);
		this.bean=bean;
		initComponents();
	}

	private void initComponents(){
		this.add(new Label("label_vendor",this.bean.getCartridgeVendor()));
		this.add(new Label("label_model",this.bean.getCartridgeModel()));
		AjaxCheckBox selected=new AjaxCheckBox("selected",new Model<Boolean>(this.bean.isSelected())) {
			private final static long serialVersionUID=1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				FindedCartridge.this.bean.setSelected(this.getModelObject());
			}
		};
		this.add(selected);
	}
}
