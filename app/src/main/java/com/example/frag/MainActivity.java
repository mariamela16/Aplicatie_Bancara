package com.example.frag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.frag.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {



    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String number=intent.getStringExtra("number");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundlex=new Bundle();
        bundlex.putString("number",number);
        ProfileFragment fragx=new ProfileFragment();
        fragx.setArguments(bundlex);
        replaceFragment(fragx);

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.prof:
                    Bundle bundle=new Bundle();
                    bundle.putString("number",number);
                    ProfileFragment frag1=new ProfileFragment();
                    frag1.setArguments(bundle);
                    replaceFragment(frag1);
                    break;

                case R.id.depozite:
                    Bundle bundle2=new Bundle();
                    bundle2.putString("number",number);
                    DepozitFragment frag2=new DepozitFragment();
                    frag2.setArguments(bundle2);
                    replaceFragment(frag2);
                    break;

                case R.id.tranzactii:
                    Bundle bundle3=new Bundle();
                    bundle3.putString("number",number);
                    TranzactiiFragment frag3=new TranzactiiFragment();
                    frag3.setArguments(bundle3);
                    replaceFragment(frag3);
                    break;

                    case R.id.grafic:
                    Bundle bundle5=new Bundle();
                    bundle5.putString("number",number);
                    GraficFragment frag5=new GraficFragment();
                    frag5.setArguments(bundle5);
                    replaceFragment(frag5);
                    break;

                case R.id.setari:
                    Bundle bundle4=new Bundle();
                    bundle4.putString("number",number);
                    SetariFragment frag4=new SetariFragment();
                    frag4.setArguments(bundle4);
                    replaceFragment(frag4);
                    break;
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == MainActivity.RESULT_OK) {
            // Revenire la fragmentul inițial
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = fragmentManager.findFragmentByTag("fragment_initial"); // înlocuiește "fragment_initial" cu tag-ul setat pentru fragmentul inițial
            fragmentTransaction.replace(R.id.setari, fragment);
            fragmentTransaction.commit();
        }
    }

}