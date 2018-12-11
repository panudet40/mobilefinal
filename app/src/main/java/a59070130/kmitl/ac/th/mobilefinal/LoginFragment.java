package a59070130.kmitl.ac.th.mobilefinal;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    private SQLiteDatabase myDB;
    SharedPreferences sharedRF;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            sharedRF = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        }
        catch (NullPointerException e)
        {
            Log.d("LOGIN", "NullPointerException : " + e.getMessage());
        }
        myDB = getActivity().openOrCreateDatabase("my.db", Context.MODE_PRIVATE, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY AUTOINCREMENT, userId VARCHAR(12), name VARCHAR(50), age INTEGER, password VARCHAR(30))");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userPermission();
        checkLogin();
        initLoginBtn();
        initRegisterBtn();


    }

    private void checkLogin() {
        try
        {
            if (!sharedRF.getString("user id", "not found").equals("not found")) {
                Log.d("LOGIN", "already login");
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new HomeFragment())
                        .commit();
            }
        }
        catch (NullPointerException e)
        {
            Log.d("final", ".equals return NullPointerException : " + e.getMessage());
        }
    }

    private void userPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    private void initRegisterBtn() {
        TextView btn = getView().findViewById(R.id.fragment_login_register_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new RegisterFragment())
                        .commit()
                ;
            }
        });
    }

    private void initLoginBtn() {
        Button btn = getView().findViewById(R.id.fragment_login_login_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userId = getView().findViewById(R.id.fragment_login_user_id);
                EditText password = getView().findViewById(R.id.fragment_login_password);

                String userIdStr = userId.getText().toString();
                String passwordStr = password.getText().toString();

                if (userIdStr.isEmpty() || passwordStr.isEmpty()) {
                    Log.d("LOGIN", "username or password not exists");
                    Toast.makeText(getActivity(), "Please fill out this\n" +
                            "form", Toast.LENGTH_SHORT).show();
                } else {

                    Cursor cursor = myDB.rawQuery("SELECT userId, name, age, password FROM user WHERE userId = '" + userIdStr + "' AND password = '" + passwordStr + "'", null);
                    if (cursor.moveToNext())
                    {
                        Log.d("LOGIN", "Login Success");
                        sharedRF.edit()
                                .putString("user id", cursor.getString(0))
                                .putString("name", cursor.getString(1))
                                .putInt("age", cursor.getInt(2))
                                .putString("password", cursor.getString(3))
                                .apply();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_view, new HomeFragment())
                                .commit();
                        Toast.makeText(getContext(), "login success", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Log.d("LOGIN", "Login Failed");
                        Toast.makeText(getContext(), "Invalid user or password", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }




}
