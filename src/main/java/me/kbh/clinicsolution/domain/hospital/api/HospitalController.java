package me.kbh.clinicsolution.domain.hospital.api;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalResponse;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalSaveRequest;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalUpdateRequest;
import me.kbh.clinicsolution.domain.hospital.service.HospitalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hospital")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HospitalController {

  HospitalService hospitalService;

  @GetMapping("/{hospitalId}")
  public ResponseEntity<HospitalResponse> bySearchRequestId(@PathVariable Long hospitalId) {
    return ResponseEntity.ok(hospitalService.findById(hospitalId));
  }

  @GetMapping("/all")
  public ResponseEntity<List<HospitalResponse>> all() {
    return ResponseEntity.ok(hospitalService.findAll());
  }

  @PostMapping("")
  public ResponseEntity<HospitalResponse> bySaveRequest(
      @Valid @RequestBody HospitalSaveRequest hospitalSaveRequest) {
    return ResponseEntity.ok(hospitalService.save(hospitalSaveRequest));
  }

  @PutMapping("/{hospitalId}")
  public ResponseEntity<HospitalResponse> byUpdateRequest(
      @PathVariable Long hospitalId,
      @Valid @RequestBody HospitalUpdateRequest hospitalUpdateRequest) {
    return ResponseEntity.ok(hospitalService.update(hospitalId, hospitalUpdateRequest));
  }

  @DeleteMapping("/{hospitalId}")
  public ResponseEntity<Boolean> byDeleteRequestId(@PathVariable Long hospitalId) {
    hospitalService.delete(hospitalId);
    return ResponseEntity.ok(true);
  }
}
