<%@page import="java.sql.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="common.JDBConnect"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JDBC</title>
</head>
<body>
	<h2>회원 목록 조회 테스트(executeQuery() 사용)</h2>
	<%
	JDBConnect jdbc = new JDBConnect();
	
	//인파라미터가 없는 정적쿼리문을 작성한다.
	String sql = "SELECT id, pass, name, regidate FROM MEMBER";
	//정적쿼리 실행을 위한 statement객체를 생성한다.
	Statement stmt = jdbc.con.createStatement();
	//행에 영향이 없는 select계열의 쿼리문은 executeQuery()메서드를
	//통해 실행한다. 이때 반환타입은 ResultSet이다.
	ResultSet rs = stmt.executeQuery(sql);
	//반환받은 결과의 갯수만큼 반복하여 출력한다.
	while(rs.next()){
		//각 컬럼에 접근시 1부터 시작하는 인덱스나 컬럼명 둘다
		//사용할 수 있다.
		String id = rs.getString(1);
		String pw = rs.getString(2);
		String name = rs.getString("name");
		java.sql.Date regidate = rs.getDate("regidate");
		
		out.println(String.format("%s %s %s %s", id, pw, name, regidate) + "<br>");
	}
	
	jdbc.close();
	%>
</body>
</html>