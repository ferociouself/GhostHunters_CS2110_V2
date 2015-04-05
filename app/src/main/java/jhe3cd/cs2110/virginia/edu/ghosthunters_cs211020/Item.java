package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

/**
 * Created by Jimmy on 3/30/2015.
 */
public abstract class Item extends Entity
{
    double duration;

    public abstract void activate();
    public abstract void terminate();

    public Item(int duration, int fileID, int xPosition, int yPosition, int xMax, int yMax,
                int hitBoxWidth, int hitBoxHeight, MainActivity main)
    {
        super(fileID,xPosition, yPosition, xMax, yMax,hitBoxWidth,hitBoxHeight, main);
        this.duration = duration;
    }

    public void randomlyGenerate()
    {
        xPosition = (int)Math.random()*(super.xMax+1);
        yPosition = (int)Math.random()*(super.yMax+1);
    }

    public void update()
    {

    }

    public void destroyer()
    {

    }

    public boolean collisionDetect()
    {
      return false;
    }

}
