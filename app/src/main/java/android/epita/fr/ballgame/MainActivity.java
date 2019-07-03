package android.epita.fr.ballgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.epita.fr.ballgame.adapters.GameGridAdapter;
import android.epita.fr.ballgame.models.GridModel;
import android.epita.fr.ballgame.path_values.PathValue_Set_One;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //Ball variables
    private ViewGroup mainLayout;
    private TextView redball;
    ImageView won,lost;
    Vibrator v;
    Timer T;
    Toast toast;

    //initialize the row and column value that enables the movement
    int row_curent=0;
    int column_current=0;
    //this the HP(Health Points) just like games
    float hp_value=50;

    //grid
    GridView gridView;
    ArrayList<GridModel> gridArray = new ArrayList<GridModel>();
    GameGridAdapter gameGridAdapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        initia();

        hp_counter();
    }

    void initia()
    {
        //initialize views
        mainLayout = (RelativeLayout) findViewById(R.id.main);
        redball = (TextView) findViewById(R.id.redball);
        gridView = (GridView) findViewById(R.id.gridView1);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        toast = new Toast(this);
        won = new ImageView(this);
        won.setImageResource(R.drawable.winner);
        lost = new ImageView(this);
        lost.setImageResource(R.drawable.loser);

        //set the value of ball
        redball.setText(hp_value+"");

        //Set the grid values from /path_values/Path_Value_Set_One
        //this is well thought path values to have both win and lose situation
        for(int i = 0; i< PathValue_Set_One.set_one_a.length; i++)
            gridArray.add(new GridModel(PathValue_Set_One.set_one_a[i]));

        //Set the grid for the the values by fashion of 8x4
        gameGridAdapter = new GameGridAdapter(this, R.layout.row_grid_view, gridArray);
        gridView.setAdapter(gameGridAdapter);

        //set item click listener for grid
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                //this function allows us to to move the ball across by left,right,bottom,top
                move(view,pos);

            }
        });
    }

    void move(View view,int pos)
    {
        //on click event gives us the position using which we can calculate row and column of selected position
        int incoming_row=pos/4;
        int incoming_column=pos%4;

        //here the move need to be left , right ,bottom and top ,but not side ways
        //also movement should be to immediate block and not at some distant block in the grid
        //for eg. ball at 0x0 should be only permitted to 0x1 and 1x0 and no where else
        if((incoming_row==row_curent+1 && incoming_column==column_current)  ||
           (incoming_row==row_curent-1 && incoming_column==column_current) ||
           (incoming_column==column_current+1 && incoming_row==row_curent) ||
           (incoming_column==column_current-1 && incoming_row==row_curent))  {

                //Based on successful block selection we need to move the ball to the selected position
                //by setting its parameters
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(100, 100);
                lp.setMargins((int) view.getX() + view.getWidth() / 10, (int) view.getY() + view.getHeight() / 10, 0, 0);
                redball.setLayoutParams(lp);

                //Now that ball has moved to the new position, we need to update the current row and current column
                row_curent = incoming_row;
                column_current = incoming_column;

                //to remove the visted nodes
                view.setVisibility(View.GONE);

            //when we traverse through we need to add or subtract the values of grid to life of the ball (HP)
            //if the value of text field is not 100 move go through this if loop
            if(Float.parseFloat(((TextView)view.findViewById(R.id.item_text)).getText().toString())!=100f) {
                hp_value = hp_value + Float.parseFloat(((TextView) view.findViewById(R.id.item_text)).getText().toString());

                //This is cool feature , every time you land on a negative value on grid phone will vibrate
                if(Float.parseFloat(((TextView)view.findViewById(R.id.item_text)).getText().toString())<0f)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }

                }
            }
            //we reached the goal that is 100
            else {
                //Here's a funny toast for all Sheldon's fans
                toast.setView(won);
                toast.show();
                resetValues();
            }

        }
        else
        {
            //To handle far away and side ways hop
            Toast.makeText(MainActivity.this, "Sorry , can't hop side ways or far away field", Toast.LENGTH_SHORT).show();
        }
    }

    //this is a function that will decrease the health of the ball over time, so using Timer class i have implemented it
    void hp_counter()
    {
        T=new Timer();
            T.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //decrement the HP by delay of a second
                            if(hp_value>0) {
                                redball.setText(hp_value + "");
                                hp_value--;
                            }
                            //you have lost and the game will reset
                            else if(hp_value<=0)
                            {
                                //Here's a funny toast for all Sheldon's fans
                                toast.setView(lost);
                                toast.show();
                                //
                                resetValues();
                            }
                        }
                    });
                }
            }, 1000, 1000);

    }

    //user has lost the game and the game will now reset will all its initial values
    void resetValues()
    {
        hp_value=50;

        redball.setText(hp_value+"");
        gameGridAdapter = new GameGridAdapter(MainActivity.this, R.layout.row_grid_view, gridArray);
        gridView.setAdapter(gameGridAdapter);

        row_curent = 0;
        column_current = 0;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(100, 100);
        lp.setMargins((int) 47 + 8, (int)  9 / 10, 0, 0);
        redball.setLayoutParams(lp);

        T.cancel();
        hp_counter();
    }

}
