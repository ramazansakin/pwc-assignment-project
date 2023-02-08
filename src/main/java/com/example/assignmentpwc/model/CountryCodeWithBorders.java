package com.example.assignmentpwc.model;

import lombok.Data;

import java.util.List;

@Data
public class CountryCodeWithBorders {

    private String cca3;
    private List<String> borders;

}
