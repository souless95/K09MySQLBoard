<%@page import="java.sql.PreparedStatement"%>
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
	<h2>회원 추가 테스트(executeUpdate() 사용)</h2>
	<%
	JDBConnect jdbc = new JDBConnect();
	
	//입력할 회원데이터 준비(하드코딩)
	String id = "test2";
	String pass = "2222";
	String name = "테스트2회원";
	
	//인파라미터가 있는 동적쿼리문을 준비한다.
	String sql = "INSERT INTO member VALUES (? , ? , ? , sysdate)";
	//동적쿼리문 실행을 위한 prepared객체 생성
	PreparedStatement psmt = jdbc.con.prepareStatement(sql);
	//인파라미터 설정 시 인덱스는 1부터 시작한다.
	psmt.setString(1, id);
	psmt.setString(2, pass);
	psmt.setString(3, name);
	
	//행에 변화를 주는 update, delete, insert쿼리문은 executeUpdate()
	//메서드를 통해 실행한다.
	int inResult = psmt.executeUpdate();
	out.println(inResult + "행이 입력되었습니다.");
	
	//자원해제(객체소멸)
	jdbc.close();
	%>
</body>
</html>