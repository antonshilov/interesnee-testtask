package gq.vaccum121.testtask;

import org.junit.Test;

import java.util.Date;

import gq.vaccum121.testtask.util.DateUtil;

import static org.junit.Assert.assertEquals;

public class DateUtilTest {
    @Test
    public void stringParsingCorrect() {
        String strDate = "Wed Apr 3 15:25:29 +0000 2013";
        Date date = new Date(1365002729000L);
        assertEquals(date, DateUtil.parse(strDate));
    }

    @Test
    public void dateToStringCorrect() {
        String strDate = "Wed Apr 3 00:00:01 +1100 2013";
        Date date = new Date(1364907601000L);
        Date res = DateUtil.parse(DateUtil.toString(date));
        assertEquals(date, res);

    }

}
