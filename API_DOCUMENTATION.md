| TYPE  | URI                          | BODY                     | EFFECT                                      |
|-------|------------------------------|--------------------------|----------------------------------------------|
| GET   | /clubs                       |                          | Get all clubs                                |
| POST  | /clubs                       |                          | Add a new club                               |
| POST  | /clubs/transfer              | p1id, p2id, Token        | Transfer players between clubs               |
| GET   | /clubs/{codeName}            |                          | Get a club by its code name                   |
| PUT   | /clubs/{codeName}            | VERSION, ClubView        | Update a club by its code name                |
| PATCH | /clubs/{codeName}            | VERSION, string          | Partially update a club by its code name      |
| DELETE| /clubs/{codeName}            |                          | Delete a club by its code name                |
| POST  | /clubs/{codeName}/players    | playerId, Token          | Add a player to a club by its code name       |
| GET   | /players                     |                          | Get all players                              |
| POST  | /players                     |                          | Add a new player                             |
| GET   | /players/{id}                |                          | Get a player by their ID                      |
| PUT   | /players/{id}                | VERSION, Player          | Update a player by their ID                   |
| PATCH | /players/{id}                | VERSION, string          | Partially update a player by their ID         |
| DELETE| /players/{id}                |                          | Delete a player by their ID                   |
| GET   | /players/{id}/games          |                          | Get games associated with a player by their ID|
| GET   | /tokens                      |                          | Get a token to post                           |
| POST  | /tokens                      | AuthenticationRequest   | Authenticate and get a token                  |
| GET   | /users                       |                          | Get all users                                |
| POST  | /users                       | RegisterRequest          | Register a new user                          |
| GET   | /users/{id}                  |                          | Get a user by their ID                        |
| PUT   | /users/{id}                  | VERSION, UserView        | Update a user by their ID                     |
| PATCH | /users/{id}                  | VERSION, string          | Partially update a user by their ID           |
| GET   | /info/titles                 |                          | Get all titles                               |
| GET   | /info/titles/genders/{gender}|                          | Get titles by gender                          |
| GET   | /info/titles/{codeName}      |                          | Get a specific title by its code name         |
| GET   | /games                       |                          | Get all games                                |
| POST  | /games                       |                          | Add a new game                               |
| GET   | /games/{id}                  |                          | Get a game by its ID                          |
| PATCH | /games/{id}                  | VERSION, string          | Update a specific field of a game by its ID   |
| DELETE| /games/{id}                  |                          | Delete a game by its ID                       |
| GET   | /games/{id}/statistics       |                          | Get statistics for a game by its ID           |
| GET   | /statistics                  |                          | Get basic statistics                          |
