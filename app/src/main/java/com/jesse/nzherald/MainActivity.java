package com.jesse.nzherald;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private RequestQueue requestQueue;
    private Context context;
    static final private String REQUEST_NEWS = "News";
    private JSONArray responseArray;
    public ArrayList<Article> articleArrayList;
    private RecyclerView recyclerView;
    private NewsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> list;

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
                                String date = jsonObject.getString("publishedAt").substring(0, 10);
                                String description = jsonObject.getString("description");
                                String photoURL = jsonObject.getString("urlToImage");
                                String articleURL = jsonObject.getString("url");
                                articleArrayList.add(new Article(title, author, date, description, photoURL, articleURL));
                            }
                            showRecyclerView();
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
                        Toast.makeText(context, "Volley Exception! ", Toast.LENGTH_SHORT).show();
                    }
                });
        jsonObjectRequest.setTag(REQUEST_NEWS); // TODO Cancel request on exit.
        requestQueue.add(jsonObjectRequest);
    }

    public void showRecyclerView()
    {
        recyclerView = findViewById(R.id.recycler);
        mAdapter = new NewsAdapter((List<Article>)articleArrayList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);
        list = articleArrayList;


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT)
        {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
            {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                list.add(toPos, list.remove(fromPos));
                mAdapter.notifyItemMoved(fromPos, toPos);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
            {
                final int position = viewHolder.getAdapterPosition();
                final Article deleted = list.get(position);
                switch (direction)
                {
                    case ItemTouchHelper.LEFT:
                        list.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        Snackbar.make(recyclerView, "Swiped left, item deleted. ", Snackbar.LENGTH_SHORT).
                                setAction("Undo", new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        list.add(position, deleted);
                                        mAdapter.notifyItemInserted(position);
                                    }
                                }).show();
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}
