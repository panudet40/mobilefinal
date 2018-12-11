package a59070130.kmitl.ac.th.mobilefinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {
    private SharedPreferences sharedRF;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
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
            Log.d("HOME", "NullPointerException : " + e.getMessage());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initProfileBtn();
        initMyfriendsBtn();
        initSignOutBtn();
    }

    private void initSignOutBtn() {
        Button btn = getView().findViewById(R.id.fragment_home_sign_out);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HOME", "sign out");
                sharedRF.edit()
                        .clear()
                        .apply();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new LoginFragment())
                        .commit();
            }
        });
    }

    private void initMyfriendsBtn() {
        Button btn = getView().findViewById(R.id.fragment_home_my_friends);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HOME", "go to my friends");
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new MyFriendsFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void initProfileBtn() {
        Button btn = getView().findViewById(R.id.fragment_home_profile_setup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("final", "go to profile");
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new ProfileSetupFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
