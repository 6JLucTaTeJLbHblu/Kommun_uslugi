package com.example.kommun_uslugi.ui.graphics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.kommun_uslugi.MainActivity;
import com.example.kommun_uslugi.R;
import com.example.kommun_uslugi.databinding.FragmentGraphicsBinding;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class GraphicsFragment extends Fragment {

    private FragmentGraphicsBinding binding;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy", Locale.US);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GraphicsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(GraphicsViewModel.class);

        binding = FragmentGraphicsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SQLiteDatabase database = MainActivity.dbHelper.getWritableDatabase();
        Cursor cursor = database.query(MainActivity.dbHelper.TABLE_NAME, null, null, null, null, null, null);
        GraphView graph_gas = (GraphView) root.findViewById(R.id.graph_gas);
        GraphView graph_water = (GraphView) root.findViewById(R.id.graph_water);
        GraphView graph_electricity = (GraphView) root.findViewById(R.id.graph_electricity);
        int months = 1;
        int current_year = Calendar.getInstance().get(Calendar.YEAR);
        boolean flag = true;

        // Определение последнего записанного в таблицу года, расходы за месяца которого будут отображаться в графиках
        if (cursor.moveToLast()){
            while (cursor.getString(1).equals("") || isExcept(cursor.getString(1))) {
                if (!cursor.moveToPrevious()) {
                    cursor.moveToFirst();
                    flag = false;
                    break;
                }
            }
            if (flag) {
                current_year = Integer.parseInt(cursor.getString(1).split("-")[2]);
            }
        }
        DataPoint[] dataPoints_gas = new DataPoint[13];
        DataPoint[] dataPoints_water = new DataPoint[13];
        DataPoint[] dataPoints_electricity = new DataPoint[13];
        dataPoints_gas[0] = new DataPoint(0, 0);
        dataPoints_water[0] = new DataPoint(0, 0);
        dataPoints_electricity[0] = new DataPoint(0, 0);

        // Добавление графикам информации по стоимости услуг за месяц
        if (cursor.moveToLast()){
            for (int i = 0; i < cursor.getCount(); i++){
                if (cursor.getString(1).equals("") || isExcept(cursor.getString(1))) {
                    cursor.moveToPrevious();
                    continue;
                }
                if (cursor.getString(1).split("-")[2].equals(Integer.toString(current_year))) {
                    int month = Integer.parseInt(cursor.getString(1).split("-")[1]);
                    dataPoints_gas[month] = new DataPoint(month, Double.parseDouble(cursor.getString(3)));
                    dataPoints_water[month] = new DataPoint(month, Double.parseDouble(cursor.getString(6)));
                    dataPoints_electricity[month] = new DataPoint(month, Double.parseDouble(cursor.getString(10)));
                }
                cursor.moveToPrevious();
            }
        }
        for (int i = 1; i < 13; i++){
            if (dataPoints_gas[i] == null){
                dataPoints_gas[i] = new DataPoint(0, 0);
                dataPoints_water[i] = new DataPoint(0, 0);
                dataPoints_electricity[i] = new DataPoint(0, 0);
            }
        }

        // Установка нужных параметров графиков
        graph_gas.getGridLabelRenderer().setGridColor(Color.BLACK);
        graph_gas.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);
        graph_gas.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLACK);
        graph_gas.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
        graph_gas.getGridLabelRenderer().setHorizontalAxisTitle("Месяц");
        graph_gas.getViewport().setScrollable(true);
        graph_gas.getViewport().setScalable(true);
        graph_gas.getViewport().setMaxX(12);
        graph_water.getGridLabelRenderer().setHorizontalAxisTitle("Месяц");
        graph_water.getViewport().setScrollable(true);
        graph_water.getViewport().setScalable(true);
        graph_water.getViewport().setMaxX(12);
        graph_water.getGridLabelRenderer().setGridColor(Color.BLACK);
        graph_water.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);
        graph_water.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLACK);
        graph_water.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
        graph_electricity.getGridLabelRenderer().setHorizontalAxisTitle("Месяц");
        graph_electricity.getViewport().setScrollable(true);
        graph_electricity.getViewport().setScalable(true);
        graph_electricity.getViewport().setMaxX(12);
        graph_electricity.getGridLabelRenderer().setGridColor(Color.BLACK);
        graph_electricity.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);
        graph_electricity.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLACK);
        graph_electricity.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
        BarGraphSeries<DataPoint> lineSeries_gas = new BarGraphSeries<>(dataPoints_gas);
        lineSeries_gas.setSpacing(10);
        lineSeries_gas.setDataWidth(1);
        lineSeries_gas.setDrawValuesOnTop(true);
        lineSeries_gas.setValuesOnTopColor(Color.BLACK);
        BarGraphSeries<DataPoint> lineSeries_water = new BarGraphSeries<>(dataPoints_water);
        lineSeries_water.setSpacing(10);
        lineSeries_water.setDataWidth(1);
        lineSeries_water.setDrawValuesOnTop(true);
        lineSeries_water.setValuesOnTopColor(Color.BLACK);
        BarGraphSeries<DataPoint> lineSeries_electricity = new BarGraphSeries<>(dataPoints_electricity);
        lineSeries_electricity.setSpacing(10);
        lineSeries_electricity.setDataWidth(1);
        lineSeries_electricity.setDrawValuesOnTop(true);
        lineSeries_electricity.setValuesOnTopColor(Color.BLACK);
        graph_gas.addSeries(lineSeries_gas);
        graph_water.addSeries(lineSeries_water);
        graph_electricity.addSeries(lineSeries_electricity);

        return root;
    }

    // Функция проверяет, является ли дата корректной
    public boolean isExcept(String d){
        try {
            LocalDate date = LocalDate.parse(d, formatter);
        } catch (Exception e){
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}