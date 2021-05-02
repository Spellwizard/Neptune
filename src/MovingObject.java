
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.lang.Math.abs;


/**
 * This is a class to be used as a parent class for sub classes such as
 * for a 'player', 'projectile', and any other moving objects in the game
 */
public class MovingObject extends SolidObject{

    //default speed of the plane
    private double defaultHSpeed; //horizontally
    private double defaultVSpeed; //vertically

    //the current actual value of the object speed
    private double objHSpeed; //the HORIZONTAL OR THE LEFT / RIGHT MOTION OF THE CRAFT
    private double objVSpeed; //THE VERTICAL OR THE UP / DOWN MOTION OF THE CRAFT


    private BufferedImage currentImage = null;

    //More logical constructor only asking for some values
    //Full constructor that on construction sets all values for the player

    /**
     *
     * @param posX the top left corner of the object's x value
     * @param posY the top left corner y value
     * @param objWidth the width of the object
     * @param objHeight the height of the object
     * @param defaultHSpeed sets the current H speed and stores the original H speed as default for calling later
     * @param defaultVSpeed sets the current V speed and stores the oringinal V speed as defualt for caling later
     */
    public MovingObject(int posX, int posY, int objWidth, int objHeight,
                        int defaultHSpeed, int defaultVSpeed) {
        super(posX, posY, objWidth, objHeight, Color.BLACK);

        this.defaultHSpeed = defaultHSpeed;
        this.defaultVSpeed = defaultVSpeed;

        this.objHSpeed = defaultHSpeed;
        this.objVSpeed = defaultVSpeed;

    }

    /**
     *
     * @param posX the top left corner of the object's x value
     * @param posY the top left corner y value
     * @param objWidth the width of the object
     * @param objHeight the height of the object
     * @param defaultHSpeed sets the current H speed and stores the original H speed as default for calling later
     * @param defaultVSpeed sets the current V speed and stores the oringinal V speed as defualt for caling later
     */
    public MovingObject(double posX, double posY, int objWidth, int objHeight,
                        double defaultHSpeed, double defaultVSpeed) {
        super(posX, posY, objWidth, objHeight, Color.BLACK);



        this.defaultHSpeed = defaultHSpeed;
        this.defaultVSpeed = defaultVSpeed;

        this.objHSpeed = defaultHSpeed;
        this.objVSpeed = defaultVSpeed;

    }

    /**
     *
     * @param posX the top left corner of the object's x value
      * @param posY the top left corner y value
     */
    public MovingObject(int posX, int posY) {
        super(posX, posY, 50, 50, Color.BLACK);

        this.defaultHSpeed = 0;
        this.defaultVSpeed = 0;

        this.objHSpeed = 0;
        this.objVSpeed = 0;
    }

    /**
     *
     * @param posX the top left corner of the object's x value
     * @param posY the top left corner y value
     * @param objWidth the width of the object
     * @param objHeight the height of the object
     * @param objHSpeed and sets defaultHSpeed as sets the current H speed and stores the original H speed as default for calling later
     * @param objVSpeed and sets defaultVSpeed sets the current V speed and stores the oringinal V speed as defualt for caling later
     */
    public MovingObject(int objWidth, int objHeight, int posX, int posY, Color objColour,
                        BufferedImage r_Image, BufferedImage l_Image, BufferedImage up_Image, BufferedImage down_Image,
                        int objHSpeed, int objVSpeed, int health) {

        super(objWidth, objHeight, posX, posY, objColour, r_Image, l_Image, up_Image, down_Image);
        this.defaultHSpeed = objHSpeed;
        this.defaultVSpeed = objVSpeed;
        this.objHSpeed = objHSpeed;
        this.objVSpeed = objVSpeed;
        this.health = health;
    }

    /**based on current values of movement calculate the new values for the x,y
    and save the old x, y to the lastX, lastY respecitvely
     */
    public void calcMovement(){
        //save the x & y values in the lastX, last Y value spots

        //finally update the values of the x & y using the respective speeds in those directions
        super.setPosX(super.getActualPosX()+objHSpeed);
        super.setPosY( super.getActualPosY()+objVSpeed);


    }



