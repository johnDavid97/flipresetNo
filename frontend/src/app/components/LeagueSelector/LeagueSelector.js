import React, { useEffect } from "react";
import { Box, Avatar, Typography, Grid } from "@mui/material";
import { useLeague } from "../../contexts/LeagueContext";
import api from "../../services/api";

const LeagueSelector = () => {
  const { leagues, setLeagues, selectedLeague, setSelectedLeague } =
    useLeague();

  useEffect(() => {
    const fetchLeagues = async () => {
      try {
        const response = await api.getLeagues();
        setLeagues(response.data);
      } catch (error) {
        console.error("Error fetching leagues:", error);
      }
    };
    fetchLeagues();
  }, [setLeagues]);

  return (
    <Box sx={{ overflowX: "auto", py: 2 }}>
      <Grid container spacing={2} wrap="nowrap">
        {leagues.map((league) => (
          <Grid item key={league._id}>
            <Box
              onClick={() => setSelectedLeague(league)}
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                cursor: "pointer",
                opacity: selectedLeague?._id === league._id ? 1 : 0.7,
              }}
            >
              <Avatar
                src={league.images[0].url}
                sx={{ width: 48, height: 48 }}
              />
              <Typography variant="caption" align="center">
                {league.name}
              </Typography>
            </Box>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default LeagueSelector;
