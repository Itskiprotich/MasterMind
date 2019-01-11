package com.imejadevs.mastermind.NewGame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imejadevs.mastermind.Leaders.UsersList;
import com.imejadevs.mastermind.MainActivity;
import com.imejadevs.mastermind.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    final Context context = this;
    String player_input;
    int Maximum_gueses;
    private Toolbar toolbar;
    String flag;

    int code_lenght;

    private int get_CodeLength() {
        SharedPreferences sharedPreferences = getSharedPreferences("Game_Setting", Context.MODE_PRIVATE);
        int aa = 4;
        int name = sharedPreferences.getInt("Code", aa);

        return name;
    }

    private int get_Number() {
        SharedPreferences sharedPreferences = getSharedPreferences("Game_Setting", Context.MODE_PRIVATE);
        int aa = 10;
        int name = sharedPreferences.getInt("Gueses", aa);

        return name;
    }

    Vibrator vibrator;
    private ImageView A, B, C, D, E, F, G, H;
    private Button Clear, Check;
    private TextView text;

    private ListView listView;

    ArrayList<Game> game;
    MyAdapter myadapter;
    AlertDialog al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {

            startActivity(new Intent(this, PlayActivity.class));
            Toast.makeText(this, "Welcome !!", Toast.LENGTH_LONG).show();
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        game = new ArrayList<>();
        Maximum_gueses = get_Number();
        code_lenght = get_CodeLength();
        A = (ImageView) findViewById(R.id.a);
        B = (ImageView) findViewById(R.id.b);
        C = (ImageView) findViewById(R.id.c);
        D = (ImageView) findViewById(R.id.d);
        E = (ImageView) findViewById(R.id.e);
        F = (ImageView) findViewById(R.id.f);
        G = (ImageView) findViewById(R.id.g);
        H = (ImageView) findViewById(R.id.h);
        Clear = (Button) findViewById(R.id.clear);
        Check = (Button) findViewById(R.id.check);
        text = (TextView) findViewById(R.id.editText);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        listView = (ListView) findViewById(R.id.listhere);
        myadapter = new MyAdapter(this, game);
        listView.setAdapter(myadapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        A.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                vibrator.vibrate(50);
                text.append("A");
            }
        });

        B.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                vibrator.vibrate(50);
                text.append("B");
            }
        });
        C.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vibrator.vibrate(50);
                text.append("C");
            }
        });
        D.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vibrator.vibrate(50);
                text.append("D");
            }
        });
        E.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vibrator.vibrate(50);
                text.append("E");
            }
        });
        F.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vibrator.vibrate(50);
                text.append("F");
            }
        });
        G.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vibrator.vibrate(50);
                text.append("G");
            }
        });
        H.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vibrator.vibrate(50);
                text.append("H");
            }
        });

        if (Clear != null) {
            Clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vibrator.vibrate(50);
                    String textString = text.getText().toString();
                    if (textString.length() > 0) {
                        text.setText(textString.substring(0, textString.length() - 1));
                    }
                }
            });
        }
        char[] letters = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        char[] newArray = new char[code_lenght];
        Random rand = new Random();
        for (int i = 0; i < code_lenght; i++) {
            char p = letters[rand.nextInt(letters.length)];
            newArray[i] = p;
            for (int j = i - 1; j != i && j > -1; j--) {
                if (newArray[i] == newArray[j]) {
                    i--;
                }
            }
        }

        flag = String.valueOf(newArray);  // Code to be guessed.

        Check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vibrator.vibrate(50);
                player_input = text.getText().toString();
                char[] n = new char[code_lenght];
                int c = 0, j = 0, x = 0, q = 0;
                if (player_input.length() != code_lenght) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1
                            .setTitle("Incorrect entry !")
                            .setMessage("Please enter " + code_lenght + " letter code")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    text.hasFocus();
                                }
                            })
                            .show();
                }
                if (Maximum_gueses == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater lay = LayoutInflater.from(context);
                    final View viewdata = lay.inflate(R.layout.lost, null);

                    ImageView A5 = (ImageView) viewdata.findViewById(R.id.a);
                    ImageView A6 = (ImageView) viewdata.findViewById(R.id.b);
                    ImageView A7 = (ImageView) viewdata.findViewById(R.id.c);
                    ImageView A8 = (ImageView) viewdata.findViewById(R.id.d);
                    ImageView A9 = (ImageView) viewdata.findViewById(R.id.e);
                    ImageView A10 = (ImageView) viewdata.findViewById(R.id.f);

                    if (flag.length() == 3) {

                        char a1 = flag.charAt(0);
                        char a2 = flag.charAt(1);
                        char a3 = flag.charAt(2);

                        A5.setVisibility(View.VISIBLE);
                        A6.setVisibility(View.VISIBLE);
                        A7.setVisibility(View.VISIBLE);
                        A8.setVisibility(View.INVISIBLE);
                        A9.setVisibility(View.INVISIBLE);
                        A10.setVisibility(View.INVISIBLE);


                        String one = get_the_Color(a1);
                        String two = get_the_Color(a2);
                        String three = get_the_Color(a3);

                        if (one == "#ff0000") {
                            A5.setImageResource(R.drawable.red);
                        }
                        if (one == "#0000ff") {
                            A5.setImageResource(R.drawable.blue);
                        }
                        if (one == "#ffff00") {
                            A5.setImageResource(R.drawable.yellow);
                        }
                        if (one == "#008000") {
                            A5.setImageResource(R.drawable.green);
                        }
                        if (one == "#ffa500") {
                            A5.setImageResource(R.drawable.orange);
                        }
                        if (one == "#800080") {
                            A5.setImageResource(R.drawable.purple);
                        }
                        if (one == "#000000") {
                            A5.setImageResource(R.drawable.black);
                        }
                        if (one == "#ffffff") {
                            A5.setImageResource(R.drawable.white);
                        }
                        /*Second*/

                        if (two == "#ff0000") {
                            A6.setImageResource(R.drawable.red);
                        }
                        if (two == "#0000ff") {
                            A6.setImageResource(R.drawable.blue);
                        }
                        if (two == "#ffff00") {
                            A6.setImageResource(R.drawable.yellow);
                        }
                        if (two == "#008000") {
                            A6.setImageResource(R.drawable.green);
                        }
                        if (two == "#ffa500") {
                            A6.setImageResource(R.drawable.orange);
                        }
                        if (two == "#800080") {
                            A6.setImageResource(R.drawable.purple);
                        }
                        if (two == "#000000") {
                            A6.setImageResource(R.drawable.black);
                        }
                        if (two == "#ffffff") {
                            A6.setImageResource(R.drawable.white);
                        }

                        if (three == "#ff0000") {
                            A7.setImageResource(R.drawable.red);
                        }
                        if (three == "#0000ff") {
                            A7.setImageResource(R.drawable.blue);
                        }
                        if (three == "#ffff00") {
                            A7.setImageResource(R.drawable.yellow);
                        }
                        if (three == "#008000") {
                            A7.setImageResource(R.drawable.green);
                        }
                        if (three == "#ffa500") {
                            A7.setImageResource(R.drawable.orange);
                        }
                        if (three == "#800080") {
                            A7.setImageResource(R.drawable.purple);
                        }
                        if (three == "#000000") {
                            A7.setImageResource(R.drawable.black);
                        }
                        if (three == "#ffffff") {
                            A7.setImageResource(R.drawable.white);
                        }


                        builder.setTitle("You Lost!!").
                                setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        PlayActivity.this.finish();
                                        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                PlayActivity.this.finish();
                                Intent loseintent = getIntent();
                                startActivity(loseintent);
                            }
                        });

                        builder.setView(viewdata);

                        al = builder.create();

                        al.show();
                    }

                    if (flag.length() == 4) {
                        A5.setVisibility(View.VISIBLE);
                        A6.setVisibility(View.VISIBLE);
                        A7.setVisibility(View.VISIBLE);
                        A8.setVisibility(View.VISIBLE);
                        A9.setVisibility(View.INVISIBLE);
                        A10.setVisibility(View.INVISIBLE);

                        char aa1 = flag.charAt(0);
                        char aa2 = flag.charAt(1);
                        char aa3 = flag.charAt(2);
                        char aa4 = flag.charAt(3);

                        String one = get_the_Color(aa1);
                        String two = get_the_Color(aa2);
                        String three = get_the_Color(aa3);
                        String four = get_the_Color(aa4);

                        if (one == "#ff0000") {
                            A5.setImageResource(R.drawable.red);
                        }
                        if (one == "#0000ff") {
                            A5.setImageResource(R.drawable.blue);
                        }
                        if (one == "#ffff00") {
                            A5.setImageResource(R.drawable.yellow);
                        }
                        if (one == "#008000") {
                            A5.setImageResource(R.drawable.green);
                        }
                        if (one == "#ffa500") {
                            A5.setImageResource(R.drawable.orange);
                        }
                        if (one == "#800080") {
                            A5.setImageResource(R.drawable.purple);
                        }
                        if (one == "#000000") {
                            A5.setImageResource(R.drawable.black);
                        }
                        if (one == "#ffffff") {
                            A5.setImageResource(R.drawable.white);
                        }
                        /*Second*/

                        if (two == "#ff0000") {
                            A6.setImageResource(R.drawable.red);
                        }
                        if (two == "#0000ff") {
                            A6.setImageResource(R.drawable.blue);
                        }
                        if (two == "#ffff00") {
                            A6.setImageResource(R.drawable.yellow);
                        }
                        if (two == "#008000") {
                            A6.setImageResource(R.drawable.green);
                        }
                        if (two == "#ffa500") {
                            A6.setImageResource(R.drawable.orange);
                        }
                        if (two == "#800080") {
                            A6.setImageResource(R.drawable.purple);
                        }
                        if (two == "#000000") {
                            A6.setImageResource(R.drawable.black);
                        }
                        if (two == "#ffffff") {
                            A6.setImageResource(R.drawable.white);
                        }
                        /*end of second*/
                        /*start of third*/

                        if (three == "#ff0000") {
                            A7.setImageResource(R.drawable.red);
                        }
                        if (three == "#0000ff") {
                            A7.setImageResource(R.drawable.blue);
                        }
                        if (three == "#ffff00") {
                            A7.setImageResource(R.drawable.yellow);
                        }
                        if (three == "#008000") {
                            A7.setImageResource(R.drawable.green);
                        }
                        if (three == "#ffa500") {
                            A7.setImageResource(R.drawable.orange);
                        }
                        if (three == "#800080") {
                            A7.setImageResource(R.drawable.purple);
                        }
                        if (three == "#000000") {
                            A7.setImageResource(R.drawable.black);
                        }
                        if (three == "#ffffff") {
                            A7.setImageResource(R.drawable.white);
                        }
                        /*end of third*/
                        /*start of fourth*/

                        if (four == "#ff0000") {
                            A8.setImageResource(R.drawable.red);
                        }
                        if (four == "#0000ff") {
                            A8.setImageResource(R.drawable.blue);
                        }
                        if (four == "#ffff00") {
                            A8.setImageResource(R.drawable.yellow);
                        }
                        if (four == "#008000") {
                            A8.setImageResource(R.drawable.green);
                        }
                        if (four == "#ffa500") {
                            A8.setImageResource(R.drawable.orange);
                        }
                        if (four == "#800080") {
                            A8.setImageResource(R.drawable.purple);
                        }
                        if (four == "#000000") {
                            A8.setImageResource(R.drawable.black);
                        }
                        if (four == "#ffffff") {
                            A8.setImageResource(R.drawable.white);
                        }

                        builder.setTitle("You Lost!!").
                                setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        PlayActivity.this.finish();
                                        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                PlayActivity.this.finish();
                                Intent loseintent = getIntent();
                                startActivity(loseintent);
                            }
                        });

                        builder.setView(viewdata);

                        al = builder.create();

                        al.show();
                    }
                    if (flag.length() == 5) {

                        A5.setVisibility(View.VISIBLE);
                        A6.setVisibility(View.VISIBLE);
                        A7.setVisibility(View.VISIBLE);
                        A8.setVisibility(View.VISIBLE);
                        A9.setVisibility(View.VISIBLE);
                        A10.setVisibility(View.INVISIBLE);

                        char aa1 = flag.charAt(0);
                        char aa2 = flag.charAt(1);
                        char aa3 = flag.charAt(2);
                        char aa4 = flag.charAt(3);
                        char aa5 = flag.charAt(4);

                        String one = get_the_Color(aa1);
                        String two = get_the_Color(aa2);
                        String three = get_the_Color(aa3);
                        String four = get_the_Color(aa4);
                        String five = get_the_Color(aa5);

                        if (one == "#ff0000") {
                            A5.setImageResource(R.drawable.red);
                        }
                        if (one == "#0000ff") {
                            A5.setImageResource(R.drawable.blue);
                        }
                        if (one == "#ffff00") {
                            A5.setImageResource(R.drawable.yellow);
                        }
                        if (one == "#008000") {
                            A5.setImageResource(R.drawable.green);
                        }
                        if (one == "#ffa500") {
                            A5.setImageResource(R.drawable.orange);
                        }
                        if (one == "#800080") {
                            A5.setImageResource(R.drawable.purple);
                        }
                        if (one == "#000000") {
                            A5.setImageResource(R.drawable.black);
                        }
                        if (one == "#ffffff") {
                            A5.setImageResource(R.drawable.white);
                        }
                        /*Second*/

                        if (two == "#ff0000") {
                            A6.setImageResource(R.drawable.red);
                        }
                        if (two == "#0000ff") {
                            A6.setImageResource(R.drawable.blue);
                        }
                        if (two == "#ffff00") {
                            A6.setImageResource(R.drawable.yellow);
                        }
                        if (two == "#008000") {
                            A6.setImageResource(R.drawable.green);
                        }
                        if (two == "#ffa500") {
                            A6.setImageResource(R.drawable.orange);
                        }
                        if (two == "#800080") {
                            A6.setImageResource(R.drawable.purple);
                        }
                        if (two == "#000000") {
                            A6.setImageResource(R.drawable.black);
                        }
                        if (two == "#ffffff") {
                            A6.setImageResource(R.drawable.white);
                        }
                        /*end of second*/
                        /*start of third*/

                        if (three == "#ff0000") {
                            A7.setImageResource(R.drawable.red);
                        }
                        if (three == "#0000ff") {
                            A7.setImageResource(R.drawable.blue);
                        }
                        if (three == "#ffff00") {
                            A7.setImageResource(R.drawable.yellow);
                        }
                        if (three == "#008000") {
                            A7.setImageResource(R.drawable.green);
                        }
                        if (three == "#ffa500") {
                            A7.setImageResource(R.drawable.orange);
                        }
                        if (three == "#800080") {
                            A7.setImageResource(R.drawable.purple);
                        }
                        if (three == "#000000") {
                            A7.setImageResource(R.drawable.black);
                        }
                        if (three == "#ffffff") {
                            A7.setImageResource(R.drawable.white);
                        }
                        /*end of third*/
                        /*start of fourth*/

                        if (four == "#ff0000") {
                            A8.setImageResource(R.drawable.red);
                        }
                        if (four == "#0000ff") {
                            A8.setImageResource(R.drawable.blue);
                        }
                        if (four == "#ffff00") {
                            A8.setImageResource(R.drawable.yellow);
                        }
                        if (four == "#008000") {
                            A8.setImageResource(R.drawable.green);
                        }
                        if (four == "#ffa500") {
                            A8.setImageResource(R.drawable.orange);
                        }
                        if (four == "#800080") {
                            A8.setImageResource(R.drawable.purple);
                        }
                        if (four == "#000000") {
                            A8.setImageResource(R.drawable.black);
                        }
                        if (four == "#ffffff") {
                            A8.setImageResource(R.drawable.white);
                        }

                        if (five == "#ff0000") {
                            A9.setImageResource(R.drawable.red);
                        }
                        if (five == "#0000ff") {
                            A9.setImageResource(R.drawable.blue);
                        }
                        if (five == "#ffff00") {
                            A9.setImageResource(R.drawable.yellow);
                        }
                        if (five == "#008000") {
                            A9.setImageResource(R.drawable.green);
                        }
                        if (five == "#ffa500") {
                            A9.setImageResource(R.drawable.orange);
                        }
                        if (five == "#800080") {
                            A9.setImageResource(R.drawable.purple);
                        }
                        if (five == "#000000") {
                            A9.setImageResource(R.drawable.black);
                        }
                        if (five == "#ffffff") {
                            A9.setImageResource(R.drawable.white);
                        }

                        builder.setTitle("You Lost!!").
                                setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        PlayActivity.this.finish();
                                        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                PlayActivity.this.finish();
                                Intent loseintent = getIntent();
                                startActivity(loseintent);
                            }
                        });

                        builder.setView(viewdata);

                        al = builder.create();

                        al.show();
                    }
                    if (flag.length() == 6) {

                        char aa1 = flag.charAt(0);
                        char aa2 = flag.charAt(1);
                        char aa3 = flag.charAt(2);
                        char aa4 = flag.charAt(3);
                        char aa5 = flag.charAt(4);
                        char aa6 = flag.charAt(5);

                        String one = get_the_Color(aa1);
                        String two = get_the_Color(aa2);
                        String three = get_the_Color(aa3);
                        String four = get_the_Color(aa4);
                        String five = get_the_Color(aa5);
                        String six = get_the_Color(aa6);

                        if (one == "#ff0000") {
                            A5.setImageResource(R.drawable.red);
                        }
                        if (one == "#0000ff") {
                            A5.setImageResource(R.drawable.blue);
                        }
                        if (one == "#ffff00") {
                            A5.setImageResource(R.drawable.yellow);
                        }
                        if (one == "#008000") {
                            A5.setImageResource(R.drawable.green);
                        }
                        if (one == "#ffa500") {
                            A5.setImageResource(R.drawable.orange);
                        }
                        if (one == "#800080") {
                            A5.setImageResource(R.drawable.purple);
                        }
                        if (one == "#000000") {
                            A5.setImageResource(R.drawable.black);
                        }
                        if (one == "#ffffff") {
                            A5.setImageResource(R.drawable.white);
                        }
                        /*Second*/

                        if (two == "#ff0000") {
                            A6.setImageResource(R.drawable.red);
                        }
                        if (two == "#0000ff") {
                            A6.setImageResource(R.drawable.blue);
                        }
                        if (two == "#ffff00") {
                            A6.setImageResource(R.drawable.yellow);
                        }
                        if (two == "#008000") {
                            A6.setImageResource(R.drawable.green);
                        }
                        if (two == "#ffa500") {
                            A6.setImageResource(R.drawable.orange);
                        }
                        if (two == "#800080") {
                            A6.setImageResource(R.drawable.purple);
                        }
                        if (two == "#000000") {
                            A6.setImageResource(R.drawable.black);
                        }
                        if (two == "#ffffff") {
                            A6.setImageResource(R.drawable.white);
                        }
                        /*end of second*/
                        /*start of third*/

                        if (three == "#ff0000") {
                            A7.setImageResource(R.drawable.red);
                        }
                        if (three == "#0000ff") {
                            A7.setImageResource(R.drawable.blue);
                        }
                        if (three == "#ffff00") {
                            A7.setImageResource(R.drawable.yellow);
                        }
                        if (three == "#008000") {
                            A7.setImageResource(R.drawable.green);
                        }
                        if (three == "#ffa500") {
                            A7.setImageResource(R.drawable.orange);
                        }
                        if (three == "#800080") {
                            A7.setImageResource(R.drawable.purple);
                        }
                        if (three == "#000000") {
                            A7.setImageResource(R.drawable.black);
                        }
                        if (three == "#ffffff") {
                            A7.setImageResource(R.drawable.white);
                        }
                        /*end of third*/
                        /*start of fourth*/

                        if (four == "#ff0000") {
                            A8.setImageResource(R.drawable.red);
                        }
                        if (four == "#0000ff") {
                            A8.setImageResource(R.drawable.blue);
                        }
                        if (four == "#ffff00") {
                            A8.setImageResource(R.drawable.yellow);
                        }
                        if (four == "#008000") {
                            A8.setImageResource(R.drawable.green);
                        }
                        if (four == "#ffa500") {
                            A8.setImageResource(R.drawable.orange);
                        }
                        if (four == "#800080") {
                            A8.setImageResource(R.drawable.purple);
                        }
                        if (four == "#000000") {
                            A8.setImageResource(R.drawable.black);
                        }
                        if (four == "#ffffff") {
                            A8.setImageResource(R.drawable.white);
                        }

                        if (five == "#ff0000") {
                            A9.setImageResource(R.drawable.red);
                        }
                        if (five == "#0000ff") {
                            A9.setImageResource(R.drawable.blue);
                        }
                        if (five == "#ffff00") {
                            A9.setImageResource(R.drawable.yellow);
                        }
                        if (five == "#008000") {
                            A9.setImageResource(R.drawable.green);
                        }
                        if (five == "#ffa500") {
                            A9.setImageResource(R.drawable.orange);
                        }
                        if (five == "#800080") {
                            A9.setImageResource(R.drawable.purple);
                        }
                        if (five == "#000000") {
                            A9.setImageResource(R.drawable.black);
                        }
                        if (five == "#ffffff") {
                            A9.setImageResource(R.drawable.white);
                        }


                        if (six == "#ff0000") {
                            A10.setImageResource(R.drawable.red);
                        }
                        if (six == "#0000ff") {
                            A10.setImageResource(R.drawable.blue);
                        }
                        if (six == "#ffff00") {
                            A10.setImageResource(R.drawable.yellow);
                        }
                        if (six == "#008000") {
                            A10.setImageResource(R.drawable.green);
                        }
                        if (six == "#ffa500") {
                            A10.setImageResource(R.drawable.orange);
                        }
                        if (six == "#800080") {
                            A10.setImageResource(R.drawable.purple);
                        }
                        if (six == "#000000") {
                            A10.setImageResource(R.drawable.black);
                        }
                        if (six == "#ffffff") {
                            A10.setImageResource(R.drawable.white);
                        }

                        builder.setTitle("You Lost!!").
                                setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        PlayActivity.this.finish();
                                        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                PlayActivity.this.finish();
                                Intent loseintent = getIntent();
                                startActivity(loseintent);
                            }
                        });

                        builder.setView(viewdata);

                        al = builder.create();

                        al.show();
                    }

                }
                if (flag.equals(player_input)) {
                    won(player_input, Maximum_gueses);
                } else if (!flag.equals(player_input) && player_input.length() == code_lenght) {
                    for (int k = 0; k < player_input.length(); k++) {
                        char l = player_input.charAt(k);
                        char m = flag.charAt(k);

                        if (l == m) {
                            c++;
                        } else {

                            forxloop:
                            for (q = flag.length() - 1; q > -1; q--) {
                                if ((l == flag.charAt(q))) {
                                    j++;
                                    break forxloop;
                                }
                                if ((q != k) && (l == flag.charAt(q))) {

                                }
                            }
                            if (q == -1) {
                                n[k] = l;
                                x++;
                            }
                        }
                    }
                    init(c, j, x, Maximum_gueses, player_input);
                    text.setText("");
                    Maximum_gueses--;
                }
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.aa) {
            PlayActivity.this.finish();
            Intent wonintent = getIntent();
            startActivity(wonintent);
            wonintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            return true;
        } else if (id == R.id.bb) {

            PlayActivity.this.finish();
            startActivity(new Intent(PlayActivity.this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void init(int c, int j, int x, int loop, String text) {

        Game game_play = new Game();

        game_play.setC(c);

        game_play.setJ(j);

        game_play.setL(loop);

        game_play.setX(x);

        game_play.setInput(player_input);

        game.add(game_play);

        myadapter.notifyDataSetChanged();

        if (x == code_lenght) {
            AlertDialog.Builder no_match = new AlertDialog.Builder(context);
            no_match
                    .setTitle("Oops !")
                    .setMessage("Your selection does not match any record!!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(R.drawable.lost)
                    .show();
        }
    }

    public void won(String code, final int loop) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater lay = LayoutInflater.from(context);
        final View viewdata = lay.inflate(R.layout.won, null);

        ImageView A5 = (ImageView) viewdata.findViewById(R.id.a);
        ImageView A6 = (ImageView) viewdata.findViewById(R.id.b);
        ImageView A7 = (ImageView) viewdata.findViewById(R.id.c);
        ImageView A8 = (ImageView) viewdata.findViewById(R.id.d);
        ImageView A9 = (ImageView) viewdata.findViewById(R.id.e);
        ImageView A10 = (ImageView) viewdata.findViewById(R.id.f);


        if (flag.length() == 3) {

            char a1 = flag.charAt(0);
            char a2 = flag.charAt(1);
            char a3 = flag.charAt(2);

            A5.setVisibility(View.VISIBLE);
            A6.setVisibility(View.VISIBLE);
            A7.setVisibility(View.VISIBLE);
            A8.setVisibility(View.INVISIBLE);
            A9.setVisibility(View.INVISIBLE);
            A10.setVisibility(View.INVISIBLE);


            String one = get_the_Color(a1);
            String two = get_the_Color(a2);
            String three = get_the_Color(a3);

            if (one == "#ff0000") {
                A5.setImageResource(R.drawable.red);
            }
            if (one == "#0000ff") {
                A5.setImageResource(R.drawable.blue);
            }
            if (one == "#ffff00") {
                A5.setImageResource(R.drawable.yellow);
            }
            if (one == "#008000") {
                A5.setImageResource(R.drawable.green);
            }
            if (one == "#ffa500") {
                A5.setImageResource(R.drawable.orange);
            }
            if (one == "#800080") {
                A5.setImageResource(R.drawable.purple);
            }
            if (one == "#000000") {
                A5.setImageResource(R.drawable.black);
            }
            if (one == "#ffffff") {
                A5.setImageResource(R.drawable.white);
            }
            /*Second*/

            if (two == "#ff0000") {
                A6.setImageResource(R.drawable.red);
            }
            if (two == "#0000ff") {
                A6.setImageResource(R.drawable.blue);
            }
            if (two == "#ffff00") {
                A6.setImageResource(R.drawable.yellow);
            }
            if (two == "#008000") {
                A6.setImageResource(R.drawable.green);
            }
            if (two == "#ffa500") {
                A6.setImageResource(R.drawable.orange);
            }
            if (two == "#800080") {
                A6.setImageResource(R.drawable.purple);
            }
            if (two == "#000000") {
                A6.setImageResource(R.drawable.black);
            }
            if (two == "#ffffff") {
                A6.setImageResource(R.drawable.white);
            }

            if (three == "#ff0000") {
                A7.setImageResource(R.drawable.red);
            }
            if (three == "#0000ff") {
                A7.setImageResource(R.drawable.blue);
            }
            if (three == "#ffff00") {
                A7.setImageResource(R.drawable.yellow);
            }
            if (three == "#008000") {
                A7.setImageResource(R.drawable.green);
            }
            if (three == "#ffa500") {
                A7.setImageResource(R.drawable.orange);
            }
            if (three == "#800080") {
                A7.setImageResource(R.drawable.purple);
            }
            if (three == "#000000") {
                A7.setImageResource(R.drawable.black);
            }
            if (three == "#ffffff") {
                A7.setImageResource(R.drawable.white);
            }

            builder.setTitle("You Won!!").
                    setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            PlayActivity.this.finish();
                            startActivity(new Intent(PlayActivity.this, MainActivity.class));
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Intent wonintent = getIntent();
                    startActivity(wonintent);
                    wonintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
            }).setNeutralButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    submittoLeaders(loop);

                }
            });

            builder.setView(viewdata);

            al = builder.create();

            al.show();
        }

        if (flag.length() == 4) {
            A5.setVisibility(View.VISIBLE);
            A6.setVisibility(View.VISIBLE);
            A7.setVisibility(View.VISIBLE);
            A8.setVisibility(View.VISIBLE);
            A9.setVisibility(View.INVISIBLE);
            A10.setVisibility(View.INVISIBLE);

            char aa1 = flag.charAt(0);
            char aa2 = flag.charAt(1);
            char aa3 = flag.charAt(2);
            char aa4 = flag.charAt(3);

            String one = get_the_Color(aa1);
            String two = get_the_Color(aa2);
            String three = get_the_Color(aa3);
            String four = get_the_Color(aa4);

            if (one == "#ff0000") {
                A5.setImageResource(R.drawable.red);
            }
            if (one == "#0000ff") {
                A5.setImageResource(R.drawable.blue);
            }
            if (one == "#ffff00") {
                A5.setImageResource(R.drawable.yellow);
            }
            if (one == "#008000") {
                A5.setImageResource(R.drawable.green);
            }
            if (one == "#ffa500") {
                A5.setImageResource(R.drawable.orange);
            }
            if (one == "#800080") {
                A5.setImageResource(R.drawable.purple);
            }
            if (one == "#000000") {
                A5.setImageResource(R.drawable.black);
            }
            if (one == "#ffffff") {
                A5.setImageResource(R.drawable.white);
            }
            /*Second*/

            if (two == "#ff0000") {
                A6.setImageResource(R.drawable.red);
            }
            if (two == "#0000ff") {
                A6.setImageResource(R.drawable.blue);
            }
            if (two == "#ffff00") {
                A6.setImageResource(R.drawable.yellow);
            }
            if (two == "#008000") {
                A6.setImageResource(R.drawable.green);
            }
            if (two == "#ffa500") {
                A6.setImageResource(R.drawable.orange);
            }
            if (two == "#800080") {
                A6.setImageResource(R.drawable.purple);
            }
            if (two == "#000000") {
                A6.setImageResource(R.drawable.black);
            }
            if (two == "#ffffff") {
                A6.setImageResource(R.drawable.white);
            }
            /*end of second*/
            /*start of third*/

            if (three == "#ff0000") {
                A7.setImageResource(R.drawable.red);
            }
            if (three == "#0000ff") {
                A7.setImageResource(R.drawable.blue);
            }
            if (three == "#ffff00") {
                A7.setImageResource(R.drawable.yellow);
            }
            if (three == "#008000") {
                A7.setImageResource(R.drawable.green);
            }
            if (three == "#ffa500") {
                A7.setImageResource(R.drawable.orange);
            }
            if (three == "#800080") {
                A7.setImageResource(R.drawable.purple);
            }
            if (three == "#000000") {
                A7.setImageResource(R.drawable.black);
            }
            if (three == "#ffffff") {
                A7.setImageResource(R.drawable.white);
            }
            /*end of third*/
            /*start of fourth*/

            if (four == "#ff0000") {
                A8.setImageResource(R.drawable.red);
            }
            if (four == "#0000ff") {
                A8.setImageResource(R.drawable.blue);
            }
            if (four == "#ffff00") {
                A8.setImageResource(R.drawable.yellow);
            }
            if (four == "#008000") {
                A8.setImageResource(R.drawable.green);
            }
            if (four == "#ffa500") {
                A8.setImageResource(R.drawable.orange);
            }
            if (four == "#800080") {
                A8.setImageResource(R.drawable.purple);
            }
            if (four == "#000000") {
                A8.setImageResource(R.drawable.black);
            }
            if (four == "#ffffff") {
                A8.setImageResource(R.drawable.white);
            }

            builder.setTitle("You Won!!").
                    setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            PlayActivity.this.finish();
                            startActivity(new Intent(PlayActivity.this, MainActivity.class));
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Intent wonintent = getIntent();
                    startActivity(wonintent);
                    wonintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
            }).setNeutralButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    submittoLeaders(loop);

                }
            });

            builder.setView(viewdata);

            al = builder.create();

            al.show();
        }
        if (flag.length() == 5) {
            A5.setVisibility(View.VISIBLE);
            A6.setVisibility(View.VISIBLE);
            A7.setVisibility(View.VISIBLE);
            A8.setVisibility(View.VISIBLE);
            A9.setVisibility(View.VISIBLE);
            A10.setVisibility(View.INVISIBLE);

            char aa1 = flag.charAt(0);
            char aa2 = flag.charAt(1);
            char aa3 = flag.charAt(2);
            char aa4 = flag.charAt(3);
            char aa5 = flag.charAt(4);

            String one = get_the_Color(aa1);
            String two = get_the_Color(aa2);
            String three = get_the_Color(aa3);
            String four = get_the_Color(aa4);
            String five = get_the_Color(aa5);

            if (one == "#ff0000") {
                A5.setImageResource(R.drawable.red);
            }
            if (one == "#0000ff") {
                A5.setImageResource(R.drawable.blue);
            }
            if (one == "#ffff00") {
                A5.setImageResource(R.drawable.yellow);
            }
            if (one == "#008000") {
                A5.setImageResource(R.drawable.green);
            }
            if (one == "#ffa500") {
                A5.setImageResource(R.drawable.orange);
            }
            if (one == "#800080") {
                A5.setImageResource(R.drawable.purple);
            }
            if (one == "#000000") {
                A5.setImageResource(R.drawable.black);
            }
            if (one == "#ffffff") {
                A5.setImageResource(R.drawable.white);
            }
            /*Second*/

            if (two == "#ff0000") {
                A6.setImageResource(R.drawable.red);
            }
            if (two == "#0000ff") {
                A6.setImageResource(R.drawable.blue);
            }
            if (two == "#ffff00") {
                A6.setImageResource(R.drawable.yellow);
            }
            if (two == "#008000") {
                A6.setImageResource(R.drawable.green);
            }
            if (two == "#ffa500") {
                A6.setImageResource(R.drawable.orange);
            }
            if (two == "#800080") {
                A6.setImageResource(R.drawable.purple);
            }
            if (two == "#000000") {
                A6.setImageResource(R.drawable.black);
            }
            if (two == "#ffffff") {
                A6.setImageResource(R.drawable.white);
            }
            /*end of second*/
            /*start of third*/

            if (three == "#ff0000") {
                A7.setImageResource(R.drawable.red);
            }
            if (three == "#0000ff") {
                A7.setImageResource(R.drawable.blue);
            }
            if (three == "#ffff00") {
                A7.setImageResource(R.drawable.yellow);
            }
            if (three == "#008000") {
                A7.setImageResource(R.drawable.green);
            }
            if (three == "#ffa500") {
                A7.setImageResource(R.drawable.orange);
            }
            if (three == "#800080") {
                A7.setImageResource(R.drawable.purple);
            }
            if (three == "#000000") {
                A7.setImageResource(R.drawable.black);
            }
            if (three == "#ffffff") {
                A7.setImageResource(R.drawable.white);
            }
            /*end of third*/
            /*start of fourth*/

            if (four == "#ff0000") {
                A8.setImageResource(R.drawable.red);
            }
            if (four == "#0000ff") {
                A8.setImageResource(R.drawable.blue);
            }
            if (four == "#ffff00") {
                A8.setImageResource(R.drawable.yellow);
            }
            if (four == "#008000") {
                A8.setImageResource(R.drawable.green);
            }
            if (four == "#ffa500") {
                A8.setImageResource(R.drawable.orange);
            }
            if (four == "#800080") {
                A8.setImageResource(R.drawable.purple);
            }
            if (four == "#000000") {
                A8.setImageResource(R.drawable.black);
            }
            if (four == "#ffffff") {
                A8.setImageResource(R.drawable.white);
            }

            if (five == "#ff0000") {
                A9.setImageResource(R.drawable.red);
            }
            if (five == "#0000ff") {
                A9.setImageResource(R.drawable.blue);
            }
            if (five == "#ffff00") {
                A9.setImageResource(R.drawable.yellow);
            }
            if (five == "#008000") {
                A9.setImageResource(R.drawable.green);
            }
            if (five == "#ffa500") {
                A9.setImageResource(R.drawable.orange);
            }
            if (five == "#800080") {
                A9.setImageResource(R.drawable.purple);
            }
            if (five == "#000000") {
                A9.setImageResource(R.drawable.black);
            }
            if (five == "#ffffff") {
                A9.setImageResource(R.drawable.white);
            }

            builder.setTitle("You Won!!").
                    setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            PlayActivity.this.finish();
                            startActivity(new Intent(PlayActivity.this, MainActivity.class));
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Intent wonintent = getIntent();
                    startActivity(wonintent);
                    wonintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
            }).setNeutralButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    submittoLeaders(loop);

                }
            });

            builder.setView(viewdata);

            al = builder.create();

            al.show();
        }
        if (flag.length() == 6) {

            char aa1 = flag.charAt(0);
            char aa2 = flag.charAt(1);
            char aa3 = flag.charAt(2);
            char aa4 = flag.charAt(3);
            char aa5 = flag.charAt(4);
            char aa6 = flag.charAt(5);

            String one = get_the_Color(aa1);
            String two = get_the_Color(aa2);
            String three = get_the_Color(aa3);
            String four = get_the_Color(aa4);
            String five = get_the_Color(aa5);
            String six = get_the_Color(aa6);

            if (one == "#ff0000") {
                A5.setImageResource(R.drawable.red);
            }
            if (one == "#0000ff") {
                A5.setImageResource(R.drawable.blue);
            }
            if (one == "#ffff00") {
                A5.setImageResource(R.drawable.yellow);
            }
            if (one == "#008000") {
                A5.setImageResource(R.drawable.green);
            }
            if (one == "#ffa500") {
                A5.setImageResource(R.drawable.orange);
            }
            if (one == "#800080") {
                A5.setImageResource(R.drawable.purple);
            }
            if (one == "#000000") {
                A5.setImageResource(R.drawable.black);
            }
            if (one == "#ffffff") {
                A5.setImageResource(R.drawable.white);
            }
            /*Second*/

            if (two == "#ff0000") {
                A6.setImageResource(R.drawable.red);
            }
            if (two == "#0000ff") {
                A6.setImageResource(R.drawable.blue);
            }
            if (two == "#ffff00") {
                A6.setImageResource(R.drawable.yellow);
            }
            if (two == "#008000") {
                A6.setImageResource(R.drawable.green);
            }
            if (two == "#ffa500") {
                A6.setImageResource(R.drawable.orange);
            }
            if (two == "#800080") {
                A6.setImageResource(R.drawable.purple);
            }
            if (two == "#000000") {
                A6.setImageResource(R.drawable.black);
            }
            if (two == "#ffffff") {
                A6.setImageResource(R.drawable.white);
            }
            /*end of second*/
            /*start of third*/

            if (three == "#ff0000") {
                A7.setImageResource(R.drawable.red);
            }
            if (three == "#0000ff") {
                A7.setImageResource(R.drawable.blue);
            }
            if (three == "#ffff00") {
                A7.setImageResource(R.drawable.yellow);
            }
            if (three == "#008000") {
                A7.setImageResource(R.drawable.green);
            }
            if (three == "#ffa500") {
                A7.setImageResource(R.drawable.orange);
            }
            if (three == "#800080") {
                A7.setImageResource(R.drawable.purple);
            }
            if (three == "#000000") {
                A7.setImageResource(R.drawable.black);
            }
            if (three == "#ffffff") {
                A7.setImageResource(R.drawable.white);
            }
            /*end of third*/
            /*start of fourth*/

            if (four == "#ff0000") {
                A8.setImageResource(R.drawable.red);
            }
            if (four == "#0000ff") {
                A8.setImageResource(R.drawable.blue);
            }
            if (four == "#ffff00") {
                A8.setImageResource(R.drawable.yellow);
            }
            if (four == "#008000") {
                A8.setImageResource(R.drawable.green);
            }
            if (four == "#ffa500") {
                A8.setImageResource(R.drawable.orange);
            }
            if (four == "#800080") {
                A8.setImageResource(R.drawable.purple);
            }
            if (four == "#000000") {
                A8.setImageResource(R.drawable.black);
            }
            if (four == "#ffffff") {
                A8.setImageResource(R.drawable.white);
            }

            if (five == "#ff0000") {
                A9.setImageResource(R.drawable.red);
            }
            if (five == "#0000ff") {
                A9.setImageResource(R.drawable.blue);
            }
            if (five == "#ffff00") {
                A9.setImageResource(R.drawable.yellow);
            }
            if (five == "#008000") {
                A9.setImageResource(R.drawable.green);
            }
            if (five == "#ffa500") {
                A9.setImageResource(R.drawable.orange);
            }
            if (five == "#800080") {
                A9.setImageResource(R.drawable.purple);
            }
            if (five == "#000000") {
                A9.setImageResource(R.drawable.black);
            }
            if (five == "#ffffff") {
                A9.setImageResource(R.drawable.white);
            }


            if (six == "#ff0000") {
                A10.setImageResource(R.drawable.red);
            }
            if (six == "#0000ff") {
                A10.setImageResource(R.drawable.blue);
            }
            if (six == "#ffff00") {
                A10.setImageResource(R.drawable.yellow);
            }
            if (six == "#008000") {
                A10.setImageResource(R.drawable.green);
            }
            if (six == "#ffa500") {
                A10.setImageResource(R.drawable.orange);
            }
            if (six == "#800080") {
                A10.setImageResource(R.drawable.purple);
            }
            if (six == "#000000") {
                A10.setImageResource(R.drawable.black);
            }
            if (six == "#ffffff") {
                A10.setImageResource(R.drawable.white);
            }

            builder.setTitle("You Won!!").
                    setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            PlayActivity.this.finish();
                            startActivity(new Intent(PlayActivity.this, MainActivity.class));
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Intent wonintent = getIntent();
                    startActivity(wonintent);
                    wonintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
            }).setNeutralButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    submittoLeaders(loop);

                }
            });

            builder.setView(viewdata);

            al = builder.create();

            al.show();
        }


    }

    private void submittoLeaders(final int loop) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Your name");
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                Success(name, loop);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void Success(String name, int loop) {
        UsersList number = new UsersList(name, "Trial" + loop);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Leaders/Users");
        String userid = reference.push().getKey();
        reference.child(userid).setValue(number);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(context, "Player record added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        vibrator.vibrate(50);
        super.onBackPressed();
        finish();
        startActivity(new Intent(PlayActivity.this, MainActivity.class));

    }

    private class MyAdapter extends ArrayAdapter {
        Context context;
        ArrayList<Game> game;

        public MyAdapter(Context context, ArrayList<Game> game) {
            super(context, R.layout.games, R.id.aa, game);
            this.context = context;
            this.game = game;
        }

        @NonNull
        @Override
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.games, parent, false);
            TextView textView = (TextView) row.findViewById(R.id.bb);
            ImageView A1 = (ImageView) row.findViewById(R.id.a1);
            ImageView A2 = (ImageView) row.findViewById(R.id.a2);
            ImageView A3 = (ImageView) row.findViewById(R.id.a3);
            ImageView A4 = (ImageView) row.findViewById(R.id.a4);
            ImageView A5 = (ImageView) row.findViewById(R.id.a5);
            ImageView A6 = (ImageView) row.findViewById(R.id.a6);
            ImageView A7 = (ImageView) row.findViewById(R.id.a7);
            ImageView A8 = (ImageView) row.findViewById(R.id.a8);
            ImageView A9 = (ImageView) row.findViewById(R.id.a9);
            ImageView A10 = (ImageView) row.findViewById(R.id.a10);
            ImageView A11 = (ImageView) row.findViewById(R.id.a11);
            ImageView A12 = (ImageView) row.findViewById(R.id.a12);

            Game now = game.get(position);
            String user_infor = now.getInput();
            int correct = now.getC();
            int misplaced = now.getJ();

            if (user_infor.length() == 3) {
                char a1 = user_infor.charAt(0);
                char a2 = user_infor.charAt(1);
                char a3 = user_infor.charAt(2);

                char aa1 = flag.charAt(0);
                char aa2 = flag.charAt(1);
                char aa3 = flag.charAt(2);

                A5.setVisibility(View.VISIBLE);
                A6.setVisibility(View.VISIBLE);
                A7.setVisibility(View.VISIBLE);

                A8.setVisibility(View.INVISIBLE);
                A9.setVisibility(View.INVISIBLE);
                A10.setVisibility(View.INVISIBLE);


                String one = get_the_Color(a1);
                String two = get_the_Color(a2);
                String three = get_the_Color(a3);

                if (one == "#ff0000") {
                    A5.setImageResource(R.drawable.red);
                }
                if (one == "#0000ff") {
                    A5.setImageResource(R.drawable.blue);
                }
                if (one == "#ffff00") {
                    A5.setImageResource(R.drawable.yellow);
                }
                if (one == "#008000") {
                    A5.setImageResource(R.drawable.green);
                }
                if (one == "#ffa500") {
                    A5.setImageResource(R.drawable.orange);
                }
                if (one == "#800080") {
                    A5.setImageResource(R.drawable.purple);
                }
                if (one == "#000000") {
                    A5.setImageResource(R.drawable.black);
                }
                if (one == "#ffffff") {
                    A5.setImageResource(R.drawable.white);
                }
                /*Second*/

                if (two == "#ff0000") {
                    A6.setImageResource(R.drawable.red);
                }
                if (two == "#0000ff") {
                    A6.setImageResource(R.drawable.blue);
                }
                if (two == "#ffff00") {
                    A6.setImageResource(R.drawable.yellow);
                }
                if (two == "#008000") {
                    A6.setImageResource(R.drawable.green);
                }
                if (two == "#ffa500") {
                    A6.setImageResource(R.drawable.orange);
                }
                if (two == "#800080") {
                    A6.setImageResource(R.drawable.purple);
                }
                if (two == "#000000") {
                    A6.setImageResource(R.drawable.black);
                }
                if (two == "#ffffff") {
                    A6.setImageResource(R.drawable.white);
                }
                /*end of second*/
                /*start of third*/

                if (three == "#ff0000") {
                    A7.setImageResource(R.drawable.red);
                }
                if (three == "#0000ff") {
                    A7.setImageResource(R.drawable.blue);
                }
                if (three == "#ffff00") {
                    A7.setImageResource(R.drawable.yellow);
                }
                if (three == "#008000") {
                    A7.setImageResource(R.drawable.green);
                }
                if (three == "#ffa500") {
                    A7.setImageResource(R.drawable.orange);
                }
                if (three == "#800080") {
                    A7.setImageResource(R.drawable.purple);
                }
                if (three == "#000000") {
                    A7.setImageResource(R.drawable.black);
                }
                if (three == "#ffffff") {
                    A7.setImageResource(R.drawable.white);
                }

                if (a1 == aa1) {
                    A1.setImageResource(R.drawable.emptypeg);
                }
                if (a2 == aa2) {
                    A2.setImageResource(R.drawable.emptypeg);
                }
                if (a3 == aa3) {
                    A3.setImageResource(R.drawable.emptypeg);
                } else {
                    if (misplaced == 1) {
                        if (!(A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                            A1.setImageResource(R.drawable.smallwhite);
                        } else {
                            A2.setImageResource(R.drawable.smallwhite);
                        }

                    }
                    if (misplaced == 2) {
                        if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A3.setImageResource(R.drawable.smallwhite);
                            A2.setImageResource(R.drawable.smallwhite);
                        }
                        if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A1.setImageResource(R.drawable.smallwhite);
                            A3.setImageResource(R.drawable.smallwhite);
                        }
                        if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A1.setImageResource(R.drawable.smallwhite);
                            A2.setImageResource(R.drawable.smallwhite);
                        }

                    }
                    if (misplaced == 3) {
                        A1.setImageResource(R.drawable.smallwhite);
                        A2.setImageResource(R.drawable.smallwhite);
                        A3.setImageResource(R.drawable.smallwhite);

                    }
                }
            }
            if (user_infor.length() == 4) {

                char a1 = user_infor.charAt(0);
                char a2 = user_infor.charAt(1);
                char a3 = user_infor.charAt(2);
                char a4 = user_infor.charAt(3);


                char aa1 = flag.charAt(0);
                char aa2 = flag.charAt(1);
                char aa3 = flag.charAt(2);
                char aa4 = flag.charAt(3);


                String one = get_the_Color(a1);
                String two = get_the_Color(a2);
                String three = get_the_Color(a3);
                String four = get_the_Color(a4);

                if (one == "#ff0000") {
                    A5.setImageResource(R.drawable.red);
                }
                if (one == "#0000ff") {
                    A5.setImageResource(R.drawable.blue);
                }
                if (one == "#ffff00") {
                    A5.setImageResource(R.drawable.yellow);
                }
                if (one == "#008000") {
                    A5.setImageResource(R.drawable.green);
                }
                if (one == "#ffa500") {
                    A5.setImageResource(R.drawable.orange);
                }
                if (one == "#800080") {
                    A5.setImageResource(R.drawable.purple);
                }
                if (one == "#000000") {
                    A5.setImageResource(R.drawable.black);
                }
                if (one == "#ffffff") {
                    A5.setImageResource(R.drawable.white);
                }
                /*Second*/

                if (two == "#ff0000") {
                    A6.setImageResource(R.drawable.red);
                }
                if (two == "#0000ff") {
                    A6.setImageResource(R.drawable.blue);
                }
                if (two == "#ffff00") {
                    A6.setImageResource(R.drawable.yellow);
                }
                if (two == "#008000") {
                    A6.setImageResource(R.drawable.green);
                }
                if (two == "#ffa500") {
                    A6.setImageResource(R.drawable.orange);
                }
                if (two == "#800080") {
                    A6.setImageResource(R.drawable.purple);
                }
                if (two == "#000000") {
                    A6.setImageResource(R.drawable.black);
                }
                if (two == "#ffffff") {
                    A6.setImageResource(R.drawable.white);
                }
                /*end of second*/
                /*start of third*/

                if (three == "#ff0000") {
                    A7.setImageResource(R.drawable.red);
                }
                if (three == "#0000ff") {
                    A7.setImageResource(R.drawable.blue);
                }
                if (three == "#ffff00") {
                    A7.setImageResource(R.drawable.yellow);
                }
                if (three == "#008000") {
                    A7.setImageResource(R.drawable.green);
                }
                if (three == "#ffa500") {
                    A7.setImageResource(R.drawable.orange);
                }
                if (three == "#800080") {
                    A7.setImageResource(R.drawable.purple);
                }
                if (three == "#000000") {
                    A7.setImageResource(R.drawable.black);
                }
                if (three == "#ffffff") {
                    A7.setImageResource(R.drawable.white);
                }
                /*end of third*/
                /*start of fourth*/

                if (four == "#ff0000") {
                    A8.setImageResource(R.drawable.red);
                }
                if (four == "#0000ff") {
                    A8.setImageResource(R.drawable.blue);
                }
                if (four == "#ffff00") {
                    A8.setImageResource(R.drawable.yellow);
                }
                if (four == "#008000") {
                    A8.setImageResource(R.drawable.green);
                }
                if (four == "#ffa500") {
                    A8.setImageResource(R.drawable.orange);
                }
                if (four == "#800080") {
                    A8.setImageResource(R.drawable.purple);
                }
                if (four == "#000000") {
                    A8.setImageResource(R.drawable.black);
                }
                if (four == "#ffffff") {
                    A8.setImageResource(R.drawable.white);
                }

                if (a1 == aa1) {
                    A1.setImageResource(R.drawable.emptypeg);
                }
                if (a2 == aa2) {
                    A2.setImageResource(R.drawable.emptypeg);
                }
                if (a3 == aa3) {
                    A3.setImageResource(R.drawable.emptypeg);
                }
                if (a4 == aa4) {
                    A4.setImageResource(R.drawable.emptypeg);
                }
                if (misplaced == 4) {
                    A1.setImageResource(R.drawable.smallwhite);
                    A2.setImageResource(R.drawable.smallwhite);
                    A3.setImageResource(R.drawable.smallwhite);
                    A4.setImageResource(R.drawable.smallwhite);

                }
                if (misplaced == 3) {
                    if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {

                        A2.setImageResource(R.drawable.smallwhite);
                        A3.setImageResource(R.drawable.smallwhite);
                        A4.setImageResource(R.drawable.smallwhite);
                    }
                    if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {

                        A1.setImageResource(R.drawable.smallwhite);
                        A3.setImageResource(R.drawable.smallwhite);
                        A4.setImageResource(R.drawable.smallwhite);
                    }
                    if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {

                        A2.setImageResource(R.drawable.smallwhite);
                        A1.setImageResource(R.drawable.smallwhite);
                        A4.setImageResource(R.drawable.smallwhite);
                    }
                    if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {

                        A2.setImageResource(R.drawable.smallwhite);
                        A3.setImageResource(R.drawable.smallwhite);
                        A1.setImageResource(R.drawable.smallwhite);
                    }

                }
                if (misplaced == 2) {
                    if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                        if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A3.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                        }
                        if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A2.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                        }
                        if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A3.setImageResource(R.drawable.smallwhite);
                            A2.setImageResource(R.drawable.smallwhite);
                        }
                    }
                    if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                        if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A3.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                        }
                        if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A1.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                        }
                        if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A1.setImageResource(R.drawable.smallwhite);
                            A2.setImageResource(R.drawable.smallwhite);
                        }
                    }
                    if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                        if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A1.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                        }
                        if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A2.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                        }
                        if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A3.setImageResource(R.drawable.smallwhite);
                            A2.setImageResource(R.drawable.smallwhite);
                        }
                    }
                    if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                        if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A3.setImageResource(R.drawable.smallwhite);
                            A1.setImageResource(R.drawable.smallwhite);
                        }
                        if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A2.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                        }
                        if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A3.setImageResource(R.drawable.smallwhite);
                            A2.setImageResource(R.drawable.smallwhite);
                        }
                    }

                }
                if (misplaced == 1) {
                    if (!(A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                        A1.setImageResource(R.drawable.smallwhite);
                    }
                    if (!(A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                        A1.setImageResource(R.drawable.smallwhite);
                    }
                    if (!(A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                        A1.setImageResource(R.drawable.smallwhite);
                    }
                    if (!(A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                        A1.setImageResource(R.drawable.smallwhite);
                    }
                }

                A5.setVisibility(View.VISIBLE);
                A6.setVisibility(View.VISIBLE);
                A7.setVisibility(View.VISIBLE);
                A8.setVisibility(View.VISIBLE);
                A9.setVisibility(View.INVISIBLE);
                A10.setVisibility(View.INVISIBLE);
            }
            if (user_infor.length() == 5) {

                char a1 = user_infor.charAt(0);
                char a2 = user_infor.charAt(1);
                char a3 = user_infor.charAt(2);
                char a4 = user_infor.charAt(3);
                char a5 = user_infor.charAt(4);

                char aa1 = flag.charAt(0);
                char aa2 = flag.charAt(1);
                char aa3 = flag.charAt(2);
                char aa4 = flag.charAt(3);
                char aa5 = flag.charAt(4);

                String one = get_the_Color(a1);
                String two = get_the_Color(a2);
                String three = get_the_Color(a3);
                String four = get_the_Color(a4);
                String five = get_the_Color(aa5);

                if (one == "#ff0000") {
                    A5.setImageResource(R.drawable.red);
                }
                if (one == "#0000ff") {
                    A5.setImageResource(R.drawable.blue);
                }
                if (one == "#ffff00") {
                    A5.setImageResource(R.drawable.yellow);
                }
                if (one == "#008000") {
                    A5.setImageResource(R.drawable.green);
                }
                if (one == "#ffa500") {
                    A5.setImageResource(R.drawable.orange);
                }
                if (one == "#800080") {
                    A5.setImageResource(R.drawable.purple);
                }
                if (one == "#000000") {
                    A5.setImageResource(R.drawable.black);
                }
                if (one == "#ffffff") {
                    A5.setImageResource(R.drawable.white);
                }
                /*Second*/

                if (two == "#ff0000") {
                    A6.setImageResource(R.drawable.red);
                }
                if (two == "#0000ff") {
                    A6.setImageResource(R.drawable.blue);
                }
                if (two == "#ffff00") {
                    A6.setImageResource(R.drawable.yellow);
                }
                if (two == "#008000") {
                    A6.setImageResource(R.drawable.green);
                }
                if (two == "#ffa500") {
                    A6.setImageResource(R.drawable.orange);
                }
                if (two == "#800080") {
                    A6.setImageResource(R.drawable.purple);
                }
                if (two == "#000000") {
                    A6.setImageResource(R.drawable.black);
                }
                if (two == "#ffffff") {
                    A6.setImageResource(R.drawable.white);
                }
                /*end of second*/
                /*start of third*/

                if (three == "#ff0000") {
                    A7.setImageResource(R.drawable.red);
                }
                if (three == "#0000ff") {
                    A7.setImageResource(R.drawable.blue);
                }
                if (three == "#ffff00") {
                    A7.setImageResource(R.drawable.yellow);
                }
                if (three == "#008000") {
                    A7.setImageResource(R.drawable.green);
                }
                if (three == "#ffa500") {
                    A7.setImageResource(R.drawable.orange);
                }
                if (three == "#800080") {
                    A7.setImageResource(R.drawable.purple);
                }
                if (three == "#000000") {
                    A7.setImageResource(R.drawable.black);
                }
                if (three == "#ffffff") {
                    A7.setImageResource(R.drawable.white);
                }
                /*end of third*/
                /*start of fourth*/

                if (four == "#ff0000") {
                    A8.setImageResource(R.drawable.red);
                }
                if (four == "#0000ff") {
                    A8.setImageResource(R.drawable.blue);
                }
                if (four == "#ffff00") {
                    A8.setImageResource(R.drawable.yellow);
                }
                if (four == "#008000") {
                    A8.setImageResource(R.drawable.green);
                }
                if (four == "#ffa500") {
                    A8.setImageResource(R.drawable.orange);
                }
                if (four == "#800080") {
                    A8.setImageResource(R.drawable.purple);
                }
                if (four == "#000000") {
                    A8.setImageResource(R.drawable.black);
                }
                if (four == "#ffffff") {
                    A8.setImageResource(R.drawable.white);
                }

                if (five == "#ff0000") {
                    A9.setImageResource(R.drawable.red);
                }
                if (five == "#0000ff") {
                    A9.setImageResource(R.drawable.blue);
                }
                if (five == "#ffff00") {
                    A9.setImageResource(R.drawable.yellow);
                }
                if (five == "#008000") {
                    A9.setImageResource(R.drawable.green);
                }
                if (five == "#ffa500") {
                    A9.setImageResource(R.drawable.orange);
                }
                if (five == "#800080") {
                    A9.setImageResource(R.drawable.purple);
                }
                if (five == "#000000") {
                    A9.setImageResource(R.drawable.black);
                }
                if (five == "#ffffff") {
                    A9.setImageResource(R.drawable.white);
                }

                if (a1 == aa1) {
                    A1.setImageResource(R.drawable.emptypeg);
                }
                if (a2 == aa2) {
                    A2.setImageResource(R.drawable.emptypeg);
                }
                if (a3 == aa3) {
                    A3.setImageResource(R.drawable.emptypeg);
                }
                if (a4 == aa4) {
                    A4.setImageResource(R.drawable.emptypeg);
                }
                if (a5 == aa5) {
                    A11.setImageResource(R.drawable.emptypeg);
                } else {
                    if (misplaced == 5) {
                        A1.setImageResource(R.drawable.smallwhite);
                        A2.setImageResource(R.drawable.smallwhite);
                        A3.setImageResource(R.drawable.smallwhite);
                        A4.setImageResource(R.drawable.smallwhite);
                        A11.setImageResource(R.drawable.smallwhite);

                    }
                    if (misplaced == 4) {

                        if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A2.setImageResource(R.drawable.smallwhite);
                            A3.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                            A11.setImageResource(R.drawable.smallwhite);
                        }
                        if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A1.setImageResource(R.drawable.smallwhite);
                            A3.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                            A11.setImageResource(R.drawable.smallwhite);
                        }
                        if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A2.setImageResource(R.drawable.smallwhite);
                            A1.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                            A11.setImageResource(R.drawable.smallwhite);
                        }
                        if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A2.setImageResource(R.drawable.smallwhite);
                            A3.setImageResource(R.drawable.smallwhite);
                            A1.setImageResource(R.drawable.smallwhite);
                            A11.setImageResource(R.drawable.smallwhite);
                        }
                        if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A2.setImageResource(R.drawable.smallwhite);
                            A3.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                            A1.setImageResource(R.drawable.smallwhite);
                        }
                    }
                    if (misplaced == 3) {
                        if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A2.setImageResource(R.drawable.smallwhite);
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A2.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                            }

                        }
                        if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A1.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A.setImageResource(R.drawable.smallwhite);
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                            }
                        }
                        if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A1.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                            }
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A2.setImageResource(R.drawable.smallwhite);
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A2.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                            }
                        }
                        if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                                A2.setImageResource(R.drawable.smallwhite);
                            }
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A2.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                            }
                        }
                        if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                            }
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A2.setImageResource(R.drawable.smallwhite);
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A2.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                            }
                        }

                    }
                    if (misplaced == 2) {
                        if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }

                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }

                        }
                        if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                        }
                        if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                            }
                        }
                        if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                        }
                        if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                        }

                    }
                    if (misplaced == 1) {
                        if (!(A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                            A1.setImageResource(R.drawable.smallwhite);
                        }
                        if (!(A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                            A2.setImageResource(R.drawable.smallwhite);
                        }
                        if (!(A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                            A3.setImageResource(R.drawable.smallwhite);
                        }
                        if (!(A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                            A4.setImageResource(R.drawable.smallwhite);
                        }
                        if (!(A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                            A11.setImageResource(R.drawable.smallwhite);
                        }
                    }
                }

                A5.setVisibility(View.VISIBLE);
                A6.setVisibility(View.VISIBLE);
                A7.setVisibility(View.VISIBLE);
                A8.setVisibility(View.VISIBLE);
                A9.setVisibility(View.VISIBLE);
                A10.setVisibility(View.INVISIBLE);

            }
            if (user_infor.length() == 6) {

                char a1 = user_infor.charAt(0);
                char a2 = user_infor.charAt(1);
                char a3 = user_infor.charAt(2);
                char a4 = user_infor.charAt(3);
                char a5 = user_infor.charAt(4);
                char a6 = user_infor.charAt(5);

                char aa1 = flag.charAt(0);
                char aa2 = flag.charAt(1);
                char aa3 = flag.charAt(2);
                char aa4 = flag.charAt(3);
                char aa5 = flag.charAt(4);
                char aa6 = flag.charAt(5);

                String one = get_the_Color(a1);
                String two = get_the_Color(a2);
                String three = get_the_Color(a3);
                String four = get_the_Color(a4);
                String five = get_the_Color(a5);
                String six = get_the_Color(a6);

                if (one == "#ff0000") {
                    A5.setImageResource(R.drawable.red);
                }
                if (one == "#0000ff") {
                    A5.setImageResource(R.drawable.blue);
                }
                if (one == "#ffff00") {
                    A5.setImageResource(R.drawable.yellow);
                }
                if (one == "#008000") {
                    A5.setImageResource(R.drawable.green);
                }
                if (one == "#ffa500") {
                    A5.setImageResource(R.drawable.orange);
                }
                if (one == "#800080") {
                    A5.setImageResource(R.drawable.purple);
                }
                if (one == "#000000") {
                    A5.setImageResource(R.drawable.black);
                }
                if (one == "#ffffff") {
                    A5.setImageResource(R.drawable.white);
                }
                /*Second*/

                if (two == "#ff0000") {
                    A6.setImageResource(R.drawable.red);
                }
                if (two == "#0000ff") {
                    A6.setImageResource(R.drawable.blue);
                }
                if (two == "#ffff00") {
                    A6.setImageResource(R.drawable.yellow);
                }
                if (two == "#008000") {
                    A6.setImageResource(R.drawable.green);
                }
                if (two == "#ffa500") {
                    A6.setImageResource(R.drawable.orange);
                }
                if (two == "#800080") {
                    A6.setImageResource(R.drawable.purple);
                }
                if (two == "#000000") {
                    A6.setImageResource(R.drawable.black);
                }
                if (two == "#ffffff") {
                    A6.setImageResource(R.drawable.white);
                }
                /*end of second*/
                /*start of third*/

                if (three == "#ff0000") {
                    A7.setImageResource(R.drawable.red);
                }
                if (three == "#0000ff") {
                    A7.setImageResource(R.drawable.blue);
                }
                if (three == "#ffff00") {
                    A7.setImageResource(R.drawable.yellow);
                }
                if (three == "#008000") {
                    A7.setImageResource(R.drawable.green);
                }
                if (three == "#ffa500") {
                    A7.setImageResource(R.drawable.orange);
                }
                if (three == "#800080") {
                    A7.setImageResource(R.drawable.purple);
                }
                if (three == "#000000") {
                    A7.setImageResource(R.drawable.black);
                }
                if (three == "#ffffff") {
                    A7.setImageResource(R.drawable.white);
                }
                /*end of third*/
                /*start of fourth*/

                if (four == "#ff0000") {
                    A8.setImageResource(R.drawable.red);
                }
                if (four == "#0000ff") {
                    A8.setImageResource(R.drawable.blue);
                }
                if (four == "#ffff00") {
                    A8.setImageResource(R.drawable.yellow);
                }
                if (four == "#008000") {
                    A8.setImageResource(R.drawable.green);
                }
                if (four == "#ffa500") {
                    A8.setImageResource(R.drawable.orange);
                }
                if (four == "#800080") {
                    A8.setImageResource(R.drawable.purple);
                }
                if (four == "#000000") {
                    A8.setImageResource(R.drawable.black);
                }
                if (four == "#ffffff") {
                    A8.setImageResource(R.drawable.white);
                }

                if (five == "#ff0000") {
                    A9.setImageResource(R.drawable.red);
                }
                if (five == "#0000ff") {
                    A9.setImageResource(R.drawable.blue);
                }
                if (five == "#ffff00") {
                    A9.setImageResource(R.drawable.yellow);
                }
                if (five == "#008000") {
                    A9.setImageResource(R.drawable.green);
                }
                if (five == "#ffa500") {
                    A9.setImageResource(R.drawable.orange);
                }
                if (five == "#800080") {
                    A9.setImageResource(R.drawable.purple);
                }
                if (five == "#000000") {
                    A9.setImageResource(R.drawable.black);
                }
                if (five == "#ffffff") {
                    A9.setImageResource(R.drawable.white);
                }


                if (six == "#ff0000") {
                    A10.setImageResource(R.drawable.red);
                }
                if (six == "#0000ff") {
                    A10.setImageResource(R.drawable.blue);
                }
                if (six == "#ffff00") {
                    A10.setImageResource(R.drawable.yellow);
                }
                if (six == "#008000") {
                    A10.setImageResource(R.drawable.green);
                }
                if (six == "#ffa500") {
                    A10.setImageResource(R.drawable.orange);
                }
                if (six == "#800080") {
                    A10.setImageResource(R.drawable.purple);
                }
                if (six == "#000000") {
                    A10.setImageResource(R.drawable.black);
                }
                if (six == "#ffffff") {
                    A10.setImageResource(R.drawable.white);
                }

                if (a1 == aa1) {
                    A1.setImageResource(R.drawable.emptypeg);
                }
                if (a2 == aa2) {
                    A2.setImageResource(R.drawable.emptypeg);
                }
                if (a3 == aa3) {
                    A3.setImageResource(R.drawable.emptypeg);
                }
                if (a4 == aa4) {
                    A4.setImageResource(R.drawable.emptypeg);
                }
                if (a5 == aa5) {
                    A11.setImageResource(R.drawable.emptypeg);
                }
                if (a6 == aa6) {
                    A12.setImageResource(R.drawable.emptypeg);
                } else {

                    if (misplaced == 6) {
                        A1.setImageResource(R.drawable.smallwhite);
                        A2.setImageResource(R.drawable.smallwhite);
                        A3.setImageResource(R.drawable.smallwhite);
                        A4.setImageResource(R.drawable.smallwhite);
                        A11.setImageResource(R.drawable.smallwhite);
                        A12.setImageResource(R.drawable.smallwhite);

                    }
                    if (misplaced == 5) {
                        if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A2.setImageResource(R.drawable.smallwhite);
                            A3.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                            A11.setImageResource(R.drawable.smallwhite);
                            A12.setImageResource(R.drawable.smallwhite);

                        }
                        if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A1.setImageResource(R.drawable.smallwhite);
                            A3.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                            A11.setImageResource(R.drawable.smallwhite);
                            A12.setImageResource(R.drawable.smallwhite);
                        }
                        if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A2.setImageResource(R.drawable.smallwhite);
                            A1.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                            A11.setImageResource(R.drawable.smallwhite);
                            A12.setImageResource(R.drawable.smallwhite);
                        }
                        if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A2.setImageResource(R.drawable.smallwhite);
                            A3.setImageResource(R.drawable.smallwhite);
                            A1.setImageResource(R.drawable.smallwhite);
                            A11.setImageResource(R.drawable.smallwhite);
                            A12.setImageResource(R.drawable.smallwhite);
                        }
                        if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A2.setImageResource(R.drawable.smallwhite);
                            A3.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                            A1.setImageResource(R.drawable.smallwhite);
                            A12.setImageResource(R.drawable.smallwhite);
                        }
                        if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            A2.setImageResource(R.drawable.smallwhite);
                            A3.setImageResource(R.drawable.smallwhite);
                            A4.setImageResource(R.drawable.smallwhite);
                            A11.setImageResource(R.drawable.smallwhite);
                            A1.setImageResource(R.drawable.smallwhite);
                        }

                    }
                    if (misplaced == 4) {
                        if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A2.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A2.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A2.setImageResource(R.drawable.smallwhite);
                            }

                        }
                        if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A1.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A1.setImageResource(R.drawable.smallwhite);
                                A3.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A1.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A3.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A1.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A3.setImageResource(R.drawable.smallwhite);
                            }
                        }
                        if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A1.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                            }
                        }
                        if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A3.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A3.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A3.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A3.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                            }
                        }
                        if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A3.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A1.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A3.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A3.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                                A12.setImageResource(R.drawable.smallwhite);
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A3.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                            }
                        }
                        if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A3.setImageResource(R.drawable.smallwhite);
                            }
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A1.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A3.setImageResource(R.drawable.smallwhite);
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A3.setImageResource(R.drawable.smallwhite);
                                A11.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                A2.setImageResource(R.drawable.smallwhite);
                                A4.setImageResource(R.drawable.smallwhite);
                                A3.setImageResource(R.drawable.smallwhite);
                                A1.setImageResource(R.drawable.smallwhite);
                            }
                        }
                    }
                    if (misplaced == 3) {
                        if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }

                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }

                        }
                        if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                        }
                        if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                            }
                        }
                        if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                        }
                        if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                        }
                        if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                        }

                    }
                    if (misplaced == 2) {
                        /*start here*/
                        if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A4.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A4.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A3.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A3.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A3.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A4.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A3.setImageResource(R.drawable.smallwhite);
                                        A4.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A3.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A4.setImageResource(R.drawable.smallwhite);
                                        A3.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A4.setImageResource(R.drawable.smallwhite);
                                    }
                                }

                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A4.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A4.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A2.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A2.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A4.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A2.setImageResource(R.drawable.smallwhite);
                                        A4.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A2.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A3.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A3.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A2.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A3.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A2.setImageResource(R.drawable.smallwhite);
                                        A3.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A3.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A3.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A2.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A4.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A2.setImageResource(R.drawable.smallwhite);
                                        A4.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A2.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A4.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A4.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A2.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A4.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A4.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A11.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                            }

                        }
                        if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A4.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A4.setImageResource(R.drawable.smallwhite);
                                        A11.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A11.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A4.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A11.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A11.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                            }
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A4.setImageResource(R.drawable.smallwhite);
                                        A11.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A2.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A11.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                        }
                        if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                            }
                        }
                        if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                        }
                        if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A12.setImageResource(R.drawable.smallwhite);
                                }
                                if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A2.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                        }
                        if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                            if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A11.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                            if (A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                if (A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A1.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A2.setImageResource(R.drawable.smallwhite);
                                        A12.setImageResource(R.drawable.smallwhite);
                                    }
                                    if (A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                        A11.setImageResource(R.drawable.smallwhite);
                                        A4.setImageResource(R.drawable.smallwhite);
                                    }
                                }
                                if (A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                    A2.setImageResource(R.drawable.smallwhite);
                                }
                                if (A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg)) {
                                    A4.setImageResource(R.drawable.smallwhite);
                                    A1.setImageResource(R.drawable.smallwhite);
                                    A3.setImageResource(R.drawable.smallwhite);
                                }
                            }
                        }
                        /*end here*/


                    }
                    if (misplaced == 1) {
                        if (!(A1.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                            A1.setImageResource(R.drawable.smallwhite);
                        }
                        if (!(A2.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                            A2.setImageResource(R.drawable.smallwhite);
                        }
                        if (!(A3.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                            A3.setImageResource(R.drawable.smallwhite);
                        }
                        if (!(A4.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                            A4.setImageResource(R.drawable.smallwhite);
                        }
                        if (!(A11.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                            A11.setImageResource(R.drawable.smallwhite);
                        }
                        if (!(A12.getDrawable() == getResources().getDrawable(R.drawable.emptypeg))) {
                            A12.setImageResource(R.drawable.smallwhite);
                        }
                    }
                }

                A5.setVisibility(View.VISIBLE);
                A6.setVisibility(View.VISIBLE);
                A7.setVisibility(View.VISIBLE);
                A8.setVisibility(View.VISIBLE);
                A9.setVisibility(View.VISIBLE);
                A10.setVisibility(View.VISIBLE);

            }


            return row;
        }
    }

    private String get_the_Color(char first) {
        if (first == 'A') {
            return "#ff0000";
        } else if (first == 'B') {
            return "#0000ff";
        } else if (first == 'C') {
            return "#ffff00";
        } else if (first == 'D') {
            return "#008000";
        } else if (first == 'E') {
            return "#ffa500";
        } else if (first == 'F') {
            return "#800080";
        } else if (first == 'G') {
            return "#000000";
        } else if (first == 'H') {
            return "#ffffff";
        }
        return null;
    }

    private class Game {
        int c, j, l, x;
        String input;

        public Game() {
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public int getC() {
            return c;
        }

        public void setC(int c) {
            this.c = c;
        }

        public int getJ() {
            return j;
        }

        public void setJ(int j) {
            this.j = j;
        }

        public int getL() {
            return l;
        }

        public void setL(int l) {
            this.l = l;
        }
    }
}
