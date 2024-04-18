package me.kbh.clinicsolution.domain.hospital.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import me.kbh.clinicsolution.domain.hospital.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(value = "SELECT h FROM Hospital h WHERE h.hospitalId = :hospitalId")
  Optional<Hospital> findByHospitalIdWithPessimisticLock(@Param("hospitalId") Long hospitalId);
}
