# 变量和函数

*val*
*var*
*出色的类型推导机制*
*kotlin中全部使用对象数据类型*
```kotlin
val a : Int = 10
val a = 10
```
**kotlin函数语法糖**
```kotlin
fun largerNumber(num1 : Int, num2 : Int) : Int {
    return max(num1, num2)
}
//-->简化
fun largerNumber(num1 : Int, num2 : Int) : Int = max(num1, num2)
//-->简化
fun largerNumber(num1 : Int, num2 : Int) = max(num1, num2)
```

# 程序逻辑控制
**if条件语句**
```kotlin
fun largerNumber(num1 : Int, num2 : Int) : Int {
    var res = 0;
    if(num1 > num2){
        res = num1
    }else{
        res = num2
    }
    return res
}
//-->if语句可以有返回值
fun largerNumber(num1 : Int, num2 : Int) : Int {
    var res = if(num1 > num2){
        num1
    }else{
        num2
    }
    return res
}
//-->
fun largerNumber(num1 : Int, num2 : Int) : Int {
    return if(num1 > num2){
        num1
    }else{
        num2
    }
}
//-->语法糖
fun largerNumber(num1 : Int, num2 : Int) = if(num1 > num2){
    num1
}else{
    num2
}
//-->
fun largerNumber(num1 : Int, num2 : Int) = if(num1 > num2) num1 else num2
```
**when条件语句**
*匹配值 -> {执行逻辑}*
*当执行逻辑只有一行代码时大括号可以省略*
```kotlin
fun getScore(name : String) = if(name == "Tom"){
    86
}else if(name == "Jack"){
    90
}else{
    0
}
//-->when
fun getScore(name : String) = when(name){
    "Tom" -> 86
    "Jack" -> 90
    else -> 0
}
```
*使用when进行类型匹配*
```kotlin
fun checkNumber(num : Number){
    when(num){
        is Int -> println("number is Int")
        is Double -> println("number is Double")
        else -> println("number not support")
    }
}
```
*when语句的不带参写法*
```kotlin
fun getScore(name : String) = when{
    name == "Tom" -> 86
    name == "Jack" -> 90
    else -> 0
}
```

**while循环语句**

**for循环语句**
*kotlin区间*
```kotlin
val range = 0..10   //[0,10]
val range = 0 until 10  //[0,10)
val range = 10 downTo 1 //[10,1]
```
```kotlin
fun main(){
    for(i in 0..10){
        println(i)
    }
}
```

# 面向对象编程
**类和对象**
```kotlin
class Person{
    var name = ""
    var age = 0
    fun eat(){
        println("$name is eating.He is $age years old.")
    }
}
```
*字符串模板*
1. 在字符串中进行变量输出
    ```kotlin
    fun main(){
        val num = 10
        println("num is ${num}")
        //其中大括号可省略
        println("num is $num")
    }
    ```
2. 在字符串中调用其他方法
    ```kotlin
    fun main(){
        println("result is ${getResult()}")
    }
    fun getResult() : String{
        return "Hello World!"
    }
    ```
3. 在字符串中执行表达式
    ```kotlin
    fun main(){
        val str = "1,2,3,4,5"
        println("change to ${str.replace(",", " ")}")
    }
    ```

**继承**
*kotlin中任何一个非抽象类默认都是不可以被继承的*
```kotlin
open class Person{
    var name = ""
    var age = 0
    
}
class Student : Person(){
    var grade = 0
}
```

**主构造函数**
*每个类默认都会有一个不带参数的主构造函数，当然也可以显式地给他指明参数*
*主构造函数的特点是没有函数体，直接定义在类名后面即可*
```kotlin
class Student(val grade : Int) : Person(){
}
```
以上写法就表明在对Student进行实例化的时候必须传入构造函数中要求的所有参数。

*主构造函数没有函数体，Kotlin提供init结构体用于实现主构造函数中的逻辑*
```kotlin
class Student(val grade : Int) : Person(){
    init{
        println("grade is $grade")
    }
}
```
*子类的构造函数必须调用父类的构造函数*
*子类的主构造函数调用父类的哪个构造函数，在继承的时候通过括号来指定*

```kotlin
open class Person(val name : String){

}
class Student(val grade : Int, name : String) : Person(name){

}
```
此处在Student类的主构造函数中增加name字段时，不能再将其声明为val，因为在*主构造函数中声明成val或者var的参数将自动成为该类的字段*，此处使name字段的作用域仅限定在主构造函数当中即可。若一kotlin类继承Java类，**可以**将父类构造函数中存在的字段在kotlin构造函数中声明为val。

