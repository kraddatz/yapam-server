Feature: Create an account

  Scenario: The email is not already used
    Given request with body
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
    When the user posts data to "/api/users"
    Then status is 200

  Scenario: The email does not exist
    Given The email does not exist
    Given request with body
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
    When the user posts data to "/api/users"
    Then status is 400

  Scenario: The email is already used and verified
    Given user with data exists
      | email    | max.mustermann@email.com |
      | verified | true                     |
    And request with body
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
    When the user posts data to "/api/users"
    Then status is 400

  Scenario: The email is already used, but not verified
    Given user with data exists
      | email    | max.mustermann@email.com |
      | verified | false                    |
    And request with body
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
    When the user posts data to "/api/users"
    Then status is 200
