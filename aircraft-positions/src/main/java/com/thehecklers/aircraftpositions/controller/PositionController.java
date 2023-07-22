package com.thehecklers.aircraftpositions.controller;

import com.thehecklers.aircraftpositions.domain.Aircraft;
import com.thehecklers.aircraftpositions.repository.AircraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@RequiredArgsConstructor
public class PositionController {

    private final AircraftRepository aircraftRepository;
    private final WebClient client = WebClient.create("http://localhost:7634/aircraft");

    @GetMapping("aircraft")
    public String getCurrentAircraftPositions(Model model) {
        aircraftRepository.deleteAll();

        client.get()
                .retrieve()
                .bodyToFlux(Aircraft.class)
                .filter(plane -> !plane.getReg().isEmpty())
                .toStream()
                .forEach(aircraftRepository::save);

        model.addAttribute("currentPositions", aircraftRepository.findAll());
        return "positions";
    }

}