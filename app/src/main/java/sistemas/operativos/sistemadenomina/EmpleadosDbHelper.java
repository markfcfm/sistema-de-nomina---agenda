package sistemas.operativos.sistemadenomina;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EmpleadosDbHelper extends SQLiteOpenHelper {

private static int version = 4;
private static String name = "NominaDb" ;
private static CursorFactory factory = null;

public EmpleadosDbHelper(Context context)
{
    super(context, name, factory, version);
}

@Override
public void onCreate(SQLiteDatabase db)
{
    Log.i(this.getClass().toString(), "Creando base de datos");

    db.execSQL( "CREATE TABLE EMPLEADOS(" +
                " _id INTEGER PRIMARY KEY," +
                " hip_nombre TEXT NOT NULL, " +
                " hip_numNomina TEXT, " +
                " hip_direccion TEXT," +
                " hip_sueldo TEXT," +
                " hip_curp TEXT," +
                " hip_fecha TEXT)" );

    db.execSQL( "CREATE UNIQUE INDEX hip_nombre ON EMPLEADOS(hip_nombre ASC)" );

    Log.i(this.getClass().toString(), "Tabla EMPLEADOS creada");

    /*
     * Insertamos datos iniciales
     */
    db.execSQL("INSERT INTO EMPLEADOS(_id, hip_nombre) VALUES(1,'Nombre1')");
    db.execSQL("INSERT INTO EMPLEADOS(_id, hip_nombre) VALUES(2,'Nombre2')");
    db.execSQL("INSERT INTO EMPLEADOS(_id, hip_nombre) VALUES(3,'Nombre3')");
    db.execSQL("INSERT INTO EMPLEADOS(_id, hip_nombre) VALUES(4,'Nombre4')");
    db.execSQL("INSERT INTO EMPLEADOS(_id, hip_nombre) VALUES(5,'Nombre5')");
    db.execSQL("INSERT INTO EMPLEADOS(_id, hip_nombre) VALUES(6,'Nombre6')");
    db.execSQL("INSERT INTO EMPLEADOS(_id, hip_nombre) VALUES(7,'Nombre7')");

    Log.i(this.getClass().toString(), "Datos iniciales EMPLEADOS insertados");

    Log.i(this.getClass().toString(), "Base de datos creada");

    // Aplicamos las sucesivas actualizaciones
    upgrade_2(db);
    upgrade_3(db);
    upgrade_4(db);
}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
{
    // Actualización a versión 2
    if (oldVersion < 2)
    {
        upgrade_2(db);
    }
    // Actualización a versión 3
    if (oldVersion < 3)
    {
        upgrade_3(db);
    }
    // Actualización a versión 4
    if (oldVersion < 4)
    {
        upgrade_4(db);
    }
}

    private void upgrade_2(SQLiteDatabase db)
    {
        //
        // Upgrade versión 2: definir algunos datos de ejemplo
        //
        db.execSQL( "UPDATE EMPLEADOS SET hip_direccion = 'AAAA'," +
                    "					hip_sueldo = 'AAAA'," +
                    "					hip_fecha = 'AAAA'" +
                    " WHERE _id = 1");

        Log.i(this.getClass().toString(), "Actualización versión 2 finalizada");
    }

    private void upgrade_3(SQLiteDatabase db)
    {
        //
        // Upgrade versión 3: Incluir activo_sn
        //
        db.execSQL("ALTER TABLE EMPLEADOS ADD hip_activo_sn   VARCHAR2(1) NOT NULL DEFAULT 'N'");

        Log.i(this.getClass().toString(), "Actualización versión 3 finalizada");
    }

    private void upgrade_4(SQLiteDatabase db)
    {
        //
        // Upgrade versión 4: Incluir la clasificación DEPARTAMENTO para los empleados
        //
        db.execSQL( "CREATE TABLE SITUACION(" +
                    " _id INTEGER PRIMARY KEY," +
                    " sit_nombre TEXT NOT NULL)");

        //
        db.execSQL( "CREATE UNIQUE INDEX sit_nombre ON SITUACION(sit_nombre ASC)" );

        db.execSQL("INSERT INTO SITUACION(_id, sit_nombre) VALUES(1,'Administracion')");
        db.execSQL("INSERT INTO SITUACION(_id, sit_nombre) VALUES(2,'Atencion al cliente')");
        db.execSQL("INSERT INTO SITUACION(_id, sit_nombre) VALUES(3,'Investigacion')");
        db.execSQL("INSERT INTO SITUACION(_id, sit_nombre) VALUES(4,'Recursos Humanos')");
        db.execSQL("INSERT INTO SITUACION(_id, sit_nombre) VALUES(5,'Finanzas')");
        db.execSQL("INSERT INTO SITUACION(_id, sit_nombre) VALUES(6,'Contabilidad')");
        db.execSQL("INSERT INTO SITUACION(_id, sit_nombre) VALUES(7,'Operaciones')");
        db.execSQL("INSERT INTO SITUACION(_id, sit_nombre) VALUES(8,'Ventas')");

        db.execSQL("ALTER TABLE EMPLEADOS ADD hip_sit_id INTEGER NOT NULL DEFAULT 1");

        Log.i(this.getClass().toString(), "Actualización versión 4 finalizada");
    }

}

