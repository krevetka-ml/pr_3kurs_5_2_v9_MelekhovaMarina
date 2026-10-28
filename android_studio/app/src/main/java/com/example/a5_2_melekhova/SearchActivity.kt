package com.example.a5_2_melekhova

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    private val apiKey = "443cb01c"   // <-- ключ с omdbapi.com
    private var currentMovie: OmdbMovie? = null

    private lateinit var tvResult: TextView
    private lateinit var tvRating: TextView
    private lateinit var ivPoster: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val tvHello = findViewById<TextView>(R.id.tvHello)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnList = findViewById<Button>(R.id.btnList)
        tvResult = findViewById(R.id.tvResult)
        tvRating = findViewById(R.id.tvRating)
        ivPoster = findViewById(R.id.ivPoster)

        tvHello.text = "Привет, " + prefs.getString("login", "") + "!"

        val savedJson = prefs.getString("last_movie", null)
        if (savedJson != null) {
            currentMovie = Gson().fromJson(savedJson, OmdbMovie::class.java)
            showMovie(currentMovie!!)
        }

        btnSearch.setOnClickListener {
            val title = etTitle.text.toString().trim()
            if (title.isEmpty()) {
                Toast.makeText(this, "Введите название", Toast.LENGTH_SHORT).show()
            } else {
                loadMovie(title)
            }
        }

        btnAdd.setOnClickListener {
            val m = currentMovie
            if (m == null) {
                Toast.makeText(this, "Сначала найдите фильм", Toast.LENGTH_SHORT).show()
            } else {
                AppDatabase.get(this).movieDao().insert(
                    Movie(
                        title = m.Title ?: "",
                        year = m.Year ?: "",
                        genre = m.Genre ?: "",
                        rating = m.imdbRating ?: "",
                        poster = m.Poster ?: ""
                    )
                )
                Toast.makeText(this, "Добавлено в коллекцию", Toast.LENGTH_SHORT).show()
            }
        }

        btnList.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }
    }

    private fun loadMovie(title: String) {
        val url = "https://www.omdbapi.com/?t=" + Uri.encode(title) + "&apikey=" + apiKey
        val request = StringRequest(Request.Method.GET, url,
            { response ->
                val movie = Gson().fromJson(response, OmdbMovie::class.java)
                if (movie.Response == "True") {
                    currentMovie = movie
                    showMovie(movie)
                    getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                        .edit()
                        .putString("last_movie", Gson().toJson(movie))
                        .apply()
                } else {
                    Toast.makeText(this, movie.Error ?: "Фильм не найден", Toast.LENGTH_SHORT).show()
                }
            },
            {
                Toast.makeText(this, "Ошибка сети", Toast.LENGTH_SHORT).show()
            })
        Volley.newRequestQueue(this).add(request)
    }

    private fun showMovie(m: OmdbMovie) {
        tvRating.text = "★ ${m.imdbRating}"
        tvResult.text = "${m.Title} (${m.Year})\n" +
                "Жанр: ${m.Genre}\n" +
                "Режиссёр: ${m.Director}\n\n" +
                "${m.Plot}"
        loadPoster(this, m.Poster, ivPoster)
    }
}