package com.example.sample_profile

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val profileFragment: Fragment by lazy { ProfileContentFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Log.d("Activity Log","Called onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Activity Log","Called onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Activity Log","Called onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Activity Log","Called onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Activity Log","Called onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Activity Log","Called onDestroy")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d("Activity Log","Called onDetachedFromWindow")
    }

    fun addProfile(view: View) {
        val transaction = supportFragmentManager.beginTransaction()

        transaction.add(R.id.main_container, profileFragment)
        transaction.commit()
    }

    fun removeProfile(view: View) {
        val transaction = supportFragmentManager.beginTransaction()

        transaction.remove(profileFragment)
        transaction.commit()
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
