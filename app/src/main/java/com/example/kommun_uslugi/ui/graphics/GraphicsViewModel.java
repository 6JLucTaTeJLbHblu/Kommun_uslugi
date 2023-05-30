package com.example.kommun_uslugi.ui.graphics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GraphicsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GraphicsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Graphics fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}