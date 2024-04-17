package me.kbh.clinicsolution.domain.patientvisit.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import me.kbh.clinicsolution.common.code.system.ClinicCategoryType;
import me.kbh.clinicsolution.common.code.system.ClinicSubjectType;
import me.kbh.clinicsolution.common.code.system.GenderType;
import me.kbh.clinicsolution.common.code.system.VisitStatusType;
import me.kbh.clinicsolution.doc.AbstractRestDocsTests;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalSaveRequest;
import me.kbh.clinicsolution.domain.hospital.entity.Hospital;
import me.kbh.clinicsolution.domain.patient.dto.PatientSaveRequest;
import me.kbh.clinicsolution.domain.patient.entity.Patient;
import me.kbh.clinicsolution.domain.patient.util.PatientUtil;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitResponse;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitSaveRequest;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitUpdateRequest;
import me.kbh.clinicsolution.domain.patientvisit.entity.PatientVisit;
import me.kbh.clinicsolution.domain.patientvisit.exception.PatientVisitBusinessException;
import me.kbh.clinicsolution.domain.patientvisit.exception.code.PatientVisitError;
import me.kbh.clinicsolution.domain.patientvisit.service.PatientVisitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@Tag("rest-doc")
@DisplayName("환자방문(내원) API 테스트")
@WebMvcTest(PatientVisitController.class)
class PatientVisitControllerTest extends AbstractRestDocsTests {

  @MockBean
  private PatientVisitService patientVisitService;
  ObjectMapper objectMapper = new ObjectMapper();

  protected static final String PATIENT_VISIT_ID =  "환자 방문 아이디";
  protected static final String VISIT_STATUS_CODE =  "방문상태코드";
  protected static final String CLINIC_SUBJECT_CODE =  "진료과목코드";
  protected static final String CLINIC_CATEGORY_CODE =  "진료유형코드";
  protected static final String CREATE_AT =  "진료유형코드";
  protected static final String HOSPITAL =  "관련 병원";
  protected static final String PATIENT =  "관련 환자";
  protected static final String HOSPITAL_ID = "병원 ID";
  protected static final String HOSPITAL_NAME = "병원 이름";
  protected static final String MEDICAL_INSTITUTION_NUMBER = "병원 기관번호";
  protected static final String HOSPITAL_DIRECTOR_NAME = "병원장 이릅";

  public static final String PATIENT_ID = "환자 아이디";
  public static final String PATIENT_NAME = "환자 명";
  public static final String PATIENT_REGISTRATION_NUMBER = "환자 등록번호";
  public static final String GENDER_CODE = "성별";
  public static final String BIRTH_DATE = "생년월일";
  public static final String PHONE_NUMBER = "휴대전화번호";

  @Test
  @Order(1)
  @DisplayName("환자 방문 정보 요청")
  void bySearchRequestId() throws Exception {
    //given
    HospitalSaveRequest hospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();

    Hospital relateHospital = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest)
        .buildBySaveRequest();

    relateHospital.increasePatientVisitCount();

    String patientRegistrationNumber = PatientUtil.generatePatientRegistrationNumber(
        relateHospital.getPatientVisitCount());

    PatientSaveRequest patientSaveRequest = PatientSaveRequest.builder()
        .patientName("환자1")
        .genderType(GenderType.GENDER_MAN)
        .birthDate(LocalDate.of(1988, 10, 20))
        .phoneNumber("010-9999-9999")
        .hospitalId(1L)
        .build();

    Patient patient = Patient.builderForSave()
        .patientSaveRequest(patientSaveRequest)
        .patientRegistrationNumber(patientRegistrationNumber)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientVisitSaveRequest patientVisitSaveRequest = PatientVisitSaveRequest.builder()
        .visitStatusType(VisitStatusType.VISIT_STATUS_ONE)
        .clinicSubjectType(ClinicSubjectType.CLINIC_SUBJECT_ONE)
        .clinicCategoryType(ClinicCategoryType.CLINIC_CATEGORY_D)
        .patientId(patient.getPatientId())
        .hospitalId(relateHospital.getHospitalId())
        .build();

    PatientVisit patientVisit = PatientVisit.builderForSave()
        .patientSaveRequest(patientVisitSaveRequest)
        .patient(patient)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientVisitResponse patientVisitResponse = PatientVisitResponse.builder()
        .mappingByEntity(patientVisit)
        .build();

    //when
    when(patientVisitService.findById(1L)).thenReturn(patientVisitResponse);

    //then
    List<FieldDescriptor> responseFieldDescriptorList = List.of(
        fieldWithPath("patientVisitId").description("방문상태코드"),
        fieldWithPath("visitStatusCode").description("방문상태코드"),
        fieldWithPath("clinicSubjectCode").description("진료과목코드"),
        fieldWithPath("clinicCategoryCode").description("진료유형코드"),
        fieldWithPath("createAt").description("진료유형코드"),
        subsectionWithPath("hospital").description("관련 병원"),
        subsectionWithPath("patient").description("관련 환자")
    );

