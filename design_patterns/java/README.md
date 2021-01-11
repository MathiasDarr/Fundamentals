## This directory contains implemntaions & discussions on design patterns in java  ## 

### Singleton Design Pattern ###
- ensures that only a single instance of an object in the JVM


### Builder Pattern ###



### Chain of responsibility ###
Chain of responsibility is a behavioral design pattern that lets you pass requests along a chain of handlers.  Upon receiving a request, each handler decides either to process the request of to pass it io the next handler in the chain



### Bridge ###
Bridge is a structural design pattern that lets you split a large class or a sed  closely related classes into two separate hierarchies.  Abstraction and implementation which can be developed independently of each other.  



### Adapter ###
Structural design pattern that allows object w/ incompatible interfaces to collaborate







### Observer Pattern ###






### Strategy Pattern ###

Encapsulates a family of algorithms

generic interface exposes all strategies.


1) Context maintains a reference to one of the concrete strategies & communicates w/ this object only via the strategy interface
2) Strategy interface is common to all concrete strategies.  It declares a method the context uses to execute a strategy
3) Concrete strategies implement different variations of an algorithm the context uses
4) context calls the execution method on the linked strategy object each time it nees to run the algorithm, context doesn't know what
type of strategy it works with or how the algorithm is executed
5) client creates a specific strategy object and passes it to the contxt, the context exposes a setter which lets clients replaced the strategy 



1) Use strategy when you have a lot of similar classes that only differ in the way they execute some heavior
2) Use the pattern  to isolate the business logic of a class from the implementation details of aglorithms that may not be as import in the context of that logic

