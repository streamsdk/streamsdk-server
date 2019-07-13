package com.ugc.facade;

import com.ugc.domain.GeoPoint;
import com.ugc.domain.QueryObject;
import com.ugc.domain.QueryObjectList;
import com.ugc.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service(value = "QueryDomainFacade")
public class QueryDomainFacadeImp implements QueryDomainFacade
{

    public Map<String, JSONArray> findMatchingObjects(String json, String query) throws JSONException {

        QueryObjectList qol = JsonUtils.convertToQueryObjectList(query);
        Map<String, JSONArray> matchingMap = null;
        if (qol.size() > 0){
           matchingMap = getMatchingArray(json, qol);
           if (qol.limitedKeysSize() > 0)
               matchingMap = getLimitedKeys(matchingMap, qol);
        }else{
           matchingMap = getMatchingIds(json, qol);
        }
        return matchingMap;

    }

    private Map<String, JSONArray> getLimitedKeys(Map<String, JSONArray> matchingMap, QueryObjectList qol){

        Map<String, JSONArray> newMap = new HashMap<String, JSONArray>();
        Set<Map.Entry<String, JSONArray>> entries = matchingMap.entrySet();
        for (Map.Entry<String, JSONArray> entry : entries) {
             if (qol.getLimitedKeys().contains(entry.getKey()))
                  newMap.put(entry.getKey(), entry.getValue());
        }
        return newMap;

    }

    private Map<String, JSONArray> getMatchingIds(String json, QueryObjectList qol) throws JSONException {

        Map<String, JSONArray> matchingMap = new HashMap<String, JSONArray>();
        JSONObject jo = new JSONObject(json);
        JSONArray data = jo.getJSONArray("group");
        int size = data.length();
        for (int i = 0; i < size; i++) {
            JSONObject jobject = data.getJSONObject(i);
            JSONArray ja = jobject.getJSONArray("data");
            String id = jobject.getString("id");
            if (qol.getLimitedKeys().contains(id))
                matchingMap.put(id, ja);
        }
        return matchingMap;
    }

