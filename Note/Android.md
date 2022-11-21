# Activity
**setContentView()**
> Android项目中添加的任何资源都会在R文件中生成一个相应的资源id，
> setCOntentView()中传入的就是布局文件的id

**在AndroidManifest文件中注册**
```xml
<!--注册activity-->
<activity
    android:name=".FirstActivity"
    android:label="This is FirstActivity."
    android:exported="true">
    <!--配置主activity-->
    <intent-filter>
        <action android:name="android.intent.action.MAIN"/>
        <category android:name="android.intent.category.LAUNCHER"/>
    </intent-filter>
    <meta-data
        android:name="android.app.lib_name"
        android:value="" />
</activity>
```

> 应用程序中没有声明任何一个Activity作为主Activity，这个程序仍然可以正常安装，只是无法从启动器中看到或打开这个程序，这种程序一般作为第三方服务供其他应用在内部进行调用的
