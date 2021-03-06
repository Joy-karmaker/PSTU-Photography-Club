package com.example.pstuphotographyclub;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView blogList;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance().getReference().child("Blog");

        blogList = (RecyclerView)findViewById(R.id.blog_list);
        blogList.setHasFixedSize(true);
        blogList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<blog, BlogViewHolder>(

                blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                database )
        {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, blog model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setCaption(model.getCaption());
                viewHolder.setImage(getApplicationContext(), model.getImage());
            }
        };
        blogList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends  RecyclerView.ViewHolder {

        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView post_title = (TextView)mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setCaption(String caption) {
            TextView post_caption = (TextView)mView.findViewById(R.id.post_caption);
            post_caption.setText(caption);
        }

        public void setImage(Context c, String image) {
            ImageView post_image = (ImageView)mView.findViewById(R.id.post_image);
            Picasso.with(c).load(image).into(post_image);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add) {

            startActivity(new Intent(MainActivity.this, PostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
