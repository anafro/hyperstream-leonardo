
<!--toc:start-->
- [Leonardo](#leonardo)
  - [Requirements](#requirements)
  - [How to get Leonardo](#how-to-get-leonardo)
    - [Get runnable JAR](#get-runnable-jar)
    - [Build yourself](#build-yourself)
    - [Via Docker](#via-docker)
  - [Usage](#usage)
    - [Required arguments](#required-arguments)
      - [Port](#port)
      - [Image size](#image-size)
    - [Generator](#generator)
    - [Profile picture caching](#profile-picture-caching)
      - [No cache (a.k.a. Ephemeral mode)](#no-cache-aka-ephemeral-mode)
      - [Cache in file system](#cache-in-file-system)
      - [Cache in Amazon S3 provider](#cache-in-amazon-s3-provider)
  - [Contribution](#contribution)
  - [Special thanks](#special-thanks)
  - [Development stack](#development-stack)
  - [License](#license)
<!--toc:end-->

# Leonardo

> [!NOTE]
> No clue what Hyperstream is? Welcome to the general README!

**Leonardo** is an procedural generator image generator
made to serve astonishing abstract profile pics
for dear **Hyperstream** users.

> [!IMPORTANT]
> Contributors, may I have your attention, please! Hyperstream offers
> a huge spectrum of technologies, including C++, PHP (Laravel), Vue, and Java!
> Feel free 'n brave to explore and write code for available issues!
> There is a tiny list of rules to consider when contributing though,
> please, check out our Hyperstream contribution guide.

## Requirements

Before installing, you must have JDK 21 or higher.

## How to get Leonardo

You can get **Leonardo** via 3 ways.
Choose one you feel the most comfortable with.

### Get runnable JAR

Get ready-to-run **Leonardo** JAR from releases.

### Build yourself

> [!NOTE]
> To build **Leonardo**, you should have JDK 21 installed,
> and Internet connection to install project dependencies.

First, clone the repository.

```bash
git clone https://github.com/anafro/hyperstream-leonardo
```

Afterwards, to build Leonardo, run Gradle build.
For UNIX-based platforms, like Linux or MacOS, use:

```bash
./gradlew jar
```

For Windows, use:

```bash
./gradlew.bat jar
```

Now Leonardo on path `build/libs/Hyperstream-Leonardo-(version).jar` is yours forever!

### Via Docker

Clone the repository.

```bash
git clone https://github.com/anafro/hyperstream-leonardo
```

There is a `Dockerfile` inside, so you can instance a container from an image!
To build a Docker image:

```bash
cd ./hyperstream-leonardo
docker build -t hyperstream-leonardo .
```

To run the container, type:

```bash
docker run -it --rm -p 80:8881 hyperstream-leonardo <leonardo-args...>
```

## Usage

Even though **Leonardo** is a micro service for **Hyperstream**,
it can be used outside of it.

**Leonardo** responds with images via `/@{username}/profile-picture` route.
Creating an image depends on whether you enabled caching or not.
See [Profile picture caching](#profile-picture-caching).

### Required arguments

#### Port

Set `--port` for HTTP server (e.g. `--port 80`)

#### Image size

Set both `--width` for width, `--height` for height
(e.g. `--width 512 --height 1024` tells Leonardo to generate vertical 512 x 1024 images).

### Generator

**Leonardo** has only one profile picture generation algorithm:
"Large Magellanic Cloud" or "LMC", so you should specify
`--generator lmc` anyway for now.

### Profile picture caching

You might want to cache images to prevent regenerating profile pictures
on each request to speed up your application.
Now, **Leonardo** provides 3 caching modes.
To provide mode, write it right after required Leonardo parameters:

```bash
java -jar ./HyperstreamLeonardo.jar --width 256 --height 256 (the-rest-required...) <cache-mode> (cache args...)
```

#### No cache (a.k.a. Ephemeral mode)

In `nocache` mode, Leonardo will generate an image each time it was requested.
> [!TIP]
> This mode is made for simple standalone usage.

```bash
java -jar ./HyperstreamLeonardo.jar nocache --width 512 --height 512 --port 80
```

Now, Leonardo is serving images on <http://localhost/your-username/profile-picture>

#### Cache in file system

In `filesystem` mode, Leonardo will store images in a directory.
To provide directory path, use `--path`:

```bash
java -jar ./HyperstreamLeonardo.jar filesystem --p "~/Leonardo-Cache" --width 512 --height 512 --port 80
```

#### Cache in Amazon S3 provider

**Leonardo** can store images in S3. Provide S3 credentials by providing:

- `--access-key`
- `--secret-key`

## Contribution

**Leonardo** is here for you, programmer,
no matter if it's gonna be your first or eighteen thirty first pull request!

> [!IMPORTANT]
> Before contributing, see contribution rules.

To see available tasks to do, bugs to fix, and features to implement,
please, check out open issues.

> [!TIP]
> If you are not a Java developer, don't leave now!
> Hyperstream is a large platform with many micro services.
> C++, PHP, Laravel, Vue, web development - welcome to choose!

## License

**Leonardo**, like any other Hyperstream part, is licensed under MIT.
Read about MIT in the LICENSE file.

## Development stack

**Leonardo** uses Java 21 as a main programming language.
Also, it uses:

- **argparse4j**
- **javalin**

> [!NOTE]
> Also note that Leonardo will likely migrate to future versions of Java.
> The reason why it is being maintained under JDK 21 is that
> this is the latest supportable version by JDTLS (a Java LPS), which I use.
> When JDTLS (hopefully) gets updated, likely so will Leonardo.

## Special thanks

Well, after using argparse in Remixer first (which amazed me how easy building CLI could be),
I tried to search for it in Java... And argparse**4j** is actually a thing!
Great thanks to them. And also my huge "wow" and as huge "thanks" gets Javalin!
After using native `com.sun` HTTP server (which, well, I deserve to be ashamed of),
your wonderful framework feels like a deep breath of fresh air in the morning!
Definitely gonna build something. I saw you guys have some Vue batteries included
into Javalin, which amazes me even more! Well, why use Spring then? (jk lol).

------------------
Copyright (c) 2026 Anatoly Frolov (anafro). All Rights Reserved.\
`contact@anafro.ru`
