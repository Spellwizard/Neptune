//AWT / Graphics are used to draw the window and the objects onto it
import java.awt.*;

//Keyevent is used to track and call functions as appropriate to adjust the player object (EG: move up, down, fire, stop, ect)
import java.awt.event.KeyEvent;

//Math.abs is just used a few times when i need to get the absolute value of a value
import static java.lang.Math.abs;

/**
 * Player
 * Player Class has 1 instance at any given time and is the object representing the player
 */
public class Player extends RotatingMovingObject {


    //the keyboard value associated with each of the following movement / input types

    //Movement
    private int buttonUp; //UP
    private int buttonDown; //DOWN
    private int buttonLeft; //LEFT
    private int buttonRight; //RIGHT
    private int buttonSprint = 16;//SPRINT

    //Combat inputs
    private int buttonFire_Right = 69;//Fire Weapons on the Right
    private int buttonFire_Left = 81;//Fire Weapons on the Left


    //Health
    private int MAXHEALTH;


    //Scoreboard to track the score that the player object has achieved :)
    private int player_score;


    /**
     *
     * @param posX - the X position of the top left of the square
     * @param posY - the Y position of the top left of the square
     * @param objWidth - the width
     * @param objHeight height
     * @param defaultHSpeed the H value that is referenced as how fast it can/ should be going
     * @param defaultVSpeed the V value that is referenced as how fast it can/ should be going
     *
     * @param buttonUp
     * @param buttonDown
     * @param buttonLeft
     * @param buttonRight
     *
     * @param health the health of the player

     */
    public Player(int posX, int posY, int objWidth, int objHeight,
                  int defaultHSpeed, int defaultVSpeed,

                  int buttonUp, int buttonDown, int buttonLeft, int buttonRight, int buttonSprint,

                  int health) {

        super(posX, posY, objWidth, objHeight, defaultHSpeed, defaultVSpeed);

        this.buttonUp = buttonUp;
        this.buttonDown = buttonDown;
        this.buttonLeft = buttonLeft;
        this.buttonRight = buttonRight;
        this.buttonSprint = buttonSprint;
        this.player_score = 0;
        super.setHealth(health);
        this.MAXHEALTH = health;

        super.setObjVSpeed(0);
        super.setObjHSpeed(0);
    }


    /**
     * Given a
     * @param gg - to draw on
     * Draw relevant UI elements
     */
    protected void drawUI(Graphics2D gg, Map maps){

        DrawStaticVariables(gg, maps);

        int barWidth = 200;
        int barX = maps.getViewWidth() - (25+barWidth);
        int barY = 30;

        //HealthBar
        drawPercentageBar(gg,maps, barX,barY,getHealth(),MAXHEALTH, "Health", new Color(255, 0, 0));

    }


    /**
     * Function used to draw the various values to be displayed and ensure they are aligned correctly
     */
    private void DrawStaticVariables(Graphics2D gg, Map maps){

        //Draw the Various moving counters eg: killcount, score, abducted
        int y = 30;
        int x = 20;

        drawVariableCounterValue(gg, "Score",
                player_score+"", y,x, 30, new Color(253, 253, 3));


        y +=45;


        if(devTools.isDebug)DevDrawStaticVariables(x,y,gg,maps);


    }


    private void DevDrawStaticVariables(int x, int y, Graphics2D gg, Map maps){

        /**
         * I had issues with the inbuilt dev tools to display information so i resorted to the player
         */
        y+=45;

        drawVariableCounterValue(gg, "",
                "("+this.getPosX()+", "+this.getPosY()+")", y,x, 30, new Color(44, 172, 10));


    }

    /**
     * Draw on the
     * @param gg - 2d Graphics that the belt is drawn on
     */
    protected void drawBelt(Graphics2D gg, Map maps){

    }


