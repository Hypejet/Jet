# ✈️ Jet

> [!WARNING]
> Jet is still under development and it is not recommended to use it by normal servers yet.

A high performance, multithreaded Minecraft server software which aims on high customizability without need to manually use packets. Designed for servers with custom behaviour, such as minigame servers.

## Goals
Jet, unlike many already existing options:
- Has a goal to provide high customizability and control via API without need to use packets
- Produces similar packet results to vanilla to be compatible with most of already existing plugins, libraries and clients
- Cares about code simplicity, readability and consistency, without compromising on the performance
- Is designed to be friendly to developers, as well as normal users, thanks to the plugin system

## Use cases
Jet was designed for servers that want to introduce their own - heavily modified or custom - behaviour.
Because of that, it is perfect for most of networks with minigames or custom gamemodes.
However, it is generally **not** recommended for someone who needs to utilize most vanilla features, for example to run a survival server.

## Building
> [!IMPORTANT]
> This project uses a [modified version](https://github.com/Codestech1/adventure/tree/feat/nbt-text-serializer) of [Adventure](https://github.com/KyoriPowered/adventure).
> This is because the [NBT Component serializer pull request](https://github.com/KyoriPowered/adventure/pull/1084) has not been merged yet by original project maintainers.
> You need firstly to publish it to maven local to make Jet compile properly.

Jet uses [Gradle](https://gradle.org/) as a build tool.
The simpliest and most common way to build Jet is executing `./gradlew build` command in the root project directory.

## Running
To run Jet, you firsty need to build it as specified in the [building section](#building).
When you get it done, you can copy the jar with `-all` suffix from the `server/build/libs` directory and run it as a normal terminal Java application.
It is recommended to create a special directory for running a Jet server because it generates configuration files at startup which let you configure the server.

## License
This project is mainly licensed under the [AGPLv3](https://www.gnu.org/licenses/agpl-3.0.en.html) license, however the API is licensed under the [MIT](https://en.wikipedia.org/wiki/MIT_License) license.
