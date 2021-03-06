@file:Suppress("NOTHING_TO_INLINE", "unused")

package top.broncho.anko

import android.app.Activity
import android.app.Service
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.fragment.app.Fragment

inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStartActivity(this, T::class.java, params)

inline fun <reified T : Activity> Fragment.startActivity(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStartActivity(requireContext(), T::class.java, params)

inline fun <reified T : Activity> Fragment.startActivityForResult(
    requestCode: Int,
    vararg params: Pair<String, Any?>
) =
    startActivityForResult(AnkoInternals.createIntent(act, T::class.java, params), requestCode)

inline fun <reified T : Service> Context.startService(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStartService(this, T::class.java, params)

inline fun <reified T : Service> Fragment.startService(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStartService(requireContext(), T::class.java, params)

inline fun <reified T : Service> Context.stopService(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStopService(this, T::class.java, params)

inline fun <reified T : Service> Fragment.stopService(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStopService(requireContext(), T::class.java, params)

inline fun <reified T : Any> Context.intentFor(vararg params: Pair<String, Any?>): Intent =
    AnkoInternals.createIntent(this, T::class.java, params)

inline fun <reified T : Any> Fragment.intentFor(vararg params: Pair<String, Any?>): Intent =
    AnkoInternals.createIntent(requireContext(), T::class.java, params)

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TOP] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
@Deprecated(
    message = "Deprecated in Android",
    replaceWith = ReplaceWith("top.broncho.anko.newDocument")
)
inline fun Intent.clearWhenTaskReset(): Intent =
    apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NEW_DOCUMENT] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.newDocument(): Intent = apply {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
    } else {
        @Suppress("DEPRECATION")
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
    }
}

/**
 * Add the [Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.excludeFromRecents(): Intent =
    apply { addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) }

/**
 * Add the [Intent.FLAG_ACTIVITY_MULTIPLE_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.multipleTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NEW_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.newTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_ANIMATION] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.noAnimation(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_HISTORY] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.noHistory(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) }

/**
 * Add the [Intent.FLAG_ACTIVITY_SINGLE_TOP] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.singleTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) }

inline fun Fragment.browse(url: String, newTask: Boolean = false) =
    requireContext().browse(url, newTask)

fun Context.browse(url: String, newTask: Boolean = false): Boolean = try {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    if (newTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
    true
} catch (e: ActivityNotFoundException) {
    e.printStackTrace()
    false
}

inline fun Fragment.share(text: String, subject: String = "") =
    requireContext().share(text, subject)

fun Context.share(text: String, subject: String = ""): Boolean = try {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, text)
    startActivity(Intent.createChooser(intent, null))
    true
} catch (e: ActivityNotFoundException) {
    e.printStackTrace()
    false
}

inline fun Fragment.email(email: String, subject: String = "", text: String = "") =
    requireContext().email(email, subject, text)

fun Context.email(email: String, subject: String = "", text: String = ""): Boolean {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
    if (subject.isNotEmpty())
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    if (text.isNotEmpty())
        intent.putExtra(Intent.EXTRA_TEXT, text)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
        return true
    }
    return false

}

inline fun Fragment.makeCall(number: String): Boolean = requireContext().makeCall(number)

fun Context.makeCall(number: String): Boolean = try {
    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
    startActivity(intent)
    true
} catch (e: Exception) {
    e.printStackTrace()
    false
}

inline fun Fragment.sendSMS(number: String, text: String = ""): Boolean =
    requireContext().sendSMS(number, text)

fun Context.sendSMS(number: String, text: String = ""): Boolean = try {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$number"))
    intent.putExtra("sms_body", text)
    startActivity(intent)
    true
} catch (e: Exception) {
    e.printStackTrace()
    false
}
