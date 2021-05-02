
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class BasePopulateLists {

    /**
     KeyBoard Inputs: buttonUP, buttonDown, buttonLeft, buttonRight ,buttonFire, buttonAltFire
     Player Values: Height, Width, VSpeed (and sets the default), HSpeed (and sets the default), health, name (entirely for role play)
     Player reference values: FIRECOOLDOWN, BOMBCOOLDOWN, DefaultProjectileHeight, DefaultProjectileWidth
     * @param count - the amount of players to be added
     */
    protected static Player basePopulatePlayers(int count, Map gameMap){

        int x = gameMap.getMapWidth()/2;
        int y = gameMap.getMapHeight()/2;

        String name = "PLAYER";

        //Default KeyBoard values
        int defaultUp = 0;
        int defaultDown = 0;
        int defaultLeft =0;
        int defaultRight = 0;


        //Default Plane values
        int width = 200;
        int height = 200;
        int VSpeed = 1;
        int HSpeed = 4;
        int health = 3;

        Player player = new Player(
                x,y,width,height,HSpeed,VSpeed,defaultUp,defaultDown,defaultLeft,defaultRight, health,health);

        FileReader file = new FileReader("Players Model\\Player_John\\playersettings.txt");
        file.setFileFolder("Players Model\\Player_John\\");

        OverridingValuesClass.OverridePlayer(player,file);


        player.setObjColour(new Color(16, 177, 194));


        return player;
    }




}
