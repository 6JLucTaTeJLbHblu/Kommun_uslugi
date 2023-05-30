package com.example.kommun_uslugi.ui.settings;

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
import com.example.kommun_uslugi.databinding.FragmentSettingsBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class SettingsFragment extends Fragment {

    private ContentValues contentValues;
    private FragmentSettingsBinding binding;
    private int columns = 7;
    public static String[] names_of_columns = {"Settings_Period", "Settings_Gas",  "Settings_Water", "Settings_Outwater", "Settings_Electricity1", "Settings_Electricity2", "Settings_Electricity3"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel homeViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TableLayout settings_table = root.findViewById(R.id.settings_table);
        Button settings_add_row_button = root.findViewById(R.id.settings_add_row_button);
        Button save_settings = root.findViewById(R.id.save_settings);
        SQLiteDatabase database = MainActivity.dbHelper.getWritableDatabase();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy", Locale.US);

        // Выгрузка данных из бд
        Cursor cursor = database.query(MainActivity.dbHelper.SETTINGS_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            for (int i = 0; i < cursor.getCount(); i++){
                TableRow newrow = new TableRow(getContext());
                for (int j = 1; j < cursor.getColumnCount(); j++) {
                    EditText editText = new EditText(getContext());
                    editText.setTextColor(Color.BLACK);
                    editText.setText(cursor.getString(j));
                    if (j == 1){
                        editText.setHint(R.string.hint);
                        editText.setHintTextColor(Color.parseColor("#808080"));
                    }
                    newrow.addView(editText);
                }
                settings_table.addView(newrow);
                cursor.moveToNext();
            }
        } else {
            TableRow newrow = new TableRow(getContext());
            for (int j = 0; j < 7; j++) {
                EditText editText = new EditText(getContext());
                editText.setTextColor(Color.BLACK);
                if (j == 0){
                    editText.setHint(R.string.hint);
                    editText.setHintTextColor(Color.parseColor("#808080"));
                }
                newrow.addView(editText);
            }
            settings_table.addView(newrow);
        }
        cursor.close();

        // Добавление нового ряда в таблицу
        settings_add_row_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow newrow = new TableRow(getContext());
                for (int j = 0; j < 7; j++) {
                    EditText editText = new EditText(getContext());
                    editText.setTextColor(Color.BLACK);
                    if (j == 0) {
                        editText.setHint(R.string.hint);
                        editText.setHintTextColor(Color.parseColor("#808080"));
                    }
                    newrow.addView(editText);
                }
                settings_table.addView(newrow);
            }
        });

        // Сохранение настроек в бд
        save_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = MainActivity.dbHelper.getWritableDatabase();
                for (int i = 1; i < settings_table.getChildCount(); i++) {
                    contentValues = new ContentValues();
                    TableRow tableRow = (TableRow) settings_table.getChildAt(i);
                    EditText et = (EditText) tableRow.getChildAt(0);
                    if (et.getText().toString().equals("")) {
                        for (int j = 0; j < tableRow.getChildCount(); j++) {
                            contentValues.put(names_of_columns[j], "");
                        }
                        database.update(MainActivity.dbHelper.SETTINGS_TABLE_NAME, contentValues, "_id = " + i, null);
                        continue;
                    }
                    try {
                        LocalDate date = LocalDate.parse(et.getText().toString(), formatter);
                    } catch (DateTimeParseException e){
                        Toast.makeText(getContext(), "Неверный формат периода в строке " + i, Toast.LENGTH_LONG).show();
                        for (int j = 0; j < tableRow.getChildCount(); j++) {
                            contentValues.put(names_of_columns[j], "");
                        }
                        database.update(MainActivity.dbHelper.SETTINGS_TABLE_NAME, contentValues, "_id = " + i, null);
                        continue;
                    }
                    for (int j = 0; j < tableRow.getChildCount(); j++) {
                        EditText editText = (EditText) tableRow.getChildAt(j);
                        contentValues.put(names_of_columns[j], editText.getText().toString());
                    }
                    if (i <= cursor.getCount())
                        database.update(MainActivity.dbHelper.SETTINGS_TABLE_NAME, contentValues, "_id = " + i, null);
                    else
                        database.insert(MainActivity.dbHelper.SETTINGS_TABLE_NAME, null, contentValues);
                }
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