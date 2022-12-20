package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;

//DB연결을 위한 클래스 생성
public class JDBConnect {
	//멤버변수 : 연결, 쿼리문 실행 및 전송, 결과셋 반환
	public Connection con;
	public Statement stmt;
	public PreparedStatement psmt;
	public ResultSet rs;
	
	//기본생성자 : 매개변수가 없는 생성자
	public JDBConnect() {
		try {
			//오라클 드라이버 로드
			Class.forName("org.mariadb.jdbc.Driver");
			//커넥션URL, 계정 아이디와 패스워드 기술
			String url = "jdbc:mariadb://localhost:3306/sample_db";
			String id = "sample_user";
			String pwd = "1234";
			//MySQL DB 연결
			con = DriverManager.getConnection(url, id, pwd);
			//연결 성공시 콘솔에서 로그 확인
			System.out.println("DB 연결 성공(기본 생성자)");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//자원 해제를 위한 메서드
	public void close()	{
		try {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (psmt != null) psmt.close();
			if (con != null) con.close();
			System.out.println("JDBC 자원 해제");			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//인수생성자1 : DB연결에 필요한 모든 정보를 매개변수로 받음
	public JDBConnect(String driver, String url, String id, String pwd) {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, id, pwd);
			System.out.println("DB 연결 성공(인수 생성자 1)");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//인수생성자2 : application 내장객체 사용을 위한 매개변수 선언
	public JDBConnect(ServletContext application) {
		/*
		JSP의 내장객체는 메서드에서는 즉시 사용할 수 없고 반드시
		매개변수로 참조값을 받은 후 사용할 수 있다. 
		*/
		try {
			//web.xml에 정의된 컨텍스트 초기화 파라미터를 직접
			//얻어와서 드라이버로드 및 연결을 진행한다. 
		 	String driver = application.getInitParameter("MysqlDriver");
		 	Class.forName(driver);
		 	String url = application.getInitParameter("MysqlURL");
		 	String id = application.getInitParameter("MysqlId");
		 	String pwd = application.getInitParameter("MysqlPwd");
		 	con = DriverManager.getConnection(url, id, pwd);
		 	System.out.println("DB 연결 성공(인수 생성자 2)");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new JDBConnect();
	}
}
