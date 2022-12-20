package model2.mvcboard;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import fileupload.FileUtil;
import utils.JSFunction;

/*
글쓰기 페이지로 진입시에는 단순한 페이지이동이므로 doGet()이 요청을
처리한다. 즉 글쓰기 페이지만 보여주면 된다.
내용을 모두 채운후 작성(submit)할때는 post방식으로 doPost()가
요청을 받아 처리한다. 
*/
public class WriteController extends HttpServlet {
	
	//글쓰기 페이지로 진입할때의 요청을 처리한다.
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//단순 포워드 기능만 있으면 된다.
		req.getRequestDispatcher("/14MVCBoard/Write.jsp").forward(req, resp);
	}
	
	//클라이언트가 작성한 내용을 DB에 입력하고 파일업로드 처리를 한다.
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//파일이 업로드될 디렉토리의 물리적 경로를 얻어온다.
		String saveDirectory = req.getServletContext().getRealPath("/Uploads");
		
		//application내장객체를 통해 web.xml에 등록된 초기화 파라미터를 얻어온다.
		//업로드 제한용량은 현재 1mb로 설정되어 있다.
		ServletContext application = getServletContext();
		int maxPostSize = Integer.parseInt(
				application.getInitParameter("maxPostSize"));
		
		/*
		파일업로드를 위해 MultipartRequest객체를 생성한다. 객체가 정상적으로 생성된다면
		파일업로드는 완료된다.
		*/
		MultipartRequest mr = FileUtil.uploadFile(req, saveDirectory, maxPostSize);
		/*
		만약 객체생성이 되지 않았다면 업로드에 실패한것이다. 이때는 파일의 용량이나
		디렉토리 경로를 확인해본다.
		만약 글 작성시 파일을 첨부하지 않더라도 객체는 생성되므로 폼값은 정상적으로
		받을 수 있다.
		*/
		if(mr==null) {
			JSFunction.alertLocation(resp, "첨부 파일이 제한 용량을 초과합니다.",
						"../mvcboard/write.do");
			/*
			객체가 생성되지 않아 업로드에 실패하면 나머지 폼값도 받을 수 없으므로
			반드시 return을 통해 실행을 중지시켜야 한다. */
			return;
		}
		//여기까지의 코드로 파일업로드는 완료된다.
		
		//파일을 제외한 나머지 폼값을 받아서 DTO객체에 저장한다.
		//단, request 내장객체가 아닌 mr을 통해 받아온다.
		MVCBoardDTO dto = new MVCBoardDTO();
		dto.setName(mr.getParameter("name"));
		dto.setTitle(mr.getParameter("title"));
		dto.setContent(mr.getParameter("content"));
		dto.setPass(mr.getParameter("pass"));
		
		//서버에 업로드 된 파일명을 얻어온다.
		String fileName = mr.getFilesystemName("ofile");
		/* 서버에 저장된 파일이 있는 경우에만 파일명 변경 처리를 한다.
		만약 첨부하지 않았다면 아래 코드는 실행하지 않는다. */
		if(fileName != null) {
			//날짜와 시간을 이용해서 파일명을 생성한다. 
			String now = new SimpleDateFormat("yyyyMMdd_HmsS").format(new Date());
			//파일명의 마지막에 있는 .(닷)의 인덱스를 찾은 후 확장자를 잘라낸다.
			String ext = fileName.substring(fileName.lastIndexOf("."));
			//파일명과 확장자를 연결해서 새로운 파일명을 생성한다.
			String newFileName = now + ext;
			//파일 객체를 생성한 후 파일명을 변경한다.
			File oldFile = new File(saveDirectory + File.separator + fileName);
			File newFile = new File(saveDirectory + File.separator + newFileName);
			oldFile.renameTo(newFile);
			//DTO객체에 원본파일명과 저장된 파일명을 저장한다.
			dto.setOfile(fileName);
			dto.setSfile(newFileName);
		}
		
		//새로운 게시물을 테이블에 입력한다.
		MVCBoardDAO dao = new MVCBoardDAO();
		int result = dao.insertWrite(dto);
		//커넥션풀 자원반납
		dao.close();
		
		//서블릿에서 특정 요청명으로 이동할때는 sendRedirect()를 사용하면 된다.
		if (result==1) {
			//insert에 성공하면 리스트로 이동한다.
			resp.sendRedirect("../mvcboard/list.do");
		}
		else {
			//실패하면 작성페이지로 다시 돌아간다.
			resp.sendRedirect("../mvcboard/write.do");			
		}
	}
}
