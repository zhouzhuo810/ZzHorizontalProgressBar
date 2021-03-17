# ZzHorizontalProgressBar

一个高度自定义的水平进度条控件.

Github地址:https://github.com/zhouzhuo810/ZzHorizontalProgressBar

**功能简介**：
- 1.支持自定义进度颜色；
- 2.支持自定义背景颜色；
- 3.支持自定义背景与进度之间的内间距大小；
- 4.支持自定义最大值和默认进度值；
- 5.支持渐变颜色进度；
- 6.支持二级进度条。

**与系统控件相比的优势**：属性配置更简单、大小适配更方便。

Gradle:

```
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

```
    implementation 'com.github.zhouzhuo810:ZzHorizontalProgressBar:1.1.1'
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

		//set background color
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
|zpb_padding|背景与进度之间的内间距大小|dimension|
|zpb_second_pb_color |二级进度背景颜色|color|
|zpb_bg_color |背景颜色|color|
|zpb_pb_color |进度颜色|color|
|zpb_max |进度最大值|int|
|zpb_progress |默认进度值|int|
|zpb_second_progress |二级进度默认进度值|int|
|zpb_open_gradient|是否使用渐变色|boolean|
|zpb_show_zero_point|进度为0时是否显示圆点|boolean|
|zpb_gradient_from|开始渐变颜色|color|
|zpb_gradient_to|结束渐变颜色|color|
|zpb_show_second_progress|二级进度是否显示|boolean|
|zpb_open_second_gradient|二级进度是否使用渐变色|boolean|
|zpb_second_gradient_from|二级进度开始渐变颜色|color|
|zpb_second_gradient_to|二级进度结束渐变颜色|color|
|zpb_show_second_point_shape|二级进度形状（point,line）|enum|
|zpb_show_mode|显示进度的模式(round,rect,round_rect)|enum|
|zpb_round_rect_radius|round_rect模式下圆角的半径|dimension|
|zpb_draw_border|是否画边框|boolean|
|zpb_border_width|边框的线宽|dimension|
|zpb_border_color|边框的颜色|color|

### Fix Records

#### v1.1.1

- 修复二级进度值为0时也显示一条线的问题；
- 添加了文档注释；

#### v1.1.0

- zpb_show_zero_point属性，控制进度为0时是否显示圆点；

#### v1.0.9

- 修复二级进度条单一色时进度设置无效问题；

#### v1.0.8

- 修复二级进度条单一色时颜色设置无效问题；

#### v1.0.7

- 修复圆角进度算法缺陷；

#### v1.0.6

- 添加动态设置边框颜色方法；

#### v1.0.5

- 添加自定义圆角大小模式；
- 添加zpb_show_mode属性，切换模式；
- 添加zpb_round_rect_radius属性，圆角半径；
- 添加zpb_draw_border属性，是否画边框；
- 添加zpb_border_width属性，边框宽度；
- 添加zpb_border_color属性，边框颜色；

#### v1.0.4

- 添加矩形进度模式;
- 添加`setOnProgressChangedListener`回调方法；

#### v1.0.3

- 添加二级进度；
- 添加二级进度渐变；
- 添加二级进度形状；

#### v1.0.2

- 添加渐变色属性；

#### v1.0.1

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
