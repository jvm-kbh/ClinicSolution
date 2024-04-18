package me.kbh.clinicsolution.domain.patient.exception.response;

import me.kbh.clinicsolution.common.exception.response.BaseErrorResponse;
import me.kbh.clinicsolution.domain.patient.exception.code.PatientError;
import org.springframework.http.ResponseEntity;

public class PatientErrorResponse extends BaseErrorResponse<PatientError> {

  public PatientErrorResponse(PatientError errorCode) {
    super(errorCode);
  }

  public static ResponseEntity<PatientErrorResponse> toResponse(
      PatientError patientError) {
    return ResponseEntity.status(patientError.getHttpStatus())
        .body(new PatientErrorResponse(patientError));
  }
}
