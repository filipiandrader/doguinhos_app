package com.doguinhos_app.main.principal.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.doguinhos_app.R
import com.doguinhos_app.entity.Doguinho
import com.doguinhos_app.entity.DoguinhoSingleton
import com.doguinhos_app.favorite.ui.FavoritesActivity
import com.doguinhos_app.main.details.ui.DetailsActivity
import com.doguinhos_app.main.principal.domain.MainInteractorImpl
import com.doguinhos_app.main.principal.presentation.MainPresenter
import com.doguinhos_app.main.principal.presentation.MainPresenterImpl
import com.doguinhos_app.main.principal.presentation.MainView
import com.doguinhos_app.util.capitalize
import com.doguinhos_app.util.setRefresh
import com.doguinhos_app.util.setVisible
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), MainView {

    override val mContext: Context
        get() = this

    private lateinit var mPresenter: MainPresenter

    private lateinit var mDrawer: Drawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)

        mPresenter = MainPresenterImpl(MainInteractorImpl())
        mPresenter.attach(this)

        mainSwipeRefreshLayout.setOnRefreshListener {
            showProgress()
            hideEmptyMessage()
            mPresenter.getDoguinhos()
        }

        val accountHeader = AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(resources.getDrawable(R.drawable.bg_dog))
            .build()

        mDrawer = DrawerBuilder()
            .withActivity(this)
            .withToolbar(mainToolbar)
            .withAccountHeader(accountHeader)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(1).withName("Início").withIcon(
                    resources.getDrawable(
                        R.drawable.ic_drawer_home
                    )
                ),
                PrimaryDrawerItem().withIdentifier(2).withName("Raças Favoritas").withIcon(
                    resources.getDrawable(
                        R.drawable.ic_drawer_favorite
                    )
                ),
                DividerDrawerItem()
            )
            .withOnDrawerItemClickListener { view, position, drawerItem ->
                when (position) {
                    1 -> mDrawer.closeDrawer()
                    2 -> {
                        mDrawer.closeDrawer()
                        startActivity<FavoritesActivity>()
                    }
                }
                true
            }
            .build()

    }

    override fun bindDoguinhos(doguinhos: MutableList<Doguinho>) {
        if (doguinhos.isEmpty()) {
            showEmptyMessage()
        } else {
            mainRecyclerView.layoutManager = GridLayoutManager(this, 2)
            mainRecyclerView.setup {
                withDataSource(dataSourceOf(doguinhos))
                withItem<Doguinho, MainViewHolder>(R.layout.item_doguinhos) {
                    onBind(::MainViewHolder) { _, item ->
                        this.doguinhosNomeTextView.text = capitalize(item.nome)
                        Picasso.get().load(item.imagem).into(this.doguinhosImageView)

                        when {
                            item.sub_raca.size == 1 -> this.doguinhosSubRacaTextView.text =
                                "Sub-raça: ${capitalize(item.sub_raca[0])}"
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
                            this.sub_raca = item.sub_raca
                        }
                        startActivity<DetailsActivity>()
                    }
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

    override fun onResume() {
        super.onResume()

        mDrawer.setSelection(1)
    }

    override fun onBackPressed() {
        if (mDrawer.isDrawerOpen) {
            mDrawer.closeDrawer()
        } else {
            finish()
        }
    }
}
