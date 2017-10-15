package tech.jpco.nazztimesheets.mainscreen

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.toast

import tech.jpco.nazztimesheets.R
import tech.jpco.nazztimesheets.model.MyRepo
import tech.jpco.nazztimesheets.model.WorkSession

class MainActivity : AppCompatActivity(), /*EasyPermissions.PermissionCallbacks,*/ MainContract.View {
    private val mPresenter: MainContract.Presenter by lazy { MainPresenter(this, MyRepo(this)) }
    private val mMinionButton by lazy { arrayOf(minionIn, personalLabel) }
    private val mPersonalButton by lazy { arrayOf(personalIn, minionLabel) }
    private var mDefaultColors: ColorStateList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mPresenter
        minionIn.setOnClickListener { mPresenter.signIn(WorkSession.WorkType.MINION) }
        personalIn.setOnClickListener { mPresenter.signIn(WorkSession.WorkType.PERSONAL) }
        dualOut.setOnClickListener { mPresenter.signOut() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            toast("That button doesn't do anything yet!")
            true
        } else super.onOptionsItemSelected(item)

    }

    private fun TextView.setHighlighting(highlighted: Boolean){
        if (mDefaultColors == null) {
            mDefaultColors = textColors
        }
        if (highlighted)
            setTextColor(Color.BLUE)
        else setTextColor(mDefaultColors)
    }

    override fun setActive(minionIsActive: Boolean, timestamp: String) {
        mMinionButton.forEach { it.isEnabled = !minionIsActive }
        mPersonalButton.forEach { it.isEnabled = minionIsActive }
        personalStarted.visibility = if (minionIsActive) GONE else VISIBLE
        minionStarted.visibility = if (minionIsActive) VISIBLE else GONE
        (if (minionIsActive) minionStarted else personalStarted).text = timestamp
        minionLabel.setHighlighting(minionIsActive)
        personalLabel.setHighlighting(!minionIsActive)
        dualOut.isEnabled = true
    }

    override fun setInactive() {
        mMinionButton.forEach { it.isEnabled = true }
        mPersonalButton.forEach { it.isEnabled = true }
        dualOut.isEnabled = false
        minionStarted.visibility = GONE
        personalStarted.visibility = GONE
        minionLabel.setHighlighting(false)
        personalLabel.setHighlighting(false)
    }

    override fun setRvList(log: List<WorkSession>) {

    }

    override fun notifyRvUpdated() {

    }

    override fun setMinionAhead(hours: Float) {
        minionMargin.text = "Ahead by $hours hours"
        personalMargin.text = ""
    }

    override fun setPersonalAhead(hours: Float) {
        personalMargin.text = "Ahead by $hours hours"
        minionMargin.text = ""
    }

    override fun setHoursEven() {
        personalMargin.text = ""
        minionMargin.text = ""
    }
}
