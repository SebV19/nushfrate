package com.example.nushfrate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import static java.lang.Long.parseLong;


public class HomeActivity extends Fragment implements View.OnClickListener {

//    AppDatabase db = AppDatabase.getDatabase(getActivity());

    Money bani = new Money(1500);
        private int code = 0;
        private int code1 = 1;
        private Button button;
        private HomeActivityListener listener;

        public interface HomeActivityListener {
            void onInputHomeSent(Money input);
        }


        @Nullable
        public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstancesState) {

            View v = inflater.inflate(R.layout.home_activity, container, false);
            TextView theMoneyText = (TextView) v.findViewById(R.id.textView2);
            theMoneyText.setText("$" + String.valueOf(bani.getSum()));
            //iconite de random idee, zaruri si un om langa
            Button btnRandom =  v.findViewById(R.id.Random);

            ImageButton btnPierde = v.findViewById(R.id.Pierde);
            ImageButton btnPrimeste = v.findViewById(R.id.Primeste);
            ImageButton btnQuickAdd = v.findViewById(R.id.Quickadd);

            btnRandom.setOnClickListener(this);
            btnPierde.setOnClickListener(this);
            btnPrimeste.setOnClickListener(this);
            btnQuickAdd.setOnClickListener(this);


            return v;


        }
        @Override
        public void onClick(View view) {
            TextView theMoneyText = getActivity().findViewById(R.id.textView2);
            String number, prop;
            switch (view.getId()) {
                case R.id.Pierde:
                    MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.crowd_boo);
                    mediaPlayer.start();
                    Intent intent = new Intent(getActivity(), Pop.class);
                    startActivityForResult(intent, code);
                    break;

                case R.id.Primeste:
                    MediaPlayer mediaPlayerL = MediaPlayer.create(getActivity(), R.raw.cha_ching);
                    mediaPlayerL.start();
                    Intent intentW = new Intent(getActivity(), Pop2.class);
                    startActivityForResult(intentW, code1);
                    break;

                case R.id.Quickadd:
                    bani.cresteBani(200);
                    theMoneyText.setText(String.valueOf(bani.getSum()));
                    prop = "Saracule, ai trecut de start, poftim 200";
                    MediaPlayer mediaPlayer200 = MediaPlayer.create(getActivity(), R.raw.cha_ching);
                    mediaPlayer200.start();
                    Toast.makeText(getActivity(), prop, Toast.LENGTH_LONG).show();
                    listener.onInputHomeSent(bani);
                    break;

                case R.id.Random:
                    Intent intentR = new Intent(getActivity(), RandomPlayers.class);
                    startActivity(intentR);
                    break;


                default:
                    throw new IllegalStateException("Unexpected value: " + view.getId());
            }


        }

    @Override
    public void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == code) {
            if (resultCode == Activity.RESULT_OK) {
                String return_text = data.getStringExtra(Intent.EXTRA_TEXT);
                if (!return_text.isEmpty()) {
                    bani.scadeBani(parseLong(return_text));
                    TextView theMoneyText = (TextView) getView().findViewById(R.id.textView2);
                    theMoneyText.setText(String.valueOf(bani.getSum()));
                    if (bani.getSum() == -1) {
                        String prop = "Ai pierdut BOSS=(((";
                        Toast.makeText(getActivity(), prop, Toast.LENGTH_SHORT).show();
                    }
                    listener.onInputHomeSent(bani);
                }
            }
        }

        if (requestCode == code1) {
            if (resultCode == Activity.RESULT_OK) {
                String return_text = data.getStringExtra(Intent.EXTRA_TEXT);
                if (!return_text.isEmpty()) {
                    bani.cresteBani(Long.parseLong(return_text));
                    TextView theMoneyText = (TextView) getView().findViewById(R.id.textView2);
                    theMoneyText.setText(String.valueOf(bani.getSum()));
                    Toast.makeText(getActivity(), "merge", Toast.LENGTH_SHORT).show();
                }
                listener.onInputHomeSent(bani);
            }
        }
    }
    public void updateBani(Money newSum){
            bani.setSum(newSum.getSum());
            bani.setUser(newSum.getUser());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof HomeActivityListener){
            listener = (HomeActivityListener) context;
        }else{

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}


