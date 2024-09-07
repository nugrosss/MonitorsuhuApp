package com.example.ayobisaaa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import com.example.ayobisaaa.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView numberPH, numberPWM;

    public ObservableField<String> kelembapan = new ObservableField<>();
    public ObservableField<String> suhu = new ObservableField<>();
    private ActivityMainBinding binding;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        numberPH = findViewById(R.id.textView1);
        numberPWM = findViewById(R.id.textViewPWM);

        databaseReference = FirebaseDatabase.getInstance().getReference("Motion");
        readSensorData();
    }

    public void showUserData() {
        Intent intent = getIntent();
        String nnumberPWM = intent.getStringExtra("suhu");
        String nnumberPH = intent.getStringExtra("kelembapan");

        numberPWM.setText(nnumberPWM);
        numberPH.setText(nnumberPH);
    }

    public void passUserData() {
        // Referensi langsung ke node "Motion"
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Motion");

        // Mengambil data langsung dari node "kelembapan" dan "suhu"
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Mengambil nilai kelembapan dan suhu dari Firebase
                    String kelembapanFromDB = snapshot.child("kelembapan").getValue(String.class);
                    String suhuFromDB = snapshot.child("suhu").getValue(String.class);


                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
                    intent.putExtra("kelembapan", kelembapanFromDB);
                    intent.putExtra("suhu", suhuFromDB);

                    // Memulai activity baru
                    startActivity(intent);
                } else {
                    Log.w("Firebase", "Data Motion tidak ditemukan");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Gagal membaca data", error.toException());
            }
        });
    }


    private void readSensorData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get the "kelembapan" and "suhu" values from Firebase
                String kelembapanValue = snapshot.child("kelembapan").getValue(String.class);
                String suhuValue = snapshot.child("suhu").getValue(String.class);

                // Update the values that are bound to the UI
                kelembapan.set("Kelembapan: " + kelembapanValue);
                suhu.set("Suhu: " + suhuValue);

                // Optionally, display these values in TextViews if you have UI elements for them
                numberPH.setText(kelembapanValue);
                numberPWM.setText(suhuValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Failed to read data", error.toException());
            }
        });
    }

    public void onClickGoToPage2() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    public void onClikgotopageimage() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}
