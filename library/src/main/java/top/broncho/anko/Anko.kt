package top.broncho.anko

import android.app.Activity
import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import java.lang.ref.WeakReference

class Anko : ContentProvider() {
    companion object {
        /**
         * Get Context at anywhere
         */
        lateinit var ctx: Context

        /**
         * Get Application at anywhere
         */
        lateinit var app: Application

        private val activityTracker = ActivityTracker()

        /**
         * Get current Activity at anywhere, Maybe "null" if there no activity.
         */
        fun currentActivity() = activityTracker.tryGetCurrentActivity()
    }

    override fun onCreate(): Boolean {
        ctx = context!!
        app = ctx.applicationContext as Application
        activityTracker.beginTracking(app)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0
}

class ActivityTracker {
    private val activities = mutableListOf<WeakReference<Activity>>()

    private val lifecycleCallbacks = object : ActivityLifecycleCallbacksAdapter() {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            add(activity)
        }

        override fun onActivityDestroyed(activity: Activity) {
            remove(activity)
        }
    }

    fun beginTracking(application: Application) {
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    fun endTracking(application: Application) {
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    fun tryGetCurrentActivity(): Activity? {
        activities.reversed().forEach {
            val activity = it.get()
            if (activity != null) {
                return activity
            }
        }
        return null
    }

    private fun add(activity: Activity) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            activities.add(WeakReference(activity))
        } else {
            throw IllegalStateException("Must in Main Thread!")
        }
    }

    private fun remove(activity: Activity) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            removeFromWeakList(activities, activity)
        } else {
            throw IllegalStateException("Must in Main Thread!")
        }
    }

    private fun <T> removeFromWeakList(
        list: MutableList<WeakReference<T>>, needle: T
    ) {
        val find = list.find { it.get() === needle }
        list.remove(find)
    }
}

abstract class ActivityLifecycleCallbacksAdapter : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}