package com.example.ayobisaaa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import com.example.ayobisaaa.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public ObservableField<String> kelembapan = new ObservableField<>();
    public ObservableField<String> suhu = new ObservableField<>();
    private ActivityMainBinding binding;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Motion");
//        readSensorData();
    }

    public void onClickGoToPage2() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);

    }

    public void onClikgotopageimage() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    private void readSensorData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Mendapatkan nilai kelembapan dan suhu dari Firebase
                String kelembapanValue = snapshot.child("kelembapan").getValue(String.class);
                String suhuValue = snapshot.child("suhu").getValue(String.class);

                // Mengupdate nilai yang terikat dengan UI
                kelembapan.set("Kelembapan: " + kelembapanValue);
                suhu.set("Suhu: " + suhuValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Gagal membaca nilai.", error.toException());
            }
        });
    }


}
