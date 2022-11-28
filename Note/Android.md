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
### Activity状态
**1. 运行状态**
返回栈栈顶
**2. 暂停状态**
不处于返回栈栈顶，但仍然可见
**3. 停止状态**
不处于返回栈栈顶，并且完全不可见
**4. 销毁状态**
从返回栈中移除

### Activity生存期
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
1. 让ViewHolder构造函数接收FruitItemBinding这个参数。但是注意，**ViewHolder的父类RecyclerView.ViewHolder它只会接收View类型的参数**，因此我们需要调用binding.root获得fruit_item.xml中根元素的实例传给RecyclerView.ViewHolder。我们在onCreateViewHolder()函数中调用FruitItemBinding的inflate()函数去加载fruit_item.xml布局文件，然后创建一个ViewHolder实例，并把加载出来的布局传入构造函数中，最后将ViewHolder实例返回。这样，我们就**不需要再使用findViewById()函数**来查找控件实例了，而是调用binding.fruitImage和binding.fruitName就可以直接引用到相应控件的实例。
2. onBindViewHolder()方法用于对RecyclerView子项数据进行赋值，会在每个子项被滚动到屏幕内的时候执行。

```kotlin
class RecyclerViewActivity : AppCompatActivity() {
    lateinit var binding : ActivityRecyclerViewBinding
    private val fruitList = ArrayList<Fruit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFruits()
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = FruitRecyclerViewAdapter(fruitList)
        binding.recyclerView.adapter = adapter
    }
    private fun initFruits(){
        ...
    }
}
```
**LayoutManager**用于指定RecyclerView的布局方式

### 实现横向滚动和瀑布流布局
#### 横向滚动
修改fruit_item布局为垂直线性布局，宽度指定固定布局
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="80dp"
    android:orientation="vertical"
    android:layout_height="wrap_content">
    <ImageView
        android:id="@+id/fruitImage"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="10dp"
        android:layout_width="40dp"
        android:layout_height="40dp"/>
    <TextView
        android:id="@+id/fruitName"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="10dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
