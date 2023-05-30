package com.example.kommun_uslugi.ui.table;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TableViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TableViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Table fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}