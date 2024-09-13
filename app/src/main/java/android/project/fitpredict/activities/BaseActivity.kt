package android.project.fitpredict.activities

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.project.fitpredict.R

open class BaseActivity : AppCompatActivity() {

    var progress_dialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun displayProgressBar(context: Context) {
        progress_dialog = Dialog(context)
        progress_dialog!!.setContentView(R.layout.progress_bar)
        progress_dialog!!.setCancelable(false)
        progress_dialog!!.show()
    }

    fun hideProgressBar(context: Context) {
        progress_dialog!!.dismiss()
        progress_dialog = null
    }
}