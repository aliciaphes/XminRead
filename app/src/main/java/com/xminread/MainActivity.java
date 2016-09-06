package com.xminread;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView url_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url_tv = (TextView) findViewById(R.id.url);

        boolean connected = checkNetworkConnection();
        if(!connected){
            url_tv.setText("No network connection available.");
        }
        else{
            float read = calculateMinutes();
            url_tv.setText(String.valueOf(read));
        }
    }

    private float calculateMinutes() {
        String stringUrl="";
        new ArticleAnalyzerTask().execute(stringUrl);
        return 3.4f;
    }

    private boolean checkNetworkConnection() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }



    public class ArticleAnalyzerTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            return urls[0];
//        try {
//            //return downloadUrl(urls[0]);
//        } catch (IOException e) {
//            return "Unable to retrieve web page. URL may be invalid.";
//        }
        }

        @Override
        protected void onPostExecute(String result) {
            url_tv.setText(result);
        }
    }
}