</LinearLayout>
```
**LayoutManager**指定RecyclerView的布局方式为水平方向布局
```kotlin
class RecyclerViewActivity : AppCompatActivity() {
    lateinit var binding : ActivityRecyclerViewBinding
    private val fruitList = ArrayList<Fruit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerView.layoutManager = layoutManager
        val adapter = FruitRecyclerViewAdapter(fruitList)
        binding.recyclerView.adapter = adapter
    }
    ...
}
```
**网格布局：GridLayoutManager**
**瀑布流布局：StaggeredGridLayoutManager**

### RecyclerView的点击事件
RecyclerView需要我们自己给子项具体的View去注册点击事件
```kotlin
class FruitRecyclerViewAdapter(val fruitList : List<Fruit>) : RecyclerView.Adapter<FruitRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(binding: FruitScrollItemBinding) : RecyclerView.ViewHolder(binding.root){
        val fruitImage : ImageView = binding.fruitImage
        val fruitName : TextView = binding.fruitName
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FruitScrollItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        viewHolder.fruitImage.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val fruit = fruitList[position]
            Toast.makeText(parent.context, "image ${fruit.name}", Toast.LENGTH_SHORT).show()
        }
        viewHolder.fruitName.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val fruit = fruitList[position]
            Toast.makeText(parent.context, "text ${fruit.name}", Toast.LENGTH_SHORT).show()
        }
        return viewHolder
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruitList[position]
        holder.fruitImage.setImageResource(fruit.imageId)
        holder.fruitName.text = fruit.name
    }
    override fun getItemCount(): Int = fruitList.size
}
```
`val position = viewHolder.bindingAdapterPosition`
* getBindingAdapterPostion：返回该ViewHolder相对于它绑定的Adapter中的位置，即**相对位置**。
* getAbsoluteAdapterPosition：返回该ViewHolder相对于RecyclerView的位置，即**绝对位置**。

## 编写界面的最佳实践
### 制作9-Patch图片
9-Patch图片能够指定哪些区域可以被拉伸、哪些区域不可以被拉伸

### 编写精美的聊天界面
Msg实体类
```kotlin
class Msg(val content : String, val type : Int) {
    companion object{
        const val TYPE_RECEIVED = 0
        const val TYPE_SENT = 1
    }
}
```
> 只有在单例类、companion object或顶层方法中才可以使用const关键字

RecyclerView的子项布局有两种，分别创建msg_left_item.xml和msg_right_item.xml

创建RecyclerView的适配器
```kotlin
class MsgAdapter(val msgList : List<Msg>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class LeftViewHolder(binding : MsgLeftItemBinding) : RecyclerView.ViewHolder(binding.root){
        val leftMsg : TextView = binding.leftMsg
    }
    inner class RightViewHolder(binding : MsgRightLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val rightMsg : TextView = binding.rightMsg
    }
    override fun getItemViewType(position: Int): Int {
        val msg = msgList[position]
        return msg.type
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = if(viewType == Msg.TYPE_RECEIVED){
        val binding = MsgLeftItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        LeftViewHolder(binding)
    }else{
        val binding = MsgRightLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        RightViewHolder(binding)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = msgList[position]
        when(holder){
            is LeftViewHolder -> holder.leftMsg.text = msg.content
            is RightViewHolder -> holder.rightMsg.text = msg.content
            else -> throw java.lang.IllegalArgumentException()
        }
    }
    override fun getItemCount(): Int = msgList.size
}
```
最后准备一些数据，并给发送按钮添加点击事件
```kotlin
class UIBestActivity : AppCompatActivity(), View.OnClickListener {
    private val msgList = ArrayList<Msg>()
    private var adapter : MsgAdapter? = null
    lateinit var binding : ActivityUibestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUibestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMsg()
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        adapter = MsgAdapter(msgList)
        binding.recyclerView.adapter = adapter
        binding.sendButton.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v){
            binding.sendButton -> {
                val content = binding.inputText.text.toString()
                if(content.isNotEmpty()){
                    val msg = Msg(content, Msg.TYPE_SENT)
                    msgList.add(msg)
                    //通知列表有新的数据插入
                    adapter?.notifyItemInserted(msgList.size - 1)
                    //将显示的数据定位到最后一行
                    binding.recyclerView.scrollToPosition(msgList.size - 1)
                    binding.inputText.setText("")
                }
            }
        }
    }
    private fun initMsg(){
        val msg1 = Msg("How are you?", Msg.TYPE_RECEIVED)
        msgList.add(msg1)
        val msg2 = Msg("Fine, thank you, and you?", Msg.TYPE_SENT)
        msgList.add(msg2)
        val msg3 = Msg("I'm fine, too!", Msg.TYPE_RECEIVED)
        msgList.add(msg3)
    }
}
```


# Fragment
## Fragment的使用方式
### Fragment的简单用法
首先编写fragment的布局
left_fragment.xml
right_fragment.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <Button
        android:id="@+id/button"
        android:layout_gravity="center_horizontal"
        android:text="button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
</LinearLayout>
```
编写Fragment中的代码
LeftFragment.kt
RightFragment.kt
```kotlin
class LeftFragment : Fragment() {
    private var _binding : LeftFragmentBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = LeftFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```
在activity布局中添加fragment
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <fragment
        android:id="@+id/leftFrag"
        android:name="com.android.fragment.LeftFragment"
        android:layout_width="0dp"
        android:layout_weight="1"
        android:layout_height="match_parent"/>
    <fragment
        android:id="@+id/rightFrag"
        android:name="com.android.fragment.RightFragment"
        android:layout_width="0dp"
        android:layout_weight="1"
        android:layout_height="match_parent"/>
</LinearLayout>
```
> 注意此处需要通过android:name属性显式声明要添加的Fragment类名

### 动态添加Fragment
activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <fragment
        android:id="@+id/leftFrag"
        android:name="com.android.fragment.LeftFragment"
        android:layout_width="0dp"
        android:layout_weight="1"
        android:layout_height="match_parent"/>
    <FrameLayout
        android:id="@+id/rightLayout"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="1"/>
</LinearLayout>
```
MainActivity.kt
```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private lateinit var leftFragmentBinding : LeftFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(AnotherRightFragment())
        val leftFrag = supportFragmentManager.findFragmentById(R.id.leftFrag) as LeftFragment
        leftFrag.binding.button.setOnClickListener {
            Log.d("TestBtn", "clicked")
        }
        replaceFragment(RightFragment())
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(binding.rightLayout.id, fragment)
        transaction.commit()
    }
}
```
`leftFragmentBinding = LeftFragmentBinding.bind(binding.root)`让fragment_one.xml布局和activity_main.xml布局能够关联起来

