package top.broncho.anko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import top.broncho.anko.dialogs.alert
import top.broncho.anko.dialogs.cancelButton
import top.broncho.anko.dialogs.okButton

class MainActivity : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        info { "onCreate" }
        toast("onCreate")
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