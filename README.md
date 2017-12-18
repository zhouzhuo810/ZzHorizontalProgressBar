# ZzHorizontalProgressBar

A Horizontal ProgressBar that is customized easily.

Github地址:https://github.com/zhouzhuo810/ZzHorizontalProgressBar

**功能简介**：
1.支持自定义进度颜色；
2.支持自定义背景颜色；
3.支持自定义背景与进度之间的内间距大小；
4.支持自定义最大值和默认进度值；
5.支持渐变颜色进度。

**与系统控件相比的优势**：属性配置更简单、大小适配更方便。

Gradle:

```
compile 'me.zhouzhuo.zzhorizontalprogressbar:zz-horizontal-progressbar:1.0.2'
```


Maven:

```xml
<dependency>
  <groupId>me.zhouzhuo.zzhorizontalprogressbar</groupId>
  <artifactId>zz-horizontal-progressbar</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```


<h3>What does it look like？</h3>

![这里写图片描述](https://github.com/zhouzhuo810/ZzHorizontalProgressBar/blob/master/zzhorizontalprogressbar.gif)



<h3>How to use it ?</h3>

1.xml


```
    <me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar
        android:id="@+id/pb"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        app:zpb_padding="0dp"
        app:zpb_pb_color="@android:color/holo_green_dark"
        app:zpb_bg_color="@android:color/holo_blue_bright"
        app:zpb_max="100"
        app:zpb_progress="30"
        />

```

2.java


```java
        final ZzHorizontalProgressBar pb = (ZzHorizontalProgressBar) findViewById(R.id.pb);

		//set progress value
        pb.setProgress(progress);

		//set padding
        pb.setPadding(0);		

		//set bacground color
        pb.setBgColor(Color.RED);

		//set progress color
        pb.setProgressColor(Color.BLUE);

		//set max value
        pb.setMax(100);		
```

</br>
<h3>属性说明：</h3>

|属性|作用|类型|
| --- | ---|---|
| zpb_padding|背景与进度之间的内间距大小|dimension|
|zpb_bg_color |背景颜色|color|
|zpb_pb_color |进度颜色|color|
|zpb_max|进度最大值|int|
|zpb_progress|默认进度值|int|
|zpb_open_gradient_|是否使用渐变色|boolean|
|zpb_gradient_from|开始渐变颜色|color|
|zpb_gradient_to|结束渐变颜色|color|


### Fix Records

#### v1.2

- 添加渐变色属性；

#### v1.1

- 修复动态修改背景颜色和进度颜色无效问题；

### License

```
Copyright © zhouzhuo810

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```