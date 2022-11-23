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



## 在Activity中使用Menu
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/add_item"
        android:title="Add"/>
    <item
        android:id="@+id/remove_item"
        android:title="Remove"/>
</menu>
```
```kotlin
class FirstActivity : AppCompatActivity() {
    ...
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        //返回true表示允许创建的菜单显示，false则无法显示
        return true
    }
}
```

### 销毁一个Activity
Activity类提供了一个**finish()**方法，只需要调用这个方法就可以销毁当前的Activity

## Intent
### 使用显式Intent
**Intent(Context packageContext, Class<?> cls)**
```kotlin
class FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        binding.button1.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
    ...
}
```
### 使用隐式Intent
指定一系列更为抽象的action和category等信息，然后交由系统去分析这个Intent，并帮我们找出合适的Activity去启动。

通过在`<activity>`标签下配置`<intent-filter>`的内容可以指定当前Activity能够响应的action和category
```xml
<!--AndroidManifest.xml-->
<activity
    android:name=".SecondActivity"
    android:exported="false">
    <intent-filter>
        <action android:name="com.android.firstofall.ACTION_START"/>
        <category android:name="android.intent.category.DEFAULT"/>
    </intent-filter>
    <meta-data
        android:name="android.app.lib_name"
        android:value="" />
</activity>
```
```kotlin
class FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        binding.button1.setOnClickListener {
            //只有action和category中的内容同时匹配，Activity才能响应Intent
            val intent = Intent("com.android.firstofall.ACTION_START")
            intent.addCategory("android.intent.category.DEFAULT")
            startActivity(intent)
        }
    }
    ...
}
```
### 隐式Intent的更多用法
```kotlin
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("https://www.baidu.com")
    startActivity(intent)
```
在`<intent-filter>`标签下还能够配置`<data>`标签，例如只需要指定android:scheme为https，就可以让activity也能够响应https协议的Intent

### 向下一个Activity传递数据
```kotlin
class FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button1.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            val data = "Hello"
            intent.putExtra("extra_data", data)
            startActivity(intent)
        }
    }
}
```
```kotlin
class SecondActivity : AppCompatActivity() {
    lateinit var binding: ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extraData = intent.getStringExtra("extra_data")
        Log.d("SecondActivity", "extraData is $extraData")
    }
}
```

### 返回数据给上一个Activity
```kotlin
class FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button1.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1 -> if(resultCode == RESULT_OK){
                val returnData = data?.getStringExtra("data_return")
                Log.d("FirstActivity", "return data is $returnData")
            }
        }
    }
}
```
```kotlin
class SecondActivity : AppCompatActivity() {
    lateinit var binding: ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button2.setOnClickListener {
            val intent = Intent()
            intent.putExtra("data_return", "return")
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
```
只有用户按下button返回FirstActivity才会返回数据，按下返回键并不会返回数据，可以重写 **onBackPressed()** 方法来解决这个问题

## Activity生命周期
**1. 完整生存期**
Activity在onCreate()方法和onDestroy()方法之间所经历的就是完整生存期。一般情况下，一个Activity会在onCreate()方法中完成各种初始化操作，而在onDestory()方法中完成释放内存的操作。
**2. 可见生存期**
Activity在onStart()方法和onStop()方法之间所经历的就是可见生存期。在可见生存期内，Activity对用户总是可见的，即便有可能无法和用户进行交互。我们可以通过这两个方法合理的管理那些对用户可见的资源。比如在onStart()方法中对资源进行加载，而在onStop()方法中对资源进行释放，从而保证处于停止状态的Activity不会占用过多内存。
**3. 前台生存期**
Activity在onResume()方法和onPause()方法之间所经历的就是前台生存期。在前台生存期内，Activity总是处于运行状态，此时的Activity是可以与用户进行交互的。

![avatar](images/Activity.png)

### Activity被回收
Activity中提供了onSaveInstanceState()回调方法，可以保证Activity被回收之前一定会被调用，此方法会携带一个Bundle类型的参数，可以用于保存数据。
在onCreate()方法中也有一个Bundle类型的参数，这个参数一般情况下是null，但如果Activity在被系统回收之前通过onSaveInstanceState()方法保存了数据，这个参数就会带有之前保存的全部数据，以此便可以实现数据的恢复。

## Activity的启动模式
通过在AndroidManifest.xml中通过给`<activity>`标签指定android:launchMode属性来选择启动模式
### standard
**默认模式**
每次启动都会创建一个该Activity的新实例
### singleTop
在启动Activity时如果发现返回栈**栈顶**已经是该Activity，则认为可以直接使用它而**不会再创建**新的Activity实例
### singleTask
在启动Activity时，系统首先在返回栈中检查是否存在该Activity的实例，如果发现存在则直接使用该实例，并把在这个Activity之上的所有其他Activity统统出栈，如果没有则创建一个新的Activity实例
### singleInstance
指定为singleInstance模式的Activity会启用一个新的返回栈来管理这个Activity，在返回时，返回栈为空后会显示另一个返回栈的栈顶

## Activity的最佳实践
### 知晓当前是在哪一个Activity
```kotlin
open class BaseActivity : AppCompatActivity{
    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        Log.d("BaseActivity", javaClass.simpleName)
    }
}

```
### 随时随地退出程序
建立一个单例类作为Activity的集合
```kotlin
object ActivityCollector{
    private val activities = ArrayList<Activity>()
    fun addActivity(activity : Activity){
        activities.add(activity)
    }
    fun removeActivity(activity : Activity){
        activities.remove(activity)
    }
    fun finishAll(){
        for(activity in activities){
            if(!activity.isFinishing){
                activity.finish()
            }
        }
        activities.clear()
    }
}
```
接下来修改BaseActivity中的代码
```kotlin
open class BaseActivity : AppCompatActivity{
    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        Log.d("BaseActivity", javaClass.simpleName)
        ActivityCollector.addActivity(this)
    }

