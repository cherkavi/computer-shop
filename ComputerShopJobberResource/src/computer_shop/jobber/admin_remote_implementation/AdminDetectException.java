package computer_shop.jobber.admin_remote_implementation;

/** Исключение, которое выбрасывается при получении неправильного/неопознанного AdminIdentifier-а */
public class AdminDetectException extends Exception{
	private final static long serialVersionUID=1L;
	
	public AdminDetectException(){
		super();
	}
	public AdminDetectException(String message){
		super(message);
	}
}
