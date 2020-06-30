package com.example.se_project;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;


    private EditText et_id, et_pass, et_email, et_passck;
    private Button btn_register;

    String sql;
    Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_id = (EditText) findViewById(R.id.et_id);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_email = (EditText) findViewById(R.id.et_email);
        et_passck = (EditText) findViewById(R.id.et_passck);
        btn_register = (Button) findViewById(R.id.btn_register);

        helper = new DatabaseOpenHelper(RegisterActivity.this,
                DatabaseOpenHelper.tableName, null, version);
        database = helper.getWritableDatabase();

        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String id = et_id.getText().toString();
                String pw = et_pass.getText().toString();
                String pwck = et_passck.getText().toString();
                String email = et_email.getText().toString();

                if(len_check(id, pw)) {
                    //아이디와 비밀번호는 필수 입력사항입니다.
                    Toast toast = Toast.makeText(RegisterActivity.this, "아이디와 비밀번호는 필수 입력사항입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                sql = "SELECT id FROM "+ helper.tableName + " WHERE id = '" + id + "'";
                cursor = database.rawQuery(sql, null);

                if(cursor.getCount() != 0){
                    //존재하는 아이디입니다.
                    Toast toast = Toast.makeText(RegisterActivity.this, "존재하는 아이디입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                else if(!pw_check(pw, pwck)){
                    Toast toast = Toast.makeText(RegisterActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                else{
                    helper.insertUser(database,id,pw, email);
                    Toast toast = Toast.makeText(RegisterActivity.this, "가입이 완료되었습니다. 로그인을 해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public boolean pw_check(String pw, String pwck){
        return pw.equals(pwck);
    }

    public boolean len_check(String id, String pw) {
        return id.length() == 0 || pw.length() == 0;
    }
}
