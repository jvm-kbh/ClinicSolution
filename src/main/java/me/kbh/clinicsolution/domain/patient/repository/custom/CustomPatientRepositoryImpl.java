package me.kbh.clinicsolution.domain.patient.repository.custom;

import static me.kbh.clinicsolution.domain.hospital.entity.QHospital.hospital;
import static me.kbh.clinicsolution.domain.patient.entity.QPatient.patient;
import static me.kbh.clinicsolution.domain.patientvisit.entity.QPatientVisit.patientVisit;

import com.querydsl.core.Tuple;
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
  public Page<Tuple> findAllByCondition(
      PatientSearchCondition patientSearchCondition,
      Pageable pageable
  ) {
    int pageNumber = pageable.getPageNumber();
    int pageSize = pageable.getPageSize();
    long offset = (long) pageSize * pageNumber;

    JPAQuery<Tuple> query = queryFactory.select(patient, patientVisit.createdDate.max())
        .from(patient)
        .leftJoin(patient.hospital, hospital).fetchJoin()
        .leftJoin(patientVisit).on(patientVisit.patient.patientId.eq(patient.patientId)).fetchJoin()
        .where(
            patientNameContain(patientSearchCondition.getPatientName()),
            patientRegistrationNumberEq(patientSearchCondition.getPatientRegistrationNumber()),
            patientBirthDateEq(patientSearchCondition.getBirthDate())
        )
        .groupBy(patient.patientId);

    long total = queryFactory.select(patient.count()).from(patient).fetchOne().longValue();
    List<Tuple> tupleList = query.limit(pageSize).offset(offset).fetch();
    return new PageImpl<>(tupleList, pageable, total);
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
