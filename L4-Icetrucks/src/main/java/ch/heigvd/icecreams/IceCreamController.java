package ch.heigvd.icecreams;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class IceCreamController {

    private final ConcurrentHashMap<Integer, IceCream> iceCreams;
    private final AtomicInteger nextId = new AtomicInteger(1);

    public IceCreamController(ConcurrentHashMap<Integer, IceCream> iceCreams) {
        this.iceCreams = iceCreams;
    }

    public void create(Context ctx) {
        IceCream iceCream = ctx.bodyAsClass(IceCream.class);
        iceCream.setId(nextId.getAndIncrement());

        iceCreams.put(iceCream.getId(), iceCream);

        ctx.json(iceCream);
        ctx.status(HttpStatus.CREATED);
    }

    public void findOneById(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        IceCream iceCream = iceCreams.get(id);
        if (iceCream == null) {
            throw new NotFoundResponse();
        }

        ctx.json(iceCream);
        ctx.status(HttpStatus.OK);
    }

    public void updateOneById(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        IceCream updateIceCreamDto = ctx.bodyValidator(IceCream.class).get();

        IceCream iceCream = iceCreams.getOrDefault(id, new IceCream());

        iceCream.setName(updateIceCreamDto.getName());
        iceCream.setFlavor(updateIceCreamDto.getFlavor());

        iceCreams.put(id, iceCream);

        ctx.json(iceCream);
        ctx.status(HttpStatus.OK);
    }

    public void findAll(Context ctx) {
        var iceCreams = new ArrayList<>(this.iceCreams.values());

        ctx.json(iceCreams);
        ctx.status(HttpStatus.OK);
    }

    public void deleteOneById(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        IceCream iceCream = iceCreams.get(id);
        if (iceCream == null) {
            throw new NotFoundResponse();
        }

        iceCreams.remove(id);

        ctx.json(iceCream);
        ctx.status(HttpStatus.OK);
    }
}
