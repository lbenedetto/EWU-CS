package com.benedetto.lars.ashman;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.benedetto.lars.ashman.entities.Ashman;
import com.benedetto.lars.ashman.entities.Entity;
import com.benedetto.lars.ashman.entities.Ghost;

import com.benedetto.lars.ashman.enums.Direction;
import com.benedetto.lars.ashman.enums.GhostColor;

public class MazeView extends View {
    //General stuff
    private int width;
    private int height;
    //Timer stuff
    private static final int TIMER_MSEC = 50;
    private boolean mIsRunning = false;
    private Handler mHandler;
    private Runnable mTimer;
    private CakeListener cakeListener;
    //Drawing stuff
    private final Paint paint = new Paint();
    private float tileSize;
    public String[][] maze;
    private Bitmap[] bitmaps;
    private Entity[] entities;
    private int cakes = 121;
    public int level = 1;
    private int ghosts;
    private int ghostIncrementer;
    private MediaPlayer gameOver;
    private MediaPlayer gameWon;
    private Context context;
    private final Matrix transformer = new Matrix();

    public MazeView(Context context) {
        super(context);
    }

    public MazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MazeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        ghosts = sp.getInt("startGhosts", 2);
        ghostIncrementer = sp.getInt("ghostsPerLevel", 2);
        loadMaze();
        loadEntities();
        //Load bitmaps
        bitmaps = new Bitmap[7];
        bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.blank);
        bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall_cap);
        bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.wall_corner);
        bitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.wall_intersection3);
        bitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.wall_intersection4);
        bitmaps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bitmaps[6] = BitmapFactory.decodeResource(getResources(), R.drawable.cake);
        mHandler = new Handler();
        mTimer = new Runnable() {
            @Override
            public void run() {
                onTimer();
                if (mIsRunning) mHandler.postDelayed(this, TIMER_MSEC);
            }
        };
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setDither(false);
        gameOver = MediaPlayer.create(context, R.raw.game_over);
        gameWon = MediaPlayer.create(context, R.raw.game_won);
    }

    private void loadEntities() {
        entities = new Entity[ghosts + 1];
        entities[0] = new Ashman(context, this);
        for (int i = 1; i <= ghosts; i++) {
            entities[i] = new Ghost(context, GhostColor.getRandomColor(), this);
        }
    }

    private void loadMaze() {
        maze = new String[14][14];
        String mazeString = context.getString(R.string.maze);
        String[] tempMaze = mazeString.split(" ");
        int x = 0;
        int y = 0;
        for (int i = 0; i < tempMaze.length; i++) {
            if (i != 0 && i % 14 == 0) {
                x = 0;
                y++;
            }
            maze[y][x] = tempMaze[i];
            x++;
        }
    }

    public void registerCakeListener(CakeListener cakeListener) {
        this.cakeListener = cakeListener;
    }

    public void cheat() {
        for (int y = 0; y < 14; y++) {
            for (int x = 0; x < 14; x++) {
                if (y != 1 || x != 2) {
                    if (maze[y][x].equals("60")) {
                        maze[y][x] = "00";
                        decrementCakes();
                    }
                }
            }
        }
        invalidate();
    }

    public void decrementCakes() {
        cakes--;
        if (cakes == 0) {
            gameWon.start();
            Toast.makeText(context, "You WIN", Toast.LENGTH_SHORT).show();
            incrementLevel();
        }
        cakeListener.updateCakes(cakes);

    }

    private void incrementLevel() {
        ghostIncrementer = PreferenceManager.getDefaultSharedPreferences(context).getInt("ghostsPerLevel", 2);
        ghosts += ghostIncrementer;
        level++;
        loadMaze();
        loadEntities();
        updateEntities();
        cakes = 122;
        decrementCakes();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.setBackgroundColor(Color.RED);
        int tileX;
        int tileY = 0;
        float xCoord;
        float yCoord = 0;
        //Draw maze
        while (tileY < 14) {
            //Draw a new column
            tileX = 0;
            xCoord = 0;
            while (tileX < 14) {
                //The second digit is number of 90degree rotations to apply to the sprite
                String[] temp = maze[tileY][tileX].split("");
                int[] wall = {Integer.valueOf(temp[1]), Integer.valueOf(temp[2])};
                Bitmap bitmap = bitmaps[wall[0]];
                transformer.reset();
                transformer.postRotate(90f * wall[1], bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
                float scale = tileSize / bitmap.getWidth();
                transformer.postScale(scale, scale);
                transformer.postTranslate(xCoord, yCoord);
                canvas.drawBitmap(bitmap, transformer, paint);
                tileX++;
                xCoord += tileSize;
            }
            tileY++;
            yCoord += tileSize;
        }
        //Draw entities
        for (Entity e : entities) {
            e.draw(canvas, paint, transformer);
        }
    }

    public void setAshmanDirection(Direction direction) {
        entities[0].setDirection(direction);
    }

    private void onTimer() {
        for (Entity e : entities) {
            e.move();
        }
        Entity ashman = entities[0];
        Entity ghost;
        for (int i = 1; i < entities.length; i++) {
            ghost = entities[i];
            if (ashman.Xindex == ghost.Xindex && ashman.Yindex == ghost.Yindex && !ashman.INVINCIBLE) {
                Toast.makeText(context, "You LOSE", Toast.LENGTH_SHORT).show();
                gameOver.start();
                loadMaze();
                loadEntities();
                updateEntities();
                cakes = 121;
                pause();
            }
        }
        invalidate();
    }

    public void pausePlay() {
        if (mIsRunning)
            pause();
        else
            play();
    }

    private void pause() {
        mIsRunning = false;
        mHandler.removeCallbacks(mTimer);
    }

    private void play() {
        mIsRunning = true;
        mHandler.postDelayed(mTimer, TIMER_MSEC);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        int aspectRatioHeight = 1;
        int aspectRatioWidth = 1;
        int calculatedHeight = maxWidth * aspectRatioHeight / aspectRatioWidth;
        int finalWidth, finalHeight;
        if (calculatedHeight > maxHeight) {
            finalWidth = maxHeight * aspectRatioWidth / aspectRatioHeight;
            finalHeight = maxHeight;
        } else {
            finalWidth = maxWidth;
            finalHeight = calculatedHeight;
        }
        setMeasuredDimension(finalWidth, finalHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        tileSize = width / 14f;
        updateEntities();
    }

    private void updateEntities() {
        for (Entity e : entities) {
            e.setTileSize(tileSize);
            e.setHeight(height);
            e.setWidth(width);
            if (e instanceof Ashman) {
                e.setVelocity(tileSize / 4);
            } else {
                int v = level >= 2 ? 4 : 6;
                e.setVelocity(tileSize / v);
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        pause();
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.stateToSave = 1;
        ss.maze = maze;
        ss.cakes = cakes;
        ss.level = level;
        ss.ghosts = ghosts;
        ss.ghostIncrementer = ghostIncrementer;
        ss.entities = entities;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        SavedState ss = (SavedState) state;
        maze = ss.maze;
        cakes = ss.cakes;
        level = ss.level;
        ghosts = ss.ghosts;
        ghostIncrementer = ss.ghostIncrementer;
        entities = ss.entities;
        play();
    }

    /**
     * http://stackoverflow.com/questions/3542333/how-to-prevent-custom-views-from-losing-state-across-screen-orientation-changes
     * I have orientation locked though, so I don't think this actually does anything..
     */
    static class SavedState extends BaseSavedState {
        int stateToSave;
        String[][] maze;
        int cakes = 121;
        int level = 1;
        int ghosts;
        int ghostIncrementer;
        Entity[] entities;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.stateToSave);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
