package membership;

import javax.servlet.ServletContext;
/*
DAO(Data Access Object)
: 실제 데이터베이스에 접근하여 여러가지 CRUD작업을 하기위한 객체
*/
import common.JDBConnect;

public class MemberDAO extends JDBConnect {

	//매개변수가 4개인 부모의 생성자를 호출하여 DB에 연결한다.
	public MemberDAO(String drv, String url, String id, String pw) {
		super(drv, url, id, pw);
	}
	
	//application 내장객체만 매개변수로 전달한 후 DB에 연결한다.
	public MemberDAO(ServletContext application) {
		super(application);
	}
	
	/* 
	사용자가 입력한 아이디, 패스워드를 통해 회원테이블을 select한 후
	존재하는 정보인 경우 DTO객체에 그 정보를 담아 반환한다. 
	*/
	public MemberDTO getMemberDTO(String uid, String upass) {
		
		//로그인을 위한 쿼리문을 실행한 후 회원정보를 저장하기 위해
		//생성
		MemberDTO dto = new MemberDTO();
		//로그인 위해 인파라미터가 있는 동적 쿼리문 작성
		String query = "SELECT * FROM member WHERE id=? AND pass=?";
		
		try {
			//쿼리문 실행을 위한 prepared객체 생성 및 인파라미터 설정
			psmt = con.prepareStatement(query);
			psmt.setString(1, uid);
			psmt.setString(2, upass);
			//select 쿼리문을 실행한 후 ResultSet으로 반환받는다.
			rs = psmt.executeQuery();
			
			//반환된 ResultSet객체를 통해 회원정보가 있는지 확인한다.
			if (rs.next()) {
				//정보가 있다면 DTO객체에 회원정보를 저장한다.
				dto.setId(rs.getString("id"));
				dto.setPass(rs.getString("pass"));
				dto.setName(rs.getString(3));
				dto.setRegidate(rs.getString(4));
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		//호출한 지점으로 DTO객체를 반환한다.
		return dto;
	}
	

	
}
