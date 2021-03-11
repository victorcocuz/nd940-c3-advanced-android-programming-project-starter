package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        detail_name.text = intent.getStringExtra(Constants.TITLE)
        if (intent.getBooleanExtra(Constants.STATUS, false)) {
            detail_status.setTextAppearance(android.R.style.TextAppearance)
            detail_status.text = getString(R.string.detail_success)
        } else {
            detail_status.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
            detail_status.text = getString(R.string.detail_fail)
        }

        detail_button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
