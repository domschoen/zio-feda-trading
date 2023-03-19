package trading.feed

import org.apache.kafka.clients.producer.ProducerRecord
import zio._
import zio.json._
import zio.kafka.consumer._
import zio.kafka.producer.{Producer, ProducerSettings}
import zio.kafka.serde._
import zio.stream.ZStream

import java.time.OffsetDateTime
import java.util.UUID

import trading.feed.Feed
import trading.feed.Feed.KafkaSerde

import trading.commands._

// the feed service:
// 1) generates random trading and forecasting commands
// 2) publishes them to Kafka to trigger the response of the entire system
object Main extends ZIOAppDefault {
  private val BOOSTRAP_SERVERS = List("localhost:29092")
  private val KAFKA_TOPIC = "trading-commands"

  private val producer: ZLayer[Any, Throwable, Producer] =
    ZLayer.scoped(
      Producer.make(
        ProducerSettings(BOOSTRAP_SERVERS)
      )
    )

  private val consumer: ZLayer[Any, Throwable, Consumer] =
    ZLayer.scoped(
      Consumer.make(
        ConsumerSettings(BOOSTRAP_SERVERS)
          .withGroupId("streaming-kafka-app")
      )
    )

  def run = {
    // Send command to Kafka every 1s
    val p: ZStream[Producer, Throwable, Nothing] =
      ZStream
        .repeatZIO(Random.nextUUID <*> Clock.currentDateTime)
        .schedule(Schedule.spaced(1.second))
        .map { case (uuid, time) =>
          new ProducerRecord(
            KAFKA_TOPIC,
            time.getMinute,
            Feed.cmd
          )
        }
        .via(Producer.produceAll(Feed.KafkaSerde.key, Feed.KafkaSerde.value))
        .drain

    val c: ZStream[Consumer, Throwable, Nothing] =
      Consumer
        .plainStream(Subscription.topics(KAFKA_TOPIC), KafkaSerde.key, KafkaSerde.value)
        .tap(e => Console.printLine(e.value))
        .map(_.offset)
        .aggregateAsync(Consumer.offsetBatches)
        .mapZIO(_.commit)
        .drain

    (p merge c).runDrain.provide(producer, consumer)
  }
}
