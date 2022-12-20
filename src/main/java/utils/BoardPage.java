package utils;

public class BoardPage {
	/* List.jsp에서 전달해준 인수를 아래 매개변수를 통해 받는다. */
	public static String pagingStr(int totalCount, int pageSize, int blockPage,
			int pageNum, String reqUrl) {
		
		//페이지 바로가기 링크를 저장할 문자열변수 생성
		String pagingStr = "";
		
		//전체 페이지 수를 계산한다.
		int totalPages = (int) (Math.ceil(((double)totalCount/pageSize)));
		
		/*
		현재 페이지 블럭에서 사용할 첫번째 페이지번호를 계산한다.
			현재 1페이지라 가정하면
				(((1-1) / 5) * 5) + 1 = 1
			현재 5페이지라 가정하면
				(((5-1) / 5) * 5) + 1 = 1
				==> 즉 1~5페이지인 경우 해당 블럭의 첫번째 페이지는 1이다.
			현재 6페이지라 가정하면
				(((6-1) / 5) * 5) + 1 = 6
			현재 11페이지라 가정하면
				(((11-1) / 5) * 5) + 1 = 6
		*/
		int pageTemp = (((pageNum-1) / blockPage) * blockPage) + 1;
		if (pageTemp != 1) {
			pagingStr += "<a href='" + reqUrl + "?pageNum=1'>[첫 페이지]</a>";
			pagingStr += "&nbsp";
			pagingStr += "<a href='" + reqUrl + "?pageNum=" + (pageTemp - 1)
						+ "'>[이전 블록]</a>";
		}
		
		/*
		각 페이지 번호로 바로가기 링크 출력
		앞에서 계산한 pageTemp를 blockPage만큼 반복해서 출력한다.
		이때 1씩 증가시켜준다. 즉 한블록당 5페이지를 출력하게된다.  
		*/
		int blockCount = 1;
		while (blockCount <= blockPage && pageTemp <= totalPages) {
			//만약 현재페이지라면 링크를 걸지 않는다.
			if(pageTemp == pageNum) {
				pagingStr += "&nbsp;" + pageTemp + "&nbsp;";
			}
			else {
				//현재페이지가 아닌 경우에만 링크를 추가한다.
				pagingStr += "&nbsp;<a href='" + reqUrl + "?pageNum=" + pageTemp
							+ "'>" + pageTemp + "</a>&nbsp;";
			}
			//반복하면서 1씩 증가시켜 순차적인 페이지번호를 출력한다.
			pageTemp++;
			blockCount++;
		}
		
		//다음 페이지 블록 바로가기 링크 추가
		//마지막 페이지가 아닌 경우에만 다음블럭을 출력한다.
		if(pageTemp <= totalPages) {
			pagingStr += "<a href='" + reqUrl + "?pageNum=" + pageTemp
						+ "'>[다음 블록]</a>";
			pagingStr += "&nbsp";
			pagingStr += "<a href='" + reqUrl + "?pageNum=" + totalPages
						+ "'>[마지막 페이지]</a>";
		}
		    return pagingStr;
	}
}
