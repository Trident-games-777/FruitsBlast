package com.starmakerinteractive.thevoic.fruit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.starmakerinteractive.thevoic.R
import com.starmakerinteractive.thevoic.di.UrlPrefKey
import com.starmakerinteractive.thevoic.utils.Settings
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FruitActivity : AppCompatActivity() {
    @UrlPrefKey
    @Inject
    lateinit var urlKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fruit)
        val fruitViewModel = ViewModelProvider(this)[FruitViewModel::class.java]

        if (Settings.checkConditions(applicationContext)) {
            fruitViewModel.url.observe(this) {
                val intent = Intent(this, FruitWeb::class.java)
                intent.putExtra(urlKey, it)
                startActivity(intent)
                finish()
            }
        } else {
            startActivity(Intent(this, FruitBlast::class.java))
            finish()
        }
    }
}