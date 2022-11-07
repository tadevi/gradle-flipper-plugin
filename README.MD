# Flipper Gradle-plugin for Android projects
Minimal steps to set up Flipper for your projects.


### Installation
A little bit more control though.
```gradle
buildscript {
  repositories {
    maven {
      url 'https://s01.oss.sonatype.org/content/repositories/snapshots/'
    }
  }
  dependencies {
    classpath 'io.github.tadevi.plugin:flipperplugin:1.0.0-SNAPSHOT'
  }
}

allprojects {
  repositories {
    maven {
      url 'https://s01.oss.sonatype.org/content/repositories/snapshots/'
    }
  }
}


apply plugin: 'io.tadevi.plugin.flipper.native' // build.gradle (app module)
```
### Compatibility
- Network Flipper Plugin only works for OkHttp (latest support version: 4.x.x)
- Recommend: Should apply the gradle plugin for debug build
- Internal dependencies:
+ com.facebook.flipper:flipper:0.125.0
+ com.facebook.flipper:flipper-network-plugin:0.125.0
+ com.github.ddyos:flipper-plugin-mmkv-viewer:1.0.0
+ com.facebook.soloader:soloader:0.10.3