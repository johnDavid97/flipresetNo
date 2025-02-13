"use client";

import React from "react";
import { Box, Typography } from "@mui/material";
import moment from "moment";
import TeamBox from "./TeamBox";
import ScoreBox from "./ScoreBox";

const MatchCard = ({ match }) => {
  if (!match) return null;

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "row",
        alignItems: "center",
        width: "100%",
        py: 1,
        px: 2,
      }}
    >
      {/* Time */}
      <Box sx={{ width: "80px", mr: 2 }}>
        <Typography variant="body2" color="text.secondary">
          {match.endTime && moment.unix(match.endTime).isBefore(moment())
            ? "Slutt"
            : match.startTime && match.startTime > 0
            ? moment.unix(match.startTime).format("HH:mm")
            : "Slutt"}
        </Typography>
      </Box>

      {/* Score-boksen */}
      <ScoreBox score1={match.team1.score} score2={match.team2.score} />

      {/* Teams */}
      <Box sx={{ flex: 1 }}>
        <TeamBox team={match.team1} />
        <TeamBox team={match.team2} />
      </Box>
    </Box>
  );
};

export default MatchCard;
