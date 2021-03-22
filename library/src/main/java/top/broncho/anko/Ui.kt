@file:Suppress("unused", "NOTHING_TO_INLINE")

package top.broncho.anko

import android.view.View

/** makes visible a view. */
fun View.visible() {
    visibility = View.VISIBLE
}

/** makes gone a view. */
fun View.gone() {
    visibility = View.GONE
}

/** makes invisible a view. */
fun View.invisible() {
    visibility = View.INVISIBLE
}