**次构造函数**
*一个类只能有一个主构造函数，但可以有多个次构造函数。次构造函数也可以用于实例化一个类，只不过它是有函数体的*
*Kotlin规定：当一个类既有主构造函数又有次构造函数时，所有的次构造函数必须调用主构造函数（包括间接调用）*

```kotlin
open class Person(val name : String){

}
class Student(val grade : Int, name : String) : Person(name){
    constructor(grade : Int) : this("", grade){

    }

    //间接调用主构造函数
    constructor() : this(""){

    }
}
```

*Kotlin中允许一个类只有次构造函数而没有主构造函数*
```kotlin
//由于没有主构造函数，次构造函数只能直接调用父类的构造函数，使用super关键字
class Student : Person{
    constructor(name : String) : super(name){

    }
}
```

## 接口
```kotlin
interface Study{
    fun readBooks()
}
```
```kotlin
class Student(name : String) : Person(name), Study{
    override fun readBooks(){

    }
}
```
**多态**
```kotlin
fun main(){
    val student = Student("Jack")
    doStudy(student)
}
//由于Student类实现了Study接口，因此Student类的实例是可以传递给doStudy()函数的
fun doStudy(study : Study){
    study.readBooks()
}
```
*Kotlin允许对接口中定义的函数进行默认实现*
*如果接口中的函数拥有了函数体，这个函数体中的内容就是它的默认实现，当一个类实现这个接口时，拥有默认实现的函数可以**自由选择实现或者不实现***

**函数的可见性修饰符**
1. public
    * ***默认***
    * 对所有类都可见
2. private
    * 只对当前类内部可见
3. protected
    * 对当前类和子类可见
4. internal
    * 只对同一模块中的类可见

|修饰符|Java|Kotlin|
|:-:|:-:|:-:|
|public|所有类可见|所有类可见（默认）|
|private|当前类可见|当前类可见|
|protected|当前类、子类、同一包路径下的类可见|当前类、子类可见|
|default|同一包路径下的类可见（默认）|无|
|internal|无|同一模块中的类可见|

## 数据类与单例类
**data关键字——数据类**
*当在一个类前面声明了data关键字时，就表明你希望这个类是一个数据类*
*当一个类没有任何代码时，还可以将尾部的大括号省略*
```kotlin
data class Cellphone(val brand : String)
fun main(){
    val cellphone = Cellphone("Samsung")
    val cellphone1 = Cellphone("Samsung")
    println(cellphone)
    println("Cellphone equals Cellphone1 is ${cellphone == cellphone1}")    //true
}
```

**object关键字——单例类**
```kotlin
object Singleton{
    fun singletonTest(){
        println("singletonTest is called")
    }
}
fun main(){
    Singleton.singletonTest()
    //Kotlin自动创建了一个Singleton的实例
}
```

# Lambda编程
## 集合的创建与遍历
**list & set**
```kotlin
//不可变集合
val list = listOf("apple", "banana", "pear")    //list中可以有重复元素
val set = setOf("apple", "banana", "pear")      //set中不能有重复元素
//可变集合
val list = mutableListOf("apple", "banana", "pear")
val set = mutableSetOf("apple", "banana", "pear")
```

**map**
> Kotlin中不建议使用put()和get()方法来对Map进行添加和读取数据操作，而是更加推荐使用一种类似于数组下标的语法结构
> ```kotlin
> val map = HashMap<String, Int>()
> map["apple"] = 1
> val num = map["apple"]
> ```

```kotlin
//Map的简化用法
val map = mapOf("apple" to 1, "banana" to 2, "pear" to 3)           //不可变
val map = mutableMapOf("apple" to 1, "banana" to 2, "pear" to 3)    //可变
//在遍历时
for((fruit, num) in map){
    ...
}
```

## 集合的函数式API
> Lambda就是一小段可以作为参数传递的代码
> Lambda表达式的语法结构：**{参数名1 : 参数类型1, 参数名2 : 参数类型2, ... -> 函数体}**

