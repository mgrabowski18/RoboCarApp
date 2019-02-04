package com.example.marcin.myrobocarapp;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    private Context mContext;
    private RelativeLayout layout_joystick;
    private TextView textView5, textView6, textView7, textView8;
    private String ip="192.168.4.1";
    private String dirs;
    private int distance1 =200;
    private int distance2 =200;
    private String pows;
    private int pow=0;
    private int sound1, sound2, sound3, sound4, sound5;
    private int StreamId1, StreamId2;
    private String[] ref_pows=new String[2];
    private String[] ref_dirs=new String[2];

    private JoyStickClass js;

    private ImageSwitcher sw1;
    private ImageSwitcher sw2;

    private SoundPool sound;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onCreate(Bundle savedInstanceState) {
        mContext = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);

        layout_joystick = findViewById(R.id.layout_joystick);

        js = new JoyStickClass(getApplicationContext()
                , layout_joystick, R.drawable.top2);
        js.setStickSize(370, 370);
        js.setLayoutSize(900, 900);
        js.setLayoutAlpha(255);
        js.setStickAlpha(255);
        js.setOffset(250);
        js.setMinimumDistance(50);

        js.drawStick0();

        int direction = js.get8Direction();
        if(direction == JoyStickClass.STICK_UP) {
            textView5.setText("Direction : Up");
        } else if(direction == JoyStickClass.STICK_UPRIGHT) {
            textView5.setText("Direction : Up Right");
        } else if(direction == JoyStickClass.STICK_RIGHT) {
            textView5.setText("Direction : Right");
        } else if(direction == JoyStickClass.STICK_DOWNRIGHT) {
            textView5.setText("Direction : Down Right");
        } else if(direction == JoyStickClass.STICK_DOWN) {
            textView5.setText("Direction : Down");
        } else if(direction == JoyStickClass.STICK_DOWNLEFT) {
            textView5.setText("Direction : Down Left");
        } else if(direction == JoyStickClass.STICK_LEFT) {
            textView5.setText("Direction : Left");
        } else if(direction == JoyStickClass.STICK_UPLEFT) {
            textView5.setText("Direction : Up Left");
        } else if(direction == JoyStickClass.STICK_NONE) {
            textView5.setText("Direction : Center");
        }

        textView6.setText("Power : " + String.valueOf(js.getPower()));
        Timer timer = new Timer();
        TimerTask task = new TimerTask()
        {
            @Override
            public void run() {
                new getValue().execute();
            }
        };
        long whenToStar = 1*1000L;
        long howOften = 1*1000L;
        timer.scheduleAtFixedRate(task, whenToStar, howOften);



        layout_joystick.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);


                int direction = js.get8Direction();
                if(direction == JoyStickClass.STICK_UP) {
                    textView5.setText("Direction : Up");
                    dirs="10";
                } else if(direction == JoyStickClass.STICK_UPRIGHT) {
                    textView5.setText("Direction : Up Right");
                    dirs="20";
                } else if(direction == JoyStickClass.STICK_RIGHT) {
                    textView5.setText("Direction : Right");
                    dirs="30";
                } else if(direction == JoyStickClass.STICK_DOWNRIGHT) {
                    textView5.setText("Direction : Down Right");
                    dirs="40";
                } else if(direction == JoyStickClass.STICK_DOWN) {
                    textView5.setText("Direction : Down");
                    dirs="50";
                } else if(direction == JoyStickClass.STICK_DOWNLEFT) {
                    textView5.setText("Direction : Down Left");
                    dirs="60";
                } else if(direction == JoyStickClass.STICK_LEFT) {
                    textView5.setText("Direction : Left");
                    dirs="70";
                } else if(direction == JoyStickClass.STICK_UPLEFT) {
                    textView5.setText("Direction : Up Left");
                    dirs="80";
                } else if(direction == JoyStickClass.STICK_NONE) {
                    textView5.setText("Direction : Center");
                    dirs="00";
                }

                textView6.setText("Power : " + String.valueOf(js.getPower()));
                pow=Integer.parseInt(String.valueOf(js.getPower()));
                if (pow>=0 && pow<=9)
                    pows = "00";
                else if (pow>=10 && pow<=19)
                    pows = "10";
                else if (pow>=20 && pow<=29)
                    pows = "20";
                else if (pow>=30 && pow<=39)
                    pows = "30";
                else if (pow>=40 && pow<=49)
                    pows = "40";
                else if (pow>=50 && pow<=59)
                    pows = "50";
                else if (pow>=60 && pow<=69)
                    pows = "60";
                else if (pow>=70 && pow<=79)
                    pows = "70";
                else if (pow>=80 && pow<=89)
                    pows = "80";
                else if (pow>=90 && pow<=97)
                    pows = "90";
                else if (pow>=98 && pow<=99)
                    pows = "99";
                else
                    pows = "00";


                ref_pows[1]=pows;
                ref_dirs[1]=dirs;

                if(ref_dirs[0]==null){
                    ref_dirs[0]=ref_dirs[1];
                    ref_pows[0]=ref_pows[1];
                    final String direct="http://"+ip+"/sterowanie?dir="+ref_dirs[1]+"&pow="+ref_pows[1];
                    TaskEsp taskEsp = new TaskEsp(direct);
                    taskEsp.execute();
                }
                else if((!ref_dirs[1].equals(ref_dirs[0])) || (!ref_pows[1].equals(ref_pows[0]))){
                    ref_dirs[0]=ref_dirs[1];
                    ref_pows[0]=ref_pows[1];
                    final String direct="http://"+ip+"/sterowanie?dir="+ref_dirs[1]+"&pow="+ref_pows[1];
                    TaskEsp taskEsp = new TaskEsp(direct);
                    taskEsp.execute();
                }
                return true;
            }
        });
        sw1 = findViewById(R.id.cz1);
        sw1.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setImageResource(R.drawable.ic_sensor1);
                return imageView;
            }
        });

        sw2 = findViewById(R.id.cz2);
        sw2.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setImageResource(R.drawable.ic_sensor2);
                return imageView;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            sound = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else
        {
            sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }


        sound1=sound.load(this, R.raw.a1, 1);
        sound2=sound.load(this, R.raw.a2, 1);
        sound3=sound.load(this, R.raw.a3, 1);
        sound4=sound.load(this, R.raw.a4, 1);
        sound5=sound.load(this, R.raw.a5, 1);


    }

    private class TaskEsp extends AsyncTask<Void, Void, String> {

        String server;

        TaskEsp(String server){
            this.server = server;
        }

        @Override
        protected String doInBackground(Void... params) {

            final String p = server;

            String serverResponse;
            HttpClient httpclient = new DefaultHttpClient();
            try {
                HttpGet httpGet = new HttpGet();
                httpGet.setURI(new URI(p));
                HttpResponse httpResponse = httpclient.execute(httpGet);

                InputStream inputStream = null;
                inputStream = httpResponse.getEntity().getContent();
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream));
                serverResponse = bufferedReader.readLine();

                inputStream.close();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            }

            return serverResponse;
        }
    }

    private class getValue extends AsyncTask<Void, Void, String[]> {
        int priority =0;
        String div_text[]=new String[3];
        char[] parsed_div;
        @Override
        protected String[] doInBackground(Void... params) {
            int check;
            try {
                Document doc = Jsoup.connect("http://192.168.4.1/json").get();
                Element div = doc.body().select("div").first();
                div_text[2] = div.text();
                parsed_div = div_text[2].toCharArray();
                check= div_text[2].length();

                for(int i=3; i>=1; i--)
                {
                    if (Character.isDigit(parsed_div[8-i]))
                    {
                        div_text[0] += parsed_div[8-i];
                    }
                }

                for(int i=3; i>=1; i--)
                {
                    if(Character.isDigit(parsed_div[check-i]))
                    {
                        div_text[1]+= parsed_div[check-i];
                    }
                }

            } catch (IOException e){
                e.printStackTrace();
            }
            return div_text;
        }

        @Override
        protected void onPostExecute(String[] div_text){
            if(div_text[0]==null)
                distance1 = 200;
            else
                distance1 = Integer.parseInt(div_text[0]);



            if(div_text[1]==null)
                distance2 = 200;
            else
                distance2 = Integer.parseInt(div_text[1]);


            if (distance1 <= 50 && distance1 > 40) {
                sw1.setImageResource(R.drawable.ic_sensor1_1);
                StreamId1 = sound.play(sound1, 1, 1, 1, 0, 1);
                priority = 1;
            } else if (distance1 <= 40 && distance1 > 30) {
                sw1.setImageResource(R.drawable.ic_sensor1_2);
                StreamId1 = sound.play(sound2, 1, 1, 1, 0, 1);
                priority = 2;
            } else if (distance1 <= 30 && distance1 > 20) {
                sw1.setImageResource(R.drawable.ic_sensor1_3);
                StreamId1 = sound.play(sound3, 1, 1, 1, 0, 1);
                priority = 3;
            } else if (distance1 <= 20 && distance1 > 10) {
                sw1.setImageResource(R.drawable.ic_sensor1_4);
                StreamId1 = sound.play(sound4, 1, 1, 1, 0, 1);
                priority = 4;
            } else if (distance1 <= 10) {
                sw1.setImageResource(R.drawable.ic_sensor1_5);
                StreamId1 = sound.play(sound5, 1, 1, 1, 0, 1);
                priority = 5;
            } else {
                sw1.setImageResource(R.drawable.ic_sensor1);
                sound.stop(StreamId1);
                priority = 0;
            }



            if (distance2 <= 50 && distance2 > 40) {
                sw2.setImageResource(R.drawable.ic_sensor2_1);
                if(priority<1) {
                    StreamId2 = sound.play(sound1, 1, 1, 1, 0, 1);
                    sound.stop(StreamId1);
                }
            } else if (distance2 <= 40 && distance2 > 30) {
                sw2.setImageResource(R.drawable.ic_sensor2_2);
                if(priority<2) {
                    StreamId2 = sound.play(sound2, 1, 1, 1, 0, 1);
                    sound.stop(StreamId1);
                }
            } else if (distance2 <= 30 && distance2 > 20) {
                sw2.setImageResource(R.drawable.ic_sensor2_3);
                if(priority<3) {
                    StreamId2 = sound.play(sound3, 1, 1, 1, 0, 1);
                    sound.stop(StreamId1);
                }
            } else if (distance2 <= 20 && distance2 > 10) {
                sw2.setImageResource(R.drawable.ic_sensor2_4);
                if(priority<4) {
                    StreamId2 = sound.play(sound4, 1, 1, 1, 0, 1);
                    sound.stop(StreamId1);
                }
            } else if (distance2 <= 10) {
                sw2.setImageResource(R.drawable.ic_sensor2_5);
                if(priority<5) {
                    StreamId2 = sound.play(sound5, 1, 1, 1, 0, 1);
                    sound.stop(StreamId1);
                }
            } else {
                sw2.setImageResource(R.drawable.ic_sensor2);
                sound.stop(StreamId2);
            }

            textView7.setText("Sensor1: "+ distance1);
            textView8.setText("Sensor2: "+ distance2);
        }
    }
}
