package com.example.testing.apolloexpetiments

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.testing.apolloexpetiments.api.GitHubRepository
import com.example.testing.apolloexpetiments.api.onError
import com.example.testing.apolloexpetiments.api.onSuccess
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import kotlinx.coroutines.experimental.android.UI

import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: GitHubRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getAppComponent().inject(this)
        setContentView(R.layout.activity_main)
        button_start.setOnClickListener {
            if (search_edit.text.isNotEmpty()) {
                launch {
                    repository.searchRepositories(search_edit.text.toString()).onSuccess {
                        withContext(UI) {
                            textView.text = "Results found: $it"
                        }
                    }.onError {
                        withContext(UI) {
                            Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Search for something", Toast.LENGTH_SHORT).show()
            }

        }
    }
}