### 在fragment中实现返回栈
```kotlin
class MainActivity : AppCompatActivity() {
    ...
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(binding.rightLayout.id, fragment)
        //接收一个名字作为返回栈的状态，一般传入null即可
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
```

### Fragment和Activity之间的交互
FragmentManager提供了一个类似于findViewBuId()的方法，专门用于从布局文件中获取Fragment
```kotlin
    val fragment = supportFragmentManager.findFragmentById(R.id.leftFrag) as LeftFragment
```
获取fragment后便可获取其中的binding对象
```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private lateinit var leftFragmentBinding : LeftFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val leftFrag = supportFragmentManager.findFragmentById(R.id.leftFrag) as LeftFragment
        leftFrag.binding.button.setOnClickListener {
            Log.d("TestBtn", "clicked")
            replaceFragment(RightFragment())
        }
    }
}
```

在每个Fragment中都可以通过调用getActivity()方法来得到和当前fragment相关联的Activity实例
```kotlin
    if(activity != null){
        val mainActivity = activity as MainActivity
    }
```
当Fragment中需要使用Context对象时，也可以使用getActivity()方法，因为获取到的Activity本身就是一个Context对象

## Fragment生命周期
### Fragment的状态和回调
**1. 运行状态**
当一个Fragment所关联的Activity正处于运行状态时，该Fragment也处于运行状态
**2. 暂停状态**
当一个Activity进入暂停状态时，与它相关联的Fragment就会进入暂停状态
**3. 停止状态**
当一个Activity进入停止状态时，与它相关联的Fragment就会进入停止状态。或者通过调用FragmentTransaction的remove()、replace()方法将Fragment从Activity中移除，但在事物提交之前**调用了**addToBackStack()方法，这时的Fragment也会进入停止状态。停止状态的Fragment对用户是完全不可见的。
**4. 销毁状态**
当一个Activity被销毁时，与它相关联的Fragment就会进入销毁状态。或者通过调用FragmentTransaction的remove()、replace()方法将Fragment从Activity中移除，但在事物提交之前**没有调用**addToBackStack()方法，这时的Fragment也会进入停止状态。

**onAttach()**：当Fragment和Activity建立关联时调用
**onCreateView()**：为Fragment创建视图时调用
**onActivityCreated()**：确保与Fragment相关联的Activity已经创建完毕时调用
**onDestroyView()**：与Fragment关联的视图被移除时调用
**onDetach()**：Fragment与Activity解除关联时调用


![avatar](images/Fragment.png)

Fragment中可以通过
```kotlin
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
```
来保存数据，保存下来的数据在onCreate()、onCreateView()、onActivityCreated()这三个方法中可以重新得到

## 动态加载布局的技巧
### 使用限定符
在res目录下新建layout-large文件夹，在这个文件夹下新建布局也叫做activity_main.xml。这样那些屏幕被认为是large的设备就会自动加载layout-large文件夹下的布局，小屏幕的设备则还是会加载layout文件夹下的布局。

### 使用最小宽度限定符
在res目录下新建layout-sw600dp文件夹，然后在文件夹下新建activity_main.xml布局，这就意味着，当程序运行在屏幕宽度大于等于600dp的设备上时，会加载layout-sw600dp文件夹下的布局，当程序运行在屏幕宽度小于600dp的设备上时，则仍然加载默认的layout目录下的布局


## Fragment的最佳实践：一个简易版的新闻应用
news_content_frag.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <!--将新闻内容布局设置为不可见，因为在双页模式下，在没选中列表中任何一条新闻时，是不应该看到新闻内容布局的-->
    <LinearLayout
        android:id="@+id/contentLayout"
        android:orientation="vertical"
        android:visibility="invisible"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <TextView
            android:id="@+id/newsTitle"
            android:gravity="center"
            android:padding="10dp"
            android:textSize="20sp"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>
        <View
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:background="#000"/>
        <TextView
            android:id="@+id/newsContent"
            android:padding="15dp"
            android:textSize="18sp"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1"/>
    </LinearLayout>
    <View
        android:layout_alignParentLeft="true"
        android:background="#000"
        android:layout_width="1dp"
        android:layout_height="match_parent"/>
