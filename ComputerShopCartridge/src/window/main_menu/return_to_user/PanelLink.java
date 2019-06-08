package window.main_menu.return_to_user;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import window.main_menu.return_to_user.marker.ReturnToUserElement;

public class PanelLink extends Panel{
	private final static long serialVersionUID=1L;
	private final int rowId;
	private static final SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	public PanelLink(String id, String caption, int rowId){
		super(id);
		this.rowId=rowId;
		initComponents(caption);
	}

	public PanelLink(String id, Date date, int rowId){
		super(id);
		this.rowId=rowId;
		String caption=null;
		if(date!=null){
			caption=sdf.format(date);
		}else{
			caption="";
		}
		initComponents(caption);
	}
	
	/** create components */
	private void initComponents(String caption){
		Link<String> link=new Link<String>("link"){
			private static final long serialVersionUID=1L; 
			@Override
			public void onClick() {
				ReturnToUserElement page=new ReturnToUserElement(PanelLink.this.rowId);
				this.setResponsePage(page);
			}
		};
		link.add(new Label("message",caption));
		this.add(link);
	}
}
