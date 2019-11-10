package com.doguinhos_app.main.details.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.doguinhos_app.R
import com.doguinhos_app.database.DogsDatabase
import com.doguinhos_app.entity.Doguinho
import com.doguinhos_app.entity.DoguinhoSingleton
import com.doguinhos_app.main.details.domain.DetailsInteractorImpl
import com.doguinhos_app.main.details.presentation.DetailsPresenter
import com.doguinhos_app.main.details.presentation.DetailsPresenterImpl
import com.doguinhos_app.main.details.presentation.DetailsView
import com.doguinhos_app.util.capitalize
import com.doguinhos_app.util.setVisible
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onScrollChange
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

class DetailsActivity : AppCompatActivity(), DetailsView {

    override val mContext: Context
        get() = this

    private lateinit var mPresenter: DetailsPresenter

    companion object {
        var hasChange = false
    }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)

        if (DoguinhoSingleton.instance.favotiro) {
            menu!!.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite)
        } else {
            menu!!.findItem(R.id.action_favorite).setIcon(R.drawable.ic_not_favorite)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_favorite -> {
                hasChange = true
                if (DoguinhoSingleton.instance.favotiro) {
                    DoguinhoSingleton.instance.favotiro = false
                    item.setIcon(R.drawable.ic_not_favorite)
                    GlobalScope.launch { DogsDatabase.getInstance(mContext).dogsDao().delete(DoguinhoSingleton.instance) }
                } else {
                    DoguinhoSingleton.instance.favotiro = true
                    item.setIcon(R.drawable.ic_favorite)
                    GlobalScope.launch { DogsDatabase.getInstance(mContext).dogsDao().insert(DoguinhoSingleton.instance) }
                }
            }
            R.id.action_share -> {
                StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())
                Picasso.get().load(DoguinhoSingleton.instance.imagem).into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Uma imagem da raça ${capitalize(DoguinhoSingleton.instance.nome)} <3")
                        shareIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmpaUri(bitmap!!))
                        shareIntent.type = ("image/*")
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivity(Intent.createChooser(shareIntent, "Enviar esse doguinho pra quem?"))
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                })
            }
        }
        return true
    }

    private fun getLocalBitmpaUri(bitmap: Bitmap): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_${System.currentTimeMillis()}.png")
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = Uri.fromFile(file)
        } catch (io: IOException) {
            io.printStackTrace()
        }

        return bmpUri
    }

    override fun bindDoguinhosImages(doguinhosImagens: MutableList<String>) {
        if (doguinhosImagens.isEmpty()) {
            detailsEmptyMessageTextView.setVisible(true)
        } else {
            detailsRecyclerView.setup {
                withLayoutManager(GridLayoutManager(mContext, 2))
                withDataSource(dataSourceOf(doguinhosImagens))
                withItem<String, DetailsViewHolder>(R.layout.item_doguinhos_imagens) {
                    onBind(::DetailsViewHolder) { index, _ ->
                        Picasso.get().load(doguinhosImagens[index]).noFade().into(this.doguinhosImageView)
                    }
                    onClick { index ->
                        StfalconImageViewer.Builder<String>(mContext, doguinhosImagens) { view, image ->
                            Picasso.get().load(image).noFade().into(view)
                        }.withStartPosition(index).show()
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
