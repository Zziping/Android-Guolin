package com.android.firstofall

import kotlin.math.max

fun main(){
    checkNumber(3.4)
    for(i in 0..10){
        println(i)
    }
}

fun largerNumber(num1 : Int, num2 : Int) : Int {
    return max(num1, num2)
}

fun checkNumber(num : Number){
    when(num){
        is Int -> println("number is Int")
        is Double -> println("number is Double")
        else -> println("number not support")
    }
}