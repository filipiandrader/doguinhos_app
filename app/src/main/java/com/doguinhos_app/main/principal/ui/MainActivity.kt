package com.doguinhos_app.main.principal.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.itemdefinition.onChildViewClick
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.doguinhos_app.R
import com.doguinhos_app.database.DogsDatabase
import com.doguinhos_app.entity.Doguinho
import com.doguinhos_app.entity.DoguinhoSingleton
import com.doguinhos_app.favorite.ui.FavoritesActivity
import com.doguinhos_app.main.details.ui.DetailsActivity
import com.doguinhos_app.main.principal.domain.MainInteractorImpl
import com.doguinhos_app.main.principal.presentation.MainPresenter
import com.doguinhos_app.main.principal.presentation.MainPresenterImpl
import com.doguinhos_app.main.principal.presentation.MainView
import com.doguinhos_app.util.capitalize
import com.doguinhos_app.util.setVisible
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

        val accountHeader = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(resources.getDrawable(R.drawable.bg_dog))
                .build()

        mDrawer = DrawerBuilder()
                .withActivity(this)
                .withToolbar(mainToolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        PrimaryDrawerItem().withIdentifier(1).withName("Início").withIcon(resources.getDrawable(R.drawable.ic_drawer_home)),
                        PrimaryDrawerItem().withIdentifier(2).withName("Raças Favoritas").withIcon(resources.getDrawable(R.drawable.ic_drawer_favorite)),
                        DividerDrawerItem(),
                        PrimaryDrawerItem().withIdentifier(4).withName("Meu GitHub").withIcon(resources.getDrawable(R.drawable.ic_drawer_github)),
                        PrimaryDrawerItem().withIdentifier(5).withName("Meu LinkedIn").withIcon(resources.getDrawable(R.drawable.ic_drawer_linkedin))
                )
                .withOnDrawerItemClickListener { view, position, drawerItem ->
                    when (position) {
                        1 -> mDrawer.closeDrawer()
                        2 -> {
                            mDrawer.closeDrawer()
                            startActivity<FavoritesActivity>()
                        }
                        4 -> {
                            mDrawer.closeDrawer()
                            val customIntent = CustomTabsIntent.Builder().build()
                            customIntent.launchUrl(mContext, Uri.parse("https://github.com/filipiandrader/doguinhos_app/"))
                        }
                        5 -> {
                            mDrawer.closeDrawer()
                            val customIntent = CustomTabsIntent.Builder().build()
                            customIntent.launchUrl(mContext, Uri.parse("https://www.linkedin.com/in/filipi-andrade-rocha-6a5081188/"))
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
            mainRecyclerView.setup {
                withLayoutManager(GridLayoutManager(mContext, 2))
                withDataSource(dataSourceOf(doguinhos))
                withItem<Doguinho, MainViewHolder>(R.layout.item_doguinhos) {
                    onBind(::MainViewHolder) { _, item ->
                        this.doguinhosNomeTextView.text = capitalize(item.nome)
                        Picasso.get().load(item.imagem).into(this.doguinhosImageView)

                        GlobalScope.launch {
                            if (DogsDatabase.getInstance(mContext).dogsDao().getDoguinho(item.nome) != null) {
                                if (DogsDatabase.getInstance(mContext).dogsDao().getDoguinho(item.nome)!!.favotiro) {
                                    item.favotiro = true
                                    GlobalScope.launch(Dispatchers.Main) { favoritarDoguinhoImageView.setImageResource(R.drawable.ic_favorite) }
                                } else {
                                    item.favotiro = false
                                    GlobalScope.launch(Dispatchers.Main) { favoritarDoguinhoImageView.setImageResource(R.drawable.ic_not_favorite) }
                                }
                            } else {
                                item.favotiro = false
                                GlobalScope.launch(Dispatchers.Main) { favoritarDoguinhoImageView.setImageResource(R.drawable.ic_not_favorite) }
                            }
                        }

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
                    onChildViewClick(MainViewHolder::favoritarDoguinhoImageView) { index, view ->
                        if (doguinhos[index].favotiro) {
                            doguinhos[index].favotiro = false
                            view.setImageResource(R.drawable.ic_not_favorite)
                            GlobalScope.launch { DogsDatabase.getInstance(mContext).dogsDao().delete(doguinhos[index]) }
                        } else {
                            doguinhos[index].favotiro = true
                            view.setImageResource(R.drawable.ic_favorite)
                            GlobalScope.launch { DogsDatabase.getInstance(mContext).dogsDao().insert(doguinhos[index]) }
                        }
                    }
                }
            }

            mainRecyclerView.setVisible(true)
        }

        hideProgress()
    }

    override fun showProgress() {
        mainProgressBar.setVisible(true)
        mainRecyclerView.setVisible(false)
        mainEmptyMessageTextView.setVisible(false)
    }

    override fun hideProgress() {
        mainProgressBar.setVisible(false)
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

        mPresenter.attach(this)
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
