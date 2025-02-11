package com.flipreset.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipreset.models.EventModel;
import com.flipreset.models.LeagueModel;
import com.flipreset.models.MatchesModel;

import java.util.ArrayList;
import java.util.List;

public class JsonProcessingService {
    private static final ObjectMapper mapper = new ObjectMapper();

    // -------------------- LEAGUE PROCESSING --------------------
    public static LeagueModel processLeagueResponse(String jsonResponse) throws Exception {
        JsonNode rootNode = mapper.readTree(jsonResponse);
        JsonNode leagueNode = rootNode.path("data").path("league");

        LeagueModel league = new LeagueModel();
        league.setId(leagueNode.path("id").asText());
        league.setName(leagueNode.path("name").asText());

        league.setEvents(parseLeagueEvents(leagueNode.path("events").path("nodes")));

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

        return event;
    }

    // -------------------- MATCHES PROCESSING --------------------
    public static MatchesModel processSingleMatch(String jsonResponse) throws Exception {
        JsonNode setNode = mapper.readTree(jsonResponse);

        MatchesModel match = new MatchesModel();
        match.setId(setNode.path("id").asText());
        match.setStartedAt(setNode.path("startedAt").asLong());
        match.setCompletedAt(setNode.path("completedAt").asLong());
        match.setDisplayScore(setNode.path("displayScore").asText());
        match.setWinnerId(setNode.path("winnerId").asLong());

        // Parse teams
        JsonNode slots = setNode.path("slots");
        if (slots.size() == 2) {
            MatchesModel.Team team1 = parseTeam(slots.get(0).path("entrant").path("team"));
            MatchesModel.Team team2 = parseTeam(slots.get(1).path("entrant").path("team"));
            match.setTeam1(team1);
            match.setTeam2(team2);

            // Parse scores from displayScore
            updateTeamScores(match, team1, team2);
        }

        return match;
    }

    private static MatchesModel.Team parseTeam(JsonNode teamNode) {
        MatchesModel.Team team = new MatchesModel.Team();
        team.setId(teamNode.path("id").asText());
        team.setName(teamNode.path("name").asText());

        // Parse images
        List<MatchesModel.Image> images = new ArrayList<>();
        for (JsonNode imageNode : teamNode.path("images")) {
            MatchesModel.Image image = new MatchesModel.Image();
            image.setUrl(imageNode.path("url").asText());
            image.setType(imageNode.path("type").asText());
            image.setWidth(imageNode.path("width").asInt());
            image.setHeight(imageNode.path("height").asInt());
            images.add(image);
        }
        team.setImages(images);

        // Parse members
        List<MatchesModel.Player> members = new ArrayList<>();
        for (JsonNode memberNode : teamNode.path("members")) {
            MatchesModel.Player player = new MatchesModel.Player();
            player.setId(memberNode.path("id").asText());
            player.setCaptain(memberNode.path("isCaptain").asBoolean());
            player.setPlayer(new MatchesModel.PlayerInfo(
                    memberNode.path("player").path("id").asText(),
                    memberNode.path("player").path("gamerTag").asText()));
            members.add(player);
        }
        team.setMembers(members);

        return team;
    }

    private static void updateTeamScores(MatchesModel match, MatchesModel.Team team1, MatchesModel.Team team2) {
        String displayScore = match.getDisplayScore();
        String[] scores = displayScore.split(" - ");
        team1.setScore(Integer.parseInt(scores[0].replaceAll("\\D", "")));
        team2.setScore(Integer.parseInt(scores[1].replaceAll("\\D", "")));
    }
}
