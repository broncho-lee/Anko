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

package top.broncho.anko.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.view.KeyEvent
import android.view.View
import top.broncho.anko.AnkoException

internal class AndroidAlertBuilder(override val ctx: Context) : AlertBuilder<AlertDialog> {
    private val builder = AlertDialog.Builder(ctx)

    override var title: CharSequence
        get() = noGetter()
        set(value) {
            builder.setTitle(value)
        }

    override var titleResource: Int
        get() = noGetter()
        set(value) {
            builder.setTitle(value)
        }

    override var message: CharSequence
        get() = noGetter()
        set(value) {
            builder.setMessage(value)
        }

    override var messageResource: Int
        get() = noGetter()
        set(value) {
            builder.setMessage(value)
        }

    override var icon: Drawable
        get() = noGetter()
        set(value) {
            builder.setIcon(value)
        }

    override var iconResource: Int
        get() = noGetter()
        set(value) {
            builder.setIcon(value)
        }

    override var customTitle: View
        get() = noGetter()
        set(value) {
            builder.setCustomTitle(value)
        }

    override var customView: View
        get() = noGetter()
        set(value) {
            builder.setView(value)
        }

    override var isCancelable: Boolean
        get() = noGetter()
        set(value) {
            builder.setCancelable(value)
        }

    override fun onCancelled(handler: (DialogInterface) -> Unit) {
        builder.setOnCancelListener(handler)
    }

    override fun onKeyPressed(handler: (dialog: DialogInterface, keyCode: Int, e: KeyEvent) -> Boolean) {
        builder.setOnKeyListener(handler)
    }

    override fun positiveButton(buttonText: String, onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setPositiveButton(buttonText) { dialog, _ -> onClicked(dialog) }
    }

    override fun positiveButton(
        buttonTextResource: Int,
        onClicked: (dialog: DialogInterface) -> Unit
    ) {
        builder.setPositiveButton(buttonTextResource) { dialog, _ -> onClicked(dialog) }
    }

    override fun negativeButton(buttonText: String, onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setNegativeButton(buttonText) { dialog, _ -> onClicked(dialog) }
    }

    override fun negativeButton(
        buttonTextResource: Int,
        onClicked: (dialog: DialogInterface) -> Unit
    ) {
        builder.setNegativeButton(buttonTextResource) { dialog, _ -> onClicked(dialog) }
    }

    override fun neutralPressed(buttonText: String, onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setNeutralButton(buttonText) { dialog, _ -> onClicked(dialog) }
    }

    override fun neutralPressed(
        buttonTextResource: Int,
        onClicked: (dialog: DialogInterface) -> Unit
    ) {
        builder.setNeutralButton(buttonTextResource) { dialog, _ -> onClicked(dialog) }
    }

    override fun items(
        items: List<CharSequence>,
        onItemSelected: (dialog: DialogInterface, index: Int) -> Unit
    ) {
        builder.setItems(Array(items.size) { i -> items[i].toString() }) { dialog, which ->
            onItemSelected(dialog, which)
        }
    }

    override fun <T> items(
        items: List<T>,
        onItemSelected: (dialog: DialogInterface, item: T, index: Int) -> Unit
    ) {
        builder.setItems(Array(items.size) { i -> items[i].toString() }) { dialog, which ->
            onItemSelected(dialog, items[which], which)
        }
    }

    override fun build(): AlertDialog = builder.create()

    override fun show(): AlertDialog = builder.show()

    companion object {
        private const val NO_GETTER: String = "Property does not have a getter"
        internal fun noGetter(): Nothing = throw AnkoException(NO_GETTER)
    }
}