import React from "react";
import { AppBar, Toolbar, Typography, Box } from "@mui/material";
import Image from "next/image"; // Korrekt import for Next.js

const Header = () => {
  return (
    <>
      {/* Header */}
      <AppBar position="static" sx={{ backgroundColor: "#0D3B69" }}>
        <Toolbar
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
            }}
          >
            {/* Logo */}
            <Box
              sx={{
                width: "40px",
                height: "40px",
                borderRadius: "50%",
                overflow: "hidden", // Gjør logoen sirkulær
                mr: 2,
              }}
            >
              <Image
                src="/logo.png" // Legg til logoen din
                alt="Flipreset Logo"
                width={40}
                height={40}
                style={{
                  objectFit: "cover", // Sørger for at bildet fyller området
                }}
              />
            </Box>
            {/* Navnet med tilpasset font */}
            <Typography
              variant="h6"
              sx={{
                fontFamily: "'CustomFont', sans-serif", // Bruk din font her
                fontWeight: "bold",
                fontSize: "24px",
              }}
            >
              Flipreset
            </Typography>
          </Box>
        </Toolbar>
      </AppBar>
    </>
  );
};

export default Header;
