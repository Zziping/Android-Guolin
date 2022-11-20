# Activity
**setContentView()**
> Android项目中添加的任何资源都会在R文件中生成一个相应的资源id，
> setCOntentView()中传入的就是布局文件的id

**在AndroidManifest文件中注册**
```xml
<!--配置主activity-->
<activity
    android:name=".FirstActivity"
    android:label="This is FirstActivity."
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN"/>
        <category android:name="android.intent.category.LAUNCHER"/>
    </intent-filter>
    <meta-data
        android:name="android.app.lib_name"
        android:value="" />
</activity>
```