package com.prgrms.p2p.domain.user.util;

public class MailBodyUtil {

  public static String getMailTitle(String name) {

    StringBuilder titleBuilder = new StringBuilder();

    titleBuilder.append("[이곳 저곳] ");
    titleBuilder.append(name);
    titleBuilder.append("님 비밀번호 찾기 이메일입니다.");

    return titleBuilder.toString();
  }

  public static String getMailBody(String name, String password) {

    StringBuilder contentBuilder = new StringBuilder();

    contentBuilder.append("<h1>[이곳 저곳] 이메일 인증</h1>");
    contentBuilder.append("<br> <h3> ");
    contentBuilder.append(name);
    contentBuilder.append(" 회원님. </h3><br> <p> 이곳저곳 임시 비밀번호는 ");
    contentBuilder.append(password);
    contentBuilder.append(" 입니다. </p><br> <h3> 비밀번호를 변경 후 다시 사용해 주세요 </h3>");

    return contentBuilder.toString();
  }

}
