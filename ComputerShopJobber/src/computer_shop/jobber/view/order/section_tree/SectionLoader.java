package computer_shop.jobber.view.order.section_tree;

import javax.swing.tree.TreePath;
import computer_shop.jobber.common_objects.StringArrayElement;

/** класс, который отвечает за удаленную загрузку данных из сети */
public interface SectionLoader {
	/** получить один корневой элемент, который будет содержать все дерево в полном объеме */
	public StringArrayElement getTreeRootNode();
	/** получить  элементы корневого элемента 
	 * @param вернуть массив из String, которые содержатся в самой верхней иерархии 
	 * */
	public String[] getSubRootElements();
	
	/** 
	 * получить подэлементы на основании указанного элемента
	 * @param path - полный путь к элементу, из которого нужно получить подэлементы
	 * @return 
	 * <li> null - если подэлементов нет </li>
	 * <li> String[] - если подэлементы есть </li>
	 * */
	public String[] getSubElements(TreePath path);

	/** 
	 * получить подэлементы на основании указанного элемента
	 * @param path - полный путь к элементу, из которого нужно получить подэлементы
	 * @return 
	 * <li> null - если подэлементов нет </li>
	 * <li> String[] - если подэлементы есть </li>
	 * */
	public String[] getSubElements(String lastElement);
	
	
	/** @return true если данный элемент имеет детей 
	 * @param path полный путь к элементу 
	 * */
	public boolean elementHasChild(TreePath path);

	/** @return true если данный элемент имеет детей 
	 * @param path полный путь к элементу 
	 * */
	public abstract boolean elementHasChild(String lastElement);
	
	
}
