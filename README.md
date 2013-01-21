Redis Backplane Speed Tests
===========================

Profiles redis read speeds for data structures similar to backplane messages (maps)

Setup
-----

1. Obtain a fresh redis database from v2012.46_RC_7 staging or production(slave),
   initialize and restart your local redis with it.


2. Initialize test data (only once):

    sbt run

runs com.janrain.redistest.ConvertFromBp, which creates a copy of each message it finds as:
- a redis map, prefixed with "redisTest-msg-map-"
- a plain string redis entry, prefixed with "redisTest-msg-string-"

also removes the expiry for all existing v1_message_* entries


Run tests
---------

Running all tests in one pass / same JVM instance with `sbt test` seems to favor the tests running last,
so run them individually:

    sbt
    > test-only com.janrain.redistest.GetChannelLegacyDao

    sbt
    > test-only com.janrain.redistest.GetChannelJavaSerialized

    sbt
    > test-only com.janrain.redistest.GetChannelStringSerialized

    sbt
    > test-only com.janrain.redistest.GetChannelMapSerialized

executes and times Redistest.testSize "getChannelMessages" operations, using:

- legacy DAO:

  the actual BackplaneMessageDAO.getMessagesByChannel from the 2012.46_RC7 jar
  performs sorting (which the other tests do not do)

- JavaSerialized:

  reads the java serialized messages
  instantiates a BackplaneMessage from each serialized entry that is read

- StringSerialized:

  reads the string version the converted messages

- MapSerialized:

  reads the redis map version of the converted messages
  executes .toString (prod would have to do toJsonString)



