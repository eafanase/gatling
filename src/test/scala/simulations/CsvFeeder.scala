package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CsvFeeder extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")

  val csvFeeder = csv("data/gameCsvFile.csv").circular //циклично прогоняется ниже

  def getSpecificVideoGame() = {
    repeat(10) {
      feed(csvFeeder)
        .exec(http("Get specific video game")
        .get("videogames/${gameId}") //gameId is taken from the gameCsvFile.csv
        .check(jsonPath("$.name").is("${gameName}")) //gameName is taken from the gameCsvFile.csv
        .check(status.is(200)))
        .pause(1)
    }
  }

  val scn = scenario("Csv Feeder test") //сам сценарий, в котором выз-ся метод
      .exec(getSpecificVideoGame())



  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
