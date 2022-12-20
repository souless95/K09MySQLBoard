<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>파일 첨부형 게시판</title>
<script type="text/javascript">
function validateForm(form) {  // 폼 내용 검증
    if (form.pass.value == "") {
        alert("비밀번호를 입력하세요.");
        form.pass.focus();
        return false;
    }
}
</script>
</head>
<body>
<jsp:include page="../Common/Link.jsp" />
<h2>파일 첨부형 게시판 - 비밀번호 검증(Pass)</h2>
<!-- 글쓰기 페이지를 복사하여 해당 페이지를 만들때 비밀번호 검증시에는
첨부파일이 필요없으믈 enctype은 제거해야한다. 만약 제거하지 않으면
request내장객체로 폼값을 받을 수 없으므로 주의해야한다. -->
<form name="writeFrm" method="post" action="../mvcboard/pass.do"
      onsubmit="return validateForm(this);">

<!-- 
해당 요청명으로 넘어온 파라미터는 컨트롤러에서 받은후 request영역에
저장하여 View에서 확인할 수 있지만, EL을 이용하며 ㄴ해당 과정없이 param
내장객체로 즉시 값을 받아올 수 있다.

※hidden박스를 사용하는 경우 웹브라우저에서는 숨김처리 되기때문에 값이
제대로 입력되었는지 화면으로 확인할 수 없다. 따라서 개발자 모드를 사용하거나
type을 디버깅용으로 잠깐 수정한 후 값이 제대로 입력하는지 반드시
확인해야한다.
-->      
<input type="hid-den" name="idx" value="${ param.idx }">
<input type="hid-den" name="mode" value="${ mode }">
      
    <table border="1" width="90%">
        <tr>
            <td>비밀번호</td>
            <td>
                <input type="password" name="pass" style="width: 100px;" />
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <button type="submit">검증하기</button>
                <button type="reset">RESET</button>
                <button type="button" onclick="location.href='../mvcboard/list.do';">
                    목록 보기</button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
