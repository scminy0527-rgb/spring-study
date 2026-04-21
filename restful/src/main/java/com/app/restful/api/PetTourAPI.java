package com.app.restful.api;

import com.app.restful.domain.dto.ApiResponseDTO;
import com.app.restful.domain.dto.PetTourDTO;
import com.app.restful.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pet-tours")
@RequiredArgsConstructor
public class PetTourAPI {

    private final OpenApiService openApiService;

    @GetMapping("")
    public ResponseEntity<ApiResponseDTO> getPetTourInto() throws Exception {
        List<PetTourDTO> petTours = openApiService.fetchPetTourPage(1, 10);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("값 불러오기 성공", petTours));
    }
}
