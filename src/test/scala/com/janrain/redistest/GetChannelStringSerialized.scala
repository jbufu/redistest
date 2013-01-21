package com.janrain.redistest

import org.scalatest.FunSuite
import com.janrain.redistest.Redistest._
import com.janrain.redistest.Constants._
import scala.collection.JavaConversions._

/**
 * @author Johnny Bufu
 */
class GetChannelStringSerialized extends FunSuite {

  test("init") {
    Redistest.channelIds.size
  }

  test("jedisGetStrings") {
    val res = for(x <- 1 to testSize) yield getChannelMessages(jedisGetStrings)(Redistest.randomChannel)
    printRes("jedis GetChannel StringSerialized", res)
  }

  val jedisGetStrings: RedisMessageGetter = msgIds => Redistest.jedis.mget(msgIds.map(stringKeyPrefix + _): _*).toList

}
