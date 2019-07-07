package computer_shop.jobber.common_objects;

import java.util.Date;

/** ������, ������� �������� ����������� ������ ��� ����������� ����� ������ � ������� ������� ��� �������������� */
public class AdminTableOrdersRow {
	/** ���������� ��� ��� ������� ������ � ���� ������ - ���������� ����� ������ */
	private int kodOrder;
	/** ��� �������� */
	private String jobberName;
	/** ������� �������� */
	private String jobberSurname;
	/** ����� ������/��������� ������� ������ � ���� ������ */
	private Date timeWrite;
	/** 
	 * <li> true - ������ ����� � USD  </li>
	 * <li> false - ������ ����� � ��� </li>
	 * */
	private boolean isCurrency;
	/** ���-�� ��������� � ������ */
	private int quantity;
	/** ����� ������ */
	private float amount;
	/** ������ ������:
	 * <li> <b>0</b> - �����</li>
	 * <li> <b>1</b> - ������������ ����������</li>
	 * <li> <b>2</b> - ���������� �� ����������</li>
	 * <li> <b>3</b> - ������������ ��������</li>
	 * */
	private int status;
	
	/** ������, ������� �������� ����������� ������ ��� ����������� ����� ������ � ������� ������� ��� �������������� 
	 * <br>
	 * <b> ������ ����������� ��������� ��� XFire</b>
	 * */
	public AdminTableOrdersRow(){
	}


	public int getKodOrder() {
		return kodOrder;
	}

	public void setKodOrder(int kodOrder) {
		this.kodOrder = kodOrder;
	}

	public String getJobberName() {
		return jobberName;
	}

	public void setJobberName(String jobberName) {
		this.jobberName = jobberName;
	}

	public String getJobberSurname() {
		return jobberSurname;
	}

	public void setJobberSurname(String jobberSurname) {
		this.jobberSurname = jobberSurname;
	}

	public Date getTimeWrite() {
		return timeWrite;
	}

	public void setTimeWrite(Date timeWrite) {
		this.timeWrite = timeWrite;
	}

	public boolean isCurrency() {
		return isCurrency;
	}

	public void setCurrency(boolean isCurrency) {
		this.isCurrency = isCurrency;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
