<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/beanbeanjuice/cafeBot">
    <img src="images/logo.gif" alt="Logo" width="260" height="186">
  </a>

  <h3 align="center">cafeBot</h3>

  <p align="center">
    A cafe bot for your discord server!
    <br />
    <a href="https://github.com/beanbeanjuice/cafeBot"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/beanbeanjuice/cafeBot">View Demo</a>
    ·
    <a href="https://github.com/beanbeanjuice/cafeBot/issues">Report Bug</a>
    ·
    <a href="https://github.com/beanbeanjuice/cafeBot/issues">Request Feature</a>
  </p>
</p>



<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
# About The Project

[![Product Name Screen Shot][product-title]](https://www.beanbeanjuice.com/cafeBot.html)


### Built With

* [Gradle](https://gradle.org/)
* [Discord JDA](https://github.com/DV8FromTheWorld/JDA)
* [Lavaplayer](https://github.com/sedmelluq/lavaplayer)
* [Spotify Web API](https://github.com/thelinmichael/spotify-web-api-java)
* [Twitch4J](https://github.com/twitch4j/twitch4j)



<!-- GETTING STARTED -->
# Getting Started

To add this bot to your server, follow these steps.

## Prerequisites

This is an example of how to list things you need to use the software and how to install them.
* A Discord Server
* Administrative Access

## Installation

1. Click this [link](https://discord.com/api/oauth2/authorize?client_id=787162619504492554&permissions=8&scope=bot).
2. Give the bot administrative access. `I promise it's safe. You can view all the code for yourself.`
3. Enjoy!



<!-- USAGE EXAMPLES -->
# Usage

#### **The Help Command**
* `!!help` - Shows a list of command section.
* `!!help (command section name)` - Shows a list of commands in that section.
* `!!help (command name/alias)` - Shows how to use the command.

<p align="center">
  <img src=images/help.png>
</p>

As you can see, this was shown using the command `!!help order`. It shows each parameter you can run. For example, `<parameter 1>` needs to be a number and it is `REQUIRED` and `<parameter 2>` is a discord user and is also `REQUIRED`. So this means, to run the `order` command, I would do something like, `!!order 2 @beanbeanjuice`.

#### **Command Section**
*There are many command sections, with more coming soon. To show the commands in a specific section, do `!!help (command section)` or for example, `!!help moderation`!* Doing `!!help (command)` will also show you an example of how to use the command.

##### 1. **GENERIC**
* `help` - Shows the list of command sections and command list for those sections.
* `ping` - Show bot information! Currently, you can only have 3 polls per Discord server due to server costs.
* `feature-request` - Request a bot feature.
* `bug-report` - Report a bug with the bot.
##### 2. **CAFE**
* `menu` - Show the list of Cafe menu items.
* `serve` - Get beanCoins! Essentially you run this command by doing `!!serve (dictionary word)`! This must be an english word. The longer the word, the more money you get. However, the more popular the word is, the less money you will get for it.
* `order` - Order a menu item for someone!
* `balance` - Check your balance!
##### 3. **FUN**
* `meme` - Send a meme in the current channel. (SFW)
* `joke` - Send a joke in the current channel. (SFW)
* `add-poll` - Create a poll!
* `add-raffle` - Create a raffle!
* `avatar` - Get yours or someone else's avatar image!
* `get-birthday` - Get yours or someone else's birthday!
##### 4. **INTERACTION**
* `hug` - Hug someone!
* `punch` - Punch someone!
* `kiss` - Kiss someone!
* `bite` - Bite someone!
* `blush` - Blush at someone!
* `cuddle` - Cuddle someone!
* `nom` - Nom at someone!
* `poke` - Poke someone!
* `slap` - Slap someone!
* `stab` - Stab someone! :O
* `hmph` - Hmph at someone!
* `pout` - Pout at someone!
* `throw` - Throw someone!
* `smile` - Smile at someone!
* `stare` - Stare at someone!
* `tickle` - Tickle someone!
* `rage` - Rage at someone!
* `yell` - Yell at someone!
* `headpat` - Give head pats to someone!
* `cry` - Cry at someone!
##### 5. **MUSIC**
* `play` - Play a song or playlist!
* `now-playing` - Show the current song playing.
* `pause` - Pause the current song.
* `queue` - Check the current queue.
* `repeat` - Choose to repeat the song or playlist!
* `shuffle` - Shuffle the current playlist.
* `skip` - Skip the current song.
* `stop` - Stop the queue and make the bot leave the channel.
##### 6. **TWITCH**
* `set-live-channel` - Set the current channel to receive twitch live notifications. You need to add twitch channels for this to work.
* `add-twitch-channel` - Add a twitch channel to leave bot notifications for.
* `remove-twitch-channel` - Remove a twitch channel you are currently receiving notifications for.
* `get-twitch-channels` - Get a list of the twitch channels you are currently receiving notifications for.
* `set-live-notifications-role` - Set the role to be mentioned when someone goes live.
##### 7. **MODERATION**
* `set-moderator-role` - Set the moderator role for the server. This is needed so that users with this role can run commands that require a moderator role.
* `set-muted-role` - Set the muted role for the server. This is needed so that users with this role can be stopped from talking in channels. This requires you to set it this way in each channel. This will not mute them if the role does not have the right permissions.
* `change-prefix` - Change the prefix from the bot to your desired prefix.
* `kick` - Kick a specified user.
* `ban` - Ban a specified user.
* `clear-chat` - Clear the chat. (Only currently works from 2-99 messages).
* `mute` - Mute a specified user.
* `un-mute` - Unmute a specified user.
* `set-update-channel` - Sets the current channel to the update channel. This means if you have `notify-on-update` set to `Enabled`, you will receive bot updates in this channel.
* `notify-on-update` - `Enable` or `Disable` bot notifications. This is `Enabled` by default.
* `set-counting-channel` - Sets the current channel to the counting channel. This does exactly as it says. Try to count as high as you can without messing up!
* `set-poll-channel` - Sets the current channel to an active poll channel. This is needed if you want to run polls on your server!
* `set-raffle-channel` - Sets the current channel to an active raffle channel. This is needed if you want to run raffles on your server!
* `set-birthday-channel` - Sets the current channel to an active birthday channel. Be notified when one of the members in your server is having a birthday!


<!-- ROADMAP -->
# Roadmap

See the [open issues](https://github.com/beanbeanjuice/cafeBot/issues) for a list of proposed features (and known issues).



<!-- CONTRIBUTING -->
# Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



<!-- LICENSE -->
# License

Distributed under the GPL-3.0 License. See `LICENSE` for more information.



<!-- CONTACT -->
# Contact

Your Name - [@beanbeanjuice](https://twitter.com/beanbeanjuice) - beanbeanjuice@outlook.com

Project Link: [https://github.com/beanbeanjuice/cafeBot](https://github.com/beanbeanjuice/cafeBot)



<!-- ACKNOWLEDGEMENTS -->
# Acknowledgements

* *There's nothing here yet... maybe in the future?*





<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/beanbeanjuice/cafeBot.svg?style=for-the-badge
[contributors-url]: https://github.com/beanbeanjuice/cafeBot/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/beanbeanjuice/cafeBot.svg?style=for-the-badge
[forks-url]: https://github.com/beanbeanjuice/cafeBot/network/members
[stars-shield]: https://img.shields.io/github/stars/beanbeanjuice/cafeBot.svg?style=for-the-badge
[stars-url]: https://github.com/beanbeanjuice/cafeBot/stargazers
[issues-shield]: https://img.shields.io/github/issues/beanbeanjuice/cafeBot.svg?style=for-the-badge
[issues-url]: https://github.com/beanbeanjuice/cafeBot/issues
[license-shield]: https://img.shields.io/github/license/beanbeanjuice/cafeBot.svg?style=for-the-badge
[license-url]: https://github.com/beanbeanjuice/cafeBot/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/beanbeanjuice
[product-title]: images/cafeBot.png
[help-screenshot]: images/help.png