# Digital Stopwatch

A lightweight Android stopwatch with start, hold, reset, and stop controls.

## Features

- Start and resume timing
- Hold/pause the timer
- Reset a paused timer
- Stop and clear the timer
- Minutes, seconds, and centiseconds display
- Button states that prevent invalid actions
- Timer callback cleanup when the activity is destroyed

## Built with

- Java
- Android Views and XML layouts
- `Handler` and `SystemClock` for time updates
- Android SDK 24+

## Run locally

1. Clone this repository.
2. Open it in Android Studio.
3. Allow Gradle to sync.
4. Run the app on an emulator or Android device.

## Project structure

`MainActivity` manages stopwatch state, elapsed-time calculation, recurring display updates, and control-button behavior.
