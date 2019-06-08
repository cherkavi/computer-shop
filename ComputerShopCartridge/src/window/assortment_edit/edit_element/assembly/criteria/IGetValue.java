package window.assortment_edit.edit_element.assembly.criteria;

/** получить текущее значение визуального компонента */
public interface IGetValue <T>{
	/** получить текущее значение описания (из визуального компонента) */
	public T getValue();
	
	/** получить тип описания элемента */
	public int getAssortmentTypeDescriptionKod();
	
	/** получить наименование типа описания  */
	public String getAssortmentTypeDescriptionTitle();
}
