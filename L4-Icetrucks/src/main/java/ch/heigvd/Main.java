package ch.heigvd;

import ch.heigvd.icecreams.IceCream;
import ch.heigvd.icecreams.IceCreamController;
import ch.heigvd.trucks.Truck;
import ch.heigvd.trucks.TrucksController;
import io.javalin.Javalin;
import io.javalin.openapi.plugin.redoc.ReDocConfiguration;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;

import java.util.concurrent.ConcurrentHashMap;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
  public final static int PORT = 8080;

  public static void main(String[] args) {
    Javalin app = Javalin.create(config -> {
      config.staticFiles.add("/web");
      ReDocConfiguration configuration = new ReDocConfiguration();
      configuration.setDocumentationPath("/openapi.yaml");
      configuration.setUiPath("/docs");
      config.plugins.register(new ReDocPlugin(configuration));
    });

    HelloWorldController helloWorldController = new HelloWorldController();

    app.get("/", helloWorldController::hello);

    ConcurrentHashMap<Integer, IceCream> iceCreams = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, Truck> trucks = new ConcurrentHashMap<>();

    IceCreamController iceCreamController = new IceCreamController(iceCreams);
    TrucksController trucksController = new TrucksController(trucks);

    app.routes(() -> {
      path("icecreams", () -> {
        post(iceCreamController::create);
        get(iceCreamController::findAll);

        path("{id}", () -> {
          get(iceCreamController::findOneById);
          put(iceCreamController::updateOneById);
          delete(iceCreamController::deleteOneById);
        });
      });

      path("trucks", () -> {
        post(trucksController::create);
        get(trucksController::findAll);

        path("{id}", () -> {
          get(trucksController::findOneById);
          put(trucksController::putOneById);
          delete(trucksController::deleteOneById);
        });
      });
    });

    app.start(PORT);
  }
}
