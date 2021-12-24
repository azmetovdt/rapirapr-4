package ru.bmstu.rapirapr.azmetov.akka;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.japi.Pair;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class ResponseTimeApp {
    public static final String ACTOR_SYSTEM_NAME = "ResponseTimeActorSystem";
    public static final Integer HTTP_PORT = 8080;
    public static final String HTTP_HOST = "localhost";
    public static final String SERVER_STARTED_MESSAGE = "Сервер запущен";
    public static final String TESTING_STARTED_RESPONSE = "Тестирование запущено";
    public static final String PACKAGE_ID_PARAMETER_ALIAS = "packageId";

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME);
        ActorRef actor = system.actorOf(Props.create(StoreActor.class));
        ActorMaterializer materializer = ActorMaterializer.create(system);
        ResponseTimeApp app = new ResponseTimeApp();
        final Http http = Http.get(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = createRoute(actor);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(HTTP_HOST, HTTP_PORT),
                materializer
        );
        System.out.println(SERVER_STARTED_MESSAGE);
        System.in.read();
        binding.thenCompose(ServerBinding::unbind).thenAccept(unbound -> system.terminate());
    }

    private static Flow<HttpRequest, HttpResponse, NotUsed> createRoute(ActorRef actor) {
        return Flow.of(HttpRequest.class)
                .map((request) -> {
                    final Query query = request.getUri().query();
                    return new Pair<String, Integer>(
                            String.valueOf(query.get("url")),
                            Integer.parseInt(String.valueOf(query.get("count")))
                    );
                })
                .mapAsync(1, pair -> {
                    CompletionStage<Object> savedResult = Patterns.ask(actor, new Message(""), Duration.ofSeconds(5));
                    return savedResult.thenCompose(
                            result -> {
                                if (Collections.singletonList(result).toArray().length > 0) {
                                    return CompletableFuture.completedFuture(result);
                                }
                                return null;
                            }
                    );
                })
        return null;
    });


}
}