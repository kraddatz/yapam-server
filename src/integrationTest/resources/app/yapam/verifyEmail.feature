Feature: Verify the email

  Scenario: with a valid verification token
    Given an existing user
      | email      | max.mustermann@email.com |
      | emailToken | emailtoken               |
    And request parameter
      | token | emailtoken |
    And userId in path
    When I send a get request to /api/users/{userId}/email/verify
    Then status is 200

  Scenario: with an invalid verification token
    Given an existing user
      | email      | max.mustermann@email.com |
      | emailToken | emailToken               |
    And request parameter
      | token | invalidEmailToken |
    And userId in path
    When I send a get request to /api/users/{userId}/email/verify
    Then status is 400
    And exception is "InvalidEmailVerificationTokenException"

  Scenario: with an invalid verification token
    Given an existing user
      | email      | max.mustermann@email.com |
      | emailToken | emailToken               |
    And request parameter
      | token | invalidEmailToken |
    And path parameter
      | userId | test |
    When I send a get request to /api/users/{userId}/email/verify
    Then status is 404
    And exception is "UserNotFoundException"

  Scenario: when the registration timeout has passed
    Given an existing user
      | email        | max.mustermann@email.com |
      | emailToken   | emailToken               |
      | creationDate | 2000-01-01T00:00:00      |
    And request parameter
      | token | invalidEmailToken |
    And userId in path
    When I send a get request to /api/users/{userId}/email/verify
    Then status is 400
    And exception is "EmailVerificationTokenExpiredException  "