</RelativeLayout>
```
NewsContentFragment.kt
```kotlin
class NewsContentFragment : Fragment() {
    private var _binding : NewsContentFragBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = NewsContentFragBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //提供显示新闻的方法
    fun refresh(title : String, content : String){
        binding.contentLayout.visibility = View.VISIBLE
        binding.newsTitle.text = title
        binding.newsContent.text = content
    }
}
```
layout/activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="horizontal"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/newsTitleLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <fragment
            android:id="@+id/newsTitleFrag"
            android:name="com.android.newsapplication.NewsTitleFragment"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    </FrameLayout>
</LinearLayout>
```
在单页模式中还需要一个显示新闻内容的activity
activity_news_content.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <fragment
        android:id="@+id/newsContentFrag"
        android:name="com.android.newsapplication.NewsContentFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```
NewsContentActivity.kt
```kotlin
class NewsContentActivity : AppCompatActivity() {
    //activity跳转的最佳实践
    companion object{
        fun actionStart(context : Context, title : String, content : String){
            val intent = Intent(context, NewsContentActivity::class.java).apply {
                putExtra("news_title", title)
                putExtra("news_content", content)
            }
            context.startActivity(intent)
        }
    }
    lateinit var binding : ActivityNewsContentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val title = intent.getStringExtra("news_title")
        val content = intent.getStringExtra("news_content")
        if(title != null && content != null){
            val fragment = supportFragmentManager.findFragmentById(R.id.newsContentFrag) as NewsContentFragment
            fragment.refresh(title, content)
        }
    }
}
```
news_title_frag.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/newsTitleRecyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```
news_item.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/newsTitle"
    android:maxLines="1"
    android:ellipsize="end"
    android:textSize="18sp"
    android:paddingLeft="10dp"
    android:paddingRight="10dp"
    android:paddingTop="15dp"
    android:paddingBottom="15dp"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```
NewsTitleFragment.kt
```kotlin
class NewsTitleFragment : Fragment() {
    private var _binding : NewsTitleFragBinding? = null
    val binding get() = _binding!!
    private var isTwoPane = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = NewsTitleFragBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isTwoPane = activity?.findViewById<View>(R.id.newsContentLayout) != null
        val layoutManager = LinearLayoutManager(activity)
        binding.newsTitleRecyclerView.layoutManager = layoutManager
        val adapter = NewsAdapter(getNews())
        binding.newsTitleRecyclerView.adapter = adapter
    }
    //数据准备
    private fun getNews() : List<News>{
        val newsList = ArrayList<News>()
        for (i in 1..50){
            val news = News("This is news title $i.", getRandomLengthString("This is news content $i."))
            newsList.add(news)
        }
        return newsList
    }
    private fun getRandomLengthString(str : String) : String{
        val n = (1..20).random()
        val builder = StringBuilder()
        repeat(n){
            builder.append(str)
        }
        return builder.toString()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //NewsAdapter
    //写成内部类的好处是此处可以直接访问NewsTitleFragment的变量，如isTwoPane
    inner class NewsAdapter(val newsList : List<News>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(){
        inner class ViewHolder(binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root){
            val newsTitle : TextView = binding.newsTitle
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val newsItemBinding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val holder = ViewHolder(newsItemBinding)
            holder.itemView.setOnClickListener {
                val news = newsList[holder.bindingAdapterPosition]
                if(isTwoPane){
                    val fragment = activity?.supportFragmentManager?.findFragmentById(R.id.newsContentFrag) as NewsContentFragment
                    fragment.refresh(news.title, news.content)
                }else{
                    NewsContentActivity.actionStart(parent.context, news.title, news.content)
                }
            }
            return holder
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val news = newsList[position]
            holder.newsTitle.text = news.title
        }
        override fun getItemCount(): Int = newsList.size
    }
}
```
layout-sw600dp/activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="horizontal"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <fragment
        android:id="@+id/newsTitleFrag"
        android:name="com.android.newsapplication.NewsTitleFragment"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="1"/>
    <FrameLayout
        android:id="@+id/newsContentLayout"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="3">
        <fragment
            android:id="@+id/newsContentFrag"
            android:name="com.android.newsapplication.NewsContentFragment"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
    </FrameLayout>
