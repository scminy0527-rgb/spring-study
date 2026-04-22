package com.app.restful.service;

import com.app.restful.domain.dto.CongestionDTO;
import com.app.restful.domain.dto.PetTourDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface OpenApiService {
    // 전체 페이지 수집 (실제 데이터 저장용)
    List<JsonNode> fetchData() throws JsonProcessingException;

    // 단일 페이지 조회 (테스트·미리보기용)
    List<JsonNode> fetchPage(int pageNo, int numOfRows) throws JsonProcessingException;

    // 전체 페이지 DTO 수집 (실제 저장용)
    List<PetTourDTO> fetchPetTour() throws JsonProcessingException;

    // 단일 페이지 DTO 조회 (테스트·미리보기용)
    List<PetTourDTO> fetchPetTourPage(int pageNo, int numOfRows) throws JsonProcessingException;

//    오픈 api 패치 레거시 버전
    public List<PetTourDTO> fetchPetTourPage() throws IOException;


//    교통 혼잡도 서비스
    public CongestionDTO fetchData2() throws URISyntaxException;
}