    /**
     * Given an ArrayList draw those objects on the provided 2D Graphics object using pictures if available and if not then using
     * coloured boxes
     * @param gg
     * @param list
     * @param maps
     */
    protected static void drawMovingObjectList(

            Graphics2D gg, ArrayList<MovingObject> list,
            Map maps, boolean allowPartialOffMap, boolean iscalcmovment)

    {

        //safety check
        if(list!=null){

            //Loop through each object in the arraylist

            for(MovingObject a: list){

                //only draw the object if it collides with the map to prevent unnecessary clutter

                if(a!=null&&iscalcmovment){

                    MovingObject.drawMovingObject(gg,a,maps,allowPartialOffMap);

                }}
        }
    }

    protected static void drawMovingObject(Graphics2D gg, MovingObject a, Map maps , boolean allowPartialOffMap){

        //Calculate motion of the object
        a.calcMovement();

        //Call the function to adjust motion towards the player

        //loop the object around the map if exceeding the map
        if(!allowPartialOffMap)a.RevisedBorderTest(a, false, maps);
        else{
            a.RevisedBorderTest_AllowPartial(a,false,maps);
        }

        //So check if the object is overlapping with the players view then draw that object
        if(     maps.isWithinMapSight(  a   )   )
        {

            //adjust the size of the object to be the size times the tile size in the map object-
            //this is largely legacy code in another project to make buildings that would align with the tile grid
            // but it helps scale when i mess with the size of the map tiles so i don't have to adjust everything else too

            int w = a.getObjWidth();
            int h = a.getObjHeight();

            a.drawobj(gg, maps);

            a.setObjWidth(w);
            a.setObjHeight(h);

        }
    }

    /**
     * Modified RevisedBorderTest to allow the object to partially but never entirely move off the border
     * @param john the movingobject that is tested
     * @param reverseDirection if true the object will be set to reverse direction
     * @return if a border was reached
     *
     * //use the given player object and calculate and adjust if the player is going to exceed the borders and then move the player to the
     *         //oppisite side of the window
     *
     *                      --------Area: B------
     *         (0,0)                                    (x,0)
     *     I                                                     I
     *  Area: A                                                  Area: D
     *    I                                                      I
     *    I                                                      I
     *    I                                                      I
     *    I                                                      I
     *    I                                                      I
     *    I                                                      I
     *          (0,y)                                   (x,y)
     *                               --------Area: C------
     *
     *
     *
     */

    protected boolean RevisedBorderTest_AllowPartial
    (MovingObject john, boolean reverseDirection, Map gameMap)
    {

        //Test Area D to determine if a horizonal adjustment is needed

        if(inArea_D_AllowPartial(john,gameMap)){

            //System.out.println("Plane.PlaneBorderTest = inArea_D true");

            //reverse object motion if reverseDirection is true

            if(reverseDirection){
                john.setObjHSpeed(-john.getObjHSpeed());
            }

            //loop the layer to the other side of the map
            john.setPosX(
                   - (john.getObjWidth())
            );

        }

        //Test Area B to determine if a Vertical adjustment is needed

        if(inArea_B_AllowPartial(john,gameMap)){

            // System.out.println("Plane.PlaneBorderTest = inArea_B true");

            //reverse object motion if reverseDirection is true

            if(reverseDirection){
                john.setObjVSpeed(-john.getObjVSpeed());
            }

            john.setPosY(-(john.getObjHeight()));

        }

        //Test Area C to determine if a Vertical adjustment is needed

        if(inArea_C_AllowPartial(john,gameMap)){

            //System.out.println("Plane.PlaneBorderTest = inArea_C true");

            //reverse object motion if reverseDirection is true

            if(reverseDirection){
                john.setObjVSpeed(-john.getObjVSpeed());
            }

            //Now adjust the Y position
            john.setPosY(   gameMap.getMapHeight()  -
                    (john.getObjHeight())
            );

        }



        //Test Area A to determine if a horizontal adjustment is needed

        if(this.inArea_A_AllowPartial(john,gameMap)){

            // System.out.println("Plane.PlaneBorderTest = inArea_A true");

            //reverse object motion if reverseDirection is true

            if(reverseDirection){
                john.setObjHSpeed(-john.getObjHSpeed());
            }
            //now loop the player to the other end of the map
            john.setPosX(
                    ( gameMap.getMapWidth())

            );

        }



        return false;

    }


