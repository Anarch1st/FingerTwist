package dey.saikat.fingertwist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Random;

public class TilesAdapter extends BaseAdapter {
    int dim;
    private Game mContext;
    private int[] array;
    private int[] used;
    private boolean res;
    private int toTouch;
    private TextView p1, p2;
    private int currentPlayer;
    private Random r;
    private int Max_Touch, no_of_touches;

    public TilesAdapter(Game c, int[] bool, int toTouch, TextView p1, TextView p2) {
        mContext = c;
        this.array = bool;
        this.toTouch = toTouch;
        this.p1 = p1;
        this.p2 = p2;
        currentPlayer = 1;
        r = new Random();
        res = false;
        used = new int[bool.length];
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPref", mContext.MODE_PRIVATE);
        Max_Touch = Integer.parseInt(sharedPreferences.getString("MaxTouch", ""));
        Max_Touch = (Max_Touch / 2) * 2;
        no_of_touches = 0;

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        Point size = new Point();
        d.getSize(size);
        dim = (int) (size.x / Math.sqrt(bool.length));
    }

    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public Object getItem(int position) {
        return array[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        TextView textView = new TextView(mContext);
        if (array[position] == 0) {
            textView.setBackgroundResource(R.drawable.grey_tile);//.setImageResource(R.drawable.grey_tile);
        } else if (array[position] == 1) {
            textView.setBackgroundResource(R.drawable.brown_tile);
        } else if (array[position] == 2) {
            textView.setBackgroundResource(R.drawable.yellow_tile);
        } else {
            textView.setBackgroundResource(R.drawable.red_tile);
        }

        textView.setTextSize(16.0f);
        textView.setLayoutParams(new GridView.LayoutParams(dim, dim));
        textView.setGravity(Gravity.CENTER);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int code = motionEvent.getAction() & MotionEvent.ACTION_MASK;
                if (code == MotionEvent.ACTION_DOWN) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    if (position == toTouch) {
                        if (res) {
                            return false;
                        }
                        Log.i("MyApp", "Correct Click = " + position);
                        array[position] = 1;
                        used[position] = currentPlayer;
                        int i = position;
                        while (used[i] != 0) {
                            i = r.nextInt(used.length);
                        }
                        array[i] = 2;
                        toTouch = i;
                        Log.i("MyApp", "Next Click = " + toTouch);
                        notifyDataSetChanged(view, position, parent.getChildAt(i), i);
                        currentPlayer = (currentPlayer == 1) ? 2 : 1;
                        p1.setTextColor((currentPlayer == 1) ? Color.RED : Color.BLACK);
                        p2.setTextColor((currentPlayer == 1) ? Color.BLACK : Color.RED);
                        no_of_touches++;

                        if (no_of_touches == Max_Touch) {
                            if (!res) {
                                Intent intent = new Intent(mContext, InformationDialog.class);
                                intent.putExtra("text", "It is a Tie.");
                                mContext.startActivity(intent);
                                res = true;
                                notifyDataSetChanged(view, position);
                            }
                        }
                    } else {
                        Log.i("MyApp", "Wrong Click = " + position);
                        array[position] = 3;
                        if (!res) {
                            Intent i = new Intent(mContext, InformationDialog.class);
                            i.putExtra("text", ("Wrong Tile. " + ((currentPlayer == 1) ? "Player 2" : "Player 1") + " is the winner."));
                            mContext.startActivity(i);
                            res = true;
                            notifyDataSetChanged(view, position);
                        }
                    }

                    return true;

                } else if (code == MotionEvent.ACTION_UP) {
                    if (res) {
                        return false;
                    }
                    Log.i("MyApp", "Up = " + position);
                    array[position] = 3;
                    notifyDataSetChanged(view, position);
                    if (!res) {
                        Intent i = new Intent(mContext, InformationDialog.class);
                        i.putExtra("text", ("Finger up. " + ((used[position] == 1) ? "Player 2" : "Player 1") + " is the winner."));
                        mContext.startActivity(i);
                        res = true;
                    }
                } else if (code == MotionEvent.ACTION_MOVE) {
                    if (res) {
                        return false;
                    }
                    Rect rect;
                    rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                    if (!rect.contains(view.getLeft() + (int) motionEvent.getX(), view.getTop() + (int) motionEvent.getY())) {
                        Log.i("MyApp", "Move = " + position);
                        array[position] = 3;
                        notifyDataSetChanged(view, position);
                        if (!res) {
                            Intent i = new Intent(mContext, InformationDialog.class);
                            i.putExtra("text", ("Moved from Tile. " + ((used[position] == 1) ? "Player 2" : "Player 1") + " is the winner."));
                            mContext.startActivity(i);
                            res = true;
                        }
                    }
                } else if (code == MotionEvent.ACTION_POINTER_UP) {
                    Log.i("MyApp", "Pointer Up = " + position);
                } else if (code == MotionEvent.ACTION_CANCEL) {
                    Log.i("MyApp", "Cancel = " + position);
                }

                if (res) {
                    return false;
                } else {
                    return true;
                }
            }
        });
        return textView;
    }

    public void notifyDataSetChanged(View view, int pos1, View next, int pos2) {
        TextView textView = (TextView) view;
        if (array[pos1] == 0) {
            textView.setBackgroundResource(R.drawable.grey_tile);
        } else if (array[pos1] == 1) {
            textView.setBackgroundResource(R.drawable.brown_tile);
            textView.setText(String.valueOf(currentPlayer));
        } else if (array[pos1] == 2) {
            textView.setBackgroundResource(R.drawable.yellow_tile);
        } else {
            textView.setBackgroundResource(R.drawable.red_tile);
            textView.setText(String.valueOf(currentPlayer));
        }
        textView = (TextView) next;
        if (array[pos2] == 0) {
            textView.setBackgroundResource(R.drawable.grey_tile);
        } else if (array[pos2] == 1) {
            textView.setBackgroundResource(R.drawable.brown_tile);
            textView.setText(String.valueOf(currentPlayer));
        } else if (array[pos2] == 2) {
            textView.setBackgroundResource(R.drawable.yellow_tile);
        } else {
            textView.setBackgroundResource(R.drawable.red_tile);
            textView.setText(String.valueOf(currentPlayer));
        }
    }

    public void notifyDataSetChanged(View view, int pos1) {
        TextView textView = (TextView) view;
        if (array[pos1] == 0) {
            textView.setBackgroundResource(R.drawable.grey_tile);
        } else if (array[pos1] == 1) {
            textView.setBackgroundResource(R.drawable.brown_tile);
            textView.setText(String.valueOf(currentPlayer));
        } else if (array[pos1] == 2) {
            textView.setBackgroundResource(R.drawable.yellow_tile);
        } else {
            textView.setBackgroundResource(R.drawable.red_tile);
            textView.setText(String.valueOf(currentPlayer));
        }
    }

}
