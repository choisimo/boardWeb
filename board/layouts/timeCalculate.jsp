<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.TimeZone" %>


<%
String formattedDateTime = null;

/** String mysqlDatetime = comment_date; // 여기에 실제 값 설정 */

// SimpleDateFormat 객체 생성
SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 날짜 형식
outputDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 서버의 타임존에 맞게 설정

Date currentDate = new Date(); // 현재 시간을 가져옴
Date mysqlDate = outputDateFormat.parse(mysqlDatetime); // MySQL datetime 값을 Date 객체로 변환

long timeDifference = currentDate.getTime() - mysqlDate.getTime(); // 현재 시간과 MySQL datetime의 차이 (밀리초 단위)

long secondsDifference = timeDifference / 1000;
long minutesDifference = secondsDifference / 60;
long hoursDifference = minutesDifference / 60;
long daysDifference = hoursDifference / 24;
					
if (daysDifference == 0) { // 24시간 이내
    if (hoursDifference == 0) { // 1시간 이내
        if (minutesDifference == 0) {
            formattedDateTime = "방금 전";
        } else {
            formattedDateTime = minutesDifference + "분 전";
        }
    } else {
        formattedDateTime = hoursDifference + "시간 전";
    }

} 
else if (daysDifference == 1) {
	formattedDateTime = "1일 전"; 
}
else if (daysDifference == 2) {
	formattedDateTime = "2일 전"; 
}
else if (daysDifference == 3) {
	formattedDateTime = "3일 전"; 
}
else if (daysDifference == 4) {
	formattedDateTime = "4일 전"; 
}
else if (daysDifference == 5) {
	formattedDateTime = "5일 전"; 
}
else if (daysDifference == 6) {
	formattedDateTime = "6일 전"; 
}
else if (daysDifference == 7) {
	formattedDateTime = "일주일 전"; 
}
else {
	formattedDateTime = (mysqlDatetime.substring(0,10)); // 날짜만 표시
}

%>