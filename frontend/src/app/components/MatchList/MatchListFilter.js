"use client";

import React from "react";
import { Box, Typography } from "@mui/material";

const MatchListFilter = ({ selected, onFilterChange }) => {
  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "flex-start",
        backgroundColor: "#F4F4F6", // Lys oransje bakgrunnsfarge
        borderRadius: "25px", // Avrundede kanter
        padding: "4px", // Mindre padding rundt knappene
        width: "fit-content",
        marginBottom: "10px", // Passer til knappene
      }}
    >
      {/* "Events"-knappen */}
      <Box
        onClick={() => onFilterChange("events")}
        sx={{
          backgroundColor: selected === "events" ? "#000000" : "transparent", // Oransje for valgt
          color: selected === "events" ? "white" : "black", // Tekstfarge
          borderRadius: "20px", // Avrundede kanter på hver knapp
          padding: "6px 12px", // Mindre padding inni hver knapp
          fontWeight: "bold",
          cursor: "pointer",
          fontSize: "12px", // Mindre tekststørrelse
          textAlign: "center",
          transition: "all 0.2s ease",
        }}
      >
        EVENTS
      </Box>

      {/* "All Matches"-knappen */}
      <Box
        onClick={() => onFilterChange("all")}
        sx={{
          backgroundColor: selected === "all" ? "#000000" : "transparent", // Oransje for valgt
          color: selected === "all" ? "white" : "black", // Tekstfarge
          borderRadius: "20px", // Avrundede kanter på hver knapp
          padding: "6px 12px", // Mindre padding inni hver knapp
          fontWeight: "bold",
          cursor: "pointer",
          fontSize: "12px", // Mindre tekststørrelse
          textAlign: "center",
          transition: "all 0.2s ease",
        }}
      >
        ALL MATCHES
      </Box>
    </Box>
  );
};

export default MatchListFilter;
