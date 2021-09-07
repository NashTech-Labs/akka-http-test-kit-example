package org.knoldus.testakkahttproutes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class DigitalLibraryServer extends BookJsonProtocol with SprayJsonSupport{

  var books = List(
    Book(1, "Martin Odersky", "Programming in Scala"),
    Book(2, "Kathy Sierra", "Head First Java"),
    Book(3, "Joshua Sureth", "Scala in Depth"),
    Book(4, "Christian Baxter","Mastering Akka")
  )

  /**
   * GET /api/book - returns all the books from the library
   * GET /api/book/x - return a single book with id x
   * GET /api/book?id=x - return a single book with id equal to x
   * POST /api/book - adds a new book to the library
   * GET /api/book/author/x - returns all the books from the library whose author is x.
   */

  val route: Route =
    pathPrefix("api" / "book") {
      (path("author" / Segment) & get) { author =>
        complete(books.filter(_.author == author))
      } ~
      get {
        (path(IntNumber) | parameter('id.as[Int])) { id =>
          complete(books.find(_.id == id))
        } ~
        pathEndOrSingleSlash {
          complete(books)
        }
      } ~
      post {
        entity(as[Book]) { book =>
          books = books :+ book
          complete(StatusCodes.OK)
        } ~
        complete(StatusCodes.BadRequest)
      }
    }

}
