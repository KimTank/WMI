package Pack01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
	private Connection conn;
	private ResultSet rs;
	//-----------------------------------
	public BbsDAO() {
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
//�Խ��� �ۼ��� ���ؼ��� 3���� �Լ��� �ʿ��ϴ�
	//�ۼ��ð��� ��Ÿ���ִ� �Լ�(mysql�� oracle sqlLite�� ����� ��ɹ��� Ʋ���Ƿ� ���ǹٶ�)
	public String getDate() {
		String sql = "SELECT SYSDATE from dual";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ""; //�����ͺ��̽�����
	}
	
	//�Խù� ��ȣ�� ��Ÿ���� �� �ִ� �Լ�//sql Lite���� �������� ����ؼ� ���������� �����ϰ� �ο��Ҽ�������
	public int getNext() {
		String sql = "SELECT bbsid FROM bbs ORDER BY bbsId DESC";
		try {

			PreparedStatement pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1;//ù��° �Խù��ΰ��
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; //�����ͺ��̽�����
	}
	
	//�۾��⸦ �����ͺ��̽��� �����ϴ� �Լ�//�ռ� ������ �Լ����� ���Խ����ش�//�̶� ������ ���� ������� ���� �ʴ� ������
	public int write(String bbsTitle, String userId, String bbsContent) {
		String sql = "INSERT INTO bbs VALUES(?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			System.out.println(getNext());
			System.out.println(bbsTitle);
			System.out.println(userId);
			System.out.println(getDate());
			System.out.println(bbsContent);
			System.out.println(1);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userId);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; //�����ͺ��̽�����
	}
	
//�Խñ� ����� �� �� �ְ� ���ִ� �Լ��� ����
	int bbsPage = 10;
	public ArrayList<Bbs> getList(int pageNumber) {
		//bbsAvailabe(�Խñ� ������ ��������)�� limit(�۸��) 10���� �������
		String sql = "SELECT * FROM bbs WHERE (bbsId < ?) AND (bbsAvailable = 1) AND (bbsId >= ?) ORDER BY bbsId DESC";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			//�Խñ��� �������� ���԰��� ?���� �۰�

			pstmt.setInt(1, getNext()-(pageNumber-1)*bbsPage);
			pstmt.setInt(2, getNext()-(pageNumber-1)*bbsPage-bbsPage);
			rs=pstmt.executeQuery();

			while(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsId(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserId(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	//���࿡ �������� 10���ۿ� ������ ���� ��ư�� �۵����� �ʾƾ��ϹǷ� �׿� �����ϴ� �Լ��� ������ش�
	public boolean nextPage(int pageNumber) {
		String sql = "SELECT * FROM bbs WHERE bbsId < ? AND bbsAvailable = 1";
		try {
			System.out.println("��"+pageNumber);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			//�Խñ��� �������� ���԰��� ?���� �۰�
			pstmt.setInt(1, getNext()-(pageNumber-1)*bbsPage);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
//�Խñ� ���� ��� ����----------------------------------------------
	public Bbs getBbs(int bbsId) {
		String sql = "SELECT * FROM bbs WHERE bbsId = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbsId);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsId(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserId(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
//�Խñ� ����-----------------------------------------------------------
	public int update(int bbsId, String bbsTitle, String bbsContent) {
		String sql = "UPDATE bbs SET bbsTitle=?, bbsContent=? WHERE bbsId=?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsId);
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; //�����ͺ��̽�����
	}
//�Խñ� ����-----------------------------------------------------------
	public int delete(int bbsId) {
		String sql = "UPDATE bbs SET bbsAvailable=0 WHERE bbsId=?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbsId);
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; //�����ͺ��̽�����
	}
}
