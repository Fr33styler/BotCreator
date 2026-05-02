package ro.fr33styler.botcreator.launcher;

import io.netty.channel.EventLoopGroup;
import ro.fr33styler.botcreator.bot.Bot;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BotLauncher {

    private static final long LOGIN_TIMEOUT_MS = 30000L;
    private static final long CONNECT_TIMEOUT_MS = 5000L;
    private static final long LOGIN_POLL_MS = 50L;

    private final Collection<Bot> bots;
    private final EventLoopGroup workerGroup;
    private final String host;
    private final int port;
    private final int joinDelay;
    private final int retryDelay;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public BotLauncher(Collection<Bot> bots, EventLoopGroup workerGroup, String host, int port, int joinDelay, int retryDelay) {
        this.bots = bots;
        this.workerGroup = workerGroup;
        this.host = host;
        this.port = port;
        this.joinDelay = joinDelay;
        this.retryDelay = retryDelay;
    }

    public void start() {
        scheduler.submit(this::runCycle);
    }

    public void stop() {
        scheduler.shutdownNow();
    }

    private void runCycle() {
        for (Bot bot : bots) {
            if (bot.isLoggedIn()) continue;

            if (!bot.isOnline()) {
                bot.connect(workerGroup, host, port);
            }

            long now = System.currentTimeMillis();
            awaitLogin(bot, now + LOGIN_TIMEOUT_MS, now + CONNECT_TIMEOUT_MS, false);
            return;
        }

        scheduler.schedule(this::runCycle, retryDelay, TimeUnit.MILLISECONDS);
    }

    private void awaitLogin(Bot bot, long deadline, long connectDeadline, boolean wasOnline) {
        long now = System.currentTimeMillis();

        if (bot.isLoggedIn()) {
            scheduler.schedule(this::runCycle, joinDelay, TimeUnit.MILLISECONDS);
            return;
        }

        boolean isOnline = bot.isOnline();
        if (!isOnline && (wasOnline || now >= connectDeadline)) {
            onLoginFailed(bot);
            return;
        }

        if (now >= deadline) {
            onLoginFailed(bot);
            return;
        }

        scheduler.schedule(() -> awaitLogin(bot, deadline, connectDeadline, isOnline), LOGIN_POLL_MS, TimeUnit.MILLISECONDS);
    }

    private void onLoginFailed(Bot bot) {
        bot.getLogger().warning("Did not finish logging in, retrying later...");
        if (bot.isOnline()) {
            bot.disconnect("Retrying login");
        }
        scheduler.schedule(this::runCycle, retryDelay, TimeUnit.MILLISECONDS);
    }
}
