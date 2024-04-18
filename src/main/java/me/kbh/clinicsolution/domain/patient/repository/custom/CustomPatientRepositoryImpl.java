package me.kbh.clinicsolution.domain.patient.repository.custom;

import static me.kbh.clinicsolution.domain.hospital.entity.QHospital.hospital;
import static me.kbh.clinicsolution.domain.patient.entity.QPatient.patient;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.domain.patient.dto.PatientSearchCondition;
import me.kbh.clinicsolution.domain.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomPatientRepositoryImpl implements CustomPatientRepository {

  JPAQueryFactory queryFactory;

  @Override
  public Page<Patient> findAllByCondition(
      PatientSearchCondition patientSearchCondition,
      Pageable pageable
  ) {
    int pageNumber = pageable.getPageNumber();
    int pageSize = pageable.getPageSize();
    long offset = (long) pageSize * pageNumber;

    JPAQuery<Patient> query = queryFactory.selectFrom(patient)
        .leftJoin(patient.hospital, hospital)
        .where(
            patientNameContain(patientSearchCondition.getPatientName()),
            patientRegistrationNumberEq(patientSearchCondition.getPatientRegistrationNumber()),
            patientBirthDateEq(patientSearchCondition.getBirthDate())
        );

    long total = query.fetch().size();
    List<Patient> patientList = query.limit(pageSize).offset(offset).fetch();
    return new PageImpl<>(patientList, pageable, total);
  }

  private static BooleanExpression patientNameContain(String patientName) {
    boolean isUsefulData = (patientName != null) && (!patientName.isBlank());
    return isUsefulData
        ? patient.patientName.contains(patientName)
        : null;
  }

  private static BooleanExpression patientRegistrationNumberEq(String patientRegistrationNumber) {
    boolean isUsefulData =
        (patientRegistrationNumber != null) && (!patientRegistrationNumber.isBlank());
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
