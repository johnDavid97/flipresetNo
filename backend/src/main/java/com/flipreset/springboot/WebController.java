package com.flipreset.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.flipreset.services.LeaguesService;
import com.flipreset.services.MatchesService;
import com.flipreset.services.PlayersService;
import com.flipreset.services.TeamsService;
import com.flipreset.services.EventsService;
import org.bson.Document;
import java.util.List;

@RestController
public class WebController {

    private final LeaguesService leaguesService;
    private final MatchesService matchesService;
    private final PlayersService playersService;
    private final TeamsService teamsService;
    private final EventsService eventsService;

    @Autowired
    public WebController(LeaguesService leaguesService, MatchesService matchesService,
            PlayersService playersService, TeamsService teamsService,
            EventsService eventsService) {
        this.leaguesService = leaguesService;
        this.matchesService = matchesService;
        this.playersService = playersService;
        this.teamsService = teamsService;
        this.eventsService = eventsService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/leagues")
    public List<Document> getLeagues() {
        return leaguesService.getAllLeagues();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/matches")
    public List<Document> getMatches() {
        return matchesService.getAllMatches();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/players")
    public List<Document> getPlayers() {
        return playersService.getAllPlayers();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/teams")
    public List<Document> getTeams() {
        return teamsService.getAllTeams();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/teams/{teamId}/matches")
    public List<Document> getMatchesByTeam(@PathVariable String teamId) {
        return teamsService.getMatchesByTeam(teamId);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/teams/{teamId}/players")
    public List<Document> getAllPlayersInTeam(@PathVariable String teamId) {
        return teamsService.getAllPlayersInTeam(teamId);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/players/{playerId}/team")
    public Document getTeamByPlayer(@PathVariable String playerId) {
        return playersService.getTeamByPlayer(playerId);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/events")
    public List<Document> getAllEvents() {
        return eventsService.getAllEvents();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/matches/event/{eventId}")
    public List<Document> getAllMatchesByEvent(@PathVariable String eventId) {
        System.out.println("Fetching matches for event ID: " + eventId);
        return matchesService.getAllMatchesByEvent(eventId);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/matches/{matchId}")
    public Document getMatchById(@PathVariable String matchId) {
        return matchesService.getMatchById(matchId);
    }
}
