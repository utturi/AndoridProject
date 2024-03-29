package com.example.andoridproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.example.andoridproject.Adapter.CommentAdapter;
import com.example.andoridproject.Etc.DBHelper2;
import com.example.andoridproject.Item.Board;
import com.example.andoridproject.Item.Comment;
import com.example.andoridproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class PostDetailActivity extends AppCompatActivity {
    Board post;
    ArrayList<Comment> comments;
    CommentAdapter adapter;
    ListView listView;
    EditText edittext;
    ImageView popup;
    MaterialButton button;
    TextView contents;
    TextView title;
    TextView userName;
    ImageView star_button;
    SwipeRefreshLayout refresh;
    Boolean star_click;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        //변수 초기화 및 인텐트 처리
        post = (Board)getIntent().getSerializableExtra("post");
        comments = new ArrayList<Comment>();
        userName = findViewById(R.id.detail_name);
        title = findViewById(R.id.detail_title);
        contents = findViewById(R.id.detail_contents);
        button = findViewById(R.id.buttonPostComment);
        edittext = findViewById(R.id.detail_text);
        listView = findViewById(R.id.detail_listview);
        popup = findViewById(R.id.popupMenu);
        refresh = findViewById(R.id.detail_refresh);
        refresh.setColorSchemeResources(R.color.MyGreen);
        star_button = findViewById(R.id.star_button);

        //초기화면 구성
        userName.setText(post.getUserName());
        title.setText(post.getTitle());
        contents.setText(post.getContent());
        isWriter();
        setList();
        setStar();
        //리스너
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        comments.clear();
                        setList();
                        refresh.setRefreshing(false);
                    }
                },1000);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeComment();
                comments.clear();//ArrayList초기화
                setList();//comment
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
            }
        });
        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopup(v);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(comments.get(position).getUserID())) {
                    deleteComment(position);
                }
                return false;
            }
        });

        //사용자 이름 클릭했을때
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(post.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) { }
                else {
                    Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                    intent.putExtra("post", post); //이 안에 게시자의 이름, id가 들어있다.
                    startActivity(intent);
                }
            }
        });
        //starbutton 클릭
        star_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference setBoard = FirebaseDatabase.getInstance().getReference("posts");
                DatabaseReference setStar = FirebaseDatabase.getInstance().getReference("stars").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                if(star_click == false)
                {
                    star_button.setImageDrawable(getResources().getDrawable(R.drawable.star_2));
                    star_click = true;
                    String post_id= post.getKey();
                    DBHelper2 helper = new DBHelper2(getApplicationContext());
                    SQLiteDatabase db = helper.getWritableDatabase();
                    db.execSQL("INSERT INTO STARPOST VALUES (NULL,?)",new String[]{post_id});
                    post.setStars(post.getStars()+1);
                    setBoard.child(post.getKey()).setValue(post);
                    setStar.child(post.getKey()).setValue("star click!");
                }
                else
                {
                    star_button.setImageDrawable(getResources().getDrawable(R.drawable.star_1));
                    star_click = false;
                    DBHelper2 helper = new DBHelper2(getApplicationContext());
                    SQLiteDatabase db = helper.getWritableDatabase();
                    String sql = "DELETE FROM STARPOST WHERE postID = '" + post.getKey() +"';";
                    db.execSQL(sql);
                    post.setStars(post.getStars()-1);
                    setBoard.child(post.getKey()).setValue(post);
                    setStar.child(post.getKey()).removeValue();
                    Toast.makeText(getApplicationContext(),"취소되었습니다",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //메소드
    private void writeComment()
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("comments").child(post.getKey());
        DatabaseReference setComments = FirebaseDatabase.getInstance().getReference("posts");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String dataset[] = email.split("@");
        String username = dataset[0];
        String text = ((EditText)findViewById(R.id.detail_text)).getText().toString();
        post.setComments(post.getComments()+1);

        if(text.length()>0) {
            Comment comment = new Comment(text,userId,username);
            database.push().setValue(comment);
            setComments.child(post.getKey()).setValue(post);
            edittext.setText("");
        }
        else
        {
            Toast.makeText(getApplicationContext(), "내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void setList()
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("comments").child(post.getKey());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot messageData : dataSnapshot.getChildren())
                {

                    String key = messageData.getKey();
                    Comment data = messageData.getValue(Comment.class);
                    data.setKey(key);
                    comments.add(data);
                }
                adapter = new CommentAdapter(comments);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void isWriter() {
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(post.getUserID()))
            popup.setVisibility(View.VISIBLE);
    }

    private void setPopup(View v)
    {
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        Menu menu = popupMenu.getMenu();
        inflater.inflate(R.menu.popup,menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.popup_edit)
                {
                    Log.e("PopUp","edit button click!");
                    Intent intent = new Intent(getApplicationContext(),PostActivity.class);
                    intent.putExtra("post",post);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PostDetailActivity.this);
                    dialogBuilder.setTitle("Delete");
                    dialogBuilder.setMessage("\n게시글을 정말 삭제하시겠습니까??")
                            .setCancelable(true)
                            .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                                    database.child("comments").child(post.getKey()).removeValue();
                                    database.child("posts").child(post.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(),"삭제 성공",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),"삭제 실패",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    finish();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    dialogBuilder.show();
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void deleteComment(int position)
    {
        final String key = comments.get(position).getKey();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PostDetailActivity.this);
        dialogBuilder.setMessage("댓글을 삭제하시겠습니까?")
                .setCancelable(true)
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        database.child("comments").child(post.getKey()).child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                DatabaseReference setBoard = FirebaseDatabase.getInstance().getReference("posts");
                                post.setComments(post.getComments()-1);
                                setBoard.child(post.getKey()).setValue(post);
                                Toast.makeText(getApplicationContext(),"삭제 성공",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"삭제 실패",Toast.LENGTH_SHORT).show();
                            }
                        });
                        comments.clear();
                        setList();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dialogBuilder.show();
    }
    private void setStar()
    {
        DBHelper2 helper = new DBHelper2(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT postID FROM STARPOST";
        Cursor cursor = db.rawQuery(sql, null);
        for(int i = 0; i<cursor.getCount();i++) {
            cursor.moveToNext();
            String posts = cursor.getString(0);

            if(posts.equals(post.getKey())) {
                star_button.setImageDrawable(getResources().getDrawable(R.drawable.star_2));
                star_click = true;
                return;
            }
        }
        star_click = false;
    }
}
