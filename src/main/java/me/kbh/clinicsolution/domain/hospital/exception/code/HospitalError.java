package me.kbh.clinicsolution.domain.hospital.exception.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.common.exception.code.BaseError;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum HospitalError implements BaseError {
  NOT_FOUND(HttpStatus.NOT_FOUND, "요청된 병원이 존재하지 않습니다.");

  HttpStatus httpStatus;
  String message;
}