    private Map<String, JSONArray> getMatchingArray(String json, QueryObjectList qol) throws JSONException, NumberFormatException {

        Map<String, JSONArray> matchingMap = new HashMap<String, JSONArray>();
        JSONObject jo = new JSONObject(json);
        JSONArray data = jo.getJSONArray("group");
        int size = data.length();
        String geoKey = qol.getGeoKey();
        Map<String, GeoPoint> geoPoints = new HashMap<String, GeoPoint>();

        for (int i = 0; i < size; i++) {
            JSONObject jobject = data.getJSONObject(i);
            JSONArray ja = jobject.getJSONArray("data");
            String id = jobject.getString("id");
            int length = ja.length();
            int currentIndex = 0;
            Set<Integer> skipCheckIndex = new HashSet<Integer>();
            boolean logicArray[] = new boolean[qol.size()];
            for (int index = 0; index < logicArray.length; index++) {
                logicArray[index] = false;
            }

            for (int j = 0; j < length; j++) {
                JSONObject jObject = ja.getJSONObject(j);
                String key = jObject.getString("key");
                //check if opeartor contains geo point 'near'
                if (geoKey != null && geoKey.equals(key)){
                    String valueObject = (String)jObject.get("value");
                    String v[] = valueObject.split(",");
                    double lat = 0.0;
				    double lon = 0.0;
				    if (!v[0].equals(""))
				       lat = Double.parseDouble(v[0]);
				    if (!v[1].equals(""))
					   lon = Double.parseDouble(v[1]);
                    GeoPoint geo = new GeoPoint(key, lat, lon);
                    geoPoints.put(id, geo);
                    skipCheckIndex.add(new Integer(currentIndex));
                }

                List<QueryObject> qos = qol.getQueryObject(key);

                for (QueryObject qo : qos) {

                    boolean currentLogic = false;
                    Object valueObject = jObject.get("value");

                    if (qo.getOperator().equals("contains")) {
                        String value[] = qo.getValueArray();
                        String currentValue = (String) valueObject;
                        boolean contains = false;
                        for (String v : value) {
                            if (v.equals(currentValue)) {
                                contains = true;
                                break;
                            }
                        }
                        currentLogic = contains;
                    }

                    if (qo.getOperator().equals("equal")) {
                        String value = (String) valueObject;
                        currentLogic = value.equals(qo.getValue());
                    }

                    if (qo.getOperator().equals("exists")){
                        currentLogic = key.equals(qo.getValue());
                    }

                    if (qo.getOperator().equals("greater")) {
                        int currentValue = 0;
                        if (valueObject instanceof String) {
                            currentValue = Integer.parseInt(valueObject.toString());
                        }

                        if (valueObject instanceof Integer) {
                            currentValue = ((Integer) valueObject).intValue();
                        }
                        int qoValue = Integer.parseInt(qo.getValue());
                        currentLogic = currentValue > qoValue;
                    }
                    if (qo.getOperator().equals("less")) {
                        int currentValue = 0;
                        if (valueObject instanceof String) {
                            currentValue = Integer.parseInt(valueObject.toString());
                        }
                        if (valueObject instanceof Integer) {
                            currentValue = ((Integer) valueObject).intValue();
                        }
                        int qoValue = Integer.parseInt(qo.getValue());
                        currentLogic = currentValue < qoValue;
                    }

                    if (qo.getOperator().equals("after")) {
                        String currentStringValue = "";
                        if (valueObject.toString().length() > qo.getValue().length())
                            currentStringValue = valueObject.toString().substring(0, qo.getValue().length());
                        else
                            currentStringValue = valueObject.toString();
                        long currentValue = Long.parseLong(currentStringValue);
                        long qoValue = Long.parseLong(qo.getValue());
                        currentLogic = (currentValue - qoValue) > 0;
                    }

                    if (qo.getOperator().equals("regex")){
                        String value = (String) valueObject;
                        String regex = qo.getValue();
                        Pattern p = Pattern.compile(regex);
		                Matcher m = p.matcher(value);
		                currentLogic = m.matches();
                    }

                    if (qo.getOperator().equals("before")) {
                        String currentStringValue = "";
                        if (valueObject.toString().length() > qo.getValue().length())
                            currentStringValue = valueObject.toString().substring(0, qo.getValue().length());
                        else
                            currentStringValue = valueObject.toString();

                        long currentValue = Long.parseLong(currentStringValue);
                        long qoValue = Long.parseLong(qo.getValue());
                        currentLogic = (currentValue - qoValue) < 0;
                    }

                    boolean logic = true;
                    if (qol.size() > 1) {
                        if (qol.getLogic().equals("and")) {
                            logic = logic && currentLogic;
                        }
                        if (qol.getLogic().equals("or")) {
                            logic = false;
                            logic = logic || currentLogic;
                        }
                    } else {
                        logic = currentLogic;
                    }

                    logicArray[currentIndex] = logic;
                    currentIndex++;


                }
            }

            boolean found = true;

            if (qol.getLogic().equals("and")) {
                for (int index = 0; index < logicArray.length; index++) {
                    if (skipCheckIndex.contains(new Integer(index)))
                        continue;
                    if (!logicArray[index]) {
                        found = false;
                        break;
                    }
                }
            } else {
                found = false;
                for (int index = 0; index < logicArray.length; index++) {
                    if (skipCheckIndex.contains(new Integer(index)))
                        continue;
                    if (logicArray[index]) {
                        found = true;
                        break;
                    }
                }
            }

            if (found) {
                matchingMap.put(id, ja);
            }

        }

        List<GeoPoint> geos = new ArrayList<GeoPoint>();

        if (geoPoints.size() > 0){

            String geoValue = qol.getGeoValue();
            String v[] = geoValue.split(",");
            double lat = 0.0;
            double lon = 0.0;
            if (!v[0].equals(""))
                lat = Double.parseDouble(v[0]);
            if (!v[1].equals(""))
                lon = Double.parseDouble(v[1]);
            int limit = Integer.parseInt(v[2]);
            Set<Map.Entry<String, GeoPoint>> points = geoPoints.entrySet();
            for (Map.Entry<String, GeoPoint> point : points){
                 GeoPoint geoPoint = point.getValue();
                 double distance = calculateDistance(lat, lon, geoPoint.getLatitude(), geoPoint.getLongitude());
                 if (distance > 0.8){
                    geoPoint.setDistance(distance);
                    geoPoint.setId(point.getKey());
                    geos.add(geoPoint);
                 }
            }
            Collections.sort(geos, new Comparator<GeoPoint>()
            {
                public int compare(GeoPoint o1, GeoPoint o2) {
                   if (o1.getDistance() > o2.getDistance())
                       return 1;
                   else if (o1.getDistance() < o2.getDistance())
                       return -1;
                   else
                       return 0;
                }
            });
            if (geos.size() > limit)
                geos = geos.subList(0, limit);

        }

        //only query near geo points
        if (matchingMap.size() == 0) {
            if (geos.size() > 0) {
                for (GeoPoint p : geos) {
                    String pid = p.getId();
                    for (int i = 0; i < size; i++) {
                        JSONObject jobject = data.getJSONObject(i);
                        JSONArray ja = jobject.getJSONArray("data");
                        String id = jobject.getString("id");
                        if (id.equals(pid)) {
                            JSONObject distance = new JSONObject();
                            distance.put("value", p.getDistance());
                            distance.put("key", "distance");
                            distance.put("type", "double");
                            ja.put(distance);
                            matchingMap.put(id, ja);
                        }
                    }
                }
            }
        } else {
            if (geos.size() > 0){
                Map<String, JSONArray> newMap = new HashMap<String, JSONArray>();
                for (GeoPoint p : geos){
                     if (matchingMap.containsKey(p.getId())){
                         JSONArray jso = matchingMap.get(p.getId());
                         JSONObject distance = new JSONObject();
                         distance.put("value", p.getDistance());
                         distance.put("key", "distance");
                         distance.put("type", "double");
                         jso.put(distance);
                         newMap.put(p.getId(), jso);
                     }
                }
                return newMap;
            }
        }

        return matchingMap;
    }

