package www.starcom.com.jualanpraktis.feature.pembayaran;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatText {
	public static String capital(String input){
        String hasil = "";
        if(!input.equals("")){
            hasil = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();;
        }
		return hasil;
	}
	
	public static String maxChar(String input, int max){
		String result=input;
		if(input.length() > max){
			result = input.substring(0, max) + "...";
		}
		return result;
		
	}
	
	public static String md5(String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();
	        
	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return hexString.toString();
	        
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}

    public static String friendlyDate(String tanggal){
        String hasil="";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dateStart = tanggal;
        String dateStop = dateFormat.format(date);

        //Custom date format
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Get msec from each, and subtract.
        long diff = d2.getTime() - d1.getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / ((60 * 60 * 1000)*24);
        long diffMonths= diff / (((60 * 60 * 1000)*24)*30);

        if(diffSeconds < 60){
            hasil = diffSeconds + " detik lalu";
        }else if(diffMinutes < 60){
            hasil = diffMinutes + " menit lalu";
        }else if(diffHours < 24){
            hasil = diffHours + " jam lalu";
        }else if(diffDays < 30){
            hasil = diffDays + " hari lalu";
        }else{
            SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                if(d2.getYear() == d1.getYear()){
                    SimpleDateFormat formatter  = new SimpleDateFormat("MMM dd");
                    Date tanggalHasil;
                    tanggalHasil = dtf.parse(dateStart);
                    hasil = formatter.format(tanggalHasil);
                }else{
                    SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd");
                    Date tanggalHasil;
                    tanggalHasil = dtf.parse(dateStart);
                    hasil = formatter.format(tanggalHasil);
                }
            }catch (Exception e) {
                hasil = e.getMessage();
            }


        }
        return hasil;
    }

    public static String numberFormat(double harga){
        String result;
        DecimalFormat kursIndonesia = new DecimalFormat("#,###");
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();


        formatRp.setCurrencySymbol("");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        result = kursIndonesia.format(harga);
        return result;
    }

    public static String rupiahFormat(double harga){
        String result;
        //DecimalFormat kursIndonesia = new  DecimalFormat("#,###");
        //DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.ENGLISH);
        DecimalFormat kursIndonesia = new DecimalFormat("#,###");
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        //formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        result = "Rp. " + kursIndonesia.format(harga);
        return result;
    }

    // Reads an InputStream and converts it to a String.
    public static String readIt(InputStream inputStream, int len) throws IOException, UnsupportedEncodingException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null){
            result += line;
        }

            /* Close Stream */
        if(null!=inputStream){
            inputStream.close();
        }
        return result;
    }
}
