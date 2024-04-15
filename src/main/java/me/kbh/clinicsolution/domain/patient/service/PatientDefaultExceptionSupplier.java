package me.kbh.clinicsolution.domain.patient.service;

import java.util.function.Supplier;
import me.kbh.clinicsolution.domain.patient.exception.PatientBusinessException;
import me.kbh.clinicsolution.domain.patient.exception.code.PatientError;

public interface PatientDefaultExceptionSupplier {

  default Supplier<PatientBusinessException> patientNotFoundException() {
    return () -> new PatientBusinessException(PatientError.NOT_FOUND);
  }

  default Supplier<PatientBusinessException> hospitalNotException() {
    return () -> new PatientBusinessException(PatientError.NOT_FOUND_HOSPITAL);
  }
}
