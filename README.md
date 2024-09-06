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
  implementation 'com.github.leodan11:custom-view:Tag'
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
  implementation("com.github.leodan11:custom-view:$tag")
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
  <artifactId>custom-view</artifactId>
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

License
-------

        Copyright [2023] [Leodan11]

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