    mockMvc.perform(
        RestDocumentationRequestBuilders.get("/patient-visit/{patientVisitId}",1L)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.visitStatusCode").value("방문중"))
        .andExpect(jsonPath("$.clinicSubjectCode").value("내과"))
        .andExpect(jsonPath("$.clinicCategoryCode").value("약처방"))
        .andExpect(jsonPath("$.patient.patientName").value("환자1"))
        .andExpect(jsonPath("$.patient.patientRegistrationNumber").value("P000000000001"))
        .andExpect(jsonPath("$.patient.genderCode").value("남"))
        .andExpect(jsonPath("$.patient.birthDate").value("1988-10-20"))
        .andExpect(jsonPath("$.patient.phoneNumber").value("010-9999-9999"))
        .andExpect(jsonPath("$.hospital.hospitalName").value("병원1"))
        .andExpect(jsonPath("$.hospital.medicalInstitutionNumber").value("기관번호1"))
        .andExpect(jsonPath("$.hospital.hospitalDirectorName").value("병원장1"))
        .andDo(document("{class-name}/{method-name}",
            preprocessResponse(prettyPrint()),
            pathParameters(parameterWithName("patientVisitId").description("환자 방문 아이디")),
            responseFields(responseFieldDescriptorList)
        ));

    verify(patientVisitService, times(1)).findById(1L);
  }

  @Test
  @Order(2)
  @DisplayName("환자 방문 정보 요청 - [case: 존재하지 않는경우]")
  void bySearchRequestId_patientVisitNotFoundException() throws Exception {
    //given
    //when
    when(patientVisitService.findById(1L)).thenThrow(new PatientVisitBusinessException(
        PatientVisitError.NOT_FOUND));

    //then
    mockMvc.perform(
            RestDocumentationRequestBuilders.get("/patient-visit/{patientVisitId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.definitionCodeName").value(
            "me.kbh.clinicsolution.domain.patientvisit.exception.code.PatientVisitError")).
        andExpect(
            jsonPath("$.httpStatusCode").value(PatientVisitError.NOT_FOUND.getHttpStatus().value())).
        andExpect(jsonPath("$.httpStatusType").value(String.valueOf(PatientVisitError.NOT_FOUND))).
        andExpect(jsonPath("$.errorMessage").value(PatientVisitError.NOT_FOUND.getMessage()))
        .andDo(
            document("{class-name}/{method-name}",
                preprocessResponse(prettyPrint()))
        );

    verify(patientVisitService, times(1)).findById(1L);
  }

  @Test
  @Order(3)
  @DisplayName("환자 방문 정보 전체 리스트 요청")
  void all() throws Exception {

    HospitalSaveRequest hospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();

    Hospital relateHospital = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest)
        .buildBySaveRequest();

    relateHospital.increasePatientVisitCount();

    String patientRegistrationNumber = PatientUtil.generatePatientRegistrationNumber(
        relateHospital.getPatientVisitCount());

    PatientSaveRequest patientSaveRequest = PatientSaveRequest.builder()
        .patientName("환자1")
        .genderType(GenderType.GENDER_MAN)
        .birthDate(LocalDate.of(1988, 10, 20))
        .phoneNumber("010-9999-9999")
        .hospitalId(1L)
        .build();

    Patient patient = Patient.builderForSave()
        .patientSaveRequest(patientSaveRequest)
        .patientRegistrationNumber(patientRegistrationNumber)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientVisitSaveRequest patientVisitSaveRequest1 = PatientVisitSaveRequest.builder()
        .visitStatusType(VisitStatusType.VISIT_STATUS_ONE)
        .clinicSubjectType(ClinicSubjectType.CLINIC_SUBJECT_ONE)
        .clinicCategoryType(ClinicCategoryType.CLINIC_CATEGORY_D)
        .patientId(patient.getPatientId())
        .hospitalId(relateHospital.getHospitalId())
        .build();

    PatientVisitSaveRequest patientVisitSaveRequest2 = PatientVisitSaveRequest.builder()
        .visitStatusType(VisitStatusType.VISIT_STATUS_TWO)
        .clinicSubjectType(ClinicSubjectType.CLINIC_SUBJECT_TWO)
        .clinicCategoryType(ClinicCategoryType.CLINIC_CATEGORY_T)
        .patientId(patient.getPatientId())
        .hospitalId(relateHospital.getHospitalId())
        .build();

    PatientVisit patientVisit1 = PatientVisit.builderForSave()
        .patientSaveRequest(patientVisitSaveRequest1)
        .patient(patient)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientVisit patientVisit2 = PatientVisit.builderForSave()
        .patientSaveRequest(patientVisitSaveRequest2)
        .patient(patient)
        .hospital(relateHospital)
        .buildBySaveRequest();

