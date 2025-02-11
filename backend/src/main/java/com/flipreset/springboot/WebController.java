package com.flipreset.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.flipreset.services.LeaguesService;
import org.bson.Document;
import java.util.List;

@RestController
public class WebController {

    private final LeaguesService leaguesService;

    @Autowired
    public WebController(LeaguesService leaguesService) {
        this.leaguesService = leaguesService;
    }

    @GetMapping("/leagues")
    public List<Document> getLeagues() {
        return leaguesService.getAllLeagues();
    }
}
