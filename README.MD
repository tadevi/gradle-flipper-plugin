# Flipper Gradle-plugin for Android projects
Minimal steps to set up Flipper for your projects.


### Installation
A little bit more control though.
```gradle
buildscript {
  repositories {
    maven {
      url 'https://plugins.gradle.org/m2/'
    }
  }
  dependencies {
    classpath 'com.piex.plugin:flipperplugin:1.0.0'
  }
}


apply plugin: ''com.piex.plugin.flipper'' // build.gradle (app module)
```
### Compatibility
- Network Flipper Plugin only works for OkHttp (latest support version: 4.x.x)
- Should apply the gradle plugin for debug build
- Internal libraries:
+ com.facebook.flipper:flipper:0.54.0
+ com.facebook.flipper:flipper-network-plugin:0.54.0
+ com.github.ddyos:flipper-plugin-mmkv-viewer:1.0.0