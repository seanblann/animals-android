package edu.cnm.deepdive.animals.controller;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.animals.BuildConfig;
import edu.cnm.deepdive.animals.R;
import edu.cnm.deepdive.animals.model.Animal;
import edu.cnm.deepdive.animals.service.WebServiceProxy;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

  private Spinner animalSelector;
  private ArrayAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    animalSelector = findViewById(R.id.animal_selector);
    new RetrieverTask().execute();
  }

  private class RetrieverTask extends AsyncTask<Void, Void, List<Animal>> {


    @Override
    protected List<Animal> doInBackground(Void... voids) {
      try {
        Response<List<Animal>> response = WebServiceProxy.getInstance()
            .getAnimals()
            .execute();
        if (response.isSuccessful()) {
          Log.d(getClass().getName(), response.body().toString());

          return response.body();
        } else {
          Log.e(getClass().getName(), response.message());
          cancel(true);
          return null;
        }
      } catch (IOException e) {
        Log.e(getClass().getName(), e.getMessage(), e);
        cancel(true);
        return null;
      }
    }

    @Override
    protected void onPostExecute(List<Animal> animals) {

      super.onPostExecute(animals);
      String url = animals.get(0).getImageUrl();
      adapter = new ArrayAdapter<>(
          MainActivity.this, R.layout.item_animal_spinner, animals);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
      if (url != null) {
        Picasso.get().load(String.format(BuildConfig.CONTENT_FORMAT, url))
                .into((ImageView) findViewById(R.id.image));
      }
      animalSelector.setAdapter(adapter);
    }
  }
}

