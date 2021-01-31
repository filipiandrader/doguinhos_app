package com.doguinhos_app.favorite.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.itemdefinition.onChildViewClick
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.doguinhos_app.R
import com.doguinhos_app.database.DogsDatabase
import com.doguinhos_app.entity.Doguinho
import com.doguinhos_app.entity.DoguinhoSingleton
import com.doguinhos_app.favorite.domain.FavoritesInteractorImpl
import com.doguinhos_app.favorite.presentation.FavoritesPresenter
import com.doguinhos_app.favorite.presentation.FavoritesPresenterImpl
import com.doguinhos_app.favorite.presentation.FavoritesView
import com.doguinhos_app.main.details.ui.DetailsActivity
import com.doguinhos_app.main.principal.ui.MainViewHolder
import com.doguinhos_app.util.capitalize
import com.doguinhos_app.util.setVisible
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
            favoritesRecyclerView.setup {
                withLayoutManager(LinearLayoutManager(mContext))
                withDataSource(dataSourceOf(doguinhos))
                withItem<Doguinho, FavoritesViewHolder>(R.layout.item_doguinhos_favoritos) {
                    onBind(::FavoritesViewHolder) { _, item ->
                        this.doguinhosNomeTextView.text = capitalize(item.nome)
                        when {
                            item.sub_raca.size == 1 -> this.doguinhosSubRacaTextView.text = "Sub-raça: ${capitalize(item.sub_raca[0])}"
                            item.sub_raca.size > 1 -> {
                                var sub_raca = capitalize(item.sub_raca[0])
                                for (i in 1 until item.sub_raca.size) {
                                    sub_raca = sub_raca.plus(", ${capitalize(item.sub_raca[i])}")
                                }
                                this.doguinhosSubRacaTextView.text = "Sub-raças: $sub_raca"
                            }
                            else -> this.doguinhosSubRacaTextView.text = "Sub-raças: não tem"
                        }
                    }
                    onClick {
                        DoguinhoSingleton.instance.run {
                            this.nome = item.nome
                            this.imagem = item.imagem
                            this.favotiro = item.favotiro
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
