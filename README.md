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
  def custom_version = "x.x.x"

  implementation "com.github.leodan11.custom-view:core:$custom_version" // Mandatory
  // Optional
  implementation "com.github.leodan11.custom-view:drawable:$custom_version"
  implementation "com.github.leodan11.custom-view:layout:$custom_version"
  implementation "com.github.leodan11.custom-view:textview:$custom_version"
  implementation "com.github.leodan11.custom-view:widget:$custom_version"
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
  val custom_version = "x.x.x"

  implementation("com.github.leodan11.custom-view:core:$custom_version")  // Mandatory
  // Optional
  implementation("com.github.leodan11.custom-view:drawable:$custom_version")
  implementation("com.github.leodan11.custom-view:layout:$custom_version")
  implementation("com.github.leodan11.custom-view:textview:$custom_version")
  implementation("com.github.leodan11.custom-view:widget:$custom_version")
}
```

---

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
