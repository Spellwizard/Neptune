//Arraylists are used to track some lists of objects that are dynamically changing
import java.awt.*;
import java.util.ArrayList;

//Random is used to randomly vary some numbers to help vary the landscape
import java.util.Random;

public class Chunk extends SolidObject{

    public Chunk(double posX, double posY, int objWidth, int objHeight, Color objColour) {
        super(posX, posY, objWidth, objHeight, objColour);
    }

    /**
     * Working on adding Chunks and therefore auto/ endless generation to the game
     *
     * Step 1 will be to move the tiles from the Map Class to this one and get the map to use the Chunks Function to draw
     * Step 2 Remove / delete chunks X distance from the view position
     * Step 3 ???
     * Step 4 Profit!!
     */


    /**
     * Draw a 'super tile' tiles:
     * use tiles as a single value and draw X tiles wide and X tiles tall
     * then within each 'super tile' draw a single color
     *
     *
     * @param SuperWidth - the width of the 'super tile'
     * @param SuperHeight - the height of the 'super tile'
     * @param Color_R the integer value to set the Red color
     * @param variance_R the Red variance (please not on first making, please make sure variance + Color_R <=255..)
     * @param Color_G the integer value to set the Green color
     * @param variance_G the Green variance (please not on first making, please make sure variance + Color_R <=255..)
     * @param Color_B the integer value to set the Green color
     * @param variance_B the Green variance (please not on first making, please make sure variance + Color_R <=255..)
     *
     * assumptions made: superwidth + superheight are above 0 and are odd numbers
     *                   and the rest are =>0
     *
     *                   additionally assuming the board is even
     */


    //private void loopSuperTile(int SuperWidth, int SuperHeight, int Color_R, int variance_R, int Color_G, int variance_G, int Color_B, int variance_B)
    /**{

        SolidObject defaultTile = new SolidObject(0,0,tileWidth,tileHeight, Color.black);

        FileReader file = new FileReader("Buildings\\Ocean\\settings.txt");
        file.setFileFolder("Buildings\\Ocean\\");

        OverridingValuesClass.OverrideSolidObject(defaultTile,file);


     // Loop through each super tile and within each super tile loop through to set colors and images appropriately


        for( int Row =0; Row<tileList.size(); Row+=SuperWidth){

            for(int Col = 0; Col<tileList.get(Row).size(); Col+= SuperHeight){

                Random r = new Random();

                Color c = new Color(
                        Color_R + (r.nextInt(variance_R)),
                        Color_G + (r.nextInt(variance_G)),
                        Color_B + (r.nextInt(variance_B))
                );

                int rowMax = Row+SuperWidth;

                int colMax = Col+SuperHeight;

                for (int R = Row; R < rowMax; R++) {

                    for (int C = Col; C < colMax; C++) {

                        tileList.get(R).get(C).setObjColour(c);


                        int adjustedinternalRow = rowMax-R;
                        int adjustedinternalCol = colMax - C;

                        int MidSuperTileWidth = (SuperWidth+1)/2;
                        if(MidSuperTileWidth !=(int)MidSuperTileWidth){
                            MidSuperTileWidth=(int) MidSuperTileWidth;
                            MidSuperTileWidth--;
                        }

                        int MidSuperTileHeight = (SuperHeight+1)/2;
                        if(MidSuperTileHeight !=(int)MidSuperTileHeight){
                            MidSuperTileHeight=(int) MidSuperTileHeight;
                            MidSuperTileHeight--;
                        }

                        int Random = r.nextInt(100);


                        if(Random>25) {

                            if (adjustedinternalRow == MidSuperTileWidth) {
                                tileList.get(R).get(C).setUp_Image(defaultTile.getDown_Image());
                            } else if (adjustedinternalCol == MidSuperTileHeight) {
                                tileList.get(R).get(C).setUp_Image(defaultTile.getDown_Image());
                            }
                            else
                            {
                                if(Random>50){
                                    tileList.get(R).get(C).setUp_Image(defaultTile.getDown_Image());
                                }
                            }

                        }

                    }

                }

            }

        }

    }

        */


}
