# ✈️ Jet
###### **Note:** Jet is still very much WIP, Jet should **NOT** be used for normal servers _(yet!)_
[![standard-readme compliant](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=flat-square)](https://github.com/RichardLitt/standard-readme)

A brand new Minecraft server software made from scratch that aims to be multithreaded, user-friendly and to have a modern API.

Jet does not have any vanilla features reimplemented, such as combat or fluids, making it good for simple minigame servers.

## Background
#### What does Jet do over other servers?
### Spigot/Paper
- **See [Why Minestom?](https://github.com/Minestom/Minestom/?tab=readme-ov-file#why-minestom)**
### Minestom
- **Simplicity** Unlike Minestom, Jet uses an approach more similar to that of Spigot and Paper, the server is just a `.jar` that you download and run like a normal application, and after which add plugins to. One downside of this is that it can often lead to clashes between plugins and generally make it harder to develop for, Jet aims to solve this with multiple approaches that you will see below.
- **Customizability** As mentioned Jet will use multiple ways to better integrate your plugin with the server:
  - **Everything is an event** In Jet everything is an event, as you may know from other APIs, events have priorities like `LOW, MEDIUM` and `HIGH`, unlike others Jet has an extra priority named `JET`, this priority will **ONLY** be used by Jet, and allows the developer to change multiple things about the event either before or after it is handled by Jet. Some features utilizing events are: _**ALL** packets_, both outgoing and ingoing, _world loading_ and simple things like _inventories_, _commands_ etc.
- **Quality of Life** Jet includes many QoL features that you may or may not have seen in other servers, some of which include: setting a block only for a specific player, automatic resourcepack creation, any many more!

## Install
To install Jet you need to download a server jar file from Github, which you run like a normal java application.

## Usage
Using Jet is not very different than Craftbukkit based software, except for not having vanilla features and their properties.

You have a basic configuration file, which contains the most important settings for the server.

If you want to implement features on your server, you need to make plugins using our [API](https://github.com/Hypejet/Jet/api), which you put into a `plugins` subdirectory.

## Contributing
You can contribute to Jet easily by creating pull requests, without any other needs.

To not waste your time, make sure to join our [Discord server](https://discord.com/invite/kS4CuPvYD2) and ask if your contribution would be merged or not. You can also make that via an issue, however it may take longer to get a response.

## License
All Jet components (except of API) are licensed under a GPL 3.0 license. However, the API is licensed under a MIT license to allow plugins to be done like their authors want.
