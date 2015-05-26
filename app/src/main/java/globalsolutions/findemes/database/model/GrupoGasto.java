package globalsolutions.findemes.database.model;

/**
 * Created by manuel.molero on 03/02/2015.
 */
public class GrupoGasto {
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
    public GrupoGasto() {
    }


}
