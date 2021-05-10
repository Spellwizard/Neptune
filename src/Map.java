

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

    /**
     * Dynamic arraylist of all the buildings that are currently built
     */
    private ArrayList<RotatingMovingObject> backgroundTextureObjects  = new ArrayList<>();


    //2D arraylist of the tiles
    private ArrayList<ArrayList<SolidObject>> tileList = new ArrayList<>();

    /**
     * @param mapWidth
     * @param mapHeight
     * @param tileWidth
     * @param tileHeight
     */
    public Map(
               int mapWidth, int mapHeight, int tileWidth, int tileHeight

    )
    {
        MapWidth = mapWidth;
        MapHeight = mapHeight;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        DrawCheckBoard(new Color(202, 211, 203),
                new Color(156, 156, 156));

        loopSuperTile(5,5,0,5,0,5,155,100);


        backgroundTextureObjects = this.initBackgroundTextureObjects(1,1,1,5);

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

    private void DrawCheckBoard(Color white, Color notWhite){

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
        }

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
     *
     *
     */
    private void loopSuperTile(
            int SuperWidth, int SuperHeight,
            int Color_R, int variance_R,
            int Color_G, int variance_G,
            int Color_B, int variance_B
    ){

        SolidObject defaultTile = new SolidObject(0,0,tileWidth,tileHeight,Color.black);

        FileReader file = new FileReader("Buildings\\Ocean\\settings.txt");
        file.setFileFolder("Buildings\\Ocean\\");

        OverridingValuesClass.OverrideSolidObject(defaultTile,file);


        /**
         * Loop through each super tile and within each super tile loop through to set colors and images appropriately
         */

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


    private void initBackgroundTextureObjects_Clouds(ArrayList<RotatingMovingObject> returnList, int amtOfObjectsToMake_Clouds){
        /**
         *CLOUDS
         */
        Random rn = new Random();

        RotatingMovingObject defaultObject = initBackgroundTextureObjects_RotatingMovingObject("Clouds\\");

        //now over write whatever needs to be done and loop however many times to add to the arraylist
        for (int i = 0; i < amtOfObjectsToMake_Clouds; i++) {
            RotatingMovingObject objectOverwrittenaLot = new RotatingMovingObject(0, 0, 0, 0, 0, 0);

            objectOverwrittenaLot.setObjHSpeed(0.1);

            int number = rn.nextInt(100);
            if(number<25){
                objectOverwrittenaLot.setUp_Image(defaultObject.getUp_Image());
            }
            else if(number<50){
                objectOverwrittenaLot.setUp_Image(defaultObject.getDown_Image());
            }
            else if(number<75){
                objectOverwrittenaLot.setUp_Image(defaultObject.getL_Image());
            }
            else{
                objectOverwrittenaLot.setUp_Image(defaultObject.getR_Image());
            }

            //random.nextInt(max - min + 1) + min
            //randomly set the x / y positions within certain values

            int x = rn.nextInt(this.getMapWidth() - 1);

            int y = rn.nextInt(this.getMapHeight() / 3)
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
    protected ArrayList<RotatingMovingObject> initBackgroundTextureObjects(
            int amtOfObjectsToMake_Clouds,
            int amtOfObjectsToMake_Moon,
            int amtOfObjectsToMake_Mars,
            int amtOfObjectsToMake_Barn
    ) {
        ArrayList<RotatingMovingObject> returnList = new ArrayList<>();

        RotatingMovingObject defaultObject = null;

        Random rn = new Random();
        int x = 0;
        int y = 0;

        initBackgroundTextureObjects_Clouds(returnList, amtOfObjectsToMake_Clouds);



        /**
         *PLANETS
         *
         * MOON
         */

        defaultObject = initBackgroundTextureObjects_RotatingMovingObject("Moon\\");

        for(int JUT = 0; JUT<amtOfObjectsToMake_Moon;JUT++) {
        //now over write whatever needs to be done and loop however many times to add to the arraylist

        RotatingMovingObject objectOverwrittenaLot = new RotatingMovingObject(0, 0, 0, 0, 0, 0);

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

        defaultObject = initBackgroundTextureObjects_RotatingMovingObject("Mars\\");

        //now over write whatever needs to be done and loop however many times to add to the arraylist
        for(int JUT = 0; JUT<amtOfObjectsToMake_Mars;JUT++) {
            RotatingMovingObject objectOverwrittenaLot = new RotatingMovingObject(0, 0, 0, 0, 0, 0);

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

        defaultObject = initBackgroundTextureObjects_RotatingMovingObject("Barn\\");

        //now over write whatever needs to be done and loop however many times to add to the arraylist
        for(int JUT = 0; JUT<amtOfObjectsToMake_Barn;JUT++) {
            RotatingMovingObject objectOverwrittenaLot =
                    new RotatingMovingObject(0, 0, 0, 0, 0, 0);

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


    protected MovingObject initBackgroundTextureObjects_MovingObject(String fileType){

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

    protected RotatingMovingObject initBackgroundTextureObjects_RotatingMovingObject(String fileType){

        RotatingMovingObject defaultObject = new RotatingMovingObject(0,0,0,0,0,0);

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

        for(ArrayList<SolidObject> list: tileList){

            for(SolidObject john: list){

                if(isWithinMapSight(john))
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


    public ArrayList<RotatingMovingObject> getBackgroundTextureObjects() {
        return backgroundTextureObjects;
    }

    public void setBackgroundTextureObjects(ArrayList<RotatingMovingObject> backgroundTextureObjects) {
        this.backgroundTextureObjects = backgroundTextureObjects;
    }
}