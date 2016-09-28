package com.xminread;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView urlTextView;
    private Article article;

    String body; //deleteeeeeeeeeeeee

    private int ReadingSpeed;//in wpm
    //average adult =300
    //400
    //elevate
    //200, 300, 400 and 500

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        article = new Article();
        urlTextView = (TextView) findViewById(R.id.url);

        boolean connected = checkNetworkConnection();
        if (!connected) {
            urlTextView.setText("No network connection available.");
        } else {
            calculateMinutes();

            setSeekBarListener();
        }
    }

    private void setSeekBarListener() {
        SeekBar sb = (SeekBar) findViewById(R.id.seekbar);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //retrieve last value
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int currentValue = seekBar.getProgress() - 200;
                Toast.makeText(MainActivity.this, String.valueOf(currentValue), Toast.LENGTH_LONG).show();
            }
        });

        DiscreteSeekBar dsb = (DiscreteSeekBar) findViewById(R.id.discrete_seekbar);
        dsb.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                //Toast.makeText(MainActivity.this, "onprogresschanged", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                //Toast.makeText(MainActivity.this, "onstarttrackingtouch", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                Toast.makeText(MainActivity.this, "onstoptrackingtouch", Toast.LENGTH_LONG).show();
                //retrieve value
                int p = seekBar.getProgress();
                Toast.makeText(MainActivity.this, String.valueOf(p), Toast.LENGTH_LONG).show();
            }
        });
    }





    private void useJsoup(String url) {
        try {
            article.setUrl(url);

            //Document doc = Jsoup.connect(url).get();
//            String html = Jsoup.connect(url)
//                    .maxBodySize(0)
//                    .timeout(0)
//                    .get()
//                    .html();
//            Document doc = Jsoup.parse(html);
            Document doc = Jsoup.connect(url).get();

            String title = doc.title();
            article.setTitle(title);

            Element ele = doc.body();
            body = doc.body().text();
            article.setContent(doc.body().text().toString());
        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }

    private void calculateMinutes() {
        String stringUrl;
        if (getIntent().getAction().equals(Intent.ACTION_SEND)) {
            stringUrl = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            if (stringUrl.length() != 0) {
                new ArticleAnalyzerTask().execute(stringUrl);
            }
            //else show some message asking to go to browser
        }
    }

    private boolean checkNetworkConnection() {

        try {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public class ArticleAnalyzerTask extends AsyncTask<String, Void, Void> {//3rd parameter:ArrayList<String>

        @Override
        protected Void doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
//            try {
//                return downloadUrl(urls[0]);
//            } catch (IOException e) {
//                return "Unable to retrieve web page. URL may be invalid.";
//            }
            useJsoup(urls[0]);
            return null;
        }


        @Override
        protected void onPostExecute(Void v) {
            //calculate minutes and show on screen
            //float minutes = countWords(result);
            //urlTextView.setText(String.format("%d", minutes));

            //urlTextView.setText(result.get(0));
            String content = article.getContent();
            TextView tv_content = (TextView) findViewById(R.id.content);
            //tv_content.setText(content);

            TextView tv_title = (TextView) findViewById(R.id.title);
            tv_title.setText(article.getTitle());

            urlTextView.setText(article.getUrl());

            //Toast.makeText(MainActivity.this, body, Toast.LENGTH_LONG).show();

            //Toast.makeText(MainActivity.this, article.getUrl(), Toast.LENGTH_LONG).show();
            //urlTextView.setText("We want to read from " + article.getUrl());

            int words = countWords(article.getContent());
            tv_content.setText(String.valueOf(words));

        }

//        private float countWords(String result) {
//            //here is the real processing, yo
//            return 3.4f;
//        }


        public int countWords(String text) {
            String[] parts = text.trim().split("\\W+");
            if (parts.length == 1 && parts[0].isEmpty()) {
                return 0;
            }
            return parts.length;
        }


//        protected String downloadUrl(String stringUrl) throws IOException {
//            InputStream is = null;
//            String contentAsString = "";
//
//            try {
//                URL url = new URL(stringUrl);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(10000 /* milliseconds */);
//                conn.setConnectTimeout(15000 /* milliseconds */);
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                // Starts the query
//                conn.connect();
//                int response = conn.getResponseCode();
//                //Log.d(DEBUG_TAG, "The response is: " + response);
//                is = conn.getInputStream();
//
//                // Convert the InputStream into a string
//                contentAsString = readContent(is);
//
//
//                // Makes sure that the InputStream is closed after the app is
//                // finished using it.
//            } finally {
//                if (is != null) {
//                    is.close();
//                }
//            }
//            //return "<html></html>"; //for now
//            return contentAsString; //THIS IS THE REAL RETURN, UNCOMMENT
//        }


//        // Reads an InputStream and converts it to a String.
//        public String readContent(InputStream stream) throws IOException, UnsupportedEncodingException {
//
//            if (stream != null) {
//
//                Writer writer = new StringWriter();
//
//                try {
//                    Reader reader = new InputStreamReader(stream, "UTF-8");
//                    int n;
//                    char[] buffer = new char[1024];
//
//                    while ((n = reader.read(buffer)) != -1) {
//                        writer.write(buffer, 0, n);
//                    }
//
//                } finally {
//                    stream.close();
//                }
//                return writer.toString();
//            }
//            return "";
//        }
    }
}
