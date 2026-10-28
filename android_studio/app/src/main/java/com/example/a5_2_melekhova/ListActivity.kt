package com.example.a5_2_melekhova

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter
    private lateinit var dao: MovieDao
    private lateinit var tvCount: TextView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        dao = AppDatabase.get(this).movieDao()
        tvCount = findViewById(R.id.tvCount)
        tvEmpty = findViewById(R.id.tvEmpty)

        adapter = MovieAdapter(
            dao.getAll(),
            onEdit = { movie -> showEditDialog(movie) },
            onDelete = { movie ->
                dao.delete(movie)
                refresh()
            }
        )

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        refresh()
    }

    private fun refresh() {
        val list = dao.getAll()
        adapter.setData(list)
        tvCount.text = list.size.toString()
        tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showEditDialog(movie: Movie) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null)
        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etYear = view.findViewById<EditText>(R.id.etYear)
        val etGenre = view.findViewById<EditText>(R.id.etGenre)
        val etRating = view.findViewById<EditText>(R.id.etRating)

        etTitle.setText(movie.title)
        etYear.setText(movie.year)
        etGenre.setText(movie.genre)
        etRating.setText(movie.rating)

        AlertDialog.Builder(this)
            .setTitle("Редактирование")
            .setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                movie.title = etTitle.text.toString()
                movie.year = etYear.text.toString()
                movie.genre = etGenre.text.toString()
                movie.rating = etRating.text.toString()
                dao.update(movie)
                refresh()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}