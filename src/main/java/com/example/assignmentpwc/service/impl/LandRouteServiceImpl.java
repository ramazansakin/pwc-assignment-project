package com.example.assignmentpwc.service.impl;

import com.example.assignmentpwc.exception.CountryNotFoundException;
import com.example.assignmentpwc.exception.NoPossibleRouteException;
import com.example.assignmentpwc.model.CountryCodeWithBorders;
import com.example.assignmentpwc.model.dto.LandRoute;
import com.example.assignmentpwc.service.LandRouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class LandRouteServiceImpl implements LandRouteService {

    private final Map<String, List> countryCodeWithBordersCache;

    private final RestTemplate restTemplate;
    private static final String COUNTRIES_INFO_URL = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json";

    public LandRouteServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.countryCodeWithBordersCache = new HashMap<>();
        // We can construct the countries with borders map and cache it
        // to prevent calling the static content api every time.
        // If the data is changeable, we can check the changes time to time
        constructCountriesWithBordersMap();
    }

    @Override
    public LandRoute findLandRouteBetween(final String origin, final String destination) {
        checkCountriesValid(origin, destination);
        List<String> route = findRoutesBFS(origin, destination);
        if (!route.isEmpty()) return new LandRoute(route);
        throw new NoPossibleRouteException(origin, destination);
    }

    private void checkCountriesValid(final String origin, final String destination) {
        isValidCountry(origin);
        isValidCountry(destination);
    }

    private void isValidCountry(String origin) {
        if (!countryCodeWithBordersCache.containsKey(origin))
            throw new CountryNotFoundException(origin);
    }

    // DFS Algorithm Solution for finding a possible path between two countries
    public LandRoute findRoutes(final String origin, final String destination) {
        List<String> route = new ArrayList<>();
        route.add(origin);
        boolean isRoutable = findRoutesDFS(origin, destination, route);
        if (isRoutable) return new LandRoute(route);
        throw new NoPossibleRouteException(origin, destination);
    }

    private boolean findRoutesDFS(String current, final String destination, List<String> route) {
        if (current.equals(destination)) {
            return true;
        }
        List<String> adjList = countryCodeWithBordersCache.get(current);
        for (String border : adjList) {
            if (!route.contains(border)) {
                route.add(border);
                if (findRoutesDFS(border, destination, route)) {
                    return true;
                }
                route.remove(route.size() - 1);
            }
        }
        return false;
    }

    // BFS Algorithm approach for finding a possible path between two countries
    // Actually, first I developed DFS and when I tried it takes little more time and it gave
    // not efficient route so I researched and saw BFS is better than DFS while searching a path
    // on unweighted graph
    public List<String> findRoutesBFS(final String origin, final String destination) {

        Deque<List<String>> queue = new ArrayDeque<>();
        HashSet<String> visited = new HashSet<>();
        List<String> route = new ArrayList<>();

        route.add(origin);
        queue.offer(route);
        visited.add(origin);

        while (!queue.isEmpty()) {
            route = queue.poll();
            String current = route.get(route.size() - 1);
            if (current.equals(destination)) {
                return route;
            }
            List<String> currBorders = countryCodeWithBordersCache.get(current);
            for (String border : currBorders) {
                if (!visited.contains(border)) {
                    List<String> newRoute = new ArrayList<>(route);
                    newRoute.add(border);
                    queue.offer(newRoute);
                    visited.add(border);
                }
            }
        }
        return Collections.emptyList();
    }

    public void constructCountriesWithBordersMap() {
        ResponseEntity<CountryCodeWithBorders[]> countriesWithBorders =
                restTemplate.getForEntity(COUNTRIES_INFO_URL, CountryCodeWithBorders[].class);

        Arrays.stream(Objects.requireNonNull(countriesWithBorders.getBody())).forEach(country -> {
            countryCodeWithBordersCache.put(country.getCca3(), new LinkedList(country.getBorders()));
        });
    }

}