package com.warren.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class DateUtilTest {
    @Test
    public void testDaysAgo() {
        Date date1 = new Date(119, 4, 2, 9, 30);
        Date date2 = new Date(119, 3, 30, 22, 30);
        Assert.assertEquals(2, DateUtil.diffDays(date1, date2));
    }
}
