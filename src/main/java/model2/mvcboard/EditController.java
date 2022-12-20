package model2.mvcboard;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

import fileupload.FileUtil;
import utils.JSFunction;

@WebServlet("/mvcboard/edit.do")
public class EditController extends HttpServlet {
	
	//수정페이지로 진입해서 기존 내용을 수정폼에 설정한다.
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//수정페이지로 전달된 일련번호를 통해 게시물을 인출한다.
		String idx = req.getParameter("idx");
		MVCBoardDAO dao = new MVCBoardDAO();
		MVCBoardDTO dto = dao.selectView(idx);
		//인출된 내용은 request영역에 저장한 후 View로 포워드한다.
		req.setAttribute("dto", dto);
		req.getRequestDispatcher("/14MVCBoard/Edit.jsp").forward(req, resp);
	}
	
	//게시물 수정처리 및 파일업로드
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//디렉토리의 물리적경로 얻어오기
		String saveDirectory = req.getServletContext().getRealPath("/Uploads");
		
		//파일업로드 제한용량 얻어오기
		ServletContext application = getServletContext();
		int maxPostSize = Integer.parseInt(
				application.getInitParameter("maxPostSize"));
		
		//파일업로드 처리. 객체 생성과 동시에 업로드는 완료된다.
		MultipartRequest mr = FileUtil.uploadFile(req, saveDirectory, maxPostSize);
		
		if(mr==null) {
			JSFunction.alertBack(resp, "첨부 파일이 제한 용량을 초과합니다.");
			return;
		}
		
		//파일을 제외한 나머지 폼값을 얻어온다.
		//hidden박스에 저장된 내용(게시물수정ㄹ 및 파일수정에 필요함)
		String idx = mr.getParameter("idx");
		String prevOfile = mr.getParameter("prevOfile");
		String prevSfile = mr.getParameter("prevSfile");
		
		//사용자가 직접 입력한 값
		String name = mr.getParameter("name");
		String title = mr.getParameter("title");
		String content = mr.getParameter("content");
		/*
		패스워드의 경우 인증완료시 session영역에 저장해 둔것을 얻어온다.
		update쿼리 실행시 where에 조건을 추가된다.
		즉, 검증 완료된 사용자만 게시물을 수정할 수 있다.
		*/
		HttpSession session = req.getSession();
		String pass = (String)session.getAttribute("pass");
		
		//DTO에 데이터 저장
		MVCBoardDTO dto = new MVCBoardDTO();
		dto.setIdx(idx);
		dto.setName(name);
		dto.setTitle(title);
		dto.setContent(content);
		dto.setPass(pass);
		
		//수정페이지에서 첨부한 파일이 있는경우 파일명을 변경한다.
		String fileName = mr.getFilesystemName("ofile");
		
		if(fileName != null) {
			//날짜와 시간으로 파일명을 생성한 후 확장자와 합쳐서 서버에
			//저장될 파일명을 만든다.
			String now = new SimpleDateFormat("yyyyMMdd_HmsS").format(new Date());
			String ext = fileName.substring(fileName.lastIndexOf("."));
			String newFileName = now + ext;
			
			//파일객체 생성 후 파일을 변경한다.
			File oldFile = new File(saveDirectory + File.separator + fileName);
			File newFile = new File(saveDirectory + File.separator + newFileName);
			oldFile.renameTo(newFile);
			
			//업로드된 파일명을 DTO에 저장한다. 
			dto.setOfile(fileName);
			dto.setSfile(newFileName);
			
			//새로운 파일이 등록되었으므로 기존파일은 삭제한다.
			FileUtil.deleteFile(req, "/Uploads", prevSfile);
		}
		else {
			//새로운 파일을 등록하지 않은 경우 기존 파일명을 DTO에 저장한다.
			dto.setOfile(prevOfile);
			dto.setSfile(prevSfile);
		}
		
		//DB연결 및 업데이트 처리
		MVCBoardDAO dao = new MVCBoardDAO();
		int result = dao.updatePost(dto);
		dao.close();
		
		if (result==1) {
			//수정이 완료되면 session영역에 저장된 패스워드는 삭제한다.
			session.removeAttribute("pass");
			//내용보기 화면으로 이동하여 수정된 내용을 확인한다.
			resp.sendRedirect("../mvcboard/view.do?idx="+idx);
		}
		else {
			JSFunction.alertLocation(resp, "비밀번호 검증을 다시 진행해주세요.", "../mvcboard/view.do?idx="+idx);
		}
	}
}
