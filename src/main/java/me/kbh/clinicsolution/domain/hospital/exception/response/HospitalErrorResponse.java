package me.kbh.clinicsolution.domain.hospital.exception.response;

import me.kbh.clinicsolution.common.exception.response.BaseErrorResponse;
import me.kbh.clinicsolution.domain.hospital.exception.code.HospitalError;
import org.springframework.http.ResponseEntity;

public class HospitalErrorResponse extends BaseErrorResponse<HospitalError> {

  public HospitalErrorResponse(HospitalError errorCode) {
    super(errorCode);
  }

  public static ResponseEntity<HospitalErrorResponse> toResponse(
      HospitalError hospitalError) {
    return ResponseEntity.status(hospitalError.getHttpStatus())
        .body(new HospitalErrorResponse(hospitalError));
  }
}
