package wicket_extension.gui.modal_window;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

import wicket_extension.action.IAjaxActionExecutor;

/** ��������� ���� ������������� */
/**
 * @author Technik
 *
 */
public class ModalWindowConfirm extends ModalWindow{
	private final static long serialVersionUID=1L;
	
	private IAjaxActionExecutor actionExecutor;
	
	/** ���������� �������������  */
	public ModalWindowConfirm(String id){
		super(id);
	};
	
	@Override
	public void close(AjaxRequestTarget target) {
		super.close(target);
		onClose(target);
	}
	
	/** ������� �� �������� ���� */
	private void onClose(AjaxRequestTarget target){
		this.actionExecutor.action(target, IAjaxActionExecutor.actionModalCancel, null);		
	}
	
	/**
	 * @param actionExecutor - ����������� ������� ������� �� ������ 
	 * @param title - ���������
	 * @param message - ��������� ��� ����������� 
	 * @param width - ������ 
	 * @param height - ������ 
	 */
	public void init( 	
							  IAjaxActionExecutor actionExecutor,
							  String title,
							  String message,
							  int width,
							  int height){
		this.setTitle(title);
		this.setHeightUnit("px");
		this.setWidthUnit("px");
		this.setInitialWidth(width);
		this.setInitialHeight(height);
		this.actionExecutor=actionExecutor;
		initComponent(this.getContentId(),
					  message,
					  null,
					  null,
					  null,
					  null,
					  null );
	}
	
	private void initComponent(String id, 
							   String message,
							   String messageCssStyle,
							   String buttonOkTitle,
							   String buttonOkCssStyle,
							   String buttonCancelTitle,
							   String buttonCancelCssStyle){
		
		this.setContent(new ModalWindowConfirmPanel(id,message, messageCssStyle, buttonOkTitle, buttonOkCssStyle, buttonCancelTitle, buttonCancelCssStyle){
			private final static long serialVersionUID=1L;
			@Override
			public void onButtonCancelClick(AjaxRequestTarget target) {
				onButtonCancel(target);
			}
			@Override
			public void onButtonOkClick(AjaxRequestTarget target) {
				onButtonOk(target);
			}
		});
	}
	
	private void onButtonCancel(AjaxRequestTarget target){
		this.actionExecutor.action(target, IAjaxActionExecutor.actionModalCancel, null);
		this.close(target);
	}
	private void onButtonOk(AjaxRequestTarget target){
		this.actionExecutor.action(target, IAjaxActionExecutor.actionModalOk, null);
		this.close(target);
	}
}
