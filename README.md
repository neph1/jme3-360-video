# jm3-360-video
Record 360 video with jMonkeyEngine3

Check CustomVideoRecorderAppStateTest.java for usage.

Example: https://youtu.be/TccLGPPZ3Iw

Use with gradle:

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
  
    dependencies {
	    implementation 'com.github.neph1:jme3-360-video:v0.2'
	}
  

This will produce an mp4 for you. You still need to inject proper metadata. I believe I used this software: https://github.com/google/spatial-media/releases/tag/v2.1


Developed for Real Training A/S (www.realtraining.no). Released as open source with their permission.