    List<PatientVisit> patientVisitList = List.of(patientVisit1, patientVisit2);

    Function<PatientVisit, PatientVisitResponse> mappingByEntityFunction = entity -> PatientVisitResponse.builder()
        .mappingByEntity(entity)
        .build();

    List<PatientVisitResponse> patientVisitResponseList = patientVisitList
        .stream()
        .map(mappingByEntityFunction)
        .toList();

    //when
    when(patientVisitService.findAll()).thenReturn(patientVisitResponseList);

    //then
    mockMvc.perform(
            RestDocumentationRequestBuilders.get("/patient-visit/all")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].visitStatusCode").value("방문중"))
        .andExpect(jsonPath("$[0].clinicSubjectCode").value("내과"))
        .andExpect(jsonPath("$[0].clinicCategoryCode").value("약처방"))
        .andExpect(jsonPath("$[0].patient.patientName").value("환자1"))
        .andExpect(jsonPath("$[0].patient.patientRegistrationNumber").value("P000000000001"))
        .andExpect(jsonPath("$[0].patient.genderCode").value("남"))
        .andExpect(jsonPath("$[0].patient.birthDate").value("1988-10-20"))
        .andExpect(jsonPath("$[0].patient.phoneNumber").value("010-9999-9999"))
        .andExpect(jsonPath("$[0].hospital.hospitalName").value("병원1"))
        .andExpect(jsonPath("$[0].hospital.medicalInstitutionNumber").value("기관번호1"))
        .andExpect(jsonPath("$[0].hospital.hospitalDirectorName").value("병원장1"))
        .andDo(document("{class-name}/{method-name}",
            preprocessResponse(prettyPrint()),
            PayloadDocumentation.responseFields(
                fieldWithPath("[].patientVisitId").description("환자 방문 ID (nullable)"),
                fieldWithPath("[].visitStatusCode").description("환자 방문 상태 코드 ('방문중' 또는 '종료')"),
                fieldWithPath("[].clinicSubjectCode").description("진료 과목 코드"),
                fieldWithPath("[].clinicCategoryCode").description("진료 카테고리 코드"),
                fieldWithPath("[].hospital.hospitalId").type(Long.class).description("HOSPITAL_ID"),
                fieldWithPath("[].hospital.hospitalName").description("HOSPITAL_NAME"),
                fieldWithPath("[].hospital.medicalInstitutionNumber").description("MEDICAL_INSTITUTION_NUMBER"),
                fieldWithPath("[].hospital.hospitalDirectorName").description("HOSPITAL_DIRECTOR_NAME"),
                fieldWithPath("[].patient.patientId").description("환자 ID (nullable)"),
                fieldWithPath("[].patient.patientName").description("환자 이름"),
                fieldWithPath("[].patient.patientRegistrationNumber").description("환자 등록 번호"),
                fieldWithPath("[].patient.genderCode").description("환자 성별 코드"),
                fieldWithPath("[].patient.birthDate").description("환자 생년월일"),
                fieldWithPath("[].patient.phoneNumber").description("환자 전화번호"),
                fieldWithPath("[].patient.hospital.hospitalId").description("환자가 속한 병원 ID (nullable)"),
                fieldWithPath("[].patient.hospital.hospitalName").description("환자가 속한 병원 이름"),
                fieldWithPath("[].patient.hospital.medicalInstitutionNumber").description("환자가 속한 병원의 의료 기관 번호"),
                fieldWithPath("[].patient.hospital.hospitalDirectorName").description("환자가 속한 병원의 병원장 이름"),
                fieldWithPath("[].createAt").description("레코드 생성 시간 (nullable)")
            )
        ));

