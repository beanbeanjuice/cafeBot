<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]
[![WakaTime][wakatime-shield]][wakatime-url]
[![CodeFactor][codefactor-shield]][codefactor-url]
![GitHub release (latest by date)](https://img.shields.io/github/v/release/beanbeanjuice/cafebot?style=for-the-badge)

<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/beanbeanjuice/cafeBot">
    <img src="https://cdn.beanbeanjuice.com/images/cafeBot/readme/logo.gif" alt="Logo" width="260" height="186">
  </a>

  <h1 align="center">cafeBot</h1>

  <p align="center">
    A cafe bot for your discord server!
    <br />
    <a href="https://github.com/beanbeanjuice/cafeBot"><strong>Explore the Docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/beanbeanjuice/cafeBot">View Demo</a>
    ·
    <a href="https://github.com/beanbeanjuice/cafeBot/issues">Report Bug</a>
    ·
    <a href="https://github.com/beanbeanjuice/cafeBot/issues">Request Feature</a>
  </p>
  <p align="center">
    <a href="https://top.gg/bot/787162619504492554">
      <img src="https://top.gg/api/widget/787162619504492554.svg">
    </a>
    <a href="https://discordbotlist.com/bots/787162619504492554">
      <img src="https://discordbotlist.com/api/v1/bots/787162619504492554/widget">
    </a>
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
    <li><a href="#data">What data does this bot store?</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
# About The Project

[![Product Name Screen Shot][product-title]](https://www.beanbeanjuice.com/cafeBot.html)

### Built With

* [Maven](https://maven.apache.org/)
* [Discord JDA](https://github.com/DV8FromTheWorld/JDA)
* [Twitch4J](https://github.com/twitch4j/twitch4j)
* [CafeAPI](https://github.com/beanbeanjuice/Java-Cafe-API-Wrapper)
* [KawaiiAPI](https://github.com/beanbeanjuice/Java-Kawaii-API-Wrapper)

### About the Bot
`cafeBot`, formerly `beanBot` is a general purpose bot that has many features. Many features work across Discord servers. Some features include;
* Global Birthday Checker
* Global Currency
* Global Counting Leaderboard (Only Tells You What Place Number You Are In)
* Interaction Commands
* Moderation Commands
* Poll/Raffle Commands
* Bind Roles to Voice Channels
* AI Responses to Certain Words/Phrases
* And more to come...

<!-- GETTING STARTED -->
# Getting Started

To add this bot to your server, follow these steps:

## Prerequisites

This is an example of how to list things you need to use the software and how to install them.
* A Discord Server
* Administrative Access

## Installation

1. Click this [link](https://discord.com/api/oauth2/authorize?client_id=787162619504492554&permissions=8&scope=bot%20applications.commands).
1. Enjoy!

<!-- USAGE EXAMPLES -->
# Usage

#### **The Help Command**
* `/help` - Shows a list of command section.
* `/help (command section name)` - Shows a list of commands in that section.
* `/help (command name/alias)` - Shows how to use the command.
* `/get-prefix` - Gets the current prefix for the server if you forget it!

<p align="center">
  <img src=https://cdn.beanbeanjuice.com/images/cafeBot/readme/help.gif>
</p>

As you can see, this was shown using the command `/help order`. It shows each parameter you can run. For example, the first `<NUMBER>` is the `CATEGORY NUMBER` for the menu, needs to be a number, and is required. The `help` command also provides an example on how to actually use the command if you are stuck!

#### **Command Section**
*There are many command sections, with more coming soon. To show the commands in a specific section, do `!!help (command section)` or for example, `!!help moderation`!* Doing `!!help (command)` will also show you an example of how to use the command.

##### 1. **GENERIC**
* `bot-donate` - Donate for the bot!
* `bot-invite` - A command to get an invite link for the bot!
* `bot-upvote` - Upvote the bot!
* `bot-version` - Gets a specific/the latest release notes for the bot!
* `bug-report` - Report a bug with the bot.
* `define` - Define a word!
* `feature-request` - Request a bot feature.
* `generate-code` - Generate a random 32-digit long code!
* `help` - Shows the list of command sections and command list for those sections.
* `ping` - Show bot information!
* `remove-my-data` - Request to remove your data from the bot!
* `support` - Get support for the bot!
* `user-info` - Get user information about someone.
##### 2. **CAFE**
* `balance` - Check your balance!
* `donate-beancoins` - Donate some of your `beanCoins` to someone! (Only up to 25 every hour though!)
* `menu` - Show the list of Cafe menu items.
* `order` - Order a menu item for someone!
* `serve` - Get beanCoins! Essentially you run this command by doing `/serve (dictionary word)`! This must be an english word. The longer the word, the more money you get. However, the more popular the word is, the less money you will get for it.
##### 3. **FUN**
* `avatar` - Get yours or someone else's avatar image!
* `birthday` - Add, change, or remove your birthday! Even get someone else's birthday!
* `coffee-meme` - Get a coffee meme!
* `counting-statistics` - Get counting information for your server!
* `joke` - Send a joke in the current channel. (SFW)
* `meme` - Send a meme in the current channel. (SFW)
* `snipe` - Snipe a recently deleted message! (30 Seconds)
* `tea-meme` - Get a tea meme!
##### 4. **GAMES**
* `8-ball` - Ask a yes or no question!
* `coin-flip` - Flip a coin!
* `connect-4` - Play connect four with someone!
* `dice-roll` - Roll a dice!
* `get-game-data` - See your win streaks for the mini-games that support it!
* `tic-tac-toe` - Play tic tac toe with someone!
##### 5. **SOCIAL**
* `member-count` - Get the member count for your server!
* `vent` - Anonymously vent to the server! ~~If the server has anonymous venting enabled...~~
##### 6. **INTERACTION**
* `bite` - Bite someone!
* `blush` - Blush at someone!
* `bonk` - Bonk someone! Send them to `h o r n i` jail.
* `cry` - Cry at someone!
* `cuddle` - Cuddle someone!
* `dab` - Dab at someone!
* `dance` - Dance with someone!
* `die` - Just straight up die.
* `headpat` - Give head pats to someone!
* `hmph` - Hmph at someone!
* `hug` - Hug someone!
* `kiss` - Kiss someone!
* `lick` - Lick... someone... ummm why?
* `nom` - Nom at someone!
* `poke` - Poke someone!
* `pout` - Pout at someone!
* `punch` - Punch someone!
* `rage` - Rage at someone!
* `shush` - Shush someone if they're being too loud!
* `slap` - Slap someone!
* `sleep` - Sleep! (Or sleep with someone...)
* `smile` - Smile at someone!
* `stab` - Stab someone! :O
* `stare` - Stare at someone!
* `throw` - Throw someone!
* `tickle` - Tickle someone!
* `welcome` - Welcome someone... for something!
* `yell` - Yell at someone!
##### 7. **TWITCH**
* `twitch-channel` - Add or remove a twitch channel to receive notifications for!
##### 8. **MODERATION**
* `add-poll` - Create a poll! Currently, you can only have 3 polls due to server costs. This will go up in the future!
* `add-raffle` - Create a raffle! Currently, you can only have 3 raffles due to server costs. This will go up in the future!
* `bind` - Bind a role to a voice channel! This gives the user a role when they enter a voice channel, and removes it when they leave.
* `clear-chat` - Clear the chat. (Only currently works from 2-99 messages).
* `create-embed` - Send a customised `embedded message` in a specified channel!
##### 9. **SETTINGS**
* `birthday-channel` - Set or remove the birthday channel for the server!
* `ai` - Sets the `AI Status` for the server. This can `enable` or `disable` the AI module. This is `disable` by default.
* `bot-update` - `enable` or `disable` bot notifications. This is `enabled` by default.
* `counting-channel` - Set or remove the counting channel. Users in this channel can count. You can also apply a custom role when a user sucks at counting!
* `daily-channel` - Set or remove the daily channel. This channel resets daily!
* `list-custom-channels` - Lists all of the custom channels in the server.
* `log-channel` - Set or remove the log channel. If enabled, some logs will be sent to this channel.
* `poll-channel` - Set or remove the poll channel. If enabled, created polls will be sent to this channel.
* `raffle-channel` - Set or remove the raffle channel. If enabled, created raffles will be sent to this channel.
* `twitch-notifications` - Set or remove the twitch notifications channel. If enabled, you will receive notifications for specified channels that you have added.
* `venting-channel` - Set or remove the venting channel. If enabled, this will allow users to anonymously vent.
* `welcome-channel` - Set or remove the welcome channel. If enabled, it will welcome users with a cute message when they join the server.
##### 10. **EXPERIMENTAL**
* `Nothing here yet!`

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

<!-- DATA THIS BOT STORES -->
# Privacy Policy

Here is ALL of the data that this bot could *possibly* store on you. This, of course, is subject to change. None of these can be accessed by other servers (that you are not a part of) by the bot.

- *__Discord Data__*
  - Your Discord ID.`*`
    - This data is needed in order to keep track of the amount of money `(beanCoins)` with the bot. Additionally, it is used for the counting module to enforce the rule that the same Discord user cannot play the counting game twice in a row.
  - Your Discord Server ID.`*`
    - This data is needed in order to remember specific settings you have for the server, such as the prefix you use for the bot, the twitch channels you have added, the moderation role ID, the log channel ID, etc.
  - Your Discord Server Role IDs.`**`
    - Needed in conjunction with your `Discord Server ID` in order to keep track of specific roles to add to users when they join a VC or a moderation role ID.
  - Your Discord Server Text Channel IDs.`**`
    - Needed in order to keep track of which channels are which. For example, to know if the text channel you are talking in is the `counting` channel.
  - Your Discord Server Voice Channel IDs.`**`
    - Needed in order to know what role to add to what voice channel when joining.

- *__Other Data__*
  - Your birthday.`***`
    - Your birthday can be removed any time. It can only be accessed by people in the same server as you. If they are not in the same server as you, they cannot see this.
  - Message.
    - Your messages **ARE NOT** saved **AT ALL**. The only thing the bot does, if enabled, is check if trigger words are sent in a message, then sends a pre-determined response.

##### `*` - This is already public information anyway, and is crucial for having the bot work.
##### `**` - This is something that is needed for the bot to do things like apply a role to someone when they join a specific voice channel, or to duplicate a voice channel when needed.
##### `***` - Your birthday is only stored WHEN specified, and can even be removed at anytime by simply using the `/birthday remove` command.

<!-- CONTACT -->
# Contact

- beanbeanjuice
- Discord: [beanbeanjuice#4595](https://discord.gg/KrUFw3uHST)
- YouTube: [beanbeanjuice](https://www.youtube.com/beanbeanjuice)
- TikTok: [@beanbeanjuiceofficial](https://www.tiktok.com/@beanbeanjuiceofficial)
- Twitter: [@beanbeanjuice](https://twitter.com/beanbeanjuice)
- Email: beanbeanjuice@outlook.com
- Project Link: [GitHub](https://github.com/beanbeanjuice/cafeBot)

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
[wakatime-shield]: https://wakatime.com/badge/github/beanbeanjuice/cafeBot.svg?style=for-the-badge
[wakatime-url]: https://wakatime.com/badge/github/beanbeanjuice/cafeBot
[product-title]: https://cdn.beanbeanjuice.com/images/cafeBot/readme/cafeBot.png
[codefactor-shield]: https://www.codefactor.io/repository/github/beanbeanjuice/cafebot/badge?style=for-the-badge
[codefactor-url]: https://www.codefactor.io/repository/github/beanbeanjuice/cafebot