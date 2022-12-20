package model1.board;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;

import common.JDBConnect;

//JDBC를 이용한 DB연결을 위해 클래스 상속
public class BoardDAO extends JDBConnect {
	
	//인수생성자에서는 application내장객체를 매개변수로 전달한다.
	public BoardDAO(ServletContext application) {
		//부모 생성자에서는 application을 통해 web.xml에 직접
		//접근하여 컨텍스트 초기화 파라미터를 얻어온다.
		super(application);
	}
	
	//멤버메서드
	//게시물의 갯수를 카운트하여 int형으로 반환한다.
	public int selectCount(Map<String, Object> map) {
		
		//결과(게시물 수)를 담을 변수
		int totalCount = 0;
		
		//게시물 수를 얻어오는 쿼리문 작성
		String query = "SELECT COUNT(*) FROM board";
		//검색어가 있는 경우 where절을 추가하여 조건에 맞는 게시물만
		//추출한다.
		if (map.get("searchWord") != null) {
			query += "WHERE" + map.get("searchField") + " "
					+ " LIKE '%" + map.get("searchWord") + "%'";
		}
		
		try {
			//정적쿼리문 실행을 위한 Statement객체 생성
			stmt = con.createStatement();
			//쿼리문 실행 후 결과는 ResultSet으로 반환한다. 
			rs = stmt.executeQuery(query);
			//커서를 첫번째 행으로 이동하여 레코드를 읽는다.
			rs.next();
			//첫번째 컬럼의 값을 가져와서 변수에 저장한다. 
			totalCount = rs.getInt(1);
		} 
		catch (Exception e) {
			System.out.println("게시물 수를 구하는 중 예외 발생");
			e.printStackTrace();
		}
		
		return totalCount;
	}
	
	//작성한 게시물을 반환한다. 특히 반환값은 여러개의 레코드를
	//반환할 수 있으므로 List컬렉션을 반환타입으로 정의한다. 
	public List<BoardDTO> selectList(Map<String, Object> map) {
		
		//List계열의 컬렉션을 생성한다. 이때 타입 매개변수는
		//BoardDTO객체로 설정한다.
		//게시판 목록은 출력 순서가 보장되야 하므로 Set컬렉션은
		//사용할 수 없고 List컬렉션을 사용해야한다.
		List<BoardDTO> bbs = new Vector<BoardDTO>();
		
		//레코드 추출을 위한 select쿼리문 작성
		String query = "SELECT * FROM board ";
		if (map.get("searchWord") != null) {
			query += "WHERE " + map.get("searchField") + " "
					+ " LIKE '%" + map.get("searchWord") + "%'";
		}
		//최근게시물을 상단에 노출하기 위해 내림차순으로 정렬한다.
		query += "ORDER BY num DESC ";
		
		try {
			//쿼리실행 및 결과값 반환
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			//2개 이상의 레코드가 반환될수 있으므로 while문을 사용
			//갯수만큼 반복하게 된다.
			while(rs.next()) {
				//하나의 레코드를 저장할 수 있는 DTO객체를 생성
				BoardDTO dto = new BoardDTO();
				
				//setter()를 이용해서 각 컬럼의 값을 저장
				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setId(rs.getString("id"));
				dto.setVisitcount(rs.getString("visitcount"));
				
				//List컬렉션에 DTO객체를 추가한다. 
				bbs.add(dto);
			}

		} 
		catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		
		return bbs;
	}
	
	//새로운 게시물 입력을 위한 메서드
	public int insertWrite(BoardDTO dto) {
		int result = 0;
		
		try {
			/*
			MySQL의 경우 시퀀스가 별도로 없고 테이블 생성시
			자동증가컬럼으로 지정하기 위한 auto_increment가 있다.
			따라서 PK로 지정된 컬럼을 쿼리문에서 제외하면 된다. 
			*/
			
			/*
			 오라클 사용시의 insert문. 시퀀스를 사용하여 일련번호를 입력한다.
			 String query = "INSERT INTO board ( "
					+ " num,title,content,id,visitcount) "
					+ " VALUES ( "
					+ " seq_board_num.NEXTVAL, ?, ?, ?, 0)";
			*/
			
			String query = "INSERT INTO board ( "
						+ " title,content,id,visitcount) "
						+ " VALUES ( "
						+ " ?, ?, ?, 0)";
			
			//동적쿼리문 이므로 prepared객체를 통해 인파라미터를
			//채워준다.
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());
			psmt.setString(3, dto.getId());
			//insert를 실행하여 입력된 행의 갯수를 반환받는다.
			result = psmt.executeUpdate();
		} 
		catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
		
