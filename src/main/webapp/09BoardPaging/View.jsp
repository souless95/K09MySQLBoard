<%@page import="model1.board.BoardDTO"%>
<%@page import="model1.board.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
/* 목록에서 제목을 클릭하면 게시물의 일련번호를 ?num=99와 같이
받아온다. 따라서 내용보기를 위해 해당 파라미터를 받아온다. */
String num = request.getParameter("num");
//DAO 객체 생성을 통해 오라클에 연결한다. 
BoardDAO dao = new BoardDAO(application);
//게시물의 조회수 증가
dao.updateVisitCount(num);
//게시물의 내용을 인출하여 DTO로 반환받는다.
BoardDTO dto = dao.selectView(num);
//자원해제
dao.close();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원제 게시판</title>
<script>
//게시물 삭제를 위한 Javascript 함수
function deletePost() {
	//confirm() 함수는 대화창에서 "예"를 누를때 true가 반환된다.
    var confirmed = confirm("정말로 삭제하겠습니까?"); 
    if (confirmed) {
    	//<form>의 name속성을 통해 DOM을 가져온다.
        var form = document.writeFrm;      
    	//전송방식과 폼값을 전송할 URL을 설정한다. 
        form.method = "post"; 
        form.action = "DeleteProcess.jsp"; 
        //submit() 함수를 통해 폼값을 전송한다.
        form.submit();  
        //<form>태그 하위의 hidden박스에 설정된 일련번호가 전송된다.
    }
}
</script>
</head>
<body>
<jsp:include page="../Common/Link.jsp" />
<h2>회원제 게시판 - 상세 보기(View)</h2>
<form name="writeFrm">
<!-- 
게시물 삭제를 위해 hidden타입의 input상자를 하나 추가한다.
삭제 버튼 클릭시 서버로 전송할 예정이다. 
-->
<input type="hidden" name="num" value="<%= num %>" />  
    <table border="1" width="90%">
        <tr>
            <td>번호</td>
            <td><%= dto.getNum() %></td>
            <td>작성자</td>
            <td><%= dto.getName() %></td>
        </tr>
        <tr>
            <td>작성일</td>
            <td><%= dto.getPostdate() %></td>
            <td>조회수</td>
            <td><%= dto.getVisitcount() %></td>
        </tr>
        <tr>
            <td>제목</td>
            <td colspan="3"><%= dto.getTitle() %></td>
        </tr>
        <tr>
            <td>내용</td>
            <td colspan="3" height="100">
	            <!-- 입력시 줄바꿈을 위한 엔터는 \r\n으로 입력된다.
	            따라서 웹브라우저에 출력시에는 <br>태그로 변경해야 
	            한다. -->
                <%= dto.getContent().replace("\r\n", "<br/>") %>
            </td> 
        </tr>
        <tr>
            <td colspan="4" align="center">
            	<%
            	/*
            	로그인이 된 상태에서, 세션영역에 저장된 아이디가 해당 게시물을
            	작성한 아이디와 일치하면 수정, 삭제 버튼을 보이게 처리한다.
            	즉, 작성자 본인이 해당 게시물을 조회했을때만 수정, 삭제 버튼이
            	보이게 처리한다.
            	*/
            	if(session.getAttribute("UserId")!= null &&
            	dto.getId().equals(session.getAttribute("UserId").toString())){
            		
            	%>
                <button type="button" onclick="location.href='Edit.jsp?num=<%=dto.getNum()%>';">
                    수정하기</button>
                <button type="button" onclick="deletePost();">
                	삭제하기</button> 
                <%
            	}
            	%>
            
                <button type="button" onclick="location.href='List.jsp';">
                    목록 보기
                </button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
