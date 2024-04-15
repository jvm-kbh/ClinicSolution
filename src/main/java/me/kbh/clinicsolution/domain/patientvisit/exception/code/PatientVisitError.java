package me.kbh.clinicsolution.domain.patientvisit.exception.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.common.exception.code.BaseError;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum PatientVisitError implements BaseError {
  NOT_FOUND(HttpStatus.NOT_FOUND, "요청된 내원 정보가 존재하지 않습니다."),
  NOT_FOUND_PATIENT(HttpStatus.NOT_FOUND, "내원 정보로 등록하려는 환자가 존재하지 않습니다"),
  NOT_FOUND_HOSPITAL(HttpStatus.NOT_FOUND, "내원 정보로 등록하려는 병원이 존재하지 않습니다");

  HttpStatus httpStatus;
  String message;
}
