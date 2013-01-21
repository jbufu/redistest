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
    val res = for(x <- 1 to testSize) yield time {
      Redistest.bmdao.getMessagesByChannel("", Redistest.randomChannel, null, null).size
    }
    printRes("jedis GetChannel legacy DAO", res)
  }

}
