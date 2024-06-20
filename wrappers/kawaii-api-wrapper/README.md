[![wakatime](https://wakatime.com/badge/github/beanbeanjuice/Java-Kawaii-API-Wrapper.svg?style=for-the-badge)](https://wakatime.com/badge/github/beanbeanjuice/Java-Kawaii-API-Wrapper)
[![CodeFactor](https://www.codefactor.io/repository/github/beanbeanjuice/java-kawaii-api-wrapper/badge?style=for-the-badge)](https://www.codefactor.io/repository/github/beanbeanjuice/java-kawaii-api-wrapper)
![Maven Central](https://img.shields.io/maven-central/v/com.beanbeanjuice/kawaii-api-wrapper?color=%23CBC3E3&style=for-the-badge)

<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/beanbeanjuice/cafeBot">
    <img src="https://cdn.beanbeanjuice.com/images/cafeBot/readme/logo.gif" alt="Logo" width="260" height="186">
  </a>

  <h1 align="center">Kawaii API Wrapper</h1>

  <p align="center">
    A wrapper for the Kawaii API!
    <br />
    <a href="https://docs.kawaii.red/"><strong>Explore the docs »</strong></a>
    <br />
    <br />
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

*For Anonymous Requests...*
```Java
KawaiiAPI kawaiiAPI = new KawaiiAPI();
kawaiiAPI.GIF.getGIF("hug");
```

*For Normal Requests...*
```Java
KawaiiAPI kawaiiAPI = new KawaiiAPI(TOKEN_HERE);
kawaiiAPI.GIF.getGIF("hug");
```

Currently, the supported "prompts" are located [here](https://docs.kawaii.red/endpoints/gif). I have no control over the actual endpoint list.

## Installation

![Maven Central](https://img.shields.io/maven-central/v/com.beanbeanjuice/kawaii-api-wrapper?color=%23CBC3E3)

Make sure you replace VERSION with the appropriate version specified above.

For `Maven`, paste this into your `dependencies` section of your `pom.xml` file.
```XML
<!-- Kawaii API Wrapper -->
<dependency>
  <groupId>com.beanbeanjuice</groupId>
  <artifactId>kawaii-api-wrapper</artifactId>
  <version>VERSION</version>
</dependency>
```

For `Gradle`, paste this into your `dependencies` section of your `build.gradle` file.
```Groovy
// Kawaii API Wrapper
implementation group: 'com.beanbeanjuice', name: 'kawaii-api-wrapper', version: 'VERSION'
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
