package me.kbh.clinicsolution.common.exception.response.system;

import me.kbh.clinicsolution.common.exception.code.system.SystemCodeError;
import me.kbh.clinicsolution.common.exception.response.BaseErrorResponse;
import org.springframework.http.ResponseEntity;

public class SystemCodeErrorResponse extends BaseErrorResponse<SystemCodeError> {

  public SystemCodeErrorResponse(SystemCodeError errorCode) {
    super(errorCode);
  }

  public static ResponseEntity<SystemCodeErrorResponse> toResponse(
      SystemCodeError systemCodeError) {
    return ResponseEntity.status(systemCodeError.getHttpStatus())
        .body(new SystemCodeErrorResponse(systemCodeError));
  }
}
