package me.kbh.clinicsolution.domain.patientvisit.exception.response;

import me.kbh.clinicsolution.common.exception.response.BaseErrorResponse;
import me.kbh.clinicsolution.domain.patientvisit.exception.code.PatientVisitError;
import org.springframework.http.ResponseEntity;

public class PatientVisitErrorResponse extends BaseErrorResponse<PatientVisitError> {

  public PatientVisitErrorResponse(PatientVisitError errorCode) {
    super(errorCode);
  }

  public static ResponseEntity<PatientVisitErrorResponse> toResponse(
      PatientVisitError patientError) {
    return ResponseEntity.status(patientError.getHttpStatus())
        .body(new PatientVisitErrorResponse(patientError));
  }
}
