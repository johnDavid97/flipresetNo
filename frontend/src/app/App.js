import React from 'react';
import { Box, Container, Grid } from '@mui/material';
import Header from './components/Header/Header';
import LeagueSelector from './components/LeagueSelector/LeagueSelector';
import MatchList from './components/MatchList/MatchList';
import { LeagueProvider } from './contexts/LeagueContext';

function App() {
  return (
    <LeagueProvider>
      <Box sx={{ flexGrow: 1 }}>
        <Header />
        <Container maxWidth="lg">
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <LeagueSelector />
            </Grid>
            <Grid item xs={9}>
              <MatchList />
            </Grid>
            <Grid item xs={3}>
              {/* NewsSidebar component would go here */}
            </Grid>
          </Grid>
        </Container>
      </Box>
    </LeagueProvider>
  );
}

export default App; 