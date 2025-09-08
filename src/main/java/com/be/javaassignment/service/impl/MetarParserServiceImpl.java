package com.be.javaassignment.service.impl;


import com.be.javaassignment.model.Metar;
import com.be.javaassignment.service.MetarParserService;
import com.be.javaassignment.utils.MetarParserUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import static com.be.javaassignment.utils.MetarParserUtils.*;


@Service
public class MetarParserServiceImpl implements MetarParserService {


    @Override
    public void parseMetar(Metar parsedMetar) {
        String metar= parsedMetar.getData().trim();

        Matcher m = MetarParserUtils.ICAO.matcher(metar);
        if(m.find()){
            String airportName=ICAO_CODES_MAP.getOrDefault(m.group(1),m.group(1));
            if (airportName!= null && !airportName.isBlank()) {
                parsedMetar.setAirportName(airportName);
            }
            metar=metar.substring(4).trim();
        }

        m=TIME.matcher(metar);
        if (m.find()) {
            String observationTime=parseObservationTime(parsedMetar.getCreatedAt());
            if (!observationTime.isBlank()) {
                parsedMetar.setObservationTime(observationTime);
            }
            metar =metar.replace(m.group(1), "").trim();
        }

        m=WIND.matcher(metar);
        if (m.find()) {
            String wind =parseWind(m);
            if(!wind.isBlank()) {
                parsedMetar.setWind(wind);
            }
            metar = metar.replace(m.group(1), "").trim();
        }

        m = WIND_VARIATION.matcher(metar);
        if (m.find()) {
            String windVariation=parseWindVariation(m.group(1));
            if (!windVariation.isBlank()) {
                parsedMetar.setWindVariation(windVariation);
            }
            metar = metar.replace(m.group(1), "").trim();
        }

        m = VISIBILITY.matcher(metar);
        if (m.find()) {
            String visibility=parseVisibility(m.group(1));
            if(!visibility.isBlank()) {
                parsedMetar.setVisibility(visibility);
            }
            metar = metar.replace(m.group(1), "").trim();
        }

        List<String> cloudCoverage = new ArrayList<>();
        m=CLOUD.matcher(metar);
        while (m.find()) {
            cloudCoverage.add(parseClouds(m));
            metar = metar.replace(m.group(), "").trim();
        }
        if(!cloudCoverage.isEmpty()) {
            parsedMetar.setCloudCoverage(String.join("; ", cloudCoverage));
        }

        m = TEMP_DEW.matcher(metar);
        if (m.find()) {
            String temperature=parseTemperature(m.group(1));
            String dew=parseTemperature(m.group(2));
            if(temperature!=null && !temperature.isBlank()) {
                parsedMetar.setTemperature(temperature);
            }
            if(dew != null && !dew.isBlank()) {
                parsedMetar.setDewPoint(dew);
            }
            metar = metar.replace(m.group(), "").trim();
        }


        m = PRESSURE.matcher(metar);
        if (m.find()) {
            String pressure=parsePressure(m.group(1));
            if(!pressure.isBlank()) {
                parsedMetar.setPressure(pressure);
            }
            metar = metar.replace(m.group(1), "").trim();
        }


        List<String> weatherList = new ArrayList<>();
        m = WEATHER.matcher(metar);
        while (m.find()) {
            weatherList.add(parseWeather(m));
            metar=metar.replace(m.group(),"").trim();
        }
        if(!weatherList.isEmpty()) {
            parsedMetar.setWeather(String.join(", ", weatherList));
        }

        List<String> rvrList=new ArrayList<>();
        m = RVR.matcher(metar);
        while(m.find()){
            rvrList.add(parseRvr(m));
            metar=metar.replace(m.group(),"").trim();
        }

        if(!rvrList.isEmpty()) {
            parsedMetar.setRvr(String.join(", ", rvrList));
        }

        m = TREND.matcher(metar);
        if (m.find()) {
            String trend=parseTrend(m.group(1));
            if(trend!= null && !trend.isBlank()) {
                parsedMetar.setTrend(trend);
            }
            metar = metar.replace(m.group(1), "").trim();
        }

    }

    private String parseObservationTime(Instant instant) {
        ZonedDateTime zonedTime=instant.atZone(ZoneId.of("UTC"));
        int day=zonedTime.getDayOfMonth();
        String month=zonedTime.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int year = zonedTime.getYear();
        String time=String.format("%02d:%02d", zonedTime.getHour(), zonedTime.getMinute());

        return String.format("%s %s, %d at %s UTC",month, day + getOrdinal(day), year, time);
    }

