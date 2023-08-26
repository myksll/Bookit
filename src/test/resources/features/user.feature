Feature: User Verification


  Scenario: verify information about logged user
    Given I logged Bookit api as a "team-member"
    When I sent get request to "/api/users/me" endpoint
    Then status code should be 200
    And content type is "application/json"
    And role is "student-team-member"
     # API vs DB ---> Two point verification
  @db @wip
  Scenario: verify information about logged user from api and database
    Given I logged Bookit api as a "team-member"
    When I sent get request to "/api/users/me" endpoint
    Then the information about current user from api and database should match

