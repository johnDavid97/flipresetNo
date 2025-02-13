"use client";

import React, { useEffect, useState } from "react";
import { Box, Typography, Divider, Button } from "@mui/material";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";

const NewsFeed = () => {
  const [news, setNews] = useState([]);

  useEffect(() => {
    const fetchNews = async () => {
      const CORS_PROXY = "https://api.allorigins.win/get?url=";
      const RSS_URL = "https://www.reddit.com/r/RocketLeagueEsports/.rss";

      try {
        const response = await fetch(CORS_PROXY + encodeURIComponent(RSS_URL));
        const data = await response.json();

        // Dekoder base64-innhold hvis det finnes
        const decodedContent = atob(data.contents.split(",")[1]);

        const parser = new DOMParser();
        const xml = parser.parseFromString(decodedContent, "application/xml");

        // Sjekker om det finnes feil i XML-strukturen
        const errorNode = xml.querySelector("parsererror");
        if (errorNode) {
          console.error("Error parsing XML:", errorNode.textContent);
          return;
        }

        // Henter og mapper RSS-elementer
        const items = Array.from(xml.querySelectorAll("entry"))
          .map((entry) => ({
            title: entry.querySelector("title")?.textContent,
            link: entry
              .querySelector("link[rel='alternate']")
              ?.getAttribute("href"),
            pubDate: entry.querySelector("updated")?.textContent,
          }))
          .slice(0, 5);

        setNews(items);
      } catch (error) {
        console.error("Error fetching news:", error);
      }
    };

    fetchNews();
  }, []);

  return (
    <Box
      sx={{
        backgroundColor: "#F4F4F6",
        borderRadius: "12px",
        boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
      }}
    >
      {/* Header */}
      <Box
        sx={{
          backgroundColor: "#0D3B69",
          borderRadius: "12px 12px 0 0",
          padding: "12px",
          color: "#fff",
          fontWeight: "bold",
          textAlign: "center",
          alignItems: "flex-start",
        }}
      >
        <Typography variant="h6" sx={{ display: "flex", fontWeight: "bold" }}>
          Nyheter
        </Typography>
      </Box>

      {/* News list */}
      <Box sx={{ marginTop: "12px" }}>
        {news.length > 0 ? (
          news.map((item, index) => (
            <React.Fragment key={index}>
              <Box sx={{ marginBottom: "12px" }}>
                <Typography
                  variant="caption"
                  sx={{
                    display: "block",
                    color: "#888",
                    marginBottom: "4px",
                    padding: "5px",
                  }}
                >
                  {/* item.pubDate.toLocaleDateString("no-NO", {
                    day: "numeric",
                    month: "long",
                    year: "numeric",
                  })*/}
                </Typography>
                <Typography
                  variant="body1"
                  sx={{
                    fontWeight: index === 0 ? "bold" : "normal",
                    color: "#333",
                    padding: "5px",
                  }}
                >
                  <a
                    href={item.link}
                    target="_blank"
                    rel="noopener noreferrer"
                    style={{
                      textDecoration: "none",
                    }}
                  >
                    {item.title}
                  </a>
                </Typography>
              </Box>
              {index < news.length - 1 && <Divider />}
            </React.Fragment>
          ))
        ) : (
          <Typography
            variant="body2"
            sx={{ color: "#888", textAlign: "center" }}
          >
            Ingen nyheter tilgjengelig.
          </Typography>
        )}
      </Box>

      {/* Button */}
      <Button
        variant="contained"
        sx={{
          backgroundColor: "#000000",
          color: "#fff",
          borderRadius: "20px",
          marginTop: "16px",
          textTransform: "none",
          width: "100%",
        }}
        endIcon={<ArrowForwardIcon />}
      >
        Se flere nyheter
      </Button>
    </Box>
  );
};

export default NewsFeed;
