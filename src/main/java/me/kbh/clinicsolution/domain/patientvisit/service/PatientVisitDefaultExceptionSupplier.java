package me.kbh.clinicsolution.domain.patientvisit.service;

import java.util.function.Supplier;
import me.kbh.clinicsolution.domain.patientvisit.exception.PatientVisitBusinessException;
import me.kbh.clinicsolution.domain.patientvisit.exception.code.PatientVisitError;

public interface PatientVisitDefaultExceptionSupplier {

  default Supplier<PatientVisitBusinessException> patientVisitNotFoundException() {
    return () -> new PatientVisitBusinessException(PatientVisitError.NOT_FOUND);
  }

  default Supplier<PatientVisitBusinessException> patientNotFoundException() {
    return () -> new PatientVisitBusinessException(PatientVisitError.NOT_FOUND_PATIENT);
  }

  default Supplier<PatientVisitBusinessException> hospitalNotException() {
    return () -> new PatientVisitBusinessException(PatientVisitError.NOT_FOUND_HOSPITAL);
  }
}
