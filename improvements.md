# Improvements
A major improvement is the restructure of our packages.

Before, all our classes are separated into packages by the layers in clean architecture and by usages (so, managers are in a package which is further divided into user managers and admin managers), which, at first sight, seems reasonable. However, this view is misguided as the code has low cohesion and high coupling:
* any manager can access any entity it wants, resulting in code that is difficult to debug since the parts that mutate the same entities are scattered across the entire manager package, which can be seen from this dependency graph:
![demo](https://user-images.githubusercontent.com/19532712/89972949-2031e000-dc2d-11ea-93c7-f34a2184265a.png), which, even after organizing this by the clean architecture circles, is messy：
![demo](https://user-images.githubusercontent.com/19532712/89973046-5cfdd700-dc2d-11ea-998b-4b22564d9172.png).

Note that the User class is used all across the program, making them extremely hard to track.

To resolve this, we make a key observation about this: the dependency on User for many of these classes can be reduced to dependency on Account only! This is the same for Admins too. For example, instead of a LoginManager that directly depends on both User and Account, we make it into LoginManager\<E extends Account>, and when we need it, we can have: LoginManager\<User> and LoginManager\<Admin>. This allows us to divide the packages in a way such that the managers in package core.useritem can only mutate Users and Items.

In the end, we get this graph:
![demo](https://user-images.githubusercontent.com/19532712/89973414-40ae6a00-dc2e-11ea-8dcb-4bb4a70ed862.png), which can easily be partitioned into subpackages depending on the entities they mutate (notice the shape of the graph: there is no class like the User class before).