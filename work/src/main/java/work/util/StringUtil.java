package work.util;

import java.util.Arrays;
import java.util.List;

public class StringUtil {
    
    public static boolean isUniqKeyword(String keyword, List<String> uniqList) {
        String notUniqList[] = {"語", "学"};

        if(Arrays.asList(notUniqList).contains(keyword) || uniqList.contains(keyword)) {
            return false;
        }
        
        return true;
    }
}
