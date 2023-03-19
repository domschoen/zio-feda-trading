## ZIO FEDA Trading

ZIO implementation of the trading example of Gabriel Volpe excellent book "Functional Event-Driven Architecture". The examble of the book is based on typelevel frameworks and is available here: https://github.com/gvolpe/trading

Here the corresponding ZIO frameworks we use here compared to the book frameworks

| Original example framework / tool                            | Description                                                  | This example framework / tool                                |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Cats                                                         |                                                              | [ZIO](https://github.com/zio/zio)                            |
| Apache Pulsar                                                |                                                              | Kafka,                                                       |
| fs2Kafka                                                     |                                                              |                                                              |
| Kittens                                                      | automatic type class derivation for Cats                     |                                                              |
| Circe                                                        |                                                              | [ZIO Json](https://github.com/zio/zio-json)                  |
|                                                              |                                                              | [ZIO Schema](https://github.com/zio/zio-schema)              |
|                                                              |                                                              | [ZIO Config](https://zio.github.io/zio-config)               |
| fs2                                                          |                                                              | [ZStream](https://zio.dev/reference/stream/zstream/)[R, E, O] |
| [ip4s](https://github.com/Comcast/ip4s)                      | IP Addresses for Scala, Scala.js & Scala Native              |                                                              |
| monocle                                                      |                                                              |                                                              |
| scalacheck                                                   |                                                              |                                                              |
| [Weaver-test](https://disneystreaming.github.io/weaver-test/) | A lean test-framework built on top of cats-effect and fs2    |                                                              |
| http4s                                                       |                                                              | [ZIO Http](https://github.com/zio/zio-http)                  |
| [IronOxide Scala SDK](https://ironcorelabs.com/docs/data-control-platform/scala/) | The IronOxide Scala SDK is a Scala library that integrates IronCore’s privacy, security, and data control solution into your app | [ZIO Logging](https://github.com/zio/zio-logging)            |
| [Neutron](https://github.com/profunktor/neutron)             | [Apache Pulsar](https://pulsar.apache.org/) library          | [ZIO Kafka](https://github.com/zio/zio-kafka)                |
| [Odin](https://github.com/valskalla/odin)                    | Odin library enables functional approach to logging in Scala applications with reasoning and performance as the top priorities |                                                              |
| [Redis4catsEffects](https://github.com/profunktor/redis4cats) | Redis client built on top of [Cats Effect](https://typelevel.org/cats-effect/), [Fs2](http://fs2.io/) and the async Java client [Lettuce](https://lettuce.io/). |                                                              |
| [zerowaste compiler plugin](https://github.com/ghik/zerowaste) | Scala compiler plugin to detect unused expressions           |                                                              |
| DoobieH2                                                     |                                                              |                                                              |
| Flyway                                                       |                                                              |                                                              |
| Tyrian                                                       |                                                              | Laminar                                                      |

 

## Nix

Package management and system configuration as well as rollbacks for upgrades.

Uses a Functional approach i.e artifacts are based on immutability

NixOS is a Linux distribution but we use here nix package manager standalone (outside NixOS).

Here the nix documentation: https://nixos.org/manual/nix/stable/

# Implementation status

Send a fake command to Kafka and subscribe to receive it back (see Usage)

# Usage

```
docker compose up
```

```
sbt feed/run
```