    private String getOrdinal(int day) {
        if (day>= 11 && day<= 13) {
            return "th";
        }
        return switch (day % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    private String parseTrend(String trend) {
        return TREND_CODES_MAP.getOrDefault(trend, "");
    }

    private String parseRvr(Matcher m) {
        String runway=m.groupCount() >= 1 ? m.group(1) : null;
        String modifier=m.groupCount() >= 2 ? m.group(2) : null;
        String value=m.groupCount() >= 3 ? m.group(3) : null;
        String trend=m.groupCount() >= 4 ? m.group(4) : null;

        StringBuilder sb = new StringBuilder();
        if(runway!=null) {
            sb.append("runway visual range along runway ").append(runway.replaceFirst("^0+(?!$)", "")).append(" is ");
        }
        if ("P".equals(modifier)) {
            sb.append("greater than ");
        } else if ("M".equals(modifier)) {
            sb.append("less than ");
        }

        sb.append(value).append(" meters and ");

        if ("U".equals(trend)) {
            sb.append("increasing");
        } else if ("D".equals(trend)) {
            sb.append("decreasing");
        } else if ("N".equals(trend)) {
            sb.append("not changing");
        }

        return sb.toString();
    }

    private String parseWeather(Matcher m) {
        StringBuilder sb=new StringBuilder();

        String intensity=m.groupCount()>= 1 ? m.group(1) : null;
        String vicinity=m.groupCount()>= 2 ? m.group(2) : null;
        String descriptor=m.groupCount()>= 3 ? m.group(3) : null;
        String phenomenon=m.groupCount()>= 4 ? m.group(4) : null;

        if(intensity != null && !intensity.isEmpty()) {
            String code=WEATHER_CODES_MAP.get(intensity);
            if (code !=null) sb.append(code).append(" ");
        }
        if(vicinity != null) {
            String code=WEATHER_CODES_MAP.get(vicinity);
            if (code !=null) sb.append(code).append(" ");
            if ("VC".equals(vicinity)) sb.append("in vicinity ");
            if ("RE".equals(vicinity)) sb.append("recent ");
        }
        if (descriptor!=null) {
            String desc=WEATHER_CODES_MAP.get(descriptor);
            if(desc!=null){
                sb.append(desc).append(" ");
            }
            else{
                sb.append(descriptor).append(" ");
            }
        }
        if (phenomenon!= null) {
            String phenom=WEATHER_CODES_MAP.get(phenomenon);
            if(phenom != null){
                sb.append(phenom).append(" ");
            }
            else{
                sb.append(phenomenon).append(" ");
            }
        }
        return sb.toString().trim();
    }

    private String parsePressure(String pressure) {
        if (pressure.startsWith("A")) {
            double inches = Double.parseDouble(pressure.substring(1)) / 100.0;
            return String.format("%.2f inches Hg", inches).replace(",", ".");
        } else if (pressure.startsWith("Q")) {
            return pressure.substring(1) + " hPa";
        }
        return pressure;
    }

    private String parseTemperature(String temp) {
        if(temp==null ||temp.isEmpty()) return temp;
        if(temp.startsWith("M")){
            return "-" + temp.substring(1).replaceFirst("^0+(?!$)","")+"°C";
        }
        return temp.replaceFirst("^0+(?!$)","")+"°C";

    }

    private String parseClouds(Matcher m) {
        String cloudCode=m.group(1);
        String cloudType =m.groupCount()>= 2 ? m.group(2) : null;

        if("SKC".equals(cloudCode) || "CLR".equals(cloudCode) || "NSC".equals(cloudCode) || "NCD".equals(cloudCode)) {
            return CLOUD_COVERAGE_MAP.get(cloudCode);
        }

        String coverageCode=cloudCode.substring(0, 3);
        int height=Integer.parseInt(cloudCode.substring(3))*100;

        int heightMeters=(int)Math.round(height*0.3048);
        StringBuilder sb = new StringBuilder();
        sb.append(CLOUD_COVERAGE_MAP.getOrDefault(coverageCode, coverageCode.toLowerCase() + " clouds"));

        sb.append(" at ").append(height).append(" feet (").append(heightMeters).append(" meters)");

        if(cloudType != null) {
            sb.append("; Cloud type: ").append(CLOUD_COVERAGE_MAP.getOrDefault(cloudType, cloudType));
        }
        return sb.toString();
    }


    private String parseVisibility(String visibility) {
        if ("CAVOK".equals(visibility)) {
            return "ceiling and visibility OK";
        }
        if (visibility.endsWith("SM")) {
            String miles=visibility.replace("SM", "");
            return miles + " statute miles";
        }
        int meters=Integer.parseInt(visibility);
        if(meters>=10000){
            return "10 kilometers or more";
        }
        return meters+" meters";

    }

    private String parseWind(Matcher m) {
        StringBuilder sb = new StringBuilder();

        if("00000KT".equals(m.group())) {
            return "calm";
        }
        String direction= m.groupCount()>=1 ? m.group(1) : null;
        String speed= m.groupCount()>=2 ? m.group(2) : null;
        String gust= m.groupCount()>=3 ? m.group(3) : null;
        String unit= m.groupCount()>=4 ? m.group(4) : "KT";

        if("VRB".equals(direction)) {
            sb.append("variable");
        }else if (direction !=null) {
            sb.append(direction.replaceFirst("^0+(?!$)","")).append(" degrees relative to true north");
        }

        if(speed != null) {
            sb.append(" at ").append(speed.replaceFirst("^0+(?!$)","")).append(" ");
            if ("MPS".equals(unit)) {
                sb.append("meters per second");
            } else {
                sb.append("knots");
            }
        }

        if(gust != null) {
            sb.append(", gusting up to ").append(gust.substring(1).replaceFirst("^0+(?!$)","")).append(" ");
            sb.append("KT".equals(unit) ? "knots" : "meters per second");
        }
        return sb.toString();
    }


    private String parseWindVariation(String variation) {
        String[] parts=variation.split("V");
        return "from " + parts[0].replaceFirst("^0+(?!$)","") + " to "+parts[1].replaceFirst("^0+(?!$)","") + " degrees";
    }


}