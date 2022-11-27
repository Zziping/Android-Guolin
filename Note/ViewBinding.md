# ViewBinding
一旦启动了ViewBinding功能之后，Android Studio会自动为我们所编写的每一个布局文件都生成一个对应的Binding类。
```xml
<!--build.gradle-->
android {
    ...
    buildFeatures {
        viewBinding true
    }
}
```
Binding类的命名规则是将布局文件按驼峰方式重命名后，再加上Binding作为结尾。

比如说，我们定义一个activity_main.xml布局，那么与它对应的Binding类就是ActivityMainBinding。

当然，如果有些布局文件你不希望为它生成对应的Binding类，可以在该布局文件的根元素位置加入如下声明：
```xml
<LinearLayout
    xmlns:tools="http://schemas.android.com/tools"
    ...
    tools:viewBindingIgnore="true">
    ...
</LinearLayout>
```
## 在Activity中使用ViewBinding
```kotlin
class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)    //from getRoot()
        binding.button1.text = "Hello World!"
        binding.button1.setOnClickListener {
            Toast.makeText(this, "Button clicked.", Toast.LENGTH_SHORT).show()
        }
    }
}
```
**如果需要在onCreate()函数之外的地方对空间进行控制，将binding变量声明为全局变量**
Kotlin声明的变量都必须在声明的同时对其进行初始化。而这里我们显然无法在声明全局binding变量的同时对它进行初始化，所以这里又使用了lateinit关键字对binding变量进行了延迟初始化。
```kotlin
class FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button1.text = "Hello World!"
        binding.button1.setOnClickListener {
            Toast.makeText(this, "Button clicked.", Toast.LENGTH_SHORT).show()
        }
    }
}
```

## 在Fragment中使用ViewBinding
通过以下方法来实现在MainFragment中显示布局
```kotlin
class MainFragment : Fragment(){
    //属性名前加下划线通常意味着不打算直接访问该属性
    private var _binding : FragmentMainBinding? = null
    //get()意味着返回_binding!!给getBinding()函数
    val binding get() = _binding!!
    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View{
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }
}
```
由于我们是在onCreateView()函数中加载的布局，那么理应在与其对应的onDestroyView()函数中对binding变量置空，从而**保证binding变量的有效生命周期是在onCreateView()函数和onDestroyView()函数之间**。

## 3. 在Adapter中使用ViewBinding
假设我们定义了fruit_item.xml来作为RecyclerView的子项布局
编写如下RecyclerView Adapter来加载和显示这个子项布局
```kotlin
class FruitAdapter(val fruitList : List<Fruit>) : RecyclerView.Adapter<FruitAdapter.ViewHolder>(){
    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val fruitImage : ImageView = view.findViewById(R.id.fruitImage)
        val fruitName : TextView = view.findViewById(R.id.fruitName)
    }
    override fun onCreateViewHolder(parent : ViewGroup, ViewType : Int) : ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fruit_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder : ViewHolder, position : Int){
        val fruit = fruitList[position]
        holder.fruitImage.setImageResource(fruit.imageId)
        holder.fruitName.text = fruit.name
    }
    override fun getItemCount() = fruitList.size
}
```
接下来在Adapter中使用ViewBinding
```kotlin
class FruitAdapter(val fruitList : List<Fruit>) : RecyclerView.Adapter<FruitAdapter.ViewHolder>(){
    inner class ViewHolder(binding : FruitItemBinding) : RecyclerView.ViewHolder(binding.root){
        val fruitImage : ImageView = binding.fruitImage
        val fruitName : TextView = binding.fruitName
    }
    override fun onCreateViewHolder(parent : ViewGroup, ViewType : Int) : ViewHolder{
        val binding = FruitItemBinding.inflate(LayoutInflter.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder : ViewHolder, position : Int){
        val fruit = fruitList[position]
        holder.fruitImage.setImageResource(fruit.imageId)
        holder.fruitName.text = fruit.name
    }
    override fun getItemCount() = fruitList.size
}
```
> 1. 我们在onCreateViewHolder()函数中调用FruitItemBinding的inflate()函数去加载fruit_item.xml布局文件。
> 2. 接下来需要改造ViewHolder，让其构造函数接收FruitItemBinding这个参数。但是注意，ViewHolder的父类RecyclerView.ViewHolder它只会接收View类型的参数，因此我们需要调用binding.root获得fruit_item.xml中根元素的实例传给RecyclerView.ViewHolder。

## 4. 对引入布局使用ViewBinding
假设我们有一titlebar.xml作为通用布局引入到其他布局中
**1. 使用include引入布局**
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    ...
    <include 
    <!--在include的时候给被引入的布局添加一个id，ViewBinding便可以关联到titlebar.xml中的控件-->
        android:id="@+id/titleBar"
        layout="@layout/titlebar" />
    ...
</LinearLayout>
```
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.titleBar.title.text = "Title"
        binding.titleBar.back.setOnClickListener {
        }
        binding.titleBar.done.setOnClickListener {
        }
    }
}
```

**2. 使用merge引入布局**
使用merge标签引入的布局在某些情况下可以减少一层布局的嵌套，而更少的布局嵌套通常就意味着更高的效率
```xml
<merge xmlns:android="http://schemas.android.com/apk/res/android">
    <Button
        android:id="@+id/back"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentLeft="true"
        android:layout_centerVertical="true"
        android:text="Back" />
    <TextView
        android:id="@+id/title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:text="Title"
        android:textSize="20sp" />
    <Button
        android:id="@+id/done"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentRight="true"
        android:layout_centerVertical="true"
        android:text="Done" />
</merge>
```
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    ...
    <include
        layout="@layout/titlebar" />
    ...
</LinearLayout>
```
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //TitlebarBinding就是Android Studio根据我们的titlebar.xml布局文件自动生成的Binding类
    private lateinit var titlebarBinding: TitlebarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        titlebarBinding = TitlebarBinding.bind(binding.root)
        setContentView(binding.root)
        titlebarBinding.title.text = "Title"
        titlebarBinding.back.setOnClickListener {
        }
        titlebarBinding.done.setOnClickListener {
        }
    }
}
```

## 5. 在自定义控件中使用ViewBinding
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
使用ViewBinding
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

## 6. 在Activity中获取fragment的binding对象
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
</LinearLayout>
```
fragment_one.xml
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
OneFragment.kt
```kotlin
class OneFragment : Fragment() {
    private var _binding : LeftFragmentBinding? = null
    //此处设置为public，这样可以在Activity中获取_binding
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
MainActivity.kt
```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val leftFrag = supportFragmentManager.findFragmentById(R.id.leftFrag) as LeftFragment
        leftFrag.binding.button.setOnClickListener {
            Log.d("TestBtn", "clicked")
        }
    }
}
```