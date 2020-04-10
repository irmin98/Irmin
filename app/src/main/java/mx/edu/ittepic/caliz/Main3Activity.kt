package mx.edu.ittepic.caliz

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.android.synthetic.main.activity_main3.elegirImagen
import kotlinx.android.synthetic.main.activity_main3.lista
import kotlinx.android.synthetic.main.activity_main3.regresar
import java.io.ByteArrayOutputStream
import java.sql.SQLException

class Main3Activity : AppCompatActivity() {
var idActividad=""
    var SQL=""
    val SELECT_PHOTO = 2222
    val nombreBaseDatos="B"
    var listID =ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        var extra = intent.extras
        idActividad = extra!!.getString("id").toString()
       // mensaje(idActividad)

        SQL ="SELECT * FROM EVIDENCIAS  WHERE IDACTIVIDAD = ${idActividad.toString()}"

        cargarLista(SQL)

        regresar.setOnClickListener {
            finish()
        }
    }



    fun cargarLista(SQL:String){
        var sql=SQL
        try {
            var baseDatos =BaseDatos(this,nombreBaseDatos,null,1)
            var select = baseDatos.readableDatabase
            //   var sql ="SELECT * FROM EVIDENCIAS WHERE ${} "


            var cursor =select.rawQuery(sql,null)
            if(cursor.count>0){
                var arreglo= ArrayList<String>()
                this.listID =ArrayList<String>()
                cursor.moveToFirst()
                var cantidad =cursor.count -1

                (0..cantidad).forEach {
                    var data =" EVIDENCIA ID: ${cursor.getString(0)} "

                    arreglo.add(data)
                    listID.add(cursor.getString(0))
                    cursor.moveToNext()
                }
                lista.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arreglo)
                lista.setOnItemClickListener { parent, view, position, id ->
                    AlertDialog.Builder(this).setTitle("ATENCION")
                        .setMessage("¿QUE DESEAS HACER CON ESTA EVIDENCIA?")
                        .setPositiveButton("VER IMAGEN DE EVIDENCIA "){d,i->
                            verImagen(listID[position])

                        }
                        .setNegativeButton("CANCELAR"){d,i->}
                        .show()
                }
            }

            if(cursor.count<0){
                mensaje("NO SE ENCONTRARON EVIDENCIAS")
            }

            select.close()
            baseDatos.close()

        }catch (error: SQLException){
            mensaje(error.message.toString())
        }

    }//CARGAR LISTA

fun verImagen(idEvidencia:String){

  try {
      SQL = "SELECT * FROM EVIDENCIAS WHERE IDEVIDENCIA = '${idEvidencia}' "
      var baseDatos = BaseDatos(this, nombreBaseDatos, null, 1)
      var select = baseDatos.writableDatabase
      var cursor = select.rawQuery(SQL, null)
        cursor.moveToFirst()
     //   mensaje("Datos encontrados"+cursor.count.toString())
     //   mensaje("ID ACTIVIDAD : "+cursor.getString(2).toString())

          var result: ByteArray? = null
          result=cursor.getBlob(cursor.getColumnIndex("FOTO"))

          var imagen=Utils.getImage(result)
          elegirImagen.setImageBitmap(imagen)




  }catch (error:SQLException){
        mensaje(error.message.toString())
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
