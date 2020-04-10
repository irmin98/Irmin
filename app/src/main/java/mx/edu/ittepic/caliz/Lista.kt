package mx.edu.ittepic.caliz

import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class AdapterLista (private val context: Activity, private val title: Array<String>, private val description: Array<String>, private val imgid: Array<Bitmap>)
    : ArrayAdapter<String>(context, R.layout.lista_imagen, title) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.lista_imagen, null, true)

        val imageView = rowView.findViewById(R.id.icon) as ImageView
        val titleText = rowView.findViewById(R.id.title) as TextView
        val subtitleText = rowView.findViewById(R.id.description) as TextView

        titleText.text = "ID de evidencia: "+title[position]
        subtitleText.text = "ID de actividad: "+description[position]
        imageView.setImageBitmap(imgid[position])

        return rowView
    }

}

