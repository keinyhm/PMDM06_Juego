package com.dedigo.pmdm06_juego;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity{
    // variables
    private Button btJugar;
    private TextView txtRecord;
    private Toast toast;
    private int record = 0;

    // para recibir el resultado del juego
    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            //  variable resultado
                            int resultado = activityResult.getResultCode();
                            //  variable data
                            Intent data = activityResult.getData();

                            // condición RESULT_OK
                            if (resultado == RESULT_OK && data != null) {
                                record = data.getIntExtra("record", 0);
                                txtRecord.setText("" + record);
                            }
                        }
                    }
            );

    // método onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // conectar elementos visuales
        txtRecord = findViewById(R.id.txtRecord);
        txtRecord.setText("" + record);

        btJugar = findViewById(R.id.btnJugar);

        // preparar botón Jugar
        btJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast = Toast.makeText(getApplicationContext(), "A jugar!", Toast.LENGTH_SHORT);
                toast.show(); // mmestra el mensaje
                jugar();
            }
        });
    }

    // método jugar() para lanzar la actividad del juego
    public void jugar() {
        Intent gameActivity = new Intent(this, GameActivity.class);
        gameActivity.putExtra("record", record);
        activityResultLauncher.launch(gameActivity);
    }


}
