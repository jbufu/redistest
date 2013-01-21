package com.janrain.redistest

import org.scalatest.FunSuite
import com.janrain.redistest.Redistest._

/**
 * @author Johnny Bufu
 */
class GetChannelLegacyDao extends FunSuite {

  test("init") {
    Redistest.channelIds.size
  }

  test("jedisLegacyDao") {
    val channels = Redistest.randomChannels
    val res = for(channel <- channels) yield time {
      Redistest.bmdao.getMessagesByChannel("", channel, null, null).size
    }
    printRes("jedis GetChannel legacy DAO      ", res)
  }

}
