package com.jesse.nzherald;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private RequestQueue requestQueue;
    private Context context;
    static final private String REQUEST_NEWS = "News";
    private JSONArray responseArray;
    public ArrayList<Article> articleArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        articleArrayList = new ArrayList<>();

        fetchNews();
    }

    private void fetchNews()
    {
        requestQueue = Volley.newRequestQueue(context);
        String url = "https://newsapi.org/v2/top-headlines?country=nz&apiKey=7f32fd5b23e947abafa4b92c55b42898";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            Toast.makeText(context, "Got JSON! ", Toast.LENGTH_SHORT).show();
                            responseArray = response.getJSONArray("articles");
                            for (int i=0; i<responseArray.length(); i++)
                            {
                                JSONObject jsonObject = responseArray.getJSONObject(i);
                                String title = jsonObject.getString("title");
                                String author = jsonObject.getString("author");
                                String date = jsonObject.getString("date");
                                String description = jsonObject.getString("description");
                                String photoURL = jsonObject.getString("urlToImage");
                                articleArrayList.add(new Article(title, author, date, description, photoURL));
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(context, "JSON Exception! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(context, "JSON Exception! ", Toast.LENGTH_SHORT).show();
                    }
                });
        jsonObjectRequest.setTag(REQUEST_NEWS); // TODO Cancel request on exit.
        requestQueue.add(jsonObjectRequest);
    }

}
