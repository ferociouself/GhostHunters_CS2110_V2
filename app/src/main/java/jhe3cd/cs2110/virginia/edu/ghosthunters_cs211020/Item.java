package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;
import android.graphics.BitmapFactory;

import java.util.*;
/**
 * Created by Jimmy on 3/30/2015.
 */
public class Item extends Entity
{
    double duration;
    protected String itemID;
    protected boolean activated;

    protected TreeMap <String,Integer> itemFrequency = new TreeMap <String,Integer>();
    protected ArrayList <String> randomList = new ArrayList<String>();

    public void activate() {
        activated = true;
    }
    public void terminate() {
        activated = false;
    }

    public Item(String itemID, double duration, int fileID, int xPosition, int yPosition, int xMax, int yMax,
                int hitBoxWidth, int hitBoxHeight, MainActivity main)
    {
        super(fileID,xPosition, yPosition, xMax, yMax,hitBoxWidth,hitBoxHeight, main);
        randomlyGenerate();
        this.itemID = itemID;
        this.duration = duration;
        activated = false;

        itemFrequency.put("shield",30);
        itemFrequency.put("timeFreezer",15);
        itemFrequency.put("extraHealth",30);

        for(String id : itemFrequency.keySet())
        {
            for(int i=0; i < (int)itemFrequency.get(id); i++)
                randomList.add(id);
        }
    }

    public void randomlyGenerate()
    {
        xPosition = (int)(Math.random()*(super.xMax - 10));
        yPosition = (int)(Math.random()*(super.yMax - 10));
        // Gives the item some wiggle room to display it's icon.
    }

    public void update()
    {
        // Items just sit there and get eaten by the ball, so no updates are needed here.
    }

    public void destroyer(String destroyer)
    {

        if (destroyer.equals(main.getBall().DESTROYER_ID)){
            main.getBall().setItemStored(this);
        }
        main.entityRemove(this);
    }

    // Getter
    public String getItemID()
    {
        return itemID;
    }

    public String getRandomItemID()
    {
       int index = (int)Math.random()*randomList.size();
       return randomList.get(index);
    }

    public double getDuration() {
        return duration;
    }
}
