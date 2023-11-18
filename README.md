# Custom View

[![](https://jitpack.io/v/leodan11/CustomView.svg)](https://jitpack.io/#leodan11/CustomView)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Android Custom View allows you to easily use actions for the projects you develop.

---

> [!IMPORTANT]
> Getting Started

### Gradle

- Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
  repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
  }
}
```

- Step 2. Add the dependency

```gradle
dependencies {
  implementation 'com.github.leodan11:CustomView:Tag'
}
```

### Kotlin

- Step 1. Add the JitPack repository to your build file.

Add it in your root build.gradle at the end of repositories:

 ```kotlin
repositories {
  maven(url = "https://jitpack.io")
}
```

- Step 2. Add the dependency

```kotlin
dependencies {
  implementation("com.github.leodan11:CustomView:$tag")
}
```
  
### Moven

- Step 1. Add the JitPack repository

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
    
- Step 2. Add the dependency

```xml
<dependency>
  <groupId>com.github.leodan11</groupId>
  <artifactId>CustomView</artifactId>
  <version>tag</version>
</dependency>
```

---

> [!TIP]
> Below are the available views that can be used.

# Material Badge TextView

TextView to display a badge with text or numerical content. See the [documentation](https://github.com/leodan11/CustomView/wiki/Material-Badge-TextView) for examples and general usage of MaterialBadgeTextView

## Credits
The badge module is taken from [BadgeTextView](https://github.com/SmartToolFactory/BadgeTextView).Credits go completely to its creator and the people who has contributed with those pull requests.

# Money TextView

TextView to display amounts of money in different formats. See the [documentation](https://github.com/leodan11/CustomView/wiki/Money-TextView) for examples and general use of MoneyTextView

## Credits

This is just an updated version of [MoneyTextView](https://github.com/fabiomsr/MoneyTextView) and applying some of the active pull requests in it. Credits go completely to its creator and the people who has contributed with those pull requests.
