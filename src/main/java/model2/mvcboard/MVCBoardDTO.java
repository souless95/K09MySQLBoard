package model2.mvcboard;

public class MVCBoardDTO {
	private String idx;
	//비회원제이므로 작성자의 아이디 대신 이름을 입력한다.
	private String name;
	private String title;
	private String content;
	private java.sql.Date postdate;
	//Original File Name : 클라이언트가 업로드한 원본파일명
	private String ofile;
	//Saved File Name : 파일명을 변경한 후 서버에 저장될 파일명
	private String sfile;
	//자료실이므로 파일을 다운로드한 횟수를 카운트한다.
	private int downcount;
	//비회원제 게시판이므로 회원인증 대신 패스워드를 통한 인증을 진행한다.
	private String pass;
	private int visitcount;
	
	
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public java.sql.Date getPostdate() {
		return postdate;
	}
	public void setPostdate(java.sql.Date postdate) {
		this.postdate = postdate;
	}
	public String getOfile() {
		return ofile;
	}
	public void setOfile(String ofile) {
		this.ofile = ofile;
	}
	public String getSfile() {
		return sfile;
	}
	public void setSfile(String sfile) {
		this.sfile = sfile;
	}
	public int getDowncount() {
		return downcount;
	}
	public void setDowncount(int downcount) {
		this.downcount = downcount;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public int getVisitcount() {
		return visitcount;
	}
	public void setVisitcount(int visitcount) {
		this.visitcount = visitcount;
	}
	
	
}
