@file:Suppress("SpellCheckingInspection")

package com.crispyxyz.jvavscript

import com.crispyxyz.jvavscript.Main.setFlag
import com.crispyxyz.jvavscript.Main.setUsing
import java.math.BigDecimal
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

object Tokens {
    var isSuccess = false

    //111 00000             111 00001              111 00010
    var `return`: String? = null
        private set
    private var matchedClass: Byte = 0
    private var matchedField: Byte = 0
    private var matchedMethod: Byte = 0

    //private static String matchedParameters;
    private val Lvalue = ArrayList<String>()
    private val Rvalue = ArrayList<String>()
    private val ERR_MSG = arrayOf("Failed to match ", "class.\n", "field.\n", "method.\n", "parameters.\n")
    private const val RETURN_VOID = ""

    //Format: STTIIIII S=Status T=Tag I=Id
    private const val SYSTEM = 0x20.toByte()
    private const val MATH = 0x21.toByte()

    //001 00000             001 00001
    private const val OUT = 0x40.toByte()
    private const val PI = 0x41.toByte()
    private const val E = 0x42.toByte()

    //010 00000             010 00001              010 00010
    private const val PRINTLN = 0x60.toByte()
    private const val EXIT = 0x61.toByte()
    private const val SIN = 0x62.toByte()
    private const val COS = 0x63.toByte()
    private const val TAN = 0x64.toByte()
    private const val COT = 0x65.toByte()
    private const val SEC = 0x66.toByte()
    private const val CSC = 0x67.toByte()

    //011 00000             011 00001              011 00010         011 00011         011 00100         011 00101         011 00110         011 00111
    private const val USING = 0x68.toByte()

    //011 01000
    private const val NONE = 0xE0.toByte()
    private const val NO_PARAM = 0xE1.toByte()
    private const val ERR = 0xE2.toByte()

    val matchInfo: String
        get() = "Matched class=0x" + Integer.toHexString(matchedClass.toInt()) +
                ";Matched field=0x" + Integer.toHexString(matchedField.toInt()) +
                ";Matched method=0x" + Integer.toHexString(matchedMethod.toInt())

