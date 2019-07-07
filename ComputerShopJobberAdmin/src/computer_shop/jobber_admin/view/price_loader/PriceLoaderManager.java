package computer_shop.jobber_admin.view.price_loader;

import computer_shop.jobber.common_objects.AdminIdentifier;

/** ���������, �������:
 * <li> ��������� � �������� �������� �����-����� � ���� ������ </li> 
 * <li>���� ������� ������� �� �������� �����-����� </li>
 * <li>�������� ������ �� ��������� ������� ���������� �������� ������ � ���� - ���� � �����-������ � ����</li>
 * <li>������������� ������ �� ��������� ������� ���������� �������� ������ � ���� - ���� � �����-������ � ����</li>
 * */
public interface PriceLoaderManager {
	/** 
	 * @return ������� �������� �����-����� � ���� ������
	 * <table style="border-style:solid; border-width:1px; border-color:gray">
	 * 	<tr >
	 * 		<th style="border-bottom-style:solid; border-bottom-width:1px; border-bottom-color:black"> �������� �������� </th> <th style="border-bottom-style:solid; border-bottom-width:1px; border-bottom-color:black"> Description </th>
	 * 	</tr>
	 * 	<tr>
	 * 		<td align="center" style="font-weight:bold"> 0</td> <td> ����� � ��������� ������� ����� Excel </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td align="center" style="font-weight:bold"> 1..99</td> <td> ����� � ��������� �������� ������ � ������� ���� ������ </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td align="center" style="font-weight:bold "> 100</td> <td> ������ �������� ��������� - ����� �������� </td>
	 * 	</tr>
	 * </table>
	 * @throws Exception  - ���� ��������� ������ ���������� � �������� 
	 * */
	public int getPercentLoad() throws Exception;
	
	/** ������� ������� ������� �� ������ �������� �������� ������ � ���������� ������� 
	 * @param adminIdentifier - ���������� ������������� �������������� ��� ������� � �������
	 * @return 
	 * <li> true - ������� �������� ������� </li>
	 * <li> false - ������� �������� �� ����� </li>
	 * @throws Exceptin ������ ������ �������, ��� ������ �� ��������
	 * */
	public boolean downloadPriceAndWriteIt(AdminIdentifier adminIdentifier) throws Exception;
	
	
	/** �������� URL � ��������� ���� ��� �������� �����-����� � ���������� �������
	 * @param adminIdentifier - ���������� ������������� ��������������  
	 * @return URL -����� �������� �����-�����
	 * @throws Exceptin ������ ������ �������, ��� ������ �� ��������
	 * */
	public String getPriceLoadUrl(AdminIdentifier adminIdentifier)throws Exception;

	/** ���������� URL � ��������� ���� ��� �������� �����-����� � ���������� �������
	 * @param adminIdentifier - ���������� ������������� ��������������  
	 * @param url - ������ URL, ������� ��������� �� ����������� ���� 
	 * @return 
	 * <li> <b>null</b> - ������ URL ������� ����������� </li>
	 * <li> <b>String</b> - ��������� �������� <i>������ ���������</i> </li>  
	 * @throws Exceptin ������ ������ �������, ��� ������ �� ��������
	 * */
	public String setPriceLoadUrl(AdminIdentifier adminIdentifier, String url)throws Exception;
	
}
