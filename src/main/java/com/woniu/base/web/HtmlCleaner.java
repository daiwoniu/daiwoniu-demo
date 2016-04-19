package com.woniu.base.web;

import com.woniu.base.lang.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

//see http://jsoup.org/cookbook/cleaning-html/whitelist-sanitizer
public class HtmlCleaner {

    public String clean(String html) {
        if (Strings.isBlank(html)) {
            return "";
        }

        Document doc = Jsoup.parse(html);
        Whitelist whitelist = Whitelist.basicWithImages();
        whitelist.addAttributes("span","style");
        whitelist.addAttributes("font","color");
        whitelist.addAttributes("p","style");
        Cleaner cleaner = new Cleaner(whitelist);
        doc = cleaner.clean(doc);

        for (Element e : doc.select("[style]")) {
            String style = e.attr("style");
            style = cleanStyle(style);
            if (style.isEmpty()) {
                e.removeAttr("style");
            } else {
                e.attr("style", style);
            }
        }

        return doc.select("body").html();
    }

    private String cleanStyle(String style) {
        if (style == null) {
            return "";
        }
        style = style.toLowerCase();
        //disable javascript/expression
        if (style.contains("javascript") || style.contains("expression")) {
            return "";
        }

        // /*xxx*/
        style = style.replaceAll("\\*", "");
        // &#xx
        style = style.replaceAll("&", "");

        return style;
    }


    public static void main(String[] args){
        String test = "<p>\n" +
                "    来火球社<span style=\"color:#ff0000\" width=\"50px\">区签到有什</span>么好处呢？<span style=\"background-color: rgb(255, 192, 0);\">今天是圣诞节</span>，有礼品发给我们火星人吗？\n" +
                "</p>";
        System.out.println(new HtmlCleaner().clean(test));
    }

}
