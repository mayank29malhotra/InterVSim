package com.example.interviewbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatbotActivity extends AppCompatActivity {
    static int idx = 1;
    static int qidx = 2;
    String list_of_question = "";
    String[] questionArray;
    String[] answerArray;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(100, TimeUnit.SECONDS)
            .build();

    private RecyclerView chat;
    private EditText userText;
    private FloatingActionButton sendMsg;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ChatsModel>chatsModelArrayList;
    private ChatBoxAdapter chatBoxAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_page);
        Intent j = getIntent();
        String init = j.getStringExtra("final_string");

        chat = findViewById(R.id.idRLMsg1);
        userText = findViewById(R.id.idEdtMessage);
        sendMsg = findViewById(R.id.sendbutton);
        chatsModelArrayList = new ArrayList<>();
        chatBoxAdapter = new ChatBoxAdapter(chatsModelArrayList,this);
        LinearLayoutManager manager =  new LinearLayoutManager(this);
        chat.setLayoutManager(manager);
        chat.setAdapter(chatBoxAdapter);
        addResponse("Getting Questions...");
        callAPI(init, true);

//        sendMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(userText.getText().toString().isEmpty()){
//                    Toast.makeText(ChatbotActivity.this, "Please enter your message", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                getResponse(userText.getText().toString());
//                chatsModelArrayList.add(new ChatsModel(userText.getText().toString(),USER_KEY));
//                userText.setText("");
//            }
//        });
    }
    void initialresponse(String response)
    {
        chatsModelArrayList.remove(chatsModelArrayList.size()-1);
        chatsModelArrayList.remove(chatsModelArrayList.size()-1);
        list_of_question = response;
        questionArray = list_of_question.split("\\d+\\.\\s*");
        addResponse((questionArray[0]));
        try {
            addResponse(questionArray[1]);
        }catch (Exception e)
        {
//            Toast.makeText(this, "Not got ques", Toast.LENGTH_SHORT).show();
        }
        storeans(questionArray);
    }
    void addResponse(String response){
//        chatsModelArrayList.remove(chatsModelArrayList.size()-1);
        addToChat(response,BOT_KEY);
    }
    private void storeans(String[] questionArray) {
        // sendButton.setEnabled(true);
        answerArray = new String[questionArray.length];
        answerArray[0] = "";

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = userText.getText().toString().trim();
               // chatsModelArrayList.add(new ChatsModel(a,USER_KEY));
                addToChat(a,USER_KEY);
                userText.setText("");
                answerArray[idx++] = a;

                try{
                    if(idx==questionArray.length)
                    {
                        //  sendButton.setEnabled(false);
                        Toast.makeText(ChatbotActivity.this, "All ans feeded Geeting feedback", Toast.LENGTH_SHORT).show();
                        getfeedback(questionArray, answerArray);

                    }else{
                        addResponse(questionArray[qidx++]);

                    }}catch (Exception e)
                {
                    Toast.makeText(ChatbotActivity.this, "All ans stored Geeting feedback", Toast.LENGTH_SHORT).show();
                    getfeedback(questionArray, answerArray);
                }
            }
        });
    }

    void addToChat(String message,String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatsModelArrayList.add(new ChatsModel(message,sentBy));
                chatBoxAdapter.notifyDataSetChanged();
                chat.smoothScrollToPosition(chatBoxAdapter.getItemCount());

            }
        });
    }
    public void getfeedback(String[] questionArray, String[] answerArray)
    {
        String feedbackques = "";
        for (int i = 1; i < questionArray.length; i++) {
            try {
                feedbackques += "ques: " + questionArray[i] + " ans: " + answerArray[i];
            }catch(Exception e)
            {

            }
        }

        feedbackques += "These are the Questions asked by interviewer along with their Answers Please tell how to improve these answers and What are right answer for it for How to Improve each Answer";
        callAPI(feedbackques, false);
    }
    void callAPI(String question, boolean initial){
        //okhttp
        chatsModelArrayList.add(new ChatsModel("Typing... ",BOT_KEY));
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model","gpt-3.5-turbo");
            JSONArray messageArr = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("role","user");
            obj.put("content",question);
            messageArr.put(obj);
            jsonBody.put("messages",messageArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("\n" +
                        "https://api.openai.com/v1/chat/completions")
                .header("Authorization","Bearer YOUR API KEY")
                .post(body)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
//                addResponse("Failed to load response due to "+e.getMessage());
                addResponse("Failed to load response due to "+e.getMessage());
            }
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject  jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                        if(initial)
                            initialresponse(result.trim());
                        else
                            addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    addResponse("Failed to load response due to "+response.body().string());                }
            }
        });
    }
}
