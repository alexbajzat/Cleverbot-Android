package bajzat.com.cleverbot.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;


import bajzat.com.cleverbot.R;
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
    private String outputString = "";
    private final String GIT = getString(R.string.repo_url);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadViews();
        okButton.setOnClickListener(this);
        inputEditText.setOnEditorActionListener(this);
        backend = new Backend();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.git:
                goToRepository();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToRepository() {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(GIT));

        startActivity(browse);

    }


    //adauga la outputEditText la final textul din parametru
    private void addMessage(String text) {
        outputString = text + "<br>" + outputString;
        outputTextView.setText(Html.fromHtml(outputString));
        System.out.println(outputString);


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
            String colorAnswer = "<font color=\"blue\">" + getString(R.string.msg_clevy) + suggestion.answer + "</font>";
            addMessage(colorAnswer);
            if (suggestion.success == false) {
                uploadNext = true;
                lastQuestion = suggestion.answer;
            }


        }


        @Override
        public void failure(RetrofitError error) {
            addMessage(getString(R.string.msg_clevy) + getString(R.string.msg_nointernet));

        }
    };

    private Callback<Suggestion> addCallback = new Callback<Suggestion>() {
        @Override
        public void success(Suggestion suggestion, Response response) {

        }

        @Override
        public void failure(RetrofitError error) {
            addMessage(getString(R.string.msg_clevy) + getString(R.string.msg_nointernet));
        }
    };

    private void loadAnswer(String input) {
        if (uploadNext == false) {
            backend.getApiService().getSuggestion(input,
                    answerCallback
            );
        } else {

            backend.getApiService().addSuggestion(lastQuestion, input, addCallback);
            addMessage(getString(R.string.msg_clevy) + getString(R.string.msg_asksomething));
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
