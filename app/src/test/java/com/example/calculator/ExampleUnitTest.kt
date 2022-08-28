package com.example.calculator

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Test
    fun checkFirstExample(){
        val result=CalculatorUtils.calculateExpression("25-2*10")
        assert(result=="5.0")
    }

    @Test
    fun checkSeconExample(){
        val result=CalculatorUtils.calculateExpression("10/2-20")
        assert(result=="-15.0")
    }
}