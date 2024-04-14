package me.kbh.clinicsolution.common.exception.response.system;

import me.kbh.clinicsolution.common.exception.code.system.SystemGroupCodeError;
import me.kbh.clinicsolution.common.exception.response.BaseErrorResponse;
import org.springframework.http.ResponseEntity;

public class SystemGroupCodeErrorResponse extends BaseErrorResponse<SystemGroupCodeError> {

  public SystemGroupCodeErrorResponse(SystemGroupCodeError errorCode) {
    super(errorCode);
  }

  public static ResponseEntity<SystemGroupCodeErrorResponse> toResponse(
      SystemGroupCodeError systemGroupCodeError) {
    return ResponseEntity.status(systemGroupCodeError.getHttpStatus())
        .body(new SystemGroupCodeErrorResponse(systemGroupCodeError));
  }
}
