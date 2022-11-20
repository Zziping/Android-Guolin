package com.android.firstofall

class Person {
    var name = ""
    var age = 0
    fun eat(){
        println("$name is eating. He is $age years old.")
    }
}

fun main(){
    val p = Person()
    p.name = "Tom"
    p.age = 20
    p.eat()
}