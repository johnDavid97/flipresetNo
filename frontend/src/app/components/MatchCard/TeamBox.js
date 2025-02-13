"use client";

import React from "react";
import { Box, Typography, Avatar } from "@mui/material";

const TeamBox = ({ team }) => {
  // Finner URL til bildet hvis tilgjengelig, ellers standard bilde
  const imageUrl =
    team.images && team.images.length > 0
      ? team.images[0].url
      : "https://rocket-league.com/content/media/items/avatar/220px/1b16f7679a1638965994.png";

  return (
    <Box sx={{ display: "flex", alignItems: "center", my: 0.5 }}>
      <Avatar
        src={imageUrl}
        alt={team.name || "Team"}
        sx={{ width: 18, height: 18, mr: 1 }}
      >
        {!team.images || team.images.length === 0 ? team.name?.charAt(0) : null}
        {/* Hvis det ikke finnes bilde, vis første bokstav i lagets navn */}
      </Avatar>
      <Typography
        variant="subtitle2"
        sx={{
          color: team.winner ? "text.primary" : "text.secondary",
          fontWeight: team.winner ? "bold" : "normal",
        }}
      >
        {team.name}
      </Typography>
    </Box>
  );
};

export default TeamBox;
