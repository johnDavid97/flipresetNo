package com.flipreset.api;

public class query {
    public static final String RLCS_DATA = """
            query LeagueStandings {
              league(slug: "RLCS-2025") {
                id
                name
                images {
                  url
                  type
                  width
                  height
                }
                events(query: {
                  page: 1,
                  perPage: 3,
                  filter: { search: { searchString: "EU" } }
                }) {
                  pageInfo {
                    totalPages
                    total
                  }
                  nodes {
                    id
                    name
                    updatedAt
                    startAt
                    tournament {
                      id
                      name
                    }
                    sets(
                      page: 1
                      perPage: 10
                      sortType: RECENT
                    ) {
                      pageInfo {
                        total
                      }
                      nodes {
                        id
                        startedAt
                        createdAt
                        completedAt
                        displayScore
                        winnerId
                        slots {
                          id
                          entrant {
                            id
                            name
                            team {
                              images {
                                url
                                type
                                width
                                height
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            """;

}