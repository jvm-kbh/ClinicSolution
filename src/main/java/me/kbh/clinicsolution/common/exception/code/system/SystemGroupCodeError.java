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
public enum SystemGroupCodeError implements BaseError {
  VALID_SYSTEM_CODE_GROUP(HttpStatus.BAD_REQUEST, "해당 시스템 코드그룹과 등록하려는 그룹코드가 다릅니다.");

  HttpStatus httpStatus;
  String message;
}
