package me.kbh.clinicsolution.domain.hospital.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.domain.hospital.exception.code.HospitalError;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HospitalBusinessException extends RuntimeException {

  HospitalError hospitalError;
}
