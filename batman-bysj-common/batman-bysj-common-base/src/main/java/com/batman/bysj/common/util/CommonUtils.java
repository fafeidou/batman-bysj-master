package com.batman.bysj.common.util;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jacky, Jonas
 * @version 0.0.2
 */
public final class CommonUtils {

    public static final String USERNAME = "USERNAME";

    public static final String COMPUTERNAME = "COMPUTERNAME";

    public static final String IP = "IP";

    public static final String MAC = "MAC";

    private static final char[] STR = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static boolean isPhoneNum(String phone) {

        // 电话号码以1开头的11位数字
        Pattern pattern = Pattern.compile("^1[\\d]{10}$");

        Matcher match = pattern.matcher(phone);

        return match.matches();
    }

    /**
     * 分割List
     *
     * @param list     待分割的list
     * @param pageSize 每段list的大小
     * @return List[List[T]]
     */
    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {

        int total = list.size();
        int start = 0;
        int end;

        List<List<T>> listArray = new ArrayList<>();

        if (total <= pageSize) {
            listArray.add(list);
            return listArray;
        }

        while (start < total) {

            end = start + pageSize;

            if (end > total) end = total;

            listArray.add(list.subList(start, end));

            start = end;
        }

        return listArray;
    }

    /**
     * 保留两位小数
     */
    public static double getRoundUp2Digits(double doubleValue) {
        BigDecimal bd1 = BigDecimal.valueOf(doubleValue);
        return bd1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Map<String, String> getLocalInfo() {

        Map<String, String> infoMap = new HashMap<>();

        Formatter formatter = null;

        try {
            InetAddress address = InetAddress.getLocalHost();

            infoMap.put(IP, address.getHostAddress());

            infoMap.put(COMPUTERNAME, address.getHostName());

            NetworkInterface ni = NetworkInterface.getByInetAddress(address);

            byte[] mac = ni.getHardwareAddress();

            formatter = new Formatter();

            for (int i = 0; i < mac.length; i++)
                formatter.format("%s%X", (i > 0) ? ":" : "", mac[i] & 0xFF);

            infoMap.put(MAC, formatter.toString());

        } catch (UnknownHostException ignored) {
            infoMap.put(IP, "UnknownHost");
        } catch (SocketException e) {
            infoMap.put(MAC, "SocketException");
        } finally {
            if (formatter != null) {
                formatter.close();
            }
        }

        Map<String, String> env = System.getenv();

        if (env != null) {
            // 使用 env 数据尝试补全
            if (!infoMap.containsKey(COMPUTERNAME) && env.containsKey(COMPUTERNAME))
                infoMap.put(COMPUTERNAME, env.get(COMPUTERNAME));

            infoMap.put(USERNAME, env.get(USERNAME));
        }

        return infoMap;
    }

    /**
     * 获取IP Set
     */
    public static Set<String> getLocalIPs() {
        Set<String> ipList = new HashSet<>();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        ipList.add(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException ignored) {
        }
        return ipList;
    }

    /**
     * 随机生成密码
     */
    public static String getRandomPassword(int length) {
        int maxNum = STR.length;
        StringBuilder pwd = new StringBuilder("");
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            pwd.append(STR[r.nextInt(maxNum)]);
        }
        return pwd.toString();
    }

    /**
     * 如果对象不为空，就获取其属性值，否则返回 null
     */
    public static <I> String getIfNotNull(I object, Function<I, String> function) {
        if (object == null)
            return null;
        return function.apply(object);
    }
}
