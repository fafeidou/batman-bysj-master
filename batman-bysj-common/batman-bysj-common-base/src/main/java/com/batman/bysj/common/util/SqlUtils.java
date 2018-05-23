package com.batman.bysj.common.util;

public final class SqlUtils {

    /**
     * 对content的内容进行转换后，使用/作为oracle的转义字符 <br>
     * 既能达到效果,而且java代码相对容易理解，建议这种使用方式 <br>
     * `name` like concat('%', #{content,jdbcType=VARCHAR}, '%')  ESCAPE '/' "这种拼接sql看起来也容易理解 <br>
     *
     * @param content
     * @return
     */
    public static String decodeSpecialCharsWhenLikeUseBackslash(String content) {
        if (content == null) return null;

        // 由于使用了/作为ESCAPE的转义特殊字符,所以需要对该字符进行转义
        // 这里的作用是将"a/a"转成"a//a"
        String afterDecode = content.replaceAll("/", "//");
        // 使用转义字符 /,对特殊字符% 进行转义,只作为普通查询字符，不是模糊匹配
        afterDecode = afterDecode.replaceAll("%", "/%");
        // 使用转义字符 /,对特殊字符_ 进行转义,只作为普通查询字符，不是模糊匹配
        afterDecode = afterDecode.replaceAll("_", "/_");
        return afterDecode;
    }

    /**
     * 对content的内容进行转换后，使用\作为的转义字符。<br>
     * 这种做法也能达到目的，但不是好的做法，比较容易出错，而且代码很难看懂。 <br>
     * 因此建议使用 decodeSpecialCharsWhenLikeUseBackslash <br>
     * `name` like concat('%', #{content,jdbcType=VARCHAR}, '%') <br>
     *
     * @param content
     */
    public static String decodeSpecialCharsWhenLikeUseSlash(String content) {
        if (content == null) return null;

        // 由于使用了\作为ESCAPE的转义特殊字符,所以需要对该字符进行转义
        // 由于\在java和正则表达式中都是特殊字符,需要进行特殊处理
        // 这里的作用是将"a\a"转成"a\\a"
        String afterDecode = content.replaceAll("\\\\", "\\\\\\\\");
        // 使用转义字符 \,对特殊字符% 进行转义,只作为普通查询字符，不是模糊匹配
        afterDecode = afterDecode.replaceAll("%", "\\\\%");
        // 使用转义字符 \,对特殊字符_ 进行转义,只作为普通查询字符，不是模糊匹配
        afterDecode = afterDecode.replaceAll("_", "\\\\_");
        return afterDecode;
    }
}
