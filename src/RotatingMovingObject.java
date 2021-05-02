import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class RotatingMovingObject extends MovingObject{


    private int rotation =0;

    private CoolDownBar Speed;

    public RotatingMovingObject(int posX, int posY, int objWidth, int objHeight, int defaultHSpeed, int defaultVSpeed) {
        super(posX, posY, objWidth, objHeight, defaultHSpeed, defaultVSpeed);

        Speed = new CoolDownBar( Math.abs(defaultHSpeed)+Math.abs(defaultVSpeed), 0, 1, 1,true);
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

        //default to the regular drawobj if the desired image is null

        if(getUp_Image()!=null){

            drawRotatingObject_outlines(gg, maps);

            int imageX = Math.round(super.getPosX()-maps.getViewX());
            int imageY = Math.round(super.getPosY()- maps.getViewY());

            /**
             * I do whatever I want

            AffineTransform old = gg.getTransform();

            int centerofObject_X = (maps.getViewX()-this.getPosX());
            int centerofObject_Y = (maps.getViewY()-this.getPosY());



            gg.rotate(          Math.toRadians(safetyRotation_number(180-rotation))  ,
                    centerofObject_X   , centerofObject_Y );


            //reset le rotation tu est olde transformation
            gg.setTransform(old);

             */


            gg.drawImage(
                    modified_rotateImageByDegrees(super.getUp_Image(),

                            safetyRotation_number(-rotation)

                                    , gg, super.getObjWidth(), super.getObjHeight()),
                    imageX,
                    imageY,
                    super.getObjWidth(), super.getObjHeight(),null);

        }

    }

    public BufferedImage modified_rotateImageByDegrees(BufferedImage img, double angle, Graphics2D g2d, int width, int height) {

        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0,w,h, null);
        /**
        g2d.setColor(Color.RED);
        g2d.drawRect(0, 0, newWidth - 1, newHeight - 1);
         */

        g2d.dispose();

        return rotated;
    }

    /**
     * Yes.. ha ha.. used stackoverflow
     * https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java
     * @param img
     * @param angle
     * @param g2d
     * @param width
     * @param height
     * @return
     */
    public BufferedImage rotateImageByDegrees(BufferedImage img, double angle, Graphics2D g2d, int width, int height) {

        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);

        g2d.drawImage(img,0,0,width,height,null);

        g2d.setColor(Color.RED);
        g2d.drawRect(0, 0, newWidth - 1, newHeight - 1);
        g2d.dispose();

        return rotated;
    }

    /**
     * Create a useful graphic to help display the current rotation point
     * @param gg
     * @param maps
     */
    private void drawRotatingObject_outlines(Graphics2D gg, Map maps){

        gg.setColor(new Color(17, 14, 14, 52));

        gg.fillOval(
                getPosX()-maps.getViewX(),
                getPosY()- maps.getViewY(),
                super.getObjWidth(), super.getObjHeight());


        gg.setColor(new Color(255,255,255, 82));

        int whiteOval_X = ( getPosX()+ (super.getObjWidth()/4))     -   maps.getViewX();
        int whiteOval_Y = (getPosY() + (super.getObjHeight()/4))      -   maps.getViewY();

        whiteOval_X+= (super.getObjHSpeed()*5);
        whiteOval_Y+= (super.getObjVSpeed()*5) ;

        gg.fillOval(whiteOval_X, whiteOval_Y, super.getObjWidth()/2, super.getObjHeight()/2);

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

    private int safetyRotation_number(int number){
        int result = number;

        if(number < 0){
            result = 360 + number;
        }

        if(number >=360){
            result = 360-number;
        }

        return result;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;


        safetyRotation_number();

    }


    public CoolDownBar getSpeed() {
        return Speed;
    }

    public void setSpeed(CoolDownBar speed) {
        Speed = speed;
    }

}
