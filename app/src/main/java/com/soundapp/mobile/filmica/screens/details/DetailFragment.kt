package com.soundapp.mobile.filmica.screens.details

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import com.google.android.material.snackbar.Snackbar
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.FilmsRepo
import com.soundapp.mobile.filmica.repository.domain.film.Film
import com.soundapp.mobile.filmica.screens.utils.TargetFinishedListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*

const val FILM_EXTRA = "film_extra"
const val LOCAL_FILM_EXTRA = "film_extra_local"

class DetailFragment: Fragment() {
    companion object {

        fun create(film: Film, isHighlighted: Boolean) : Fragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putSerializable(FILM_EXTRA, film)
            args.putBoolean(LOCAL_FILM_EXTRA, isHighlighted)
            fragment.arguments = args
            return fragment
        }

    }

    var film: Film? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            shareFilm()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareFilm() {
        film?.let {film ->
            val intent = Intent(Intent.ACTION_SEND)
            val template = getString(R.string.share_template, film.title, film.score)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, template)
            startActivity(Intent.createChooser(intent, getString(R.string.share_title)))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        film = arguments?.getSerializable(FILM_EXTRA) as? Film
        val isHighlighted = arguments?.getBoolean(LOCAL_FILM_EXTRA)
        if (film == null || isHighlighted == null)
            throw IllegalArgumentException("The arguments provided does not fit requirements of ${DetailFragment::class.java.canonicalName}")
        if (isHighlighted) buttonAdd.hide() else buttonAdd.show()
        buttonAdd.setOnClickListener {
            film?.let { addFilmToFavorites(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        film?.let {film ->
            textTitle.text = film.title
            textRating.text = "${film.score}"
            labelDescription.text = film.overview
            labelGenres.text = film.genre
            labelDate.text = film.released
            loadImage(film)
        }
    }

    private fun addFilmToFavorites(film: Film) {
        FilmsRepo.saveFilm(context!!, film) {
            Snackbar.make(detailFragment, R.string.add_snackbar, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo){
                        FilmsRepo.removeFilm(this@DetailFragment.context!!, film)
                    }.show()
        }
    }

    private fun loadImage(value: Film) {
        // Avoid to lose the reference to target
        val target = TargetFinishedListener { bitmap ->
            setColorFrom(bitmap)
        }

        topShadow.tag = target

        Picasso.with(context)
                .load(value.coverURL())
                .error(R.drawable.placeholder)
                .into(target)
    }

    private fun setColorFrom(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            // This code cannot be executed if it is not on an activity
            val defaultColor = ContextCompat.getColor(context!!, R.color.colorPrimary)
            val swatch = palette?.vibrantSwatch ?: palette?.dominantSwatch
            val color = swatch?.rgb ?: defaultColor
            topShadow.setImageBitmap(bitmap)
            val overlayColor = Color.argb(
                    (Color.alpha(color)*0.5).toInt(),
                    Color.red(color),
                    Color.green(color),
                    Color.blue(color))
            overlay.setBackgroundColor(overlayColor)
            buttonAdd.backgroundTintList = ColorStateList.valueOf(color)
        }
    }
}