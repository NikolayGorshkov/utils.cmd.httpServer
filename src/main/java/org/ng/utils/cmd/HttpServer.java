package org.ng.utils.cmd;

import java.nio.file.Paths;

import org.jboss.logging.Logger;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

@QuarkusMain
public class HttpServer implements QuarkusApplication {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    private final Logger log = Logger.getLogger(getClass());

    @Override
    public int run(String... args) throws Exception {
        String host;
        int port;
        if (args.length > 0) {
            String arg = args[0];
            if (arg.matches("\\d+")) {
                port = Integer.parseInt(arg);
                host = DEFAULT_HOST;
            } else if (arg.matches("\\S+:\\d+")) {
                int cIdx = arg.lastIndexOf(':');
                host = arg.substring(0, cIdx);
                port = Integer
                        .parseInt(arg.substring(cIdx + 1));
            } else {
                return usage();
            }
        } else {
            port = DEFAULT_PORT;
            host = DEFAULT_HOST;
        }

        Vertx vertx = Vertx.vertx();

        Router router = Router.router(vertx);
        router.route().handler(StaticHandler.create()
                .setWebRoot(".").setCachingEnabled(false)
                .setIncludeHidden(true)
                .setDirectoryListing(true));

        vertx.exceptionHandler(this::handleExceptionCaught)
                .createHttpServer().requestHandler(router)
                .exceptionHandler(
                        this::handleExceptionCaught)
                .listen(port, host, asyncResult -> {
                    asyncResult.map(server -> {
                        return server;
                    });
                    log.info("Listening on " + host + ":"
                            + port);
                    log.info("Current directory: " + Paths
                            .get(".").toAbsolutePath());
                });

        synchronized (this) {
            this.wait();
        }
        return 0;
    }

    private int usage() {
        log.error("Usage: \n" //
                + "1. server # for default localhost:8080 \n" //
                + "2. server 8081 # for localhost and port 8081 \n" //
                + "3. server 0.0.0.0:8081 # for defining host and port to listen on" //
        );
        return 1;
    }

    private void handleExceptionCaught(Throwable e) {
        log.error("Server execution error", e);
    }

}