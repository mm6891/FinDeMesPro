package globalsolutions.findemes.database.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by manuel.molero on 06/02/2015.
 */
public class MovimientoItem {

    String tipoMovimiento;
    String descripcion;

    public int get_idRegistro() {
        return _idRegistro;
    }

    public void set_idRegistro(int _idRegistro) {
        this._idRegistro = _idRegistro;
    }

    int _idRegistro;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    int _id;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    String categoria;

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
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

    String valor;
    String fecha;
}
