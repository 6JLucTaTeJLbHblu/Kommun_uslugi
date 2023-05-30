package com.example.kommun_uslugi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

public class Authorization extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.authorization);

        setContentView(R.layout.authorization);
        EditText email = findViewById(R.id.authoriz_email);
        EditText password = findViewById(R.id.authoriz_password);
        Button login = findViewById(R.id.login_button);
        TextView create_account = findViewById(R.id.textView2);
        Intent i = new Intent(Authorization.this, MainActivity.class);
        startActivity(i);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Authorization.this, Registration.class);
                startActivity(i);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_LONG).show();
                }
                else{
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent i = new Intent(Authorization.this, MainActivity.class);
                                startActivity(i);
                            }
                            else{
                                try {
                                    throw task.getException();
                                } catch (FirebaseNoSignedInUserException e){
                                    Toast.makeText(Authorization.this, "Пользователя с таким e-mail не существует", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseTooManyRequestsException e) {
                                    Toast.makeText(Authorization.this, "Слишком много запросов, попробуйте позже", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(Authorization.this, "Некорректный e-mail или пароль", Toast.LENGTH_SHORT).show();
                                } catch (Exception e){
                                    Toast.makeText(Authorization.this, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
