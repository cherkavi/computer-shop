package computer_shop.jobber.common_objects;

import java.io.Serializable;

public class StringArrayElement implements Serializable{
	private final static long serialVersionUID=1L;
	
	private String name;
	private StringArrayElement[] elements;
	
	public StringArrayElement(){
		this.name=null;
		this.elements=null;
	}
	
	public StringArrayElement(String name){
		this.name=name;
		this.elements=null;
	}

	public StringArrayElement(String name, StringArrayElement[] elements){
		this.name=name;
		this.elements=elements;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the elements
	 */
	public StringArrayElement[] getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(StringArrayElement[] elements) {
		this.elements = elements;
	}

}
