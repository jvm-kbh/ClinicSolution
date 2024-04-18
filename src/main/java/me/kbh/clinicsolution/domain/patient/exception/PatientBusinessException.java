package me.kbh.clinicsolution.domain.patient.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.domain.patient.exception.code.PatientError;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientBusinessException extends RuntimeException {

  PatientError patientError;
}
