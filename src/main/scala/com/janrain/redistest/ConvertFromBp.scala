package com.janrain.redistest

import com.redis.RedisClient
import scala.collection.JavaConversions._
import com.janrain.redistest.Constants._
import redis.clients.jedis.Jedis

/**
 * Converts Backplane messages from the 2012.46_RCxx format to:
 * - redis hash maps, keys prefixed with "redisTest-msg-map-"
 * - json strings (as returned by BackplaneMessage.asFrame(), keys prefixed with "redisTest-msg-string-"
 *
 * @author Johnny Bufu
 */
object ConvertFromBp extends App {

  val redis = new RedisClient
  val jedis = new Jedis("localhost")

  val channelIds = redis.keys("v1_channel_idx*")
    .getOrElse(throw new Exception("redis read channel indexes error"))
    .flatMap(_.map(_.substring(15)))

  println("read %d channel IDs".format(channelIds.size))

  val bmdao = new com.janrain.backplane.server.dao.redis.RedisBackplaneMessageDAO

  for {
    channel <- channelIds
    message <- bmdao.getMessagesByChannel(null, channel, null, null)
  } {
    jedis.persist(message.getIdValue)
    redis.hmset(mapKeyPrefix + message.getIdValue, message)
    redis.set(stringKeyPrefix + message.getIdValue, message.asFrame("v1.3"))
    println("converted message " + message.getIdValue)
  }

}
