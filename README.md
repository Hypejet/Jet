# ✈️ Jet
[![standard-readme compliant](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=flat-square)](https://github.com/RichardLitt/standard-readme)

> [!NOTE]
> Jet is still during development. It should **NOT** be used for normal servers yet.

A brand-new Minecraft server software made from scratch that aims to be multithreaded, user-friendly and to have a modern API.

Jet does not have any vanilla features reimplemented, such as combat or fluids, making it good for simple minigame servers.

## Background
What does Jet do over other servers?

### (Craft-)Bukkit based software (Spigot/Paper)
Minecraft minigame servers are popular for a long time. The oldest ones started from simple minigame servers and ended up with a large server networks.

Since Minecraft release, a lot of features were added, affecting server performance. However, minigame servers don't need all of those features. Some servers may also want to have a multi-threaded solution, which Minecraft doesn't have. That's why server software like Jet and Minestom came up.

Jet doesn't have any vanilla features re-implemented by default, allowing for developers to implement their own mechanics and vanilla mechanics, but only these, which they need.

Our server software isn't ideal. We recommend it for people, for which it's faster to add vanilla mechanics that they need than removing existing mechanics from (Craft-)Bukkit bassed software.
### Minestom
- **Simplicity** Unlike Minestom, Jet uses an approach more similar to that of Spigot and Paper, the server is just a `.jar` that you download and run like a normal application, and after which add plugins to. One downside of this is that it can often lead to clashes between plugins and generally make it harder to develop for, Jet aims to solve this with multiple approaches that you will see below.
- **Customizability** As mentioned Jet will use multiple ways to better integrate your plugin with the server:
  - **Everything is an event** In Jet everything is an event, as you may know from other APIs, events have priorities like `FIRST`, `NORMAL` and `LATE`. Some features utilizing events are: _world loading_ and simple features like _inventories_, _commands_ etc.
- **Quality of Life** Jet includes many QoL features that you may or may not have seen in other servers.

## Install
To install Jet you need to download a server jar file from GitHub, which you run like a normal java application.

## Usage
Using Jet is not very different from CraftBukkit-based software, except for not having vanilla features and their properties.

You have a basic configuration file, which contains the most important settings for the server.

If you want to implement features on your server, you need to make plugins using our [API](https://github.com/Hypejet/Jet/tree/ver/1.0/api), which you put into a `plugins` subdirectory.

## Contributing
You can contribute to Jet easily by creating pull requests, without any other needs.

To not waste your time, make sure to join our [Discord server](https://discord.com/invite/kS4CuPvYD2) and ask if your contribution would be merged or not. You can also make that via an issue, however it may take longer to get a response.

## License
All Jet components (except of API) are licensed under an AGPL 3.0 license.

License of the API however is a MIT license, allowing plugins freedom of licensing or even being close-sourced. 
