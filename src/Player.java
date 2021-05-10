import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class Player extends RotatingMovingObject {

    //the keyboard value associated with each of the following movment / input types
    private int buttonUp; //UP
    private int buttonDown; //DOWN
    private int buttonLeft; //LEFT
    private int buttonRight; //RIGHT
    private int buttonSprint = 16;//SPRINT

    private int buttonFire_Left = 81;//fire weapons on the left side

    //Scoreboard to track the score that the player object has achieved :)
    private int player_score;

    private int MAXHEALTH;


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


    /**
     * KeyBoard Inputs: buttonUP, buttonDown, buttonLeft, buttonRight ,buttonFire, buttonAltFire
     * Player Values: Height, Width, VSpeed (and sets the default), HSpeed (and sets the default), health, name (entirely for role play)
     *  Player reference values: FIRECOOLDOWN, BOMBCOOLDOWN, DefaultProjectileHeight, DefaultProjectileWidth
     *
     *  and will safely loop through and override if any such values are found
     *
     *  This looping allows for easy changes to the numbers of players without having to add additional code but
     *             means that to set a value it will look for buttonUp = 'Player_' + (the position, yes from 0)+'buttonUp'
     *             so as a whole it might set the 3rd players buttonAltFire = 'Player_2_buttonAltFire'
     */
    protected static void OverrideAllPlayerValues(Player johndoe, Map maps) {

        FileReader file = new FileReader("Players Model\\Player_0\\playersettings.txt");
        String temp = "";
        String players_folder = "Players Model\\Player_";
        String fileName = "playersettings.txt";
        String type = "Player";

        file.setFileName("Players Model\\Player_0\\playersettings.txt");

        file.setFileFolder("Players Model\\Player_0\\");

        System.out.println("OverridingPlayer: "+file.getFileName());

        johndoe = BasePopulateLists.basePopulatePlayers(1, maps);


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
     * @param pointy
     */
    private void calcPlayerFire(Point pointy, Map map){


    } //end of function


    /**
     *
     * @param gg
     * @param maps
     */
    protected static void drawPlayer(   Graphics2D gg,      Player a,       Map maps) {


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
        a.RevisedBorderTest(a, false, maps);



    }



    /**
     * Jump Function to prevent unsupported jumps
     * ie: jumping off air
     *
     * This function will ascertain if the player is either
     * 1. on the map floor
     * 2. on an object
     *
     * and only then will perform the jump command if one or both of the above instances are met
     */
    private void movePlayerUp(Map gameMap){

        //now a proper jump may occur
        this.setObjVSpeed(
                -abs(this.getDefaultVSpeed()
                ));


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

    protected void calcPlayerPressedKey(KeyEvent e, Map gameMap){

        int key = e.getKeyCode();

            //UP Key
            if (this.getButtonUp() == key) {

                //call the jump fuction which factors in a number of points and decides if jumping is appropriate
                movePlayerUp(gameMap);


            } else //DOWN Key
                if (this.getButtonDown() == key) {
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

                            projectile_1.setRotation(this.getRotation()+90);
                            projectile_2.setRotation(this.getRotation()+90);
                            projectile_3.setRotation(this.getRotation()+90);

                            projectile_1.setSpeed(
                                    new CoolDownBar(10, 10,1,1,true));

                            projectile_2.setSpeed(projectile_1.getSpeed());

                            projectile_3.setSpeed(projectile_1.getSpeed());


                            this.getProjectileList().add(projectile_1);
                            this.getProjectileList().add(projectile_2);
                            this.getProjectileList().add(projectile_3);

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

}
