package globalsolutions.findemes.pantallas.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import globalsolutions.findemes.R;


/**
 * Created by Manuel on 23/02/2015.
 */
public class OptionActivityBarChart extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        String periodo = getIntent().getExtras().getString("periodo");
        ((TextView)findViewById(R.id.tvTipoInforme)).setText(periodo);
        double[] ingresos = getIntent().getExtras().getDoubleArray("ingresos");
        double[] gastos = getIntent().getExtras().getDoubleArray("gastos");
        String[] ejeX = getIntent().getExtras().getStringArray("ejeX");
        String tipoGrafica = getIntent().getExtras().getString("tipoGrafica");

        double max = 0.00;
        double size =  ingresos.length;

        for(int i = 0 ; i < size; i++){
            if(ingresos[i] > max){
                max = ingresos[i];
            }
            if(gastos[i] > max){
                max = gastos[i];
            }
        }

        final double MAX_VALUE = max;
        double dist = MAX_VALUE/size;

        GraphicalView chartView = getGrafica(tipoGrafica,gastos, ingresos, ejeX, size, dist, MAX_VALUE);
        LinearLayout parent = (LinearLayout) findViewById(R.id.graph);
        parent.addView(chartView);
    }

    private GraphicalView getGrafica(String tipoGrafica, double[] gastos, double[] ingresos, String[] ejeX, double size, double dist, double MAX_VALUE){
        XYMultipleSeriesDataset multipleSeriesDataset = new XYMultipleSeriesDataset();
        XYSeries serieGastos = new XYSeries(getResources().getString(R.string.MENU_GASTOS));
        XYSeries serieIngresos = new XYSeries(getResources().getString(R.string.MENU_INGRESOS));
        double distancia = 0;
        for(int i = 0 ; i < size; i++) {
            serieGastos.add(distancia, new Double(gastos[i]));
            serieGastos.addAnnotation(ejeX[i], distancia, new Double(gastos[i]));
            serieIngresos.add(distancia, new Double(ingresos[i]));
            serieIngresos.addAnnotation(ejeX[i], distancia, new Double(ingresos[i]));
            distancia += dist;
        }
        multipleSeriesDataset.addSeries(serieGastos);
        multipleSeriesDataset.addSeries(serieIngresos);

        // Now we create the renderer
        XYSeriesRenderer rendererGastos = new XYSeriesRenderer();
        rendererGastos.setLineWidth(2);
        rendererGastos.setColor(Color.RED);
        // Include low and max value
        rendererGastos.setDisplayBoundingPoints(true);
        // we add point markers
        rendererGastos.setPointStyle(PointStyle.CIRCLE);
        rendererGastos.setPointStrokeWidth(3);

        XYSeriesRenderer rendererIngresos = new XYSeriesRenderer();
        rendererIngresos.setLineWidth(2);
        rendererIngresos.setColor(Color.GREEN);
        // Include low and max value
        rendererIngresos.setDisplayBoundingPoints(true);
        // we add point markers
        rendererIngresos.setPointStyle(PointStyle.CIRCLE);
        rendererIngresos.setPointStrokeWidth(3);

        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(rendererGastos);
        mRenderer.addSeriesRenderer(rendererIngresos);

        // We want to avoid black border
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setYAxisMax(MAX_VALUE);
        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(true); // we show the grid
        mRenderer.setYLabelsColor(0, Color.BLACK);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setLabelsColor(Color.BLACK);

        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setZoomEnabled(true);

        GraphicalView chartView = null;
        if(tipoGrafica.equals(getResources().getString(R.string.OpcionGrafica_Lineal)))
            chartView = ChartFactory.getLineChartView(this, multipleSeriesDataset, mRenderer);
        else
            chartView = ChartFactory.getBarChartView(this, multipleSeriesDataset, mRenderer, BarChart.Type.DEFAULT);

        return  chartView;
    }

    @Override
    public void onBackPressed() {
        backActivity();
    }

    private void backActivity(){
        Intent in = new Intent(OptionActivityBarChart.this, InformesActivity.class);
        startActivity(in);
        setResult(RESULT_OK);
        finish();
    }
}
