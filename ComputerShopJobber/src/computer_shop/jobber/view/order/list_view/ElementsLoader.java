package computer_shop.jobber.view.order.list_view;

import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.common_objects.RowElement;

public interface ElementsLoader {
	/** получить элементы на основании выделенного имени секции и уникального идентификатора пользователя 
	 * @param sectionName - имя секции по данному пользователю 
	 * @param jobberIdentifier - уникальный идентификатор оптовика  
	 * */
	public RowElement[] getElementsFromSection(String sectionName, JobberIdentifier jobberIdentifier,boolean isCurrency);
}