    verify(patientVisitService, times(1)).findAll();
  }

  @Test
  @Order(4)
  @DisplayName("환자 방문 정보 전체 리스트 요청 - [case: 존재하지 않는 경우]")
  void all_noResult() throws Exception {
    //when
    when(patientVisitService.findAll()).thenReturn(Collections.emptyList());

    //then
    mockMvc.perform(
            RestDocumentationRequestBuilders.get("/patient-visit/all")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

    verify(patientVisitService, times(1)).findAll();
  }
  @Test
  @Order(5)
  @DisplayName("환자 방문 정보 저장")
  void bySaveRequest() throws Exception {
    //given
    HospitalSaveRequest hospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();

    Hospital relateHospital = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest)
        .buildBySaveRequest();

    relateHospital.increasePatientVisitCount();

    String patientRegistrationNumber = PatientUtil.generatePatientRegistrationNumber(
        relateHospital.getPatientVisitCount());

    PatientSaveRequest patientSaveRequest = PatientSaveRequest.builder()
        .patientName("환자1")
        .genderType(GenderType.GENDER_MAN)
        .birthDate(LocalDate.of(1988, 10, 20))
        .phoneNumber("010-9999-9999")
        .hospitalId(1L)
        .build();

    Patient patient = Patient.builderForSave()
        .patientSaveRequest(patientSaveRequest)
        .patientRegistrationNumber(patientRegistrationNumber)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientVisitSaveRequest patientVisitSaveRequest = PatientVisitSaveRequest.builder()
        .visitStatusType(VisitStatusType.VISIT_STATUS_ONE)
        .clinicSubjectType(ClinicSubjectType.CLINIC_SUBJECT_ONE)
        .clinicCategoryType(ClinicCategoryType.CLINIC_CATEGORY_D)
        .patientId(1L)
        .hospitalId(1L)
        .build();

    PatientVisit patientVisit = PatientVisit.builderForSave()
        .patientSaveRequest(patientVisitSaveRequest)
        .patient(patient)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientVisitResponse patientVisitResponse = PatientVisitResponse.builder()
        .mappingByEntity(patientVisit)
        .build();

    //when
    when(patientVisitService.save(any(PatientVisitSaveRequest.class))).thenReturn(patientVisitResponse);

    //then
    List<FieldDescriptor> requestFieldsSnippet = List.of(
        fieldWithPath("visitStatusType").description(VISIT_STATUS_CODE),
        fieldWithPath("clinicSubjectType").description(CLINIC_SUBJECT_CODE),
        fieldWithPath("clinicCategoryType").description(CLINIC_CATEGORY_CODE),
        fieldWithPath("hospitalId").description(HOSPITAL_ID),
        fieldWithPath("patientId").description(PATIENT_ID)
    );

    List<FieldDescriptor> responseFieldDescriptorList = List.of(
        fieldWithPath("patientVisitId").description(PATIENT_VISIT_ID),
        fieldWithPath("visitStatusCode").description(VISIT_STATUS_CODE),
        fieldWithPath("clinicSubjectCode").description(CLINIC_SUBJECT_CODE),
        fieldWithPath("clinicCategoryCode").description(CLINIC_CATEGORY_CODE),
        fieldWithPath("createAt").description(CREATE_AT),
        subsectionWithPath("hospital").description(HOSPITAL),
        subsectionWithPath("patient").description(PATIENT)
    );

    mockMvc.perform(
            MockMvcRequestBuilders.post("/patient-visit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientVisitSaveRequest))
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.visitStatusCode").value("방문중"))
        .andExpect(jsonPath("$.clinicSubjectCode").value("내과"))
        .andExpect(jsonPath("$.clinicCategoryCode").value("약처방"))
        .andExpect(jsonPath("$.patient.patientName").value("환자1"))
        .andExpect(jsonPath("$.patient.patientRegistrationNumber").value("P000000000001"))
        .andExpect(jsonPath("$.patient.genderCode").value("남"))
        .andExpect(jsonPath("$.patient.birthDate").value("1988-10-20"))
        .andExpect(jsonPath("$.patient.phoneNumber").value("010-9999-9999"))
        .andExpect(jsonPath("$.hospital.hospitalName").value("병원1"))
        .andExpect(jsonPath("$.hospital.medicalInstitutionNumber").value("기관번호1"))
        .andExpect(jsonPath("$.hospital.hospitalDirectorName").value("병원장1"))
        .andDo(document("{class-name}/{method-name}",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(requestFieldsSnippet),
            responseFields(responseFieldDescriptorList)
        ));

    verify(patientVisitService, times(1)).save(any(PatientVisitSaveRequest.class));
  }

  @Test
  @Order(6)
  @DisplayName("환자 방문 정보 저장 - [case: 요청 값 형식 불일치")
  void bySaveRequest_methodArgumentNotValidException() throws Exception {
    //given
    PatientVisitSaveRequest patientVisitSaveRequest = PatientVisitSaveRequest.builder()
        .build();
    //when
    when(patientVisitService.save(any(PatientVisitSaveRequest.class))).thenThrow(RuntimeException.class);

    //then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/patient-visit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientVisitSaveRequest))
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.patientId").value("환자 ID는 필수 값입니다."))
        .andExpect(jsonPath("$.hospitalId").value("병원 ID는 필수 값입니다."))
        .andExpect(jsonPath("$.clinicSubjectType").value("진료 과목은 필수 값입니다."))
        .andExpect(jsonPath("$.clinicCategoryType").value("진료 유형은 필수 값입니다."))
        .andExpect(jsonPath("$.visitStatusType").value("방문 상태는 필수 값입니다."))
        .andDo(document("{class-name}/{method-name}",preprocessResponse(prettyPrint())));

    verify(patientVisitService, times(0)).save(any(PatientVisitSaveRequest.class));
  }

  @Test
  @Order(5)
  @DisplayName("환자 방문 정보 저장 - [case: 병원 정보 미확인]")
  void bySaveRequest_patientVisitBusinessException_notFoundHospital() throws Exception {
    //given
    HospitalSaveRequest hospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();

    Hospital relateHospital = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest)
        .buildBySaveRequest();

    relateHospital.increasePatientVisitCount();

    String patientRegistrationNumber = PatientUtil.generatePatientRegistrationNumber(
        relateHospital.getPatientVisitCount());

    PatientSaveRequest patientSaveRequest = PatientSaveRequest.builder()
        .patientName("환자1")
        .genderType(GenderType.GENDER_MAN)
        .birthDate(LocalDate.of(1988, 10, 20))
        .phoneNumber("010-9999-9999")
        .hospitalId(1L)
        .build();

    Patient patient = Patient.builderForSave()
        .patientSaveRequest(patientSaveRequest)
        .patientRegistrationNumber(patientRegistrationNumber)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientVisitSaveRequest patientVisitSaveRequest = PatientVisitSaveRequest.builder()
        .visitStatusType(VisitStatusType.VISIT_STATUS_ONE)
        .clinicSubjectType(ClinicSubjectType.CLINIC_SUBJECT_ONE)
        .clinicCategoryType(ClinicCategoryType.CLINIC_CATEGORY_D)
        .hospitalId(100000L)
        .patientId(1L)
        .build();

    //when
    when(patientVisitService.save(any(PatientVisitSaveRequest.class))).thenThrow(
        new PatientVisitBusinessException(PatientVisitError.NOT_FOUND_HOSPITAL));

    //then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/patient-visit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientVisitSaveRequest))
        )
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.definitionCodeName").value("me.kbh.clinicsolution.domain.patientvisit.exception.code.PatientVisitError"))
        .andExpect(jsonPath("$.httpStatusCode").value(PatientVisitError.NOT_FOUND_HOSPITAL.getHttpStatus().value()))
        .andExpect(jsonPath("$.httpStatusType").value(PatientVisitError.NOT_FOUND_HOSPITAL.getHttpStatus().name()))
        .andExpect(jsonPath("$.errorMessage").value(PatientVisitError.NOT_FOUND_HOSPITAL.getMessage()))
        .andDo(document("{class-name}/{method-name}",preprocessResponse(prettyPrint())
        ));

    verify(patientVisitService, times(1)).save(any(PatientVisitSaveRequest.class));
  }
  @Test
  @Order(5)
  @DisplayName("환자 방문 정보 저장 - [case: 환자 정보 미확인]")
  void bySaveRequest_patientVisitBusinessException_notFoundPatient() throws Exception {
    //given
    HospitalSaveRequest hospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();

    Hospital relateHospital = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest)
        .buildBySaveRequest();

    relateHospital.increasePatientVisitCount();

    String patientRegistrationNumber = PatientUtil.generatePatientRegistrationNumber(
        relateHospital.getPatientVisitCount());

    PatientSaveRequest patientSaveRequest = PatientSaveRequest.builder()
        .patientName("환자1")
        .genderType(GenderType.GENDER_MAN)
        .birthDate(LocalDate.of(1988, 10, 20))
        .phoneNumber("010-9999-9999")
        .hospitalId(1L)
        .build();

    Patient patient = Patient.builderForSave()
        .patientSaveRequest(patientSaveRequest)
        .patientRegistrationNumber(patientRegistrationNumber)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientVisitSaveRequest patientVisitSaveRequest = PatientVisitSaveRequest.builder()
        .visitStatusType(VisitStatusType.VISIT_STATUS_ONE)
        .clinicSubjectType(ClinicSubjectType.CLINIC_SUBJECT_ONE)
        .clinicCategoryType(ClinicCategoryType.CLINIC_CATEGORY_D)
        .hospitalId(1L)
        .patientId(1L)
        .build();

    //when
    when(patientVisitService.save(any(PatientVisitSaveRequest.class))).thenThrow(
        new PatientVisitBusinessException(PatientVisitError.NOT_FOUND_PATIENT));

    //then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/patient-visit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientVisitSaveRequest))
        )
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.definitionCodeName").value("me.kbh.clinicsolution.domain.patientvisit.exception.code.PatientVisitError"))
        .andExpect(jsonPath("$.httpStatusCode").value(PatientVisitError.NOT_FOUND_PATIENT.getHttpStatus().value()))
        .andExpect(jsonPath("$.httpStatusType").value(PatientVisitError.NOT_FOUND_PATIENT.getHttpStatus().name()))
        .andExpect(jsonPath("$.errorMessage").value(PatientVisitError.NOT_FOUND_PATIENT.getMessage()))
        .andDo(document("{class-name}/{method-name}",preprocessResponse(prettyPrint())
        ));

    verify(patientVisitService, times(1)).save(any(PatientVisitSaveRequest.class));
  }
  @Test
  @Order(6)
  @DisplayName("환자 방문 정보 수정")
  void byUpdateRequest() throws Exception {
    //given
    HospitalSaveRequest hospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();

    Hospital relateHospital = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest)
        .buildBySaveRequest();

    relateHospital.increasePatientVisitCount();

    String patientRegistrationNumber = PatientUtil.generatePatientRegistrationNumber(
        relateHospital.getPatientVisitCount());

    PatientSaveRequest patientSaveRequest = PatientSaveRequest.builder()
        .patientName("환자1")
        .genderType(GenderType.GENDER_MAN)
        .birthDate(LocalDate.of(1988, 10, 20))
        .phoneNumber("010-9999-9999")
        .hospitalId(1L)
        .build();

    Patient patient = Patient.builderForSave()
        .patientSaveRequest(patientSaveRequest)
        .patientRegistrationNumber(patientRegistrationNumber)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientVisitSaveRequest patientVisitSaveRequest = PatientVisitSaveRequest.builder()
        .visitStatusType(VisitStatusType.VISIT_STATUS_ONE)
        .clinicSubjectType(ClinicSubjectType.CLINIC_SUBJECT_ONE)
        .clinicCategoryType(ClinicCategoryType.CLINIC_CATEGORY_D)
        .patientId(1L)
        .hospitalId(1L)
        .build();

    PatientVisit patientVisit = PatientVisit.builderForSave()
        .patientSaveRequest(patientVisitSaveRequest)
        .patient(patient)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientVisitUpdateRequest patientVisitUpdateRequest = PatientVisitUpdateRequest.builder()
        .visitStatusType(VisitStatusType.VISIT_STATUS_TWO)
        .clinicSubjectType(ClinicSubjectType.CLINIC_SUBJECT_TWO)
        .clinicCategoryType(ClinicCategoryType.CLINIC_CATEGORY_T)
        .patientId(2L)
        .hospitalId(2L)
        .build();

    patientVisit.update(patientVisitUpdateRequest,relateHospital, patient);

    PatientVisitResponse patientVisitResponse = PatientVisitResponse.builder()
        .mappingByEntity(patientVisit)
        .build();

    //when
    when(patientVisitService.update(eq(1L), any(PatientVisitUpdateRequest.class)))
        .thenReturn(patientVisitResponse);

    //then
    List<FieldDescriptor> requestFieldsSnippet = List.of(
        fieldWithPath("visitStatusType").description(VISIT_STATUS_CODE),
        fieldWithPath("clinicSubjectType").description(CLINIC_SUBJECT_CODE),
        fieldWithPath("clinicCategoryType").description(CLINIC_CATEGORY_CODE),
        fieldWithPath("hospitalId").description(HOSPITAL_ID),
        fieldWithPath("patientId").description(PATIENT_ID)
    );

    List<FieldDescriptor> responseFieldDescriptorList = List.of(
        fieldWithPath("patientVisitId").description(PATIENT_VISIT_ID),
        fieldWithPath("visitStatusCode").description(VISIT_STATUS_CODE),
        fieldWithPath("clinicSubjectCode").description(CLINIC_SUBJECT_CODE),
        fieldWithPath("clinicCategoryCode").description(CLINIC_CATEGORY_CODE),
        fieldWithPath("createAt").description(CREATE_AT),
        subsectionWithPath("hospital").description(HOSPITAL),
        subsectionWithPath("patient").description(PATIENT)
    );

    mockMvc.perform(
            MockMvcRequestBuilders.put("/patient-visit/{patientVisitId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientVisitSaveRequest))
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.visitStatusCode").value("종료"))
        .andExpect(jsonPath("$.clinicSubjectCode").value("안과"))
        .andExpect(jsonPath("$.clinicCategoryCode").value("검사"))
        .andExpect(jsonPath("$.patient.patientName").value("환자1"))
        .andExpect(jsonPath("$.patient.patientRegistrationNumber").value("P000000000001"))
        .andExpect(jsonPath("$.patient.genderCode").value("남"))
        .andExpect(jsonPath("$.patient.birthDate").value("1988-10-20"))
        .andExpect(jsonPath("$.patient.phoneNumber").value("010-9999-9999"))
        .andExpect(jsonPath("$.hospital.hospitalName").value("병원1"))
        .andExpect(jsonPath("$.hospital.medicalInstitutionNumber").value("기관번호1"))
        .andExpect(jsonPath("$.hospital.hospitalDirectorName").value("병원장1"))
        .andDo(document("{class-name}/{method-name}",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(requestFieldsSnippet),
            responseFields(responseFieldDescriptorList)
        ));

    verify(patientVisitService, times(1)).update(eq(1L),any(PatientVisitUpdateRequest.class));
  }

  @Test
  @Order(7)
  @DisplayName("환자 방문 정보 수정 - [case: 요청 값 형식 불일치")
  void byUpdateRequest_methodArgumentNotValidException() throws Exception {
    //given
    PatientVisitUpdateRequest patientVisitUpdateRequest = PatientVisitUpdateRequest.builder().build();

    //when
    when(patientVisitService.update(eq(1L), any(PatientVisitUpdateRequest.class))).thenThrow(RuntimeException.class);

    //then

    mockMvc.perform(
            MockMvcRequestBuilders.put("/patient-visit/{patientVisitId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientVisitUpdateRequest))
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.patientId").value("환자 ID는 필수 값입니다."))
        .andExpect(jsonPath("$.hospitalId").value("병원 ID는 필수 값입니다."))
        .andExpect(jsonPath("$.clinicSubjectType").value("진료 과목은 필수 값입니다."))
        .andExpect(jsonPath("$.clinicCategoryType").value("진료 유형은 필수 값입니다."))
        .andExpect(jsonPath("$.visitStatusType").value("방문 상태는 필수 값입니다."))
        .andDo(document("{class-name}/{method-name}",preprocessResponse(prettyPrint())));

    verify(patientVisitService, times(0)).update(eq(1L),any(PatientVisitUpdateRequest.class));
  }

  @Test
  @Order(8)
  @DisplayName("환자 방문 정보 수정 - [case: 병원 정보 미확인]")
  void byUpdateRequest_patientVisitBusinessException_notFoundHospital() throws Exception {
    //given
    HospitalSaveRequest hospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();

    Hospital relateHospital = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest)
        .buildBySaveRequest();

    relateHospital.increasePatientVisitCount();

    String patientRegistrationNumber = PatientUtil.generatePatientRegistrationNumber(
        relateHospital.getPatientVisitCount());

    PatientSaveRequest patientSaveRequest = PatientSaveRequest.builder()
        .patientName("환자1")
        .genderType(GenderType.GENDER_MAN)
        .birthDate(LocalDate.of(1988, 10, 20))
        .phoneNumber("010-9999-9999")
        .hospitalId(1L)
        .build();

    Patient patient = Patient.builderForSave()
        .patientSaveRequest(patientSaveRequest)
        .patientRegistrationNumber(patientRegistrationNumber)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientVisitSaveRequest patientVisitSaveRequest = PatientVisitSaveRequest.builder()
        .visitStatusType(VisitStatusType.VISIT_STATUS_ONE)
        .clinicSubjectType(ClinicSubjectType.CLINIC_SUBJECT_ONE)
        .clinicCategoryType(ClinicCategoryType.CLINIC_CATEGORY_D)
        .patientId(1L)
        .hospitalId(1L)
        .build();

    PatientVisit patientVisit = PatientVisit.builderForSave()
        .patientSaveRequest(patientVisitSaveRequest)
        .patient(patient)
        .hospital(relateHospital)
        .buildBySaveRequest();

    long notExistPatientId = 10000L;

    PatientVisitUpdateRequest patientVisitUpdateRequest = PatientVisitUpdateRequest.builder()
        .visitStatusType(VisitStatusType.VISIT_STATUS_TWO)
        .clinicSubjectType(ClinicSubjectType.CLINIC_SUBJECT_TWO)
        .clinicCategoryType(ClinicCategoryType.CLINIC_CATEGORY_T)
        .patientId(notExistPatientId)
        .hospitalId(2L)
        .build();

    patientVisit.update(patientVisitUpdateRequest, relateHospital, patient);

    PatientVisitResponse patientVisitResponse = PatientVisitResponse.builder()
        .mappingByEntity(patientVisit)
        .build();

    //when
    when(patientVisitService.update(eq(1L), any(PatientVisitUpdateRequest.class)))
        .thenThrow(new PatientVisitBusinessException(PatientVisitError.NOT_FOUND_PATIENT));

    //then
    mockMvc.perform(
            MockMvcRequestBuilders.put("/patient-visit/{patientVisitId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientVisitSaveRequest))
        )
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.definitionCodeName").value("me.kbh.clinicsolution.domain.patientvisit.exception.code.PatientVisitError"))
        .andExpect(jsonPath("$.httpStatusCode").value(PatientVisitError.NOT_FOUND_PATIENT.getHttpStatus().value()))
        .andExpect(jsonPath("$.httpStatusType").value(PatientVisitError.NOT_FOUND_PATIENT.getHttpStatus().name()))
        .andExpect(jsonPath("$.errorMessage").value(PatientVisitError.NOT_FOUND_PATIENT.getMessage()))
        .andDo(document("{class-name}/{method-name}",preprocessResponse(prettyPrint())
        ));

    verify(patientVisitService, times(1)).update(eq(1L),any(PatientVisitUpdateRequest.class));
  }

  @Test
  @Order(9)
  @DisplayName("환자 방문 정보 수정 - [case: 환자 정보 미확인]")
  void byUpdateRequest_patientVisitBusinessException_notFoundPatient() throws Exception {
    //given
    HospitalSaveRequest hospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();

    Hospital relateHospital = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest)
        .buildBySaveRequest();

    relateHospital.increasePatientVisitCount();

    String patientRegistrationNumber = PatientUtil.generatePatientRegistrationNumber(
        relateHospital.getPatientVisitCount());

    PatientSaveRequest patientSaveRequest = PatientSaveRequest.builder()
        .patientName("환자1")
        .genderType(GenderType.GENDER_MAN)
        .birthDate(LocalDate.of(1988, 10, 20))
        .phoneNumber("010-9999-9999")
        .hospitalId(1L)
        .build();

    Patient patient = Patient.builderForSave()
        .patientSaveRequest(patientSaveRequest)
        .patientRegistrationNumber(patientRegistrationNumber)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientVisitSaveRequest patientVisitSaveRequest = PatientVisitSaveRequest.builder()
        .visitStatusType(VisitStatusType.VISIT_STATUS_ONE)
        .clinicSubjectType(ClinicSubjectType.CLINIC_SUBJECT_ONE)
        .clinicCategoryType(ClinicCategoryType.CLINIC_CATEGORY_D)
        .patientId(1L)
        .hospitalId(1L)
        .build();

    PatientVisit patientVisit = PatientVisit.builderForSave()
        .patientSaveRequest(patientVisitSaveRequest)
        .patient(patient)
        .hospital(relateHospital)
        .buildBySaveRequest();

    long notExistHospitalId = 10000L;

    PatientVisitUpdateRequest patientVisitUpdateRequest = PatientVisitUpdateRequest.builder()
        .visitStatusType(VisitStatusType.VISIT_STATUS_TWO)
        .clinicSubjectType(ClinicSubjectType.CLINIC_SUBJECT_TWO)
        .clinicCategoryType(ClinicCategoryType.CLINIC_CATEGORY_T)
        .patientId(1L)
        .hospitalId(notExistHospitalId)
        .build();

    patientVisit.update(patientVisitUpdateRequest, relateHospital, patient);

    PatientVisitResponse patientVisitResponse = PatientVisitResponse.builder()
        .mappingByEntity(patientVisit)
        .build();

    //when
    when(patientVisitService.update(eq(1L), any(PatientVisitUpdateRequest.class)))
        .thenThrow(new PatientVisitBusinessException(PatientVisitError.NOT_FOUND_HOSPITAL));

    //then
    mockMvc.perform(
            MockMvcRequestBuilders.put("/patient-visit/{patientVisitId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientVisitSaveRequest))
        )
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.definitionCodeName").value("me.kbh.clinicsolution.domain.patientvisit.exception.code.PatientVisitError"))
        .andExpect(jsonPath("$.httpStatusCode").value(PatientVisitError.NOT_FOUND_HOSPITAL.getHttpStatus().value()))
        .andExpect(jsonPath("$.httpStatusType").value(PatientVisitError.NOT_FOUND_HOSPITAL.getHttpStatus().name()))
        .andExpect(jsonPath("$.errorMessage").value(PatientVisitError.NOT_FOUND_HOSPITAL.getMessage()))
        .andDo(document("{class-name}/{method-name}",preprocessResponse(prettyPrint())
        ));

    verify(patientVisitService, times(1)).update(eq(1L),any(PatientVisitUpdateRequest.class));
  }

  @Test
  @Order(10)
  @DisplayName("환자 방문 정보 삭제")
  void byDeleteRequestId() throws Exception {
    //given
    //when
    Long deletePatientVisitId = 1L;

    //then
    mockMvc.perform(delete("/patient-visit/{patientId}", deletePatientVisitId))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  @Order(11)
  @DisplayName("환자 방문 정보 삭제 - [case: 존재하지 않는 경우]")
  void byDeleteRequestId_patientVisitBusinessException() throws Exception {
    //given
    Long nonExistingPatientVisitId = 1L;

    //when
    doThrow(new PatientVisitBusinessException(PatientVisitError.NOT_FOUND))
        .when(patientVisitService)
        .delete(nonExistingPatientVisitId);

    //then
    mockMvc.perform(delete("/patient-visit/{patientVisitId}",nonExistingPatientVisitId))
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.definitionCodeName").value(
            "me.kbh.clinicsolution.domain.patientvisit.exception.code.PatientVisitError")).
        andExpect(
            jsonPath("$.httpStatusCode").value(PatientVisitError.NOT_FOUND.getHttpStatus().value())).
        andExpect(jsonPath("$.httpStatusType").value(String.valueOf(PatientVisitError.NOT_FOUND))).
        andExpect(jsonPath("$.errorMessage").value(PatientVisitError.NOT_FOUND.getMessage()))
        .andDo(
            document("{class-name}/{method-name}",
                preprocessResponse(prettyPrint()))
        );

    verify(patientVisitService, times(1)).delete(1L);
  }
}