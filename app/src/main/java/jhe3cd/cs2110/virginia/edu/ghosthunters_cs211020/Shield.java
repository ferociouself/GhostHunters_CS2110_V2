package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.graphics.BitmapFactory;

/**
 * Created by Xiaowei on 15/4/19.
 */
public class Shield extends Item
{
    public Shield(int duration, int fileID, int xPosition, int yPosition, int xMax, int yMax,
                 int hitBoxWidth, int hitBoxHeight, MainActivity main) {
        super ("Shield", duration, fileID, xPosition, yPosition, xMax, yMax, hitBoxWidth, hitBoxHeight, main);

        bmp = BitmapFactory.decodeResource(main.context.getResources(), R.drawable.Shield);

    }

    public void activate()
    {
        super.randomlyGenerate();
    }

    public void terminate()
    {

    }

    public void update() {

    }
}
