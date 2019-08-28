package com.doniapr.githubuser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_USER = "extra_user";
    ImageView imgAvatar;
    TextView txtName, txtUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imgAvatar = findViewById(R.id.img_detail_avatar);
        txtName = findViewById(R.id.txt_detail_name);
        txtUrl = findViewById(R.id.txt_detail_url);

        User user = getIntent().getParcelableExtra(EXTRA_USER);
        Glide.with(getApplicationContext())
                .load(user.getAvatar())
                .into(imgAvatar);
        txtName.setText(user.getName());
        txtUrl.setText(user.getUrl());

    }
}
