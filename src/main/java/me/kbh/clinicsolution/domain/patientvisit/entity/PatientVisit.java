package me.kbh.clinicsolution.domain.patientvisit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kbh.clinicsolution.domain.hospital.entity.Hospital;
import me.kbh.clinicsolution.domain.patient.entity.Patient;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PatientVisit {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long patientVisitId;

  @Column(nullable = false)
  LocalDate registrationDate;

  @Column(nullable = false)
  String visitStatusCode;

  @Column(nullable = false)
  String clinicSubjectCode;

  @Column(nullable = false)
  String clinicCategoryCode;

  @ManyToOne
  @JoinColumn(nullable = false, name = "patient_id")
  Patient patient;

  @ManyToOne
  @JoinColumn(nullable = false, name = "hospital_id")
  Hospital hospital;
}
