package me.kbh.clinicsolution.domain.patient.api;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.common.dto.PageInfoWrapper;
import me.kbh.clinicsolution.domain.patient.dto.PatientResponse;
import me.kbh.clinicsolution.domain.patient.dto.PatientSaveRequest;
import me.kbh.clinicsolution.domain.patient.dto.PatientSearchCondition;
import me.kbh.clinicsolution.domain.patient.dto.PatientTotalInfoResponse;
import me.kbh.clinicsolution.domain.patient.dto.PatientUpdateRequest;
import me.kbh.clinicsolution.domain.patient.service.PatientService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientController {

  PatientService patientService;

  @GetMapping("/{patientId}")
  public ResponseEntity<PatientTotalInfoResponse> bySearchRequestId(@PathVariable Long patientId) {
    return ResponseEntity.ok(patientService.findById(patientId));
  }

  @GetMapping("/all/condition")
  public ResponseEntity<PageInfoWrapper<PatientResponse>> allByCondition(
      @Valid @ModelAttribute PatientSearchCondition patientSearchCondition,
      Pageable pageable) {
    return ResponseEntity.ok(patientService.findAllByCondition(patientSearchCondition, pageable));
  }

  @PostMapping("")
  public ResponseEntity<PatientResponse> bySaveRequest(
      @Valid @RequestBody PatientSaveRequest patientSaveRequest) {
    return ResponseEntity.ok(patientService.save(patientSaveRequest));
  }

  @PutMapping("/{patientId}")
  public ResponseEntity<PatientResponse> byUpdateRequest(
      @PathVariable Long patientId,
      @Valid @RequestBody PatientUpdateRequest patientUpdateRequest) {
    return ResponseEntity.ok(patientService.update(patientId, patientUpdateRequest));
  }

  @DeleteMapping("/{patientId}")
  public ResponseEntity<Boolean> byDeleteRequestId(@PathVariable Long patientId) {
    patientService.delete(patientId);
    return ResponseEntity.ok(true);
  }
}
