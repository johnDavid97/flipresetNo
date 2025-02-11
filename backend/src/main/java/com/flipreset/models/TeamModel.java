package com.flipreset.models;

import java.util.List;

public class TeamModel {
    private String id;
    private String name;
    private List<Player> members;
    private List<Image> images;

    public static class Player {
        private String id;
        private String gamerTag;
        private boolean isCaptain;

        // Getters and setters
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

        public boolean isCaptain() {
            return isCaptain;
        }

        public void setCaptain(boolean captain) {
            isCaptain = captain;
        }
    }

    public static class Image {
        private String url;
        private String type;
        private int width;
        private int height;

        // Getters and setters
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

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    // Getters and setters
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

    public List<Player> getMembers() {
        return members;
    }

    public void setMembers(List<Player> members) {
        this.members = members;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
