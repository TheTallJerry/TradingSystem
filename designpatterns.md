# Design Patterns
This document explains the usage of design patterns in our project, and gives reasons for using/not using each specific patterns.

## Command Pattern
The point of command pattern is to encapsulate all the information to perform an action, and this is fit for the mandatory undo functionality. The class that implements this pattern is the ActionReverter class and all its subclasses. 

The reason we are using this pattern is that it gives a unifying way to define what is undoable and what is not: if there is a class called XReverter, then X can be undone. There is no need to inspect any code.

The patten also results in more generic manipulation in our Controller and Facade levels, since all they need to know is that there are a list of reverters. The controller can categorize ActionReverters by the methods **getAssociatedUsername** and **getActionType** without having any information on what the reverters actually do. This makes writing code easy.

Most importantly, with this, there needs to be little change in existing code to add new actions and the associated reverters. One simply writes the new actin method and add a new ActionReverter.

One counterargument for the use of this is that it directly manipulates the entities but not through Manager classes. This results in code that could be hard to debug since it is difficult to track down who modified what. Luckily, during refactoring, we were able to resolve by restructuring our core code (Use cases & Entities) so that each the code in a subpackage only modifies the entities in the subpackage itself. So, for example, the reverters that need to mutate a User are all stored in core.user.reverter. Please read more about this in our improvement document.


## Dependency Injection
Dependency injection is used throughout the program. 

One example is inside the generic AccountManager\<E extends Account> in core.account package. Here, the AccountManager operates on a list of Account which are abstract classes. Instead of initializing a list of Accounts by itself, it takes in a list of accounts from the constructor. 

As a side note, the AccountManager is a generic, meaning that the manager can manage any list of any objects that are of type Account. This serves the basis of **UserAccountManager** and **AdminAccountManager** and is easily extensible to any new type of accounts.

The most interesting example of dependency injection occurs at the GUI level. In particular, each Screen classes in client.guiandpresenter. Here, each Screen requires a **DataSerializer** in the constructor, which serializes and deserializes the dataBundle. And **DataSerializer** is an interface implemented by **LocalDataSerializer**, which serializes the data to local disk files. This decision is left to the implementation, and the GUI classes only depends on the abstraction. So it is easy to swap **LocalDataSerializer** out with **ServerDataSerializer**.

## Facade Pattern
The facade pattern is implemented by core.AdminFacade and core.UserFacade. The reason this is used is twofold: to eliminate the dependency of controllers on entities and to simplify the interface for controllers. 

With the two facade we have, we are able completely separate the "client" and "core" code, meaning that clients such as controllers do not need to know anything about the entities. For example, strings that represent something in the core are turned into actual core objects (e.g username --> Users) by the facade layer, so that the controller does not need to worry about this. Similarly, core objects like **ActionReverters** are also translated into displayable strings by the facade.

Moreoever, the facades hides away any complicated collaborations between different managers so controllers can focus on what they do, which is taking in inputs and passing them into the core for processing.

## (Abstract) Factory Methods
Factory methods are not used in our project because our project does not consist of inheritance hierarchies that can benefit from factory methods.

Take ActionReverters for example. Although the inheritance hierarchy is quite large and we do want to write code that is generic enough to handle all ActionReverters, this simply does not require a factory: having a centralized factory for it does not make sense! They each have vastly different reasons to exists because they are the result of very different actions. It will not help to call factory.createReverter() every time after a manager has done some action, because the manager are responsible for creating those themselves.

## Builder Pattern
The builder pattern came close to be implemented. We originally wanted to use builder pattern the resolve the issue of extremely long method signatures for the facade classes. However, the builder pattern is not suited for this since:
* every parameter is necessary for the creation of Facade, so missing one step causes the facade to be unusable,
* there are only two facade classes (with almost completely different methods), so even if we implement the pattern, we will not be making much out of it.

## Observer Pattern
This pattern is removed from our project because trades are no longer entities that can operate on their own data and notify other entities. We used to have this since when two traders confirm transactions, the items will be automatically exchanged (the Users are observers). However, due to having too complicated logic inside entitie objects, this layer of logic is moved to managers which can directly manipulate both Users and Meetings, so there is no need for such pattern anymore.

