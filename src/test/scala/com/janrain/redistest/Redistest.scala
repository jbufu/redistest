package com.janrain.redistest

import com.redis.RedisClient
import redis.clients.jedis.Jedis
import collection.immutable.IndexedSeq
import com.janrain.redistest.Constants._

object Redistest {

  val testSize = 100000

  val redis = new RedisClient
  val jedis = new Jedis("localhost")

  val bmdao = new com.janrain.backplane.server.dao.redis.RedisBackplaneMessageDAO
  val random: java.util.Random = new java.security.SecureRandom

  val channelIds = redis.keys("v1_channel_idx*")
    .getOrElse(throw new Exception("redis read channel indexes error"))
    .flatMap(_.map(_.substring(15)))
    .toArray

  println("loaded " + channelIds.size /*filter(_._2.size > 1).toMap*/ + " channel IDs")


  def randomChannels = for (x <- 1 to testSize) yield {channelIds(random.nextInt(channelIds.size))}

  def getChannelMessages(redisLib: RedisMessageGetter)(channels: Seq[String]) =
    for (channel <- channels) yield {
      val (ms, res) = time {
        redisLib(getMessageIds(channel))
      }
      ms -> res.size
    }

  /** function that takes a list of message IDs, returns list of messages */
  type RedisMessageGetter = List[String] => List[Any]

  def getMessageIds(channel: String): List[String] =
    Redistest.redis.lrange(channelIdxPrefix + channel, 0, -1)
      .getOrElse(throw new Exception("get message IDS failed for channel " + channel))
      .flatten

  def time[R](block: => R): (Long,R) = {
    val t0 = System.nanoTime()
    val result = block // call-by-name
    val t1 = System.nanoTime()
    ((t1 - t0) / 1000000 , result)
  }

  def printRes(who: String, res: Seq[(Long, Int)]) {
    val totalMsg = res.map(_._2).sum
    val totalMs = res.map(_._1).sum
    println("%s retrieved %d channels / %d total messages, %.1f average msgs/channel, %.3fms average channelGet"
      .format(
      who,
      res.size,
      totalMsg,
      (totalMsg.toDouble / res.size),
      (totalMs.toDouble / res.size)
    ))
  }

}