package me.kbh.clinicsolution.domain.patient.api;

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
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import me.kbh.clinicsolution.common.code.system.ClinicCategoryType;
import me.kbh.clinicsolution.common.code.system.ClinicSubjectType;
import me.kbh.clinicsolution.common.code.system.GenderType;
import me.kbh.clinicsolution.common.code.system.VisitStatusType;
import me.kbh.clinicsolution.common.dto.PageInfoWrapper;
import me.kbh.clinicsolution.doc.AbstractRestDocsTests;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalSaveRequest;
import me.kbh.clinicsolution.domain.hospital.entity.Hospital;
import me.kbh.clinicsolution.domain.patient.dto.PatientResponse;
import me.kbh.clinicsolution.domain.patient.dto.PatientSaveRequest;
import me.kbh.clinicsolution.domain.patient.dto.PatientSearchCondition;
import me.kbh.clinicsolution.domain.patient.dto.PatientTotalInfoResponse;
import me.kbh.clinicsolution.domain.patient.dto.PatientUpdateRequest;
import me.kbh.clinicsolution.domain.patient.entity.Patient;
import me.kbh.clinicsolution.domain.patient.exception.PatientBusinessException;
import me.kbh.clinicsolution.domain.patient.exception.code.PatientError;
import me.kbh.clinicsolution.domain.patient.service.PatientService;
import me.kbh.clinicsolution.domain.patient.util.PatientUtil;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitSaveRequest;
import me.kbh.clinicsolution.domain.patientvisit.entity.PatientVisit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Tag("rest-doc")
@DisplayName("환자 API 테스트")
@WebMvcTest(PatientController.class)
class PatientControllerTest extends AbstractRestDocsTests {

  @MockBean
  private PatientService patientService;
  ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  public static final String PATIENT_ID = "환자 아이디";
  public static final String PATIENT_NAME = "환자 명";
  public static final String PATIENT_REGISTRATION_NUMBER = "환자 등록번호";
  public static final String GENDER_CODE = "성별";
  public static final String BIRTH_DATE = "생년월일";
  public static final String PHONE_NUMBER = "휴대전화번호";
  public static final String HOSPITAL = "관련 병원";
  public static final String HOSPITAL_ID = "관련 병원 아이디";
  public static final String PATIENT_VISIT_RESPONSE_LIST = "내원 기록";
  public static final String PAGE_NO = "페이지 번호";
  public static final String PAGE_SIZE = "페이지 사이즈";

  @Test
  @Order(1)
  @DisplayName("단일 환자 정보 요청 (내원정보 포함)")
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

    Patient relatePatient = Patient.builderForSave()
        .patientSaveRequest(patientSaveRequest)
        .patientRegistrationNumber(patientRegistrationNumber)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientVisitSaveRequest patientVisitSaveRequest1 = PatientVisitSaveRequest.builder()
        .visitStatusType(VisitStatusType.VISIT_STATUS_ONE)
        .clinicSubjectType(ClinicSubjectType.CLINIC_SUBJECT_ONE)
        .clinicCategoryType(ClinicCategoryType.CLINIC_CATEGORY_D)
        .patientId(relatePatient.getPatientId())
        .hospitalId(relateHospital.getHospitalId())
        .build();

    PatientVisitSaveRequest patientVisitSaveRequest2 = PatientVisitSaveRequest.builder()
        .visitStatusType(VisitStatusType.VISIT_STATUS_TWO)
        .clinicSubjectType(ClinicSubjectType.CLINIC_SUBJECT_TWO)
        .clinicCategoryType(ClinicCategoryType.CLINIC_CATEGORY_T)
        .patientId(relatePatient.getPatientId())
        .hospitalId(relateHospital.getHospitalId())
        .build();

    PatientVisit patientVisit1 = PatientVisit.builderForSave()
        .patientSaveRequest(patientVisitSaveRequest1)
        .patient(relatePatient)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientVisit patientVisit2 = PatientVisit.builderForSave()
        .patientSaveRequest(patientVisitSaveRequest2)
        .patient(relatePatient)
        .hospital(relateHospital)
        .buildBySaveRequest();

    List<PatientVisit> patientVisitList = List.of(patientVisit1, patientVisit2);

