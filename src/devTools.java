import java.awt.*;
import java.util.ArrayList;

public class devTools {

    protected static boolean isDebug= true;

    /**
     *
     * @param gg - the 2d graphics window to draw on
     * @param gameMap - the Map to use to adjust as needed
     */
    protected static void drawScoreBoard(Graphics2D gg
            ,
                                         Map gameMap,
                                         Player self
            ){
            //  SCOREBOARD

            gg.setColor(Color.black);

            int posy = 35;

            gg.setFont(new Font("TimesRoman", Font.PLAIN, 13));

            posy+=25;

            if(self!=null){

                    String line = "Player: "+getVars_Player(self, gameMap, false,
                            false, false, false, false,
                            false, false,false, false, true);


                    posy+=250;

                gg.setColor(Color.black);
                    gg.drawString(line
                            , 25, (50+posy));

                    posy+=15;



                /*
                 * PROJECTILES
                 */
                if (self.getProjectileList() != null)
                    for (RotatingMovingObject john : self.getProjectileList()) {
                        line = "Projectiles: " + john.getRotation()+", "+ devTools.getVars_MovingObject(john, gameMap, true, false, false,
                                false, false, false, false, false);
                        gg.drawString(line
                                , 50, 50 + posy);
                        posy += 15;
                    }




                }
            }


    /**
     * Given obj and the various booleans, return a string with a complete list of the desired values with indiactors of each:
     * eg: if coords is true then the string is added with 'Coords: (X,Y)'
     * @param obj - The object that the values are pulled from
     * @param maps - the map to compensate values for the map
     * @param coords - the actual x,y coordinates
     * @param compCoords - the coordinates in comparision to the window
     * @param size - the width & height of the object
     * @param color - the colour of the object
     * @param images - the up,down, left, right and if they are null
     * @return a string of the values that were set as true
     */
    protected static String getVars_SolidObject(SolidObject obj,Map maps, boolean coords,boolean compCoords,
                                         boolean size, boolean color, boolean images){
        String answer = "";

        if(coords){
            answer+=" Coords: ("+obj.getActualPosX()+", "+obj.getActualPosY()+")";
        }
        if(compCoords&&maps!=null){
            answer+=" Compensated Coords: ("+
                    (obj.getPosX()+maps.getViewX())
                    +", "+
                    (obj.getPosY()+maps.getViewY())
                    +")";
        }
        if(size){
            answer+=" W: "+obj.getObjWidth()+ " H: "+obj.getObjHeight();
        }
        if(color){
            answer+=" Colour: "+obj.getObjColour();
        }
        if(images){
            answer+=
            " Images - Up: "+ (obj.getUp_Image()!=null)+
                    " Down: "+(obj.getDown_Image()!=null)+
                    " Left: "+(obj.getL_Image()!=null)+
                    " Right: "+(obj.getR_Image()!=null)
            ;
        }

        return answer;
    }

    /**
     * Given obj and the various booleans, return a string with a complete list of the desired values with indiactors of each:
     * eg: if coords is true then the string is added with 'Coords: (X,Y)'
     * @param obj - The object that the values are pulled from
     * @param maps - the map to compensate values for the map
     * @param coords - the actual x,y coordinates
     * @param compCoords - the coordinates in comparision to the window
     * @param size - the width & height of the object
     * @param color - the colour of the object
     * @param images - the up,down, left, right and if they are null
     * @param defaultSpeeds -
     * @param Speeds -
     * @param lastCoords -
     * @return String of the desired values
     */
    protected static String getVars_MovingObject(
            MovingObject obj, Map maps, boolean coords, boolean compCoords, boolean size, boolean color, boolean images
            , boolean defaultSpeeds, boolean Speeds, boolean lastCoords
    ){
        String answer = getVars_SolidObject(obj,maps,coords,compCoords,size,color,images);

        if(defaultSpeeds){
            answer+=" Default VSpeed: "+obj.getDefaultVSpeed()+" Default HSpeed: "+obj.getDefaultHSpeed();
        }
        if(Speeds){
            answer+=" VSpeed: "+obj.getObjVSpeed()+" HSpeed: "+obj.getObjHSpeed();
        }


        return answer;
    }

    protected static String getVars_RotatingMovingObject(RotatingMovingObject obj, Map maps, boolean coords, boolean compCoords, boolean size, boolean color, boolean images
            , boolean defaultSpeeds, boolean Speeds, boolean lastCoords, boolean rotation_speed){
        String answer = devTools.getVars_MovingObject(obj,maps,coords,compCoords,size,color,images,defaultSpeeds,Speeds, lastCoords);

        if(rotation_speed)      answer+=" Rotation: "+obj.getRotation() +
                " Speed: "+ obj.getSpeed().getCurrent_Position();


        return answer;
    }

    /**
     * Given obj and the various booleans, return a string with a complete list of the desired values with indiactors of each:
     * eg: if coords is true then the string is added with 'Coords: (X,Y)'
     * @param obj - The object that the values are pulled from
     * @param maps - the map to compensate values for the map
     * @param coords - the actual x,y coordinates
     * @param compCoords - the coordinates in comparision to the window
     * @param size - the width & height of the object
     * @param color - the colour of the object
     * @param images - the up,down, left, right and if they are null
     * @param defaultSpeeds -
     * @param Speeds -
     * @param lastCoords -
     * @return String of the desired values
     */
    protected static String getVars_Player(Player obj, Map maps, boolean coords,
                                           boolean compCoords, boolean size, boolean color, boolean images
            , boolean defaultSpeeds, boolean Speeds, boolean lastCoords, boolean health, boolean rotation
    ){

        String answer = devTools.getVars_RotatingMovingObject(obj,maps,coords,compCoords,size,color,images,defaultSpeeds,Speeds, lastCoords, rotation);

        if(health)answer+=" Health: "+obj.getHealth();



        return answer;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }
}
