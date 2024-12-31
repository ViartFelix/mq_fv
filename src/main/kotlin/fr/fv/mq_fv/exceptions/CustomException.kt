package fr.fv.mq_fv.exceptions

/**
 * Exception class to signify the thrown exception is a custom exception
 */
open class CustomException (message: String? = null, cause: Throwable? = null)
    : Exception(message, cause)