    /**
     * see Diagram in notes for PlaneBorderTest to see area noted for testing of border exceeding
     * @param john - object noted for testing
     * @return true only when john is not within the gameMap's borders
     *
     * ***Assumption made map position is 0,0 then extends only in a positive direction
     *
     * Modified  to allow the object to partially but never entirely move off the border
     */
    protected boolean inArea_A_AllowPartial(MovingObject john, Map gameMap){

        if(
                john.getActualPosX() +  (john.getObjWidth())+1
                <
                       0
            ){

            return true;

        }

        return false;
    }


    /**
     * see Diagram in notes for PlaneBorderTest to see area noted for testing of border exceeding
     * @param john - object noted for testing
     * @return true only when john is not within the gameMap's borders
     *
     *  ***Assumption made map position is 0,0 then extends only in a positive direction
     *
     *  Modified  to allow the object to partially but never entirely move off the border
     */

    protected boolean inArea_B_AllowPartial(MovingObject john, Map gameMap){


        if( (john.getActualPosY()   +  ( john.getObjHeight())   )
                <
                0
        ){

            return true;

        }


        return false;
    }



    /**
     * see Diagram in notes for PlaneBorderTest to see area noted for testing of border exceeding
     * @param john - object noted for testing
     * @param gameMap d
     * @return true only when john is not within the gameMap's borders
     *
     *  ***Assumption made map position is 0,0 then extends only in a positive direction
     *
     *  Modified  to allow the object to partially but never entirely move off the border
     */
    protected boolean inArea_C_AllowPartial(MovingObject john, Map gameMap){

        if(
                (
                        john.getActualPosY()
                                 )
                        >
                        gameMap.getMapHeight()
        ){
                     return true;
        }

        return false;
    }

    /**
     * see Diagram in notes for PlaneBorderTest to see area noted for testing of border exceeding
     * @param john - object noted for testing
     * @param gameMap d
     * @return true only when john is not within the gameMap's borders
     *
     *  ***Assumption made map position is 0,0 then extends only in a positive direction
     *
     *  Modified  to allow the object to partially but never entirely move off the border
     */
    protected boolean inArea_D_AllowPartial(MovingObject john, Map gameMap){

        if(
                (   john.getActualPosX() )
                        >
                        gameMap.getMapWidth()
        ){

            return true;

        }


        return false;
    }





    /**
     * @param john the movingobject that is tested
     * @param reverseDirection if true the object will be set to reverse direction
     * @return if a border was reached
     *
     * //use the given player object and calculate and adjust if the player is going to exceed the borders and then move the player to the
     *         //oppisite side of the window
     *
     *                      --------Area: B------
     *         (0,0)                                    (x,0)
     *     I                                                     I
     *  Area: A                                                  Area: D
     *    I                                                      I
     *    I                                                      I
     *    I                                                      I
     *    I                                                      I
     *    I                                                      I
     *    I                                                      I
     *          (0,y)                                   (x,y)
     *                               --------Area: C------
     */