    PatientTotalInfoResponse patientTotalInfoResponse = PatientTotalInfoResponse.builder()
        .mappingByEntity(relatePatient)
        .patientVisitList(patientVisitList)
        .build();

    ///when
    when(patientService.findById(1L)).thenReturn(patientTotalInfoResponse);

    //then
    List<FieldDescriptor> responseFieldDescriptorList = List.of(
        fieldWithPath("patientId").type(Long.class).description(PATIENT_ID),
        fieldWithPath("patientName").description(PATIENT_NAME),
        fieldWithPath("patientRegistrationNumber").description(PATIENT_REGISTRATION_NUMBER),
        fieldWithPath("genderCode").description(GENDER_CODE),
        fieldWithPath("birthDate").description(BIRTH_DATE),
        fieldWithPath("phoneNumber").description(PHONE_NUMBER),
        subsectionWithPath("hospital").description(HOSPITAL),
        subsectionWithPath("patientVisitResponseList").description(PATIENT_VISIT_RESPONSE_LIST)
            .optional()
    );
    mockMvc.perform(
            RestDocumentationRequestBuilders.get("/patient/{patientId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.patientName").value("환자1"))
        .andExpect(jsonPath("$.patientRegistrationNumber").value("P000000000001"))
        .andExpect(jsonPath("$.genderCode").value("남"))
        .andExpect(jsonPath("$.birthDate").value("1988-10-20"))
        .andExpect(jsonPath("$.phoneNumber").value("010-9999-9999"))
        .andExpect(jsonPath("$.hospital.hospitalId").doesNotExist())
        .andExpect(jsonPath("$.hospital.hospitalName").value("병원1"))
        .andExpect(jsonPath("$.hospital.medicalInstitutionNumber").value("기관번호1"))
        .andExpect(jsonPath("$.hospital.hospitalDirectorName").value("병원장1"))
        .andExpect(jsonPath("$.patientVisitResponseList").isArray())
        .andExpect(jsonPath("$.patientVisitResponseList[0].visitStatusCode").value("방문중"))
        .andExpect(jsonPath("$.patientVisitResponseList[0].clinicSubjectCode").value("내과"))
        .andExpect(jsonPath("$.patientVisitResponseList[0].clinicCategoryCode").value("약처방"))
        .andExpect(jsonPath("$.patientVisitResponseList[0].patient.patientName").value("환자1"))
        .andExpect(
            jsonPath("$.patientVisitResponseList[0].patient.patientRegistrationNumber").value(
                "P000000000001"))
        .andExpect(jsonPath("$.patientVisitResponseList[0].patient.genderCode").value("남"))
        .andExpect(jsonPath("$.patientVisitResponseList[0].patient.birthDate").value("1988-10-20"))
        .andExpect(
            jsonPath("$.patientVisitResponseList[0].patient.phoneNumber").value("010-9999-9999"))
        .andExpect(jsonPath("$.patientVisitResponseList[0].hospital.hospitalName").value("병원1"))
        .andExpect(
            jsonPath("$.patientVisitResponseList[0].hospital.medicalInstitutionNumber").value(
                "기관번호1"))
        .andExpect(
            jsonPath("$.patientVisitResponseList[0].hospital.hospitalDirectorName").value("병원장1"))
        .andExpect(jsonPath("$.patientVisitResponseList[1].visitStatusCode").value("종료"))
        .andExpect(jsonPath("$.patientVisitResponseList[1].clinicSubjectCode").value("안과"))
        .andExpect(jsonPath("$.patientVisitResponseList[1].clinicCategoryCode").value("검사"))
        .andExpect(jsonPath("$.patientVisitResponseList[1].patient.patientName").value("환자1"))
        .andExpect(
            jsonPath("$.patientVisitResponseList[1].patient.patientRegistrationNumber").value(
                "P000000000001"))
        .andExpect(jsonPath("$.patientVisitResponseList[1].patient.genderCode").value("남"))
        .andExpect(jsonPath("$.patientVisitResponseList[1].patient.birthDate").value("1988-10-20"))
        .andExpect(
            jsonPath("$.patientVisitResponseList[1].patient.phoneNumber").value("010-9999-9999"))
        .andExpect(jsonPath("$.patientVisitResponseList[1].hospital.hospitalName").value("병원1"))
        .andExpect(
            jsonPath("$.patientVisitResponseList[1].hospital.medicalInstitutionNumber").value(
                "기관번호1"))
        .andExpect(
            jsonPath("$.patientVisitResponseList[1].hospital.hospitalDirectorName").value("병원장1"))
        .andDo(document("{class-name}/{method-name}",
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("patientId").description(PATIENT_ID)),
                responseFields(responseFieldDescriptorList)
            )
        );
    verify(patientService, times(1)).findById(1L);
  }

  @Test
  @Order(2)
  @DisplayName("단일 환자 정보 요청 - [case: 존재하지 않는 경우]")
  void bySearchRequestId_patientNotFoundException() throws Exception {
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

    ///when
    when(patientService.findById(1L)).thenThrow(
        new PatientBusinessException(PatientError.NOT_FOUND));

    //then
    mockMvc.perform(
            RestDocumentationRequestBuilders.get("/patient/{patientId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.definitionCodeName").value(
            "me.kbh.clinicsolution.domain.patient.exception.code.PatientError")).
        andExpect(
            jsonPath("$.httpStatusCode").value(PatientError.NOT_FOUND.getHttpStatus().value())).
        andExpect(jsonPath("$.httpStatusType").value(String.valueOf(PatientError.NOT_FOUND))).
        andExpect(jsonPath("$.errorMessage").value(PatientError.NOT_FOUND.getMessage()))
        .andDo(
            document("{class-name}/{method-name}",
                preprocessResponse(prettyPrint()))
        );

    verify(patientService, times(1)).findById(1L);
  }

  @Test
  @Order(3)
  @DisplayName("환자 정보 전체 리스트 요청")
  void allByCondition() throws Exception {
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

    Patient relatePatient = Patient.builderForSave()
        .patientSaveRequest(patientSaveRequest)
        .patientRegistrationNumber(patientRegistrationNumber)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientSearchCondition patientSearchCondition = PatientSearchCondition.builder()
        .patientName("환자1")
        .patientRegistrationNumber("P000000000001")
        .birthDate(LocalDate.of(1988, 10, 20))
        .build();

    Pageable pageable = PageRequest.of(0, 10);

    List<Patient> patientList = List.of(relatePatient);
    Page<Patient> patientPage = new PageImpl<>(patientList, pageable, 1);

    Function<Patient, PatientResponse> mappingByEntityFunction = entity -> PatientResponse.builder()
        .mappingByEntity(entity)
        .build();

    List<PatientResponse> patientResponseList =
        patientPage.getContent().stream()
            .map(mappingByEntityFunction)
            .toList();

    PageInfoWrapper<PatientResponse> patientResponsePageInfoWrapper = new PageInfoWrapper<>(
        patientResponseList, patientPage);

    ///when
    when(patientService.findAllByCondition(any(PatientSearchCondition.class),
        any(Pageable.class))).thenReturn(
        patientResponsePageInfoWrapper);

    //then
    List<ParameterDescriptor> queryParameterDescriptorList = List.of(
        parameterWithName("patientName").description(PATIENT_NAME),
        parameterWithName("patientRegistrationNumber").description(PATIENT_REGISTRATION_NUMBER),
        parameterWithName("birthDate").description(BIRTH_DATE),
        parameterWithName("page").description(PAGE_NO),
        parameterWithName("size").description(PAGE_SIZE)
    );

    List<FieldDescriptor> responseFieldDescriptorList = List.of(
        subsectionWithPath("pageDataList").type(List.class)
            .description("페이지의 정보로 구성된 실제 데이터, 현 요청에서 환자정보"),
        subsectionWithPath("pageable").type(Pageable.class).description("현제 페이지 구성정보"),
        subsectionWithPath("totalCount").description("총 환자 수"),
        subsectionWithPath("totalPage").description("페이지 정보로 구성될 수 있는 전체 페이지 개수")
    );

    mockMvc.perform(
            RestDocumentationRequestBuilders.get("/patient/all/condition")
                .param("patientName", patientSearchCondition.getPatientName())
                .param("patientRegistrationNumber",
                    patientSearchCondition.getPatientRegistrationNumber())
                .param("birthDate", patientSearchCondition.getBirthDate().toString())
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.pageDataList[0].patientId").doesNotExist())
        .andExpect(jsonPath("$.pageDataList[0].patientName").value("환자1"))
        .andExpect(jsonPath("$.pageDataList[0].patientRegistrationNumber").value("P000000000001"))
        .andExpect(jsonPath("$.pageDataList[0].genderCode").value("남"))
        .andExpect(jsonPath("$.pageDataList[0].birthDate").value("1988-10-20"))
        .andExpect(jsonPath("$.pageDataList[0].phoneNumber").value("010-9999-9999"))
        .andExpect(jsonPath("$.pageDataList[0].hospital.hospitalId").doesNotExist())
        .andExpect(jsonPath("$.pageDataList[0].hospital.hospitalName").value("병원1"))
        .andExpect(jsonPath("$.pageDataList[0].hospital.medicalInstitutionNumber").value("기관번호1"))
        .andExpect(jsonPath("$.pageDataList[0].hospital.hospitalDirectorName").value("병원장1"))
        .andExpect(jsonPath("$.pageable.pageNumber").value(0))
        .andExpect(jsonPath("$.pageable.pageSize").value(10))
        .andExpect(jsonPath("$.pageable.sort.empty").value(true))
        .andExpect(jsonPath("$.pageable.sort.sorted").value(false))
        .andExpect(jsonPath("$.pageable.sort.unsorted").value(true))
        .andExpect(jsonPath("$.pageable.offset").value(0))
        .andExpect(jsonPath("$.pageable.paged").value(true))
        .andExpect(jsonPath("$.pageable.unpaged").value(false))
        .andExpect(jsonPath("$.totalCount").value(1))
        .andExpect(jsonPath("$.totalPage").value(1))
        .andDo(document("{class-name}/{method-name}",
            preprocessResponse(prettyPrint()),
            responseFields(responseFieldDescriptorList),
            queryParameters(queryParameterDescriptorList)
        ));
    verify(patientService, times(1)).findAllByCondition(any(PatientSearchCondition.class),
        any(Pageable.class));
  }

  @Test
  @Order(4)
  @DisplayName("환자 정보 전체 리스트 요청 - [case: 요청 값 형식 불일치]")
  void allByCondition_methodArgumentNotValidException() throws Exception {
    //given
    String testLongString = "병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병";

    PatientSearchCondition patientSearchCondition = PatientSearchCondition.builder()
        .patientName("환자1")
        .patientRegistrationNumber(testLongString)
        .birthDate(LocalDate.of(1988, 10, 20))
        .build();

    ///when
    when(patientService.findAllByCondition(any(PatientSearchCondition.class),
        any(PageRequest.class)))
        .thenThrow(RuntimeException.class);

    //then
    List<ParameterDescriptor> queryParameterDescriptorList = List.of(
        parameterWithName("patientName").description(PATIENT_NAME),
        parameterWithName("patientRegistrationNumber").description(PATIENT_REGISTRATION_NUMBER),
        parameterWithName("birthDate").description(BIRTH_DATE),
        parameterWithName("page").description(PAGE_NO),
        parameterWithName("size").description(PAGE_SIZE)
    );
    mockMvc.perform(
            MockMvcRequestBuilders.get("/patient/all/condition")
                .param("patientName", patientSearchCondition.getPatientName())
                .param("patientRegistrationNumber",
                    patientSearchCondition.getPatientRegistrationNumber())
                .param("birthDate", patientSearchCondition.getBirthDate().toString())
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.patientRegistrationNumber").value("환자 등록번호 형식에 맞지 않습니다."))
        .andDo(document(
            "{class-name}/{method-name}",
            preprocessResponse(prettyPrint()),
            queryParameters(queryParameterDescriptorList)
        ));

    verify(patientService, times(0)).findAllByCondition(any(PatientSearchCondition.class),
        any(PageRequest.class));
  }

  @Test
  @Order(5)
  @DisplayName("환자 정보 저장")
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

    Patient relatePatient = Patient.builderForSave()
        .patientSaveRequest(patientSaveRequest)
        .patientRegistrationNumber(patientRegistrationNumber)
        .hospital(relateHospital)
        .buildBySaveRequest();

    PatientResponse patientResponse = PatientResponse.builder()
        .mappingByEntity(relatePatient)
        .build();

    //when
    when(patientService.save(any(PatientSaveRequest.class))).thenReturn(patientResponse);

    //then
    List<FieldDescriptor> requestFieldsSnippet = List.of(
        fieldWithPath("patientName").description(PATIENT_NAME),
        fieldWithPath("genderType").description(GENDER_CODE),
        fieldWithPath("birthDate").description(BIRTH_DATE),
        fieldWithPath("phoneNumber").description(PHONE_NUMBER),
        fieldWithPath("hospitalId").description(HOSPITAL_ID)
    );

    List<FieldDescriptor> responseFieldsSnippet = List.of(
        fieldWithPath("patientId").description(PATIENT_ID),
        fieldWithPath("patientName").description(PATIENT_NAME),
        fieldWithPath("patientRegistrationNumber").description(PATIENT_REGISTRATION_NUMBER),
        fieldWithPath("genderCode").description(GENDER_CODE),
        fieldWithPath("birthDate").description(BIRTH_DATE),
        fieldWithPath("phoneNumber").description(PHONE_NUMBER),
        subsectionWithPath("hospital").description(HOSPITAL)
    );
    mockMvc.perform(
            MockMvcRequestBuilders.post("/patient")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientSaveRequest))
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.patientName").value("환자1"))
        .andExpect(jsonPath("$.patientRegistrationNumber").value("P000000000001"))
        .andExpect(jsonPath("$.genderCode").value("남"))
        .andExpect(jsonPath("$.birthDate").value("1988-10-20"))
        .andExpect(jsonPath("$.phoneNumber").value("010-9999-9999"))
        .andExpect(jsonPath("$.hospital.hospitalName").value("병원1"))
        .andExpect(jsonPath("$.hospital.medicalInstitutionNumber").value("기관번호1"))
        .andExpect(jsonPath("$.hospital.hospitalDirectorName").value("병원장1"))
        .andDo(document("{class-name}/{method-name}",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(requestFieldsSnippet),
            responseFields(responseFieldsSnippet)
        ));

    verify(patientService, times(1)).save(any(PatientSaveRequest.class));
  }

  @Test
  @Order(6)
  @DisplayName("환자 정보 저장 - [case: 요청 값 형식 불일치]")
  void bySaveRequest_methodArgumentNotValidException() throws Exception {
    //given
    PatientSaveRequest patientSaveRequest = PatientSaveRequest.builder()
        .birthDate(LocalDate.of(1988, 10, 20))
        .phoneNumber("010-9999-99999999999")
        .hospitalId(-1L)
        .build();

    //when
    when(patientService.save(any(PatientSaveRequest.class))).thenThrow(RuntimeException.class);

    //then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/patient")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientSaveRequest))
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.patientName").value("환자 이름은 필수 값입니다."))
        .andExpect(jsonPath("$.phoneNumber").value("휴대폰 번호 양식에 맞지 않습니다."))
        .andExpect(jsonPath("$.hospitalId").value("병원의 ID는 음수로 구성되어있지 않습니다."))
        .andExpect(jsonPath("$.genderType").value("성별 코드 값은 필수 값입니다."))
        .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint())));

    verify(patientService, times(0)).save(any(PatientSaveRequest.class));
  }

  @Test
  @Order(8)
  @DisplayName("환자 정보 저장 - [case: 병원 정보 미확인]")
  void bySaveRequestId_patientBusinessException() throws Exception {

    long notExistHospitalId = 100000L;

    PatientSaveRequest patientSaveRequest = PatientSaveRequest.builder()
        .patientName("환자1")
        .genderType(GenderType.GENDER_MAN)
        .birthDate(LocalDate.of(1988, 10, 20))
        .phoneNumber("010-9999-9999")
        .hospitalId(notExistHospitalId)
        .build();

    when(patientService.save(any(PatientSaveRequest.class))).thenThrow(
        new PatientBusinessException(PatientError.NOT_FOUND_HOSPITAL));

    mockMvc.perform(
            MockMvcRequestBuilders.post("/patient")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientSaveRequest))
        )
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.definitionCodeName").value(
            "me.kbh.clinicsolution.domain.patient.exception.code.PatientError"))
        .andExpect(
            jsonPath("$.httpStatusCode").value(
                PatientError.NOT_FOUND_HOSPITAL.getHttpStatus().value())).
        andExpect(jsonPath("$.httpStatusType").value(
            PatientError.NOT_FOUND_HOSPITAL.getHttpStatus().name())).
        andExpect(jsonPath("$.errorMessage").value(PatientError.NOT_FOUND_HOSPITAL.getMessage()))
        .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint())));
  }

  @Test
  @Order(9)
  @DisplayName("환자 정보 수정")
  void byUpdateRequest() throws Exception {
    //given
    HospitalSaveRequest hospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();

    Hospital saveHospital = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest)
        .buildBySaveRequest();

    saveHospital.increasePatientVisitCount();

    String patientRegistrationNumber = PatientUtil.generatePatientRegistrationNumber(
        saveHospital.getPatientVisitCount());

    PatientSaveRequest patientSaveRequest = PatientSaveRequest.builder()
        .patientName("환자1")
        .genderType(GenderType.GENDER_MAN)
        .birthDate(LocalDate.of(1988, 10, 20))
        .phoneNumber("010-9999-9999")
        .hospitalId(1L)
        .build();

    Patient relatePatient = Patient.builderForSave()
        .patientSaveRequest(patientSaveRequest)
        .patientRegistrationNumber(patientRegistrationNumber)
        .hospital(saveHospital)
        .buildBySaveRequest();

    HospitalSaveRequest anotherHospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName("병원2")
        .medicalInstitutionNumber("기관번호2")
        .hospitalDirectorName("병원장2")
        .build();

    Hospital anotherSaveHospital = Hospital.builderForSave()
        .hospitalSaveRequest(anotherHospitalSaveRequest)
        .buildBySaveRequest();

    anotherSaveHospital.increasePatientVisitCount();

    String anotherPatientRegistrationNumber = PatientUtil.generatePatientRegistrationNumber(
        saveHospital.getPatientVisitCount());

    PatientUpdateRequest patientUpdateRequest = PatientUpdateRequest.builder()
        .patientName("환자2")
        .genderType(GenderType.GENDER_FEMALE)
        .birthDate(LocalDate.of(1988, 11, 11))
        .phoneNumber("010-9999-0000")
        .hospitalId(2L)
        .build();

    relatePatient.update(patientUpdateRequest, anotherSaveHospital,
        anotherPatientRegistrationNumber);

    PatientResponse patientResponse = PatientResponse.builder().mappingByEntity(relatePatient)
        .build();

    //when
    when(patientService.update(eq(1L), any(PatientUpdateRequest.class))).thenReturn(
        patientResponse);

    //then
    List<FieldDescriptor> requestFieldsSnippet = List.of(
        fieldWithPath("patientName").description(PATIENT_NAME),
        fieldWithPath("genderType").description(GENDER_CODE),
        fieldWithPath("birthDate").description(BIRTH_DATE),
        fieldWithPath("phoneNumber").description(PHONE_NUMBER),
        fieldWithPath("hospitalId").description(HOSPITAL_ID)
    );

    List<FieldDescriptor> responseFieldsSnippet = List.of(
        fieldWithPath("patientId").description(PATIENT_ID),
        fieldWithPath("patientName").description(PATIENT_NAME),
        fieldWithPath("patientRegistrationNumber").description(PATIENT_REGISTRATION_NUMBER),
        fieldWithPath("genderCode").description(GENDER_CODE),
        fieldWithPath("birthDate").description(BIRTH_DATE),
        fieldWithPath("phoneNumber").description(PHONE_NUMBER),
        subsectionWithPath("hospital").description(HOSPITAL)
    );
    mockMvc.perform(
            MockMvcRequestBuilders.put("/patient/{patientId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientUpdateRequest))
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.patientName").value("환자2"))
        .andExpect(jsonPath("$.patientRegistrationNumber").value("P000000000001"))
        .andExpect(jsonPath("$.genderCode").value(GenderType.GENDER_FEMALE.getName()))
        .andExpect(jsonPath("$.birthDate").value("1988-11-11"))
        .andExpect(jsonPath("$.phoneNumber").value("010-9999-0000"))
        .andExpect(jsonPath("$.hospital.hospitalName").value("병원2"))
        .andExpect(jsonPath("$.hospital.medicalInstitutionNumber").value("기관번호2"))
        .andExpect(jsonPath("$.hospital.hospitalDirectorName").value("병원장2"))
        .andDo(document("{class-name}/{method-name}",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(requestFieldsSnippet),
            responseFields(responseFieldsSnippet)
        ));

    verify(patientService, times(1)).update(eq(1L), any(PatientUpdateRequest.class));
  }

  @Test
  @Order(10)
  @DisplayName("환자 정보 전체 리스트 요청 - [case: 요청 값 형식 불일치]")
  void byUpdateRequest_methodArgumentNotValidException() throws Exception {
    //given
    long wrongPatientId = -1L;

    PatientUpdateRequest patientUpdateRequest = PatientUpdateRequest.builder()
        .birthDate(LocalDate.of(1988, 10, 20))
        .phoneNumber("010-9999-99999999999")
        .hospitalId(wrongPatientId)
        .build();

    //when
    when(patientService.update(eq(1L), any(PatientUpdateRequest.class)))
        .thenThrow(RuntimeException.class);

    //then
    mockMvc.perform(
            MockMvcRequestBuilders.put("/patient/{patientId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientUpdateRequest))
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.patientName").value("환자 이름은 필수 값입니다."))
        .andExpect(jsonPath("$.phoneNumber").value("휴대폰 번호 양식에 맞지 않습니다."))
        .andExpect(jsonPath("$.hospitalId").value("병원의 ID는 음수로 구성되어있지 않습니다."))
        .andExpect(jsonPath("$.genderType").value("성별 코드 값은 필수 값입니다."))
        .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint())));

    verify(patientService, times(0)).update(eq(1L), any(PatientUpdateRequest.class));
  }

  @Test
  @Order(11)
  @DisplayName("환자 정보 전체 리스트 요청 - [case: 병원 정보 미확인]")
  void byUpdateRequestId_patientBusinessException() throws Exception {

    long notExistHospitalId = 100000L;

    PatientUpdateRequest patientUpdateRequest = PatientUpdateRequest.builder()
        .patientName("환자1")
        .genderType(GenderType.GENDER_MAN)
        .birthDate(LocalDate.of(1988, 10, 20))
        .phoneNumber("010-9999-9999")
        .hospitalId(notExistHospitalId)
        .build();

    when(patientService.update(any(Long.class), any(PatientUpdateRequest.class))).thenThrow(
        new PatientBusinessException(PatientError.NOT_FOUND_HOSPITAL));

    mockMvc.perform(
            MockMvcRequestBuilders.put("/patient/{patientId}", notExistHospitalId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientUpdateRequest))
        )
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.definitionCodeName").value(
            "me.kbh.clinicsolution.domain.patient.exception.code.PatientError"))
        .andExpect(
            jsonPath("$.httpStatusCode").value(
                PatientError.NOT_FOUND_HOSPITAL.getHttpStatus().value()))
        .andExpect(jsonPath("$.httpStatusType").value(
            PatientError.NOT_FOUND_HOSPITAL.getHttpStatus().name()))
        .andExpect(jsonPath("$.errorMessage").value(PatientError.NOT_FOUND_HOSPITAL.getMessage()))
        .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint())));
  }

  @Test
  @Order(12)
  @DisplayName("환자 정보 삭제")
  void byDeleteRequestId() throws Exception {
    Long deletePatientId = 1L;

    mockMvc.perform(delete("/patient/{patientId}", deletePatientId))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

    verify(patientService, times(1)).delete(1L);
  }

  @Test
  @Order(13)
  @DisplayName("환자 정보 삭제 - [case: 존재하지 않는 경우]")
  void byDeleteRequestId_patientBusinessException() throws Exception {
    Long nonExistingPatientId = 1L;

    doThrow(new PatientBusinessException(PatientError.NOT_FOUND))
        .when(patientService)
        .delete(nonExistingPatientId);

    mockMvc.perform(delete("/patient/{patientId}", nonExistingPatientId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.definitionCodeName").value(
            "me.kbh.clinicsolution.domain.patient.exception.code.PatientError"))
        .andExpect(
            jsonPath("$.httpStatusCode").value(PatientError.NOT_FOUND.getHttpStatus().value()))
        .andExpect(jsonPath("$.httpStatusType").value(String.valueOf(PatientError.NOT_FOUND)))
        .andExpect(jsonPath("$.errorMessage").value(PatientError.NOT_FOUND.getMessage()))
        .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint())));

    verify(patientService, times(1)).delete(1L);
  }
}