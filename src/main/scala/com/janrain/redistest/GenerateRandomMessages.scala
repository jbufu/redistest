package com.janrain.redistest

import com.redis.RedisClient

/**
 * Generates random map-like structures similar to backplane messages and stores them as:
 * - redis hash maps, keys prefixed with "redisTest-random-map-"
 * - strings, keys prefixed with "redisTest-random-string-"
 *
 * @author Johnny Bufu
 */
object GenerateRandomMessages {

  val testSize = 3000 //args(0).toInt
  assert(testSize > 0)

  val redis = new RedisClient

  private val random: java.util.Random = new java.security.SecureRandom

  private val base62: Array[Char] =
    Array('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
      'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
      'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

  val testDataMaps: Seq[(String,Map[String,String])] = {
    for (m <- 1 to testSize) yield {
      (
        randomString(10),
        (for (e <- 1 to 30) yield {
          randomString(12) -> randomString(50)
        }).toMap
        )
    }
  }
  println("[generated %d maps]".format(testDataMaps.size))

  val res1 = redis.pipeline(p => testDataMaps.foreach(idAndMap => p.hmset("redisTest-random-map-" + idAndMap._1, idAndMap._2)))
  println("[stored " + res1.get.size + " maps]")

  val res2 = redis.pipeline(p =>
    testDataMaps.map( idAndMap => ("redisTest-random-string-" + idAndMap._1, idAndMap._2.toString() ))
                .foreach(idAndString => p.set(idAndString._1, idAndString._2)))
  println("[stored " + res2.get.size + " strings]")


  private def randomString(length: Int) = {
    val randomBytes = new Array[Byte](length)
    random nextBytes randomBytes

    new String(randomBytes.map(b => base62(scala.math.abs(b % base62.length))))
  }
}
