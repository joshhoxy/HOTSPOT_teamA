package com.example.se_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;

    EditText et_id;
    EditText et_pass;
    Button btn_login;
    Button btn_register;

    String sql;
    Cursor cursor;

    SharedPreferences sh_Pref;
    SharedPreferences.Editor toEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = (EditText) findViewById(R.id.et_id);
        et_pass = (EditText) findViewById(R.id.et_pass);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);

        helper = new DatabaseOpenHelper(LoginActivity.this, DatabaseOpenHelper.tableName, null, version);
        database = helper.getWritableDatabase();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = et_id.getText().toString();
                String pw = et_pass.getText().toString();


                if (len_check(id, pw)) {
                    //아이디와 비밀번호를 입력하지 않았을 때
                    Toast toast = Toast.makeText(LoginActivity.this, "아이디와 비밀번호는 필수 입력사항입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                    //}
                }

                sql = "SELECT id FROM " + helper.tableName + " WHERE id = '" + id + "'";
                cursor = database.rawQuery(sql, null);

                //if (id_check(cursor.getCount())) {
                if (cursor.getCount() != 1) {
                    //아이디가 틀렸을 때
                    Toast toast = Toast.makeText(LoginActivity.this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                sql = "SELECT pw FROM " + helper.tableName + " WHERE id = '" + id + "'";
                cursor = database.rawQuery(sql, null);

                cursor.moveToNext();

                if (!pw_check(pw, cursor.getString(0))){
                    //비밀번호 틀렸을 때 {

                    Toast toast = Toast.makeText(LoginActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    Toast toast = Toast.makeText(LoginActivity.this, "로그인성공", Toast.LENGTH_SHORT);
                    toast.show();
                    sharedPrefernces();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("ID", id);
                    startActivity(intent);
                    finish();
                }

            }
        });

        applySharedPreference();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);

            }
        });
    }

    public void sharedPrefernces() {
        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();
        toEdit.putString("Username", et_id.getText().toString());
        toEdit.putString("Password", et_pass.getText().toString());
        toEdit.commit(); // 데이터베이스에서 아이디, 비밀번호 일치여부 확인
    }

    public void applySharedPreference() {
        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        if (sh_Pref != null && sh_Pref.contains("Username")) {
            String name = sh_Pref.getString("Username", "noname");
            et_id.setText(name);
        }

    }

    public boolean len_check(String id, String pw) {
        return id.length() == 0 || pw.length() == 0;
    }

    public boolean pw_check(String pw, String pw1) {
        return pw.equals(pw1);
    }

}