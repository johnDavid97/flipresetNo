"use client";

import React from "react";
import { Box, Typography, Avatar } from "@mui/material";

const ScoreBox = ({ score1, score2 }) => {
  return (
    <Box
      sx={{
        backgroundColor: "#071E35", // Oransje bakgrunn
        width: "25px", // Bredde på score-boksen
        height: "50px", // Høyde justeres for å matche TeamBox
        borderRadius: "8px",
        display: "flex",
        flexDirection: "column",
        alignItems: "center", // Sentraliser horisontalt
        justifyContent: "space-evenly", // Fordel scoren jevnt
        marginRight: "16px", // Mellomrom mellom score og lagene
      }}
    >
      <Typography
        sx={{
          color: "white",
          fontWeight: "bold",
          fontSize: "14px",
          lineHeight: "1",
        }}
      >
        {score1}
      </Typography>
      <Typography
        sx={{
          color: "white",
          fontWeight: "bold",
          fontSize: "14px",
          lineHeight: "1",
        }}
      >
        {score2}
      </Typography>
    </Box>
  );
};

export default ScoreBox;
