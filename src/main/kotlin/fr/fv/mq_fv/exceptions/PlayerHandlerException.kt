package fr.fv.mq_fv.exceptions

/**
 * Exception thrown for any related database error
 */
class PlayerHandlerException (message: String? = null, cause: Throwable? = null)
    : CustomException(message, cause)