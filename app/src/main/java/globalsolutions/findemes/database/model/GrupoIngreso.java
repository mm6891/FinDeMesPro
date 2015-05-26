package globalsolutions.findemes.database.model;

/**
 * Created by manuel.molero on 06/02/2015.
 */
public class GrupoIngreso {
    int _id;

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    String grupo;

    // constructors
    public GrupoIngreso() {
    }
}
