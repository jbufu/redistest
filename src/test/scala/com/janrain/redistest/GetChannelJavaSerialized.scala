package com.janrain.redistest

import org.scalatest.FunSuite
import com.janrain.redistest.Redistest._
import com.janrain.redistest.Constants._
import scala.collection.JavaConversions._
import com.janrain.backplane.server.BackplaneMessage
import org.apache.commons.lang.SerializationUtils

/**
 * @author Johnny Bufu
 */
class GetChannelJavaSerialized extends FunSuite {

  test("init") {
    Redistest.channelIds.size
  }

  test("jedisGetSerialized") {
    val res = getChannelMessages(jedisGetSerialized)(Redistest.randomChannels)
    printRes("jedis GetChannel JavaSerialized", res)
  }

  val jedisGetSerialized: RedisMessageGetter = msgIds =>
    Redistest.jedis.mget(msgIds.map( id => (origKeyPrefix + id).getBytes("UTF-8")): _*)
    .withFilter(_ != null)
    .map(SerializationUtils.deserialize(_)).toList

}
