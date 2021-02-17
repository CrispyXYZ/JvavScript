package com.crispyxyz.jvavscript

import java.io.*
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


object Main {
    private val sc = Scanner(System.`in`)
    private var flag = true
    private var shouldPrintErr = false
    private var using = ""
    private val VERSION = intArrayOf(0, 3, 0)
    private const val SUFFIX = ""
    private const val COMPLETED = 60
    private var debug = true// = false

    @JvmStatic
	fun setFlag(flag: Boolean) {
        Main.flag = flag
    }

    @JvmStatic
	fun setUsing(us: String) {
        using = us
    }

    @JvmStatic
    fun main(args: Array<String>) {
        when (args.size) {
            0 -> interactive()
            1 -> {
                if (args[0] == "--debug" || args[0] == "-d") {
                    debug = true
                    interactive()
                } else if (args[0] == "--help" || args[0] == "-h") {
                    joutln("usage: java -jar JvavScript.jar [--debug|-d] [scriptFile]")
                    joutln("   or  dalvikvm -cp JvavScript.dex com.crispyxyz.jvavscript.Main [--debug|-d] [scriptFile]")
                    joutln()
                    joutln("Options: ")
                    joutln("    -d, --debug:   Enable debug mode.")
                }
                script(args[0])
            }
            2 -> if (args[0] == "--debug" || args[0] == "-d") {
                debug = true
                script(args[1])
            }
            else -> {
                joutf("The length of arguments must be %d to %d.%n", 0, 2)
                throw ArgumentsException("Too many arguments.")
            }
        }
    }

    private fun interactive() {
        d("Debug is enabled.")
        joutf("JvavScript %d.%d.%d%s (%d%% completed)%n", VERSION[0], VERSION[1], VERSION[2], SUFFIX, COMPLETED)
        while (flag) {
            jout("> ")
            val input = sc.nextLine() //get input
            try {
                splitAndMatch(input)
            } catch (e: ArrayIndexOutOfBoundsException) {
                joutln("Input error. Method name required.")
                continue
            }
        }
        sc.close()
        joutln("exit")
        exitProcess(0)
    }

    private fun script(fileName: String) {
        val file = File(fileName)
        try {
            val lines = readFile(file)
            d("Lines=$lines")
            for (line in lines) {
                splitAndMatch(line)
            }
        } catch (e: IOException) {
            joutln("Error: " + e.message)
        } catch (e: ArrayIndexOutOfBoundsException) {
            joutln("Inout error. Method name required.")
        }
    }

    @Throws(IOException::class)
    private fun readFile(fin: File): ArrayList<String?> {
        val fis = FileInputStream(fin)
        val lines = ArrayList<String?>()
        //Construct BufferedReader from InputStreamReader
        val br = BufferedReader(InputStreamReader(fis))
        var line: String?
        while (br.readLine().also { line = it } != null) {
            lines.add(line)
        }
        br.close()
        fis.close()
        return lines
    }

    private fun splitAndMatch(inline: String?) {
        var line = inline
        when (val index = line!!.indexOf('#')) {
            -1 -> {
            }
            else -> line = StringBuilder()
                    .append(line)
                    .delete(index, line.length)
                    .toString()
        }
        val commands = line.split(";".toRegex()).toTypedArray() //split command
        for (each in commands) {
            var eachCmd: String = each
            if (eachCmd.isNotEmpty()) { //ignore empty command (comment)
                if (eachCmd.contains("=")) eachCmd = eachCmd.replace(" ".toRegex(), "").replace("=", ":set(") + ")"
                if (eachCmd.contains("+")) eachCmd = eachCmd.replace(" ".toRegex(), "").replace("+", ":plus(") + ")"
                if (eachCmd.contains("-")) eachCmd = eachCmd.replace(" ".toRegex(), "").replace("-", ":minus(") + ")"
                if (eachCmd.contains("*")) eachCmd = eachCmd.replace(" ".toRegex(), "").replace("*", ":multiply(") + ")"
                if (eachCmd.contains("/")) eachCmd = eachCmd.replace(" ".toRegex(), "").replace("/", ":divided(") + ")"
                splitParam(eachCmd)
            }
        }
        return
    }

    private fun d(msg: String) {
        if (debug) println("[DEBUG: $msg]")
    }

    private fun splitParam(inp: String) { //in = eachCmd
        var `in` = inp
        Tokens.isSuccess = false
        val s = `in`
        if (using.isNotEmpty()) {
            `in` = "$using.$`in`"
        }
        d("Using=$using")
        shouldPrintErr = false
        for (i in 0..1) {
            if (!Tokens.isSuccess) {
                var matchedParameters: String? = null
                try {
                    matchedParameters = `in`.substring(`in`.indexOf('(') + 1, `in`.lastIndexOf(')'))
                } catch (e: StringIndexOutOfBoundsException) {
                    d("Warning: Param not found!")
                }
                d("Origin param=$matchedParameters")
                var fullMethodName: String?
                var param = Tokens.`return`
                if (matchedParameters != null && matchedParameters.isNotEmpty()) {
                    if (matchedParameters.contains("(")) {
                        splitParam(matchedParameters)
                        matchedParameters = Tokens.`return`
                    }
                    param = if (isNumeric(matchedParameters) || `in`.contains("using(")) {
                        matchedParameters
                    } else {
                        val parameterTokens = matchedParameters!!.split("\\.".toRegex()).toTypedArray()
                        d("Parameter tokens=" + parameterTokens.contentToString())
                        if (param != null) {
                            Tokens.match(parameterTokens, param, false)
                        }
                        d(Tokens.matchInfo)
                        Tokens.`return`
                    }
                    fullMethodName = StringBuilder()
                            .append(`in`)
                            .delete(`in`.indexOf('('), `in`.lastIndexOf(')') + 1)
                            .toString()
                } else if (matchedParameters != null) { //param is empty
                    param = matchedParameters
                    fullMethodName = StringBuilder()
                            .append(`in`)
                            .delete(`in`.indexOf('('), `in`.lastIndexOf(')') + 1)
                            .toString()
                } else {
                    param = matchedParameters
                    fullMethodName = `in`
                }
                d("Converted param=$param")
                val tokens = fullMethodName.split("\\.".toRegex()).toTypedArray() //split token
                d("Tokens=" + tokens.contentToString())
                if (param != null) {
                    Tokens.match(tokens, param, shouldPrintErr)
                }
                d(Tokens.matchInfo)
                d("Return=" + Tokens.`return`)
                `in` = s
                shouldPrintErr = true
            }
        }
    }

    private fun isNumeric(str: String?): Boolean {
        val pattern = Pattern.compile("^-?[0-9]*$")
        return if (str!!.indexOf(".") > 0) {
            if (str.indexOf(".") == str.lastIndexOf(".") && str.split("\\.".toRegex()).toTypedArray().size == 2) {
                pattern.matcher(str.replace(".", "")).matches()
            } else {
                false
            }
        } else {
            pattern.matcher(str).matches()
        }
    }

    //redirect
    private fun joutln(x: Any?) {
        println(x)
    }

    private fun joutln() {
        println()
    }

    @Suppress("SameParameterValue")
    private fun jout(obj: Any?) {
        print(obj)
    }
    
    private fun joutf(format: String, vararg args: Any?) {
        System.out.printf(format, *args)
    }
}