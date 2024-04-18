package me.kbh.clinicsolution.common.exception.advice;

import java.util.HashMap;
import java.util.Map;
import me.kbh.clinicsolution.common.exception.response.system.SystemCodeBusinessException;
import me.kbh.clinicsolution.common.exception.response.system.SystemCodeErrorResponse;
import me.kbh.clinicsolution.common.exception.response.system.SystemGroupCodeBusinessException;
import me.kbh.clinicsolution.common.exception.response.system.SystemGroupCodeErrorResponse;
import me.kbh.clinicsolution.common.exception.util.MessageConvertUtil;
import me.kbh.clinicsolution.domain.hospital.exception.HospitalBusinessException;
import me.kbh.clinicsolution.domain.hospital.exception.response.HospitalErrorResponse;
import me.kbh.clinicsolution.domain.patient.exception.PatientBusinessException;
import me.kbh.clinicsolution.domain.patient.exception.response.PatientErrorResponse;
import me.kbh.clinicsolution.domain.patientvisit.exception.PatientVisitBusinessException;
import me.kbh.clinicsolution.domain.patientvisit.exception.response.PatientVisitErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppRestControllerAdvice {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> test(HttpMessageNotReadableException exception) {
    String message = exception.getMessage();
    boolean isIncorrectEnumType = message.contains("Enum");
    String commonReadableExceptionMessage = "요청에 허용되지 않는 값입니다.";

    if (isIncorrectEnumType) {
      commonReadableExceptionMessage = MessageConvertUtil.convertMessageForEnum(message);
    }

    return new ResponseEntity<>(commonReadableExceptionMessage, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handlingValidation(
      MethodArgumentNotValidException exception) {
    Map<String, String> errors = new HashMap<>();
    exception.getBindingResult().getAllErrors()
        .forEach(e -> errors.put(((FieldError) e).getField(), e.getDefaultMessage()));
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(SystemCodeBusinessException.class)
  public ResponseEntity<SystemCodeErrorResponse> handlingForSystemCodeBusinessException(
      SystemCodeBusinessException exception) {
    return SystemCodeErrorResponse.toResponse(exception.getSystemCodeError());
  }

  @ExceptionHandler(SystemGroupCodeBusinessException.class)
  public ResponseEntity<SystemGroupCodeErrorResponse> handlingForSystemGroupCodeBusinessException(
      SystemGroupCodeBusinessException exception) {
    return SystemGroupCodeErrorResponse.toResponse(exception.getSystemGroupCodeError());
  }

  @ExceptionHandler(HospitalBusinessException.class)
  public ResponseEntity<HospitalErrorResponse> handlingForHospitalBusinessException(
      HospitalBusinessException exception) {
    return HospitalErrorResponse.toResponse(exception.getHospitalError());
  }

  @ExceptionHandler(PatientBusinessException.class)
  public ResponseEntity<PatientErrorResponse> handlingForPatientBusinessException(
      PatientBusinessException exception) {
    return PatientErrorResponse.toResponse(exception.getPatientError());
  }

  @ExceptionHandler(PatientVisitBusinessException.class)
  public ResponseEntity<PatientVisitErrorResponse> handlingForPatientVisitBusinessException(
      PatientVisitBusinessException exception) {
    return PatientVisitErrorResponse.toResponse(exception.getPatientVisitError());
  }
}
