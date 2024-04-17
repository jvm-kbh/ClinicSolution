package me.kbh.clinicsolution.domain.hospital.api;

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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.kbh.clinicsolution.doc.AbstractRestDocsTests;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalResponse;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalSaveRequest;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalUpdateRequest;
import me.kbh.clinicsolution.domain.hospital.entity.Hospital;
import me.kbh.clinicsolution.domain.hospital.exception.HospitalBusinessException;
import me.kbh.clinicsolution.domain.hospital.exception.code.HospitalError;
import me.kbh.clinicsolution.domain.hospital.service.HospitalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Tag("rest-doc")
@DisplayName("병원 API 테스트")
@WebMvcTest(HospitalController.class)
class HospitalControllerTest extends AbstractRestDocsTests {

  @MockBean
  private HospitalService hospitalService;

  ObjectMapper objectMapper = new ObjectMapper();

  protected static final String HOSPITAL_ID = "병원 ID";
  protected static final String HOSPITAL_NAME = "병원 이름";
  protected static final String MEDICAL_INSTITUTION_NUMBER = "병원 기관번호";
  protected static final String HOSPITAL_DIRECTOR_NAME = "병원장 이름";

  @Test
  @Order(1)
  @DisplayName("하나의 병원정보 요청")
  void bySearchRequestId() throws Exception {
    // given
    HospitalSaveRequest request = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();

    Hospital hospital = Hospital.builderForSave()
        .hospitalSaveRequest(request)
        .buildBySaveRequest();

    HospitalResponse hospitalResponse
        = HospitalResponse.builder().mappingByEntity(hospital).build();

    // when
    when(hospitalService.findById(1L)).thenReturn(hospitalResponse);

    // then
    List<FieldDescriptor> responseFieldDescriptorList = List.of(
        fieldWithPath("hospitalId").type(Long.class).description(HOSPITAL_ID),
        fieldWithPath("hospitalName").description(HOSPITAL_NAME),
        fieldWithPath("medicalInstitutionNumber").description(
            MEDICAL_INSTITUTION_NUMBER),
        fieldWithPath("hospitalDirectorName").description(HOSPITAL_DIRECTOR_NAME)
    );

    mockMvc.perform(
            RestDocumentationRequestBuilders.get("/hospital/{hospitalId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.hospitalName").value("병원1"))
        .andExpect(jsonPath("$.medicalInstitutionNumber").value("기관번호1"))
        .andExpect(jsonPath("$.hospitalDirectorName").value("병원장1"))
        .andDo(document("{class-name}/{method-name}",
            preprocessResponse(prettyPrint()),
            pathParameters(parameterWithName("hospitalId").description("병원 아이디")),
            responseFields(responseFieldDescriptorList)
        ));

    verify(hospitalService, times(1)).findById(1L);
  }

  @Test
  @Order(2)
  @DisplayName("하나의 병원정보 요청 - [case: 존재하지 않는 경우]")
  void bySearchRequestId_HospitalNotFoundException() throws Exception {
    // given
    HospitalSaveRequest hospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();

    Hospital hospital = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest)
        .buildBySaveRequest();

    HospitalResponse hospitalResponse
        = HospitalResponse.builder().mappingByEntity(hospital).build();

    when(hospitalService.findById(1L)).thenReturn(hospitalResponse);
    when(hospitalService.findById(2L)).thenThrow(
        new HospitalBusinessException(HospitalError.NOT_FOUND));

