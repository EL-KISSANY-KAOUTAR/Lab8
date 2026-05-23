package projet.fst.ma.labthreadsasynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private TextView txtooooooStatussssss;
    private TextView txtooooooProgressValueeeeeee;
    private ProgressBar progressssssssBarre;
    private ImageView imagoooooo;
    private Handler mainnnnnnnnHandlerrrrrrr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Liaison UI avec les IDs personnalisés du XML
        txtooooooStatussssss = findViewById(R.id.txtooooStatusss);
        txtooooooProgressValueeeeeee = findViewById(R.id.txtooProgressValueeee);
        progressssssssBarre = findViewById(R.id.progressooBar);
        imagoooooo = findViewById(R.id.imagooo);

        Button boutonnnnnnnnChargerrrrrrr = findViewById(R.id.boutnLoaddddThreaddd);
        Button boutonnnnnnnnCalculerrrrrrr = findViewById(R.id.boutnCalcAaasync);
        Button boutonnnnnnnnToasttttttttt = findViewById(R.id.boutnToastttt);

        mainnnnnnnnHandlerrrrrrr = new Handler(Looper.getMainLooper());

        // Bouton Toast
        boutonnnnnnnnToasttttttttt.setOnClickListener(v ->
                Toast.makeText(getApplicationContext(), R.string.ui_reactive, Toast.LENGTH_SHORT).show()
        );

        // Bouton Thread
        boutonnnnnnnnChargerrrrrrr.setOnClickListener(v -> loadImageWithThread());

        // Bouton AsyncTask
        boutonnnnnnnnCalculerrrrrrr.setOnClickListener(v -> new HeavyCalcTask(this).execute());
    }

    private void loadImageWithThread() {
        // Reset UI
        progressssssssBarre.setVisibility(View.VISIBLE);
        progressssssssBarre.setProgress(0);
        txtooooooStatussssss.setText(R.string.status_loading_thread);
        txtooooooProgressValueeeeeee.setVisibility(View.VISIBLE);
        txtooooooProgressValueeeeeee.setText("0%");

        new Thread(() -> {
            // Simulation progression 0 -> 100
            for (int i = 0; i <= 100; i++) {
                final int p = i;
                mainnnnnnnnHandlerrrrrrr.post(() -> {
                    progressssssssBarre.setProgress(p);
                    txtooooooProgressValueeeeeee.setText(p + "%");
                });
                try {
                    Thread.sleep(15); // Vitesse de chargement
                } catch (InterruptedException e) {
                    break;
                }
            }

            // Affichage final
            mainnnnnnnnHandlerrrrrrr.post(() -> {
                imagoooooo.setImageResource(R.drawable.iconnn);
                progressssssssBarre.setVisibility(View.INVISIBLE);
                txtooooooProgressValueeeeeee.setVisibility(View.INVISIBLE);
                txtooooooStatussssss.setText(R.string.status_loaded_thread);
            });
        }).start();
    }

    @SuppressWarnings("deprecation")
    private static class HeavyCalcTask extends AsyncTask<Void, Integer, Long> {
        private final WeakReference<MainActivity> activityRef;

        HeavyCalcTask(MainActivity activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            MainActivity activity = activityRef.get();
            if (activity == null) return;
            activity.progressssssssBarre.setVisibility(View.VISIBLE);
            activity.progressssssssBarre.setProgress(0);
            activity.txtooooooProgressValueeeeeee.setVisibility(View.VISIBLE);
            activity.txtooooooProgressValueeeeeee.setText("0%");
            activity.txtooooooStatussssss.setText(R.string.status_calc_async);
        }

        @Override
        protected Long doInBackground(Void... voids) {
            long result = 0;
            for (int i = 1; i <= 100; i++) {
                for (int k = 0; k < 200000; k++) {
                    result += (long) (i * k) % 7;
                }
                publishProgress(i);
                try { Thread.sleep(20); } catch (InterruptedException e) { break; }
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            MainActivity activity = activityRef.get();
            if (activity == null) return;
            int p = values[0];
            activity.progressssssssBarre.setProgress(p);
            activity.txtooooooProgressValueeeeeee.setText(p + "%");
        }

        @Override
        protected void onPostExecute(Long result) {
            MainActivity activity = activityRef.get();
            if (activity == null) return;
            activity.progressssssssBarre.setVisibility(View.INVISIBLE);
            activity.txtooooooProgressValueeeeeee.setVisibility(View.INVISIBLE);
            activity.txtooooooStatussssss.setText(activity.getString(R.string.status_calc_done, result));
        }
    }
}
