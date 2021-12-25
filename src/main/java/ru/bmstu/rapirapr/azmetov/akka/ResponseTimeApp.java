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
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import scala.concurrent.Future;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.Directives.*;

public class ResponseTimeApp {
    public static final String ACTOR_SYSTEM_NAME = "ResponseTimeActorSystem";
    public static final Integer HTTP_PORT = 8080;
    public static final String HTTP_HOST = "localhost";
    public static final String SERVER_STARTED_MESSAGE = "Сервер запущен";
    public static final String URL_QUERY_PARAMETER_ALIAS = "url";
    public static final String COUNT_QUERY_PARAMETER_ALIAS = "count";
    public static final String HTTP_RESPONSE_PREFIX = "Среднее время ответа: ";

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME);
        ActorRef actor = system.actorOf(Props.create(StoreActor.class));
        ActorMaterializer materializer = ActorMaterializer.create(system);
        final Http http = Http.get(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = createRoute(actor, http).flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(HTTP_HOST, HTTP_PORT),
                materializer
        );
        System.out.println(SERVER_STARTED_MESSAGE);
        System.in.read();
        binding.thenCompose(ServerBinding::unbind).thenAccept(unbound -> system.terminate());
    }

    private static Route createRoute(ActorRef actor, Http http) {
        return route(
                get(() -> parameter(URL_QUERY_PARAMETER_ALIAS, url ->
                        parameter(COUNT_QUERY_PARAMETER_ALIAS, count -> {
                            if (Integer.parseInt(count) > 0) {
                                return completeWithFuture(fetch(http, url));
                            }
                            return completeWithFuture(
                                    Patterns.ask(actor, new RandomHostMessage(), Duration.ofSeconds(5))
                                            .thenCompose(msg -> {
                                                HostMessage m = (HostMessage) msg;
                                                return fetch(http, m.getHost() + url + "/" + count.toString());
                                            })
                            );
                        })
                )));
    }

    private static CompletionStage<HttpResponse> fetch(Http http, String url) {
        return http.singleRequest(HttpRequest.create(url));
    }
}

