package me.kbh.clinicsolution.common.exception.advice;

import me.kbh.clinicsolution.common.exception.response.system.SystemCodeBusinessException;
import me.kbh.clinicsolution.common.exception.response.system.SystemCodeErrorResponse;
import me.kbh.clinicsolution.common.exception.response.system.SystemGroupCodeBusinessException;
import me.kbh.clinicsolution.common.exception.response.system.SystemGroupCodeErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppRestControllerAdvice {

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
}
