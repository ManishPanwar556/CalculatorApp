package com.example.calculator.utils

import com.example.calculator.models.OperatorModel
import com.example.calculator.models.ResultModel
import java.util.*

object CalculatorUtils {
    private val secondStack = Stack<OperatorModel>()
    private val maxOperatorStack = Stack<OperatorModel>()
    private val numberList = arrayListOf<Double>()
    private val resultMap = HashMap<Int, ResultModel>()
    fun calculateExpression(exp: String): String {
        val operatorOrderList = arrayListOf<OperatorModel>()
        var num = ""
        for (element in exp) {
            val ch = element
            if (ch == '+' || ch == '/' || ch == '*' || ch == '-') {
                if(num.isEmpty()){
                    num="0"
                }
                addNumber(num)
                num = ""
                checkPriority(ch + "")
            } else {
                num += ch
            }
        }
        if(num.isNotEmpty()){

            addNumber(num)
        }
        while (secondStack.isNotEmpty()) {
            maxOperatorStack.push(secondStack.pop())
        }
        while (maxOperatorStack.isNotEmpty()) {
            operatorOrderList.add(maxOperatorStack.pop())
        }
        var result: Double? = null
        var index = operatorOrderList.size - 1
        while (index >= 0) {
            val model = operatorOrderList.get(index)
            val operator = model.operator
            val numberPos = model.index
            if (operator == "+") {
                if (resultMap.containsKey(numberPos) && resultMap.containsKey(numberPos + 1)) {
                    val result1 = resultMap[numberPos]
                    val result2 = resultMap[numberPos + 1]
                    result = result1!!.value + result2!!.value
                    result1.value = result
                    result2.value = result
                } else if (resultMap.containsKey(numberPos)) {
                    val result1 = resultMap[numberPos]
                    result = result1!!.value + numberList[numberPos + 1]
                    result1.value = result
                    resultMap.put(numberPos + 1, result1)

                } else if (resultMap.containsKey(numberPos + 1)) {
                    val result2 = resultMap[numberPos + 1]
                    result = numberList[numberPos] + result2!!.value
                    result2.value = result
                    resultMap.put(numberPos, result2)
                } else {
                    result = numberList[numberPos] + numberList[numberPos + 1]
                    val resultModel = ResultModel(result)
                    resultMap.put(numberPos, resultModel)
                    resultMap.put(numberPos + 1, resultModel)
                }
            } else if (operator == "-") {
                if (resultMap.containsKey(numberPos) && resultMap.containsKey(numberPos + 1)) {
                    val result1 = resultMap[numberPos]
                    val result2 = resultMap[numberPos + 1]
                    result = result1!!.value - result2!!.value
                    result1.value = result
                    result2.value = result
                } else if (resultMap.containsKey(numberPos)) {
                    val result1 = resultMap[numberPos]
                    result = result1!!.value - numberList[numberPos + 1]
                    result1.value = result
                    resultMap.put(numberPos + 1, result1)

                } else if (resultMap.containsKey(numberPos + 1)) {
                    val result2 = resultMap[numberPos + 1]
                    result = numberList[numberPos] - result2!!.value
                    result2.value = result
                    resultMap.put(numberPos, result2)
                } else {
                    result = numberList[numberPos] - numberList[numberPos + 1]
                    val resultModel = ResultModel(result)
                    resultMap.put(numberPos, resultModel)
                    resultMap.put(numberPos + 1, resultModel)
                }
            } else if (operator == "*") {
                if (resultMap.containsKey(numberPos) && resultMap.containsKey(numberPos + 1)) {
                    val result1 = resultMap[numberPos]
                    val result2 = resultMap[numberPos + 1]
                    result = result1!!.value * result2!!.value
                    result1.value = result
                    result2.value = result
                } else if (resultMap.containsKey(numberPos)) {
                    val result1 = resultMap[numberPos]
                    result = result1!!.value * numberList[numberPos + 1]
                    result1.value = result
                    resultMap.put(numberPos + 1, result1)

                } else if (resultMap.containsKey(numberPos + 1)) {
                    val result2 = resultMap[numberPos + 1]
                    result = numberList[numberPos] * result2!!.value
                    result2.value = result
                    resultMap.put(numberPos, result2)
                } else {
                    result = numberList[numberPos] * numberList[numberPos + 1]
                    val resultModel = ResultModel(result)
                    resultMap.put(numberPos, resultModel)
                    resultMap.put(numberPos + 1, resultModel)
                }
            } else {
                if (resultMap.containsKey(numberPos) && resultMap.containsKey(numberPos + 1)) {
                    val result1 = resultMap[numberPos]
                    val result2 = resultMap[numberPos + 1]
                    result = result1!!.value / result2!!.value
                    result1.value = result
                    result2.value = result
                } else if (resultMap.containsKey(numberPos)) {
                    val result1 = resultMap[numberPos]
                    result = result1!!.value / numberList[numberPos + 1]
                    result1.value = result
                    resultMap.put(numberPos + 1, result1)

                } else if (resultMap.containsKey(numberPos + 1)) {
                    val result2 = resultMap[numberPos + 1]
                    result = numberList[numberPos] / result2!!.value
                    result2.value = result
                    resultMap.put(numberPos, result2)
                } else {
                    result = numberList[numberPos] / numberList[numberPos + 1]
                    val resultModel = ResultModel(result)
                    resultMap.put(numberPos, resultModel)
                    resultMap.put(numberPos + 1, resultModel)
                }

            }
            index--
        }
        clearAll()
        return result?.toString() ?: ""
    }

