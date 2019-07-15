Feature: Create an account

  Scenario: with an unused email
    Given a request body
    """
      {
        "culture": "de-DE",
        "name": "Max Mustermann",
        "email": "max.mustermann@email.com",
        "masterPasswordHint": "passwordispassword",
        "masterPasswordHash": "$2a$10$HFeBQjv4d.iGubQvGZe31uMBxWqoaHLQt9O1na7KlFZKhxvPkf7ge",
        "publicKey": "publicKey"
      }
    """
    When I send a post request to /api/users
    Then status is 200

  Scenario: but the email is invalid
    Given The email is invalid
    And a request body
    """
      {
        "culture": "de-DE",
        "name": "Max Mustermann",
        "email": "invalidemail@email.com",
        "masterPasswordHint": "passwordispassword",
        "masterPasswordHash": "$2a$10$HFeBQjv4d.iGubQvGZe31uMBxWqoaHLQt9O1na7KlFZKhxvPkf7ge",
        "publicKey": "publicKey"
      }
    """
    When I send a post request to /api/users
    Then status is 400
    And exception is "InvalidEmailRecipientException"

  Scenario: but the email is already used and verified
    Given an existing user
      | email    | max.mustermann@email.com |
      | verified | true                     |
    And a request body
    """
      {
        "culture": "de-DE",
        "name": "Max Mustermann",
        "email": "max.mustermann@email.com",
        "masterPasswordHint": "passwordispassword",
        "masterPasswordHash": "$2a$10$HFeBQjv4d.iGubQvGZe31uMBxWqoaHLQt9O1na7KlFZKhxvPkf7ge",
        "publicKey": "publicKey"
      }
    """
    When I send a post request to /api/users
    Then status is 400
    And exception is "EmailAlreadyExistsException"

  Scenario: but the email is already used and not verified
    Given an existing user
      | email    | max.mustermann@email.com |
      | verified | false                    |
    And a request body
    """
      {
        "culture": "de-DE",
        "name": "Max Mustermann",
        "email": "max.mustermann@email.com",
        "masterPasswordHint": "passwordispassword",
        "masterPasswordHash": "$2a$10$HFeBQjv4d.iGubQvGZe31uMBxWqoaHLQt9O1na7KlFZKhxvPkf7ge",
        "publicKey": "publicKey"
      }
    """
    When I send a post request to /api/users
    Then status is 200
