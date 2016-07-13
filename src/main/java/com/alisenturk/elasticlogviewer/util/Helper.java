package com.alisenturk.elasticlogviewer.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;
import org.primefaces.json.JSONArray;

/**
 *
 * @author asenturk
 */
@Stateless
public class Helper implements Serializable {

    public static void addMessage(String msg) {
        FacesMessage fMsg = new FacesMessage(msg);
        FacesContext.getCurrentInstance().addMessage(null, fMsg);
    }

    public static Locale getLocale(String language) {
        String firstLang = "tr";
        String secondLang = "TR";

        if (language == null || language.equals("")) {
            language = "TR";
        }
        if (language.equals("EN")) {
            firstLang = "en";
            secondLang = "US";
        } else if (language.equals("IR")) {
            language = "IR";
            firstLang = "ir";
            secondLang = "IR";
        } else if (language.equals("UA")) {
            language = "UA";
            firstLang = "uk";
            secondLang = "UA";
        } else if (language.equals("RU")) {
            language = "RU";
            firstLang = "ru";
            secondLang = "RU";
        } else {
            language = "TR";
            firstLang = "tr";
            secondLang = "TR";
        }
        return new Locale(firstLang, secondLang);
    }

    public static String getMessage(String messageKey) {
        String msgValue = "";
        try {

            FacesContext ctx = FacesContext.getCurrentInstance();
            String bundleName = ctx.getApplication().getMessageBundle();
            ResourceBundle message = ResourceBundle.getBundle(bundleName);
            msgValue = message.getString(messageKey);

            return msgValue;
        } catch (Exception e) {
            msgValue = messageKey;
            e.printStackTrace();
        }
        return messageKey;
    }

    public static String getMessage(String messageKey, String lang) {
        try {
            String msgValue = "";
            FacesContext ctx = FacesContext.getCurrentInstance();
            String bundleName = ctx.getApplication().getMessageBundle();

            ResourceBundle message = ResourceBundle.getBundle(bundleName, getLocale(lang));
            msgValue = message.getString(messageKey);

            return msgValue;
        } catch (Exception e) {
            //Utils.errorLogger(Utils.class, e);
            e.printStackTrace();
        }
        return messageKey;
    }

    public static String utfConvStrEng(String str) {
        int unicodeIntValue = 0;
        String unicodeString = "";
        String ucValue = "";
        if (str == null) {
            return null;
        }
        try {
            int length = str.length();

            for (int i = 0; i < length; i++) {
                unicodeIntValue = str.charAt(i);

                if (unicodeIntValue == 38) {//& ile basliyorsa
                    ucValue = "" + str.charAt(i) + str.charAt(i + 1) + str.charAt(i + 2) + str.charAt(i + 3) + str.charAt(i + 4);
                    if (ucValue != null && ucValue.equals("&#304")) {
                        unicodeString += "I";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#305")) {
                        unicodeString += "i";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#214")) {
                        unicodeString += "O";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#246")) {
                        unicodeString += "o";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#220")) {
                        unicodeString += "U";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#252")) {
                        unicodeString += "u";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#199")) {
                        unicodeString += "C";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#231")) {
                        unicodeString += "c";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#286")) {
                        unicodeString += "G";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#287")) {
                        unicodeString += "g";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#350")) {
                        unicodeString += "S";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#351")) {
                        unicodeString += "s";
                        i = i + 5;
                        continue;
                    }
                }

                if (unicodeIntValue == 221 || unicodeIntValue == 304) {
                    unicodeString += "I";
                    continue;
                }
                if (unicodeIntValue == 253 || unicodeIntValue == 305) {
                    unicodeString += "i";
                    continue;
                }
                if (unicodeIntValue == 254 || unicodeIntValue == 351) {
                    unicodeString += "s";
                    continue;
                }
                if (unicodeIntValue == 222 || unicodeIntValue == 350) {
                    unicodeString += "S";
                    continue;
                }
                if (unicodeIntValue == 208 || unicodeIntValue == 286) {
                    unicodeString += "G";
                    continue;
                }
                if (unicodeIntValue == 240 || unicodeIntValue == 287) {
                    unicodeString += "g";
                    continue;
                }
                if (unicodeIntValue == 231) {
                    unicodeString += "c";
                    continue;
                }
                if (unicodeIntValue == 199) {
                    unicodeString += "C";
                    continue;
                }
                if (unicodeIntValue == 252) {
                    unicodeString += "u";
                    continue;
                }
                if (unicodeIntValue == 220) {
                    unicodeString += "U";
                    continue;
                }
                if (unicodeIntValue == 246) {
                    unicodeString += "o";
                    continue;
                }
                if (unicodeIntValue == 214) {
                    unicodeString += "O";
                    continue;
                }

                unicodeString += (char) unicodeIntValue;
            }
            return unicodeString;
        } catch (Exception e) {

            return "";
        }
    }

    public static String date2String(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("dd/MM/yyyy");
            return sdf.format(date);
        } else {
            return "";
        }
    }

    public static Date dateAddMinute(Date date, int minute) {
        Date newdate = date;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, minute);
            newdate = cal.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newdate;
    }

    public static Date dateAdd(Date date, int day) {
        Date newdate = date;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, day);
            newdate = cal.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newdate;
    }

    public static Date dateAddMonth(Date date, int month) {
        Date newdate = date;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, month);
            newdate = cal.getTime();

        } catch (Exception e) {
           e.printStackTrace();
        }
        return newdate;
    }

    public static double roundDecimal(double val){        
    
        try{    
            
            double newval = BigDecimal.valueOf(val).setScale(2,RoundingMode.HALF_UP).doubleValue();
            DecimalFormat df = new DecimalFormat("#.##",DecimalFormatSymbols.getInstance(Locale.ENGLISH));
            return new Double(df.format(newval));
        }catch(Exception e){
            System.out.println("roundecimal-error..:" + e.getMessage() + " val..:" + val);
        }
        
        return val;
    }
    
    public static String getAppParameterValue(String key) {
        String msgValue = "";
        try {
            String propFileName = "application.properties";
            Properties properties = new Properties();
            InputStream inputStream = Helper.class.getClassLoader().getResourceAsStream(propFileName);
            if(null!=inputStream){
                properties.load(inputStream);
                msgValue = properties.getProperty(key);
            }else{
                
            }
            return msgValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgValue;
    }
    
    public static String date2String(Date date,String format) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern(format);
            return sdf.format(date);
        } else {
            return "";
        }
    }
    
}
