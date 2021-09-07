package org.knoldus.testakkahttproutes

import spray.json.DefaultJsonProtocol

trait BookJsonProtocol extends DefaultJsonProtocol {
  implicit val bookFormat = jsonFormat3(Book)
}