    /**
     * @param gg - graphics to draw on
     * Draw the health of the player
     */
    private void drawPercentageBar(Graphics2D gg, Map maps, int x, int y,
                                   int barcurrentValue, int barMaxValue, String VariableName, Color c){

        int height = 30;

        int bar_width = 200;


        //draw the background for the bar(for comparison to the percentage
        gg.setColor(new Color (43, 41, 41, 161));
        int increment = 5;
        gg.fillRect(    x-increment,y-increment,
                bar_width+(increment*2), height+(increment*2));

        //draw the background for the bar(for comparison to the percentage
        gg.setColor(Color. white);
        gg.fillRect(    x,y,
                bar_width, height);

        //Draw the current percentage of the bar
        gg.setColor(c);

        //Need to find the percentage that the bar is filled eg:
        //player has 3 health points and to convert that to whatever size the bar is, need to convert the current value of 2 to be 66%
        int percentage = 100*(int)Math.round(barcurrentValue) /barMaxValue;

        percentage = (percentage*bar_width)/100;

        gg.fillRect(x,y,
               percentage
                ,height);

        gg.setFont(new Font("TimesRoman", Font.PLAIN,
                (height/
                        (4)
                )
                        +height
        ));

        gg.setColor(Color.black);

        gg.drawString(VariableName, x, y+height);

    }

    /**
     *  Given a 2D graphics window and a value,
     *  Draw at a specified X, Y that value
     * @param gg
     */
    protected void drawVariableCounterValue(Graphics2D gg, String CounterValueName,
                                            String CounterValue, int y, int x
                                            ,int fontSize, Color c
    ){

        if(gg!=null && CounterValue!=null && CounterValueName!=null) {

            String description = CounterValueName + ": " + CounterValue;

            gg.setColor(Color.red);

            gg.setFont(new Font("TimesRoman", Font.PLAIN, fontSize)   );

            gg.setColor(new Color(0, 0, 0, 154));

            gg.fillRect(x, y,

                    (description.length() *gg.getFont().getSize())/2

                    , gg.getFont().getSize());

            gg.setColor(c);

            gg.drawString(
                    description
                    , x, y + gg.getFont().getSize());


        }


    }

    /**
     * If any of the players weapons are activated then fire towards the mouse target
     */
    private void calcPlayerFire(double Rotation){

        //fire the projectile(s?) to the left of the player

        //make an projectile at the center of the object and set it to -90 degrees of the current position.

        RotatingMovingObject projectile_1 =
                new RotatingMovingObject(
                        this.getPosX()+(this.getObjWidth()/2),
                        this.getPosY()+(this.getObjHeight()/2),

                        5,5,1,1
                )  ;

        RotatingMovingObject projectile_2 =
                new RotatingMovingObject(
                        projectile_1.getPosX()+(this.getObjWidth()/4),
                        projectile_1.getPosY()+ (this.getObjHeight()/4),

                        5,5,1,1
                )  ;

        RotatingMovingObject projectile_3 =
                new RotatingMovingObject(
                        projectile_1.getPosX()-(this.getObjWidth()/4),
                        projectile_1.getPosY()-(this.getObjHeight()/4),

                        5,5,1,1
                )  ;

        projectile_1.setRotation(Rotation);
        projectile_2.setRotation(Rotation);
        projectile_3.setRotation(Rotation);

        projectile_1.setSpeed(
                new CoolDownBar(10, 10,1,1,true));

        projectile_2.setSpeed(projectile_1.getSpeed());

        projectile_3.setSpeed(projectile_1.getSpeed());


        this.getProjectileList().add(projectile_1);
        this.getProjectileList().add(projectile_2);
        this.getProjectileList().add(projectile_3);


    } //end of function


    /**
     *
     * @param gg
     * @param maps
     */
    protected static void drawPlayer(   Graphics2D gg,      Player a,       Map maps) {

        //draw a circle to use temp for collision detection/ debugging
        gg.setColor(new Color(127, 115, 115, 72));

        a.drawobj(gg, maps);

        if(a.getProjectileList()!=null){

                RotatingMovingObject.drawRotatingMovingObject_Round(gg,a.getProjectileList(),maps,true,true);

                for(int i = 0; i < a.getProjectileList().size(); i++
                ){

                    a.getProjectileList().get(i).calcMovement();


                    if(a.getProjectileList().get(i).getDistanceTraveled().getCurrent_Position() >=
                            a.getProjectileList().get(i).getDistanceTraveled().getMax_Level()
                    ){
                        a.getProjectileList().remove(i);
                        i--;
                        if(i<0)i=0;
                    }


                }

        }


        //Draw the UI for the player (health bar, sprint, hover time, ect
        a.drawUI(gg, maps);

        //then if they exceeded any border then loop them around the value
       // a.RevisedBorderTest(a, false, maps);



    }