     private static double calculateDistance(double lat1, double lon1, double lat2, double lon2){

       double a = 6378137, b = 6356752.314245, f = 1 / 298.257223563; // WGS-84 ellipsoid params
       double L = Math.toRadians(lon2 - lon1);
       double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat1)));
       double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat2)));
       double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
       double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

       double sinLambda, cosLambda, sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
       double lambda = L, lambdaP, iterLimit = 100;
       do {
           sinLambda = Math.sin(lambda);
           cosLambda = Math.cos(lambda);
           sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda)
                                + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
           if (sinSigma == 0)
               return 0; // co-incident points
           cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
           sigma = Math.atan2(sinSigma, cosSigma);
           sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
           cosSqAlpha = 1 - sinAlpha * sinAlpha;
           cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
           if (Double.isNaN(cos2SigmaM))
               cos2SigmaM = 0; // equatorial line: cosSqAlpha=0 (§6)
           double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
           lambdaP = lambda;
           lambda = L + (1 - C) * f * sinAlpha
                        * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
       } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

       if (iterLimit == 0)
           return Double.NaN; // formula failed to converge

       double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
       double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
       double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
       double deltaSigma = B
                           * sinSigma
                           * (cos2SigmaM + B
                                           / 4
                                           * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM
                                                                                              * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
       double dist = b * A * (sigma - deltaSigma);

       return dist;

   }

}
