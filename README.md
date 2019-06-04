# ExpandableTextView
[Android CustomView] ExpandableTextView

![Alt Text](https://github.com/aqoong/ExpandableTextView/raw/master/ExpandableTextViewSample_Video/ExpandableTextView.gif)
![Alt Text](https://github.com/aqoong/ExpandableTextView/raw/master/ExpandableTextViewSample_Video/ExpandableTextView_2.gif)

[Custom View]
This is free Custom View for Android.
View for use on the same screen as SNS feeds.


## How to Use
  - [xml]
  ```
    <com.aqoong.lib.expandabletextview.ExpandableTextView
        android:id="@+id/textView"
        app:collapseLine="2"                                 <!-- standard line count -->
        app:text_more="show_more"                            <!-- defalut : "More" -->
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
  ```

  - [.java]
  ```
  ExpandableTextView textView = findViewById(R.id.textView);
  textView.setText('any text', 'show more option text');
  textView.setState(ExpandableTextView.STATE.COLLAPSE | EXPAND);
  ```
## Add ExpandableTextView Lib
  - project build.gradle
  ```
    allprojects {
      repositories {
        google()
        jcenter()
        ...
        maven { url "https://jitpack.io"}
        ...
      }
    }
  ```
  - app build.gradle  [![release](https://jitpack.io/v/aqoong/ExpandableTextView.svg)](https://jitpack.io/#aqoong/ExpandableTextView)
  ```
    dependencies {
      ...
      implementation 'com.github.aqoong:ExpandableTextView:x.y.z'
      ...
    }
  ```
