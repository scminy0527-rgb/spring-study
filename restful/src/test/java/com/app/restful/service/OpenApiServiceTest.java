package com.app.restful.service;

import com.app.restful.domain.dto.PetTourDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class OpenApiServiceTest {

    @Autowired
    private OpenApiService openApiService;

    // 1페이지 10건만 가져와서 응답 구조 확인 (빠른 검증용)
    @Test
    void fetchPage() throws Exception {
        List<JsonNode> result = openApiService.fetchPage(1, 10);

        log.info("수신 건수: {}", result.size());
        log.info("첫 번째 아이템:\n{}", result.isEmpty() ? "없음" : result.get(0).toPrettyString());

        assertThat(result).isNotEmpty();
    }

    // 1페이지 10건만 DTO로 변환 확인 (빠른 검증용)
    @Test
    void testFetchPetTour() throws JsonProcessingException {
        List<PetTourDTO> result = openApiService.fetchPetTourPage(1, 10);
        log.info("수신 건수: {}", result.size());
        log.info("첫 번째 DTO: {}", result.isEmpty() ? "없음" : result);
    }

    // 전체 데이터 수집 - 시간이 오래 걸리므로 필요할 때만 실행
    // @Test
    void fetchAllData() throws Exception {
        List<JsonNode> result = openApiService.fetchData();
        log.info("전체 수집 건수: {}", result.size());
        assertThat(result).isNotEmpty();
    }

//    혼잡도 테스트
    @Test
    void testFetchData2() throws URISyntaxException {
        log.info("정보: {}", openApiService.fetchData2());
    }
}
