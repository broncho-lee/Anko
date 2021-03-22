package top.broncho.anko.permission

import kotlinx.coroutines.suspendCancellableCoroutine
import top.broncho.anko.permission.RequestPool.add
import top.broncho.anko.permission.RequestPool.remove

fun hasPermission(permission: String): Boolean {
    return checkPermission(permission)
}

fun hasPermissions(vararg permission: String): Boolean {
    return checkPermissions(permission)
}

suspend fun request(vararg permission: String) = suspendCancellableCoroutine<Result> {
    checkMainThread()
    val request = Request(permission)
    add(request, it)
    it.invokeOnCancellation { remove(request) }
    exec(request)
}