</LinearLayout>
```

# 广播
## 广播机制简介
**Android中广播的分类**
* 标准广播
    异步执行的广播
* 有序广播
    同步执行的广播

## 接收系统广播
**注册BroadcastReceiver的方式**
* 动态注册
* 静态注册
### 动态注册
```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var timeChangeReceiver : TimeChangeReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intentFilter = IntentFilter()
        //系统每隔1分钟就会发出一条android.intent.action.TIME_TICK广播
        intentFilter.addAction("android.intent.action.TIME_TICK")
        timeChangeReceiver = TimeChangeReceiver()
        registerReceiver(timeChangeReceiver, intentFilter)
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(timeChangeReceiver)
    }
    inner class TimeChangeReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Time is changed", Toast.LENGTH_SHORT).show()
        }
    }
}
```
系统广播列表可以在 **\<Android SDK>/platforms/<android api版本>/data/broadcast_actions.txt** 查看
**动态注册的BroadcastReceiver一定要取消注册**

### 静态注册
动态注册的BroadcastReceiver可以自由控制注册与注销，但其必须在程序启动后才能接收广播。

实现开机启动
```kotlin
class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Toast.makeText(context, "Boot Complete", Toast.LENGTH_SHORT).show()
    }
}
```
在AndroidManifest.xml中注册，并添加权限
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <!--权限-->
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.BroadcastReceiverApplication"
        tools:targetApi="31">
        <!--注册-->
        <receiver
            android:name=".BootCompleteReceiver"
            android:enabled="true"
            android:exported="true"></receiver>
        ...
    </application>
</manifest>
```
BroadcastReceiver中是不允许开启线程的，当onRecrive()方法运行了较长时间而没有结束时，程序就会出现错误。

## 发送自定义广播
### 发送标准广播
```kotlin
class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Toast.makeText(context, "Receive in myBroadcastReceiver", Toast.LENGTH_SHORT).show()
    }
}
```
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.BroadcastReceiverApplication"
        tools:targetApi="31">
        <receiver
            android:name=".MyBroadcastReceiver"
            android:enabled="true"
            android:exported="true">
            <intent-filter android:priority="100">
                <action android:name="com.android.broadcastreceiverapplication.MY_BROADCAST" />
            </intent-filter>
        </receiver>
        ...
    </application>

</manifest>
```
```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            val intent = Intent("com.android.broadcastreceiverapplication.MY_BROADCAST")
            intent.setPackage(packageName)
            sendBroadcast(intent)
        }
    }
}
```
在Android8.0系统之后，静态注册的BroadcastReceiver是无法接收隐式广播的，而默认情况下我们自定义的广播恰恰都是隐式广播，因此一定要调用setPackage(packageName)方法指定这条广播是发给哪个应用程序的，从而让它变成一条显式广播。
### 发送有序广播
自定义另一个BroadcastReceiver
```kotlin
class AnotherBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Toast.makeText(context, "Receive in anotherBroadcastReceiver", Toast.LENGTH_SHORT).show()
    }
}
```
```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            val intent = Intent("com.android.broadcastreceiverapplication.MY_BROADCAST")
            intent.setPackage(packageName)
            sendOrderedBroadcast(intent, null)
        }
    }
}
```
```xml
    <!--指定优先级-->
            <intent-filter android:priority="100">
                <action android:name="com.android.broadcastreceiverapplication.MY_BROADCAST" />
            </intent-filter>
