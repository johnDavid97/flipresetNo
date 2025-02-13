import React, { createContext, useState, useContext } from 'react';

const LeagueContext = createContext();

export const LeagueProvider = ({ children }) => {
  const [selectedLeague, setSelectedLeague] = useState(null);
  const [leagues, setLeagues] = useState([]);

  return (
    <LeagueContext.Provider value={{ selectedLeague, setSelectedLeague, leagues, setLeagues }}>
      {children}
    </LeagueContext.Provider>
  );
};

export const useLeague = () => useContext(LeagueContext); 