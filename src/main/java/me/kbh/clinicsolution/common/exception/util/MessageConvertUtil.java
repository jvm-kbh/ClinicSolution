package me.kbh.clinicsolution.common.exception.util;

public class MessageConvertUtil {

  public static String convertMessageForEnum(String message) {
    String[] splitExceptionMessage = message.split("\"");
    String requestValue = splitExceptionMessage[1];
    String acceptableValue = splitExceptionMessage[2].split(": ")[2];
    return String.join("",
        "요청하신 \"" + requestValue + "\"는 허용되는 코드 값 " + acceptableValue + " 에 해당되지 않습니다.");
  }
}
