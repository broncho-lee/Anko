package top.broncho.anko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import top.broncho.anko.dialogs.toast

class MainActivity : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        info { "onCreate" }
        toast("onCreate")
    }
}