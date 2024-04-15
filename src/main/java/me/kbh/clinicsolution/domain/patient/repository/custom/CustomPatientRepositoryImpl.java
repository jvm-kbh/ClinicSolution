package me.kbh.clinicsolution.domain.patient.repository.custom;

import static me.kbh.clinicsolution.domain.hospital.entity.QHospital.hospital;
import static me.kbh.clinicsolution.domain.patient.entity.QPatient.patient;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.domain.hospital.entity.QHospital;
import me.kbh.clinicsolution.domain.patient.dto.PatientSearchCondition;
import me.kbh.clinicsolution.domain.patient.entity.Patient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomPatientRepositoryImpl implements CustomPatientRepository {

  JPAQueryFactory queryFactory;

  @Override
  public List<Patient> findAllByCondition(PatientSearchCondition patientSearchCondition) {
    return queryFactory.selectFrom(patient)
        .leftJoin(patient.hospital, hospital)
        .where(
            patientNameContain(patientSearchCondition.getPatientName()),
            patientRegistrationNumberEq(patientSearchCondition.getPatientRegistrationNumber()),
            patientBirthDateEq(patientSearchCondition.getBirthDate())
        ).fetch();
  }
  private static BooleanExpression patientNameContain(String patientName) {
    boolean isUsefulData = (patientName != null) && (!patientName.isBlank());
    return isUsefulData
        ? patient.patientName.contains(patientName)
        : null;
  }

  private static BooleanExpression patientRegistrationNumberEq(String patientRegistrationNumber) {
    boolean isUsefulData = (patientRegistrationNumber != null) && (!patientRegistrationNumber.isBlank());
    return isUsefulData
        ? patient.patientRegistrationNumber.eq(patientRegistrationNumber)
        : null;
  }

  private static BooleanExpression patientBirthDateEq(LocalDate birthDate) {
    boolean isUsefulData = birthDate != null;
    return isUsefulData
        ? patient.birthDate.eq(birthDate)
        : null;
  }
}
