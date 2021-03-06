package com.janrain.redistest

import com.janrain.redistest.Redistest._
import com.janrain.redistest.Constants._
import org.scalatest.FunSuite


/**
 * @author Johnny Bufu
 */
class GetChannelScalaRedisLib extends FunSuite {


  ignore("scalaLibGetSerialized") {
    val res = getChannelMessages(redisGetSerialized)(Redistest.randomChannels)
    printRes("redis GetSerialized", res)
  }

  ignore("scalaLibGetStrings") {
    val res = getChannelMessages(redisGetStrings)(Redistest.randomChannels)
    printRes("redis    GetStrings", res)
  }

  ignore("scalaLibGetMaps") {
    val res = getChannelMessages(redisGetMaps)(Redistest.randomChannels)
    printRes("redis       GetMaps", res)
  }

  val redisGetStrings: RedisMessageGetter = msgIds => {
    if (msgIds.isEmpty) List.empty
    else Redistest.redis.mget[String](stringKeyPrefix + msgIds.head, msgIds.tail.map(stringKeyPrefix + _): _*)
      .getOrElse(throw new Exception("redisGetStrings returned None"))
      .flatten
  }

  val redisGetMaps: RedisMessageGetter = msgIds =>
    Redistest.redis.pipeline(p => msgIds.map(id => p.hgetall(mapKeyPrefix + id)))
      .getOrElse(throw new Exception("redisGetMaps returned None"))

  val redisGetSerialized: RedisMessageGetter = msgIds => {
    if (msgIds.isEmpty) List.empty
    else Redistest.redis.mget(origKeyPrefix + msgIds.head, msgIds.tail.map(origKeyPrefix + _): _*)
      .getOrElse(throw new Exception("redisGetSerialized returned None"))
      .flatten
  }

}
