package greenebolt.autotrade;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.apache.logging.log4j.util.StringBuilders.escapeJson;

public class Config {
    private static final Properties defaultValues = new Properties();
    public static String fileName;

    public static String[] targetItems;
    public static String[] buyTargetItems;
    public static TradeMode tradeMode = TradeMode.SELL;

    private static final String SELL_KEY = "Trade Cost Targets:";
    private static final String BUY_KEY = "Trade Buy Targets:";
    private static final String MODE_KEY = "Trade Mode:";

    Config(String fileName) {
        this.fileName = fileName;
    }

    public void read() {
        try {
            BufferedReader configReader = new BufferedReader(new FileReader(fileName));
            String json = Files.readString(Paths.get(fileName));
            configReader.close();

            targetItems = parseArray(json, SELL_KEY);
            buyTargetItems = parseArray(json, BUY_KEY);
            tradeMode = parseMode(json);

        } catch (FileNotFoundException ignored) {
            // If the config does not exist, generate the default one.
            AutoTrade.LOGGER.info("Generating the config file at: " + fileName);
            generateConfig();
            return;
        } catch (IOException e) {
            AutoTrade.LOGGER.info("Failed to read the config file: " + fileName);
            e.printStackTrace();
        }

    }

    private static String[] parseArray(String json, String key) {
        int keyIndex = json.indexOf(key);
        if (keyIndex == -1) {
            return new String[0];
        }

        int start = json.indexOf('[', keyIndex);
        int end = json.indexOf(']', start);

        if (start == -1 || end == -1 || end <= start) {
            throw new RuntimeException("Invalid JSON format for key: " + key);
        }

        String arrayContent = json.substring(start + 1, end);
        String[] rawItems = arrayContent.split(",");

        List<String> result = new ArrayList<>();

        for (String item : rawItems) {
            String cleaned = item.trim()
                    .replaceAll("^\"|\"$", ""); // remove surrounding quotes
            if (!cleaned.isEmpty()) {
                result.add(cleaned);
            }
        }

        return result.toArray(new String[0]);
    }

    private static TradeMode parseMode(String json) {
        int keyIndex = json.indexOf(MODE_KEY);
        if (keyIndex == -1) {
            return TradeMode.SELL;
        }

        // Config is written as "Trade Mode:": "buy" — skip past the key's closing
        // quote and the JSON colon so we parse the value, not ": ".
        int afterKey = keyIndex + MODE_KEY.length();
        int start = json.indexOf('"', afterKey + 1);
        if (start == -1) {
            return TradeMode.SELL;
        }
        start += 1;

        int end = json.indexOf('"', start);
        if (end == -1 || end <= start) {
            return TradeMode.SELL;
        }

        String modeValue = json.substring(start, end).trim().toLowerCase();
        return switch (modeValue) {
            case "buy" -> TradeMode.BUY;
            case "both" -> TradeMode.BOTH;
            default -> TradeMode.SELL;
        };
    }

    private static String arrayToJson(String[] items) {
        StringBuilder json = new StringBuilder("[\n");

        for (int i = 0; i < items.length; i++) {
            json.append("    \"")
                    .append(items[i])
                    .append("\"");

            if (i < items.length - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ]");
        return json.toString();
    }

    public static void save() {

        StringBuilder json = new StringBuilder();

        json.append("{\n  \"").append(SELL_KEY).append("\": ")
                .append(arrayToJson(targetItems))
                .append(",\n  \"").append(BUY_KEY).append("\": ")
                .append(arrayToJson(buyTargetItems))
                .append(",\n  \"").append(MODE_KEY).append("\": \"")
                .append(tradeMode.toString().toLowerCase())
                .append("\"\n}");

        try {
            File config = new File(fileName);
            File parentDir = config.getParentFile();
            if (!parentDir.exists())
                parentDir.mkdirs();

            FileWriter configWriter = new FileWriter(config);

            configWriter.flush();
            configWriter.write(json.toString());

            configWriter.close();
        } catch (IOException e) {
            AutoTrade.LOGGER.info("Failed to write the config file: " + fileName);
            e.printStackTrace();
        }
    }

    private void generateConfig() {
        try {
            File config = new File(fileName);
            File parentDir = config.getParentFile();
            if (!parentDir.exists())
                parentDir.mkdirs();
            FileWriter configWriter = new FileWriter(config);

            configWriter.write("{\n  \"" + SELL_KEY + "\": [\n    \"iron_ingot\"\n  ],\n  \"" + BUY_KEY + "\": [\n  ],\n  \"" + MODE_KEY + "\": \"sell\"\n}");
            targetItems = new String[]{"iron_ingot"};
            buyTargetItems = new String[0];
            tradeMode = TradeMode.SELL;

            configWriter.close();

        } catch (IOException e) {
            AutoTrade.LOGGER.info("Failed to generate config file: " + fileName);
            e.printStackTrace();
        }

    }
}
