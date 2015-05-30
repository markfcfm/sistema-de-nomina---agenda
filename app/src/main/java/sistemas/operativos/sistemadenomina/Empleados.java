package sistemas.operativos.sistemadenomina;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class Empleados {

    private Context context;

    private Long id;

    private String nombre;
    private String numNomina;
    private String direccion;
    private String sueldo;
    private String curp;
    private String fecha;

    private boolean activo; //lo que define un registro visible
    private Long departamento;

    public Empleados(Context context)
    {
        this.context = context;
    }

    public Empleados(Context context, Long id, String nombre, String numNomina, String direccion, String sueldo, String curp, String fecha, boolean activo, Long departamento) {
        this.context = context;
        this.id = id;

        this.nombre = nombre;
        this.numNomina = numNomina;
        this.direccion = direccion;
        this.sueldo = sueldo;
        this.curp = curp;
        this.fecha = fecha;
        this.activo = activo; //checkmark
        this.departamento = departamento;//menú desplegable
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getnumNomina() {
        return numNomina;
    }

    public void setnumNomina(String numNomina) {
        this.numNomina = numNomina;
    }

    public String getdireccion() {
        return direccion;
    }

    public void setdireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getsueldo() {
        return sueldo;
    }

    public void setsueldo(String sueldo) {
        this.sueldo = sueldo;
    }

    public String getcurp() {
        return curp;
    }

    public void setcurp(String curp) {
        this.curp = curp;
    }

    public String getfecha() {
        return fecha;
    }

    public void setfecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isactivo() {
        return activo;
    }

    public void setactivo(boolean activo) {
        this.activo = activo;
    }

    public Long getdepartamento() {
        return departamento;
    }

    public void setdepartamento(Long departamento) {
        this.departamento = departamento;
    }

    public static Empleados find(Context context, long id)
    {
        EmpleadosDbAdapter dbAdapter = new EmpleadosDbAdapter(context);

        Cursor c = dbAdapter.getRegistro(id);

        Empleados empleados = Empleados.cursorToEmpleados(context, c);

        c.close();

        return empleados;
    }

    public static Empleados cursorToEmpleados(Context context, Cursor c)
    {
        Empleados empleados = null;

        if (c != null)
        {
            empleados = new Empleados(context);

            empleados.setId(c.getLong(c.getColumnIndex(EmpleadosDbAdapter.C_COLUMNA_ID)));

            empleados.setNombre(c.getString(c.getColumnIndex(EmpleadosDbAdapter.C_COLUMNA_NOMBRE)));
            empleados.setnumNomina(c.getString(c.getColumnIndex(EmpleadosDbAdapter.C_COLUMNA_NUMNOMINA)));
            empleados.setdireccion(c.getString(c.getColumnIndex(EmpleadosDbAdapter.C_COLUMNA_DIRECCION)));
            empleados.setsueldo(c.getString(c.getColumnIndex(EmpleadosDbAdapter.C_COLUMNA_SUELDO)));
            empleados.setcurp(c.getString(c.getColumnIndex(EmpleadosDbAdapter.C_COLUMNA_CURP)));
            empleados.setfecha(c.getString(c.getColumnIndex(EmpleadosDbAdapter.C_COLUMNA_FECHA)));
            empleados.setactivo(c.getString(c.getColumnIndex(EmpleadosDbAdapter.C_COLUMNA_ACTIVO)).equals("S"));
            empleados.setdepartamento(c.getLong(c.getColumnIndex(EmpleadosDbAdapter.C_COLUMNA_DEPARTAMENTO)));
        }

        return empleados;
    }

    private ContentValues toContentValues()
    {
        ContentValues reg = new ContentValues();

        reg.put(EmpleadosDbAdapter.C_COLUMNA_ID, this.getId());
        reg.put(EmpleadosDbAdapter.C_COLUMNA_NOMBRE, this.getNombre());
        reg.put(EmpleadosDbAdapter.C_COLUMNA_NUMNOMINA, this.getnumNomina());
        reg.put(EmpleadosDbAdapter.C_COLUMNA_DIRECCION, this.getdireccion());
        reg.put(EmpleadosDbAdapter.C_COLUMNA_SUELDO, this.getsueldo());
        reg.put(EmpleadosDbAdapter.C_COLUMNA_CURP, this.getcurp());
        reg.put(EmpleadosDbAdapter.C_COLUMNA_FECHA, this.getfecha());
        reg.put(EmpleadosDbAdapter.C_COLUMNA_ACTIVO, (this.isactivo())?"S":"N");
        reg.put(EmpleadosDbAdapter.C_COLUMNA_DEPARTAMENTO, this.getdepartamento());

        return reg;
    }

    public long save()
    {
        EmpleadosDbAdapter dbAdapter = new EmpleadosDbAdapter(this.getContext());

        // comprobamos si estamos insertando o actualizando según esté o no relleno el identificador
        if ((this.getId() == null) || (!dbAdapter.exists(this.getId())))
        {
            long nuevoId = dbAdapter.insert(this.toContentValues());

            if (nuevoId != -1)
            {
                this.setId(nuevoId);
            }
        }
        else
        {
            dbAdapter.update(this.toContentValues());
        }

        return this.getId();
    }

    public long delete()
    {
        // borramos el registro
        EmpleadosDbAdapter dbAdapter = new EmpleadosDbAdapter(this.getContext());

        return dbAdapter.delete(this.getId());
    }
}
