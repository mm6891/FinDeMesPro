package globalsolutions.findemes.database.model;

/**
 * Created by manuel.molero on 08/04/2015.
 */
public class Registro {

    int _id;
    String descripcion;
    String periodicidad;
    String tipo;
    String valor;
    String grupo;
    Integer activo;
    String fecha;

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

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

    public String getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

     @Override
     public boolean equals(Object o) {
         Registro r = (Registro)o;
         boolean eq = true;
         if(!descripcion.equals(r.getDescripcion()))
             return false;
         if(!periodicidad.equals(r.getPeriodicidad()))
             return false;
         if(!tipo.equals(r.getTipo()))
             return false;
         if(!valor.equals(r.getValor()))
             return false;
         if(!grupo.equals(r.getGrupo()))
             return false;
         if(!activo.equals(r.getActivo()))
             return false;
         if(!fecha.equals(r.getFecha()))
             return false;

         return eq;
     }
}
