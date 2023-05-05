<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
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
* `info` - Show information about the bot!
* `ping` - Show technical information about the bot!
* `remove-my-data` - Request to remove your data from the bot!
* `stats` - Show statistics such as commands run, current servers, and users!
* `support` - Get support for the bot!
* `whois` - Get user information about someone.
##### 2. **CAFE**
* `balance` - Check your balance!
* `donate-beancoins` - Donate some of your `beanCoins` to someone! (Only up to 25 every hour though!)
* `menu` - Show the list of Cafe menu items.
* `order` - Order a menu item for someone!
* `serve` - Get beanCoins! Essentially you run this command by doing `/serve (dictionary word)`! This must be an english word. The longer the word, the more money you get. However, the more popular the word is, the less money you will get for it.
##### 3. **FUN**
* `avatar` - Get yours or someone else's avatar image!
* `banner` - Get yours or someone else's profile banner!
* `birthday` - Add, change, or remove your birthday! Even get someone else's birthday!
* `coffee-meme` - Get a coffee meme!
* `counting-statistics` - Get counting information for your server!
* `joke` - Send a joke in the current channel. (SFW)
* `meme` - Send a meme in the current channel. (SFW)
* `rate` - Rate the percentages of someone! (*somewhat* NSFW)
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

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/beanbeanjuice/cafeBot.svg?style=for-the-badge
[contributors-url]: https://github.com/beanbeanjuice/cafeBot/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/beanbeanjuice/cafeBot.svg?style=for-the-badge
[forks-url]: https://github.com/beanbeanjuice/cafeBot/network/members
[stars-shield]: https://img.shields.io/github/stars/beanbeanjuice/cafeBot.svg?style=for-the-badge
[stars-url]: https://github.com/beanbeanjuice/cafeBot/stargazers
[product-title]: https://cdn.beanbeanjuice.com/images/cafeBot/readme/cafeBot.png
[codefactor-shield]: https://www.codefactor.io/repository/github/beanbeanjuice/cafebot/badge?style=for-the-badge
[codefactor-url]: https://www.codefactor.io/repository/github/beanbeanjuice/cafebot