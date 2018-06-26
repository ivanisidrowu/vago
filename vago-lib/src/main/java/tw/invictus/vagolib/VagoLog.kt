package tw.invictus.vagolib

/**
 * Created by ivan on 2018/6/25.
 */
object VagoLog {
    const val LOG_TAG = "LOG"
    const val ERROR_TAG = "ERROR"
    const val WARNING_TAG = "WARNING"

    fun l(message: String) {
        System.out.println("[$LOG_TAG] $message")
    }

    fun w(message: String) {
        System.out.println("[$WARNING_TAG] $message")
    }

    fun e(message: String) {
        System.out.println("[$ERROR_TAG] $message")
    }
}