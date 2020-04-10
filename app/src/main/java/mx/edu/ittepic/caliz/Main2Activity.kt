package mx.edu.ittepic.caliz

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.ByteArrayOutputStream

class Main2Activity : AppCompatActivity() {
    var idActividad = ""
    val nombreBaseDatos = "B"

    val SELECT_PHOTO = 2222

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        var extra = intent.extras
        idActividad = extra!!.getString("id").toString()
     //   mensaje(idActividad)

        elegir.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            startActivityForResult(photoPicker, SELECT_PHOTO)
        }

        insertarE.setOnClickListener {
            val bitmap = (elegirImagen.drawable as BitmapDrawable).bitmap
            val alertDialog = AlertDialog.Builder(this)
           // alertDialog.setTitle("Ingrese ID de actividad")
          //  val editText = EditText(this)
          //  alertDialog.setView((editText))

            alertDialog.setPositiveButton("LISTO") { dialog, which ->
               // var evidencia = Evidencias(editText.text.toString(), Utils.getBytes(bitmap))
                var evidencia = Evidencias(idActividad.toString(), Utils.getBytes(bitmap))
                evidencia.asignarPuntero(this)
                var resultado = evidencia.insertarImagen()
           //     mensaje("error"+evidencia.error)
                if (resultado == true) {
                    mensaje("SE GUARDÓ LA EVIDENCIA")
                } else {
                    when (evidencia.error) {
                        1 -> {
                            mensaje("Error en tabla, no se creó o no se conectó a la base de datos")
                        }
                        2 -> {
                            mensaje("Error no se pudo insertar en la tabla")
                        }
                    }
                }
            }//setPositive
            alertDialog.show()


        }

        regresar.setOnClickListener {
            finish()
        }

        VerEvi.setOnClickListener {
            var otroActivity = Intent(this,Main3Activity::class.java)
            otroActivity.putExtra("id",idActividad)
            startActivity(otroActivity)


        }

    }


    fun mensaje(mensaje: String) {
        AlertDialog.Builder(this).setMessage(mensaje).show()
    }

    object Utils {
        fun getBytes(bitmap: Bitmap): ByteArray {
            var stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            return stream.toByteArray()
        }//getBytes

        fun getImage(image: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(image, 0, image.size)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK && data !== null) {
            val pickedImage = data.data
            elegirImagen.setImageURI(pickedImage)
        }
    }




}