    override fun onDestroy(){
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
}
```
至此，以后想在何时退出直接调用 **ActivityCollector.finishAll()** 即可直接退出程序


### 启动Activity的最佳写法
```kotlin
class SecondActivity : BaseActivity{
    companion object{
        fun actionStart(context : Context, data1 : String, data2 : String){
            val intent = Intent(context, SecondActivity::class.java)
            intent.putExtra("param1", data1)
            intent.putExtra("param2", data2)
            context.startActivity(intent)
        }
    }
}
```
以此实现在启动SecondActivity时便可以知道其需要传递哪些数据的效果



# UI
## 常见控件
### TextView

### Button
button中英文字母默认全大写，使用`android:textAllCaps="false"`属性保留原始文字内容

#### 监听器的注册
**1. 函数式API**
```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            //点击逻辑
        }
    }
}
```
**2. 实现接口**
```kotlin
class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button1.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.button1 -> {
                //点击逻辑
            }
        }
    }
}
```

### EditText
`android:hint="Type something here."`：设置提示性文本
`android:maxLines="2"`：设置文本最大行数，超过将向上滚动

### ImageView

### ProgressBar
Android控件都具有可见属性，在xml中可以通过`android:visibility=""`来指定，在代码中可以使用`setVisibility()`方法来设置

Visibility有三种属性：
**visible**：可见
**invisible**：不可见但仍占用屏幕空间
**gone**：不可见且不占用屏幕空间

可以通过`style="?android:attr/..."`属性来设置进度条的样式

### ALertDialog
弹出对话框，置顶于所有界面元素之上


## 基本布局
### LinearLayout——线性布局
`android:orientation`设置控件排列方向
`android:layout_gravity`设置控件在布局中的对齐方式
`android:layout_weight`用比例的方式指定控件的大小

### RelativeLayout——相对布局


### FrameLayout——帧布局

## 自定义控件
### 引入布局
自定义标题栏
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@drawable/title_bg">
    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/titleBack"
        android:text="back"
        android:layout_margin="5dp"
        android:background="@drawable/back_bg"
        android:textColor="#fff"/>
    <TextView
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:id="@+id/titleText"
        android:layout_weight="1"
        android:layout_gravity="center"
        android:gravity="center"
        android:text="Title Text"
        android:textColor="#fff"
        android:textSize="24sp"/>
    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/titleEdit"
        android:text="Edit"
        android:layout_margin="5dp"
        android:background="@drawable/edit_bg"
        android:textColor="#fff"/>
</LinearLayout>
```
引入布局
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    <include layout="@layout/title"/>
</LinearLayout>
```
隐藏系统自带的标题栏
```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}
```


### 创建自定义控件
自定义导航栏
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@drawable/title_bg">
    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/titleBack"
        android:text="back"
        android:layout_margin="5dp"
        android:background="@drawable/back_bg"
        android:textColor="#fff"/>
    <TextView
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:id="@+id/titleText"
        android:layout_weight="1"
        android:layout_gravity="center"
        android:gravity="center"
        android:text="Title Text"
        android:textColor="#fff"
        android:textSize="24sp"/>
    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/titleEdit"
        android:text="Edit"
        android:layout_margin="5dp"
        android:background="@drawable/edit_bg"
        android:textColor="#fff"/>
</LinearLayout>
```
添加自定义控件
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <com.android.widgettest.TitleLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
</LinearLayout>
```
为自定义控件中的控件注册点击事件
```kotlin
class TitleLayout(context : Context, attrs : AttributeSet) : LinearLayout(context, attrs) {
    var binding : TitleBinding
    init {
        //inflate(layoutInflater, parent, attachToParent)
        binding = TitleBinding.inflate(LayoutInflater.from(context), this, true)
        binding.titleBack.setOnClickListener {
            val activity = context as Activity
            activity.finish()
        }
        binding.titleEdit.setOnClickListener {
            Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show()
        }
    }
}
```

## ListView
### ListView的简单用法
在布局中引入ListView
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <ListView
        android:id="@+id/listView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```
