package database.bind;

/** связывание Java кода с данными из базы данных: ASSORTMENT_TYPE_DESCRIPTION.ASSEMBLY_EDIT_TYPE */
public enum EAssemblyEditType {
	EDIT(0), SELECT(1), LIST(2);
	
	public static EAssemblyEditType getByInt(int value){
		switch(value){
			case 0: return EDIT;
			case 1: return SELECT;
			case 2: return LIST;
			default: return null;
		}
	}
	
	private int value;
	
	private EAssemblyEditType(int value){
		this.value=value;
	}
	
	public int getDatabaseKey(){
		return value;
	}
}
