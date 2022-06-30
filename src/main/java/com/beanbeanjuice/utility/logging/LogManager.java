package com.beanbeanjuice.utility.logging;

import com.beanbeanjuice.utility.Helper;
import com.beanbeanjuice.utility.exception.WebhookException;
import com.beanbeanjuice.utility.time.Time;
import com.beanbeanjuice.utility.webhook.Webhook;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A class used for logging.
 *
 * @author beanbeanjuice
 */
public class LogManager {

    private final Time time;

    private final String name;
    private TextChannel logChannel;
    private final ArrayList<String> webhookURLs;
    private String currentLogFileName;
    private final String filePath;
    private String logFileTime;

    /**
     * Create a {@link LogManager LogManager} instance.
     * @param name The name for the {@link LogManager LogManager}.
     * @param logChannel The {@link TextChannel TextChannel} to be used for logging.
     */
    public LogManager(@NotNull String name, @Nullable TextChannel logChannel, @NotNull String filePath) {
        time = new Time();

        this.name = name;
        this.logChannel = logChannel;
        this.filePath = filePath;

        webhookURLs = new ArrayList<>(); // Creates the ArrayList

        checkFiles();

        log(LogManager.class, LogLevel.INFO, "Starting the Uncaught Exception Handler", true, false);
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            this.log(thread.getClass(), LogLevel.WARN, "Unhandled Exception: " + exception.getMessage());
            this.logStackTrace(exception);
        });
    }

    private void checkFiles() {

        // Setting the current log file time.
        logFileTime = time.format("MM-dd-yyyy");

        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }

        compressOldLogs();

        // If the log file already exists, it doesn't need to make a new one.
        if (!createLogFile(filePath)) {
            log(this.getClass(), LogLevel.INFO, "Log for today has already been created.");
        } else {
            log(this.getClass(), LogLevel.OKAY, "Created Log File!");
        }
    }

    private void logStackTrace(@NotNull Throwable exception) {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        PrintWriter printWriter = null;

        try {

            fileWriter = new FileWriter(currentLogFileName, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);

            // Appends this to the current log file.
            exception.printStackTrace(printWriter);

            // Flushes the print writer.
            printWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                printWriter.close();
                bufferedWriter.close();;
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        exception.printStackTrace();
    }

    /**
     * Logs to file.
     * @param c The class that called the log.
     * @param logLevel The current {@link LogLevel} of the log to be created.
     * @param message The message contents for the log.
     * @param time The current {@link Time} the log was sent.
     */
    private void logToFile(@NotNull Class<?> c, @NotNull LogLevel logLevel, @NotNull String message, @NotNull Time time) {
        String formattedMessage = "[" + time.format("hh:mm:ss a zzz") + "] ["
                + c.getName() + "/" + logLevel + "]: " + message;
        logToFile(formattedMessage);
    }

    /**
     * This is used to log a message to a file.
     * @param message The message to log.
     */
    private void logToFile(@NotNull String message) {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        PrintWriter printWriter = null;

        try {

            fileWriter = new FileWriter(currentLogFileName, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);

            // Appends this to the current log file.
            printWriter.println(message);

            // Flushes the print writer.
            printWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                printWriter.close();
                bufferedWriter.close();;
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create a new log file from its designated name.
     * @param fileName The name of the new {@link File}.
     * @return Returns false if the file already exists.
     */
    @NotNull
    private Boolean createLogFile(@NotNull String fileName) {
        try {
            // Sets the current log file name.
            currentLogFileName = fileName + time.format("MM-dd-yyyy") + ".log";
            File file = new File(currentLogFileName);

            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Compresses the old logs.
     */
    private void compressOldLogs() {
        String[] pathnames;

        File file = new File(filePath);
        pathnames = file.list();

        // Goes through all files in the directory.
        for (String fileName : pathnames) {

            // If the file contains ".log" then compress it if the file name
            // No longer matches today's date.
            if (fileName.contains(".log")) {
                String parsedFileName = fileName.split(" ")[0];
                parsedFileName = parsedFileName.replace(".log", "");

                // Compress the file
                if (!parsedFileName.equals(time.format("MM-dd-yyyy"))) {
                    try {
                        compress(filePath, fileName, parsedFileName);

                        // Deletes the file once it has been compressed.
                        Path fileFromPath = Paths.get(filePath + fileName);
                        fileFromPath.toFile().delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Compress a file from a filepath to its designated compressed file name.
     * @param logDirectory The path to the directory of the log files.
     * @param fileName The file name of the {@link File logFile}.
     * @param compressedFileName The file name of the new {@link File} for compression.
     * @throws IOException The exception that is thrown if the file cannot be found.
     */
    private void compress(@NotNull String logDirectory, @NotNull String fileName, @NotNull String compressedFileName) throws IOException {

        File compressedDirectory = new File(logDirectory + compressedFileName);

        compressedDirectory.mkdir();

        String gZipCompressedFileName = logDirectory + compressedFileName + "/" + compressedFileName + ".gz";
        String zipCompressedFileName = logDirectory + compressedFileName + "/" + compressedFileName + ".zip";
        String uncompressedFileName = logDirectory + fileName;

        Path filePath = Paths.get(logDirectory + fileName);
        Path gZipCompressedPath = Paths.get(gZipCompressedFileName);
        Path zipCompressedPath = Paths.get(zipCompressedFileName);

        // Creating a GZIP.
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(
                new FileOutputStream(gZipCompressedPath.toFile()
                ));

             FileInputStream fileInputStream = new FileInputStream(filePath.toFile())) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buffer)) > 0) {
                gzipOutputStream.write(buffer, 0, len);
            }
        }

        // Creating a ZIP.
        FileOutputStream fileOutputStream = new FileOutputStream(zipCompressedFileName);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

        File file = new File(uncompressedFileName);
        FileInputStream fileInputStream = new FileInputStream(file);

        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOutputStream.putNextEntry(zipEntry);

        byte[] buffer = new byte[1024];
        int len;

        while ((len = fileInputStream.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, len);
        }

        zipOutputStream.flush();
        zipOutputStream.close();
        fileInputStream.close();
    }

    /**
     * Sets the log channel for the {@link LogManager}.
     * @param logChannel The {@link TextChannel logChannel} for the {@link Guild}.
     */
    public void setLogChannel(@NotNull TextChannel logChannel) {
        this.logChannel = logChannel;
    }

    /**
     * Log to discord, webhook, and file.
     * @param c The class that called the log.
     * @param logLevel The current {@link LogLevel} of the log to be created.
     * @param message The message contents for the log.
     */
    public void log(@NotNull Class<?> c, @NotNull LogLevel logLevel, @NotNull String message) {
        log(c, logLevel, message, true, true);
    }

    /**
     * This is for optionally choosing to log with an exception to everything.
     * @param c The {@link Class} that called the log.
     * @param logLevel The {@link LogLevel} for the log.
     * @param message The message contents for the log.
     * @param exception The {@link Throwable} that goes with the log.
     */
    public void log(@NotNull Class<?> c, @NotNull LogLevel logLevel, @NotNull String message, @NotNull Throwable exception) {
        log(c, logLevel, message, true, true, exception);
    }

    /**
     * This is for optionally choosing to log without an exception.
     * @param c The {@link Class} that called the log.
     * @param logLevel The current {@link LogLevel} of the log to be created.
     * @param message The message contents for the log.
     * @param logToWebhook True, if logging to the webhook.
     * @param logToLogChannel True, if logging to the Discord {@link TextChannel logChannel}.
     */
    public void log(@NotNull Class<?> c, @NotNull LogLevel logLevel, @NotNull String message,
                    @NotNull Boolean logToWebhook, @NotNull Boolean logToLogChannel) {
        log(c, logLevel, message, logToWebhook, logToLogChannel, null);
    }

    /**
     * This is for optionally choosing to log to the webhook and Discord channel.
     * @param c The class that called the log.
     * @param logLevel The current {@link LogLevel} of the log to be created.
     * @param message The message contents for the log.
     */
    public void log(@NotNull Class<?> c, @NotNull LogLevel logLevel, @NotNull String message,
                    @NotNull Boolean logToWebhook, @NotNull Boolean logToLogChannel, @Nullable Throwable exception) {

        if (!time.format("MM-dd-yyyy").equals(logFileTime)) {
            checkFiles();
            log(this.getClass(), LogLevel.INFO, "New day... creating new log file.", true, false);
        }

        Logger logger = LoggerFactory.getLogger(c);

        switch (logLevel) {
            case INFO, LOADING, OKAY -> {
                logger.info(message);
            }

            case WARN -> {
                logger.warn(message);
            }

            case DEBUG -> {
                logger.debug(message);
            }

            case ERROR -> {
                logger.error(message);
            }

        }

        logToFile(c, logLevel, message, time);

        // Printing the Stack Trace if the Exception Exists
        if (exception != null) {
            logStackTrace(exception);
        }

        if (logToWebhook) {
            logToWebhook(c, logLevel, message, time);
        }

        if (logToLogChannel) {
            logToLogChannel(c, logLevel, message, time);
        }

    }

    /**
     * Logs to the webhook URLs.
     * @param c The class that called the log.
     * @param logLevel The current {@link LogLevel} of the log to be created.
     * @param message The message contents for the log.
     */
    private void logToWebhook(@NotNull Class<?> c, @NotNull LogLevel logLevel, @NotNull String message, @NotNull Time time) {
        String temp = "``[" + time.format("HH:mm:ss Z") + "]" + " [" + c.getName() + "/" + logLevel + "]: " + message + "``";

        temp = Helper.shortenToLimit(temp, 2000); // Shortens it to 2000 characters.

        for (String url : webhookURLs) {
            Webhook hook = new Webhook(url);

            hook.setUsername(name);
            hook.setContent(temp);

            try {
                hook.execute();
            } catch (IOException | WebhookException e) {
                log(this.getClass(), LogLevel.WARN, "Webhook rate limit...", false, false, new WebhookException(url, e.getMessage()));
            }
        }
    }

    /**
     * Logs to the {@link TextChannel Discord Log Channel}.
     * @param c The class that called the log.
     * @param logLevel The current {@link LogLevel} of the log to be created.
     * @param message The message contents for the log.
     */
    private void logToLogChannel(@NotNull Class<?> c, @NotNull LogLevel logLevel, @NotNull String message, @NotNull Time time) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(logLevel.toString());
        embedBuilder.setThumbnail(logLevel.getImageURL());
        embedBuilder.setTitle("`" + c.getSimpleName() + "`");
        embedBuilder.setDescription(Helper.shortenToLimit(message, 4000));
        embedBuilder.setColor(logLevel.getColor());
        embedBuilder.setTimestamp(new Date().toInstant());

        try {
            logChannel.sendMessageEmbeds(embedBuilder.build()).complete();
        } catch (NullPointerException ignored) {}
    }

    /**
     * Add a {@link Webhook} URL that receives logs.
     * @param url The link for the webhook.
     */
    public void addWebhookURL(@NotNull String url) {
        webhookURLs.add(url);
    }

}