    protected boolean RevisedBorderTest(MovingObject john, boolean reverseDirection, Map gameMap){

        //Test Area A to determine if a horizontal adjustment is needed

        if(this.inArea_A(john)){
            // System.out.println("Plane.PlaneBorderTest = inArea_A true");

            //reverse object motion if reverseDirection is true

            if(reverseDirection){
                john.setObjHSpeed(-john.getObjHSpeed());
            }
            //now loop the player to the other end of the map
            john.setPosX(
                    ( gameMap.getMapWidth())
                            -
                            (john.getObjWidth()+50)
            );

        }

        //Test Area D to determine if a horizonal adjustment is needed

        if(inArea_D(john,gameMap)){

            //System.out.println("Plane.PlaneBorderTest = inArea_D true");

            //reverse object motion if reverseDirection is true

            if(reverseDirection){
                john.setObjHSpeed(-john.getObjHSpeed());
            }

            //loop the layer to the other side of the map
            john.setPosX(
                    (john.getObjWidth())
            );

        }

        //Test Area B to determine if a Vertical adjustment is needed

        if(inArea_B(john)){

            // System.out.println("Plane.PlaneBorderTest = inArea_B true");

            //reverse object motion if reverseDirection is true

            if(reverseDirection){
                john.setObjVSpeed(-john.getObjVSpeed());
            }

            john.setPosY(1);

        }

        //Test Area C to determine if a Vertical adjustment is needed

        if(inArea_C(john,gameMap)){

            //System.out.println("Plane.PlaneBorderTest = inArea_C true");

            //reverse object motion if reverseDirection is true

            if(reverseDirection){
                john.setObjVSpeed(-john.getObjVSpeed());
            }

            //Now adjust the Y position
            john.setPosY(   gameMap.getMapHeight()  -
                    (john.getObjHeight())
            );

        }

        return false;

    }




    /**
     * see Diagram in notes for PlaneBorderTest to see area noted for testing of border exceeding
     * @param john - object noted for testing
     * @return true only when john is not within the gameMap's borders
     *
     * ***Assumption made map position is 0,0 then extends only in a positive direction
     */
    protected boolean inArea_A(MovingObject john){

        if(john.getActualPosX()<0){

            return true;

        }



        return false;
    }


    /**
     * see Diagram in notes for PlaneBorderTest to see area noted for testing of border exceeding
     * @param john - object noted for testing
     * @return true only when john is not within the gameMap's borders
     *
     *  ***Assumption made map position is 0,0 then extends only in a positive direction
     */

    protected boolean inArea_B(MovingObject john){


        if(john.getActualPosY()<0){

            return true;

        }


        return false;
    }



    /**
     * see Diagram in notes for PlaneBorderTest to see area noted for testing of border exceeding
     * @param john - object noted for testing
     * @param gameMap d
     * @return true only when john is not within the gameMap's borders
     *
     *  ***Assumption made map position is 0,0 then extends only in a positive direction
     */
    protected boolean inArea_C(MovingObject john, Map gameMap){

        if(
                (
                        john.getActualPosY()    +
                                (john.getObjHeight())
                        >
                        gameMap.getMapHeight())
        ){
            /**
             System.out.println("Plane.InArea_C: "+
             john.getActualPosY()+" + ("+
             john.getObjHeight()+
             " * "+
             gameMap.getTileHeight()+") > "+
             gameMap.getMapHeight()

             );
             */

            return true;

        }

        return false;
    }

    /**
     * see Diagram in notes for PlaneBorderTest to see area noted for testing of border exceeding
     * @param john - object noted for testing
     * @param gameMap d
     * @return true only when john is not within the gameMap's borders
     *
     *  ***Assumption made map position is 0,0 then extends only in a positive direction
     */
    protected boolean inArea_D(MovingObject john, Map gameMap){

        if(
                (   john.getActualPosX()    +  ( john.getObjWidth()  ))
                        >
                        gameMap.getMapWidth()
        ){

            return true;

        }


        return false;
    }



    /**
     *
     * @param map - the map
     * @param x - the object to move towards
     * @param y - the y position of the object to move towards
     *
     * @param desiredActualSpeed the desired speed hypotentuse to set the speed of the interceptor
     *
     * There is an inherent variablity due to the nature of the calculation simply looping to the max and min of the hypotenuse by scalling with 1.001
     * but it seems to work so 'why fix it if it ain't broke'?
     */
    public void moveTowards(Map map, double x, double y, double desiredActualSpeed){
        moveTowards(map,new SolidObject(x,y,1,1,Color.BLACK), desiredActualSpeed);
    }

    /**
     *
     * @param map - the map
     * @param target - the object to move towards
     *  - the movingobject that is directed towards the target
     * @param desiredActualSpeed the desired speed hypotentuse to set the speed of the interceptor
     *
     * There is an inherent variablity due to the nature of the calculation simply looping to the max and min of the hypotenuse by scalling with 1.001
     * but it seems to work so 'why fix it if it ain't broke'?
     */
    public void moveTowards(Map map,SolidObject target, double desiredActualSpeed){
        moveTowards(map,target, this, desiredActualSpeed);
    }

