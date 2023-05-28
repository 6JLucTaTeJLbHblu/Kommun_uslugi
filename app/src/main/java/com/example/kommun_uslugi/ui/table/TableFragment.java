package com.example.kommun_uslugi.ui.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.kommun_uslugi.MainActivity;
import com.example.kommun_uslugi.R;
import com.example.kommun_uslugi.databinding.FragmentTableBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class TableFragment extends Fragment {

    private ContentValues contentValues;
    private FragmentTableBinding binding;
    private int columns = 10;
    private String[] names_of_columns = {"Period", "Gas", "Price_gas", "Water", "Outwater", "Water_price", "Electricity1", "Electricity2", "Electricity3", "Price_electricity"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TableViewModel tableViewModel =
                new ViewModelProvider(this).get(TableViewModel.class);

        binding = FragmentTableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textTable;
//        tableViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        TableLayout main_table = root.findViewById(R.id.main_table);
        Button add_row_button = root.findViewById(R.id.settings_add_row_button);
        Button save_button = root.findViewById(R.id.save_settings);
        SQLiteDatabase database = MainActivity.dbHelper.getWritableDatabase();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy", Locale.US);

        Cursor cursor = database.query(MainActivity.dbHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            for (int i = 0; i < cursor.getCount(); i++){
                TableRow newrow = new TableRow(getContext());
                for (int j = 1; j < cursor.getColumnCount(); j++) {
                    EditText editText = new EditText(getContext());
                    editText.setTextColor(Color.BLACK);
                    editText.setText(cursor.getString(j));
                    if (j == 3 || j == 6 || j == 10)
                        editText.setEnabled(false);
                    if (j == 1) {
                        editText.setHint(R.string.hint);
                        editText.setHintTextColor(Color.parseColor("#808080"));
                    }
                    newrow.addView(editText);
                }
                main_table.addView(newrow);
                cursor.moveToNext();
            }
        } else {
            TableRow newrow = new TableRow(getContext());
            for (int j = 1; j < cursor.getColumnCount(); j++) {
                EditText editText = new EditText(getContext());
                editText.setTextColor(Color.BLACK);
                if (j == 3 || j == 6 || j == 10) {
                    editText.setText("0.00");
                    editText.setEnabled(false);
                }
                if (j == 1) {
                    editText.setHint(R.string.hint);
                    editText.setHintTextColor(Color.parseColor("#808080"));
                }
                newrow.addView(editText);
            }
            main_table.addView(newrow);
        }
        cursor.close();

        add_row_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow newrow = new TableRow(getContext());
                for (int j = 1; j < cursor.getColumnCount(); j++) {
                    EditText editText = new EditText(getContext());
                    editText.setTextColor(Color.BLACK);
                    if (j == 3 || j == 6 || j == 10) {
                        editText.setText("0.00");
                        editText.setEnabled(false);
                    }
                    if (j == 1) {
                        editText.setHint(R.string.hint);
                        editText.setHintTextColor(Color.parseColor("#808080"));
                    }
                    newrow.addView(editText);
                }
                main_table.addView(newrow);
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = MainActivity.dbHelper.getWritableDatabase();
                Cursor cursor = database.query(MainActivity.dbHelper.SETTINGS_TABLE_NAME, null, null, null, null, null, null);
                if (!cursor.moveToFirst())
                    database.insert(MainActivity.dbHelper.TABLE_NAME, null, contentValues);
                for (int i = 1; i < main_table.getChildCount(); i++) {
                    contentValues = new ContentValues();
                    TableRow tableRow = (TableRow) main_table.getChildAt(i);
                    EditText editText_date = (EditText) tableRow.getChildAt(0);
                    if (editText_date.getText().toString().trim().length() != 0) {
                        try {
                            LocalDate current_date = LocalDate.parse(editText_date.getText().toString(), formatter);
                            cursor.moveToLast();
                            int flag = 0;
                            while (cursor.getString(1).equals("") || current_date.isBefore(LocalDate.parse(cursor.getString(1), formatter))) {
                                if (!cursor.moveToPrevious()) {
                                    flag = 1;
                                    cursor.moveToNext();
                                    Toast.makeText(getContext(), "Добавьте период " + editText_date.getText().toString() + "-" + LocalDate.parse(cursor.getString(1), formatter) + " в настройки", Toast.LENGTH_LONG).show();
                                    break;
                                }
                            }
                            if (flag == 1)
                                continue;
                        } catch (DateTimeParseException e){
                            if (!editText_date.getText().toString().equals(""))
                                if (cursor.getString(1).equals(""))
                                    Toast.makeText(getContext(), "Отсутствует период " + editText_date.getText().toString() + " в настройках", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getContext(), "Неверный формат периода в строке " + i, Toast.LENGTH_LONG).show();
                         }
                    }
                    EditText update_to_zero = (EditText) tableRow.getChildAt(2);
                    update_to_zero.setText("0.00");
                    update_to_zero = (EditText) tableRow.getChildAt(5);
                    update_to_zero.setText("0.00");
                    update_to_zero = (EditText) tableRow.getChildAt(9);
                    update_to_zero.setText("0.00");
                    for (int j = 0; j < tableRow.getChildCount(); j++) {
                        EditText editText = (EditText) tableRow.getChildAt(j);
                        try {
                            if (editText.getText().toString().trim().length() != 0) {
                                if (j == 1) {
                                    EditText price_editText = (EditText) tableRow.getChildAt(2);
                                    double gas = Double.parseDouble(editText.getText().toString());
                                    double price = Double.parseDouble(cursor.getString(2));
                                    price_editText.setText(Double.toString(gas * price));
                                } else if (j == 3 || j == 4) {
                                    EditText price_editText = (EditText) tableRow.getChildAt(5);
                                    double water = Double.parseDouble(editText.getText().toString());
                                    double price = Double.parseDouble(cursor.getString(j));
                                    price_editText.setText(Double.toString(Double.parseDouble(price_editText.getText().toString()) + water * price));
                                } else if (j == 6 || j == 7 || j == 8) {
                                    EditText price_editText = (EditText) tableRow.getChildAt(9);
                                    double electricity = Double.parseDouble(editText.getText().toString());
                                    double price = Double.parseDouble(cursor.getString(j - 1));
                                    price_editText.setText(Double.toString(Double.parseDouble(price_editText.getText().toString()) + electricity * price));
                                }
                            }
                        } catch (NumberFormatException e){
                            Toast.makeText(getContext(), "Отсутствует период " + editText_date.getText().toString() + " в настройках", Toast.LENGTH_LONG).show();
                        }
                        contentValues.put(names_of_columns[j], editText.getText().toString());
                    }
                    Cursor cursor_main = database.query(MainActivity.dbHelper.TABLE_NAME, null, null, null, null, null, null);
                    if (i <= cursor_main.getCount()) {
                        database.update(MainActivity.dbHelper.TABLE_NAME, contentValues, "_id = " + i, null);
                    }
                    else {
                        database.insert(MainActivity.dbHelper.TABLE_NAME, null, contentValues);
                    }
                }
                cursor.close();
                MainActivity.dbHelper.close();

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}