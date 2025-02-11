package com.flipreset.models;

public class QueryModel {
    public String name;
    public String topic;
    public String query;

    public QueryModel(String name, String topic, String query) {
        this.name = name;
        this.topic = topic;
        this.query = query;
    }

    public String getName() {
        return name;
    }

    public String getTopic() {
        return topic;
    }

    public String getQuery() {
        return query;
    }

    public Object getModel() {
        switch (this.name) {
            case "getLeagues":
                return new LeagueModel();
            case "getEvents":
                return new TournamentModel();
            case "getSets":
                return new MatchesModel();
            case "getTeams":
                return new TeamModel();
            default:
                throw new IllegalArgumentException("Ukjent modell: " + this.name);
        }
    }
}