    /**
     *
     * @param map - the map
     * @param target - the object to move towards
     * @param interceptor - the movingobject that is directed towards the target
     * @param desiredActualSpeed the desired speed hypotentuse to set the speed of the interceptor
     *
     * There is an inherent variablity due to the nature of the calculation simply looping to the max and min of the hypotenuse by scalling with 1.001
     * but it seems to work so 'why fix it if it ain't broke'?
     */
    public static void moveTowards(Map map,SolidObject target, MovingObject interceptor, double desiredActualSpeed){

        /**
         * Basic Algrebra / Geometry here that took my an embarrassing  amt of time to solve
         *
         * what we do is use an angle theta and a speed limit (speed of weapon) to setup the projectile
         *
         * SOH CAH TOA (basic math here)
         * S= speed limit or the hypotenuse or the desiredActualSpeed
         * so that means:
         * VSpeed = sin(theta) * S
         * HSpeed = cos(theta) * S
         *
         * Theta is found by another basic equation:
         *
         * Theta = y1 - y2 / x1 - x2
         * or
         * targetY - InterceptorY / targetX - InterceptorX
         *
         */

        double YTheta = interceptor.getActualPosY() - target.getActualPosY();
        double XTheta = interceptor.getActualPosX() - target.getActualPosX();
        double Theta = YTheta / XTheta;
        Theta*=100;

        //-------------
        //
        //-------------

        interceptor.setObjHSpeed(   (Math.cos(Theta)    )   *   desiredActualSpeed  );

        interceptor.setObjVSpeed(   (Math.sin(Theta)    )  *   desiredActualSpeed   );

    }

    /**
     * @param target - the object to move towards

     * @param desiredActualSpeed the desired speed hypotentuse to set the speed of the interceptor
     *
     * There is an inherent variablity due to the nature of the calculation simply looping to the max and min of the hypotenuse by scalling with 1.001
     * but it seems to work so 'why fix it if it ain't broke'?
     */
    public void moveTowards_YOnly(SolidObject target, double desiredActualSpeed){

        /**
         * Basic Algrebra / Geometry here that took my an embarrassing  amt of time to solve
         *
         * what we do is use an angle theta and a speed limit (speed of weapon) to setup the projectile
         *
         * SOH CAH TOA (basic math here)
         * S= speed limit or the hypotenuse or the desiredActualSpeed
         * so that means:
         * VSpeed = sin(theta) * S
         * HSpeed = cos(theta) * S
         *
         * Theta is found by another basic equation:
         *
         * Theta = y1 - y2 / x1 - x2
         * or
         * targetY - InterceptorY / targetX - InterceptorX
         *
         */

        double YTheta = getActualPosY() - target.getActualPosY();
        double XTheta = getActualPosX() - target.getActualPosX();
        double Theta = YTheta / XTheta;
        Theta*=100;

        //-------------
        //
        //-------------

        setObjVSpeed(   (Math.sin(Theta)    )  *   desiredActualSpeed   );

    }

    public int getEnergy() {
        return health;
    }

    public void setEnergy(int energy) {
        this.health = energy;
    }

    private int health;

    //Basic Getter's / Setters that simply set and or get the variable


    public double getDefaultHSpeed() {
        return defaultHSpeed;
    }

    public void setDefaultHSpeed(double defaultHSpeed) {
        this.defaultHSpeed = defaultHSpeed;
    }

    public double getDefaultVSpeed() {
        return  defaultVSpeed;
    }

    public void setDefaultVSpeed(double defaultVSpeed) {
        this.defaultVSpeed = defaultVSpeed;
    }

    public double getObjHSpeed() {
        return objHSpeed;
    }

    public void setObjHSpeed(double objHSpeed) {
        this.objHSpeed = objHSpeed;
    }

    public double getObjVSpeed() {
        return objVSpeed;
    }



