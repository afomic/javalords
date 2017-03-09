package com.afomic.javalords.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afomic.javalords.R;
import com.afomic.javalords.data.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class DetailActivity extends AppCompatActivity {
    String  mUsername,mProfileURL,mImageURl;
    FloatingActionButton share;
    Toolbar toolbar;
    ImageView profileImage;
    CollapsingToolbarLayout profileCollasping;
    TextView username,profileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle data=getIntent().getExtras();
        mUsername=data.getString(Constants.USERNAME);
        mProfileURL=data.getString(Constants.PROFILE_URL);
        mImageURl=data.getString(Constants.PROFILE_PICTURE_URL);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        profileCollasping=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
       getSupportActionBar().setTitle("");

        profileImage=(ImageView) findViewById(R.id.detail_image);
        username=(TextView) findViewById(R.id.detail_username);
        profileUrl=(TextView) findViewById(R.id.detail_profile);
        //set values
        username.setText(mUsername);
        profileUrl.setText(mProfileURL);
        Glide.with(this).load(mImageURl)
                .placeholder(R.drawable.placeholder)
                .into(profileImage);
        setGotoSpan(profileUrl,mProfileURL);


        share=(FloatingActionButton) findViewById(R.id.fab);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg="“Check out this awesome developer @"+mUsername +","+mProfileURL;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                intent.setType("text/plain");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent,"Send Developer ..."));
                }

            }
        });


    }
    public void setGotoSpan(TextView view,String url){
        String text=view.getText().toString();
        SpannableString spannableString=new SpannableString(text);
        spannableString.setSpan(new OpenURL(url), 0, text.length(), 0);
        view.setMovementMethod(new LinkMovementMethod());
        view.setText(spannableString);
    }
    public class OpenURL extends ClickableSpan{
        String url;
        private OpenURL(String url){
            this.url=url;
        }

        @Override
        public void onClick(View widget) {
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent,"View Profile ..."));
            }
        }
    }
}
