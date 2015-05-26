package globalsolutions.findemes.database.model;


/**
 * Created by manuel.molero on 03/02/2015.
 */
public class Gasto {

    int _id;
    String descripcion;
    String valor;
    String fecha;

    public int get_idRegistro() {
        return _idRegistro;
    }

    public void set_idRegistro(int _idRegistro) {
        this._idRegistro = _idRegistro;
    }

    int _idRegistro;

    public GrupoGasto getGrupoGasto() {
        return grupoGasto;
    }

    public void setGrupoGasto(GrupoGasto grupoGasto) {
        this.grupoGasto = grupoGasto;
    }

    GrupoGasto grupoGasto;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }



    // constructors
    public Gasto() {
    }


}
