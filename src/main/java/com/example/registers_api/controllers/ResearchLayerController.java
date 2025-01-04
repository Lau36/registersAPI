package com.example.registers_api.controllers;

import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.response.BasicResponse;
import com.example.registers_api.services.ResearchLayerService;
import com.example.registers_api.utils.Constants;
import com.example.registers_api.utils.SwaggerConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ResearchLayer")
@RequiredArgsConstructor
public class ResearchLayerController {

    private final ResearchLayerService researchLayerService;

    @Operation(
            summary = SwaggerConstants.CREATE_RESEARCH_LAYER,
            description = SwaggerConstants.CREATE_RESEARCH_LAYER_DESCRIPTION
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.RESEARCH_LAYER_CREATED,
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResearchLayerDTO.class)) }),
            @ApiResponse(responseCode = "400", description = SwaggerConstants.EMPTY_FIELDS,
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<BasicResponse> saveLayer(ResearchLayerDTO researchLayer) {
        BasicResponse response = new BasicResponse(Constants.RESEARCH_LAYER_CREATED);
        researchLayerService.saveResearchLayer(researchLayer);
        return ResponseEntity.ok(response);
    }
}
