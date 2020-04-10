package mx.edu.ittepic.caliz

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.SQLException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    val nombreBaseDatos="B"
    var listID =ArrayList<String>()
/////////////////////////////////
//calendario
var c = Calendar.getInstance()
    var year = c.get(Calendar.YEAR)
    var month = c.get(Calendar.MONTH)
    var day = c.get(Calendar.DAY_OF_MONTH)
    var fecha = "${year}/${month + 1}/${day}"

    ///////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setear la fecha actual a fecha captura
        FechaCaptrura.setText(fecha)


        insertar.setOnClickListener {
            insertarPersona()
        }
        buscar.setOnClickListener {
            consultarPorID()
        }
        BTNFechaEntrega.setOnClickListener{
            capturaFecha()
        }

        cargarLista("SELECT * FROM ACTIVIDAD ")
    }//onCreate

    fun cargarLista(SQL:String){
        var sql=SQL
        try {
            var baseDatos =BaseDatos(this,nombreBaseDatos,null,1)
            var select = baseDatos.readableDatabase
         //   var sql ="SELECT * FROM ACTIVIDAD "


            var cursor =select.rawQuery(sql,null)
            if(cursor.count>0){
                var arreglo= ArrayList<String>()
                this.listID =ArrayList<String>()
                cursor.moveToFirst()
                var cantidad =cursor.count -1

                (0..cantidad).forEach {
                    var data ="ACTIVIDAD ID: ${cursor.getString(0)}  \nDESCRIPCION: ${cursor.getString(1)}  \nFECHA DE CAPTURA :${cursor.getString(2)}  \n" +
                            "FECHA DE ENTREGA : ${cursor.getString(3)}"
                    arreglo.add(data)
                    listID.add(cursor.getString(0))
                    cursor.moveToNext()
                }
                lista.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arreglo)
                lista.setOnItemClickListener { parent, view, position, id ->
                    AlertDialog.Builder(this).setTitle("ATENCION")
                        .setMessage("¿QUE DESAS HACER CON ESTA ACTIVIDAD ?")
                        .setPositiveButton("ELIMINAR"){d,i->
                            eliminarPorID(listID[position])

                        }
                        .setNeutralButton("AGREGAR EVIDENCIA"){d,i->
                            VerEvidencia(listID[position])
                        }
                        .setNegativeButton("CANCELAR"){d,i->}
                        .show()
                }
            }

            select.close()
            baseDatos.close()

        }catch (error:SQLException){
            mensaje(error.message.toString())
        }

    }//CARGAR LISTA

    fun VerEvidencia(id:String){
        var otroActivity = Intent(this,Main2Activity::class.java)
        otroActivity.putExtra("id",id)
        startActivity(otroActivity)
    }

    fun eliminarPorID(id:String){
        try {
            var baseDatos =BaseDatos(this,nombreBaseDatos,null,1)

            var eliminar =baseDatos.writableDatabase
            var SQL ="DELETE FROM ACTIVIDAD WHERE IDACTIVIDAD =?"
            var parametros= arrayOf(id)

            eliminar.execSQL(SQL,parametros)
            mensaje("SE ELIMINO CORRECTAMENTE")
            eliminar.close()
            baseDatos.close()

            cargarLista("SELECT * FROM ACTIVIDAD ")

        }catch (error:SQLException){
            mensaje(error.message.toString())
        }

    }//eliminarPorID

    fun consultarPorID(){
        var campoIDBuscar = EditText(this)
        campoIDBuscar.setHint("Numero entero")
        campoIDBuscar.inputType = InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this).setMessage("ESCRIBA ID A BUSCAR ").setTitle("ATENCION")
            .setPositiveButton("BUSCAR"){d,i->
                if(campoIDBuscar.text.toString().isEmpty()){
                    mensaje("ERROR NO ESCRIBISTE UN ID A BUSCAR ")
                    return@setPositiveButton
                }
                buscarPorID(campoIDBuscar.text.toString())
            }
            .setNeutralButton("CANCELAR"){d,i->}
            .setView(campoIDBuscar)
            .show()
    }

    fun buscarPorID(id:String){

        var SQL ="SELECT * FROM ACTIVIDAD WHERE IDACTIVIDAD =${id} "
        cargarLista(SQL)
    }

    fun capturaFecha() :String{
        var fecha=""

        //CALENDAR
        var c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        //PICKER
        var datePicker = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, myear, mmonth, mdayOfMonth ->
                //setToTextView
                 fecha = "${myear}/${mmonth + 1}/${mdayOfMonth}"
                FechaEntrega.setText(fecha)
            },
            year,
            month,
            day
        )
        //showDialog
        datePicker.show()


        return fecha
    }


    fun insertarPersona(){


        try {
            var baseDatos =BaseDatos(this,nombreBaseDatos,null,1)

            var insertar =baseDatos.writableDatabase
             //var SQL ="INSERT INTO PERSONA VALUES(NULL, '${editText.text.toString()}','${editText2.text.toString()}')"
            //  var SQL ="INSERT INTO PERSONA VALUES(NULL,'${editText.text.toString()}','${editText2.text.toString()}')"
            var SQL ="INSERT INTO ACTIVIDAD   VALUES(NULL,'${Descripcion.text.toString()}','${FechaCaptrura.text.toString()}','${FechaEntrega.text.toString()}')"

            insertar.execSQL(SQL)
            insertar.close()
            baseDatos.close()
            mensaje("SE INSERTO CORRECTAMENTE")
            cargarLista("SELECT * FROM ACTIVIDAD ")
            Descripcion.setText("")
            FechaCaptrura.setText("")
            FechaEntrega.setText("")


        }catch (error:SQLException){
            mensaje(error.message.toString())
        }

    }//funcion
    fun mensaje(mensaje :String){
        AlertDialog.Builder(this).setMessage(mensaje).show()
    }


}