    // then
    mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/hospital/{hospitalId}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()).
        andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).
        andExpect(jsonPath("$.definitionCodeName").value(
            "me.kbh.clinicsolution.domain.hospital.exception.code.HospitalError")).
        andExpect(
            jsonPath("$.httpStatusCode").value(HospitalError.NOT_FOUND.getHttpStatus().value())).
        andExpect(jsonPath("$.httpStatusType").value(String.valueOf(HospitalError.NOT_FOUND))).
        andExpect(jsonPath("$.errorMessage").value(HospitalError.NOT_FOUND.getMessage()))
        .andDo(
            document("{class-name}/{method-name}",
                preprocessResponse(prettyPrint()))
        );

    verify(hospitalService, times(1)).findById(2L);
  }

  @Test
  @Order(3)
  @DisplayName("병원 정보 전체 리스트 요청")
  void all() throws Exception {
    // given
    HospitalSaveRequest hospitalSaveRequest1 = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();
    Hospital hospital1 = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest1)
        .buildBySaveRequest();

    HospitalSaveRequest hospitalSaveRequest2 = HospitalSaveRequest.builder()
        .hospitalName("병원2")
        .medicalInstitutionNumber("기관번호2")
        .hospitalDirectorName("병원장2")
        .build();
    Hospital hospital2 = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest2)
        .buildBySaveRequest();

    List<HospitalResponse> hospitalResponseList = Arrays.asList(
        HospitalResponse.builder().mappingByEntity(hospital1).build(),
        HospitalResponse.builder().mappingByEntity(hospital2).build()
    );

    // when
    when(hospitalService.findAll()).thenReturn(hospitalResponseList);

    // then
    mockMvc.perform(
            MockMvcRequestBuilders.get("/hospital/all")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].hospitalName").value("병원1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].medicalInstitutionNumber").value("기관번호1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].hospitalDirectorName").value("병원장1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].hospitalName").value("병원2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].medicalInstitutionNumber").value("기관번호2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].hospitalDirectorName").value("병원장2"))
        .andDo(document("{class-name}/{method-name}",
            PayloadDocumentation.responseFields(
                fieldWithPath("[].hospitalId").type(Long.class).description(HOSPITAL_ID),
                fieldWithPath("[].hospitalName").description(HOSPITAL_NAME),
                fieldWithPath("[].medicalInstitutionNumber").description(
                    MEDICAL_INSTITUTION_NUMBER),
                fieldWithPath("[].hospitalDirectorName").description(
                    HOSPITAL_DIRECTOR_NAME)
            )
        ));

    verify(hospitalService, times(1)).findAll();
  }

  @Test
  @Order(4)
  @DisplayName("병원 정보 전체 리스트 요청 - [case: 존재하지 않는 경우]")
  void all_noResults() throws Exception {
    // when
    when(hospitalService.findAll()).thenReturn(Collections.emptyList());

    // then
    mockMvc.perform(
            MockMvcRequestBuilders.get("/hospital/all")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

    verify(hospitalService, times(1)).findAll();
  }

  @Test
  @Order(5)
  @DisplayName("병원 정보 저장")
  void bySaveRequest() throws Exception {
    // given
    HospitalSaveRequest hospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();

    Hospital hospital = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest)
        .buildBySaveRequest();

    HospitalResponse hospitalResponse = HospitalResponse.builder()
        .mappingByEntity(hospital)
        .build();

    // when
    when(hospitalService.save(any(HospitalSaveRequest.class))).thenReturn(hospitalResponse);

    // then
    List<FieldDescriptor> requestFieldsSnippet = List.of(
        fieldWithPath("hospitalName").type(JsonFieldType.STRING).description(HOSPITAL_NAME),
        fieldWithPath("medicalInstitutionNumber").type(JsonFieldType.STRING)
            .description(MEDICAL_INSTITUTION_NUMBER),
        fieldWithPath("hospitalDirectorName").type(JsonFieldType.STRING)
            .description(HOSPITAL_DIRECTOR_NAME)
    );

    List<FieldDescriptor> responseFieldsSnippet = List.of(
        fieldWithPath("hospitalId").type(Long.class).description(HOSPITAL_ID),
        fieldWithPath("hospitalName").description(HOSPITAL_NAME),
        fieldWithPath("medicalInstitutionNumber").description(
            MEDICAL_INSTITUTION_NUMBER),
        fieldWithPath("hospitalDirectorName").description(HOSPITAL_DIRECTOR_NAME)
    );

    mockMvc.perform(
            MockMvcRequestBuilders.post("/hospital")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hospitalSaveRequest))
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.hospitalName").value("병원1"))
        .andExpect(jsonPath("$.medicalInstitutionNumber").value("기관번호1"))
        .andExpect(jsonPath("$.hospitalDirectorName").value("병원장1"))
        .andDo(document("{class-name}/{method-name}",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(requestFieldsSnippet),
            responseFields(responseFieldsSnippet)
        ));

    verify(hospitalService, times(1)).save(any(HospitalSaveRequest.class));
  }

  @Test
  @Order(6)
  @DisplayName("병원 정보 저장 - [case: 각 요청 값이 긴 경우]")
  void bySaveRequest_MethodArgumentNotValidException() throws Exception {
    // given
    String testLongString = "병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병";
    HospitalSaveRequest hospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName(testLongString)
        .medicalInstitutionNumber(testLongString)
        .hospitalDirectorName(testLongString)
        .build();

    // when
    when(hospitalService.save(any(HospitalSaveRequest.class))).thenThrow(RuntimeException.class);

    mockMvc.perform(
            MockMvcRequestBuilders.post("/hospital")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hospitalSaveRequest))
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.hospitalDirectorName").value("병원장 이름의 최대 길이는 10입니다."))
        .andExpect(jsonPath("$.hospitalName").value("병원 이름의 최대 길이는 45입니다."))
        .andExpect(jsonPath("$.medicalInstitutionNumber").value("기관 번호의 최대 길이는 20입니다."))
        .andDo(document("{class-name}/{method-name}",
            preprocessResponse(prettyPrint())
        ));

    verify(hospitalService, times(0)).save(any(HospitalSaveRequest.class));
  }

  @Test
  @Order(7)
  @DisplayName("병원 정보 수정")
  void byUpdateRequest() throws Exception {
    // given
    HospitalSaveRequest hospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();

    Hospital hospital = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest)
        .buildBySaveRequest();

    HospitalUpdateRequest hospitalUpdateRequest = HospitalUpdateRequest.builder()
        .hospitalName("병원2")
        .medicalInstitutionNumber("기관번호2")
        .hospitalDirectorName("병원장2")
        .build();

    hospital.update(hospitalUpdateRequest);

    HospitalResponse updatedHospitalResponse = HospitalResponse.builder()
        .mappingByEntity(hospital)
        .build();

    // when
    when(hospitalService.update(eq(1L), any(HospitalUpdateRequest.class))).thenReturn(
        updatedHospitalResponse);

    // then
    List<FieldDescriptor> requestFieldsSnippet = List.of(
        fieldWithPath("hospitalName").type(JsonFieldType.STRING).description(HOSPITAL_NAME),
        fieldWithPath("medicalInstitutionNumber").type(JsonFieldType.STRING)
            .description(MEDICAL_INSTITUTION_NUMBER),
        fieldWithPath("hospitalDirectorName").type(JsonFieldType.STRING)
            .description(HOSPITAL_DIRECTOR_NAME)
    );

    List<FieldDescriptor> responseFieldsSnippet = List.of(
        fieldWithPath("hospitalId").type(Long.class).description(HOSPITAL_ID),
        fieldWithPath("hospitalName").description(HOSPITAL_NAME),
        fieldWithPath("medicalInstitutionNumber").description(
            MEDICAL_INSTITUTION_NUMBER),
        fieldWithPath("hospitalDirectorName").description(HOSPITAL_DIRECTOR_NAME)
    );

    mockMvc.perform(
            MockMvcRequestBuilders.put("/hospital/{hospitalId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hospitalUpdateRequest))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.hospitalName").value("병원2"))
        .andExpect(jsonPath("$.medicalInstitutionNumber").value("기관번호2"))
        .andExpect(jsonPath("$.hospitalDirectorName").value("병원장2"))
        .andDo(document("{class-name}/{method-name}",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(requestFieldsSnippet),
            responseFields(responseFieldsSnippet)
        ));

    verify(hospitalService, times(1)).update(eq(1L), any(HospitalUpdateRequest.class));
  }

  @Test
  @Order(8)
  @DisplayName("병원 정보 수정 - [case: 각 요청 값이 긴 경우]")
  void byUpdateRequest_MethodArgumentNotValidException() throws Exception {
    // given
    HospitalSaveRequest hospitalSaveRequest = HospitalSaveRequest.builder()
        .hospitalName("병원1")
        .medicalInstitutionNumber("기관번호1")
        .hospitalDirectorName("병원장1")
        .build();

    Hospital hospital = Hospital.builderForSave()
        .hospitalSaveRequest(hospitalSaveRequest)
        .buildBySaveRequest();

    String testLongString = "병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병병";

    HospitalUpdateRequest hospitalUpdateRequest = HospitalUpdateRequest.builder()
        .hospitalName(testLongString)
        .medicalInstitutionNumber(testLongString)
        .hospitalDirectorName(testLongString)
        .build();

    hospital.update(hospitalUpdateRequest);

    // when
    when(hospitalService.update(any(Long.class), any(HospitalUpdateRequest.class))).thenThrow(
        RuntimeException.class);

    mockMvc.perform(
            MockMvcRequestBuilders.put("/hospital/{hospitalId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hospitalUpdateRequest))
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.hospitalDirectorName").value("병원장 이름의 최대 길이는 10입니다."))
        .andExpect(jsonPath("$.hospitalName").value("병원 이름의 최대 길이는 45입니다."))
        .andExpect(jsonPath("$.medicalInstitutionNumber").value("기관 번호의 최대 길이는 20입니다."))
        .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint())));

    verify(hospitalService, times(0))
        .update(eq(1L), any(HospitalUpdateRequest.class));
  }

  @Test
  @Order(9)
  @DisplayName("병원 정보 삭제")
  void byDeleteRequestId() throws Exception {
    Long deleteHospitalId = 1L;

    mockMvc.perform(delete("/hospital/{hospitalId}", deleteHospitalId))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

    verify(hospitalService, times(1)).delete(1L);
  }

  @Test
  @Order(10)
  @DisplayName("병원 정보 삭제 - [case: 요청한 병원이 없는 경우")
  void byDeleteRequestId_HospitalBusinessException() throws Exception {
    Long nonExistingHospitalId = 1L;

    doThrow(new HospitalBusinessException(HospitalError.NOT_FOUND))
        .when(hospitalService)
        .delete(nonExistingHospitalId);

    mockMvc.perform(delete("/hospital/{hospitalId}", nonExistingHospitalId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.definitionCodeName").value(
            "me.kbh.clinicsolution.domain.hospital.exception.code.HospitalError"))
        .andExpect(
            jsonPath("$.httpStatusCode").value(HospitalError.NOT_FOUND.getHttpStatus().value()))
        .andExpect(jsonPath("$.httpStatusType").value(String.valueOf(HospitalError.NOT_FOUND)))
        .andExpect(jsonPath("$.errorMessage").value(HospitalError.NOT_FOUND.getMessage()));

    verify(hospitalService, times(1)).delete(1L);
  }
}