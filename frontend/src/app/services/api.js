import axios from "axios";

const BASE_URL = "http://localhost:8080";

export const api = {
  getLeagues: () =>
    axios.get(`${BASE_URL}/leagues`).then((response) => {
      console.log(response.data);
      return response;
    }),
  getMatches: () => axios.get(`${BASE_URL}/matches`),
  getMatchesByEvent: (eventId) =>
    axios.get(`${BASE_URL}/matches/event/${eventId}`),
  getEvents: () => axios.get(`${BASE_URL}/events`),
  getMatchById: (matchId) => axios.get(`${BASE_URL}/matches/${matchId}`),
};

export default api;
