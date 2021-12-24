package ru.bmstu.rapirapr.azmetov.akka;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.Route;
import akka.japi.Pair;
import akka.pattern.Patterns;
import akka.routing.RouterActor;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import scala.concurrent.Future;

import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.Directives.*;

public class ResponseTimeApp {
    public static final String ACTOR_SYSTEM_NAME = "ResponseTimeActorSystem";
    public static final Integer HTTP_PORT = 8080;
    public static final String HTTP_HOST = "localhost";
    public static final String SERVER_STARTED_MESSAGE = "Сервер запущен";
    public static final String TESTING_STARTED_RESPONSE = "Тестирование запущено";
    public static final String PACKAGE_ID_PARAMETER_ALIAS = "packageId";

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME);
        ActorRef actor = system.actorOf(Props.create(RouterActor.class));
        ActorMaterializer materializer = ActorMaterializer.create(system);
        ResponseTimeApp app = new ResponseTimeApp();
        final Http http = Http.get(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute(actor).flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(HTTP_HOST, HTTP_PORT),
                materializer
        );
        System.out.println(SERVER_STARTED_MESSAGE);
        System.in.read();
        binding.thenCompose(ServerBinding::unbind).thenAccept(unbound -> system.terminate());
    }

    private Route createRoute(ActorRef actor) {
        return Flow.of(HttpRequest.class).map(
                (request) -> {
                    return new Pair<>(request.)
                }
                get(() -> parameter(PACKAGE_ID_PARAMETER_ALIAS, id -> {
                    Future<Object> result = Patterns.ask(actor, id, 5000);
                    return completeOKWithFuture(result, Jackson.marshaller());
                })),
                post(() -> entity(Jackson.unmarshaller(Message.class), order -> {
                    actor.tell(order, ActorRef.noSender());
                    return complete(TESTING_STARTED_RESPONSE);
                }))
        );
    }
}