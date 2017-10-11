import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac-pc on 16/8/19.
 */

public class HttpUtil {
    public static String doGet(String url, Map<String, Object> params){
        String result = "";
        String paramStr = "";
        if (params != null && !params.isEmpty()) {
            paramStr = map2Url(params);
        }
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + paramStr;
            System.out.print(urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    private static String map2Url(Map<String, Object> params) {
        String paramStr = "";
        for (String key : params.keySet()) {
            paramStr += key + "=" + params.get(key) + "&";
        }
        return paramStr;
    }
    public static void main(String[] args) {
        String str = "' 车牌号 '".replaceAll("[\'\\s\\n\\t]", "");
        System.out.println(str);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("x", 118.232589);
        param.put("y", 37.1953427708);
        param.put("token","BF833A1145CB44669FB387DEA62AC464" );
//        String result = HttpUtil.doGet("http://www.baidu.com", param);
        String result = HttpUtil.doGet("http://172.16.100.146:8000/networkserver/rest/networkservice/nearroad", param);
        Gson gson = new Gson();
        Map map = gson.fromJson(result,
                new TypeToken<Map<String, Object>>() {
                }.getType());
        if ("1".equals(map.get("returnFlag"))) {
            Map obj = (Map)map.get("data");
            if (obj != null && !"".equals(obj)) {
                List point = (List)obj.get("point");
                if (point != null && point.size() > 0) {
                    System.out.println("x-----");
                    System.out.println("x-----" + Double.parseDouble(point.get(0).toString()));
                    System.out.println("y-------" + Double.parseDouble(point.get(1).toString()));
                }
            }
        }



    }

}