# SpeechToText
Coding exercise to record and convert audio to text. The project incorporates:

1. Realm to track audio recordings and the speech to text translation.
2. AndroidAudioConverter(https://github.com/adrielcafe/AndroidAudioConverter) to convert Androids '.3gp' audio format to '.flac' format required by Google's Speech to Text translation apil
2. Google Signin with Firebase for access to Google Cloud Storage (gcs)
3. RxJava/Retrofit/Firebase to upload audio recordings to gcs, initiate Google's Speech to Text api, and retrieve corresponding text

This project is still WIP.

