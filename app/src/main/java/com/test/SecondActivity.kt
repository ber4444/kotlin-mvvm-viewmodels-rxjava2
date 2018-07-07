package com.test

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lib.model.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_common.*

class SecondActivity : AppCompatActivity() {

    private val adapter = SecondAdapter()
    private lateinit var viewModel: SecondViewModel
    private val disposable = CompositeDisposable()
    private var id = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        id = intent.getLongExtra("conversationId", -1L)

        viewModel = ViewModelProviders.of(this).get(SecondViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        updateList()
    }

    private fun updateList() {
        if (id == -1L) return
        disposable.add(viewModel.getEvents(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({onNext(it)}, {e->onError(e)}))
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    private fun onNext(t: Event) {
        if (t.type == null) { // not archived
            adapter.add(t)
            if (!recyclerView.canScrollVertically(1))
                recyclerView.smoothScrollToPosition(adapter.itemCount)
        }
    }

    private fun onError(e: Throwable) {
        Log.e("SecondActivity", e.message)
        updateList()
    }
}
