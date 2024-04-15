package me.kbh.clinicsolution.domain.patient.exception.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.common.exception.code.BaseError;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum PatientError implements BaseError {
  NOT_FOUND(HttpStatus.NOT_FOUND, "요청된 환자가 존재하지 않습니다."),
  NOT_FOUND_HOSPITAL(HttpStatus.NOT_FOUND, "환자 정보로 등록하려는 병원이 존재하지 않습니다."),
  MAX_PATIENT_REGISTRATION(HttpStatus.INTERNAL_SERVER_ERROR, "시스템 내 횐자등록 번호가 최대입니다. 관리자에게 문의하세요");

  HttpStatus httpStatus;
  String message;
}
