package me.kbh.clinicsolution.domain.hospital.service;

import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalResponse;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalSaveRequest;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalUpdateRequest;
import me.kbh.clinicsolution.domain.hospital.entity.Hospital;
import me.kbh.clinicsolution.domain.hospital.repository.HospitalRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HospitalServiceImpl implements HospitalService {

  HospitalRepository hospitalRepository;

  @Override
  public HospitalResponse findById(Long hospitalId) {
    Hospital hospital =
        hospitalRepository.findById(hospitalId).orElseThrow(hospitalNotFoundException());

    return HospitalResponse.builder().mappingByEntity(hospital).build();
  }

  @Override
  public List<HospitalResponse> findAll() {
    List<Hospital> hospitalList = hospitalRepository.findAll();
    Function<Hospital, HospitalResponse> mappingByEntityFunction = hospital -> HospitalResponse.builder()
        .mappingByEntity(hospital)
        .build();
    return hospitalList.stream()
        .map(mappingByEntityFunction)
        .toList();
  }

  @Override
  public HospitalResponse save(HospitalSaveRequest hospitalSaveRequest) {
    Hospital hospital =
        Hospital.builderForSave()
            .hospitalSaveRequest(hospitalSaveRequest)
            .buildBySaveRequest();

    Hospital savedHospital = hospitalRepository.save(hospital);
    return HospitalResponse.builder().mappingByEntity(savedHospital).build();
  }

  @Override
  public HospitalResponse update(Long hospitalId, HospitalUpdateRequest hospitalUpdateRequest) {
    Hospital hospital =
        hospitalRepository.findById(hospitalId).orElseThrow(hospitalNotFoundException());

    hospital.update(hospitalUpdateRequest);

    return HospitalResponse.builder().mappingByEntity(hospital).build();
  }

  @Override
  public void delete(Long hospitalId) {
    hospitalRepository.findById(hospitalId).orElseThrow(hospitalNotFoundException());
    hospitalRepository.deleteById(hospitalId);
  }
}
