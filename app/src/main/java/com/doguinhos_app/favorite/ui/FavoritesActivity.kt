package com.doguinhos_app.favorite.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.doguinhos_app.R
import com.doguinhos_app.entity.Doguinho
import com.doguinhos_app.entity.DoguinhoSingleton
import com.doguinhos_app.favorite.domain.FavoritesInteractorImpl
import com.doguinhos_app.favorite.presentation.FavoritesPresenter
import com.doguinhos_app.favorite.presentation.FavoritesPresenterImpl
import com.doguinhos_app.favorite.presentation.FavoritesView
import com.doguinhos_app.main.details.ui.DetailsActivity
import com.doguinhos_app.util.capitalize
import com.doguinhos_app.util.setVisible
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_favorites.*
import org.jetbrains.anko.startActivity

class FavoritesActivity : AppCompatActivity(), FavoritesView {

    override val mContext: Context
        get() = this

    private lateinit var mPresenter: FavoritesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        setSupportActionBar(favoritesToolbar)
        favoritesToolbar.setNavigationOnClickListener { finish() }

        mPresenter = FavoritesPresenterImpl(FavoritesInteractorImpl(), mContext)
        mPresenter.attach(this)
    }

    override fun onResume() {
        super.onResume()

        mPresenter.getRacasFavoritas(mContext)
    }

    override fun bindRacasFavoritas(doguinhos: MutableList<Doguinho>) {
        if (doguinhos.isEmpty()) {
            favoritesEmptyMessageTextView.setVisible(true)
            favoritesRecyclerView.setVisible(false)
        } else {
            favoritesRecyclerView.layoutManager = GridLayoutManager(mContext, 2)
            favoritesRecyclerView.setup {
                withDataSource(dataSourceOf(doguinhos))
                withItem<Doguinho, FavoritesViewHolder>(R.layout.item_doguinhos_favoritos) {
                    onBind(::FavoritesViewHolder) { index, item ->
                        this.doguinhosNomeTextView.text = capitalize(item.nome)
                        Picasso.get().load(item.imagem).into(this.doguinhosImageView)
                    }
                    onClick {
                        DoguinhoSingleton.instance.run {
                            this.nome = item.nome
                            this.imagem = item.imagem
                            this.sub_raca = item.sub_raca
                        }
                        startActivity<DetailsActivity>()
                    }
                }
            }

            favoritesRecyclerView.setVisible(true)
        }

        hideProgress()
    }

    override fun showProgress() {
        favoritesProgressBar.setVisible(true)
        favoritesEmptyMessageTextView.setVisible(false)
        favoritesRecyclerView.setVisible(false)
    }

    override fun hideProgress() {
        favoritesProgressBar.setVisible(false)
    }
}
