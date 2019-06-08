package window.main_menu.create_order.find_user.finded_customer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import wicket_extension.action.IActionExecutor;

/** панель, отображающая найденного пользователя */
public class FindedCustomer extends Panel{
	private final static long serialVersionUID=1L;
	private Integer customerId;
	private IActionExecutor executor;
	
	public FindedCustomer(String id, FindedCustomerBean bean, IActionExecutor executor){
		super(id);
		this.executor=executor;
		this.customerId=bean.getCustomerId();
		AjaxLink<Object> link=new AjaxLink<Object>("link"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				onLink(target);
			}
		};
		this.add(link);
		String caption= ((bean.getCustomerSurname()==null)?"":bean.getCustomerSurname())+" "+
						((bean.getCustomerName()==null)?"":bean.getCustomerName());
		link.add(new Label("label_name",caption));
	}

	private void onLink(AjaxRequestTarget target){
		this.executor.action("CUSTOMER", new Object[]{target,customerId});
	}
}
