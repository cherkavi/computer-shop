package computer_shop.jobber_admin.view.common;

import javax.swing.JInternalFrame;

/** ���������, ������� �������� ���������� �� ������������ �� �������� ��������� ���� */
public interface IParent {
	/** �������� ���� ��������� ������������ � ������������� ��������� ������ ������� ��������� */
	public void selfSetVisible(boolean isVisible);
	/** �������� ���� ��������� ������������ � ����� ��������, � �������� � ��������� ���� ���� */
	public void windowWasClosed(JInternalFrame source);
}
