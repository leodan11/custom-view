# MoneyTextView

[![](https://jitpack.io/v/leodan11/MoneyTextView.svg)](https://jitpack.io/#leodan11/MoneyTextView)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

TextView to display amounts of money in different formats.

# Credits

This is just an updated version of [MoneyTextView](https://github.com/fabiomsr/MoneyTextView) and applying some of the active pull requests in it. 
Credits go completely to its creator and the people who has contributed with those pull requests.

The badge module is taken from [BadgeTextView](https://github.com/SmartToolFactory/BadgeTextView).
Credits go completely to its creator and the people who has contributed with those pull requests.

# Setup

<details>
  <summary>Gradle</summary>

- Step 1. Add the JitPack repository to your build file

  Add it in your root build.gradle at the end of repositories:

  ```gradle
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
      }
  }
  ```

- Step 2. Add the dependency
  
  ```gradle
  dependencies {
    implementation 'com.github.leodan11:MoneyTextView:Tag'
  }
  ```
  
</details>

<details>
    <summary>Kotlin</summary>

  - Step 1. Add the JitPack repository to your build file.

    Add it in your root build.gradle at the end of repositories:

    ```kotlin
    repositories {
        ...
        maven(url = "https://jitpack.io")
    }
    ```

- Step 2. Add the dependency
  
    ```kotlin
    dependencies {
      implementation("com.github.leodan11:MoneyTextView:$tag")
    }
    ```
  
</details>

<details>
    <summary>Moven</summary>

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
        <artifactId>MoneyTextView</artifactId>
        <version>latest version</version>
     </dependency>
    ```
  
</details>

## Attributes

Money text view offers several attributes for a deeper view configuration, the following table shows all these options and their default value.

|           Name          |                                                             Description                                                             |        Values        |   Default  |
|:-----------------------:|:-----------------------------------------------------------------------------------------------------------------------------------:|:--------------------:|:----------:|
| format                  | String containing a DecimalFormat valid format [DecimalFormat]       | string               | ###,##0.00 |
| amount                  | Amount of money to be displayed                                                                                                                  | float                | 0          |
| baseTextSize            | Text size, if neither of decimalDigitsTextSize or symbolTextSize are specified this attribute will effect the whole text                            | sp                   | 18sp       |
| baseTextColor           | Text size, if neither of decimalTextColor or symbolTextColor are specified this attribute will effect the whole text                                          | color                | #000000    |
| gravity                 | Text relative position inside the view                                                                                      | top,bottom,center... | center     |
| symbol                  | Currency Symbol                                                                                                                     | string               | $          |
| symbolMargin            | Separation between the currency symbol and the amount                                                                                           | dp                   | 2dp        |
| symbolTextSize          | Currency symbol text size                                                                                                       | sp                   | 18sp       |
| symbolGravity           | Currency symbol gravity attribute | start,end,top,bottom | top,start  |
| symbolTextColor         | Currency symbol Color                                                                                                                   | color                | #000000    |
| decimalSeparator        | Decimal part separator character                                                                                                | string               | '          |
| decimalMargin           | Separator between the integer part and the decimal                                                                                 | dp                   | 2dp        |
| decimalDigitsTextSize   | Decimal part text size                                                                                               | sp                   | 18sp       |
| decimalGravity          | Decimal part gravity attribute                                                    | top,bottom           | top        |
| decimalTextColor        | Decimal part color                                                                                                           | color                | #000000    |
| decimalUnderline        | Enables decimal part underlining                                                                                              | boolean              | false      |
| includeDecimalSeparator | Hides/Shows the decimal part separator                                                                                                | boolean              | true       |
| fontPath                | Path to a custom font                                                                                                         | string               |            |

## Usage

* Include MoneyTextView in a layout xml file:

~~~xml                                            

<com.leodan11.textview.MoneyTextView
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       app:symbol="¥"
       app:amount="1256.56"/>

~~~

#### More settings example

~~~xml
<com.leodan11.textview.MoneyTextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="32dp"
        app:symbol="$"
        app:symbolGravity="start|top"
        app:symbolTextSize="30sp"
        app:symbolMargin="6dp"
        app:amount="1256.56"
        app:baseTextSize="54sp"
        app:decimalDigitsTextSize="30sp"
        app:decimalMargin="6dp"
        app:includeDecimalSeparator="false"
        app:baseTextColor="#009688" />
~~~
