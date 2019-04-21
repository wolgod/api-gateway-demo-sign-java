import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.api.gateway.demo.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:heshan664754022@gmail.com">Frank</a>
 * @version V1.0
 * @description
 * @date 2019/4/21 12:14
 */
public class OcrTest {

    public static void main(String[] args) {
        String host = "https://ocrapi-ecommerce.taobao.com";
        String path = "/ocrservice/ecommerce";
        String method = "POST";
        String appcode = "";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        String filePath="E:\\workspace\\OCRImages\\aaa.jpg";
        String base64=imageChangeBase64(filePath);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("img",base64);
        jsonObject.put("url","");
        jsonObject.put("prob",false);

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, jsonObject.toJSONString());
            System.out.println(response.toString());
            //获取response的body
            String body=EntityUtils.toString(response.getEntity());
            System.out.println(body);
            JSONObject jsonObject1=JSONObject.parseObject(body);
            JSONArray array=jsonObject1.getJSONArray("prism_wordsInfo");
            StringBuffer buffer=new StringBuffer();
            for (int i = 0; i <array.size() ; i++) {

                JSONObject a=(JSONObject)array.get(i);
                System.out.println(a.get("word"));
                buffer.append(a.get("word"));
            }
            System.out.println(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 图片转BASE64
     * @param imagePath 路径
     * @return
     */
    public static String imageChangeBase64(String imagePath){
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imagePath);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
}
