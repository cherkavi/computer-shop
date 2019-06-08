package window.catalog;

import java.io.Serializable;


import wicket_extension.UserSession;
import window.BasePage;
import window.catalog.autorization.Autorization;
import window.catalog.contakt.Contakt;
import window.catalog.description.Description;
import window.catalog.reserv.Reserv;
import window.catalog.table.Table;
import window.catalog.thank_order.ThankOrder;
import window.catalog.thank_reserv.ThankReserv;
import window.catalog.tree.Tree;

/** панель-заголовок для базовой страницы */
public class Catalog extends BasePage{
	private final static long serialVersionUID=1L;
	private final static String idTree="tree";
	private final static String idTable="table";
	private Tree tree;
	private Table table;
	
	public Catalog(){
		initComponents();
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		tree=new Tree(idTree,this);
		table=new Table(idTable,this);
		this.add(tree);
		this.add(table);
	}

	
	@Override
	public void action(String actionName, Serializable argument) {
		super.action(actionName, argument);
		while(true){
			if(actionName.equals("TREE")){
				this.table.showClass((Integer)argument);
				if(!this.get(idTable).equals(table)){
					this.remove(idTable);
					this.add(table);
				}
				break;
			};
			if(actionName.equals("COMMODITY")){
				this.remove(idTable);
				this.add(new Description(idTable, this, (Integer)argument));
				break;
			}
			if(actionName.equals("DESCRIPTION.RESERV")){
				if(((UserSession)this.getSession()).getUser()!=null){
					// пользователь уже определен - показать ему страницу с резервом
					Integer assortmentKod=(Integer)argument;
					this.remove(idTable);
					this.add(new Reserv(idTable, this, assortmentKod));
				}else{
					// пользователь еще не определен, показать ему страницу авторизации
					Integer assortmentKod=(Integer)argument;
					this.remove(idTable);
					this.add(new Autorization(idTable,
											  this,
											  "RESERV.AUTORIZATION.OK",
											  "RESERV.AUTORIZATION.CANCEL",
											  assortmentKod));
				}
				break;
			}
			
			/** запрос  на резервирование товара, авторизация завершена успешно, путь запроса: Reserv->Autorization*/
			if(actionName.equals("RESERV.AUTORIZATION.OK")){
				this.remove(idTable);
				this.add(new Reserv(idTable, this, (Integer)argument));
				break;
			}

			if(actionName.equals("RESERV.OK")){
				this.remove(idTable);
				this.add(new ThankReserv(idTable, this));
				break;
			}
			if(actionName.equals("THANK_RESERV.OK")){
				this.remove(idTable);
				this.add(table);
				break;
			}
			
			if(actionName.equals("RESERV.CANCEL")){
				this.remove(idTable);
				this.add(table);
				break;
			}

			/** запрос на авторизацию завершен успешно, путь запроса: Reserv->Autorization*/
			if(actionName.equals("RESERV.AUTORIZATION.CANCEL")){
				this.remove(idTable);
				this.add(table);
				break;
			}
			
			// на панели Description была нажата кнопка Order
			if(actionName.equals("DESCRIPTION.ORDER")){
				Integer assortmentKod=(Integer)argument;
				this.remove(idTable);
				this.add(new Contakt(idTable,this,assortmentKod));
				break;
			}

			if(actionName.equals("CONTAKT.OK")){
				this.remove(idTable);
				this.add(new ThankOrder(idTable,this));
				break;
			}
			
			if(actionName.equals("THANK_ORDER.OK")){
				this.remove(idTable);
				this.add(table);
				break;
			}
			
			if(actionName.equals("CONTAKT.CANCEL")){
				this.remove(idTable);
				this.add(table);
				break;
			}
			
			// на панели Description была нажата кнопка Cancel
			if(actionName.equals("DESCRIPTION.CANCEL")){
				this.remove(idTable);
				this.add(table);
				break;
			}
			
			/*
			// авторизация по умолчанию прошла успешно
			if(actionName.equals("AUTORIZATION.OK")){
				break;
			}
			// отмена авторизации 
			if(actionName.equals("AUTORIZATION.CANCEL")){
				break;
			}*/
			
			break;
		}
	}
}
