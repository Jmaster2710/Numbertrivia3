package com.example.joel_.numbertrivia3;

import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TriviaObjectAdapter mAdapter;
    private RecyclerView mRecyclerView;
    final List<TriviaObject> mTriviaObjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assigning the layout manager.
        mRecyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TriviaObjectAdapter(this, mTriviaObjects);
        mRecyclerView.setAdapter(mAdapter);


        FloatingActionButton fab = findViewById(R.id.floatingButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestData();
            }
        });
    }

    public void setQuoteTextView(String message, int number) {
        mTriviaObjects.add(new TriviaObject(message, number));
        updateUI();
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new TriviaObjectAdapter(this , mTriviaObjects);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mTriviaObjects);
        }
    }

    private void requestData() {
        NumbersApiServices service = NumbersApiServices.retrofit.create(NumbersApiServices.class);
        Random random = new Random();
        final int number = random.nextInt(500);

        /**
         * Make an a-synchronous call by enqueing and definition of callbacks.
         */

        Call<TriviaItem> call = service.getQuote(number);
        call.enqueue(new Callback<TriviaItem>() {

            @Override
            public void onResponse(Call<TriviaItem> call, Response<TriviaItem> response) {
                TriviaItem dayQuoteItem = response.body();
                setQuoteTextView(dayQuoteItem.getText(), number);
            }

            @Override
            public void onFailure(Call<TriviaItem> call, Throwable t) {
            }

        });

    }

}