    /**
     * Given e handle any relevant action that should occur with the players
     * @param e - Keyevent
     *
     * calls ArrayList PlayerList<Plane>
     *
     * This will move the plane on a speed fom the planes default speed value
     *
     */
    protected static void calcPlayerReleasedInput(KeyEvent e, Player self){

        int key = e.getKeyCode();

        /**
         * Loop through each plane in the arraylist and handle the relevant action if any match each individuals list of actions
         */

            //UP Key
            if(self.getButtonUp()==key){

               self.getSpeed().heatByOneIncrement();

            }

            else //DOWN Key
                if(self.getButtonDown()==key){

                    self.getSpeed().coolByOneIncrement();

                }

                /**
                 * IN ORDER TO ALLOW FOR CONSTANT SIDE SCROLLING, DISABLING ON RELEASE PLAYER STOPS MOVING
                 */
                else //LEFT Key
                    if(self.getButtonLeft()==key){

                    }
                    else //RIGHT Key
                        if(self.getButtonRight()==key){


                        }

                        //SPRINTING
                        else if(self.getButtonSprint()==key){

                        }

            self.calcMovement();
    }

    /**
     * Function to call whenenever a button is pressed to calculate and adjust player if it was one of the desire key presses
     * @param e
     * @param gameMap
     */
    protected void calcPlayerPressedKey(KeyEvent e, Map gameMap){

        int key = e.getKeyCode();

            //UP Key
            if (this.getButtonUp() == key) {
                this.setObjVSpeed(
                        -abs(this.getDefaultVSpeed()
                        ));

            }

            //DOWN Key
            else if (   this.getButtonDown() == key     ) {
                    //handle moving the plane / player in the requested direction
                    //Move the player up by absolute of the default value

                    this.setObjVSpeed(
                            abs(this.getDefaultVSpeed()
                            ));

                } else //LEFT Key
                    if (this.getButtonLeft() == key) {


                        //handle moving the plane / player in the requested direction
                        //Move the player left by negativify an absolute of the default value

                       // this.setObjHSpeed(-abs(this.getDefaultHSpeed()));

                        this.setRotation(this.getRotation() + this.getSpeed().getIncrement_HeatUp()    );


                    } else //RIGHT Key
                        if (this.getButtonRight() == key) {
                            //handle moving the plane / player in the requested direction
                            //Move the player right an absolute of the default speed value

                          //  this.setObjHSpeed(abs(this.getDefaultHSpeed()));


                            this.setRotation(this.getRotation() - this.getSpeed().getIncrement_HeatUp()    );
                        }



                        //SPRINTING
                        else if(this.getButtonSprint()==key){

                            super.setObjVSpeed(0);
                            super.setObjHSpeed(0);

                        }


                        //Left Fire
                        else if(this.getButtonFire_Left()==key){

                          calcPlayerFire(this.getRotation()+90);
                        }

                        //Right Fire
                        else if (this.getButtonFire_Right()==key){

                            calcPlayerFire(this.getRotation()-90);

                        }
                        else{
                            System.out.println("Key; "+key);
                        }

    }


    public int getButtonFire_Left() {
        return buttonFire_Left;
    }

    public void setButtonFire_Left(int buttonFire_Left) {
        this.buttonFire_Left = buttonFire_Left;
    }

    protected int getButtonUp() {
        return buttonUp;
    }

    protected void setButtonUp(int buttonUp) {
        this.buttonUp = buttonUp;
    }

    protected int getButtonDown() {
        return buttonDown;
    }

    protected void setButtonDown(int buttonDown) {
        this.buttonDown = buttonDown;
    }

    protected int getButtonLeft() {
        return buttonLeft;
    }

    protected void setButtonLeft(int buttonLeft) {
        this.buttonLeft = buttonLeft;
    }

    protected int getButtonRight() {
        return buttonRight;
    }

    protected void setButtonRight(int buttonRight) {
        this.buttonRight = buttonRight;
    }


    public int getButtonSprint() {
        return buttonSprint;
    }

    public void setButtonSprint(int buttonSprint) {
        this.buttonSprint = buttonSprint;
    }


    public int getPlayer_score() {
        return player_score;
    }

    public void setPlayer_score(int player_score) {
        this.player_score = player_score;
    }


    public int getMAXHEALTH() {
        return MAXHEALTH;
    }

    public void setMAXHEALTH(int MAXHEALTH) {
        this.MAXHEALTH = MAXHEALTH;
    }


    public int getButtonFire_Right() {
        return buttonFire_Right;
    }

    public void setButtonFire_Right(int buttonFire_Right) {
        this.buttonFire_Right = buttonFire_Right;
    }
}