		return result;
	}
	
	//인수로 전달된 게시물의 일련번호로 하나의 게시물을 인출한다.
	public BoardDTO selectView(String num) {
		//하나의 레코드 저장을 위한 DTO객체 생성
		BoardDTO dto = new BoardDTO();
		
		//inner join(내부조인)을 통해 member테이블의 name컬럼까지
		//가져온다. 
		String query = "SELECT B.*, M.name "
				+ " FROM member M INNER JOIN board B "
				+ " ON M.id=B.id "
				+ " WHERE num=? ";
		
		try {
			//인파라미터 설정 및 쿼리문 실행
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			rs = psmt.executeQuery();
			/*
			일련번호는 중복되지 않으므로 단 한개의 게시물만 인출하게
			된다. 따라서 while문이 아닌 if문으로 처리한다.
			next() 메서드는 ResultSet으로 반환된 게시물을 확인해서
			존재하면 true를 반환해준다.
			*/
			if (rs.next()) {
				//DTO 객체에 레코드를 저장한다.
				dto.setNum(rs.getString(1));
				dto.setTitle(rs.getString(2));
				/*
				각 컬럼의 값을 추출할때 1부터 시작하는 인덱스와
				컬럼명을 둘다 사용할 수 있다. 날짜인 경우에는
				getDate()메서드로 추출할 수 있다.
				*/
				dto.setContent(rs.getString("content"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setId(rs.getString("id"));
				dto.setVisitcount(rs.getString(6));
				dto.setName(rs.getString("name"));
			}
		} 
		catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생");
			e.printStackTrace();
		}
		
		return dto;
	}
	
	//게시물의 조회수를 1 증가시킨다.
	public void updateVisitCount(String num) {
		
		/* 게시물의 일련번호를 통해 visitcount를 1 증가 시킨다.
 		해당 컬럼은 number 타입이므로 사칙연산이 가능하다. */
		String query = "UPDATE board SET "
				+ " visitcount=visitcount+1 "
				+ " WHERE num=? ";
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			rs = psmt.executeQuery();
		} 
		catch (Exception e) {
			System.out.println("게시물 조회수 증가 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	public int updateEdit(BoardDTO dto) {
		int result = 0;
		
		try {
			//특정 일련번호에 해당하는 게시물을 수정한다. 
			String query = "UPDATE board SET "
						+ " title=?, content=? "
						+ " WHERE num=? ";
			
			psmt = con.prepareStatement(query);
			//인파라미터 설정하기
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());
			psmt.setString(3, dto.getNum());
			//수정된 레코드의 갯수가 반환된다.
			result = psmt.executeUpdate();
		} 
		catch (Exception e) {
			System.out.println("게시물 수정 중 예외 발생");
			e.printStackTrace();
		}
		
		return result;
	}
	
	//게시물 삭제하기
	public int deletePost(BoardDTO dto) {
		int result = 0;
		
		try {
			//인파라미터가 있는 delete쿼리문 작성
			String query = "DELETE FROM board WHERE num=?";
			
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getNum());
			
			result = psmt.executeUpdate();
		} 
		catch (Exception e) {
			System.out.println("게시물 삭제 중 예외 발생");
			e.printStackTrace();
		}
		
		return result;
	}
	
	//게시물 목록 출력시 페이징 기능 추가
	public List<BoardDTO> selectListPage(Map<String, Object> map) {
			List<BoardDTO> bbs = new Vector<BoardDTO>();
			
			/* 검색조건에 일치하는 게시물을 얻어온 후 각 페이지에 출력할
			 * 구간까지 설정한 쿼리문 작성 */
			String query = "    SELECT * FROM board ";
					
			//검색어가 있는 경우에만 where을 추가한다.
			if (map.get("searchWord") != null) {
				query += " WHERE " + map.get("searchField")
						+ " LIKE '%" + map.get("searchWord") + "%' ";
			}
			
			//between을 통해 게시물의 구간을 결정할 수 있다.
			query += " ORDER BY num DESC LIMIT ?, ? ";
			/*
			between절 대신 비교연산자를 통해 쿼리문을 구성할수도 있다.
			==> where rNum>=? and rNum<=?
			*/
			
			try {
				//인파라미터가 있는 쿼리문이므로 prepared객체를 생성한다.
				psmt = con.prepareStatement(query);
				
				//인파라미터를 설정한다. 구간의 시작과 끝을 계산한 값이다.
				/* setString()으로 인파라미터를 설정하면 문자형이 되므로
				값 양쪽에 싱글쿼테이션이 자동으로 삽입된다. 여기서는 정수를
				입력해야 하므로 setInt()를 사용하고, 인수로 전달되는 변수를
				졍수로 변환해야한다. */
				psmt.setInt(1, Integer.parseInt(map.get("start").toString()));
				//쿼리문을 실행하고 결과레코드를 ResultSet을 반환받는다.
				psmt.setInt(2, Integer.parseInt(map.get("end").toString()));
				rs = psmt.executeQuery();
				//결과 레코드의 갯수만큼 반복하여 List컬렉션에 저장한다.
				while(rs.next()) {
					BoardDTO dto = new BoardDTO();
					dto.setNum(rs.getString("num"));
					dto.setTitle(rs.getString("title"));
					dto.setContent(rs.getString("content"));
					dto.setPostdate(rs.getDate("postdate"));
					dto.setId(rs.getString("id"));
					dto.setVisitcount(rs.getString("visitcount"));
					
					bbs.add(dto);
				}
	
			} 
			catch (Exception e) {
				System.out.println("게시물 조회 중 예외 발생");
				e.printStackTrace();
			}
			
			return bbs;
		}
}
