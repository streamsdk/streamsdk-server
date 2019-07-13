package com.ugc.test;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TestFile
{
    public static void main(String[]args){

        String regex = "([a-zA-Z])+";
        String text = "ffdsa2safsa";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        System.out.println(m.matches());

        //london --- dublin city
        /* double la1 = 51.511214;
        double lon1 = -0.119824;
        double la2 = 53.349805;
        double lon2 = -6.260310;*/

        //upper gardiner to lower gardier
        /*double la1 = 53.351772;
        double lon1 = -6.256357;
        double la2 = 53.357897;
        double lon2 = -6.260192;*/

        //shakill to cork
        /*double la1 = 53.233266;
        double lon1 = -6.123758;
        double la2 = 51.896892;
        double lon2 = -8.486316;
       */

        //paris to london
       /* double la1 = 48.856614;
        double lon1 = 2.352222;
        double la2 = 51.511214;
        double lon2 = -0.119824;

        double d = calculateDistance(la1, lon1, la2, lon2);*/

       /* int di = (int)d;
        double dd = d/1000;
        int ddi = (int)dd;*/
        //double d = haverSineDistance(la1, lon1, la2, lon2);

        //System.out.println(d);
    }

   public static double haverSineDistance(double lat1, double lng1, double lat2, double lng2)
{
    // mHager 08-12-2012
    // http://en.wikipedia.org/wiki/Haversine_formula
    // Implementation

    // convert to radians
    lat1 = Math.toRadians(lat1);
    lng1 = Math.toRadians(lng1);
    lat2 = Math.toRadians(lat2);
    lng2 = Math.toRadians(lng2);

    double dlon = lng2 - lng1;
    double dlat = lat2 - lat1;

    double a = Math.pow((Math.sin(dlat/2)),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2),2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

    return 6384 * c;
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

   private static List<File> fileToBeRemoved(){
        List<File> files = new ArrayList<File>();
        File file = new File("c:\\tmp");
        File listFiles[] = file.listFiles();
        listFiles(files, listFiles);
        List<File> fileToBeRemoved = new ArrayList<File>();
        long currentTime = System.currentTimeMillis();
        for (File currentFile : files){
            if (currentTime > currentFile.lastModified()){
                fileToBeRemoved.add(currentFile);
            }
        }
        return fileToBeRemoved;
    }

    private static void listFiles(List<File> files, File[] listFiles){

        for (File file : listFiles){
            if (file.isDirectory()){
                listFiles(files, file.listFiles());
            }else{
                System.out.println("adding file: " + file.getName() + " with absolute path: " + file.getAbsolutePath());
                files.add(file);
            }
        }
    }

}
