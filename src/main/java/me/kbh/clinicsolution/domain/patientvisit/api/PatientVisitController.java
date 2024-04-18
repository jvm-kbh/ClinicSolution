package me.kbh.clinicsolution.domain.patientvisit.api;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitResponse;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitSaveRequest;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitUpdateRequest;
import me.kbh.clinicsolution.domain.patientvisit.service.PatientVisitService;
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
@RequestMapping("/patient-visit")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientVisitController {

  PatientVisitService patientVisitService;


  @GetMapping("/{patientVisitId}")
  public ResponseEntity<PatientVisitResponse> bySearchRequestId(@PathVariable Long patientVisitId) {
    return ResponseEntity.ok(patientVisitService.findById(patientVisitId));
  }

  @GetMapping("/all")
  public ResponseEntity<List<PatientVisitResponse>> all() {
    return ResponseEntity.ok(patientVisitService.findAll());
  }

  @PostMapping("")
  public ResponseEntity<PatientVisitResponse> bySaveRequest(
      @Valid @RequestBody PatientVisitSaveRequest patientVisitSaveRequest) {
    return ResponseEntity.ok(patientVisitService.save(patientVisitSaveRequest));
  }

  @PutMapping("/{patientVisitId}")
  public ResponseEntity<PatientVisitResponse> byUpdateRequest(
      @PathVariable Long patientVisitId,
      @Valid @RequestBody PatientVisitUpdateRequest patientVisitUpdateRequest) {
    return ResponseEntity.ok(patientVisitService.update(patientVisitId, patientVisitUpdateRequest));
  }

  @DeleteMapping("/{patientVisitId}")
  public ResponseEntity<Boolean> byDeleteRequestId(@PathVariable Long patientVisitId) {
    patientVisitService.delete(patientVisitId);
    return ResponseEntity.ok(true);
  }
}
