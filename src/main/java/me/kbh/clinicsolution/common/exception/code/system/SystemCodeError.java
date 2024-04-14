package me.kbh.clinicsolution.common.exception.code.system;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.common.exception.code.BaseError;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum SystemCodeError implements BaseError {
  VALID_SYSTEM_CODE(HttpStatus.BAD_REQUEST, "해당 시스템 코드그룹에 해당되는 코드가 다릅니다.");

  HttpStatus httpStatus;
  String message;
}
