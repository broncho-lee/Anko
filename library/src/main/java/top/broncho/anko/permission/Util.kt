package top.broncho.anko.permission

import android.os.Looper
import androidx.annotation.RestrictTo
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.FragmentActivity
import top.broncho.anko.Anko
import top.broncho.anko.Anko.Companion.currentActivity
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

internal fun checkPermission(permission: String): Boolean {
    return checkSelfPermission(Anko.ctx, permission) == PERMISSION_GRANTED
}

internal fun checkPermissions(permissions: Array<out String>): Boolean {
    var result = true
    permissions.forEach {
        if (!checkPermission(it)) {
            result = false
        }
    }
    return result
}

internal fun resume(request: Request, result: Result) {
    resume(request.requestCode, result)
}

internal fun resume(requestCode: Int, result: Result) {
    val continuation = RequestPool.get(requestCode) ?: return
    if (continuation.isActive) {
        continuation.resumeWith(success(result))
    }
}

internal fun resumeFailed(request: Request, failedMsg: String) {
    resumeFailed(request.requestCode, failedMsg)
}

internal fun resumeFailed(requestCode: Int, failedMsg: String) {
    val continuation = RequestPool.get(requestCode) ?: return
    if (continuation.isActive) {
        continuation.resumeWith(failure(RuntimeException(failedMsg)))
    }
}

internal fun exec(request: Request) {
    when (val currentActivity = currentActivity()) {
        null -> resumeFailed(request, "Activity not found!")
        !is FragmentActivity -> resumeFailed(request, "Activity should inherit FragmentActivity!")
        else -> {
            val fm = currentActivity.supportFragmentManager
            if (fm.isDestroyed) {
                resumeFailed(request, "Activity already finished!")
            } else {
                VirtualFragment.open(fm, request)
            }
        }
    }
}

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun checkMainThread() = check(Looper.myLooper() == Looper.getMainLooper()) {
    "Expected to be called on the main thread but was " + Thread.currentThread().name
}