```
拦截广播
```kotlin
class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Toast.makeText(context, "Receive in myBroadcastReceiver", Toast.LENGTH_SHORT).show()
        abortBroadcast()
    }
}
```

## 广播的最佳实践：实现强制下线功能
强制下线需要关闭所有的Activity，因此建立ActivityCollector
```kotlin
object ActivityCollector {
    private val activities = ArrayList<Activity>()
    fun addActivity(activity: Activity){
        activities.add(activity)
    }
    fun removeActivity(activity: Activity){
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
创建BaseActivity作为所有Activity的父类
```kotlin
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
}
```
创建LoginActivity与其布局
```kotlin
class LoginActivity : BaseActivity() {
    lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.login.setOnClickListener {
            val account = binding.accountEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            if(account == "admin" && password == "123456"){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
```
在MainActivity中发送强制下线广播
```kotlin
class MainActivity : BaseActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.forceOffline.setOnClickListener {
            val intent = Intent("com.android.broadcastpractice.FORCE_OFFLINE")
            sendBroadcast(intent)
        }
    }
}
```
由于静态注册的BroadcastReceiver是无法在onReceive()方法中弹出对话框这样的UI控件的，而我们也无法在每一个Activity中都注册一个动态的BroadcastReceiver。因此，可以在BaseActivity中动态注册一个BroadcastReceiver即可
```kotlin
open class BaseActivity : AppCompatActivity() {
    lateinit var receiver : ForceOfflineReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
    }
    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.android.broadcastpractice.FORCE_OFFLINE")
        receiver = ForceOfflineReceiver()
        registerReceiver(receiver, intentFilter)
    }
    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }
    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
    inner class ForceOfflineReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent?) {
            AlertDialog.Builder(context).apply {
                setTitle("Warning")
                setMessage("You are forced to offline")
                setCancelable(false)
                setPositiveButton("OK"){_, _ ->
                    ActivityCollector.finishAll()
                    val i = Intent(context, LoginActivity::class.java)
                    context.startActivity(i)
                }
                show()
            }
        }
    }
}
```
在onResume()和onPause()方法中注册与取消注册的原因：我们始终只需要保证处于栈顶的Activity才能接收到这条广播即可。


# 数据存储——持久化技术
## 持久化技术简介
Android中主要提供了3种方式用于简单的实现数据持久化功能：
    1. 文件存储
    2. SharedPreferences
    3. 数据库存储

## 文件存储
Context类中提供了一个openFileOutput()方法，可以用于将数据存储到指定的文件中。此方法接收两个参数，第一个参数是文件名，第二个参数是操作模式，主要有MODE_PRIVATE和MODE_APPEND两种模式。在Android4.2版本中被废弃了两种操作模式，MODE_WORLD_READABLE和MODE_WORLD_WRITEABLE。此方法返回一个FileOutputStream对象。

### 存储
```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun onDestroy() {
        super.onDestroy()
        val inputText = binding.editText.text.toString()
        save(inputText)
    }
    private fun save(inputText : String){
        try {
            //获得FileOutputStream对象
            val output = openFileOutput("data", Context.MODE_PRIVATE)
            //构建OutputStreamWriter对象，再构建BufferWriter对象
            val writer = BufferedWriter(OutputStreamWriter(output))
            //使用use函数保证Lambda表达式中的代码全部执行完之后自动将外层的流关闭
            writer.use {
                it.write(inputText)
            }
        }catch (e : IOException){
            e.printStackTrace()
        }
    }
}
```

### 读取
在Context类中提供了一个openFileInput()方法，它接收一个参数，即要读取的文件名，然后系统会自动到/data/data/\<package name\>/files/目录下加载这个文件，并返回一个FileInputStream对象，得到这个对象，再通过流的方式就可以将数据读出来的。
```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val inputText = load()
        if(inputText.isNotEmpty()){
            binding.editText.setText(inputText)
            //将光标移动到文本的末尾
            binding.editText.setSelection((inputText.length))
            Toast.makeText(this, "succeed", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroy() {...}
    private fun load() : String{
        val content = StringBuilder()
        try{
            //获取FileInputStream对象
            val input = openFileInput("data")
            //构建InputStreamReader对象，再构建BufferedReader对象
            val reader = BufferedReader(InputStreamReader(input))
            //通过BufferedReader对象将文件中的数据一行一行读取出来
            reader.use {
                reader.forEachLine {
                    content.append(it)
                }
            }
        }catch (e : IOException){
            e.printStackTrace()
        }
        return content.toString()
    }

    private fun save(inputText : String){...}
}
```

## SharedPreferences存储
SharedPreferences是使用**键值对**的方式来存储数据的
### 将数据存储到SharedPreferences中
#### 获取SharedPreferences
**1. Context类中的getSharedPreferences()方法**
此方法接收两个参数：
1. 第一个参数用于指定SharedPreferences文件的名称，如果文件不存在则会创建一个
2. 第二个参数用于指定操作模式，目前只有默认的MODE_PRIVATE这一种模式可选。MODE_WORLD_READABLE、MODE_WORLD_WRITEABLE、MODE_MULTI_PROCESS模式均已被废弃。

**2. Activity类中的getPreferences()方法**
此方法只接收一个操作模式参数，因为使用这个方法时会自动将当前Activity的类名作为SharedPreferences的文件名。
#### 向SharedPreferences中存储数据
1. 调用SharedPreferences对象的edit()方法获取一个SharedPreferences.Editor对象。
2. 向SharedPreferences.Editor对象中添加数据。
3. 调用apply()方法将添加的数据提交。

```kotlin
class SharedPreferencesActivity : AppCompatActivity() {
    lateinit var binding :  ActivitySharedPreferencesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.saveButton.setOnClickListener {
            val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
            editor.putString("name", "Tom")
            editor.putInt("age", 28)
            editor.putBoolean("married", false)
            editor.apply()
        }
    }
}
```

### 从SharedPreferences中读取数据
SharedPreferences对象中提供了一系列的get方法用于读取存储的数据，每种get方法都对应了SharedPreferences.Editor中的一种put方法。这些get()方法都接收两个参数，第一个参数是键，第二个参数是默认值，表示当传入的键找不到对应的值时会以什么样的默认值进行返回。
```kotlin
class SharedPreferencesActivity : AppCompatActivity() {
    lateinit var binding :  ActivitySharedPreferencesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        binding.restoreButton.setOnClickListener {
            val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
            val name = prefs.getString("name", "")
            val age = prefs.getInt("age", 0)
            val married = prefs.getBoolean("married", false)
            Log.d("SharedPreferences", "name is $name")
            Log.d("SharedPreferences", "age is $age")
            Log.d("SharedPreferences", "married is $married")
        }
    }
}
```
### 实现记住密码功能
```kotlin
class LoginActivity : BaseActivity() {
    lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs = getPreferences(Context.MODE_PRIVATE)
        val isRemember = prefs.getBoolean("remember_password", false)
        if(isRemember){
            val account = prefs.getString("account", "")
            val password = prefs.getString("password", "")
            binding.accountEdit.setText(account)
            binding.passwordEdit.setText(password)
            binding.rememberPass.isChecked = true
        }
        binding.login.setOnClickListener {
            val account = binding.accountEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            if(account == "admin" && password == "123456"){
                val editor = prefs.edit()
                if(binding.rememberPass.isChecked){
                    editor.putBoolean("remember_password", true)
                    editor.putString("account", account)
                    editor.putString("password", password)
                }else{
                    editor.clear()
                }
                editor.apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
```

## SQLite数据库存储
Android为了让我们更加方便地管理数据库，专门提供了一个SQLiteOpenHelper帮助类。
SQLiteOpenHelper是一个抽象类，其中有两个抽象方法：**onCreate()** 和 **onUpgrade()**。
SQLiteOpenHelper中还有两个实例方法：**getReadableDatabase()** 和**getWritableDatabase()**。当数据库不可写入时（如磁盘空间已满），getReadableDatabase()方法返回的对象以只读的方式打开数据库，而getWritableDatabase()方法将抛出异常。
SQLiteOpenHelper中有两个构造方法可供重写，一般使用参数少一点的那个构造函数。此构造函数中接收4个参数：第一个参数是Context；第二个参数是数据库名；第三个参数允许我们在查询数据库时返回一个自定义的Cursor，一般传入null即可；第四个参数表示当前数据库的版本号，可用于对数据库进行升级操作。

### 创建数据库
```kotlin
class MyDatabaseHelper(val context : Context, name : String, version : Int) : SQLiteOpenHelper(context, name, null, version) {
    private  val createBook = "create table book(" +
            "id integer primary key autoincrement," +
            "author text," +
            "price real," +
            "pages integer," +
            "name text)"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createBook)
        Toast.makeText(context, "Create succeed", Toast.LENGTH_SHORT).show()
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}
```
```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dbHelper = MyDatabaseHelper(this, "BookStore.db", 1)
        binding.createDatabase.setOnClickListener {
            dbHelper.writableDatabase
        }
    }
}
```

### 升级数据库


### 添加数据


### 更新数据


### 删除数据


### 查询数据


### 使用SQL操作数据库


## SQLite数据库的最佳实践


