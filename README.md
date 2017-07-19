#走穿Android自定义View---补间动画alpha、scale、translate、rotate、set的xml属性及用法
先看几种简单的补间动画效果：
![1](http://i.imgur.com/ezReYiI.gif)
上面一共显示了五个效果：平移、透明、缩放、旋转和旋转+透明
代码也是比较简单的，以平移和组合效果简单讲解：
##1.资源文件：
补间动画的文件都是放在res下的anmi文件夹内，没有的话要自己创建
###（1）平移的translate_anim.xml
往同时往右边和下边方向移动100个像素
```
<?xml version="1.0" encoding="utf-8"?>
<translate xmlns:android="http://schemas.android.com/apk/res/android"
           android:fromXDelta="0"
           android:toXDelta="100"
           android:fromYDelta="0"
           android:toYDelta="100"
           android:duration="2000"
           android:fillBefore="true"/>

```
###(2)组合的all_anim.xml
透明效果：从完全显示到很透明
旋转效果：逆时针旋转650度
```

<?xml version="1.0" encoding="utf-8"?>
<set>
    <alpha xmlns:android="http://schemas.android.com/apk/res/android"
           android:fromAlpha="1.0"
           android:toAlpha="0.1"
           android:duration="3000"
           android:fillBefore="true"/>

    <rotate xmlns:android="http://schemas.android.com/apk/res/android"
            android:fromDegrees="0"
            android:toDegrees="-650"
            android:pivotX="50%"
            android:pivotY="50%"
            android:duration="3000"
            android:fillAfter="true"/>
</set>

```
这里的组个可以放置多个，这里只是放置两个动画透明和旋转做示例。


##2.java控制的主要代码

```
  ImageView iv;
  Animation animation;


    /**
     * 平移动画
     */
    public void translate(View view) {
        animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        iv.startAnimation(animation);
    }

  /**
     * 各种效果集于一身的动画
     */
    public void all(View view) {
        setIVAnimation(R.anim.all_anim);
    }

  /**
     * 传入动画的ID，直接给ImageView设置动画
     */
    private void setIVAnimation(int animID) {
        iv.startAnimation(AnimationUtils.loadAnimation(this, animID));
    }


```
上面平移动画也是可以直接调用那个私有方法来调用动画。


#下面是补间动画和各种属性的详细讲解。

##一.基本知识
补间动画就是通过对场景的对象不断进行图像变化来产生动画效果,在实现补间动画时,只要定义动画开始和结束的”关键帧”其它过度帧由系统自动计算并补齐.android中提供了4种补间动画效果：透明、旋转、缩放、平移。
###1.android补间动画的四种类别

|alpha|渐变透明度动画效果|
|-|-|
|translate|画面转换位置移动动画效果|
|scale|渐变尺寸伸缩动画效果|
|rotate|画面转移旋转动画效果|
上面这四个类别名称是可以在xml中当作根标签使用，来对应各种补间动画。
当然也是可以用set做根标签，里面放几个补间动画，做组合效果。

###2.关于补间动画的类
Animation有五个子类：AlphaAnimation (透明动画)TranslateAnimation (平移动画)、ScaleAnimation (缩放动画)、RotateAnimation (旋转动画)、AnimationSet (组合动画)

###2.动画文件存放位置
补间动画定义文件应该存放在res/anim文件夹下，访问时采用R.anim.XXX.xml的方式，位置如图：
![1](http://i.imgur.com/DRIZFuu.png)

###3.根据xml文件获取动画类的对象，并设置动画
```
//获取动画对象
 Animation  animation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
//你也可以获取具体的动画对象
     AlphaAnimation  animation = (AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
//给控件设置动画
  imageView.startAnimation(animation);

//这里控件不仅仅是ImageView，也可以是TextView，或是一个布局或一个自定义View都是可以的！

```


###4.各个补间动画可以设置的通用属性（Animation类的属性）
下面这些属性在各个补间动画效果内都是通用的，并且表示的意义也是一样的。
####（1）android:interpolator 插值器 
用于控制动画的变化速度,使用动画效果可以匀速,先加速再减速,先减速后加速或抛物线速度等各种速度变化，后面会有文章详解 
####（2）android:repeatMode 
用于设置动画的重复方式,可选择为reverse(反向)或restart(重新开始) 
比如放大动画，图像变化刚开始从小到大，第二次会从大变小，后面一直重复。。。
####（3）android:repeatCount 
用于设置动画的重复次数,属性可以是代表次数的数值,也可以是infinite(无限循环) 
默认次数为0，如果设置次数为1，动画会执行2次，以此类推。。。
####（4）android:duration 
用于指定动画持续的时间,单位为毫秒 
如果你的动画时间没有设置，动画就是在一瞬间完成的，比如从图像从正常到很透明，就想换了一个图片的感觉，没有过程。 
####（5）android:fillBefore      
如果设置为true,控件动画结束时，还原到开始动画前的状态
比如，透明动画，从透明到完全显示，动画结束后，图像会显示动画没开始前的样子。
####（6）android:fillEnabled    
与android:fillBefore 效果相同，都是在动画结束时，将控件还原到初始化状态
####（7）android:fillAfter          
如果设置为true，控件动画结束时，将保持动画最后时的状态
这个属性如果设置了为true，fillBefore也设置为true，动画结束后，图像是不会恢复的，就是以fillAfter为准。
如果fillAfter属性设置为false，不管fillBefore设置为true还是false，图像最终都是会恢复动画前的样子！

##二.透明动画：alpha
图像大小不变，动画实现图像从某一个透明度的值变成另一个透明度的值。透明度的值从0到1的浮点数，0为透明，1为完全显示。
###xml代码：
```
<?xml version="1.0" encoding="utf-8"?> 
<alpha xmlns:android="http://schemas.android.com/apk/res/android" 
       android:fromAlpha="1.0"        //设置开始时的透明度，这里表示完全可见
       android:toAlpha="0.1"		  //设置动画结束时的透明度，很透明
       android:duration="3000"		  //动画持续时间
       android:fillBefore="true"      //是否是保持开始的样子（完全可见），默认为true
	   android:fillAfter="true"       //是否保存结束时的透明度，默认为false
/>   


```
###解析：
 透明动画的属性设置
####1. android:fromAlpha 
用于指定动画开始时的透明度,值为0.0代表完全透明 ,值为1.0代表完全不透明 
####2. android:toAlpha 
用于指定动画结束时的透明度,值为0.0代表完全透明,值为1.0代表完全不透明 
####其他
注意上面属性fillBefore和fillBefore如果都是true，以fillAfter属性为主，即图像保持动画最后的效果。
如果上面两个属性都是false，最后图像还是会显示动画开始时的样子。

###上面这段代码运行的效果：
![2](http://i.imgur.com/FuwofL4.gif)
####重复3次的效果：
从不太显示到完全显示三次效果。把重复的次数设置为2，即可。
####设置：

```
<?xml version="1.0" encoding="utf-8"?>
<alpha xmlns:android="http://schemas.android.com/apk/res/android"
       android:fromAlpha="0.1"
       android:toAlpha="1"
       android:duration="3000"
       android:fillAfter="false"
       android:fillBefore="false"
       android:repeatCount="2"
        />

```

####效果：
![3](http://i.imgur.com/InC0P5S.gif)


##三.平移动画：translate
图像大小不变，通过为图像指定开始时的位置,结束时的位置,经及持续时间来创建动画效果。
###xml代码
从左上角往右下角平移
```
<?xml version="1.0" encoding="utf-8"?>
<translate xmlns:android="http://schemas.android.com/apk/res/android"
           android:fromXDelta="0"	//x轴开始的位置
           android:toXDelta="100"   //往右滑动100个像素点
           android:fromYDelta="0"   //y轴开始的位置
           android:toYDelta="100"   //往下移动100个像素点
           android:duration="2000"  //时间2000毫秒
           android:fillBefore="true"//动画结束后，回到原来的位置
/>

```

###解析：
####1. android:fromXDelta
 用于指定动画开始时水平方向上的起始位置，默认单位是像素，可以使用100%代表的是自身长度，100%p代表的是父框体的长度。50%就是表示一半的距离。下面属性的距离也可以这样表示。 
####2. android:toXDelta 
用于指定动画结束时水平方向上的位置 
####3. android:fromYDelta 
用于指定动画开始时垂直方向上的位置 
####4. android:toYDelta
 用于指定动画结束时垂直方向上的位置 
###从左往右的平移：
刚好移出屏幕
####设置：
```
 		   android:fromXDelta="0"	//x轴开始的位置
           android:toXDelta="100%p" //往右滑动一个父框体的距离
           android:fromYDelta="0"   //y轴开始的位置
           android:toYDelta="0"     //不往下移动
           android:duration="2000"  //时间2000毫秒
           android:fillBefore="true"//动画结束后，回到原来的位置

```

####效果：
![4](http://i.imgur.com/FHkhn7d.gif)
图解
![5](http://i.imgur.com/HeuaKeu.png)
###从上下的平移：
####设置：
```
     	   android:fromXDelta="100"
           android:toXDelta="100"
           android:fromYDelta="0"
           android:toYDelta="200"
           android:duration="2000"
```

####效果：
![6](http://i.imgur.com/xTUAUEF.gif)
可以看到在距离y轴100像素的地方，从上往下移动200像素的效果。


###从右下角往右上角平移：
####设置：
````
  		   android:fromXDelta="80%p"
           android:toXDelta="0"
           android:fromYDelta="80%p"
           android:toYDelta="0"

```

####效果：
![7](http://i.imgur.com/noGUwnj.gif)

##四.缩放动画：scale
改变图片大小，动画指定开始时缩放系数,结束时的缩放系数,以及持续时间来他建动画,在缩放时还可以通过指定轴心点坐标来改变绽放的中心。
###1.xml代码
```
<?xml version="1.0" encoding="utf-8"?>
<scale xmlns:android="http://schemas.android.com/apk/res/android"
       android:fromXScale="0.0"
       android:toXScale="1.4"
       android:fromYScale="0.0"
       android:toYScale="1.4"
       android:pivotX="200"
       android:pivotY="200"
       android:duration="1000"
        android:fillAfter="true"
        />

```
###2.解析
####（1）android:fromXScale 用于指定动画开始时水平方向上的缩放系数，浮点数，值0表示无，值为1.0表示不变化 ,值为2.0表示放大一倍 。。。以此类推
####（2）android:toXScale 用于指定动画结束时水平方向上的缩放系数。
####（3）android:fromYScale 用于指定动画开始时垂直方向上的缩放系数。
####（4）android:toYScale 用于指定动画结束时垂直方向上的缩放系数。
####（5）android:pivotX 用于指定轴心点X轴坐标，200表示距离y轴200个像素点的距离，50%表示自身的x轴中心点的，50%p表示父框体的x轴中心点 
####（6）android:pivotY 用于指定轴心点Y轴坐标，200表示距离x轴200个像素点的距离，50%表示自身的y轴中心点的，50%p表示父框体的y轴中心点 

###上面代码运行的效果：
![9](http://i.imgur.com/OftxOrM.gif)
可以看到图像是由无变到有，并且是（200，200）这个点开始做放大，最后到1.4倍后停下，并保持最后的样子。

###从0变大到3倍效果：
####设置代码：
设置在自身位置中心开始放大

```
       android:fromXScale="0.0"
       android:toXScale="3"
       android:fromYScale="0.0"
       android:toYScale="3"
       android:pivotX="50%"
       android:pivotY="50%"

```
####效果：
![9](http://i.imgur.com/nX5jY85.gif)

###在父窗体中心位置放大到0.5倍

####设置代码：
```
  	   android:fromXScale="0.0"
       android:toXScale="0.5"
       android:fromYScale="0.0"
       android:toYScale="0.5"
       android:pivotX="50%p"
       android:pivotY="50%p"

```
这个比较坑的就是它会向你图像本来的位置移动！这里会从中心点向原点方向移动。
####效果：
![10](http://i.imgur.com/NJoQssJ.gif)
这里可以看到图像向原来的方向移动一半，如果设置放大的倍率大于1，就会发现图像移动到屏幕外面去了！如果设置x和y的倍率刚好是1，图像放大后，刚好到原点的位置。
要想图像在中心点放大或缩小，就要把图像位置定在中心点，然后在自身的中心点，进行放大或缩小。



##五.旋转动画：rotate
旋转动画就是通过为动画指定开始时的旋转角度,结束时的旋转角度,经及持续时间来创建动画,在旋转时还可以通过指定轴心点坐标来改为旋转的中心。 

###1.xml代码
```

<?xml version="1.0" encoding="utf-8"?>
<rotate xmlns:android="http://schemas.android.com/apk/res/android"
        android:fromDegrees="0"
        android:toDegrees="90"
        android:pivotX="50%"
        android:pivotY="50%"
        android:duration="3000"
        android:fillAfter="true"/>


```
###2.解析
####(1)android:fromDegrees 用于指定动画开始时旋转的角度数,正负任意大小的浮点数，正的表示顺时针，负的表示逆时针 
####(2) android:toDeggrees 用于指定动画结束时旋转的角度 数,正负任意大小的浮点数，正的表示顺时针，负的表示逆时针 
####(3) android:pivotX 用于指定轴心点X轴坐标，20表示距离y轴20个像素点的距离，50%表示自身的x轴中心点的，50%p表示父框体的x轴中心点 
####(4) android:pivotY 用于指定轴心点Y轴坐标 ，20表示距离x轴20个像素点的距离，50%表示自身的y轴中心点的，50%p表示父框体的y轴中心点 

###上面代码显示的效果：

![12](http://i.imgur.com/uwzQevC.gif)
可以看到图像，以自身为中心旋转90度后停下来。

###图片以父窗体的40%的一个中心点旋转180度
####代码：
```
   		android:fromDegrees="0"
        android:toDegrees="180"
        android:pivotX="40%p"
        android:pivotY="40%p"
        android:duration="3000"

```
####效果：
![12](http://i.imgur.com/mwXdSMv.gif)
可以看到图片旋转了180，但是旋转到了屏幕右下角
图解：
![13](http://i.imgur.com/b8C07KM.png)
###如果把最终角度换成360度，效果：
 语句：android:toDegrees="360"
![14](http://i.imgur.com/csJTwZA.gif)
角度可以任意指定，旋转720度，就会显示旋转两圈效果！

这里的pivotX和pivotY，我刚开始是以为图像在这个点旋转，但是实际上是以这个点为中心旋转，半径就是图像原点到这个中心点之间的距离！

##六.组合动画：set
组合动画就是可以把多种动画放在一起显示。可以是单独一个，也可以是多个东西一起显示。
###1.xml代码
```
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
     android:duration="5000"
     android:fillAfter="true"
        >
    <alpha
            android:fromAlpha="0.3"
            android:toAlpha="1"
            android:fillBefore="true"
            android:duration="1000"
            />

    <rotate
            android:fromDegrees="0"
            android:toDegrees="810"
            android:pivotX="50%"
            android:pivotY="50%"
            android:fillBefore="true"
            android:duration="2000"
            />
    <translate
               android:fromXDelta="0"
               android:toXDelta="80%p"
               android:fromYDelta="0"
               android:toYDelta="0"
               android:fillBefore="true"/>
</set>


```
###2.解析
####组合动画，各个动画的属性还是不变
####set标签也是可以设置通用的那些属性。
####但是如果通用属性在set标签中设置，在其他动画也设置，会有什么效果呢？
根据之前Android的其他知识，我们知道里面的标签属性会覆盖掉外面的标签属性，
但是这个补间动画的却不是，通用属性都是以外边set标签中设置为准，不然可以看看上面代码运行的效果，
上面代码从左到右移动要5秒，里面标签设置旋转时间2秒，但是实际效果是旋转5秒时间
同样，fillAfter等等这些通用属性都是以set标签为准的。
###上面代码的效果：
![15](http://i.imgur.com/RQGeVSm.gif)


关于xml使用补间动画，这里已经详细介绍了各方面的知识，但是插值器还没有说，这个会在后面的文章介绍。
插值器，其实就是可以让动画显示效果有点不同，比如一个平移过程，前期匀速，到中间加速到最后。
动画还是原来动画，只是显示的速率会发生改变！

#共勉：成功是失败走剩的路。
失败是什么？没有什么，只是走向成功更近一步；怎么走向成功呢？当你走穿所有失败的路，剩下的那条路必定是成功的路，但是也不一定是每条路都走到底，有时候看穿也可以的。








