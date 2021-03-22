package top.broncho.anko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import top.broncho.anko.dialogs.alert
import top.broncho.anko.dialogs.cancelButton
import top.broncho.anko.dialogs.okButton
import top.broncho.anko.permission.request

class MainActivity : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        debug { "onCreate" }
        toast("onCreate")

        lifecycleScope.launch {
            val result = request(android.Manifest.permission.CALL_PHONE)
            toast("request call permission: $result")
        }
    }

    override fun onResume() {
        super.onResume()
        alert {
            title = "test"
            message = "description"
            okButton {
                find<TextView>(R.id.tv_info).snackbar("ok")
                makeCall("18824322016")
            }
            cancelButton {
                find<TextView>(R.id.tv_info).longSnackbar("cancel")
            }
        }.show()
    }
}