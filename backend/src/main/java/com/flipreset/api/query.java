package com.flipreset.api;

import com.flipreset.models.QueryModel;

public class query {

  public static final QueryModel GET_LEAGUES = new QueryModel("getLeagues", "league-info-topic", """
      query GetLeague {
             league(slug: "RLCS-2025") {
               id
               name
               events{
                nodes{
                    id
                    name

                sets {
                    nodes{
                        id

                    }

                }
                }
               }
               images {
                 url
                 type
                 width
                 height
               }
             }
           }
           """);

  public static final QueryModel GET_EVENTS = new QueryModel("getEvents", "events-topic", """
      query GetTournaments {
            league(slug: "RLCS-2025") {
                id
                name
              events(query: {
                page: 1,
                perPage: 10,
                filter: { search: { searchString: "EU" } }
              }) {
                nodes {
                  id
                  name
                  startAt
                  sets{
                   nodes{
                       id
                   }
                  }
                  tournament {
                    id
                    name
                  }
                }
              }
            }
          }


           """);

  public static final QueryModel GET_SETS = new QueryModel("getSets", "sets-topic", """
      query GetMatches {
                       league(slug: "RLCS-2025") {
        id
        name
                         events(query: {
                           page: 1,
                           perPage: 1,
                           filter: { search: { searchString: "EU" } }
                         })

        {
                           nodes {
                            id
                            name
                            tournament {
                              id
                              name
                            }
                             sets(
                               page: 1
                               perPage: 10
                               sortType: RECENT
                             ) {
                               nodes {
                                 id
                                 startedAt
                                 completedAt
                                 displayScore
                                 winnerId
                                 slots {
                                   entrant {
                                     name
                                     team {
                                      id
                                      name
                                      members{

                                          id
                                        isCaptain
                                        player{
                                          id
                                          gamerTag
                                        }

                                      }
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
                         """);

}