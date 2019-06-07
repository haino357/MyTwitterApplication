package com.websarva.wings.android.mytwitterapplication;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TwitterAPI extends AsyncTask<String, String, String> {

    // JSON形式の要素名
    public static final String JSON_ELEMENT_COUNT = "count";
    public static final String JSON_ELEMENT_SINCE_ID = "since_id";
    public static final String JSON_ELEMENT_MAX_ID = "max_id";
    public static final String JSON_ELEMENT_TRIM_USER = "trim_user";
    public static final String JSON_ELEMENT_EXCLUDE_REPLIES = "exclude_replies";

    // WebAPIのリクエストメソッド
    private static final String WEB_API_REQUEST_METHOD_POST = "POST";
    private static final String WEB_API_REQUEST_METHOD_GET = "GET";

    // エンドポイントURL
    private static final String endPointURL = "https://api.twitter.com/oauth/request_token";

    //WebAPIのレスポンス情報
    public static final String WEB_API_RESPONSE_IS_STAY_STAY = "1";

    //扱う文字コード
    private static final String CHAR_SET_NAME = "UTF-8";

    //Listenerを定義する。
    private TwitterAPI.TwitterAPIListener TwitterAPIListener;

    /**
     * TwitterAPIの実行結果を取得するためのメソッド
     */
    interface TwitterAPIListener {
        void onPostExecuted();
    }

    /**
     * TwitterAPIのリスナーを設定する。
     *
     * @param listener
     */
    public void TwitterAPIListener(TwitterAPI.TwitterAPIListener listener) {
        this.TwitterAPIListener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String responseData = "";

        JSONObject paramsJSONObject = null;

        URL url = null;
        HttpURLConnection urlConnection = null;

        String jsonText;
        byte[] jsonBytes;
        OutputStream outputStream = null;

        int responseCode;

        InputStream inputStream = null;

        try {
            // WebAPIを実行
            url = new URL(endPointURL);

            paramsJSONObject = new JSONObject();
//            paramsJSONObject.put()


            //
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod(WEB_API_REQUEST_METHOD_GET);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            jsonText = paramsJSONObject.toString();
            jsonBytes = jsonText.getBytes(CHAR_SET_NAME);

            // 接続に書き込みを行う
            outputStream = urlConnection.getOutputStream();
            outputStream.write(jsonBytes);
            outputStream.flush();
            outputStream.close();

            // URLが参照するリソースへの通信リンクを確立する。
            urlConnection.connect();

            //HTTP 応答メッセージからの状態コードを取得する。
            responseCode = urlConnection.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                // 接続に失敗した場合はエラーとして、""を返却する。
                responseData = "";
                return responseData;
            }

            // この接続からの入力を受け取り、返り値に設定する。
            inputStream = urlConnection.getInputStream();
            responseData = GetStringFromInputStream(inputStream);
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if(urlConnection != null) {
                // サーバーへの要求を終了する。
                urlConnection.disconnect();
            }
        }
        return responseData;
    }

    /**
     * WebAPIの実行処理(非同期処理)が終了したときの処理としてリスナーに値を返却する。
     *
     * @param s WebAPIからの返り値
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        JSONObject jsonObject;
        JSONObject jsonContent;

    }

    /**
     * inputStreamからStringに変換(UTF-8)する。
     *
     * @param inputStream
     * @return inputStreamから読み込んだ文字列
     */
    private String GetStringFromInputStream(InputStream inputStream) {
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;
        String line = "";

        try {
            //inputStreamReaderとbufferedReaderをかいして、StringBuilderにすべての文字列を設定し、Stringで出力する。
            //inputStream → inputStreamReader → bufferedReaderに変換。
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, CHAR_SET_NAME));

            //1行ずつbufferedReaderから取得し、stringBuilderに設定。
            stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            //文字列返却
            return stringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
