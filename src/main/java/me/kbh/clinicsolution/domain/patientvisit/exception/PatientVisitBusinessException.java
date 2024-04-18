package me.kbh.clinicsolution.domain.patientvisit.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.domain.patientvisit.exception.code.PatientVisitError;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientVisitBusinessException extends RuntimeException {

  PatientVisitError patientVisitError;
}