```kotlin
val list = listOf("apple", "banana", "pear")
val maxLengthFruit = list.maxByOrNull { it.length }  //找到list中长度最大的字符串

//maxByOrNull()函数的工作原理是根据传入的条件遍历集合，从而找到最大值
val lambda = { fruit : String -> fruit.length }
val maxLengthFruit = list.maxByOrNull(lambda)

//可以直接将lambda表达式传入maxByOrNull()函数中，因此可以简化为
val maxLengthFruit = list.maxByOrNull({ fruit : String -> fruit.length })

//根据Kotlin规定，当Lambda参数是函数的最后一个参数时，可以将Lambda表达式移到函数括号的外面
val maxLengthFruit = list.maxByOrNull() { fruit : String -> fruit.length }

//如果Lambda参数是函数的唯一一个参数的话，还可以将函数的括号省略
val maxLengthFruit = list.maxByOrNull { fruit : String -> fruit.length }

//由于Kotlin优秀的类型推导机制，Lambda表达式中的参数列表大多数情况下不必声明参数类型
val maxLengthFruit = list.maxByOrNull { fruit -> fruit.length }

//当Lambda表达式的参数列表只有一个参数时，也不必声明参数名而是可以使用it关键字来代替
val maxLengthFruit = list.maxByOrNull { it.length }
```
**map函数**
> 用于将集合中的每一个元素都映射成一个另外的值，映射的规则在Lambda表达式中指定，最终生成一个新的集合
> ```kotlin
> fun main(){
>     val list = listOf("apple", "banana", "pear")
>     val newList = list.map { it.toUpperCase() }   //将字母转化为大写
> }
> ```

**filter函数**
> 用于过滤集合中的数据
> ```kotlin
> fun main(){
>     val list = listOf("apple", "banana", "pear")
>     val newList = list.filter{ it.length < 5 }    //保留字符串长度小于5的元素
>                       .map { it.toUpperCase() }   //将字母转化为大写
> }
> ```

**any函数 & all函数**
> any函数用于判断集合中是否至少存在一个元素满足指定条件
> all函数用于判断集合中是否所有元素都满足指定条件
> ```kotlin
> fun main(){
>     val list = listOf("apple", "banana", "pear")
>     val anyResult = list.any{ it.length < 5 }     //true
>     val allResult = list.all{ it.length < 5 }     //false
> }
> ```

## Java函数式API的使用
> 如果我们在Kotlin代码中调用了一个Java方法，并且该方法接收一个**Java单抽象方法接口**参数，就可以使用函数式API。
```java
//在Java中创建并执行一个子线程
new Thread(new Runnable() {
    @Override
    public void run() {
        ...
    }
}).start();
```
```kotlin
//将上述写法翻译为kotlin版本，由于kotlin舍弃了new关键字，因此创建匿名类实例的时候使用object关键字
Thread(object : Runnable {
    override fun run() {
        ...
    }
}).start()

//由于Runnable类中只有一个待实现的方法，因此不必显式地重写run()方法
Thread(Runnable {
    ...
}).start()

//如果一个Java方法地参数列表中有且仅有一个Java单抽象方法接口参数，我们还可以省略接口名
Thread({
    ...
}).start()

//当Lambda表达式是方法的最后一个参数时，可以将Lambda表达式移到方法括号的外面；如果Lambda表达式还是方法的唯一一个参数，还可以将方法的括号省略
Thread {
    ...
}.start()
```

# 空指针检查
## 可空类型系统
> 在类名后面加一个问号则为可空类型系统
> 例如：**Int**表示不可为空的整型，而**Int?** 就表示可为空的整型

## 判空辅助工具
**?.操作符**
*当对象不为空时正常调用相应的方法，当对象为空时则什么都不做*
```kotlin
if(a != null){
    a.doSth()
}

//使用 ?. 操作符可简化为
a?.doSth()
```

**?:操作符**
> 这个操作符的左右两边都接收一个表达式，如果左边表达式的结果不为空就返回左边表达式的结果，否则返回右边表达式的结果

```kotlin
val c = if(a != null){
    a
}else{
    b
}

//使用 ?: 操作符可简化为
val c = a ?: b
```

**!!非空断言工具**
```kotlin
var content : String? = "hello"
fun main(){
    if(content != null){
        printUpperCase()
    }
}
fun printUpperCase(){
    val upperCase = content!!.toUpperCase()
    println(upperCase)
}
```

**let函数**
> let函数提供了函数式API的编程接口，并将原始调用对象作为参数传递到Lambda表达式中
> 示例代码如下：
> ```kotlin
> obj.let { obj2 ->
>     ...
> }
> ```

```kotlin
fun doStudy(study : Study?){
    study?.readBooks()
    study?.doHomeworks()
}

//使用 ?.操作符和let函数结合优化代码
fun doStudy(study : Study?){
    study?.let{ stu ->
        stu.readBooks()
        stu.doHomework()
    }
}

//当Lambda表达式的参数列表只有一个参数时，可以使用it关键字来代替
fun doStudy(study : Study?){
    study?.let{
        it.readBooks()
        it.doHomework()
    }
}
```
## 函数的参数默认值
> Kotlin提供了给函数设置参数默认值的功能，并且调用此函数时不会强制要求调用方为此参数传值，在没有传值的情况下会自动使用参数的默认值。

