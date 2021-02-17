@file:Suppress("unused")

package com.crispyxyz.jvavscript

class ArgumentsException : RuntimeException {
    constructor()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}