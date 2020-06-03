using System;
using System.Diagnostics;

namespace Tetris
{
    class Tetris : Azul.Game
    {
        // Audio: ---------------------------------------
        /*IrrKlang.ISoundEngine AudioEngine = null;

        IrrKlang.ISound music = null;
        public float vol_delta = 0.005f;

        IrrKlang.ISoundSource srcShoot = null;
        IrrKlang.ISound sndShoot = null;
        */
        // Font: ----------------------------------------
        Azul.Texture pFont;

        // Demo: ----------------------------------------
        Azul.Texture pText;
        Azul.Sprite pRedBird;
        GameStats stats;
        int count = 0;
        bool gameover = false;
        bool startgame = false;
        bool paused = false;
        bool muted = false;

        // Speed of the falling piece:
        int fallDelay = Constants.START_SPEED;

        //testing adding the active piece to the grid
        Shape active_piece;
        Shape future_piece;
 /*     int only_one_run = 0;
        Shape next;

        //these are just for testing the drawing; they're not in the actual grid
        T t = new T(5, 29);
        T turned = new T(8, 27);
        Line line = new Line(5, 24);
        L1 l1 = new L1(5, 21);
        L2 l2 = new L2(5, 17);
        Z1 z1 = new Z1(5, 13);
        Z2 z2 = new Z2(5, 9);
        Square s = new Square(5, 5);
 */

        //random number generator
        Random rand;

        //bool if the system is ready for a new input
        bool isReady = true;

        //variable to contain the last key pressed
        Azul.AZUL_KEY lastKey;

        //variable for input counting
        int inputCount = 0;

        Music soundEngine = new Music();
        DateTime timelaunched = DateTime.Now;


        //-----------------------------------------------------------------------------
        // Game::Initialize()
        //		Allows the engine to perform any initialization it needs to before 
        //      starting to run.  This is where it can query for any required services 
        //      and load any non-graphic related content. 
        //-----------------------------------------------------------------------------


        public Shape generateRandomShape()
        {
            int s = rand.Next(1, 8);
            Shape shape;

            switch (s)
            {
                case 1:
                    //                    shape = new L1(5, Constants.GAME_MAX_Y);
                    shape = new L1(Constants.PREVIEW_WINDOW_X, Constants.PREVIEW_WINDOW_Y);
                    break;
                case 2:
                    shape = new L2(Constants.PREVIEW_WINDOW_X, Constants.PREVIEW_WINDOW_Y);
                    break;
                case 3:
                    shape = new Line(Constants.PREVIEW_WINDOW_X, Constants.PREVIEW_WINDOW_Y);
                    break;
                case 4:
                    shape = new Square(Constants.PREVIEW_WINDOW_X, Constants.PREVIEW_WINDOW_Y);
                    break;
                case 5:
                    shape = new T(Constants.PREVIEW_WINDOW_X, Constants.PREVIEW_WINDOW_Y);
                    break;
                case 6:
                    shape = new Z1(Constants.PREVIEW_WINDOW_X, Constants.PREVIEW_WINDOW_Y);
                    break;
                case 7:
                    shape = new Z2(Constants.PREVIEW_WINDOW_X, Constants.PREVIEW_WINDOW_Y);
                    break;
                // default to Z2 just cause
                default:
                    shape = new Z2(Constants.PREVIEW_WINDOW_X, Constants.PREVIEW_WINDOW_Y);
                    break;

            }

            //for testing
            //shape = new Z2(6, 20);

            return shape;

        }


