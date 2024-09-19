package com.example.cobalagi;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Grafik_data extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private LineChart lineChartSuhu, lineChartKelembapan;
    private ArrayList<Entry> kelembapanEntries = new ArrayList<>();
    private ArrayList<Entry> suhuEntries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grafik_data);

        // Mengatur layout agar sesuai dengan insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi LineChart untuk suhu dan kelembapan
        lineChartSuhu = findViewById(R.id.chart1); // Untuk suhu
        lineChartKelembapan = findViewById(R.id.chart2); // Untuk kelembapan

        // Inisialisasi Firebase Database Reference
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Motion");

        // Mendengarkan data dari Firebase Realtime Database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Mengambil nilai kelembapan dan suhu
                Integer kelembapan = dataSnapshot.child("kelembapan").getValue(Integer.class);
                Integer suhu = dataSnapshot.child("suhu").getValue(Integer.class);

                if (kelembapan != null && suhu != null) {
                    // Menampilkan data pada log untuk memastikan
                    Log.d("FirebaseData", "Kelembapan: " + kelembapan + ", Suhu: " + suhu);

                    // Mengupdate data di LineChart
                    updateChartData(kelembapan, suhu);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Menangani jika terjadi error saat mengakses Firebase
                Log.w("FirebaseError", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    // Fungsi untuk memperbarui data di LineChart
    private void updateChartData(int kelembapan, int suhu) {
        // Menambahkan data baru ke entries kelembapan dan suhu
        kelembapanEntries.add(new Entry(kelembapanEntries.size(), kelembapan));
        suhuEntries.add(new Entry(suhuEntries.size(), suhu));

        // Membuat LineDataSet untuk kelembapan
        LineDataSet kelembapanDataSet = new LineDataSet(kelembapanEntries, "Kelembapan");
        kelembapanDataSet.setColor(android.graphics.Color.parseColor("#3a6ea5")); // Warna khusus untuk kelembapan
        kelembapanDataSet.setValueTextColor(android.graphics.Color.BLACK); // Warna teks hitam
        kelembapanDataSet.setValueTextSize(12f); // Ukuran teks
        kelembapanDataSet.setDrawCircles(false); // Nonaktifkan titik data
        kelembapanDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Membuat garis lebih halus

        // Membuat LineDataSet untuk suhu
        LineDataSet suhuDataSet = new LineDataSet(suhuEntries, "Suhu");
        suhuDataSet.setColor(android.graphics.Color.parseColor("#004e98")); // Warna biru untuk suhu
        suhuDataSet.setValueTextColor(android.graphics.Color.BLACK); // Warna teks hitam
        suhuDataSet.setValueTextSize(12f); // Ukuran teks
        suhuDataSet.setDrawCircles(false); // Nonaktifkan titik data
        suhuDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Membuat garis lebih halus

        // Nonaktifkan nilai di semua titik kecuali titik terakhir
        suhuDataSet.setDrawValues(false); // Nonaktifkan nilai untuk semua titik
        if (!suhuEntries.isEmpty()) {
            ArrayList<Entry> lastSuhuEntry = new ArrayList<>();
            lastSuhuEntry.add(suhuEntries.get(suhuEntries.size() - 1)); // Ambil hanya titik terakhir
            LineDataSet suhuLastValueSet = new LineDataSet(lastSuhuEntry, "Nilai Akhir Suhu");
            suhuLastValueSet.setValueTextColor(android.graphics.Color.RED); // Warna teks di ujung garis
            suhuLastValueSet.setValueTextSize(12f); // Ukuran teks di ujung garis
            suhuLastValueSet.setDrawCircles(true); // Tampilkan lingkaran di titik terakhir
            suhuLastValueSet.setCircleColor(android.graphics.Color.RED); // Warna lingkaran titik terakhir

            // Menggabungkan data suhu untuk menampilkan nilai hanya di titik terakhir
            LineData lineDataSuhu = new LineData(suhuDataSet, suhuLastValueSet);

            // Mengatur LineData ke chart
            lineChartSuhu.setData(lineDataSuhu); // Untuk suhu
        }

        // Mengatur LineData untuk kelembapan tanpa nilai khusus di ujung
        LineData lineDataKelembapan = new LineData(kelembapanDataSet);
        lineChartKelembapan.setData(lineDataKelembapan); // Untuk kelembapan

        // Refresh chart
        lineChartKelembapan.invalidate();
        lineChartSuhu.invalidate();
    }

}

