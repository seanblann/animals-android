package edu.cnm.deepdive.animals.service;

import android.content.Context;
import edu.cnm.deepdive.animals.model.Animal;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import retrofit2.Response;

public class AnimalRepository {

  private final Context context;

  public AnimalRepository(Context context) {
    this.context = context;
  }

  public Single<List<Animal>> getAnimals() {
      return WebServiceProxy
              .getInstance()
              .getAnimals()
              .subscribeOn(Schedulers.io());
  }


}
