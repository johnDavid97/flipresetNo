package com.flipreset.models;

import java.util.List;

public class MatchesModel {
    private String id;
    private Long startedAt;
    private Long completedAt;
    private String displayScore;
    private Team team1;
    private Team team2;
    private Long winnerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Long startedAt) {
        this.startedAt = startedAt;
    }

    public Long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Long completedAt) {
        this.completedAt = completedAt;
    }

    public String getDisplayScore() {
        return displayScore;
    }

    public void setDisplayScore(String displayScore) {
        this.displayScore = displayScore;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Long winnerId) {
        this.winnerId = winnerId;
    }

    public static class Team {
        private String id;
        private String name;
        private Integer score;
        private List<Image> images;
        private List<Player> members;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public List<Image> getImages() {
            return images;
        }

        public void setImages(List<Image> images) {
            this.images = images;
        }

        public List<Player> getMembers() {
            return members;
        }

        public void setMembers(List<Player> members) {
            this.members = members;
        }
    }

    public static class Image {
        private String url;
        private String type;
        private Integer width;
        private Integer height;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }
    }

    public static class Player {
        private String id;
        private boolean isCaptain;
        private PlayerInfo player;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isCaptain() {
            return isCaptain;
        }

        public void setCaptain(boolean isCaptain) {
            this.isCaptain = isCaptain;
        }

        public PlayerInfo getPlayer() {
            return player;
        }

        public void setPlayer(PlayerInfo player) {
            this.player = player;
        }
    }

    public static class PlayerInfo {
        private String id;
        private String gamerTag;

        public PlayerInfo(String id, String gamerTag) {
            this.id = id;
            this.gamerTag = gamerTag;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGamerTag() {
            return gamerTag;
        }

        public void setGamerTag(String gamerTag) {
            this.gamerTag = gamerTag;
        }
    }
}
