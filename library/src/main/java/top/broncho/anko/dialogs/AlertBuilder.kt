/*
 * Copyright 2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("NOTHING_TO_INLINE", "unused")
package top.broncho.anko.dialogs

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.view.KeyEvent
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface AlertBuilder<out D : DialogInterface> {
    val ctx: Context

    var title: CharSequence

    var titleResource: Int

    var message: CharSequence

    var messageResource: Int

    var icon: Drawable

    @setparam:DrawableRes
    var iconResource: Int

    var customTitle: View

    var customView: View

    var isCancelable: Boolean

    fun onCancelled(handler: (dialog: DialogInterface) -> Unit)

    fun onKeyPressed(handler: (dialog: DialogInterface, keyCode: Int, e: KeyEvent) -> Boolean)

    fun positiveButton(buttonText: String, onClicked: (dialog: DialogInterface) -> Unit)
    fun positiveButton(@StringRes buttonTextResource: Int, onClicked: (dialog: DialogInterface) -> Unit)

    fun negativeButton(buttonText: String, onClicked: (dialog: DialogInterface) -> Unit)
    fun negativeButton(@StringRes buttonTextResource: Int, onClicked: (dialog: DialogInterface) -> Unit)

    fun neutralPressed(buttonText: String, onClicked: (dialog: DialogInterface) -> Unit)
    fun neutralPressed(@StringRes buttonTextResource: Int, onClicked: (dialog: DialogInterface) -> Unit)

    fun items(items: List<CharSequence>, onItemSelected: (dialog: DialogInterface, index: Int) -> Unit)
    fun <T> items(items: List<T>, onItemSelected: (dialog: DialogInterface, item: T, index: Int) -> Unit)

    fun build(): D
    fun show(): D
}

inline fun AlertBuilder<*>.okButton(noinline handler: (dialog: DialogInterface) -> Unit) =
    positiveButton(android.R.string.ok, handler)

inline fun AlertBuilder<*>.cancelButton(noinline handler: (dialog: DialogInterface) -> Unit) =
    negativeButton(android.R.string.cancel, handler)

inline fun AlertBuilder<*>.yesButton(noinline handler: (dialog: DialogInterface) -> Unit) =
    positiveButton(android.R.string.yes, handler)

inline fun AlertBuilder<*>.noButton(noinline handler: (dialog: DialogInterface) -> Unit) =
    negativeButton(android.R.string.no, handler)