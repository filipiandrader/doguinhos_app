package com.doguinhos_app.main.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.doguinhos_app.R
import com.doguinhos_app.entity.Doguinho
import com.doguinhos_app.main.domain.MainInteractorImpl
import com.doguinhos_app.main.presentation.MainPresenter
import com.doguinhos_app.main.presentation.MainPresenterImpl
import com.doguinhos_app.main.presentation.MainView
import com.doguinhos_app.util.capitalize
import com.doguinhos_app.util.setRefresh
import com.doguinhos_app.util.setVisible
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {

    override val mContext: Context
        get() = this

    private lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPresenter = MainPresenterImpl(MainInteractorImpl())
        mPresenter.attach(this)

        mainSwipeRefreshLayout.setOnRefreshListener {
            showProgress()
            hideEmptyMessage()
            mPresenter.getDoguinhos()
        }
    }

    override fun bindDoguinhos(doguinhos: MutableList<Doguinho>) {
        if (doguinhos.isEmpty()) {
            showEmptyMessage()
        } else {
            mainRecyclerView.setup {
                withDataSource(dataSourceOf(doguinhos))
                withItem<Doguinho, MainViewHolder>(R.layout.item_doguinhos) {
                    onBind(::MainViewHolder) { _, item ->
                        this.doguinhosNomeTextView.text = capitalize(item.nome)

                        if (item.sub_raca.size == 1) {
                            this.doguinhosSubRacaTextView.text = "Sub-raça: ${capitalize(item.sub_raca[0])}"
                        } else if (item.sub_raca.size > 1) {
                            var sub_raca = capitalize(item.sub_raca[0])
                            for (i in 1 until item.sub_raca.size) {
                                sub_raca = sub_raca.plus(", ${capitalize(item.sub_raca[i])}")
                            }
                            this.doguinhosSubRacaTextView.text = "Sub-raças: $sub_raca"
                        } else {
                            this.doguinhosSubRacaTextView.text = "Sub-raças: não tem"
                        }
                    }
                    onClick { showMessage("clicked on ${item.nome}") }
                }
            }

            mainRecyclerView.setVisible(true)
        }

        hideProgress()
    }

    override fun showProgress() {
        mainSwipeRefreshLayout.setRefresh(true)
        mainRecyclerView.setVisible(false)
        mainEmptyMessageTextView.setVisible(false)
    }

    override fun hideProgress() {
        mainSwipeRefreshLayout.setRefresh(false)
    }

    override fun showEmptyMessage() {
        mainRecyclerView.setVisible(false)
        mainEmptyMessageTextView.setVisible(true)
    }

    override fun hideEmptyMessage() {
        mainEmptyMessageTextView.setVisible(false)
    }
}
