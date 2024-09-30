package ch.heigvd;

import io.javalin.http.Context;

public class HelloWorldController {
    private record HomeResult(
            String about,
            String documentation,
            String name
    ) {

    }


    public void hello(Context ctx) {
        ctx.json(new HomeResult(
                "Welcome traveler!",
                "https://icecreamtruck.lol/docs",
                "Icecream Truck Simulator"
        ));
    }
}