        public override void Initialize()
        {
            // Game Window Device setup
            this.SetWindowName("Tetris");
            this.SetWidthHeight(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
            this.SetClearColor(0.4f, 0.4f, 0.8f, 1.0f);

            //initialize the random number generator
            rand = new Random();


            Grid.InitializeGrid();
            ZeroOneGrid.initializeGrid();

        }

        //-----------------------------------------------------------------------------
        // Game::LoadContent()
        //		Allows you to load all content needed for your engine,
        //	    such as objects, graphics, etc.
        //-----------------------------------------------------------------------------
        public override void LoadContent()
        {
            //---------------------------------------------------------------------------------------------------------
            // Audio
            //---------------------------------------------------------------------------------------------------------
            /*
            // Create the Audio Engine
            AudioEngine = new IrrKlang.ISoundEngine();

            // Play a sound file
            music = AudioEngine.Play2D("theme.wav", true);
            music.Volume = 0.2f;

            // Resident loads
            srcShoot = AudioEngine.AddSoundSourceFromFile("shoot.wav");
            sndShoot = AudioEngine.Play2D(srcShoot, false, false, false);
            sndShoot.Stop();
            */
            
            soundEngine.themeSong();
            soundEngine.init();
            //---------------------------------------------------------------------------------------------------------
            // Setup Font
            //---------------------------------------------------------------------------------------------------------

            // Font - texture
            pFont = new Azul.Texture("consolas20pt.tga");
            Debug.Assert(pFont != null);

            GlyphMan.AddXml("Consolas20pt.xml", pFont);

            //---------------------------------------------------------------------------------------------------------
            // Load the Textures
            //---------------------------------------------------------------------------------------------------------

            // Red bird texture
            pText = new Azul.Texture("unsorted.tga");
            Debug.Assert(pText != null);

            //---------------------------------------------------------------------------------------------------------
            // Create Sprites
            //---------------------------------------------------------------------------------------------------------

            pRedBird = new Azul.Sprite(pText, new Azul.Rect(903.0f, 797.0f, 46.0f, 46.0f), new Azul.Rect(300.0f, 100.0f, 30.0f, 30.0f));
            Debug.Assert(pRedBird != null);

            //---------------------------------------------------------------------------------------------------------
            // Demo variables
            //---------------------------------------------------------------------------------------------------------

            stats = new GameStats();

            if (startgame)
            {
                active_piece = generateRandomShape();
                active_piece.setToPlay();
                future_piece = generateRandomShape();
            }
        }
        
        //-----------------------------------------------------------------------------
        // Game::Update()
        //      Called once per frame, update data, tranformations, etc
        //      Use this function to control process order
        //      Input, AI, Physics, Animation, and Graphics
        //-----------------------------------------------------------------------------

        public void FutureSetToActiveThenGenerateNew()
        {
            if (gameover) return;
            else
            {
                active_piece = future_piece;
                active_piece.setToPlay();
                future_piece = generateRandomShape();
            }
        }

        public void Pause()
        {
            Console.WriteLine("Game Paused.\nPress 'c' to Continue or Press 'q' to Exit.");
            paused = true;
            isReady = true;
        }




        public void Exit()
        {
            Console.WriteLine("Exiting Tetris.");
            Debug.WriteLine("Exited Gracefully");
            this.Quit();
            Environment.Exit(0);
        }

        public void CheckGameOver()
        {
            int j = Constants.GAME_MAX_Y - 1;
            for (int i = 0; i < Constants.GAME_MAX_X; i++)
            {
                Box tmp1 = Grid.boxes[i, j];
               //testing  Console.WriteLine("CheckGO: " + i + " " + j);
                if (tmp1 != null)
                {
                    
                    gameover = true;
                    isReady = true;
                    CallGameOver();
                }
            }
        }

        public void CallGameOver()
        {
            Console.WriteLine("GameOver");
            soundEngine.gameOver();
        }

        public void CheckBottomCollision()
        {
            CheckGameOver();
            if (gameover) return;
            if (active_piece.didHitBin()) //|| active_piece.didHit())
            {
                soundEngine.fastPlace();
                for (int i = 0; i < 4; i++)
                {
                    Grid.addToGrid(active_piece.boxes[i].x, active_piece.boxes[i].y, active_piece.boxes[i]);
                    ZeroOneGrid.zeroGrid[active_piece.boxes[i].y, active_piece.boxes[i].x] = 1;
                }
                // Check for any completed rows:
                int rowsCollapsed = collapse();
                stats.updateStats(rowsCollapsed);
                // Adjusts the game speed based on the current level, capped at MAX_SPEED:
                fallDelay = Math.Max(Constants.START_SPEED - (stats.getLevelNum()*2), Constants.MAX_SPEED);
                //active_piece = generateRandomShape();
                FutureSetToActiveThenGenerateNew();

            }
            else if (active_piece.didHit())
            {
                soundEngine.fastPlace();
                for (int i = 0; i < 4; i++)
                {
                    Grid.addToGrid(active_piece.boxes[i].x, active_piece.boxes[i].y, active_piece.boxes[i]);
                    ZeroOneGrid.zeroGrid[active_piece.boxes[i].y, active_piece.boxes[i].x] = 1;
                }
                int rowsCollapsed = collapse();
                stats.updateStats(rowsCollapsed);
                fallDelay = Math.Max(Constants.START_SPEED - (stats.getLevelNum()*2), Constants.MAX_SPEED);

                //active_piece = generateRandomShape();
                FutureSetToActiveThenGenerateNew(); 
            }

        }

        public bool getKeyState(Azul.AZUL_KEY key)
        {
            bool res = false;
            if (isReady)
            {
                if (isReady && Azul.Input.GetKeyState(key))
                {
                    res = Azul.Input.GetKeyState(key);
                    isReady = false;
                }
                else if (!(lastKey == key) && Azul.Input.GetKeyState(key))
                {
                    res = Azul.Input.GetKeyState(key);
                    isReady = false;
                }

                lastKey = key;
            }
            return res;
        }

        public override void Update()
        {
            if (getKeyState(Azul.AZUL_KEY.KEY_P))
            {
                Pause();
            }

            if (paused && getKeyState(Azul.AZUL_KEY.KEY_Q))
            {
                Exit();
            }

            if (!muted && getKeyState(Azul.AZUL_KEY.KEY_M))
            {
                soundEngine.getAE().SoundVolume = 0;
                //soundEngine.getAE().SetAllSoundsPaused(true);
                //soundEngine.sndRotate.Volume = 0;
                muted = true;

            }

            if (muted && getKeyState(Azul.AZUL_KEY.KEY_N))
            {
                soundEngine.getAE().SoundVolume = 0.2f;
                //soundEngine.getAE().SetAllSoundsPaused(false);
                //soundEngine.sndRotate.Volume = 0.2f;
                muted = false;
            }


            if (paused && getKeyState(Azul.AZUL_KEY.KEY_C))
            {
                paused = false;
            }

            if (!startgame && getKeyState(Azul.AZUL_KEY.KEY_SPACE) && count == 0)
            {
                Console.WriteLine(timelaunched);
                active_piece = generateRandomShape();
                active_piece.setToPlay();
                future_piece = generateRandomShape();
                startgame = true;
                soundEngine.normalPlace();
                isReady = false;
            }
            if (!startgame || paused) return;

            // Snd update - Need to be called once a frame
            if (gameover && getKeyState(Azul.AZUL_KEY.KEY_SPACE))
            {
                UnLoadContent();
            }
            if (gameover && getKeyState(Azul.AZUL_KEY.KEY_SPACE) != true) return;

            if (paused && getKeyState(Azul.AZUL_KEY.KEY_ESCAPE)) Exit();

            soundEngine.getAE().Update();


            CheckGameOver();

            // Was 60, we can't have magic numbers though, so we're extracting it to a variable.
            if (count == fallDelay)
            {
                CheckGameOver();
                active_piece.tryFall();
                CheckBottomCollision();
                count = 0;
            }

            else if (count % 7 == 0)
            {
                if (getKeyState(Azul.AZUL_KEY.KEY_ARROW_DOWN))
                {
                    CheckGameOver();
                    active_piece.tryFall();
                    CheckBottomCollision();
                }

                if (startgame && getKeyState(Azul.AZUL_KEY.KEY_SPACE) && (DateTime.Now > timelaunched.AddSeconds(1)))
                {
                    //soundEngine.fastPlace();
                    while (active_piece.didHitBin() != true && active_piece.didHit() != true)
                    {
                        CheckGameOver();
                        active_piece.tryFall();
                    }
                    CheckBottomCollision();
                }

                else if (getKeyState(Azul.AZUL_KEY.KEY_ARROW_RIGHT))
                {

                    if (active_piece.getRightmostPoint() < Constants.GAME_MAX_X)
                    {
                        active_piece.moveRight();
                    }

                }
                else if (getKeyState(Azul.AZUL_KEY.KEY_ARROW_LEFT))
                {
                    if (active_piece.getLeftmostPoint() > Constants.GAME_MIN_X)
                    {
                        active_piece.moveLeft();
                    }
                }
                else if (getKeyState(Azul.AZUL_KEY.KEY_ARROW_UP))
                {
                    soundEngine.rotateSound();
                    active_piece.cycleOrientation();
                }

            }
            //game runs at 60 FPS this counter makes it so that input is only checked twice a second
            if (inputCount % 30 == 0)
            {
                isReady = true;
                inputCount = 0;
            }
            count++;
            inputCount++;
        }

        //-----------------------------------------------------------------------------
        // Game::Draw()
        //		This function is called once per frame
        //	    Use this for draw graphics to the screen.
        //      Only do rendering here
        //-----------------------------------------------------------------------------
        public override void Draw()
        {
            // Draw sprite with texture
            //pRedBird.Render();

            if (gameover)
            {
                SOM.drawBackground();
                SOM.drawStrings(stats);
                active_piece.draw();
                future_piece.draw();
                printGrid();
                SOM.drawGameOver(stats);
                stats = new GameStats();
                return;
            }
            // Update background
            SOM.drawBackground();
            SOM.drawStrings(stats);


            if (!startgame)
            {
                SOM.drawStartTetris();
                SOM.drawHighScores(stats);
            }

            if (startgame)
            {
                active_piece.draw();
                future_piece.draw();
            }

            if (paused && startgame)
            {
                SOM.drawPaused();
                //soundEngine.getAE().SetAllSoundsPaused(true);
            }

            if (!paused && startgame)
            {
                if (!muted)
                {
                    SOM.drawMenu();
                }
                if (muted)
                {
                    SOM.drawMutedMenu();
                }
            
                //soundEngine.getAE().SetAllSoundsPaused(false);
            }
            printGrid();


            //test drawing all shapes
            //if statement to make sure this only happens once, for testing purposes. Remove later to do stuff every frame
            /*if (only_one_run == 0)
			{
				t.draw(t.orientation);
				line.draw(line.orientation);
				l1.draw(l1.orientation);
				l2.draw(l2.orientation);
				z1.draw(z1.orientation);
				z2.draw(z2.orientation);
				s.draw(s.orientation);

				only_one_run++;


				t.erase(t.orientation);
				line.erase(line.orientation);
				l1.erase(l1.orientation);
				l2.erase(l2.orientation);
				z1.erase(z1.orientation);
				z2.erase(z2.orientation);
				s.erase(s.orientation);

			}*/


            //test drawing a turned T, if staement is so that it only does it once
            /*if (turned.orientation == Shape.Orientation.ORIENT_0)
			{
				turned.turn();
			}
			turned.draw(turned.orientation);

            */

            /*

            // Draw one box, demo at position 1,1
            SOM.drawBox(1, 1, DrawColor.Shade.COLOR_LT_GREEN);

            // Test play field
            SOM.drawBox(Constants.GAME_MIN_X, Constants.GAME_MIN_Y, DrawColor.Shade.COLOR_ORANGE);
            SOM.drawBox(Constants.GAME_MAX_X, Constants.GAME_MIN_Y, DrawColor.Shade.COLOR_YELLOW);
            SOM.drawBox(Constants.GAME_MIN_X, Constants.GAME_MAX_Y, DrawColor.Shade.COLOR_RED);
            SOM.drawBox(Constants.GAME_MAX_X, Constants.GAME_MAX_Y, DrawColor.Shade.COLOR_BLUE);
			*/

        }

        //-----------------------------------------------------------------------------
        // Game::UnLoadContent()
        //       unload content (resources loaded above)
        //       unload all content that was loaded before the Engine Loop started
        //-----------------------------------------------------------------------------
        public override void UnLoadContent()
        {
            gameover = false;
            Initialize();

        }
        //for testing, not something we should use in our final implementation
        public void printGrid()
        {

            foreach (Box b in Grid.boxes)
            {
                if (b != null)
                    b.draw();
            }

        }

        /// <summary>
        /// Looks for any rows to collapse in the grid each time a shape is placed. 
        /// </summary>
        /// <returns>
        /// Number of rows that were collapsed (0-4)
        /// </returns>
        private int collapse()
        {
            // Init a list of possible complete rows;
            int[] completeRows = { -1, -1, -1, -1 };
            int numRowsComplete = 0;

            bool rowIsFull;
            // Top down searching for complete rows:
            for (int j = Constants.GAME_MAX_Y; j >= 0; j--)
            {
                // Iterate the row
                rowIsFull = true;
                for (int i = 0; i < Constants.GAME_MAX_X + 1; i++)
                {
                    if (Grid.boxes[i, j] == null)
                    {
                        rowIsFull = false;
                        break;
                    }
                }

                if (rowIsFull)
                {
                    // Add row to list and increment:
                    completeRows[numRowsComplete] = j;
                    ++numRowsComplete;
                }
            }
            // Now we have the rows that are potentially complete.
            // Check if any are complete:
            if (completeRows[0] == -1)
            {
                // Nothing to do, exit.
                return 0;
            }

            // First pass through of the list: trigger animation:
            //for(int i = 0; completeRows[i] != -1 && i < 4; i++)
            //{

            //// TODO: find method to trigger an animation for this, otherwise just play the sound.
            //}
            // Play the complete line sound:
            soundEngine.completeLine();

            // Shift the rows downwards:
            for (int i = 0; completeRows[i] != -1 && i < 4; i++)
            {
                clearAndShiftRow(completeRows[i]);
                if (i >= 3)
                {
                    break;
                }
            }
            // Redraw the game:
            printGrid();
            // Return the number of rows collapsed:
            return numRowsComplete;
        }

        private void clearAndShiftRow(int yIndex)
        {
            // Set the given row to null:
            for(int i = 0; i < Constants.GAME_MAX_X + 1; i++)
            {
                // Both the boxes and colliders:
                Grid.boxes[i, yIndex] = null;
                // Code doesn't operate properly and causes a noticeable pause in gameplay.
                // Revise and fix.
                //Box box = new Box(i, yIndex, DrawColor.Shade.COLOR_LT_BLUE);
                //box.draw();
                //System.Threading.Thread.Sleep(50);
                ZeroOneGrid.zeroGrid[yIndex, i] = 0; // NOTE: Zero One is 30,10 and Grid is 10,30

            }

            // Shift all pieces above down one row:
            for(int j = yIndex; j < Constants.GAME_MAX_Y; j++)
            {
                for(int i = 0; i < Constants.GAME_MAX_X + 1; i++)
                {
                    Box tmp1 = Grid.boxes[i, j + 1];
                    if (tmp1 != null)
                    {
                        tmp1.y--;
                        Grid.boxes[i, j] = tmp1;
                    }
                    else
                    {
                        Grid.boxes[i, j] = null;
                    }
                    // Update collider list:
                    ZeroOneGrid.zeroGrid[j, i] = ZeroOneGrid.zeroGrid[j + 1, i]; // NOTE: see above.
                }
            }
        }          
         

    }
}