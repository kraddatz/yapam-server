Feature: Create an account

  Scenario: with an unused email
    Given an authorized user
    And a request body
    """
      {
        "locale": "de-DE",
        "name": "Max Mustermann",
        "publicKey": "publicKey"
      }
    """
    When I send a post request to /api/users
    Then status is 200

