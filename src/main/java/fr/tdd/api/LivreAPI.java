package fr.tdd.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tdd.service.LivreService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/livre")
@RequiredArgsConstructor
public class LivreAPI {

    private final LivreService livreService;

    
}
