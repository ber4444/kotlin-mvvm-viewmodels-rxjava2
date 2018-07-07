package com.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lib.model.Conversation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_common.*


class MainActivity : AppCompatActivity() {

    private val adapter = MainAdapter()
    private lateinit var viewModel: MainViewModel
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        adapter.listener = object : RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(view.context, SecondActivity::class.java)
                intent.putExtra("conversationId", adapter.getId(position-1))
                startActivity(intent)
            }
        }

        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    updateDataList()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        updateDataList()
    }

    private fun updateDataList() {
        disposable.add(viewModel.getConversations(adapter.itemCount.toLong())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({onNext(it)}, {e->onError(e)}))
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    private fun onNext(list: List<Conversation>) {
        adapter.addAll(list)
    }

    private fun onError(e: Throwable) {
        Log.e("MainActivity", e.message)
    }
}