    fun match(tokens: Array<String>, param: String, isNotMatchingParam: Boolean) {
        `return` = tokens[0]
        isSuccess = false
        val index1 = Lvalue.indexOf(tokens[0])
        if (index1 != -1) {
            `return` = Rvalue[index1]
            isSuccess = true
            return
        }
        if (tokens[0].contains(":")) {
            val expr = tokens[0].replace(" ".toRegex(), "")
            val values = expr.split(":".toRegex()).toTypedArray()
            if (values.size < 2) {
                jout("Invalid expressions.\n") //failed
                return
            }
            val index2 = Lvalue.indexOf(values[0])
            when (values[1]) {
                "set" -> {
                    if (index2 == -1) {
                        //add new variable
                        Lvalue.add(values[0])
                        Rvalue.add(param)
                    } else {
                        //reset variable
                        Rvalue[index2] = param
                    }
                    `return` = RETURN_VOID
                    isSuccess = true
                    return  //success
                }
                "add", "plus" -> {
                    if (index2 == -1) {
                        //two numbers add
                        try {
                            `return` = BigDecimal((values[0].toDouble() + param.toDouble()).toString()).toPlainString()
                            isSuccess = true
                        } catch (e: NumberFormatException) {
                            jout("""
    Error: variable not found or number format error: ${e.message}
    
    """.trimIndent())
                        }
                    } else {
                        try {
                            `return` = BigDecimal((Rvalue[index2].toDouble() + param.toDouble()).toString()).toPlainString()
                            isSuccess = true
                        } catch (e: NumberFormatException) {
                            jout("""
    Error: number format error: ${e.message}
    
    """.trimIndent())
                        }
                    }
                    return
                }
                "minus" -> {
                    if (index2 == -1) {
                        //two numbers add
                        try {
                            `return` = BigDecimal((values[0].toDouble() - param.toDouble()).toString()).toPlainString()
                            isSuccess = true
                        } catch (e: NumberFormatException) {
                            jout("""
    Error: variable not found or number format error: ${e.message}
    
    """.trimIndent())
                        }
                    } else {
                        try {
                            `return` = BigDecimal((Rvalue[index2].toDouble() - param.toDouble()).toString()).toPlainString()
                            isSuccess = true
                        } catch (e: NumberFormatException) {
                            jout("""
    Error: number format error: ${e.message}
    
    """.trimIndent())
                        }
                    }
                    return
                }
                "time", "times", "multiply" -> {
                    if (index2 == -1) {
                        //two numbers add
                        try {
                            `return` = BigDecimal((values[0].toDouble() * param.toDouble()).toString()).toPlainString()
                            isSuccess = true
                        } catch (e: NumberFormatException) {
                            jout("""
    Error: variable not found or number format error: ${e.message}
    
    """.trimIndent())
                        }
                    } else {
                        try {
                            `return` = BigDecimal((Rvalue[index2].toDouble() * param.toDouble()).toString()).toPlainString()
                            isSuccess = true
                        } catch (e: NumberFormatException) {
                            jout("""
    Error: number format error: ${e.message}
    
    """.trimIndent())
                        }
                    }
                    return
                }
                "divide", "divided" -> {
                    if (index2 == -1) {
                        //two numbers add
                        try {
                            `return` = BigDecimal((values[0].toDouble() / param.toDouble()).toString()).toPlainString()
                            isSuccess = true
                        } catch (e: NumberFormatException) {
                            jout("""
    Error: variable not found or number format error: ${e.message}
    
    """.trimIndent())
                        } catch (e: ArithmeticException) {
                            jout("Error: " + e.message)
                        }
                    } else {
                        try {
                            `return` = BigDecimal((Rvalue[index2].toDouble() / param.toDouble()).toString()).toPlainString()
                            isSuccess = true
                        } catch (e: NumberFormatException) {
                            jout("""
    Error: number format error: ${e.message}
    
    """.trimIndent())
                        } catch (e: ArithmeticException) {
                            jout("Error: " + e.message)
                        }
                    }
                    return
                }
            }
        }
        when (matchMethod(tokens[0])) {
            USING -> {
                setUsing(param)
                `return` = RETURN_VOID
                isSuccess = true
                return
            }
        }
        when (matchClass(tokens[0])) {
            NONE -> {
                if (isNotMatchingParam) jout(ERR_MSG[0] + ERR_MSG[1]) //failed: Class not found
                return
            }
            MATH -> {
                when (matchField(tokens[1])) {
                    NONE -> {
                    }
                    E -> {
                        `return` = Math.E.toString()
                        isSuccess = true
                        return
                    }
                    PI -> {
                        `return` = Math.PI.toString()
                        isSuccess = true
                        return
                    }
                }
                when (matchMethod(tokens[1])) {
                    NONE -> {
                    }
                    SIN -> {
                        try {
                            `return` = BigDecimal(sin(param.toDouble()).toString()).toPlainString()
                            isSuccess = true
                        } catch (e: NumberFormatException) {
                            jout("""
    Calculation error:${e.message}
    
    """.trimIndent())
                        }
                        return
                    }
                    COS -> {
                        try {
                            `return` = BigDecimal(cos(param.toDouble()).toString()).toPlainString()
                            isSuccess = true
                        } catch (e: NumberFormatException) {
                            jout("""
    Calculation error:${e.message}
    
    """.trimIndent())
                        }
                        return
                    }
                    TAN -> {
                        try {
                            `return` = BigDecimal(tan(param.toDouble()).toString()).toPlainString()
                            isSuccess = true
                        } catch (e: NumberFormatException) {
                            jout("""
    Calculation error:${e.message}
    
    """.trimIndent())
                        }
                        return
                    }
                    COT -> {
                        try {
                            `return` = BigDecimal((1 / tan(param.toDouble())).toString()).toPlainString()
                            isSuccess = true
                        } catch (e: NumberFormatException) {
                            jout("""
    Calculation error:${e.message}
    
    """.trimIndent())
                        }
                        return
                    }
                    SEC -> {
                        try {
                            `return` = BigDecimal((1 / cos(param.toDouble())).toString()).toPlainString()
                            isSuccess = true
                        } catch (e: NumberFormatException) {
                            jout("""
    Calculation error:${e.message}
    
    """.trimIndent())
                        }
                        return
                    }
                    CSC -> {
                        try {
                            `return` = BigDecimal((1 / sin(param.toDouble())).toString()).toPlainString()
                            isSuccess = true
                        } catch (e: NumberFormatException) {
                            jout("""
    Calculation error:${e.message}
    
    """.trimIndent())
                        }
                        return
                    }
                }
                return
            }
            SYSTEM -> {
                when (matchField(tokens[1])) {
                    NONE -> {
                    }
                    OUT -> when (matchMethod(tokens[2])) {
                        ERR, NO_PARAM -> {
                            if (isNotMatchingParam) jout(ERR_MSG[0] + ERR_MSG[4]) //failed: Param not found
                            return
                        }
                        NONE -> {
                            if (isNotMatchingParam) jout(ERR_MSG[0] + ERR_MSG[3]) //failed: Method not found
                            return
                        }
                        PRINTLN -> {
                            val index = Lvalue.indexOf(param)
                            if (index == -1) println(param) //success: param
                            else println(Rvalue[index]) //success: variable
                            `return` = RETURN_VOID
                            isSuccess = true
                            return
                        }
                    }
                }
                when (matchMethod(tokens[1])) {
                    ERR, NO_PARAM -> {
                        if (isNotMatchingParam) jout(ERR_MSG[0] + ERR_MSG[4]) //failed: Param not found
                        return
                    }
                    NONE -> {
                        if (isNotMatchingParam) jout(ERR_MSG[0] + ERR_MSG[3]) //failed: Method not found
                        return
                    }
                    EXIT -> {
                        setFlag(false)
                        `return` = RETURN_VOID
                        isSuccess = true
                        return  //success
                    }
                }
            }
        }
        `return` = RETURN_VOID
        jout("""
    ${ERR_MSG[0]}.
    
    """.trimIndent()) //failed: unknown error
    }

    private fun matchClass(arg: String): Byte {
        return when (arg) {
            "System" -> SYSTEM.also { matchedClass = it }
            "Math" -> MATH.also { matchedClass = it }
            else -> NONE.also { matchedClass = it }
        }
    }

    private fun matchField(arg: String): Byte {
        return when (arg) {
            "out" -> OUT.also { matchedField = it }
            "PI" -> PI.also { matchedField = it }
            "E" -> E.also { matchedField = it }
            else -> NONE.also { matchedField = it }
        }
    }

    private fun matchMethod(arg: String): Byte {
        return when (arg) {
            "using" -> USING.also { matchedMethod = it }
            "println" -> PRINTLN.also { matchedMethod = it }
            "exit" -> EXIT.also { matchedMethod = it }
            "sin" -> SIN.also { matchedMethod = it }
            "cos" -> COS.also { matchedMethod = it }
            "tan" -> TAN.also { matchedMethod = it }
            "cot" -> COT.also { matchedMethod = it }
            "sec" -> SEC.also { matchedMethod = it }
            "csc" -> CSC.also { matchedMethod = it }
            else -> NONE.also { matchedMethod = it }
        }
    }

    private fun jout(arg: Any?) {
        print(arg)
    }
}