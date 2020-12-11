package www.starcom.com.jualanpraktis.SubKategori;

/**
 * Created by ADMIN on 08/02/2018.
 */

public class util {

    public static String stripHtml(String html)
    {
        //html = "<p sapn = 'div'>hhhhhhhhh<p> Hello World </p></span>";
        while(html.contains("<"))
            html = html.replace(html.substring(html.indexOf("<"),html.indexOf(">")+1),"");
        return html;//will return hhhhhhhh Hello World
    }

}
