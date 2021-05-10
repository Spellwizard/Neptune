//The swing and awt are used as the graphics classes that make a lot of the coding way easier b/c it is built by someone else
import javax.swing.*;
//why reinvent the wheel when the car needs to be built?
import java.awt.*;

//Java.awt.event.* is used to track bother keyboard and mouse inputs when the user has the window selected
import java.awt.event.*;

public class MainSecond {

        public static JFrame frame = new JFrame("Neptune");

        protected GameCanvas program;

        public MainSecond() {

            Container c = frame.getContentPane();

            frame.setLocationRelativeTo(null);
            program = new GameCanvas();
            frame.add(program);
            frame.setVisible(true);

        }

    }

    class GameCanvas extends JComponent {

        //Its the object that the player is represented
        private Player PlayerZero;

        //The GameMap is used as the object with all things Map Related: where the view of the map is, the size of the map, drawing any/ all background images
        private Map world_Map;


        /** GAME CANVAS
         *
         */
        protected GameCanvas() {

            //Set the size of the grid tiles
            int tile_width = 40;
            //For ease of change I keep the height and width as
            // different objects but for now but I just set them to the same value
            int tile_height = tile_width;


            //By multiplying the desired tiles by the respective size it ensures proper fitting.
            int map_width = tile_width *150;
            int map_height = tile_width *75;

            //Initialize the object used to track the fundamentals: gravity, grid sizes, the position and size of the players view of the overall map
            world_Map = new Map(map_width, map_height, tile_width, tile_height);

            //Init the Player object
            PlayerZero = BasePopulateLists.basePopulatePlayers(1, world_Map);


            firstTimeinitialization();


        }

        /**
         * The InputTracker is used to track keyboard actions as both listed under the developer commands and the
         * various commands of the players in the player lists
         *
         *
         */
        KeyListener InputTracker = new KeyListener() {

            public void keyPressed(KeyEvent e) {
                PlayerZero.calcPlayerPressedKey(e, world_Map);

            }
            public void keyTyped(KeyEvent e){
            }

            public void keyReleased(KeyEvent e) {

                Player.calcPlayerReleasedInput(e,PlayerZero);
            }

        };

        private void firstTimeinitialization() {

            //use prebuilt values, make players and put them into the frogList arrayList

            //make sure that the window will actually listen for inputs
            initListeners();

            Thread animationThread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        repaint();
                        try {
                            Thread.sleep(10);
                        } catch (Exception ex) {
                        }
                    }
                }
            });
            animationThread.start();
        }

        public void initListeners() {
            //Gotta actually inialize the keyboard tracker to track all those key presses
            this.addKeyListener(InputTracker);
            this.setFocusable(true);
        }


        /**
         * Although called by other functions, this is the function that the gome runs out of and loops through to simulate time in the game
         * @param g
         */
        public void paintComponent(Graphics g) {

                    //Cast the Graphics object g into a 2D object, this allows for 2D optimization / design
                    Graphics2D gg = (Graphics2D) g;

                    //update the map
                    world_Map.CycleUpdate( PlayerZero, gg,this.getWidth(), this.getHeight());



                    //PLAYER
                    Player.drawPlayer(gg, PlayerZero, world_Map);


                    //need to update the dev info so that we get the various variables I need on the fly to debug / test
                    devTools.drawScoreBoard(gg,world_Map, PlayerZero  );


                }

    }
