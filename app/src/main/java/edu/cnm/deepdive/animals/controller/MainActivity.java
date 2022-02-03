package edu.cnm.deepdive.animals.controller;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.animals.BuildConfig;
import edu.cnm.deepdive.animals.R;
import edu.cnm.deepdive.animals.model.Animal;
import edu.cnm.deepdive.animals.service.WebServiceProxy;
import edu.cnm.deepdive.animals.viewmodel.MainViewModel;
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
    animalSelector.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            Animal animal = (Animal) adapterView.getItemAtPosition(position);
            if (animal.getImageUrl() != null) {
                  Picasso.get().load(String.format(BuildConfig.CONTENT_FORMAT, animal.getImageUrl()))
                        .into((ImageView) findViewById(R.id.image));
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });
    MainViewModel viewModel = new ViewModelProvider(this)
        .get(MainViewModel.class);
    getLifecycle().addObserver(viewModel);
    viewModel.getAnimals().observe(this, new Observer<List<Animal>>() {
      @Override
      public void onChanged(List<Animal> animals) {
          ArrayAdapter<Animal> adapter = new ArrayAdapter<>(
              MainActivity.this, R.layout.item_animal_spinner, animals);
          adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
          animalSelector.setAdapter(adapter);
      }
    });
    viewModel.getThrowable().observe(this, new Observer<Throwable>() {
      @Override
      public void onChanged(Throwable throwable) {
          Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
        Snackbar
            .make(MainActivity.this.findViewById(R.id.image), throwable.getMessage(),
                  BaseTransientBottomBar.LENGTH_INDEFINITE)
            .show();
      }
    });
  }
}

