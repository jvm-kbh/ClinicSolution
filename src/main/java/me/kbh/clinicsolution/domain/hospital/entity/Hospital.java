package me.kbh.clinicsolution.domain.hospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Hospital {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long hospitalId;

  @Column(nullable = false, length = 45)
  String hospitalName;

  @Column(nullable = false, length = 20)
  String medicalInstitutionNumber;

  @Column(nullable = false, length = 10)
  String hospitalDirectorName;

  @Column(nullable = false, length = 12)
  Long patientVisitCount;
}
