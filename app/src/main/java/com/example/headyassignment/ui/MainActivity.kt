package com.example.headyassignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.headyassignment.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]

        viewModel.showBack.observe(this, Observer {

            if (it != null) {
                updateAppBar(it)
                viewModel.showBack.value = null
            }
        })

        updateAppBar(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateAppBar(showBack: Boolean) {
        supportActionBar?.apply {
            title = viewModel.title
            setDefaultDisplayHomeAsUpEnabled(showBack)
            setDisplayHomeAsUpEnabled(showBack)
            setHomeButtonEnabled(showBack)
        }
    }

}
