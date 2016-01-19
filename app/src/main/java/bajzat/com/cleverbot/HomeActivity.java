package bajzat.com.cleverbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bajzat.com.cleverbot.models.Suggestion;
import bajzat.com.cleverbot.service.Backend;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    private TextView outputTextView;
    private EditText inputEditText;
    private Button okButton;
    private Backend backend;
    private boolean uploadNext = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadViews();

        okButton.setOnClickListener(this);
        inputEditText.setOnEditorActionListener(this);
        backend = new Backend();


    }

    //adauga la outputEditText la final textul din parametru
    private void addMessage(String text) {

        outputTextView.setText(outputTextView.getText().toString() + '\n' + text);


    }

    private void loadViews() {
        outputTextView = (TextView) findViewById(R.id.home_output);
        inputEditText = (EditText) findViewById(R.id.home_input);
        okButton = (Button) findViewById(R.id.home_ok_button);
    }



    @Override
    public void onClick(View v) {
        String input = inputEditText.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            addMessage("You: " + input);
            loadAnswer(input);
            inputEditText.setText("");

        } else {
            inputEditText.setError(getString(R.string.msg_saysomething));
        }
    }

    private String lastQuestion;
    private Callback<Suggestion> answerCallback = new Callback<Suggestion>() {

        @Override
        public void success(Suggestion suggestion, Response response) {
            addMessage(getString(R.string.msg_clevy) + suggestion.answer);
            if (suggestion.success == false) {
                uploadNext = true;
                lastQuestion = suggestion.answer;
            }


        }

        @Override
        public void failure(RetrofitError error) {
            addMessage(getString(R.string.msg_clevy)+getString(R.string.msg_nointernet));

        }
    };

    private Callback<Suggestion> addCallback = new Callback<Suggestion>() {
        @Override
        public void success(Suggestion suggestion, Response response) {

        }

        @Override
        public void failure(RetrofitError error) {
            addMessage(getString(R.string.msg_clevy)+getString(R.string.msg_nointernet));
        }
    };

    private void loadAnswer(String input) {
        if (uploadNext == false) {
            backend.getApiService().getSuggestion(input,
                    answerCallback
            );
        } else {

            backend.getApiService().addSuggestion(lastQuestion, input, addCallback);
            addMessage(getString(R.string.msg_clevy)+getString(R.string.msg_asksomething));
            uploadNext = false;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            okButton.callOnClick();
            return true;
        }
        return false;
    }
}
