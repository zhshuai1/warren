package com.warren.strategy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.warren.model.StockDayInfo;
import com.warren.model.TradingEntry;
import com.warren.strategy.model.MinuteDataEntry;
import com.warren.strategy.model.TradingResult;
import com.warren.util.DateUtil;

import java.util.Date;
import java.util.List;

public interface Strategy {
    /**
     * import indexes:
     * ==============sh000001================
     * 0: open price including prevclose; 09:30
     * 119: last effective trading entry; 11:29
     * 120: volume=0, ineffective
     * 121: 13:01
     * 239: 14:59
     * 240: volume=0;
     * 241: volume=0
     * 242: close
     * ================other================
     * 0: open price including prevclose: 09:30
     * 119: 11:29
     * 120: 11:30
     * 121: volume=0
     * 122: 13:01
     * slightly different, but seems no need to make it so clear...
     */
    default List<MinuteDataEntry> parse(String rawData) {
        return new Gson().fromJson(rawData, new TypeToken<List<MinuteDataEntry>>() {
        }.getType());
    }

    default int normalizeOffset(int index) {
        if (index <= 120) {
            return index;
        } else {
            return index + 89;
        }
    }

    default Date index2Time(String dateStr, int index) {
        Date date = DateUtil.parseFromFull(dateStr);
        int absoluteMinute = normalizeOffset(index) + 570;
        int hour = absoluteMinute / 60;
        int minute = absoluteMinute % 60;
        date.setHours(hour);
        date.setMinutes(minute);
        return date;

    }

    boolean checkCandidate(List<StockDayInfo> stockDayInfos, int index);

    TradingResult checkBuy(List<StockDayInfo> stockDayInfos, int index, TradingEntry lastTradingEntry);

    TradingResult checkSell(List<StockDayInfo> stockDayInfos, int index, TradingEntry lastTradingEntry);
}
