package aerizostudios.com.speechtotext;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    protected static final int RESULT_SPEECH = 1;

    private ImageButton btnSpeak;
    private TextView txtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txtText = (TextView) findViewById(R.id.txtText);

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    txtText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Oops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT).show();
                    }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    txtText.setText(text.get(0));
                    String FileText = txtText.getText().toString();
                    writeTOfile(FileText);
                }
                break;
            }

        }
    }

    public void writeTOfile(String data) {

            File ResultFile = null;
            try {
                File file = new File(Environment.getExternalStoragePublicDirectory("ResultText"), "");
                if (!file.exists()) {
                    file.mkdir();
                }
                ResultFile = new File(file, "STP_" + (new SimpleDateFormat("yyyyMMddHHmmss")).
                        format(Calendar.getInstance().getTime()) + ".txt");

                FileOutputStream out =
                        new FileOutputStream(ResultFile);

                out.write(data.getBytes(Charset.forName("UTF-8")));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                out.close();

                //catch all exceptions
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


