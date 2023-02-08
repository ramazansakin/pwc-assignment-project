package com.example.assignmentpwc.service;

import com.example.assignmentpwc.model.dto.LandRoute;

public interface LandRouteService {

    LandRoute findLandRouteBetween(String origin, String destination);

}
