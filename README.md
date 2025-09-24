# Canteen Dish Recognition Android App

This repository contains an Android application that captures an image of a diner's tray, uploads it to a backend recognition service, and displays the identified dishes with their nutritional information and prices.

## Features
- Uses CameraX to detect the presence of a tray and capture a photo automatically.
- Uploads the captured image to a backend REST API for dish recognition.
- Displays the recognition results in a responsive UI.

## Project Structure
```
.
├── app/                    # Android application module
│   ├── src/main/java/com/example/canteen/ …
├── docs/
│   ├── architecture.md     # High-level system architecture
│   └── design.md           # Detailed component & data-flow design
└── README.md
```

## Prerequisites
1. **Android Studio Bumblebee (or newer)** on Windows.
2. **Android SDK Platform 34**, **Android Gradle Plugin 8+**.
3. A physical Android device running Android 8.0 (API 26) or higher.

## Getting Started
```bash
# Clone the project
$ git clone https://github.com/your_org/canteen-recognition-android.git
$ cd canteen-recognition-android

# Open with Android Studio
```
Android Studio will automatically download the required Gradle dependencies. 

## Building & Running
1. Connect your Android device with USB debugging enabled.
2. Select the device in the Android Studio toolbar.
3. Click *Run ▶️*.

## Flashing (ADB Install)
If you prefer command-line:
```bash
./gradlew assembleRelease
adb install -r app/build/outputs/apk/release/app-release.apk
```

## Backend Endpoint
The app expects a POST endpoint at:
```
POST https://<YOUR_BACKEND>/api/v1/recognize
Content-Type: multipart/form-data (field "image")
Response: { "dishes": [ { "name": "Kung Pao Chicken", "calories": 300 } ] }
```
Configure this URL in `local.properties` or replace the placeholder in `NetworkModule.kt`.

## License
MIT
