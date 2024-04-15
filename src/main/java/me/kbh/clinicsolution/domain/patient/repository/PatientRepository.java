package me.kbh.clinicsolution.domain.patient.repository;

import me.kbh.clinicsolution.domain.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}
