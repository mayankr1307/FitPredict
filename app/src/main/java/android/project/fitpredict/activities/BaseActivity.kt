package android.project.fitpredict.activities

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.project.fitpredict.R

open class BaseActivity : AppCompatActivity() {

    private var progress_dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun displayProgressBar(context: Context) {
        if (progress_dialog == null) {
            progress_dialog = Dialog(context)
            progress_dialog?.setContentView(R.layout.progress_bar)
            progress_dialog?.setCancelable(false)
            progress_dialog?.show()
        }
    }

    fun hideProgressBar() {
        progress_dialog?.let {
            if (it.isShowing) {
                it.dismiss()
                progress_dialog = null
            }
        }
    }
}
