# DynamicCircleView
[![](https://jitpack.io/v/faultaddr/DynamicCircleView.svg)](https://jitpack.io/#faultaddr/DynamicCircleView)
[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)
[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)

Dynamic Circle View for Android, XIAOMI Sport old version main page View.

###  Display of results
<image src="https://s1.ax1x.com/2022/05/04/OZu5QA.gif" width = "200" height = "400" align="center"/>

### How to Use

in project build.gradle
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

in module build.gradle

```gradle
dependencies {
        implementation 'com.github.faultaddr:DynamicCircleView:0.1.0'
}
```

#### Using DynamicCircleView in xml
```xml

<com.faultaddr.dynamiccircleview.DynamicCircleView
    android:id="@+id/circle_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:bgColor="#333333"
    app:bgPic="@drawable/main"
    app:circleCount="3"
    app:endColor="#000000"
    app:layout_constraintEnd_toEndOf="parent"
    app:lineColor="#555555"
    app:startColor="#ffffff"
    app:count="100" />

```
#### Using DynamicCircleView in Java
```java
DynamicCircleView.ViewConfig config = new DynamicCircleView.ViewConfig();
DynamicCircleView dynamicCircleView =
        config.startColor(Color.parseColor("#fffff1"))
                .endColor(Color.parseColor("#c2ffec"))
                .circleCount(1)
                .lineColor(Color.parseColor("#c7ffec"))
                .bgPic(R.drawable.main_pic)
                .config(this);
// start the animation
dynamicCircleView.startScan();
```


### Attributes:
- bgColor: background color
- bgPic: background picture
- circleCount: the circle count in the view
- startColor&endColor: gradient color
- lineColor: the color of line
- count: the text in the middle of the view


## Maintainers

[@faultaddr](https://github.com/faultaddr)。

## Star History

[![Star History Chart](https://api.star-history.com/svg?repos=faultaddr/DynamicCircleView&type=Date)](https://star-history.com/#bytebase/star-history&Date)

## Contributing

Feel free to dive in! [open an issue](https://github.com/faultaddr/DynamicCircleView/issues/new) or submit PRs.


DynamicCircleView follows the  [Contributor Covenant](http://contributor-covenant.org/version/1/3/0/) Code of Conduct.


## License
