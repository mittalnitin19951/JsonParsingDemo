package com.mittal.nitin.jsonparsingdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mittal.nitin.jsonparsingdemo.Adapter.MovieRecyclerAdapter;
import com.mittal.nitin.jsonparsingdemo.Helper.OnStartDragListener;
import com.mittal.nitin.jsonparsingdemo.Helper.SimpleItemTouchHelperCallback;
import com.mittal.nitin.jsonparsingdemo.models.MovieModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnStartDragListener{

    private final String URL_TO_HIT = "http://jsonparsing.parseapp.com/jsonData/moviesData.txt";
    private List<MovieModel> movieModelList;
    private RecyclerView recyclerView;
    private MovieRecyclerAdapter adapter;
    private ProgressBar progressBar;
    private ItemTouchHelper mItemTouchHelper;
    OnStartDragListener onStartDragListener=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);


        recyclerView=(RecyclerView)findViewById(R.id.movieRecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));



        new JSONTask().execute(URL_TO_HIT);

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

    }

    public class JSONTask extends AsyncTask<String,String, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params)
        {
            progressBar.setVisibility(View.VISIBLE);
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            int result=0;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int statuscode=connection.getResponseCode();

                //200 represents Http Ok
                if(statuscode==200)
                {
                    InputStream stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder response = new StringBuilder();
                    String line ="";
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result=1;//successful
                }
                else
                {
                    result=0;//failed to fecth the result
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.INVISIBLE);

            if(result==1)
            {
                adapter=new MovieRecyclerAdapter(MainActivity.this,movieModelList,onStartDragListener);
                recyclerView.setAdapter(adapter);

                //item callback method
                ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                mItemTouchHelper = new ItemTouchHelper(callback);
                mItemTouchHelper.attachToRecyclerView(recyclerView);

            }
            else{
                Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void parseResult(String finalJson)
    {
        try{

            JSONObject parentObject=new JSONObject(finalJson);
            JSONArray parentArray=parentObject.getJSONArray("movies");
            movieModelList=new ArrayList<>();

            for(int i=0;i<parentArray.length();i++)
            {
                JSONObject finalObject=parentArray.getJSONObject(i);
                MovieModel movieModel=new MovieModel();
                movieModel.setImage(finalObject.getString("image"));
                movieModel.setMovie(finalObject.getString("movie"));
                movieModel.setYear(finalObject.getInt("year"));
                movieModel.setStory(finalObject.getString("story"));
//                movieModel.setRating((float) finalObject.getDouble("movie"));
//                movieModel.setDirector(finalObject.getString("director"));
//                movieModel.setDuration(finalObject.getString("duration"));
//                movieModel.setTagline(finalObject.getString("tagline"));
//                List<Cast> castList=new ArrayList<>();
//                for(int j=0;j<finalObject.getJSONArray("cast").length();j++)
//                {
//                    Cast cast=new Cast();
//                    cast.setName(finalObject.getJSONArray("cast").getJSONObject(j)
//                            .getString("name"));
//                    castList.add(cast);
//                }
//                movieModel.setCastList(castList);
                movieModelList.add(movieModel);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }






}
