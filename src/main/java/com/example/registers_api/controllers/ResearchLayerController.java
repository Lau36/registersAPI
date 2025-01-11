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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ResearchLayer")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ResearchLayerController {

    private final ResearchLayerService researchLayerService;

    @Operation(
            summary = SwaggerConstants.CREATE_RESEARCH_LAYER,
            description = SwaggerConstants.CREATE_RESEARCH_LAYER_DESCRIPTION
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.RESEARCH_LAYER_CREATED, content = @Content),
            @ApiResponse(responseCode = "400", description = SwaggerConstants.EMPTY_FIELDS,
                    content = @Content),
            @ApiResponse(responseCode = "400", description = SwaggerConstants.RESEARCH_LAYER_NAME_ALREADY_EXISTS,
                    content = @Content),
            @ApiResponse(responseCode = "400", description = SwaggerConstants.RESEARCH_LAYER_NAME_TOO_LONG,
                    content = @Content),
            @ApiResponse(responseCode = "400", description = SwaggerConstants.RESEARCH_LAYER_DESCRIPTION_TOO_LONG,
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<BasicResponse> saveLayer(@RequestBody ResearchLayerDTO researchLayer) {
        BasicResponse response = new BasicResponse(Constants.RESEARCH_LAYER_CREATED);
        researchLayerService.saveResearchLayer(researchLayer);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = SwaggerConstants.GET_RESEARCH_LAYER,
            description = SwaggerConstants.GET_RESEARCH_LAYER_DESCRIPTION
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResearchLayerDTO.class))),
    })
    @GetMapping()
    public ResponseEntity<ResearchLayerDTO> getResearchLayerById(@RequestParam String id) {
        return ResponseEntity.ok(researchLayerService.getResearchLayerById(id));
    }

    @Operation(
            summary = SwaggerConstants.GET_RESEARCH_LAYERS,
            description = SwaggerConstants.GET_RESEARCH_LAYERS_DESCRIPTION
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResearchLayerDTO.class))),
    })
    @GetMapping("/GetAll")
    public ResponseEntity<List<ResearchLayerDTO>> getAllResearchLayers() {
        return ResponseEntity.ok(researchLayerService.getAllResearchLayers());
    }

}
