import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RotatingMovingObject extends MovingObject{


    private double rotation =0;

    private CoolDownBar Speed;


    private ArrayList<RotatingMovingObject> projectileList;


    //Simple number used to track distance traveled
    private CoolDownBar DistanceTraveled;

    /**
     *
     * @param posX
     * @param posY
     * @param objWidth
     * @param objHeight
     * @param defaultHSpeed
     * @param defaultVSpeed
     */
    public RotatingMovingObject(int posX, int posY, int objWidth, int objHeight, int defaultHSpeed, int defaultVSpeed) {
        super(posX, posY, objWidth, objHeight, defaultHSpeed, defaultVSpeed);

        Speed = new CoolDownBar(
                Math.abs(defaultHSpeed)+Math.abs(defaultVSpeed),
                0, 2.5, 2.5,true);

        projectileList = new ArrayList<RotatingMovingObject>();

        DistanceTraveled = new CoolDownBar(500, 0, 1, 1, true);
    }



    /**given a 2d Graphics graw the object in reference to viewX and viewY of the MapView
     * Eg: object 0,0 and the mapview 1,1
     * draw the object @ -1,-1
     * Additionally scale the object in comparision to the width and height of the map view to allow objects to appear
     * to get bigger / smaller as the zoom changes
     * @param gg - the Graphics 2D that the object is drawn on
     * @param maps - the Map that is used to draw the object relatively to the view X, Y positions
     *
     *
     * Draw the object rotated
     */
    @Override
    protected void drawobj(Graphics2D gg, Map maps){

        this.calcMovement();

        //default to the regular drawobj if the desired image is null

            drawRotatingObject_outlines(gg, maps);


    }


    /**
     * Given an ArrayList draw those objects on the provided 2D Graphics object using pictures if available and if not then using
     * coloured boxes
     * @param gg
     * @param list
     * @param maps
     */
    protected static void drawRotatingMovingObject_Round(

            Graphics2D gg, ArrayList<RotatingMovingObject> list,
            Map maps, boolean allowPartialOffMap, boolean iscalcmovment)

    {

        //safety check
        if(list!=null){

            //Loop through each object in the arraylist

            for(RotatingMovingObject a: list){

                //only draw the object if it collides with the map to prevent unnecessary clutter

                if(a!=null&&iscalcmovment){

                    MovingObject.drawMovingObject(gg,a,maps,allowPartialOffMap);

                }}
        }
    }

    /**
     * Create a useful graphic to help display the current rotation point
     * @param gg
     * @param maps
     */
    private void drawRotatingObject_outlines(Graphics2D gg, Map maps){

        double rotate_X =( this.getPosX()
                + this.getObjWidth()/2)
                -maps.getViewX();

        double rotate_Y = (this.getPosY()
                + this.getObjHeight()/2)
                - maps.getViewY();


        /**
         * Draw a rectangle that rotates around the center
         */
        AffineTransform old = gg.getTransform();

        AffineTransform transform = gg.getTransform();

        transform.rotate(Math.toRadians(-1*this.getRotation()),
                rotate_X,
                rotate_Y);


        gg.setTransform(transform);

        if(getUp_Image()!=null){
            super.setCurrentImage( getUp_Image());

            gg.drawImage(
                    super.getUp_Image(),
                    Math.round(super.getPosX()-maps.getViewX()),
                    Math.round(super.getPosY()- maps.getViewY()),
                    super.getObjWidth(), super.getObjHeight(),null);
        }
        else{
            super.drawobj(gg,maps);
        }

        gg.setTransform(old);

    }


    /**
     * based on current values of movement calculate the new values for the x,y
     *
     * Mother of all this took way too long but i think i figured out why my algebra is always so wierdly wrong:
     * the Y axis on the computer is inverted to the mathematics one
     *
     *
     *      So what I need to do is calibrate for that:
     *      Math goes counterclockwise to the inverted computer Axis
     *
     *              Sectors:
     *                   (-V Speed) (Math 180)
     *                      I
     *                      I
     *            IV        I           I
     *                      I
     *                      I
     *(-H Speed)------------+-----------------(+H Speed)
     *(Math 270)            I                 (Math 90)
     *                      I
     *           III        I          II
     *                      I
     *                      I
     *                   (+V Speed) (Math 0)
     *
     *OMGASDFASDF;AF THIS TOOK WAAAYYY TOO MUCH TIME TO FIGURE OUT..
     * THE JAVA CLASS MATH FUNCTION SIN USES RADIANS NOT DEGREES ASDFAFOE4BFJKJF
     */
    @Override
    public void calcMovement(){

        getDistanceTraveled().setCurrent_Position(
                getDistanceTraveled().getCurrent_Position()+(Math.abs(super.getObjHSpeed())+(Math.abs(super.getObjVSpeed())))
        );

        RotatingMovingObject_calcMovementRotation();
    }


    /**
     * based on current values of movement calculate the new values for the x,y
     *
     * Mother of all this took way too long but i think i figured out why my algebra is always so wierdly wrong:
     * the Y axis on the computer is inverted to the mathematics one
     *
     *
     *      So what I need to do is calibrate for that:
     *      Math goes counterclockwise to the inverted computer Axis
     *
     *              Sectors:
     *                   (-V Speed) (Math 180)
     *                      I
     *                      I
     *            IV        I           I
     *                      I
     *                      I
     *(-H Speed)------------+-----------------(+H Speed)
     *(Math 270)            I                 (Math 90)
     *                      I
     *           III        I          II
     *                      I
     *                      I
     *                   (+V Speed) (Math 0)
     *
     *OMGASDFASDF;AF THIS TOOK WAAAYYY TOO MUCH TIME TO FIGURE OUT..
     * THE JAVA CLASS MATH FUNCTION SIN USES RADIANS NOT DEGREES ASDFAFOE4BFJKJF
     */
    protected void RotatingMovingObject_calcMovementRotation(){

        //Side A
        super.setObjHSpeed(
                Speed.getCurrent_Position() * Math.sin(Math.toRadians(rotation))
        );

        super.setObjVSpeed(
                Speed.getCurrent_Position() * Math.cos(Math.toRadians(rotation))
        );


        //finally update the values of the x & y using the respective speeds in those directions
        super.setPosX(      super.getActualPosX()     +    super.getObjHSpeed()    );
        super.setPosY(      super.getActualPosY()    +   super.getObjVSpeed()   );



    }


    /**
     * called to ensure that the rotation number stays within bounds eg:
     * 360 > rotation >= 0
     */
    private void safetyRotation_number(){

        rotation = safetyRotation_number(rotation);

    }

    private double safetyRotation_number(double number){
        double result = number;

        if(number < 0){
            result = 360 + number;
        }

        if(number >=360){
            result = 360-number;
        }

        return result;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;


        safetyRotation_number();

    }


    public CoolDownBar getSpeed() {
        return Speed;
    }

    public void setSpeed(CoolDownBar speed) {
        Speed = speed;
    }


    public ArrayList<RotatingMovingObject> getProjectileList() {
        return projectileList;
    }

    public void setProjectileList(ArrayList<RotatingMovingObject> projectileList) {
        this.projectileList = projectileList;
    }

    public CoolDownBar getDistanceTraveled() {
        return DistanceTraveled;
    }

    public void setDistanceTraveled(CoolDownBar distanceTraveled) {
        DistanceTraveled = distanceTraveled;
    }
}
