package me.kbh.clinicsolution.domain.patientvisit.repository;

import java.util.List;
import me.kbh.clinicsolution.domain.patientvisit.entity.PatientVisit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientVisitRepository extends JpaRepository<PatientVisit, Long> {

  List<PatientVisit> findAllByPatientPatientId(Long patientId);
}
