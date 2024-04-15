package me.kbh.clinicsolution.common.exception.advice;

import java.util.HashMap;
import java.util.Map;
import me.kbh.clinicsolution.common.exception.response.system.SystemCodeBusinessException;
import me.kbh.clinicsolution.common.exception.response.system.SystemCodeErrorResponse;
import me.kbh.clinicsolution.common.exception.response.system.SystemGroupCodeBusinessException;
import me.kbh.clinicsolution.common.exception.response.system.SystemGroupCodeErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppRestControllerAdvice {

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
}