```kotlin
class ListViewActivity : AppCompatActivity() {
    private val data = listOf("apple", "banana", "orange", "watermelon", "pear", "grape", "pineapple", "strawberry", "cherry", "mango", "apple", "banana", "orange", "watermelon", "pear", "grape", "pineapple", "strawberry", "cherry", "mango")
    lateinit var binding : ActivityListViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //在Adapter的构造函数中依次传入 Activity的实例、ListView子项布局的id、数据源
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
        binding.listView.adapter = adapter
    }
}
```

### 定制ListView的界面
定义实体类
```kotlin
class Fruit(val name : String, val imageId : Int)
```
定制item
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="60dp">
    <ImageView
        android:id="@+id/fruitImage"
        android:layout_gravity="center_vertical"
        android:layout_marginLeft="10dp"
        android:layout_width="40dp"
        android:layout_height="40dp"/>
    <TextView
        android:id="@+id/fruitName"
        android:layout_gravity="center_vertical"
        android:layout_marginLeft="10dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
</LinearLayout>
```
创建自定义适配器
```kotlin
class FruitAdapter(activity : Activity, resourceId : Int, data : List<Fruit>) : ArrayAdapter<Fruit>(activity, resourceId, data) {
    lateinit var binding : FruitItemBinding
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        binding = FruitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val fruit = getItem(position)
        if(fruit != null){
            binding.fruitImage.setImageResource(fruit.imageId)
            binding.fruitName.text = fruit.name
        }
        return binding.root
    }
}
```
最后修改Activity中的代码
```kotlin
class ListViewActivity : AppCompatActivity() {
    private val fruitList = ArrayList<Fruit>()
    lateinit var binding : ActivityListViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFruits()
        val adapter = FruitAdapter(this, R.layout.fruit_item, fruitList)
        binding.listView.adapter = adapter
    }
    private fun initFruits(){
        repeat(2){
            fruitList.apply {
                add(Fruit("apple", R.drawable.apple_pic))
                add(Fruit("banana", R.drawable.banana_pic))
                add(Fruit("cherry", R.drawable.cherry_pic))
                add(Fruit("grape", R.drawable.grape_pic))
                add(Fruit("mango", R.drawable.mango_pic))
                add(Fruit("orange", R.drawable.orange_pic))
                add(Fruit("pear", R.drawable.pear_pic))
                add(Fruit("pineapple", R.drawable.pineapple_pic))
                add(Fruit("strawberry", R.drawable.strawberry_pic))
                add(Fruit("watermelon", R.drawable.watermelon_pic))
            }
        }
    }
}
```

> binding = FruitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
> **XxxBinding.inflate(layoutInflater, parent, attachToParent)** 中三个参数的含义
> 1. layoutInflater可以直接从contex或parent.context中获取
> 2. parent是给加载好的布局指定父布局
> 3. attachToParent为true时表示将布局添加入parent中并且不能再向parent中添加view，为false时表示不将第一个参数的view添加到parent中，parent会协助第一个参数view的根节点生成布局参数，这个时候我们要手动地把view添加进来

### 提升ListView的运行效率
getView()方法中有一个convertView参数，这个参数就是用于将之前加载好的布局进行缓存，因此可以做出如下优化
```kotlin
class FruitAdapter(activity : Activity, resourceId : Int, data : List<Fruit>) : ArrayAdapter<Fruit>(activity, resourceId, data) {
    lateinit var binding : FruitItemBinding
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View
        //如果convertView为null，则使用LayoutInflater加载布局；如果不为null，则直接对convertView进行重用
        if(convertView == null){
            binding = FruitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            view = binding.root
        }else{
            view = convertView
        }
        val fruit = getItem(position)
        if(fruit != null){
            binding.fruitImage.setImageResource(fruit.imageId)
            binding.fruitName.text = fruit.name
        }
        return view
    }
}
```

虽然现在已经**不会再重复加载布局**，但是每次getView()方法中仍然会**获取一次控件的实例**，可以借助ViewHolder来对这部分进行性能优化
```kotlin
class FruitAdapter(activity : Activity, resourceId : Int, data : List<Fruit>) : ArrayAdapter<Fruit>(activity, resourceId, data) {
    lateinit var binding : FruitItemBinding
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View
        val viewHolder : ViewHolder
        if(convertView == null){
            binding = FruitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            view = binding.root
            val fruitImage : ImageView = binding.fruitImage
            val fruitName : TextView = binding.fruitName
            viewHolder = ViewHolder(fruitImage, fruitName)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val fruit = getItem(position)
        if(fruit != null){
            viewHolder.fruitImage.setImageResource(fruit.imageId)
            viewHolder.fruitName.text = fruit.name
        }
        return view
    }
    inner class ViewHolder(val fruitImage : ImageView, val fruitName : TextView)
}
```
View中的setTag（Onbect）表示给View添加一个格外的数据，以后可以用getTag()将这个数据取出来。
此时当convertView为null时，会创建一个ViewHolder对象，并将控件的实例存放在其中，然后用setTag()方法将ViewHolder对象存放在View中。当convertView不为null时，则调用View的getTag()方法把ViewHolder对象取出，这样就免去了findViewById()操作，不用重复获取控件实例。

### ListView的点击事件
```kotlin
class ListViewActivity : AppCompatActivity() {
    private val fruitList = ArrayList<Fruit>()
    lateinit var binding : ActivityListViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFruits()
        val adapter = FruitAdapter(this, R.layout.fruit_item, fruitList)
        binding.listView.adapter = adapter
        //binding.listView.setOnItemClickListener { parent, view, position, id ->
        //kotlin中允许我们将没用到的参数用下划线来替代
        binding.listView.setOnItemClickListener { _, _, position, _ ->
            val fruit = fruitList[position]
            Toast.makeText(this, fruit.name, Toast.LENGTH_SHORT).show()
        }
    }
    ...
}
```

## RecyclerView
**运行效率**
**更好的扩展性**
### RecyclerView基本用法
RecyclerView属于新增控件，使用前需要在在build.gradle中添加依赖
```gradle
dependencies {
    implementation 'androidx.recyclerview:recyclerview:1.2.1'
    ...
}
```
在布局中引入RecyclerView
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</androidx.constraintlayout.widget.ConstraintLayout>
```
为RecyclerView准备一个适配器
```kotlin
class FruitRecyclerViewAdapter(val fruitList : List<Fruit>) : RecyclerView.Adapter<FruitRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(binding: FruitItemBinding) : RecyclerView.ViewHolder(binding.root){
        val fruitImage : ImageView = binding.fruitImage
        val fruitName : TextView = binding.fruitName
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FruitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruitList[position]
        holder.fruitImage.setImageResource(fruit.imageId)
        holder.fruitName.text = fruit.name
    }
    override fun getItemCount(): Int = fruitList.size
}
```