    fun addNumber(num: String) {
        numberList.add(num.toDouble())
    }

    fun clearAll() {
        numberList.clear()
        secondStack.clear()
        maxOperatorStack.clear()
        resultMap.clear()
    }

    fun checkPriority(operator: String) {
        val pos = numberList.size - 1
        if (operator == "*") {
            while (true) {
                if (maxOperatorStack.isNotEmpty()) {
                    val peekEle = maxOperatorStack.peek()
                    if (peekEle.operator == "+" || peekEle.operator == "-" || peekEle.operator == "/") {
                        maxOperatorStack.pop()
                        secondStack.push(peekEle)
                    } else {
                        maxOperatorStack.push(OperatorModel(operator, pos))
                        break
                    }
                } else {
                    maxOperatorStack.push(OperatorModel(operator, pos))
                    return
                }
            }
            while (secondStack.isNotEmpty()) {
                val popEle = secondStack.pop()
                maxOperatorStack.push(popEle)
            }
        } else if (operator == "+") {
            while (true) {
                if (maxOperatorStack.isNotEmpty()) {
                    val peekEle = maxOperatorStack.peek()
                    if (peekEle.operator == "/" || peekEle.operator == "-") {
                        maxOperatorStack.pop()
                        secondStack.push(peekEle)
                    } else {
                        maxOperatorStack.push(OperatorModel(operator, pos))
                        break
                    }
                } else {

                    maxOperatorStack.push(OperatorModel(operator, pos))
                    return
                }
            }
            while (secondStack.isNotEmpty()) {
                val popEle = secondStack.pop()
                maxOperatorStack.push(popEle)
            }
        } else if (operator == "/") {
            while (true) {
                if (maxOperatorStack.isNotEmpty()) {
                    val peekEle = maxOperatorStack.peek()
                    if (peekEle.operator == "-") {
                        maxOperatorStack.pop()
                        secondStack.push(peekEle)
                    } else {
                        maxOperatorStack.push(OperatorModel(operator, pos))
                        break
                    }
                } else {

                    maxOperatorStack.push(OperatorModel(operator, pos))
                    return
                }
            }
            while (secondStack.isNotEmpty()) {
                val popEle = secondStack.pop()
                maxOperatorStack.push(popEle)
            }
        } else {
            maxOperatorStack.push(OperatorModel(operator, pos))
        }
    }


}