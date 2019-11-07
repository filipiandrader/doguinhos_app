package com.doguinhos_app.main.details.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.doguinhos_app.R
import com.doguinhos_app.entity.DoguinhoSingleton
import com.doguinhos_app.main.details.domain.DetailsInteractorImpl
import com.doguinhos_app.main.details.presentation.DetailsPresenter
import com.doguinhos_app.main.details.presentation.DetailsPresenterImpl
import com.doguinhos_app.main.details.presentation.DetailsView
import com.doguinhos_app.util.capitalize
import com.doguinhos_app.util.setVisible
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity(), DetailsView {

    override val mContext: Context
        get() = this

    private lateinit var mPresenter: DetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        detailsToolbar.title = capitalize(DoguinhoSingleton.instance.nome)
        setSupportActionBar(detailsToolbar)

        if (DoguinhoSingleton.instance.nome.isEmpty()) {
            finish()
        }

        mPresenter = DetailsPresenterImpl(DetailsInteractorImpl())
        mPresenter.attach(this)
        mPresenter.getDoguinhosImagens(DoguinhoSingleton.instance.nome)

        detailsToolbar.setNavigationOnClickListener { finish() }
    }

    override fun bindDoguinhosImages(doguinhosImagens: MutableList<String>) {
        if (doguinhosImagens.isEmpty()) {
            detailsEmptyMessageTextView.setVisible(true)
        } else {
            detailsRecyclerView.layoutManager = GridLayoutManager(this, 2)
            detailsRecyclerView.setup {
                withDataSource(dataSourceOf(doguinhosImagens))
                withItem<String, DetailsViewHolder>(R.layout.item_doguinhos_imagens) {
                    onBind(::DetailsViewHolder) { index, _ ->
                        Picasso.get().load(doguinhosImagens[index]).into(this.doguinhosImageView)
                    }
                }
            }

            detailsRecyclerView.setVisible(true)
        }

        hideProgress()
    }

    override fun showProgress() {
        detailsEmptyMessageTextView.setVisible(false)
        detailsProgressBar.setVisible(true)
        detailsRecyclerView.setVisible(false)
    }

    override fun hideProgress() {
        detailsProgressBar.setVisible(false)
    }
}
