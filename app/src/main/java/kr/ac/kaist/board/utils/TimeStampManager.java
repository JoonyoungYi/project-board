package kr.ac.kaist.board.utils;

/**
 * Created by yearnning on 2014. 3. 23..
 */
public class TimeStampManager {

    public static String convertTimeFormat(String strDate) {

        return convertTimeFormat(strDate, "yyyy-MM-dd HH:mm:ss");
    }

    public static String convertTimeFormat(String strDate, String format) {
        String msg = "";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
        java.util.Date date = null;

        try {
            date = sdf.parse(strDate);
            long curTime = System.currentTimeMillis();
            long regTime = date.getTime();
            long diffTime = (curTime - regTime) / 1000;


            if (diffTime < 60) {
                // sec
                msg = "방금 전";
            } else if ((diffTime /= 60) < 60) {
                // min
                msg = diffTime + "분 전";
            } else if ((diffTime /= 60) < 24) {
                // hour
                msg = (diffTime) + "시간 전";
            } else if ((diffTime /= 24) < 30) {
                // day
                msg = (diffTime) + "일 전";
            } else if ((diffTime /= 30) < 12) {
                // day
                msg = (diffTime) + "달 전";
            } else {
                msg = (diffTime / 12) + "년 전";
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = strDate;
        }
        return msg;
    }


}
