# MoneyTextView
TextView to display amounts of money in different formats.

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
