package org.knoldus.testakkahttproutes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.MethodRejection
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

class DigitalLibraryServerSpec extends WordSpec with Matchers
  with ScalatestRouteTest
  with BookJsonProtocol
  with SprayJsonSupport {

  val digitalLibraryServer = new DigitalLibraryServer
  val libraryRoute = digitalLibraryServer.route
  val booksInLibrary = digitalLibraryServer.books

  "A digital library server" should {
    "return all the books in the library" in {
      // send an HTTP request through an endpoint that you want to test
      Get("/api/book") ~> libraryRoute ~> check {
        // inspect the response
        status shouldBe StatusCodes.OK
        entityAs[List[Book]] shouldBe booksInLibrary
      }
    }

    "return a book by hitting the query parameter endpoint" in {
      Get("/api/book?id=2") ~> libraryRoute ~> check {
        status shouldBe StatusCodes.OK
        entityAs[Option[Book]] shouldBe Some(Book(2, "Kathy Sierra", "Head First Java"))
      }
    }

    "return a book by calling the endpoint with the id in the path" in {
      Get("/api/book/2") ~> libraryRoute ~> check {
        response.status shouldBe StatusCodes.OK
        entityAs[Option[Book]] shouldBe Some(Book(2, "Kathy Sierra", "Head First Java"))
      }
    }

    "add a new book in the library" in {
      val newBook = Book(5, "Munish K. Gupta", "Akka Essentials")

      Post("/api/book", newBook) ~> libraryRoute ~> check {
        val updatedBooksInLibrary = digitalLibraryServer.books
        status shouldBe StatusCodes.OK
        assert(updatedBooksInLibrary.contains(newBook))
        updatedBooksInLibrary should contain(newBook) // same as above
      }
    }

    "return all the books of a given author" in {
      Get("/api/book/author/Martin%20Odersky") ~> libraryRoute ~> check {
        status shouldBe StatusCodes.OK
        entityAs[List[Book]] shouldBe booksInLibrary.filter(_.author == "Martin Odersky")
      }
    }

    "not accept other methods except POST and GET" in {
      Delete("/api/book") ~> libraryRoute ~> check {
        rejections should not be empty   // "natural language" style
        rejections.should(not).be(empty) // same as above

        val methodRejections = rejections.collect {
          case rejection: MethodRejection => rejection
        }

        methodRejections.length shouldBe 2
      }
    }
  }

}
