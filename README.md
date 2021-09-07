# Akka Http Testkit Example
- This template demonstrates how we can test our high level Akka Http Routes using the akka-http-testkit.
- In this project, we have a small server that will allow fetching and adding books to a digital library.  
- For this purpose, I have defined a high level akka http route with some endpoints in DigitalLibraryServer.scala file. 
- To test this akka http route, I have written the test cases in DigitalLibraryServerSpec.scala file. 
## Prerequisites

- Scala Build Tool(SBT), version 1.4.7
- Scala, version 2.12.12
- Akka-Http Testkit, version 10.1.7
## Steps for project execution

- sbt test - To run all the test cases