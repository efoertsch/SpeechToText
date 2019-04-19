# SpeechToText
Coding exercise to record and convert audio to text. The project incorporates:

1. Realm to track audio recordings and the speech to text translation.
2. AndroidAudioConverter(https://github.com/adrielcafe/AndroidAudioConverter) to convert Androids '.3gp' audio format to '.flac' format required by Google's Speech to Text translation API.
3. Google Signin with Firebase for access to Google Cloud Storage (gcs)
4. RxJava/Retrofit/Firebase to upload audio recordings to gcs, initiate Google's Speech to Text api, and retrieve corresponding text

To download and execute this process you will need to set:
1. Set up a project in Firebase.
2. Create a gcs storage bucket
3. Create Oauth2 web client key for the project (to allow authorization for speech to text api to gain access to uploaded audio files)
4. Create a strings resource file similar to that below and inserting your web client id and bucket name where you see {...}

    &lt;!-- used to id speech to text project for google/firebase signon --&gt;  
    &lt;!--https://console.developers.google.com/apis/credentials?project={project name just for documentation} --&gt;  
    
    &lt;string name="web_client_id"&gt;{Oauth2 web application client id. Yeah it is Android app but need web app client id}&lt;/string&gt;  
    &lt;string name="gcs_access_scope"&gt;"https://www.googleapis.com/auth/cloud-platform"&lt;/string&gt;  
    &lt;string name="gcs_read_write_scope"&gt;"https://www.googleapis.com/auth/devstorage.read-write"&lt;/string&gt;  

    &lt;!-- google cloud services bucket that audio files to be uploaded to --&gt;  
    &lt;string name="gcs_bucket"&gt;{firebase/gcs bucket name - unique across all of Google gcs}&lt;/string&gt;  
    &lt;string name="gcs_speech_url"&gt;"gs://{same bucket name here}/"&lt;/string&gt;    

5. Download the Firebase google-services.json file and place in your app root directory. (Wait until OAuth2 stuff set up before downloading)


This project is still WIP.

