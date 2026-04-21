package com.app.restful.service;

import com.app.restful.domain.dto.CongestionDTO;
import com.app.restful.domain.dto.PetTourDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenApiServiceImpl implements OpenApiService {

    @Value("${api.base-url}")
    private String baseUrl;

    @Value("${api.area-based-list}")
    private String areaBasedList;

    @Value("${api.service-key}")
    private String serviceKey;

    @Value("${api.base-url2}")
    private String baseUrl2;

    @Value("${api.uddi-99771417}")
    private String uddi;

    private final RestTemplate restTemplate;

    // JSON 문자열 → JsonNode 변환 도구
    private final ObjectMapper objectMapper;

//    지하철 혼잡도 정보
    @Override
    public CongestionDTO fetchData2() throws URISyntaxException {
        String uriStr = UriComponentsBuilder
                .fromHttpUrl(baseUrl2)
                .path("/" + uddi)
                .queryParam("serviceKey", serviceKey)
                .queryParam("_type", "json")
                .queryParam("perPage", 10)
                .queryParam("page", 1)
                .build()
                .toUriString();

        URI uri = URI.create(uriStr);
        log.info("uri 경로: {}", uri);

//        이걸 사용 해야 함
        CongestionDTO res = restTemplate.getForObject(uri, CongestionDTO.class);
        return res;
    }

//    아래는 레거시 프로젝트 기존 내용임
    @Override
    public List<JsonNode> fetchData() throws JsonProcessingException {
        int numOfRows = 100;

        // 1) 1페이지 호출 → totalCount 파악
        String firstRaw = callApi(1, numOfRows);
        JsonNode firstBody = objectMapper.readTree(firstRaw)
                .path("response").path("body");

        int totalCount = firstBody.path("totalCount").asInt();
        int totalPages = (int) Math.ceil((double) totalCount / numOfRows);
        log.info("totalCount: {}, totalPages: {}", totalCount, totalPages);

        // 2) 결과를 담을 버퍼 역할의 리스트
        List<JsonNode> allItems = new ArrayList<>();
        collectItems(allItems, firstBody);

        // 3) 2페이지부터 마지막 페이지까지 순회
        for (int page = 2; page <= totalPages; page++) {
            String raw = callApi(page, numOfRows);
            JsonNode body = objectMapper.readTree(raw)
                    .path("response").path("body");
            collectItems(allItems, body);
        }

        log.info("수집 완료 - 총 {}건", allItems.size());
        return allItems;
    }

    @Override
    public List<JsonNode> fetchPage(int pageNo, int numOfRows) throws JsonProcessingException {
        String raw = callApi(pageNo, numOfRows);
        JsonNode body = objectMapper.readTree(raw)
                .path("response").path("body");

        int totalCount = body.path("totalCount").asInt();
        log.info("totalCount: {}, 요청 pageNo: {}, numOfRows: {}", totalCount, pageNo, numOfRows);

        List<JsonNode> items = new ArrayList<>();
        collectItems(items, body);
        return items;
    }

    private String callApi(int pageNo, int numOfRows) {
        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path("/" + areaBasedList)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "TestApp")
                .queryParam("_type", "json")
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .build()
                .toUriString();

        log.info("Request URL (page {}): {}", pageNo, url);
        // RestTemplate이 HTTP 연결·버퍼링·응답 읽기를 모두 처리함
        return restTemplate.getForObject(url, String.class);
    }

    @Override
    public List<PetTourDTO> fetchPetTour() throws JsonProcessingException {
        return toDto(this.fetchData());
    }

    @Override
    public List<PetTourDTO> fetchPetTourPage(int pageNo, int numOfRows) throws JsonProcessingException {
        return toDto(this.fetchPage(pageNo, numOfRows));
    }

    // List<JsonNode> → List<PetTourDTO> 변환 공통 로직
    private List<PetTourDTO> toDto(List<JsonNode> jsonList) {
        List<PetTourDTO> result = jsonList.stream()
                .map(node -> {
                    PetTourDTO dto = new PetTourDTO();
                    dto.setContentId(node.get("contentid") != null ? node.get("contentid").asText() : null);
                    dto.setAreaCode(node.get("areacode") != null ? node.get("areacode").asText() : null);
                    dto.setFirstImage(node.get("firstimage") != null ? node.get("firstimage").asText() : null);
                    dto.setFirstImage2(node.get("firstimage2") != null ? node.get("firstimage2").asText() : null);
                    dto.setTel(node.get("tel") != null ? node.get("tel").asText() : null);
                    dto.setTitle(node.get("title") != null ? node.get("title").asText() : null);
                    dto.setZipcode(node.get("zipcode") != null ? node.get("zipcode").asText() : null);
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());

        log.info("DTO 변환 완료 - 총 {}건", result.size());
        return result;
    }

    private void collectItems(List<JsonNode> list, JsonNode body) {
        JsonNode items = body.path("items").path("item");
        if (items.isArray()) {
            // 아이템이 여러 개인 경우
            items.forEach(list::add);
        } else if (!items.isMissingNode() && !items.isNull()) {
            // 아이템이 단 1개여서 객체로 내려오는 경우
            list.add(items);
        }
    }
}
