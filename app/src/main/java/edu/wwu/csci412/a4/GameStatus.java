package edu.wwu.csci412.a4;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameStatus extends Fragment {

    private static TextView textView0;
    private TextView textView1;


    public interface Callbacks{
        public void setTexView0(String s);
        public void play();
    };



    public GameStatus() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_game_status, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View fragmentView = getView();
        textView0 = fragmentView.findViewById(R.id.game_status_0);
        textView1 = fragmentView.findViewById(R.id.game_status_1);
        setLivesText("");
    }

        @Override
        public void onAttach(Context context) {
            super.onAttach( context );
            if ( !( context instanceof Callbacks ) ) {
                throw new IllegalStateException(
                        "Context must implement fragment's callbacks." );
            }
        }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setLivesText(String test){
    }

    public void play(){
    }
}
