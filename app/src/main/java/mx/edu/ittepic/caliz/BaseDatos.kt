package mx.edu.ittepic.caliz


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(contex: Context,
                nombreBaseDatos:String,
                cursorFactory: SQLiteDatabase.CursorFactory?,
                numeroVersion:Int) :SQLiteOpenHelper(contex,nombreBaseDatos,cursorFactory,numeroVersion){

    var error = -1
    var idEvi=0
    var idAct=0
    var img:ByteArray?=null



    override fun onCreate(db: SQLiteDatabase?) {
        //PERMITE CREAR LA ESTRUCUTRA DE TABLAS DE LAS BD SQLite
        //AQUI ES DONDE VOY A ESCRIBIR TODOS LOS CREATE TABLE
        try {
            db!!.execSQL("CREATE TABLE EVIDENCIAS (IDEVIDENCIA INTEGER PRIMARY KEY AUTOINCREMENT, IDACTIVIDAD INTEGER, FOTO BLOB,FOREIGN KEY(IDACTIVIDAD) REFERENCES ACTIVIDADES (IDACTIVIDAD))")

            //  db!!.execSQL("CREATE TABLE PERSONA (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR(200), DOMICILIO VARCHAR(500))")
            db!!.execSQL("CREATE TABLE ACTIVIDAD (IDACTIVIDAD INTEGER PRIMARY KEY AUTOINCREMENT, DESCRIPCION VARCHAR(200), FECHACAPTURA VARCHAR(200),FECHAENTREGA VARCHAR(200))")


        }catch (error:SQLiteException){
            //SE DISPARA SI HAY UN ERROR EN EXECSQL
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        //permite crear la estrucutura de las tablas de una base de datos
        //UPGRADE = ACTUALIZACION MAYOR = CAMBIO EN TABLAS
        //UPDATE = ACTUALIZACION MENOR = CAMBIO EN DATA
    }

}