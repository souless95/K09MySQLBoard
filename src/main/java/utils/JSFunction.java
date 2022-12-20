package utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

public class JSFunction {
	/*
	메서드 생성시 static을 통해 정적메서드로 정의되었으므로
	객체 생성없이 클래스명으로 즉시 메서드를 호출할 수 있다. 
	*/
	public static void alertLocation(String msg, String url, JspWriter out) {
        /*
        Java클래스에서는 JSP의 내장객체를 사용할 수 없으므로 반드시
        매개변수로 전달받아 사용해야한다.
        여기서는 화면에 문자열을 출력학 위해 out내장객체를
        JspWriter 타입으로 받은 후 사용하고 있다. 
        */
		try {
			//Javascript를 하나의 문자열로 정의한다. 
            String script = ""  
                          + "<script>"
                          + "    alert('" + msg + "');"
                          + "    location.href='" + url + "';"
                          + "</script>";
            //해당 문자열을 췝브라우저에 출력한다. 
            out.println(script);
        }
        catch (Exception e) {}
    }

    public static void alertBack(String msg, JspWriter out) {
        try {
            String script = ""
                          + "<script>"
                          + "    alert('" + msg + "');"
                          + "    history.back();"
                          + "</script>";
            out.println(script);
        }
        catch (Exception e) {}
    }
    
    /*
    앞의 2개의 메서드는 JSP에서 out내장객체를 받은 후 Javascript함수를
    실행한다. 하지만 서블릿에서는 JSP의 내장객체를 매개변수로 받을 수
    없으므로 out내장객체 대신 response내장객체를 통해 컨텐츠 타입을
    설정한 후 JS코드를 실행해야한다.
    */
	public static void alertLocation(HttpServletResponse resp, String msg, String url) {
		try {
			//컨텐츠 타입을 설정
			resp.setContentType("text/html; charset=UTF-8");
			//PrintWriter객체를 통해 스크립트를 서블릿에서 직접 출력한다.
			PrintWriter writer = resp.getWriter();
			
            String script = ""  
                          + "<script>"
                          + "    alert('" + msg + "');"
                          + "    location.href='" + url + "';"
                          + "</script>";
            writer.println(script);
        }
        catch (Exception e) {}
    }

    public static void alertBack(HttpServletResponse resp, String msg) {
        try {
        	resp.setContentType("text/html; charset=UTF-8");
			PrintWriter writer = resp.getWriter();
            String script = ""
                          + "<script>"
                          + "    alert('" + msg + "');"
                          + "    history.back();"
                          + "</script>";
            writer.println(script);
        }
        catch (Exception e) {}
    }

}
