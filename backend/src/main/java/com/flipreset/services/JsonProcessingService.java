package com.flipreset.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipreset.models.EventModel;
import com.flipreset.models.LeagueModel;
import com.flipreset.models.MatchesModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonProcessingService {
    private static final ObjectMapper mapper = new ObjectMapper();

    // -------------------- LEAGUE PROCESSING --------------------
    public static LeagueModel processLeagueResponse(String jsonResponse) throws Exception {
        JsonNode rootNode = mapper.readTree(jsonResponse);
        JsonNode leagueNode = rootNode.path("data").path("league");
        JsonNode eventNode = leagueNode.path("events").path("nodes");
        JsonNode imageNode = leagueNode.path("images");

        LeagueModel league = new LeagueModel();
        league.setId(leagueNode.path("id").asText());
        league.setName(leagueNode.path("name").asText());

        List<LeagueModel.Event> events = parseLeagueEvents(eventNode);
        league.setEvents(events);

        List<LeagueModel.Set> sets = parseLeagueSets(eventNode);
        league.setSets(sets);

        List<LeagueModel.Image> images = parseLeageImages(imageNode);
        league.setImages(images);

        return league;
    }

    private static List<LeagueModel.Event> parseLeagueEvents(JsonNode eventsNode) {
        List<LeagueModel.Event> events = new ArrayList<>();
        for (JsonNode eventNode : eventsNode) {
            LeagueModel.Event event = new LeagueModel.Event();
            event.setId(eventNode.path("id").asText());
            event.setName(eventNode.path("name").asText());
            events.add(event);
        }
        return events;
    }

    private static List<LeagueModel.Image> parseLeageImages(JsonNode imagesNode) {
        List<LeagueModel.Image> images = new ArrayList<>();
        for (JsonNode imageNode : imagesNode) {
            LeagueModel.Image image = new LeagueModel.Image();
            image.setUrl(imageNode.path("url").asText());
            image.setType(imageNode.path("type").asText());
            image.setWidth(imageNode.path("width").asInt(0));
            image.setHeight(imageNode.path("height").asInt());
            images.add(image);
        }
        return images;
    }

    private static List<LeagueModel.Set> parseLeagueSets(JsonNode eventsNode) {
        List<LeagueModel.Set> sets = new ArrayList<>();
        for (JsonNode eventNode : eventsNode) {

            JsonNode setsNode = eventNode.path("sets").path("nodes");
            for (JsonNode setNode : setsNode) {
                LeagueModel.Set set = new LeagueModel.Set();
                set.setId(setNode.path("id").asText());
                sets.add(set);
            }
        }
        return sets;
    }

    // -------------------- EVENT PROCESSING --------------------
    public static EventModel processSingleEvent(String jsonResponse) throws Exception {
        JsonNode eventNode = mapper.readTree(jsonResponse);

        EventModel event = new EventModel();
        event.setId(eventNode.path("id").asText());
        event.setName(eventNode.path("name").asText());

        // Parse tournament
        JsonNode tournamentNode = eventNode.path("tournament");
        if (!tournamentNode.isMissingNode()) {
            EventModel.Tournament tournament = new EventModel.Tournament();
            tournament.setId(tournamentNode.path("id").asText());
            tournament.setName(tournamentNode.path("name").asText());
            event.setTournament(tournament);
        }

        List<EventModel.Series> series = parseEventSeries(eventNode);
        event.setSeries(series);

        return event;
    }

    // Listing all series in a event(Sets)
    private static List<EventModel.Series> parseEventSeries(JsonNode eventNode) {
        List<EventModel.Series> series = new ArrayList<>();

        // Hent sets fra eventNode
        JsonNode seriesNodes = eventNode.path("sets").path("nodes");
        for (JsonNode serieNode : seriesNodes) {
            EventModel.Series serie = new EventModel.Series();
            serie.setId(serieNode.path("id").asText());
            series.add(serie);
        }

        return series;
    }

    // -------------------- MATCHES PROCESSING --------------------

    public static MatchesModel processSingleMatch(String matchJson) throws Exception {
        // Les JSON og parse enkelt set/match
        JsonNode setNode = mapper.readTree(matchJson);

        MatchesModel match = new MatchesModel();

        // Match hoveddata
        match.setId(setNode.path("id").asText());
        match.setStartedAt(setNode.path("startedAt").asLong(0)); // Håndter nullverdier
        match.setCompletedAt(setNode.path("completedAt").asLong(0));
        match.setDisplayScore(setNode.path("displayScore").asText());
        match.setWinnerId(setNode.path("winnerId").asLong());

        // Event-objekt for match
        JsonNode eventNode = setNode.path("event");
        if (!eventNode.isMissingNode()) {
            MatchesModel.Event event = new MatchesModel.Event();
            event.setId(eventNode.path("id").asText());
            event.setName(eventNode.path("name").asText("")); // Kan settes til tom string hvis `name` mangler
            match.setEvent(event);
        }

        // League-objekt
        JsonNode leagueNode = setNode.path("league"); // Om league-data ikke er inkludert, dette kan ignoreres
        if (!leagueNode.isMissingNode()) {
            MatchesModel.League league = new MatchesModel.League();
            league.setId(leagueNode.path("id").asText());
            league.setName(leagueNode.path("name").asText());
            match.setLeague(league);
        }

        // Teams (team1 og team2)
        JsonNode slotsNode = setNode.path("slots");
        if (slotsNode.size() >= 2) {
            MatchesModel.Team team1 = parseTeam(slotsNode.get(0).path("entrant").path("team"));
            MatchesModel.Team team2 = parseTeam(slotsNode.get(1).path("entrant").path("team"));

            match.setTeam1(team1);
            match.setTeam2(team2);

            // Oppdater score for team1 og team2 basert på displayScore
            updateTeamScores(match, team1, team2);
        }

        return match;
    }

    // Parse en enkelt Team fra JSON
    private static MatchesModel.Team parseTeam(JsonNode teamNode) {
        if (teamNode == null || teamNode.isMissingNode()) {
            return null; // Håndter null eller manglende data
        }

        MatchesModel.Team team = new MatchesModel.Team();
        team.setId(teamNode.path("id").asText());
        team.setName(teamNode.path("name").asText());

        // Parse images
        List<MatchesModel.Image> images = new ArrayList<>();
        for (JsonNode imageNode : teamNode.path("images")) {
            MatchesModel.Image image = new MatchesModel.Image();
            image.setUrl(imageNode.path("url").asText());
            image.setType(imageNode.path("type").asText());
            image.setWidth(imageNode.path("width").asInt(0));
            image.setHeight(imageNode.path("height").asInt(0));
            images.add(image);
        }
        team.setImages(images);

        // Parse members
        List<MatchesModel.Player> players = new ArrayList<>();
        for (JsonNode memberNode : teamNode.path("members")) {
            MatchesModel.Player player = new MatchesModel.Player();
            player.setId(memberNode.path("id").asText());
            player.setCaptain(memberNode.path("isCaptain").asBoolean(false));

            // Hent spillerinfo
            JsonNode playerInfoNode = memberNode.path("player");
            if (!playerInfoNode.isMissingNode()) {
                MatchesModel.PlayerInfo playerInfo = new MatchesModel.PlayerInfo(
                        playerInfoNode.path("id").asText(),
                        playerInfoNode.path("gamerTag").asText());
                player.setPlayer(playerInfo);
            }
            players.add(player);
        }
        team.setMembers(players);

        return team;
    }

    // Oppdater team-score fra displayScore
    private static void updateTeamScores(MatchesModel match, MatchesModel.Team team1, MatchesModel.Team team2) {
        String displayScore = match.getDisplayScore();
        if (displayScore != null && displayScore.contains(" - ")) {
            String[] parts = displayScore.split(" - ");
            if (parts.length == 2) {
                try {
                    // Bruk regex for å matche poeng på slutten av teksten
                    String team1Score = parts[0].replaceAll(".*\\s(\\d+)$", "$1"); // Finn siste tall i team1
                    String team2Score = parts[1].replaceAll(".*\\s(\\d+)$", "$1"); // Finn siste tall i team2

                    team1.setScore(Integer.parseInt(team1Score));
                    team2.setScore(Integer.parseInt(team2Score));
                } catch (NumberFormatException e) {
                    // Logg en advarsel hvis displayScore ikke kan parses
                    System.err.println("Kunne ikke parse score fra displayScore: " + displayScore);
                }
            } else {
                System.err.println("Feil format på displayScore: " + displayScore);
            }
        }
    }

}
