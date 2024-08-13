[![wakatime](https://wakatime.com/badge/github/beanbeanjuice/Java-Cafe-API-Wrapper.svg?style=for-the-badge)](https://wakatime.com/badge/github/beanbeanjuice/Java-Cafe-API-Wrapper)
[![CodeFactor](https://www.codefactor.io/repository/github/beanbeanjuice/java-cafe-api-wrapper/badge?style=for-the-badge)](https://www.codefactor.io/repository/github/beanbeanjuice/java-cafe-api-wrapper)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/beanbeanjuice/Java-Cafe-API-Wrapper?style=for-the-badge)

<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/beanbeanjuice/cafeBot">
    <img src="https://cdn.beanbeanjuice.com/images/cafeBot/readme/logo.gif" alt="Logo" width="260" height="186">
  </a>

  <h1 align="center">Cafe API Wrapper</h1>

  <p align="center">
    A wrapper for the Cafe API!
    <br />
    <a href="https://github.com/beanbeanjuice/cafe-api-wrapper"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/beanbeanjuice/cafe-api-wrapper">View Demo</a>
    ·
    <a href="https://github.com/beanbeanjuice/cafe-api-wrapper/issues">Report Bug</a>
    ·
    <a href="https://github.com/beanbeanjuice/cafe-api-wrapper/issues">Request Feature</a>
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
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
# About The Project

### Built With

* [Maven](https://maven.apache.org/)

<!-- GETTING STARTED -->
# Getting Started

## Usage

```Java
CafeAPI cafeAPI = new CafeAPI(USERNAME_HERE, PASSWORD_HERE, RequestLocation.RELEASE);
cafeAPI.BIRTHDAY.getUserBirthday(USER_ID_HERE);
```

## Installation

![Maven Central](https://img.shields.io/maven-central/v/com.beanbeanjuice/cafe-api-wrapper?color=%23CBC3E3)

For `Maven`, paste this into your `dependencies` section of your `pom.xml` file.
```XML
<!-- Cafe API -->
<dependency>
  <groupId>com.beanbeanjuice</groupId>
  <artifactId>cafe-api-wrapper</artifactId>
  <version>VERSION</version>
</dependency>
```

For `Gradle`, paste this into your `dependencies` section of your `build.gradle` file.
```Groovy
// Cafe API
implementation group: 'com.beanbeanjuice', name: 'cafe-api-wrapper', version: 'VERSION'
```

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

- beanbeanjuice
- Twitter [@beanbeanjuice](https://twitter.com/beanbeanjuice)
- Email - beanbeanjuice@outlook.com
- Project Link: [GitHub](https://github.com/beanbeanjuice/cafeBot)

<!-- ACKNOWLEDGEMENTS -->
# Acknowledgements

* *There's nothing here yet... maybe in the future?*
