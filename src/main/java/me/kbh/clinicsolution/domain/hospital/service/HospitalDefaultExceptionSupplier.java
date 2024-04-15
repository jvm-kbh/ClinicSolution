package me.kbh.clinicsolution.domain.hospital.service;

import java.util.function.Supplier;
import me.kbh.clinicsolution.domain.hospital.exception.HospitalBusinessException;
import me.kbh.clinicsolution.domain.hospital.exception.code.HospitalError;

public interface HospitalDefaultExceptionSupplier {

  default Supplier<HospitalBusinessException> hospitalNotFoundException() {
    return () -> new HospitalBusinessException(HospitalError.NOT_FOUND);
  }
}
