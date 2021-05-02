

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Map {
    /**
     * To point of this class to handle the playable area the 'map' if you will is larger than the visible area
     * Additionally this should handle providing the visible area and drawing it
     * <p>
     * The view will follow a moving object, typically a player and will compensate as the player in a given direction until the limit of the map size is met in any given direction and then the viewing port will stop in that direction
     */

    private int viewX; //The X Position of the top left of the viewing area
    private int viewY; //the Y position of the top left of the viewing area

    private int viewWidth;//the width of the viewing area, typically going to be the size of the graphical window
    private int viewHeight;//the width of the viewing area, typically going to be the size of the graphical window


    private int MapWidth;
    private int MapHeight;

    //Tiles will be used as the boxes to alow for a consistant grid system to allow for easy building
    private int tileWidth; // the width of the tiles
    private int tileHeight; //the height of the tiles


    private double Verticial_gravityConstant = 0;

    private double Horizontal_gravityConstant = 0;

    /**
     * Dynamic arraylist of all the buildings that are currently built
     */
    private ArrayList<MovingObject> backgroundTextureObjects  = new ArrayList<>();


    //2D arraylist of the tiles
    private ArrayList<ArrayList<SolidObject>> tileList = new ArrayList<>();

    /**
     *
     * @param viewX
     * @param viewY
     * @param viewWidth
     * @param viewHeight
     * @param mapWidth
     * @param mapHeight
     * @param tileWidth
     * @param tileHeight
     * @param verticial_gravityConstant
     * @param horizontal_gravityConstant
     */
    public Map(
            int viewX, int viewY, int viewWidth, int viewHeight,

               int mapWidth, int mapHeight, int tileWidth, int tileHeight,

               double verticial_gravityConstant,

               double horizontal_gravityConstant
    )
    {
        this.viewX = viewX;
        this.viewY = viewY;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        MapWidth = mapWidth;
        MapHeight = mapHeight;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        Verticial_gravityConstant = verticial_gravityConstant;
        Horizontal_gravityConstant = horizontal_gravityConstant;

        initTiles(new Color(202, 211, 203),
                new Color(156, 156, 156));


        backgroundTextureObjects = this.initBackgroundTextureObjects(25,1,1,5);

    }



    /**
     * Update the position of the map values every frame call
     * @param target
     * @param gg
     * @param width
     * @param height
     */
    protected void CycleUpdate(MovingObject target, Graphics2D gg, int width, int height){
        this.setViewHeight(height);
        this.setViewWidth(width);
        this.drawCheckerboard(gg);

        this.updatePosition(target);
    }

    private void initTiles(Color white, Color notWhite){

            //calculate the number of rows / columns by finding out how may of the size given fit
            int rows = MapWidth/tileWidth;
            int columns = MapHeight/tileHeight;

            //make a checkerboard pattern on the screen equally sized based on the window size

            int colSize = tileHeight; // calculate the actual size of the rows

            int rowSize = tileWidth; // calculates the actual size of the columns

            boolean isWhite = false; // used to change back and forth the value of squares color

            ArrayList<SolidObject> sub_list = new ArrayList<>();

            Color colour = Color.white;

            //loop through every row
            // within each row loop through every column
            //in each position make a box in the available space

            for (int r = 0; r < rows; r++) {

                sub_list = new ArrayList<>();//erase for new set

                for (int c = 0; c < columns; c++) {
                    //make a square at the current iterated position of the columns and rows of the size of those columns and rows divided by the window size
                    int X = (r * rowSize)-viewX;
                    // calculate the position of the top left of the rectangle

                    int Y = (c* colSize)-viewY;

                    //alternate what needs to be drawn where
                    if (isWhite) {
                        colour = white;
                        //flip it back to the other color
                        isWhite = !isWhite;


                    } else {
                        colour = notWhite;
                        //flip it back to the other color
                        isWhite = !isWhite;
                    }

                    sub_list.add(new SolidObject(X,Y,rowSize,colSize,colour));

                }

                tileList.add(sub_list);

                //end of a row looping through each column position

                if (columns == rows && (columns % 2) == 0) {
                    isWhite = !isWhite;
                } else if (rows < columns && (columns % 2) == 0) {
                    isWhite = !isWhite;
                }

            }

           // textureBoardWhole();

        }


        /**
         * Add some texture to the background by systemically going
         * through and randomly colouring various positions
         */
        private void textureBoardRandomly(){
            //random number object to call when i want a random number
            Random rn = new Random();
            Color c = Color.cyan;

            //Lets start by going through and randomly select values within the board size and change their colours randomly

            int number_of_texturedBlocks= (tileList.size()*tileList.get(0).size());

            //lets just loop through down based on the number of random spots to select
            for (int i=number_of_texturedBlocks;i>0;i--){

                //just select a random y and random x within the range

                int random_X = rn.nextInt(tileList.size());
                int random_Y = rn.nextInt(tileList.get(0).size());

                //Randomly set each R,G,B to random colours within bounds 0 to 255
                //thought i was funny on the spelling :)
                int random_Colour_Red= rn.nextInt(25);
                int random_Colur_Green= rn.nextInt(100)+120;
                int random_Color_Blue= 0;

                tileList.get(random_X).get(random_Y).setObjColour(
                        new Color(      random_Colour_Red       ,random_Colur_Green,        random_Color_Blue       ));

            }

        }

    /**
     * Add some texture to the background by systemically going
     * through and randomly colouring various positions
     */
    private void textureBoardWhole(){
        //random number object to call when i want a random number
        Random rn = new Random();
        Color c = Color.cyan;

        //Lets start by going through and randomly select values within the board size and change their colours randomly

        int number_of_texturedBlocks= (tileList.size()*tileList.get(0).size());

        int skyboundMax = (this.getMapHeight()/2) +     (this.getMapHeight()/3)           ;

        //lets just loop through down based on the number of random spots to select
        for (ArrayList<SolidObject> RowList: tileList){

            for(SolidObject Tile: RowList) {

                //just select a random y and random x within the range

                int random_X = rn.nextInt(tileList.size());
                int random_Y = rn.nextInt(tileList.get(0).size());

                //call the background Preset to set the colour based on the Y position

                Tile.setObjColour(
                        background_Preset_OCEANSEA(        Tile.getPosY()      ));

            }
        }

    }

    /** PRESET 1 (TEST)
     * Using a inbuilt system, return based on a y positional the background color to use within a variance
     * @param Pos_y
     * @return
     */
    private Color background_Preset_1(int Pos_y){

        Random rn = new Random();

        int Color_R=0;
        int Color_G=0;
        int Color_B=0;

        //need to convert the Y position to a usable percentage of the map:
        //EG: if the map height is 100 and y = 50 then it should be at 50% of the map;
        int percentage = (100*Pos_y)/getMapHeight();

        //get an invertaged percentaged so that can work backwards too - used to set a gradient brighter going to y = 0
        int inverted_percentage = 100 - percentage;


        //random variance to use to help tell apart otherwise same colored tiles
        int variance = 2;


        /**
         * BLUE SKY 	135, 206, 235
         * GRADIENT BLUE SKY TO WHITE
         * WHITE 255, 255, 255
         * OCEAN BLUE 	0, 0, 255
         * LIGHT GREEN 144, 238, 144
         * DARK GREEN 	34, 139, 34
         */

        //Need to if statement through each of the colors and decide which is the correct colour to set
        //then add a minor variance to keep things fun and return that as the resulting colour

        //BLUE SKY 135, 206, 235
        if(percentage < 55){
            Color_R = 50        +       rn.nextInt(     variance    +   inverted_percentage        );
            Color_G = 100        +       rn.nextInt(     variance    +   inverted_percentage        );
            Color_B = 235        +       rn.nextInt(     variance    +   inverted_percentage        );
        }

            else
                //WHITE 255, 255, 255
                    if(percentage < 56 ){
                        Color_R = 255        +       rn.nextInt(     variance            );
                        Color_G = 255        +       rn.nextInt(     variance            );
                        Color_B = 255        +       rn.nextInt(     variance            );
            }

                else
                    //OCEAN BLUE 	0, 0, 255
                        if(percentage < 65){
                            Color_R = 0         +       rn.nextInt(     variance    +   inverted_percentage        );
                            Color_G = 0         +       rn.nextInt(     variance    +   inverted_percentage        );
                            Color_B = 255         +       rn.nextInt(     variance    +   inverted_percentage        );
                }

                    else
                        //LIGHT GREEN 144, 238, 144
                        if(percentage < 73){
                            Color_R = 144        +       rn.nextInt(     variance    +   inverted_percentage        );
                            Color_G = 238        +       rn.nextInt(     variance    +   inverted_percentage        );
                            Color_B = 144        +       rn.nextInt(     variance    +   inverted_percentage        );
                        }

                        else
                            //DARK GREEN 		34, 139, 34
                            {
                                Color_R = 34        +       rn.nextInt(     variance    +   inverted_percentage        );
                                Color_G = 139        +       rn.nextInt(     variance    +   inverted_percentage        );
                                Color_B = 34        +       rn.nextInt(     variance    +   inverted_percentage        );
                            }


                           //Need to safety check no number is above 255 or below 0;
        //CHECK FOR BELOW ZEROS:

        if(Color_R < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_R = -1*Color_R;
        }

        if(Color_G < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_G = -1*Color_G;
        }

        if(Color_B < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_B = -1*Color_B;
        }



        //CHECK FOR ABOVE 255, SUBTRACT OUT THE DIFFERENCE THEN SUBTRACT THAT FROM 255: EG
        //265 : 265 - 255 = 10; 255 - 10 = 245; ANSWER WOULD BE 245
        int differnce = 0;

        if(Color_R > 255){
            differnce = Color_R - 255;
            Color_R = 255 - differnce;
        }

        if(Color_G > 255){
            differnce = Color_G - 255;
            Color_G = 255 - differnce;
        }

        if(Color_B > 255){
            differnce = Color_B - 255;
            Color_B = 255 - differnce;
        }

        return new Color(Color_R,Color_G,Color_B);
    }


    /** SPACE
     * Using a inbuilt system, return based on a y positional the background color to use within a variance
     * @param Pos_y
     * @return
     */
    private Color background_Preset_SPACE(int Pos_y){

        Random rn = new Random();

        int Color_R=0;
        int Color_G=0;
        int Color_B=0;

        //need to convert the Y position to a usable percentage of the map:
        //EG: if the map height is 100 and y = 50 then it should be at 50% of the map;
        int percentage = (100*Pos_y)/getMapHeight();

        //get an invertaged percentaged so that can work backwards too - used to set a gradient brighter going to y = 0
        int inverted_percentage = 100 - percentage;


        //random variance to use to help tell apart otherwise same colored tiles
        int variance = 1;

        /**
         * BACKGROUND PLAN:
         *
         * VARIOUS BASE COLOURS FROM BLUE (100%) TO BLACK  (0%)
         *
         *
         */

        //Need to if statement through each of the colors and decide which is the correct colour to set
        //then add a minor variance to keep things fun and return that as the resulting colour

        //TOP OF THE MAP

        //
        if(percentage < 10){
            Color_R = 0;
            Color_G = 0;
            Color_B =  10+              rn.nextInt(     variance    +   percentage        );
        }

        else if(percentage < 20){
            Color_R = 0        ;
            Color_G = 0        ;
            Color_B = 40        +       rn.nextInt(     variance    +   percentage        );
        }

        else if(percentage < 30){
            Color_R = 0        +       rn.nextInt(     variance            );
            Color_G = 0;
            Color_B =  76       +       rn.nextInt(     variance    +   percentage        );
        }

        else if(percentage < 40){
            Color_R = 0        +       rn.nextInt(     variance    +   percentage        );
            Color_G = 0  ;
            Color_B = 114        +       rn.nextInt(     variance    +   percentage        );
        }

        else if(percentage < 50){
            Color_R = 0        +       rn.nextInt(     variance    +   percentage        );
            Color_G = 0     ;
            Color_B = 152        +       rn.nextInt(     variance    +   percentage        );
        }

        else if(percentage < 60){
            Color_R = 20        +       rn.nextInt(     variance    +   percentage        );
            Color_G = 0     ;
            Color_B = 200        +       rn.nextInt(     variance    +   percentage        );
        }

        else if(percentage < 70){
            Color_R = 5        +       rn.nextInt(     variance    +   percentage        );
            Color_G = 0;
            Color_B = 255        +       rn.nextInt(     variance    +   percentage        );
        }

        else if(percentage < 80){
            Color_R = 200        +       rn.nextInt(     variance    +   inverted_percentage        );
            Color_G = 0      ;
            Color_B = 15        +       rn.nextInt(     variance    +   inverted_percentage        );
        }

        else if(percentage < 90){
            Color_R = 210        +       rn.nextInt(     variance    +   inverted_percentage        );
            Color_G = 210        +       rn.nextInt(     variance    +   inverted_percentage        );
            Color_B = 210        +       rn.nextInt(     variance    +   inverted_percentage        );
        }

        else{
            Color_R = 200        +       rn.nextInt(     variance  +inverted_percentage );
            Color_G = 230        +       rn.nextInt(     variance  +inverted_percentage );
            Color_B = 255        +       rn.nextInt(     variance  +inverted_percentage  );
        }

        //BOTTOM OF THE MAP


        //Need to safety check no number is above 255 or below 0;
        //CHECK FOR BELOW ZEROS:

        if(Color_R < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_R = -1*Color_R;
        }

        if(Color_G < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_G = -1*Color_G;
        }

        if(Color_B < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_B = -1*Color_B;
        }



        //CHECK FOR ABOVE 255, SUBTRACT OUT THE DIFFERENCE THEN SUBTRACT THAT FROM 255: EG
        //265 : 265 - 255 = 10; 255 - 10 = 245; ANSWER WOULD BE 245
        int differnce = 0;

        if(Color_R > 255){
            differnce = Color_R - 255;
            Color_R = 255 - differnce;
        }

        if(Color_G > 255){
            differnce = Color_G - 255;
            Color_G = 255 - differnce;
        }

        if(Color_B > 255){
            differnce = Color_B - 255;
            Color_B = 255 - differnce;
        }

        return new Color(Color_R,Color_G,Color_B);
    }

    /** SPACE
     * Using a inbuilt system, return based on a y positional the background color to use within a variance
     * @param Pos_y
     * @return
     */
    private Color background_Preset_OCEANSEA(int Pos_y){

        Random rn = new Random();

        int Color_R=0;
        int Color_G=0;
        int Color_B=0;

        //need to convert the Y position to a usable percentage of the map:
        //EG: if the map height is 100 and y = 50 then it should be at 50% of the map;
        int percentage = (100*Pos_y)/getMapHeight();

        //get an invertaged percentaged so that can work backwards too - used to set a gradient brighter going to y = 0
        int inverted_percentage = 100 - percentage;


        //random variance to use to help tell apart otherwise same colored tiles
        int variance = 1;

        /**
         * BACKGROUND PLAN:
         *
         * VARIOUS BASE COLOURS FROM BLUE (100%) TO BLACK  (0%)
         *
         *
         */

        //Need to if statement through each of the colors and decide which is the correct colour to set
        //then add a minor variance to keep things fun and return that as the resulting colour

        //TOP OF THE MAP


            Color_R = rn.nextInt(     20      );
            Color_G = 0;
            Color_B =                rn.nextInt(     255      );




        //BOTTOM OF THE MAP


        //Need to safety check no number is above 255 or below 0;
        //CHECK FOR BELOW ZEROS:

        if(Color_R < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_R = -1*Color_R;
        }

        if(Color_G < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_G = -1*Color_G;
        }

        if(Color_B < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_B = -1*Color_B;
        }



        //CHECK FOR ABOVE 255, SUBTRACT OUT THE DIFFERENCE THEN SUBTRACT THAT FROM 255: EG
        //265 : 265 - 255 = 10; 255 - 10 = 245; ANSWER WOULD BE 245
        int differnce = 0;

        if(Color_R > 255){
            differnce = Color_R - 255;
            Color_R = 255 - differnce;
        }

        if(Color_G > 255){
            differnce = Color_G - 255;
            Color_G = 255 - differnce;
        }

        if(Color_B > 255){
            differnce = Color_B - 255;
            Color_B = 255 - differnce;
        }

        return new Color(Color_R,Color_G,Color_B);
    }


    /** SPACE w/ Planet
     * Using a inbuilt system, return based on a y positional the background color to use within a variance
     * @param Pos_y
     * @return
     */
    private Color background_Preset_SPACEWithPlanet(int Pos_y){

        Random rn = new Random();

        int Color_R=0;
        int Color_G=0;
        int Color_B=0;

        //need to convert the Y position to a usable percentage of the map:
        //EG: if the map height is 100 and y = 50 then it should be at 50% of the map;
        int percentage = (100*Pos_y)/getMapHeight();

        //get an invertaged percentaged so that can work backwards too - used to set a gradient brighter going to y = 0
        int inverted_percentage = 100 - percentage;


        //random variance to use to help tell apart otherwise same colored tiles
        int variance = 1;

        /**
         * BACKGROUND PLAN:
         *
         * VARIOUS BASE COLOURS FROM BLUE (100%) TO BLACK  (0%)
         *
         *
         */

        //Need to if statement through each of the colors and decide which is the correct colour to set
        //then add a minor variance to keep things fun and return that as the resulting colour

        //TOP OF THE MAP

        //ONE BLACK SKY
        if(percentage < 5){
            Color_R = 0;
            Color_G = 0;
            Color_B =  10+              rn.nextInt(     variance    +   percentage        );
        }

        //TWO VERY DARK BLUE SPACE /SKY
        else if(percentage < 10){
            Color_R = 0        ;
            Color_G = 0        ;
            Color_B = 40        +       rn.nextInt(     variance    +   percentage        );
        }

        //THREE DARK ISH BLUE SKY
        else if(percentage < 15){
            Color_R = 0        +       rn.nextInt(     variance            );
            Color_G = 0;
            Color_B =  76       +       rn.nextInt(     variance    +   percentage        );
        }

        //FOUR DARK SORTA BLUE SKY
        else if(percentage < 20){
            Color_R = 0        +       rn.nextInt(     variance    +   percentage        );
            Color_G = 0  ;
            Color_B = 114        +       rn.nextInt(     variance    +   percentage        );
        }

        //FIVE SECOND BLUE BEFORE HORIZON
        else if(percentage < 25){
            Color_R = 0        +       rn.nextInt(     variance    +   percentage        );
            Color_G = 0     ;
            Color_B = 152        +       rn.nextInt(     variance    +   percentage *2       );
        }

        //SIX LAST BLUE BEFORE HORIZON
        else if(percentage < 30){
            Color_R = 20        +       rn.nextInt(     variance    +   percentage        );
            Color_G = 0     ;
            Color_B = 200        +       rn.nextInt(     variance    +   percentage        );
        }

        //SEVEN RED HORIZON
        else if(percentage < 35){
            Color_R = 5        +       rn.nextInt(     variance    +   percentage        );
            Color_G = 0;
            Color_B = 255        +       rn.nextInt(     variance    +   percentage *2       );
        }

        //EIGHT WHITE HORIZON
        else if(percentage < 40){
            Color_R = 200        +       rn.nextInt(     variance    +   inverted_percentage        );
            Color_G = 0      ;
            Color_B = 15        +       rn.nextInt(     variance    +   inverted_percentage        );
        }

        //NINE Blue Sky
        else if(percentage < 42){
            Color_R = 210        +       rn.nextInt(     variance    +   inverted_percentage        );
            Color_G = 210        +       rn.nextInt(     variance    +   inverted_percentage        );
            Color_B = 210        +       rn.nextInt(     variance    +   inverted_percentage        );
        }

        else
            //TEN
        if(percentage <44){
            Color_R = 200        +       rn.nextInt(     variance  +inverted_percentage );
            Color_G = 230        +       rn.nextInt(     variance  +inverted_percentage );
            Color_B = 255        +       rn.nextInt(     variance  +inverted_percentage  );
        }

        //BOTTOM OF THE SPACE MAP


        /**
         * PLANET >50%
         *
         * BLUE SKY 	135, 206, 235
         * GRADIENT BLUE SKY TO WHITE
         * WHITE 255, 255, 255
         * OCEAN BLUE 	0, 0, 255
         * LIGHT GREEN 144, 238, 144
         * DARK GREEN 	34, 139, 34
         */

        //Need to if statement through each of the colors and decide which is the correct colour to set
        //then add a minor variance to keep things fun and return that as the resulting colour

        //BLUE SKY 135, 206, 235
        else if(percentage < 78){
            Color_R = 50 +        rn.nextInt(        inverted_percentage        )       ;
            Color_G = 100;
            Color_B = 235        ;
        }

        else
            //WHITE 255, 255, 255
            if(percentage < 79 ){
                Color_R = 255        +       rn.nextInt(     variance            );
                Color_G = 255        +       rn.nextInt(     variance            );
                Color_B = 255        +       rn.nextInt(     variance            );
            }

            else
                //OCEAN BLUE 	0, 0, 255
                if(percentage < 80){
                    Color_R = 0;
                    Color_G = 0;
                    Color_B = 225        ;
                }

                else
                    //LIGHT GREEN 144, 238, 144
                    if(percentage < 83){
                        Color_R = 144        +       rn.nextInt(     variance    +   inverted_percentage        );
                        Color_G = 238        +       rn.nextInt(     variance    +   inverted_percentage        );
                        Color_B = 144        +       rn.nextInt(     variance    +   inverted_percentage        );
                    }

                    else
                    //DARK GREEN 		34, 139, 34
                    {
                        Color_R = 34        +       rn.nextInt(     variance    +   inverted_percentage        );
                        Color_G = 139        +       rn.nextInt(     variance    +   inverted_percentage        );
                        Color_B = 34        +       rn.nextInt(     variance    +   inverted_percentage        );
                    }


        //Need to safety check no number is above 255 or below 0;
        //CHECK FOR BELOW ZEROS:

        if(Color_R < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_R = -1*Color_R;
        }

        if(Color_G < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_G = -1*Color_G;
        }

        if(Color_B < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_B = -1*Color_B;
        }



        //CHECK FOR ABOVE 255, SUBTRACT OUT THE DIFFERENCE THEN SUBTRACT THAT FROM 255: EG
        //265 : 265 - 255 = 10; 255 - 10 = 245; ANSWER WOULD BE 245
        int differnce = 0;

        if(Color_R > 255){
            differnce = Color_R - 255;
            Color_R = 255 - differnce;
        }

        if(Color_G > 255){
            differnce = Color_G - 255;
            Color_G = 255 - differnce;
        }

        if(Color_B > 255){
            differnce = Color_B - 255;
            Color_B = 255 - differnce;
        }

        return new Color(Color_R,Color_G,Color_B);
    }


    /**
     * Determine if object is within map border and if so then return true
     *      primarily used to prevent unneccessary taxing of the system drawing unseeable objects
     * @param object
     * @return
     */
    protected boolean isWithinMapSight(SolidObject object){


        return object.isCollision(
                this.getViewX() -(object.getObjWidth())
                ,   this.getViewY() -(object.getObjHeight())     ,
                this.getMapWidth()  ,   this.getMapHeight()
        );

    }

    /**
     * Using inbuilt presets return an arrayList of background objects littered on the map to provide texture;
     * Really only the preset Space - planet should call this function but can be retooled for other things later on
     *
     * specifically
     * a stationary moon + planets
     * clouds
     *
     * buildings (windmills, barns, crops(?))
     *
     * @return a populated arraylist with all the needed info
     */
    protected ArrayList<MovingObject> initBackgroundTextureObjects(
            int amtOfObjectsToMake_Clouds,
            int amtOfObjectsToMake_Moon,
            int amtOfObjectsToMake_Mars,
            int amtOfObjectsToMake_Barn
    ) {
        ArrayList<MovingObject> returnList = new ArrayList<>();

        MovingObject defaultObject = null;

        Random rn = new Random();
        int x = 0;
        int y = 0;

        /**
         *CLOUDS
         */

        defaultObject = initBackgroundTextureObjects_DefaultObject("Fish\\");

        //now over write whatever needs to be done and loop however many times to add to the arraylist
        for (int i = 0; i < amtOfObjectsToMake_Clouds; i++) {
            MovingObject objectOverwrittenaLot = new MovingObject(0, 0, 0, 0, 0, 0);

            OverridingValuesClass.OverrideMovingObject(objectOverwrittenaLot, defaultObject);

            //random.nextInt(max - min + 1) + min
            //randomly set the x / y positions within certain values

            x = rn.nextInt(this.getMapWidth() - 1);

            y = rn.nextInt(this.getMapHeight() / 3)
                    +
                    this.getMapHeight() / 3;

            //    System.out.println("Clouds: "+i+" ("+x+","+y+")");

            objectOverwrittenaLot.setPosX(x);
            objectOverwrittenaLot.setPosY(y);

            //randomly invert the HSpeed of the cloud

            if (x % 2 == 0) objectOverwrittenaLot.setObjHSpeed(-1 * objectOverwrittenaLot.getObjHSpeed());

            returnList.add(objectOverwrittenaLot);


        }
        /**
         * END OF CLOUDS
         */


        /**
         *PLANETS
         *
         * MOON
         */

        defaultObject = initBackgroundTextureObjects_DefaultObject("Moon\\");

        for(int JUT = 0; JUT<amtOfObjectsToMake_Moon;JUT++) {
        //now over write whatever needs to be done and loop however many times to add to the arraylist

        MovingObject objectOverwrittenaLot = new MovingObject(0, 0, 0, 0, 0, 0);

        OverridingValuesClass.OverrideMovingObject(objectOverwrittenaLot, defaultObject);

        //random.nextInt(max - min + 1) + min
        //randomly set the x / y positions within certain values

        x = rn.nextInt(this.getMapWidth() - 1);

        y = rn.nextInt(10 * getTileList().get(0).get(0).getObjHeight());

        //set the object X/Y positions based on math done above
        objectOverwrittenaLot.setPosX(x);
        objectOverwrittenaLot.setPosY(y);

        objectOverwrittenaLot.setObjHSpeed(.1);

            if (x % 2 == 0) objectOverwrittenaLot.setObjHSpeed(-1 * objectOverwrittenaLot.getObjHSpeed());

        returnList.add(objectOverwrittenaLot);
    }


        /**
         * END OF MOON
         *
         * MARS
         */

        defaultObject = initBackgroundTextureObjects_DefaultObject("Mars\\");

        //now over write whatever needs to be done and loop however many times to add to the arraylist
        for(int JUT = 0; JUT<amtOfObjectsToMake_Mars;JUT++) {
            MovingObject objectOverwrittenaLot = new MovingObject(0, 0, 0, 0, 0, 0);

            OverridingValuesClass.OverrideMovingObject(objectOverwrittenaLot, defaultObject);

            //random.nextInt(max - min + 1) + min
            //randomly set the x / y positions within certain values

            x = rn.nextInt(this.getMapWidth() - 1);

            y = rn.nextInt(10 * getTileList().get(0).get(0).getObjHeight())+       (4 * getTileList().get(0).get(0).getObjHeight());

            //set the object X/Y positions based on math done above
            objectOverwrittenaLot.setPosX(x);
            objectOverwrittenaLot.setPosY(y);

            objectOverwrittenaLot.setObjHSpeed(0.2);

            if (x % 2 == 0) objectOverwrittenaLot.setObjHSpeed(-1 * objectOverwrittenaLot.getObjHSpeed());

            returnList.add(objectOverwrittenaLot);
        }
        /**
         * END OF MARS
         */

        /**
         * BARNS
         */

        defaultObject = initBackgroundTextureObjects_DefaultObject("Barn\\");

        //now over write whatever needs to be done and loop however many times to add to the arraylist
        for(int JUT = 0; JUT<amtOfObjectsToMake_Barn;JUT++) {
            MovingObject objectOverwrittenaLot =
                    new MovingObject(0, 0, 0, 0, 0, 0);

            OverridingValuesClass.OverrideMovingObject(objectOverwrittenaLot, defaultObject);

            //random.nextInt(max - min + 1) + min
            //randomly set the x / y positions within certain values

            //randomly set the X anywhere within the map bounds
            x = rn.nextInt(this.getMapWidth() -1);

            //set the y value based on the section of the map that is desired
            y = getMapHeight()+(rn.nextInt(30));

            //set the object X/Y positions based on math done above
            objectOverwrittenaLot.setPosX(x);
            objectOverwrittenaLot.setPosY(y);

            returnList.add(objectOverwrittenaLot);
        }
        /**
         * BARNS
         */



        return returnList;
    }


    protected MovingObject initBackgroundTextureObjects_DefaultObject(String fileType){

        MovingObject defaultObject = new MovingObject(0,0,0,0,0,0);

        FileReader file = new FileReader("");

        String fileName = "Settings.txt";

        //Buildings
        String fileFolder="Buildings\\";

        file = new FileReader("Buildings\\"+fileType+"Settings.txt");
        file.setFileFolder("Buildings\\"+fileType);
        //OVERRIGHT BASIC VALUES USING THE FILE READER + GET IMAGES
        OverridingValuesClass.OverrideMovingObject(defaultObject,file);

        return defaultObject;
    }


    /**MARS
     * Using a inbuilt system, return based on a y positional the background color to use within a variance
     * @param Pos_y
     * @return
     */
    private Color background_Preset_MARS(int Pos_y){

        Random rn = new Random();

        int Color_R=0;
        int Color_G=0;
        int Color_B=0;

        //need to convert the Y position to a usable percentage of the map:
        //EG: if the map height is 100 and y = 50 then it should be at 50% of the map;
        int percentage = (100*Pos_y)/getMapHeight();

        //invert the percentage so that the calculations running off it invert
        percentage =100 - percentage;

        //random variance to use to help tell apart otherwise same colored tiles
        int variance = 127;


        /**
         * MARS
         *
         * ORANGE: 	255, 165, 0
         *
         * BLACK 0, 0, 0
         */

        //Need to if statement through each of the colors and decide which is the correct colour to set
        //then add a minor variance to keep things fun and return that as the resulting colour


            Color_R = 255       +       rn.nextInt(variance     +       (percentage)      );
            Color_G = 165       +       rn.nextInt(variance     +       (percentage)      );
            Color_B = 0         +       rn.nextInt(variance     +       (percentage)      );


        //Need to safety check no number is above 255 or below 0;
        //CHECK FOR BELOW ZEROS:

        if(Color_R < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_R = -1*Color_R;
        }

        if(Color_G < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_G = -1*Color_G;
        }

        if(Color_B < 0){
            //rather than invent the wheel, just invert it since less than 0 is negative, then it will be above 0
            Color_B = -1*Color_B;
        }



        //CHECK FOR ABOVE 255, SUBTRACT OUT THE DIFFERENCE THEN SUBTRACT THAT FROM 255: EG
        //265 : 265 - 255 = 10; 255 - 10 = 245; ANSWER WOULD BE 245
        int differnce = 0;

        if(Color_R > 255){
            differnce = Color_R - 255;
            Color_R = 255 - differnce;
        }

        if(Color_G > 255){
            differnce = Color_G - 255;
            Color_G = 255 - differnce;
        }

        if(Color_B > 255){
            differnce = Color_B - 255;
            Color_B = 255 - differnce;
        }

        Color result  = new Color(Color_R,Color_G,Color_B);

        return result;
    }


        //Move an object within the borders of the Map where it may have exceeded the border
        public void resetWithinBounds(Object A){

        

        }


    /**
     * Draw a checkerboard to help determining if the program is working as intended
     * @param gg - it's what's drawn on
     */

    /**
     * The class values of the col, row are used to distiished how many rows / columns are needed
     * The  WindowLength and WindowWidth are used to disginquished how many columns may be neded to fill the vailable space
     * <p>
     * This function should only be called by the paint class as anyone else may cause an endless loop
     */
    public void drawCheckerboard(Graphics2D gg) {
        gg.setBackground(Color.red);

        for(ArrayList<SolidObject> list: tileList){

            for(SolidObject john: list){


                gg.setColor(john.getObjColour());

                john.drawobj(gg,this);

            }

        }


    }


    /**
     * Given an X,Y coordinates return a SolidObject representing the tile position (x,y,width, length, and Color Orange)
     * @param  targetx - x coordinate (assumes positive)
     * @param targety - y coordinate (assumes positive)
     */
    public SolidObject CollidedTile(int targetx, int targety) {
        SolidObject output = null;

        for(int i = 0; i< tileList.size(); i ++){

            for(int j = 0 ; j < tileList.get(i).size();j++){

                if(
                        tileList.get(i).get(j).
                                isCollision(targetx,targety,1,1)){

                    output =  tileList.get(i).get(j);
                }
            }


        }

        return output;
    }

    /**
     * Draw the vision that should be the only area on the screen
     * @param gg - graphics to draw stuff
     */
    public void drawVisionSight(Graphics2D gg){

        gg.setColor(Color.orange);

        gg.fillRect(
                viewX+100
                ,
                viewY+100,

                (viewWidth-10), (viewHeight-10)

        );

    }

    public void updatePosition(SolidObject centerObject){

        viewX = centerObject.getPosX()-(viewWidth/2);
        viewY = (centerObject.getPosY()-(viewHeight/2));

        if(
                (viewX+viewWidth)
                >MapWidth)viewX = MapWidth- viewWidth;
        if(
                (viewY + viewHeight)
                > MapHeight) viewY = MapHeight - viewHeight;


        if(viewX<0)viewX = 1;//stop the X value of the view from exceeding the overall position of the map
        if(viewY<0)viewY=1;

    }


    /**
     * @return true if the Building and the object collide tiles
     */
    public boolean calcTileCollision(SolidObject john, SolidObject sam){
        boolean isCollision = true;

        if(isAbove(sam, john)||isBelow(sam,john)){
            return false;
        }
        else if(isRight(sam,john)||isLeft(sam,john)){
            return false;
        }
        return true;
    }

    public boolean isBelow(SolidObject sam, SolidObject john){
        /**
         * this is true when 'this' object is entirely above sam
         */
        if((john.getPosY()+john.getObjHeight()<sam.getPosY())){
            return true;
        }
        return false;
    }
    public boolean isAbove(SolidObject sam, SolidObject john){
        /**
         * this is true when 'this' object is entirely below sam
         */
        if(john.getPosY()>sam.getPosY()+sam.getObjHeight()){
            return true;
        }
        return false;
    }

    public boolean isRight(SolidObject sam, SolidObject john){
        /**
         * this is true when 'this' object is entirely above sam
         */
        if((john.getPosX()+john.getObjHeight()<sam.getPosX())){
            return true;
        }
        return false;
    }
    public boolean isLeft(SolidObject sam, SolidObject john){
        /**
         * this is true when 'this' object is entirely below sam
         */
        if(john.getPosX()>sam.getPosX()+sam.getObjWidth()){
            return true;
        }
        return false;

    }

    protected void updateBoardSize(){
        int x = 0;
        int y = 0;
        for(ArrayList<SolidObject> a:tileList){

            y =tileList.indexOf(a)*tileWidth;

            for(SolidObject b: a){

                x = (a).indexOf(b)*tileHeight;


                b.setObjHeight(tileHeight);
                b.setObjWidth(tileWidth);

                b.setPosX(x);
                b.setPosY(y);

            }

        }

    }




    protected void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
        if(this.tileHeight<0)this.tileHeight=1;

        updateBoardSize();
    }

    protected void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
        if(this.tileWidth<0)this.tileWidth=1;
        updateBoardSize();
    }

    /**
     *
     * @param object - the object to determine
     *               if the object is found to be within the maps view then return true
     * @return if the obj is colliding with the view of the map
     */
    protected boolean isVisible(SolidObject object){
        boolean isVisible = false;

        isVisible=object.isCollision(this.getViewX(),this.getViewY(),this.getViewWidth(),this.getViewHeight(),this);


        return isVisible;
    }







    public int getViewX() {
        return viewX;
    }

    public void setViewX(int viewX) {
        this.viewX = viewX;
    }

    public int getViewY() {
        return viewY;
    }

    public void setViewY(int viewY) {
        this.viewY = viewY;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public int getMapWidth() {
        return MapWidth;
    }

    public void setMapWidth(int mapWidth) {
        MapWidth = mapWidth;
    }

    public int getMapHeight() {
        return MapHeight;
    }

    public void setMapHeight(int mapHeight) {
        MapHeight = mapHeight;
    }


    public ArrayList<ArrayList<SolidObject>> getTileList() {
        return tileList;
    }

    public void setTileList(ArrayList<ArrayList<SolidObject>> tileList) {
        this.tileList = tileList;
    }

    public double getVerticial_gravityConstant() {
        return Verticial_gravityConstant;
    }

    public void setVerticial_gravityConstant(double verticial_gravityConstant) {
        Verticial_gravityConstant = verticial_gravityConstant;
    }

    public double getHorizontal_gravityConstant() {
        return Horizontal_gravityConstant;
    }

    public void setHorizontal_gravityConstant(double horizontal_gravityConstant) {
        Horizontal_gravityConstant = horizontal_gravityConstant;
    }


    public ArrayList<MovingObject> getBackgroundTextureObjects() {
        return backgroundTextureObjects;
    }

    public void setBackgroundTextureObjects(ArrayList<MovingObject> backgroundTextureObjects) {
        this.backgroundTextureObjects = backgroundTextureObjects;
    }
}