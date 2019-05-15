# 1. Git

## 1.1 Git branch
To standardize the output of ``git branch`` and the branch overview in bitbucket, we use the following template for our branch names:

```
<type>/<branch_name>
```

Allowed **\<type>** values:
* feat (new feature)
* fix (bugfix)
* docs (change to the documentation)
* refactor (refactoring production code, eg. renaming a variable)
* style (formatting, missing semi colons, etc; no production code change)
* test (add or refactor tests, no production code change)
* chore (updating grunt tasks etc; no production code change) 


The **\<branch_name>** has to be in snake_case   

## 1.2 Git
Use the following command to set our template as your commit message:
``git config commit.template .commit-msg``

### Git template
We use the following template to standardize our commit messages:

```
<type>(<scope>): <subject>

<body>

<footer>
```

#### Message Subject
Allowed **\<type>** values:
* feat (new feature, can or should also contain docs and tests)
* fix (bugfix)
* docs (change to the documentation)
* refactor (refactoring production code, eg. renaming a variable)
* style (formatting, missing semi colons, etc; no production code change)
* test (add or refactor tests, no production code change)
* chore (updating grunt tasks etc; no production code change)

Example **\<scope>** values;
* database
* cloud foundry
* mbcimport
* ci/jenkins
* logging
* swagger

#### Message Body
In case **\<subject>** is not detailed enough, the message body must contain the motivation and results of the change. 
More information about writing a commit message can be found [here][commit-template-1] and [here][commit-template-2].

#### Message Footer

In case there is a breaking change with this commit, it has to be mentioned in the footer, describing the contrast to previous behaviour.
  
  
# 2. Package stucture

We developed the following stucture for our project:

```
+-- common
|   +-- annotation
|   |   +-- customAnnotation
|   |   |   +--
|   |   +-- AnnotationHandlerInterceptor
|   |   +--
|   +-- error
|   |   +-- YapamException
|   |   +-- YapamExceptionHandler
|   |   +--
|   +-- service
|   |   +-- 
+-- config
|   +--
+-- examplecontroller1
|   +-- model
|   |   +--
|   +-- ExampleController1.java
|   +-- ExampleService1.java
+-- examplecontroller2
    +-- model
    |   +--
    +-- ExampleController2.java
    +-- ExampleService2.java
```

As the name suggests, the common package contains everything that is required by more than one implementation:

# 3. Naming convention
* **Spring Controller**: ExampleController.java
* **Spring Service**: ExampleService.java 
* **Enum**: ExampleEnum

<!-- referenced links here -->
[commit-template-1]: https://365git.tumblr.com/post/3308646748/writing-git-commit-messages
[commit-template-2]: https://tbaggery.com/2008/04/19/a-note-about-git-commit-++messages.html
[jsonignore]: https://fasterxml.github.io/jackson-annotations/javadoc/2.5/com/fasterxml/jackson/annotation/JsonIgnore.html
[transient]: https://docs.oracle.com/javaee/5/api/javax/persistence/Transient.html