package com.example.assignmentpwc.controller;

import com.example.assignmentpwc.model.dto.LandRoute;
import com.example.assignmentpwc.service.LandRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

// URL versioning is important
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/v1/route-management")
public class LandRouteController {

    private final LandRouteService landPathService;

    @GetMapping("/routing/{origin}/{destination}")
    public ResponseEntity<LandRoute> findAnyLandRoute(@PathVariable @NotBlank final String origin,
                                                      @PathVariable @NotBlank final String destination) {
        LandRoute path = landPathService.findLandRouteBetween(origin, destination);
        return ResponseEntity.ok().body(path);
    }

}
