package member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	//-----------------------------------
	public MemberDAO() {
		//������ SQL�� ������ �� �ְ� ���ִ� �κ�
		try {
			System.out.println(Class.forName("oracle.jdbc.driver.OracleDriver"));
			String dbURL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
			String dbID = "KTY";
			String dbPassword = "1234";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			System.out.println(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//�α���---------------------------------
	public int login(String userId, String userPassword) {
		//System.out.println(userId);
		//System.out.println(userPassword);//Ȯ�ο�
		String sql = "SELECT userPassword FROM member WHERE userId= ?";
		try {
			pstmt = conn.prepareStatement(sql);//�̸� ����� ���� sql���忡 ?�κ��� ����Ұ���
			pstmt.setString(1, userId);//������ ���� ?�κп� ���Խ�����
			rs = pstmt.executeQuery();//��ü�� ���� ����
			//�񱳱������� �α������� �ƴ��� �����ͺ��̽� �������� �Ǻ���
			if(rs.next()) {//����� �����Ѵٸ� rs.next�� �����
				if(rs.getString(1).equals(userPassword)) {//id�� password��ġ �� ������Ű��
					return 1; // �α��� ����
				} else {
					return 0; //��й�ȣ ����ġ
				}
			}
			return -1; // ���̵� ����

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2;// �����ͺ��̽� ����
		
		//ȸ������------------------------------
		}
	public int join(Member member) {
		
		 String sql = "INSERT INTO MEMBER VALUES(?, ?, ?, ?, ?, ?, ?)";
		 try {
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, member.getUserId());
			 pstmt.setString(2, member.getUserPassword());
			 pstmt.setString(3, member.getUserGender());
			 pstmt.setString(4, member.getUserName());
			 pstmt.setString(5, member.getUserBirth().toString());
			 pstmt.setString(6, member.getUserCell().toString());
			 pstmt.setString(7, member.getUserEmail());
			 return pstmt.executeUpdate();
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
		 return -1;
	}
}
