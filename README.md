# Caller ID App Prototype

This is a prototype for a Caller ID app that helps users view contacts and manage block lists, simulate the incoming call. The app is built using modern Android tools and libraries such as Jetpack, Hilt for dependency injection, and Room for local data storage.

## Features

- **View Contacts:** View all the contacts in a user-friendly interface.
- **Block List:** Add or remove numbers to the block list.
- **Incoming Call:** Simulate a incoming call from a random number of your contact list.
- **Navigation:** Seamless navigation between fragments using Jetpack Navigation.
- **Room Database:** Store contacts and block list data locally using Room Database.
- **Hilt Dependency Injection:** Simplify dependency management with Hilt.

## Libraries and Technologies Used

- **Kotlin**: The main programming language used for development.
- **Jetpack Libraries**: Including Navigation, Room, SplashScreen, and more.
- **Hilt**: For Dependency Injection to manage dependencies.
- **Glide**: For image loading and caching.
- **Room**: For local database management.

## Installation

Clone this repository and open it in Android Studio:

```bash
git clone https://github.com/ahmedsiddique01/CallerIdAppPrototype.git
```

Open the project in Android Studio and sync the project with Gradle. You will need a device or emulator running Android API 24+.

## Setup

1. Ensure that your environment is set up for Kotlin development.
2. Make sure to enable **ViewBinding** in your project (`viewBinding = true` in `build.gradle`).
3. Set up Hilt by adding the necessary dependencies in your project and application-level `build.gradle`.

## Building the App

You can build the app from Android Studio with the following steps:

1. Open the project in Android Studio.
2. Click **Build** > **Make Project** or simply press `Ctrl+F9` (Windows/Linux) or `Cmd+F9` (macOS).
3. Once the build completes, you can run the app on a connected device or emulator.

## Run Tests

To run unit and instrumentation tests, you can use Android Studio's **Run** menu, or run the following command in the terminal:

```bash
./gradlew test
./gradlew connectedAndroidTest
```

## Demo Video
You can watch a demo of the app below:
[Click here to watch the demo](demo/demo_video.mp4)

## APK
You can get the apk:
[Get APK](demo/output.apk)

---

Feel free to hire me ! :P