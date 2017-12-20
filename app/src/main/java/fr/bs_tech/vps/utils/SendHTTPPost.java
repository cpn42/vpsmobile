package fr.bs_tech.vps.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cpellerin on 13/12/2017.
 */

public class SendHTTPPost extends AsyncTask<String, Void, String>
//public class SendHTTPPost
{
    private String result;

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args)
    {
        String address = args[0];
        String data = args[1];
        OutputStreamWriter wr;
        HttpURLConnection conn;
        String text = "";

        setResult(null);

        try
        {
            URL url = new URL(address);
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //conn.setReadTimeout(10000);
            //conn.setConnectTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.connect();


            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            int res = conn.getResponseCode();
            if(res == 200)
            {
                InputStream input = new BufferedInputStream(conn.getInputStream());
                text = InputStream2String(input);
                if(text == null)
                    text = "";
                this.setResult(text);
            }
            else
            {
                this.setResult("ERROR");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return text;
    }

    @Override
    protected void onPostExecute(String result)
    {

        Log.d("HTTP POST", result);
    }

    private String InputStream2String(InputStream is)
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder result = new StringBuilder();
        String line = "";

        while(line != null)
        {
            try
            {
                line = br.readLine();
                if(line != null)
                    result.append(line);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return result.toString();
    }
}
