package sistemas.operativos.sistemadenomina;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class EmpleadosDbAdapter {

	//
	// Definimos constante con el nombre de la tabla
	//
	public static final String C_TABLA = "EMPLEADOS" ;
	
    //
    // Definimos constantes con el nombre de las columnas de la tabla
    //
    public static final String C_COLUMNA_ID	= "_id";
    public static final String C_COLUMNA_NOMBRE = "hip_nombre";
    public static final String C_COLUMNA_NUMNOMINA = "hip_numNomina";
    public static final String C_COLUMNA_DIRECCION = "hip_direccion";
    public static final String C_COLUMNA_SUELDO = "hip_sueldo";
    public static final String C_COLUMNA_CURP = "hip_curp";

    public static final String C_COLUMNA_FECHA = "hip_fecha";
    public static final String C_COLUMNA_ACTIVO = "hip_activo_sn";
    public static final String C_COLUMNA_DEPARTAMENTO = "hip_sit_id";


    private Context contexto;
    private EmpleadosDbHelper dbHelper;
    private SQLiteDatabase db;

    //
    // Definimos lista de columnas de la tabla para utilizarla en las consultas a la base de datos
    //
    private String[] columnas = new String[]{
            C_COLUMNA_ID,
            C_COLUMNA_NOMBRE,
            C_COLUMNA_NUMNOMINA,
            C_COLUMNA_DIRECCION,
            C_COLUMNA_SUELDO,
            C_COLUMNA_CURP,
            C_COLUMNA_FECHA,
            C_COLUMNA_ACTIVO,
            C_COLUMNA_DEPARTAMENTO} ;

	public EmpleadosDbAdapter(Context context)
	{
		this.contexto = context;
	}

	public EmpleadosDbAdapter abrir() throws SQLException
	{
		dbHelper = new EmpleadosDbHelper(contexto);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void cerrar()
	{
		dbHelper.close();
	}

	
    /**
     * Devuelve cursor con todos los registros y columnas de la tabla
     */
    public Cursor getCursor(String filtro) throws SQLException {

        if (db == null)
            abrir();

        Cursor c = db.query(true, C_TABLA, columnas, filtro, null, null, null, null, null);

        return c;
    }
	
	/**
	 * Devuelve cursor con todos las columnas del registro
	 */
	public Cursor getRegistro(long id) throws SQLException
	{
        if (db == null)
            abrir();

        Cursor c = db.query( true, C_TABLA, columnas, C_COLUMNA_ID + "=" + id, null, null, null, null, null);
		
		//Nos movemos al primer registro de la consulta
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	
	/**
	 * Inserta los valores en un registro de la tabla
	 */
	public long insert(ContentValues reg)
	{
		if (db == null)
			abrir();
		
		return db.insert(C_TABLA, null, reg);
	}
	
	/**
	 * Eliminar el registro con el identificador indicado
	 */
	public long delete(long id)
	{
		if (db == null)
			abrir();
		
		return db.delete(C_TABLA, "_id=" + id, null);
	}
	
    /**
     * Modificar el registro
     */
    public long update(ContentValues reg)
    {
        long result = 0;

        if (db == null)
            abrir();

        if (reg.containsKey(C_COLUMNA_ID))
        {
            //
            // Obtenemos el id y lo borramos de los valores
            //
            long id = reg.getAsLong(C_COLUMNA_ID);

            reg.remove(C_COLUMNA_ID);

            //
            // Actualizamos el registro con el identificador que hemos extraido
            //
            result = db.update(C_TABLA, reg, "_id=" + id, null);
        }
        return result;
    }

    /**
     * Comprueba si existe el registro
     */
    public boolean exists(long id) throws SQLException
    {
        boolean exists ;

        if (db == null)
            abrir();

        Cursor c = db.query( true, C_TABLA, columnas, C_COLUMNA_ID + "=" + id, null, null, null, null, null);

        exists = (c.getCount() > 0);

        c.close();

        return exists;
    }

    public ArrayList<Empleados> getEmpleados(String filtro)
    {
        ArrayList<Empleados> empleadoses = new ArrayList<Empleados>();

        if (db == null)
            abrir();

        Cursor c = db.query(true, C_TABLA, columnas, filtro, null, null, null, null, null);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
        {
            empleadoses.add(Empleados.cursorToEmpleados(contexto, c));
        }

        c.close();

        return empleadoses;
    }



}