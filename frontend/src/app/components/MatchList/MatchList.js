"use client";

import React, { useEffect, useState } from "react";
import { Box, Typography, List, ListItem, Divider } from "@mui/material";
import MatchCard from "../MatchCard/MatchCard";
import MatchListFilter from "./MatchListFilter";

const MatchList = () => {
  const [events, setEvents] = useState([]);
  const [matches, setMatches] = useState([]);
  const [filter, setFilter] = useState("events"); // "events" eller "all"

  useEffect(() => {
    let isMounted = true;

    const fetchData = async () => {
      try {
        const eventsResponse = await fetch("http://localhost:8080/events");
        const eventsData = await eventsResponse.json();

        // Sort events fra nyeste til eldste basert på id
        const sortedEvents = eventsData.sort((a, b) => b.id - a.id);

        if (isMounted) setEvents(sortedEvents);

        const allMatches = [];
        for (const event of sortedEvents) {
          try {
            const matchesResponse = await fetch(
              `http://localhost:8080/matches/event/${event.id}`
            );
            const matchesData = await matchesResponse.json();
            const formattedMatches = matchesData.map((match) => ({
              ...match,
              eventName: event.name, // Legg til event-navn på hver kamp
            }));
            allMatches.push(...formattedMatches);
          } catch (err) {
            console.error(
              `Error fetching matches for event ${event.name}:`,
              err
            );
          }
        }

        if (isMounted) setMatches(allMatches);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchData();

    return () => {
      isMounted = false;
    };
  }, []);

  if (!events.length) {
    return (
      <Box sx={{ p: 2 }}>
        <Typography>No events available</Typography>
      </Box>
    );
  }

  return (
    <Box>
      <MatchListFilter selected={filter} onFilterChange={setFilter} />
      {filter === "events" ? (
        events.map((event) => (
          <Box key={event.id}>
            {matches.filter((match) => match.eventName === event.name).length >
              0 && (
              <>
                <Typography
                  variant="h6"
                  sx={{
                    mt: 0,
                    mb: 0,
                    backgroundColor: "#f5f5f5",
                    p: 0.5,
                    borderRadius: 1,
                    fontWeight: "bold",
                    fontSize: "15px",
                  }}
                >
                  {event.name.replace("#", "")}
                </Typography>
                <List>
                  {matches
                    .filter((match) => match.eventName === event.name)
                    .slice(0, 3)
                    .map((match, index) => (
                      <ListItem key={match._id || index} disablePadding>
                        <Box sx={{ width: "100%" }}>
                          <MatchCard match={match} />
                          {index < matches.length - 1 && <Divider />}
                        </Box>
                      </ListItem>
                    ))}
                </List>
              </>
            )}
          </Box>
        ))
      ) : (
        <List>
          {matches.map((match, index) => (
            <ListItem key={match._id || index} disablePadding>
              <Box sx={{ width: "100%" }}>
                <MatchCard match={match} />
                {index < matches.length - 1 && <Divider />}
              </Box>
            </ListItem>
          ))}
        </List>
      )}
    </Box>
  );
};

export default MatchList;
