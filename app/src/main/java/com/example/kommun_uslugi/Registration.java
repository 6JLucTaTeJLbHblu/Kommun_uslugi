package com.example.kommun_uslugi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

public class Registration extends AppCompatActivity {
    private FirebaseAuth registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        registration = FirebaseAuth.getInstance();
        Button register = findViewById(R.id.register_button);
        EditText email = findViewById(R.id.register_email);
        EditText password = findViewById(R.id.register_password);
        EditText repeat_password = findViewById(R.id.register_repeat_password);
        TextView gotoauth = findViewById(R.id.textView4);

        // Переход на окно авторизации
        gotoauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registration.this, Authorization.class);
                startActivity(i);
            }
        });

        // Регистрация
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || repeat_password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_LONG).show();
                }
                else if (!password.getText().toString().equals(repeat_password.getText().toString())){
                    Toast.makeText(Registration.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                }
                else{
                    registration.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent i = new Intent(Registration.this, MainActivity.class);
                                startActivity(i);
                            }
                            else{
                                // Обработка возможных ошибок
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthUserCollisionException e){
                                    Toast.makeText(Registration.this, "Пользователь с таким e-mail уже зарегистрирован", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseTooManyRequestsException e) {
                                    Toast.makeText(Registration.this, "Слишком много запросов, попробуйте позже", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthWeakPasswordException e){
                                    Toast.makeText(Registration.this, "Слабый пароль", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(Registration.this, "Некорректный e-mail", Toast.LENGTH_SHORT).show();
                                } catch (Exception e){
                                    Toast.makeText(Registration.this, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }

            }
        });

    }
}