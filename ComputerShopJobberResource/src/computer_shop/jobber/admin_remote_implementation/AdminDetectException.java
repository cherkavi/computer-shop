package computer_shop.jobber.admin_remote_implementation;

/** ����������, ������� ������������� ��� ��������� �������������/������������� AdminIdentifier-� */
public class AdminDetectException extends Exception{
	private final static long serialVersionUID=1L;
	
	public AdminDetectException(){
		super();
	}
	public AdminDetectException(String message){
		super(message);
	}
}
