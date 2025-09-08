package com.be.javaassignment.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MetarParserUtils {

    public static final Map<String, String> CLOUD_COVERAGE_MAP = new HashMap<>();
    public static final Map<String, String> WEATHER_CODES_MAP = new HashMap<>();
    public static final Map<String, String> TREND_CODES_MAP = new HashMap<>();
    public static final Map<String, String> ICAO_CODES_MAP = new HashMap<>();

    public static final Pattern ICAO = Pattern.compile("^([A-Z]{4})\\b");
    public static final Pattern TIME = Pattern.compile("\\b(\\d{6}Z)\\b");
    public static final Pattern WIND = Pattern.compile("\\b(\\d{3}|VRB)(\\d{2,3})(G\\d{2,3})?(KT|MPS)\\b");
    public static final Pattern WIND_VARIATION = Pattern.compile("\\b(\\d{3}V\\d{3})\\b");
    public static final Pattern VISIBILITY = Pattern.compile("\\b(\\d{4}|\\d{1,2}SM|CAVOK)\\b");
    public static final Pattern TEMP_DEW = Pattern.compile("\\b(M?\\d{2})/(M?\\d{2})\\b");
    public static final Pattern PRESSURE = Pattern.compile("\\b([AQ]\\d{4})\\b");
    public static final Pattern WEATHER = Pattern.compile("([-+]?)(VC|RE)?(MI|PR|BC|DR|BL|SH|TS|FZ)?(DZ|RA|SN|SG|GS|GR|PL|IC|UP|FG|BR|HZ|VA|DU|FU|SA|PY|SQ|PO|DS|SS|FC)\\b");
    public static final Pattern CLOUD = Pattern.compile("\\b(SKC|NCD|CLR|NSC|FEW\\d{3}|SCT\\d{3}|BKN\\d{3}|OVC\\d{3}|VV\\d{3})(CB|TCU)?\\b");
    public static final Pattern RVR = Pattern.compile("\\bR(\\d{2})/([PM]?)(\\d{4})([UDN]?)\\b");
    public static final Pattern TREND = Pattern.compile("\\b(NOSIG|BECMG|TEMPO)\\b");


    static {
        CLOUD_COVERAGE_MAP.put("FEW", "few clouds (1-2 oktas)");
        CLOUD_COVERAGE_MAP.put("SCT", "scattered clouds (3-4 oktas)");
        CLOUD_COVERAGE_MAP.put("BKN", "broken clouds (5-7 oktas)");
        CLOUD_COVERAGE_MAP.put("OVC", "overcast (8 oktas)");
        CLOUD_COVERAGE_MAP.put("VV", "vertical visibility");
        CLOUD_COVERAGE_MAP.put("SKC", "sky clear");
        CLOUD_COVERAGE_MAP.put("NCD", "no cloud detected");
        CLOUD_COVERAGE_MAP.put("CLR", "no clouds below 12000 ft");
        CLOUD_COVERAGE_MAP.put("NSC", "no significant clouds");
        CLOUD_COVERAGE_MAP.put("TCU", "towering cumulus clouds");
        CLOUD_COVERAGE_MAP.put("CB", "cumulonimbus clouds");

        WEATHER_CODES_MAP.put("-", "light");
        WEATHER_CODES_MAP.put("+", "heavy");
        WEATHER_CODES_MAP.put("VC", "in the vicinity");
        WEATHER_CODES_MAP.put("RE", "recent");
        WEATHER_CODES_MAP.put("MI", "shallow");
        WEATHER_CODES_MAP.put("PR", "partial");
        WEATHER_CODES_MAP.put("BC", "patches");
        WEATHER_CODES_MAP.put("DR", "low drifting");
        WEATHER_CODES_MAP.put("BL", "blowing");
        WEATHER_CODES_MAP.put("SH", "showers");
        WEATHER_CODES_MAP.put("TS", "thunderstorm");
        WEATHER_CODES_MAP.put("FZ", "freezing");

        WEATHER_CODES_MAP.put("DZ", "drizzle");
        WEATHER_CODES_MAP.put("RA", "rain");
        WEATHER_CODES_MAP.put("SN", "snow");
        WEATHER_CODES_MAP.put("SG", "snow grains");
        WEATHER_CODES_MAP.put("GS", "small hail");
        WEATHER_CODES_MAP.put("GR", "hail");
        WEATHER_CODES_MAP.put("PL", "ice pellets");
        WEATHER_CODES_MAP.put("IC", "ice crystals");
        WEATHER_CODES_MAP.put("UP", "unknown precipitation");
        WEATHER_CODES_MAP.put("FG", "fog");
        WEATHER_CODES_MAP.put("BR", "mist");
        WEATHER_CODES_MAP.put("HZ", "haze");
        WEATHER_CODES_MAP.put("VA", "volcanic ash");
        WEATHER_CODES_MAP.put("DU", "dust");
        WEATHER_CODES_MAP.put("FU", "smoke");
        WEATHER_CODES_MAP.put("SA", "sand");
        WEATHER_CODES_MAP.put("PY", "spray");

        WEATHER_CODES_MAP.put("SQ", "squalls");
        WEATHER_CODES_MAP.put("PO", "dust whirls");
        WEATHER_CODES_MAP.put("DS", "dust storm");
        WEATHER_CODES_MAP.put("SS", "sand storm");
        WEATHER_CODES_MAP.put("FC", "funnel cloud");

        WEATHER_CODES_MAP.put("AO1", "automatic station without precipitation discriminator");
        WEATHER_CODES_MAP.put("AO2", "automatic station with precipitation discriminator");

        TREND_CODES_MAP.put("NOSIG", "no significant changes expected");
        TREND_CODES_MAP.put("BECMG", "becoming trend forecast");
        TREND_CODES_MAP.put("TEMPO", "temporary fluctuations expected");

        loadIcaoMap("src/main/resources/airports.csv");
    }

    private static void loadIcaoMap(String filePath) {
        try {
            Files.lines(Paths.get(filePath)).skip(1)
                    .forEach(line -> {
                        String[] parts=line.split("\",\"");
                        if (parts.length >= 5) {
                            String icaoCode = parts[3].replace("\"", "").trim();
                            String airport = parts[4].replace("\"", "").trim();
                            if (!icaoCode.isEmpty()) {
                                ICAO_CODES_MAP.put(icaoCode, airport);
                            }
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ICAO map from " + filePath, e);
        }
    }
}