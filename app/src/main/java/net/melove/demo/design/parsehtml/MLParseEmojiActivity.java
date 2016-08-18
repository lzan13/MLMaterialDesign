package net.melove.demo.design.parsehtml;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonWriter;
import android.view.View;
import android.widget.TextView;

import net.melove.demo.design.R;
import net.melove.demo.design.application.MLBaseActivity;
import net.melove.demo.design.utils.MLDateUtil;
import net.melove.demo.design.utils.MLFileUtil;
import net.melove.demo.design.utils.MLLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MLParseEmojiActivity extends MLBaseActivity {


    private final String KEY_EMOJI = "emoji";
    private final String KEY_EMOJI_UNICODE = "emoji-unicode";
    private final String KEY_EMOJI_UTF8 = "emoji-utf8";
    private final String KEY_EMOJI_ARRAY = "emoji-array";
    private final String KEY_EMOJI_CATEGORY = "emoji-category";
    private final String KEY_EMOJI_DESCRIPTION = "emoji-description";
    private final String KEY_EMOJI_ICON_URL_APPLE = "emoji-icon-apple";
    private final String KEY_EMOJI_ICON_URL_ANDROID = "emoji-icon-android";

    private String iconPrefix = "http://apps.timwhitlock.info";

    private TextView emojiTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_emoji);


        findViewById(R.id.ml_btn_parse_emoji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseEmojiToHtml();
            }
        });

        findViewById(R.id.ml_btn_show_emoji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmoji();
            }
        });
        emojiTextView = (TextView) findViewById(R.id.ml_text_emoji);
    }

    /**
     * 通过解析上 http://apps.timwhitlock.info/emoji/tables/unicode
     * 的信息得到 emoji 的 unicode 字符
     */
    private void parseEmojiToHtml() {
        /**
         * 开始构建json，参考下边的结构
         * {
         *      "emoji":[
         *          {
         *              "emoji-category":"Emoticons(1F601-1F64F)",
         *              "emoji-array":[
         *                  {
         *                      "emoji-icon-android":"http://apps.timwhitlock.info/static/images/emoji/emoji-android/1f601.png"
         *                      "emoji-icon-apple":"http://apps.timwhitlock.info/static/images/emoji/emoji-apple/1f601.png",
         *                      "emoji-description":"grinning face with smiling eyes",
         *                      "emoji":"1f601",
         *                  },
         *                  {
         *                      "emoji-icon-android":"http://apps.timwhitlock.info/static/images/emoji/emoji-android/1f602.png"
         *                      "emoji-icon-apple":"http://apps.timwhitlock.info/static/images/emoji/emoji-apple/1f602.png",
         *                      "emoji-description":"face with tears of joy",
         *                      "emoji":"1f602",
         *                  }
         *              ]
         *          },
         *          {
         *              "emoji-category":"Dingbats(2702-27B0)",
         *              "emoji-array":[
         *                  {
         *                      "emoji-icon-android":"http://apps.timwhitlock.info/static/images/emoji/emoji-android/2702.png"
         *                      "emoji-icon-apple":"http://apps.timwhitlock.info/static/images/emoji/emoji-apple/2703.png",
         *                      "emoji-description":"black scissors",
         *                      "emoji":"2702",
         *                  }
         *              ]
         *          }
         *      ]
         *  }
         */
        try {

            int count = 1;
            String emojiSavePath = MLFileUtil.getFilesFromSDCard() + count + "_" + MLDateUtil.getCurrentMillisecond() + ".json";
            MLFileUtil.createFile(emojiSavePath);
            OutputStream outputStream = new FileOutputStream(new File(emojiSavePath));
            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(outputStream, "utf-8"));
            /**
             * 开始构建 json 文件的头部
             * 首先开始一个 JSONObject
             */
            jsonWriter.beginObject();
            // 设置最外层的key
            jsonWriter.name(KEY_EMOJI);
            // 开始一个 JSONArray
            jsonWriter.beginArray();
            jsonWriter.beginObject();
            //            while (count <= ) {
            parseEmoji(count, jsonWriter);
            //                count++;
            //            }

            // 结束总的 emoji JSONArray
            jsonWriter.endArray();
            // 结束总的 emoji JSONObject
            jsonWriter.endObject();

            jsonWriter.flush();
            outputStream.flush();

            // 关闭 JsonWriter
            jsonWriter.close();
            // 关闭FileOutputStream
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseEmoji(int count, JsonWriter jsonWriter) throws IOException {
        // 从html文件读取网页字符串
        InputStream inputStream = getResources().getAssets().open("html/emoji-code-" + count + ".html");
        // 根据输入流获取document对象
        Document document = Jsoup.parse(inputStream, "utf-8", "http://apps.timwhitlock.info/emoji/tables/unicode");

        // 开始解析一层层剥离对象
        Element h3 = document.getElementsByClass("category").get(0);
        // 开始第一个分类
        jsonWriter.name(KEY_EMOJI_CATEGORY).value(h3.html());
        jsonWriter.name(KEY_EMOJI_ARRAY);
        jsonWriter.beginArray();

        Elements trs = document.getElementsByClass("table-bordered").get(0).child(0).getElementsByTag("tr");

        for (int i = 0; i < trs.size(); i++) {
            // 开始记录emoji
            jsonWriter.beginObject();

            // 获取emoji对应的值
            String codeUnicode = trs.get(i).child(7).child(0).html();
            String codeUtf8 = trs.get(i).child(8).html();
            String name = trs.get(i).child(9).html();

            jsonWriter.name(KEY_EMOJI_ICON_URL_ANDROID).value("http://apps.timwhitlock.info/static/images/emoji/emoji-android/" + codeUnicode + ".png");
            jsonWriter.name(KEY_EMOJI_ICON_URL_APPLE).value("http://apps.timwhitlock.info/static/images/emoji/emoji-apple/" + codeUnicode + ".png");
            jsonWriter.name(KEY_EMOJI_DESCRIPTION).value(name);
            jsonWriter.name(KEY_EMOJI_UNICODE).value(codeUnicode);
            jsonWriter.name(KEY_EMOJI_UTF8).value(codeUtf8);

            // 结束一个 emoji JSONObject
            jsonWriter.endObject();
        }
        // 结束一个 emoji category JSONArray
        jsonWriter.endArray();
        // 结束一个 emoji category JSONObject
        jsonWriter.endObject();
        inputStream.close();
    }


    private void showEmoji() {
        try {
            MLLog.i("current date millisecond -1- %d", MLDateUtil.getCurrentMillisecond());
            InputStream inputStream = getResources().getAssets().open("emoji_json_full.json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            MLLog.i("current date millisecond -2- %d", MLDateUtil.getCurrentMillisecond());

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.optJSONArray(KEY_EMOJI);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject emojiJSONObject = jsonArray.optJSONObject(i);
                String category = emojiJSONObject.optString(KEY_EMOJI_CATEGORY);

                emojiTextView.setText(category + "\n");

                JSONArray emojiJSONArray = emojiJSONObject.optJSONArray(KEY_EMOJI_ARRAY);
                for (int j = 0; j < emojiJSONArray.length(); j++) {
                    JSONObject object = emojiJSONArray.optJSONObject(j);
                    String icon1 = object.optString(KEY_EMOJI_ICON_URL_ANDROID);
                    String icon2 = object.optString(KEY_EMOJI_ICON_URL_APPLE);
                    String emojiDescription = object.optString(KEY_EMOJI_DESCRIPTION);
                    String emojiStr = object.optString(KEY_EMOJI).toUpperCase();

                    emojiTextView.append(" " + emojiStr);
                }

                emojiTextView.append("\n\n");
            }
            MLLog.i("current date millisecond -3- %d", MLDateUtil.getCurrentMillisecond());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
