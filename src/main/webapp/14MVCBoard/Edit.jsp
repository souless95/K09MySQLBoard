<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>파일 첨부형 게시판</title>
<script type="text/javascript">
/* 패스워드 검증을 통해 수정페이지로 진입하므로 해당 페이지에서는
추가로 패스워드를 입력하지 않는다. */
function validateForm(form) {  // 폼 내용 검증
    if (form.name.value == "") {
        alert("작성자를 입력하세요.");
        form.name.focus();
        return false;
    }
    if (form.title.value == "") {
        alert("제목을 입력하세요.");
        form.title.focus();
        return false;
    }
    if (form.content.value == "") {
        alert("내용을 입력하세요.");
        form.content.focus();
        return false;
    }
}
</script>
</head>
<body>
<jsp:include page="../Common/Link.jsp" />
<h2>파일 첨부형 게시판 - 수정하기(Edit)</h2>
<!-- 글쓰기 페이지를 그대로 사용하되 action 부분만 수정한다. 수정시에드
파일첨부가 있으므로 enctype속성은 추가되어야한다. -->
<form name="writeFrm" method="post" action="../mvcboard/edit.do"
	enctype="multipart/form-data"
      onsubmit="return validateForm(this);">
<!-- 게시물 수정을 위한 일련번호 -->      
<input type="hidden" name="idx" value="${ dto.idx }">
<!-- 기존의 원본파일명 -->
<input type="hidden" name="prevOfile" value="${ dto.ofile }">
<!-- 기존의 서버에 저장된 파일명 -->
<input type="hidden" name="prevsfile" value="${ dto.sfile }">
<!-- 해당 hidden값은 게시물 수정시 첨부파일이 없는 경우 사용될것이다. -->

검증된패스워드 : ${pass}
<table border="1" width="90%">
    <tr>
        <td>작성자</td>
        <td>
            <input type="text" name="name" style="width: 150px;" value="${ dto.name }" />
        </td>
    </tr>
    <tr>
        <td>제목</td>
        <td>
            <input type="text" name="title" style="width: 90%;" value="${ dto.title }" />
        </td>
    </tr>
    <tr>
        <td>내용</td>
        <td>
            <textarea name="content" style="width: 90%; height: 100px;" wrap="hard">${ dto.content }</textarea>
        </td>
    </tr>
    <tr>
        <td>첨부파일</td>
        <td>
            <input type="file" name="ofile" />
        </td>
    </tr>
    <!-- 패스워드 부분은 삭제한다. -->
    <tr>
        <td colspan="2" align="center">
            <button type="submit">작성 완료</button>
            <button type="reset">다시 입력</button>
            <button type="button" onclick="location.href='../mvcboard/list.do';">
                목록 보기</button>
        </td>
    </tr>
</table>
</form>
</body>
</html>