```kotlin
fun printParams(num : Int, str : String = "hello"){
    println("num is $num, str is $str")
}
fun main(){
    printParams(123)
}
```
> Kotlin还提供了通过键值对的方式传参

```kotlin
fun main(){
    printParams(num = 123)
}
```

kotlin中完全可以通过只编写一个主构造函数，然后给参数设定默认值的方式来实现与次构造函数一样的效果


## kotlin语法糖
在Java中有JavaBean的概念，会根据类中的字段自动生成Getter和Setter方法

在kotlin中可以使用一种更加简便的写法，看上去可以直接对字段进行赋值和读取，其实kotlin背后会自动调用Getter或Setter方法


# 标准函数和静态方法
## 标准函数
### with
with接收两个参数，第一个参数是任意类型的对象，第二个参数是一个Lambda表达式
with函数会在Lambda表达式中提供第一个参数对象的上下文，并使用Lambda表达式最后一行代码作为返回值返回
```kotlin
fun main(){
    val list = listOf("apple", "banana", "orange", "pear")
    val result = with(StringBuilder()){
        append("start eating")
        for(fruit in list){
            append(fruit).append("\n")
        }
        append("end eating")
        toString()
    }
    println(result)
}
```

### run
run方法需要在某个对象的基础上调用，只接收一个Lambda参数，并且在Lambda表达式中提供调用对象的上下文
```kotlin
fun main(){
    val list = listOf("apple", "banana", "orange", "pear")
    val result = StringBuilder().run{
        append("start eating")
        for(fruit in list){
            append(fruit).append("\n")
        }
        append("end eating")
        toString()
    }
    println(result)
}
```
### apply
与run方法类似
但apply函数无法指定返回值
```kotlin
fun main(){
    val list = listOf("apple", "banana", "orange", "pear")
    val result = StringBuilder().apply{
        append("start eating")
        for(fruit in list){
            append(fruit).append("\n")
        }
        append("end eating")
    }
    println(result.toString())
}
```

## 静态方法
### 单例类
单例类会使类中所有方法都成为静态方法
### companion object
```kotlin
class Util{
    fun Method1(){

    }
    companion object{
        fun Method2(){

        }
    }
}
```
Method2()并不是静态方法，companion object关键字会在类Util的内部创建一个伴生类，Method2()就是定义在这个伴生类里面的实例方法。但Kotlin会保证Util类始终只会存在一个伴生类对象，因此调用Util.Method2()方法实际上就是调用了Util类中伴生类的方法
### @JvmStatic注解
```kotlin
class Util{
    fun Method1(){

    }
    companion object{
        @JvmStatic
        fun Method2(){

        }
    }
}
```
Method()方法成为真正的静态方法
### 顶层方法
```kotlin
fun Method(){

}
```
在Kotlin File（相比于Kotlin Class而言）中直接定义的方法为顶层方法，顶层方法全部编译为静态方法

# 延迟初始化和密封类
## 延迟初始化
如果类中存在很多全局变量实例，为了保证它们能够满足Kotlin的空指针检查语法标准，将不得不做很多非空判断。
对全局变量进行延迟初始化便可以简化代码的书写，也可以避免冗余的非空判断。
但lateinit关键字也不是没有任何风险，我们必须在使用前对lateinit的变量进行初始化。

可以使用`::adapter.isInitialized`来判断adapter变量是否已经初始化

## 密封类


# 扩展函数和运算符重载
## 扩展函数

## 运算符重载
语法糖表达式和实际调用函数对照表
|语法糖表达式|实际调用函数|
|:-:|:-:|
|a + b|a.plus(b)|
|a - b|a.minus(b)|
|a * b|a.times(b)|
|a / b|a.div(b)|
|a % b|a.rem(b)|
|a++|a.inc()|
|a--|a.dec()|
|+a|a.umaryPlus()|
|-a|a.unaryMinus()|
|!a|a.not()|
|a == b|a.equals(b)|
|a > b|a.compareTo(b)|
|a < b|a.compareTo(b)|
|a >= b|a.compareTo(b)|
|a <= b|a.compareTo(b)|
|a..b|a.rangeTo(b)|
|a[b]|a.get(b)|
|a[b] = c|a.set(b, c)|
|a in b|b.contains(a)|


# 高阶函数
## 定义高阶函数

## 内联函数的作用

## noinline与crossinline


# 高阶函数的应用

**Smart Cast**

**contentValuesOf()**
```kotlin
    val values = contentValuesOf("name" to "Game of Thrones", "author" to "George Martin", "pages" to 720, "price" to 20.85)
    db.insert("book", null, values)
```

# 泛型和委托
## 泛型的基本用法


## 类委托和委托属性


## 实现lazy函数


