package com.dedigo.pmdm06_juego;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends Activity {

    private Toast toast;
    // Botones del tablero
    private ImageButton imb00, imb01, imb02,
            imb10, imb11, imb12,
            imb20, imb21, imb22,
            imb30, imb31, imb32;

    private ImageButton[] tablero = new ImageButton[12];
    // Textos
    private TextView txtRecord, txtIntentos, txtAciertos;
    // Contadores
    private int record, intentos, aciertos;
    // Imágenes y sonidos
    private int[] imagenes;
    private int[] sonidos;
    private int fondo;

    // Variables del juego
    private ArrayList<Integer> arrayDesordenado;
    private ImageButton primero, segundo;
    private int numeroPrimero, numeroSegundo;
    private boolean bloqueo = false;

    private Bundle datos;
    private final Handler temporizador = new Handler();

    // =========================
    // onCreate
    // =========================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        datos = getIntent().getExtras();
        record = datos.getInt("record");

        iniciar();
    }

    // =========================
    // Cargar tablero
    // =========================
    private void cargarTablero() {
        tablero[0] = imb00 = findViewById(R.id.imb00);
        tablero[1] = imb01 = findViewById(R.id.imb01);
        tablero[2] = imb02 = findViewById(R.id.imb02);
        tablero[3] = imb10 = findViewById(R.id.imb10);
        tablero[4] = imb11 = findViewById(R.id.imb11);
        tablero[5] = imb12 = findViewById(R.id.imb12);
        tablero[6] = imb20 = findViewById(R.id.imb20);
        tablero[7] = imb21 = findViewById(R.id.imb21);
        tablero[8] = imb22 = findViewById(R.id.imb22);
        tablero[9] = imb30 = findViewById(R.id.imb30);
        tablero[10] = imb31 = findViewById(R.id.imb31);
        tablero[11] = imb32 = findViewById(R.id.imb32);
    }

    // =========================
    // Cargar textos
    // =========================
    private void cargarTextos() {
        intentos = 0;
        aciertos = 0;

        txtRecord = findViewById(R.id.txtRecord);
        txtIntentos = findViewById(R.id.txtIntentos);
        txtAciertos = findViewById(R.id.txtAciertos);

        txtRecord.setText("" + record);
        txtIntentos.setText("" + intentos);
        txtAciertos.setText("" + aciertos);
    }

    // =========================
    // Cargar imágenes
    // =========================
    private void cargarImagenes() {
        imagenes = new int[]{
                R.drawable.caballo,
                R.drawable.gato,
                R.drawable.cerdo,
                R.drawable.pato,
                R.drawable.perro,
                R.drawable.vaca
        };
        fondo = R.drawable.cara;
    }

    // =========================
    // Cargar sonidos
    // =========================
    private void cargarSonidos() {
        sonidos = new int[]{
                R.raw.caballo,
                R.raw.gato,
                R.raw.cerdo,
                R.raw.pato,
                R.raw.perro,
                R.raw.vaca
        };
    }

    // =========================
    // Barajar cartas
    // =========================
    private ArrayList<Integer> barajar(int longitud) {
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < longitud * 2; i++) {
            result.add(i % longitud);
        }

        Collections.shuffle(result);
        return result;
    }

    // =========================
    // Comprobar selección
    // =========================
    private void comprobarSeleccion(int i, final ImageButton imb) {

        MediaPlayer.create(this, sonidos[arrayDesordenado.get(i)]).start();

        @SuppressLint("ResourceType")
        Animator animator = AnimatorInflater.loadAnimator(this, R.anim.rotar_y);
        animator.setTarget(imb);
        animator.start();

        if (primero == null) {

            primero = imb;
            primero.setScaleType(ImageView.ScaleType.CENTER_CROP);
            primero.setImageResource(imagenes[arrayDesordenado.get(i)]);
            primero.setEnabled(false);
            numeroPrimero = arrayDesordenado.get(i);

        } else {

            bloqueo = true;

            segundo = imb;
            segundo.setScaleType(ImageView.ScaleType.CENTER_CROP);
            segundo.setImageResource(imagenes[arrayDesordenado.get(i)]);
            segundo.setEnabled(false);
            numeroSegundo = arrayDesordenado.get(i);

            intentos++;
            txtIntentos.setText("" + intentos);

            if (numeroPrimero == numeroSegundo) {

                primero = null;
                segundo = null;
                bloqueo = false;

                aciertos++;
                txtAciertos.setText("" + aciertos);

                if (aciertos == imagenes.length) {

                    toast = Toast.makeText(getApplicationContext(),
                            "Enhorabuena!\nHas ganado.", Toast.LENGTH_LONG);
                    toast.show();

                    temporizador.postDelayed(() -> {
                        Intent data = new Intent();

                        if (intentos < record || record == 0) {
                            data.putExtra("record", intentos);
                            setResult(RESULT_OK, data);

                            Toast.makeText(getApplicationContext(),
                                    "¡¡¡Nuevo récord!!!", Toast.LENGTH_LONG).show();
                        } else {
                            data.putExtra("record", record);
                            setResult(RESULT_OK, data);
                        }
                        finish();
                    }, 2000);
                }

            } else {

                temporizador.postDelayed(() -> {
                    primero.setImageResource(fondo);
                    primero.setEnabled(true);

                    segundo.setImageResource(fondo);
                    segundo.setEnabled(true);

                    primero = null;
                    segundo = null;
                    bloqueo = false;
                }, 500);
            }
        }
    }

    // =========================
    // Iniciar juego
    // =========================
    private void iniciar() {

        cargarTablero();
        cargarTextos();
        cargarImagenes();
        cargarSonidos();

        arrayDesordenado = barajar(imagenes.length);

        for (ImageButton carta : tablero) {
            carta.setScaleType(ImageView.ScaleType.CENTER_CROP);
            carta.setImageResource(fondo);
        }

        for (int i = 0; i < tablero.length; i++) {
            final int finalI = i;
            tablero[i].setEnabled(true);
            tablero[i].setOnClickListener(view -> {
                if (!bloqueo) {
                    comprobarSeleccion(finalI, tablero[finalI]);
                }
            });
        }
    }
}