    /**
     *Move towards the object at the defaualt speeds of the moving object
     * @param sam - target
     *            move this abject towards the same object
     */
    protected void moveTowardsobj(SolidObject sam, Map map){
        this.setObjVSpeed(0);
        this.setObjHSpeed(0);

        //UP
        if(this.isAbove(sam)){
            this.setObjVSpeed(
                    -abs(    this.getDefaultVSpeed() )
            );
        }
        else
            //DOWN
            if(this.isBelow(sam)){
            this.setObjVSpeed(
                    abs(    this.getDefaultVSpeed() )
            );
        }
            //LEFT
        if(this.isLeft(sam)){
            this.setObjHSpeed(
                    -abs(    this.getDefaultHSpeed() )
            );
        }
        else
            //RIGHT
            if(this.isRight(sam)){
                this.setObjHSpeed(
                        abs(    this.getDefaultHSpeed() )
                );
        }
    }




    @Override
    protected void drawobj(Graphics2D gg, Map maps){

            //default to the regular drawobj if the desired image is not initalized

            if(getUp_Image()!=null && getDown_Image()!=null

                    &&getL_Image()!=null&&getR_Image()!=null){

                //draw the object moving up if the movement is upwards
                if(getObjVSpeed()<0){
                    currentImage = getUp_Image();
                    gg.drawImage(
                            super.getUp_Image(),
                            Math.round(super.getPosX()-maps.getViewX()),
                            Math.round(super.getPosY()- maps.getViewY()),
                            super.getObjWidth(), super.getObjHeight(),null);
                }
                //Draw the object downards if moving down
                else if(getObjVSpeed()>0){
                    currentImage = super.getDown_Image();
                    gg.drawImage(
                            super.getDown_Image(),
                            Math.round(super.getPosX()-maps.getViewX()),
                            Math.round(super.getPosY()- maps.getViewY()),
                            super.getObjWidth(), super.getObjHeight(),null);
                }

                //ONLY draw the image moving left/Right if the up/down motion is 0
                if(getObjVSpeed()==0){

                    //Draw the image moving right
                    if(getObjHSpeed()>0){
                        currentImage = super.getR_Image();
                        gg.drawImage(super.getR_Image(),Math.round(super.getPosX()-maps.getViewX()),
                                Math.round(super.getPosY()- maps.getViewY()),
                                super.getObjWidth(), super.getObjHeight(),null);
                    }
                    else if(getObjHSpeed()<0) //Draw the image moving left
                    {
                        currentImage = super.getL_Image();
                        gg.drawImage(super.getL_Image(), Math.round(super.getPosX() - maps.getViewX()),
                                Math.round(super.getPosY() - maps.getViewY()),
                                super.getObjWidth(), super.getObjHeight(), null);
                    }
                }

                if(getObjVSpeed()==0&&getObjHSpeed()==0){
                    if(currentImage!=null) {
                        gg.drawImage(currentImage, Math.round(super.getPosX() - maps.getViewX()),
                                Math.round(super.getPosY() - maps.getViewY()),
                                super.getObjWidth(), super.getObjHeight(), null);
                    }
                    else{
                        gg.drawImage(super.getUp_Image(), Math.round(super.getPosX() - maps.getViewX()),
                                Math.round(super.getPosY() - maps.getViewY()),
                                super.getObjWidth(), super.getObjHeight(), null);
                    }
                }

            }
            else{
                super.drawobj(gg,maps);
            }
        }

        public boolean isStationary(){
        return (abs(objVSpeed)+abs(objHSpeed))<0;
        }

    public void setObjVSpeed(double objVSpeed) {
        this.objVSpeed = objVSpeed;
    }


    /**
     * Reverse the direction of the object both VSpeed and HSpeed using the default values
     */
    public void reverseDirection(){
        if(objVSpeed>0){
            objVSpeed = -abs(defaultVSpeed);
        }
        else if(objVSpeed< 0){
            objVSpeed = abs(defaultVSpeed);
        }

        if(objHSpeed>0){
            objHSpeed = -abs(defaultHSpeed);
        }
        else if(objVSpeed< 0){
            objHSpeed = abs(defaultHSpeed);
        }

    }


    public BufferedImage getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(BufferedImage currentImage) {
        this.currentImage = currentImage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
