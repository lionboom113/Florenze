package com.codepath.apps.restclienttemplate.Fragment;


import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.HomeTweet;
import com.codepath.apps.restclienttemplate.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostTweetFragment extends DialogFragment {
    @BindView(R.id.etPostTweet)
    EditText etPostTweet;
    @BindView(R.id.btnPostTweet)
    Button btnPostTweet;

    public PostTweetFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static PostTweetFragment newInstance(String title) {
        PostTweetFragment frag = new PostTweetFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_tweet_fragment, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Show soft keyboard automatically and request focus to field
        etPostTweet.requestFocus();
        btnPostTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeTweet homeTweetActivity = (HomeTweet) getActivity();
                homeTweetActivity.postTweet(etPostTweet.getText().toString());
                dismiss();
            }
        });

    }
    public void postTweet(View view){

    }

}
