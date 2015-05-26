package globalsolutions.findemes.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by manuel.molero on 06/02/2015.
 */
public class InformeItem implements Parcelable{

    String periodoDesc;
    String ingresoValor;
    String gastoValor;

    public ArrayList<MovimientoItem> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(ArrayList<MovimientoItem> movimientos) {
        this.movimientos = movimientos;
    }

    ArrayList<MovimientoItem> movimientos;

    public String getTipoInforme() {
        return tipoInforme;
    }

    public void setTipoInforme(String tipoInforme) {
        this.tipoInforme = tipoInforme;
    }

    //TODOS, INGRESOS o GASTOS
    String tipoInforme;
    public String getPeriodoDesc() {
        return periodoDesc;
    }

    public void setPeriodoDesc(String periodoDesc) {
        this.periodoDesc = periodoDesc;
    }

    public String getIngresoValor() {
        return ingresoValor;
    }

    public void setIngresoValor(String ingresoValor) {
        this.ingresoValor = ingresoValor;
    }

    public String getGastoValor() {
        return gastoValor;
    }

    public void setGastoValor(String gastoValor) {
        this.gastoValor = gastoValor;
    }

    public String getTotalValor() {
        return totalValor;
    }

    public void setTotalValor(String totalValor) {
        this.totalValor = totalValor;
    }

    String totalValor;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getPeriodoDesc());
        dest.writeString(getIngresoValor());
        dest.writeString(getGastoValor());
        dest.writeString(getTotalValor());
        dest.writeArray(this.getMovimientos().toArray());
    }
}
