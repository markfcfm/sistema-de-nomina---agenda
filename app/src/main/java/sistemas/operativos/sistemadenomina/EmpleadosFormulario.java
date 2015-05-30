package sistemas.operativos.sistemadenomina;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class EmpleadosFormulario extends Activity {
	
	private SituacionDbAdapter dbAdapterSituacion ;
    private SituacionSpinnerAdapter situacionSpinnerAdapter ;

	//
    // Modo del formulario
    //
	private int modo ;
	
	//
	// Identificador del registro que se edita cuando la opción es MODIFICAR
	//
	private long id ;
    private Empleados empleados = new Empleados(this);
	
    //
    // Elementos de la vista
    //
    private EditText nombre;
    private EditText numNomina;
    private EditText direccion;
    private EditText curp;
    private EditText sueldo;
    private EditText fecha;
    private CheckBox activo ;
    private Spinner departamento ;
	
	private Button boton_guardar;
	private Button boton_cancelar;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empleados_formulario);
		
		Intent intent = getIntent();
		Bundle extra = intent.getExtras();

		if (extra == null) return;
		
        //
        // Obtenemos los elementos de la vista
        //
        nombre = (EditText) findViewById(R.id.nombre);
        numNomina = (EditText) findViewById(R.id.numNomina);
        direccion = (EditText) findViewById(R.id.direccion);
        curp = (EditText) findViewById(R.id.curp);
        sueldo = (EditText) findViewById(R.id.sueldo);
        fecha = (EditText) findViewById(R.id.fecha);
        activo = (CheckBox) findViewById(R.id.activo);
        departamento = (Spinner) findViewById(R.id.departamento);

		boton_guardar = (Button) findViewById(R.id.boton_guardar);
		boton_cancelar = (Button) findViewById(R.id.boton_cancelar);

        //
        // Creamos el adaptador del spinner de departamento y lo asociamos
        //
        situacionSpinnerAdapter = new SituacionSpinnerAdapter(this, Situacion.getAll(this, null));
        departamento.setAdapter(situacionSpinnerAdapter);

		//
		// Obtenemos el identificador del registro si viene indicado
		//
		if (extra.containsKey(EmpleadosDbAdapter.C_COLUMNA_ID))
		{
			id = extra.getLong(EmpleadosDbAdapter.C_COLUMNA_ID);
			consultar(id);
		}
		
		//
		// Establecemos el modo del formulario
		//
		establecerModo(extra.getInt(EmpleadosActivity.C_MODO));
		
		//
		// Definimos las acciones para los dos botones
		//
		boton_guardar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				guardar();
			}
		});
		
		boton_cancelar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				cancelar();	
			}
		});
		
	}
	
    private void establecerModo(int m)
    {
        this.modo = m ;

        if (modo == EmpleadosActivity.C_VISUALIZAR)
        {
            this.setTitle(nombre.getText().toString());
            this.setEdicion(false);
        }
        else if (modo == EmpleadosActivity.C_CREAR)
        {
            this.setTitle(R.string.empleados_crear_titulo);
            this.setEdicion(true);
        }
        else if (modo == EmpleadosActivity.C_EDITAR)
        {
            this.setTitle(R.string.empleados_editar_titulo);
            this.setEdicion(true);
        }
    }
	
private void consultar(long id)
{
    //
    // Consultamos la empleados por el identificador
    //
    empleados = Empleados.find(this, id);

    nombre.setText(empleados.getNombre());
    numNomina.setText(empleados.getnumNomina());
    direccion.setText(empleados.getdireccion());
    curp.setText(empleados.getcurp());
    sueldo.setText(empleados.getsueldo());
    fecha.setText(empleados.getfecha());
    activo.setChecked(empleados.isactivo());
    departamento.setSelection(situacionSpinnerAdapter.getPositionById(empleados.getdepartamento()));

}

private void setEdicion(boolean opcion)
{
    nombre.setEnabled(opcion);
    numNomina.setEnabled(opcion);
    direccion.setEnabled(opcion);
    curp.setEnabled(opcion);
    sueldo.setEnabled(opcion);
    fecha.setEnabled(opcion);
    activo.setEnabled(opcion);
    departamento.setEnabled(opcion);

    // Controlamos visibilidad de botonera
    LinearLayout v = (LinearLayout) findViewById(R.id.botonera);

    if (opcion)
        v.setVisibility(View.VISIBLE);

    else
        v.setVisibility(View.GONE);
}
	
private void guardar()
{
    empleados.setNombre(nombre.getText().toString());
    empleados.setnumNomina(numNomina.getText().toString());
    empleados.setdireccion(direccion.getText().toString());
    empleados.setcurp(curp.getText().toString());
    empleados.setsueldo(sueldo.getText().toString());
    empleados.setfecha(fecha.getText().toString());
    empleados.setactivo(activo.isChecked());
    empleados.setdepartamento(departamento.getSelectedItemId());

    empleados.save();

    if (modo == EmpleadosActivity.C_CREAR)
    {
        Toast.makeText(EmpleadosFormulario.this, R.string.empleados_crear_confirmacion, Toast.LENGTH_SHORT).show();
    }
    else if (modo == EmpleadosActivity.C_EDITAR)
    {
        Toast.makeText(EmpleadosFormulario.this, R.string.empleados_editar_confirmacion, Toast.LENGTH_SHORT).show();
    }

    //
    // Devolvemos el control
    //
    setResult(RESULT_OK);
    finish();
}
	
	private void cancelar()
	{
		setResult(RESULT_CANCELED, null);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.clear();
	    
		if (modo == EmpleadosActivity.C_VISUALIZAR)
			getMenuInflater().inflate(R.menu.empleados_formulario_ver, menu);
		
		else
			getMenuInflater().inflate(R.menu.empleados_formulario_editar, menu);
		
		return true;
	}
	
@Override
public boolean onMenuItemSelected(int featureId, MenuItem item) {
	
	switch (item.getItemId())
	{
		case R.id.menu_eliminar:
			borrar(id);
			return true;
			
		case R.id.menu_cancelar:
			cancelar();
			return true;
			
		case R.id.menu_guardar:
			guardar();
			return true;
			
		case R.id.menu_editar:
			establecerModo(EmpleadosActivity.C_EDITAR);
			return true;
	}
	
	return super.onMenuItemSelected(featureId, item);
}
	
	private void borrar(final long id)
	{
		/*
		 * Borramos el registro con confirmación
		 */
		AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(this);
		
		dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
		dialogEliminar.setTitle(getResources().getString(R.string.empleados_eliminar_titulo));
		dialogEliminar.setMessage(getResources().getString(R.string.empleados_eliminar_mensaje));
		dialogEliminar.setCancelable(false);
		
		dialogEliminar.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int boton) {
				empleados.delete();
				Toast.makeText(EmpleadosFormulario.this, R.string.empleados_eliminar_confirmacion, Toast.LENGTH_SHORT).show();
				/*
				 * Devolvemos el control
				 */
				setResult(RESULT_OK);
				finish();
			}
		});
		
		dialogEliminar.setNegativeButton(android.R.string.no, null);
		
		dialogEliminar.show();
		
	}

}