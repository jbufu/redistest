package com.janrain.redistest

import org.scalatest.FunSuite
import com.janrain.redistest.Redistest._
import com.janrain.redistest.Constants._


/**
 * @author Johnny Bufu
 */
class GetChannelMapSerialized extends FunSuite {

  test("init") {
    Redistest.channelIds.size
  }

  test("jedisGetMaps") {
    val res = for(x <- 1 to testSize) yield getChannelMessages(jedisGetMaps)(Redistest.randomChannel)
    printRes("jedis    GetChannel MapSerialized", res)
  }

  val jedisGetMaps: RedisMessageGetter = msgIds => {
    val p = Redistest.jedis.pipelined()
    val res = msgIds.map(id => p.hgetAll(mapKeyPrefix + id))
    p.sync()
    res.map(_.get.toString) // prod would be toJsonString, a bit more expensive
  }

}
