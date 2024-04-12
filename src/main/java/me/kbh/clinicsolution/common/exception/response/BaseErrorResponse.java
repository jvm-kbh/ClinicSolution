package me.kbh.clinicsolution.common.exception.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.common.exception.code.BaseError;

@FieldDefaults(level = AccessLevel.PUBLIC)
public abstract class BaseErrorResponse<T extends Enum<T> & BaseError> {

  String definitionCodeName;
  int httpStatusCode;
  String httpStatusType;
  String errorMessage;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  LocalDateTime currentTime;

  public BaseErrorResponse(T errorCode) {
    this.definitionCodeName = errorCode.getClass().getName();
    this.httpStatusCode = errorCode.getHttpStatus().value();
    this.httpStatusType = errorCode.getHttpStatus().name();
    this.errorMessage = errorCode.getMessage();
    this.currentTime = LocalDateTime.now();
  }
}