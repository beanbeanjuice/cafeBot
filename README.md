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
[![WakaTime][wakatime-shield]][wakatime-url]

<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/beanbeanjuice/cafeBot">
    <img src="http://cdn.beanbeanjuice.com/images/cafeBot/readme/logo.gif" alt="Logo" width="260" height="186">
  </a>

  <h1 align="center">cafeBot</h1>

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
  <figure><embed src="https://wakatime.com/share/@beeb4317-977b-4b19-878a-21e9aa8e43ed/31fd7762-6212-4dd2-b4d7-4bfdc2a3b5a3.svg"></embed></figure>
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

* [Gradle](https://gradle.org/)
* [Discord JDA](https://github.com/DV8FromTheWorld/JDA)
* [Lavaplayer](https://github.com/sedmelluq/lavaplayer)
* [Spotify Web API](https://github.com/thelinmichael/spotify-web-api-java)
* [Twitch4J](https://github.com/twitch4j/twitch4j)

### About the Bot
`cafeBot`, formerly `beanBot` is a general purpose bot that has many features. Many features work across Discord servers. Some features include;
* Global Birthday Checker
* Global Currency
* Global Counting Leaderboard (Only Tells You What Place Number You Are In)
* Interaction Commands
* Moderation Commands
* Poll/Raffle Commands
* Music Player
* Bind Roles to Voice Channels
* AI Responses to Certain Words/Phrases
* And more to come...

<!-- GETTING STARTED -->
# Getting Started

To add this bot to your server, follow these steps.

## Prerequisites

This is an example of how to list things you need to use the software and how to install them.
* A Discord Server
* Administrative Access

## Installation

1. Click this [link](https://discord.com/api/oauth2/authorize?client_id=787162619504492554&permissions=305654886&scope=bot).
2. Give the bot administrative access. `I promise it's safe. You can view all the code for yourself.`
3. Enjoy!

<!-- USAGE EXAMPLES -->
# Usage

#### **The Help Command**
* `!!help` - Shows a list of command section.
* `!!help (command section name)` - Shows a list of commands in that section.
* `!!help (command name/alias)` - Shows how to use the command.
* `!!get-prefix` - Gets the current prefix for the server if you forget it!

<p align="center">
  <img src=http://cdn.beanbeanjuice.com/images/cafeBot/readme/help.png>
</p>

As you can see, this was shown using the command `!!help order`. It shows each parameter you can run. For example, the first `<NUMBER>` is the `CATEGORY NUMBER` for the menu, needs to be a number, and is required. The `help` command also provides an example on how to actually use the command if you are stuck!

#### **Command Section**
*There are many command sections, with more coming soon. To show the commands in a specific section, do `!!help (command section)` or for example, `!!help moderation`!* Doing `!!help (command)` will also show you an example of how to use the command.

##### 1. **GENERIC**
* `help` - Shows the list of command sections and command list for those sections.
* `ping` - Show bot information!
* `feature-request` - Request a bot feature.
* `bug-report` - Report a bug with the bot.
* `support` - Get support for the bot!
* `invite-bot` - A command to get an invite link for the bot!
* `user-info` - Get user information about someone.
* `member-count` - Get the member count for your server!
* `bot-upvote` - Upvote the bot!
* `bot-donate` - Donate for the bot!
* `remove-my-data` - Request to remove your data from the bot!
* `generate-code` - Generate a random 32-digit long code!
* `get-bot-release-version` - Gets a specific/the latest release notes for the bot!
##### 2. **CAFE**
* `menu` - Show the list of Cafe menu items.
* `serve` - Get beanCoins! Essentially you run this command by doing `!!serve (dictionary word)`! This must be an english word. The longer the word, the more money you get. However, the more popular the word is, the less money you will get for it.
* `order` - Order a menu item for someone!
* `balance` - Check your balance!
* `bc-donate` - Donate some of your `beanCoins` to someone! (Only up to 25 every hour though!)
##### 3. **FUN**
* `coffee-meme` - Get a coffee meme!
* `tea-meme` - Get a tea meme!
* `meme` - Send a meme in the current channel. (SFW)
* `joke` - Send a joke in the current channel. (SFW)
* `add-poll` - Create a poll! Currently, you can only have 3 polls due to server costs. This will go up in the future!
* `add-raffle` - Create a raffle! Currently, you can only have 3 raffles due to server costs. This will go up in the future!
* `avatar` - Get yours or someone else's avatar image!
* `get-birthday` - Get yours or someone else's birthday!
* `set-birthday` - Set your birthday! `(MM-DD)`
* `remove-birthday` - Remove your birthday.
* `counting-statistics` - Get counting information for your server!
##### 4. **GAMES**
* `8-ball` - Ask a yes or no question!
* `coin-flip` - Flip a coin!
* `dice-roll` - Roll a dice!
* `tic-tac-toe` - Play tic tac toe with someone!
* `connect-four` - Play connect four with someone!
* `get-game-data` - See your win streaks for the mini-games that support it!
##### 5. **SOCIAL**
* `vent` - Anonymously vent to the server! ~~If the server has anonymous venting enabled...~~
##### 6. **INTERACTION**
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
* `dance` - Dance with someone!
* `dab` - Dab at someone!
* `bonk` - Bonk someone! Send them to `h o r n i` jail.
* `sleep` - Sleep! (Or sleep with someone...)
* `die` - Just straight up die.
* `welcome` - Welcome someone... for something!
* `lick` - Lick... someone... ummm why?
* `shush` - Shush someone if they're being too loud!
##### 7. **MUSIC**
* `play` - Play a song or playlist!
* `now-playing` - Show the current song playing.
* `pause` - Pause the current song.
* `queue` - Check the current queue.
* `repeat` - Choose to repeat the song or playlist!
* `shuffle` - Shuffle the current playlist.
* `skip` - Skip the current song.
* `stop` - Stop the queue and make the bot leave the channel.
* `play-last` - Puts the song that is at the back of the queue in the front of the queue.
##### 8. **TWITCH**
* `set-live-channel` - Set the current channel to receive twitch live notifications. You need to add twitch channels for this to work.
* `add-twitch-channel` - Add a twitch channel to leave bot notifications for.
* `remove-twitch-channel` - Remove a twitch channel you are currently receiving notifications for.
* `get-twitch-channels` - Get a list of the twitch channels you are currently receiving notifications for.
* `set-live-notifications-role` - Set the role to be mentioned when someone goes live.
##### 9. **MODERATION**
* `set-log-channel` - Sets the log channel for the guild.
* `set-update-channel` - Sets the current channel to the update channel. This means if you have `notify-on-update` set to `Enabled`, you will receive bot updates in this channel.
* `set-counting-channel` - Sets the current channel to the counting channel. This does exactly as it says. Try to count as high as you can without messing up!
* `set-poll-channel` - Sets the current channel to an active poll channel. This is needed if you want to run polls on your server!
* `set-raffle-channel` - Sets the current channel to an active raffle channel. This is needed if you want to run raffles on your server!
* `set-birthday-channel` - Sets the current channel to an active birthday channel. Be notified when one of the members in your server is having a birthday!
* `set-moderator-role` - Set the moderator role for the server. This is needed so that users with this role can run commands that require a moderator role.
* `set-muted-role` - Set the muted role for the server. This is needed so that users with this role can be stopped from talking in channels. This requires you to set it this way in each channel. This will not mute them if the role does not have the right permissions.
* `change-prefix` - Change the prefix from the bot to your desired prefix.
* `kick` - Kick a specified user.
* `ban` - Ban a specified user.
* `clear-chat` - Clear the chat. (Only currently works from 2-99 messages).
* `mute` - Mute a specified user.
* `un-mute` - Unmute a specified user.
* `notify-on-update` - `Enable` or `Disable` bot notifications. This is `Enabled` by default.
* `create-embed` - Send a customised `embedded message` in a specified channel!
* `voice-role-bind` - Bind specific role(s) to a `voice channel`!
* `get-voice-role-binds` - See which `voice channels` have `roles` bound to them!
* `set-venting-channel` - Sets the current channel to an anonymous venting channel!
* `ai-status` - Sets the `AI Status` for the server. This can `enable` or `disable` the AI module.
* `set-daily-channel` - Sets the current channel to a daily channel. This channel is deleted and re-made once a day!
* `get-custom-channels` - Gets ALL of the custom channels currently being used in the server!
* `set-welcome-channel` - Sets the current channel to welcome users when they join!
* `edit-welcome-message` - Edits the welcome message that is sent when users join. (Make sure to check the usage for this command doing `help edit-welcome-message` because this one is a little complicated!)
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

<!-- DATA THIS BOT STORED -->
# Data

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

##### * - This is already public information anyway, and is crucial for having the bot work.
##### ** - This is something that is needed for the bot to do things like apply a role to someone when they join a specific voice channel, or to duplicate a voice channel when needed.
##### *** - Your birthday is only stored WHEN specified, and can even be removed at anytime by simply using the `remove-birthday` command.

<!-- CONTACT -->
# Contact

- beanbeanjuice
- Twitter [@beanbeanjuice](https://twitter.com/beanbeanjuice)
- Email - beanbeanjuice@outlook.com
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
[product-title]: http://cdn.beanbeanjuice.com/images/cafeBot/readme/cafeBot.png