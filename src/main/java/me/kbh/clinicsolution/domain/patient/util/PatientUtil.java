package me.kbh.clinicsolution.domain.patient.util;

import me.kbh.clinicsolution.domain.patient.exception.PatientBusinessException;
import me.kbh.clinicsolution.domain.patient.exception.code.PatientError;

public class PatientUtil {

  private static final String PREFIX = "P";
  private static final int MAX_LENGTH = 13;

  public static String generatePatientRegistrationNumber(long number) {
    String covertNumberString = Long.toString(number);
    int numberLength = covertNumberString.length();

    if (numberLength > MAX_LENGTH - PREFIX.length()) {
      throw new PatientBusinessException(PatientError.MAX_PATIENT_REGISTRATION);
    }

    int zeroToAppend = MAX_LENGTH - PREFIX.length() - numberLength;
    String paddedNumber = "0".repeat(Math.max(0, zeroToAppend)) + covertNumberString;
    return PREFIX + paddedNumber;
  }
}
