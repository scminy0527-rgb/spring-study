package com.app.restful.api;

import com.app.restful.domain.dto.ApiResponseDTO;
import com.app.restful.domain.dto.CongestionDTO;
import com.app.restful.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/congestions")
@RequiredArgsConstructor
public class CongestionAPI {

    private final OpenApiService openApiService;

    @GetMapping("")
    public ResponseEntity<ApiResponseDTO> getSubwayCongestions() throws URISyntaxException {
        CongestionDTO congestionDTO = openApiService.fetchData2();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("요청 성공", congestionDTO));
    }
}
