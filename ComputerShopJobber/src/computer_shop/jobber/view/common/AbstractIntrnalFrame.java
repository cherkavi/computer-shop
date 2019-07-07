package computer_shop.jobber.view.common;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public abstract class AbstractIntrnalFrame extends JInternalFrame implements InternalFrameListener, IParent{
	private static final long serialVersionUID=1L;
	protected IParent parent;
	protected JDesktopPane desktop;
	protected int width;
	protected int height;
	
	public AbstractIntrnalFrame(String title,
								boolean resizable, 
								boolean closable, 
								boolean maximazable,
								IParent parent,
								JDesktopPane desktop,
								int width,
								int height){
		super(title, resizable, closable, maximazable);
		this.addInternalFrameListener(this);
		this.parent=parent;
		this.desktop=desktop;
		this.width=width;
		this.height=height;
		this.setToCenter();
		this.desktop.add(this);
		this.parent.selfSetVisible(false);
		this.setVisible(true);
	}

	private void setToCenter(){
		this.setBounds((this.desktop.getWidth()-this.width)/2,
					   (this.desktop.getHeight()-this.height)/2,	
					    this.width,
					    this.height);
		
	}
	
	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		this.setVisible(false);
		this.desktop.remove(this);
		if(this.parent!=null){
			this.parent.windowWasClosed(this);
		}else{
		}
		
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
	}

	@Override
	public abstract void windowWasClosed(JInternalFrame source);
	
	@Override
	public void selfSetVisible(boolean isVisible){
		this.setVisible(isVisible);
	};
}
