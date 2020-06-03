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
        Azul.AZUL_KEY prevEnterKey = 0;
        Azul.AZUL_KEY prevSpaceKey = 0;

        //testing adding the active piece to the grid
        Shape active_piece;
        Shape future_piece;
        int only_one_run = 0;
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

        //random number generator
        Random rand;

        Music soundEngine = new Music();

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
            this.SetWindowName("Tetris Framework");
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

            active_piece = generateRandomShape();
            active_piece.setToPlay();
            future_piece = generateRandomShape();
        }

        //-----------------------------------------------------------------------------
        // Game::Update()
        //      Called once per frame, update data, tranformations, etc
        //      Use this function to control process order
        //      Input, AI, Physics, Animation, and Graphics
        //-----------------------------------------------------------------------------

        public void FutureSetToActiveThenGenerateNew()
        {
            active_piece = future_piece;
            active_piece.setToPlay();
            future_piece = generateRandomShape();
        }

        public void CheckBottomCollision()
        {
            if (active_piece.didHitBin()) //|| active_piece.didHit())
            {
                soundEngine.normalPlace();
                for (int i = 0; i < 4; i++)
                {
                    Grid.addToGrid(active_piece.boxes[i].x, active_piece.boxes[i].y, active_piece.boxes[i]);
                    ZeroOneGrid.zeroGrid[active_piece.boxes[i].y, active_piece.boxes[i].x] = 1;
                }
                //active_piece = generateRandomShape();
                FutureSetToActiveThenGenerateNew();

            }
            else if (active_piece.didHit())
            {
                soundEngine.normalPlace();
                for (int i = 0; i < 4; i++)
                {
                    Grid.addToGrid(active_piece.boxes[i].x, active_piece.boxes[i].y, active_piece.boxes[i]);
                    ZeroOneGrid.zeroGrid[active_piece.boxes[i].y, active_piece.boxes[i].x] = 1;
                }
                //active_piece = generateRandomShape();
                FutureSetToActiveThenGenerateNew();
            }
            
        }

        public override void Update()
        {
            // Snd update - Need to be called once a frame
            soundEngine.getAE().Update();

            if (count == 60)
            {
                active_piece.tryFall();
                CheckBottomCollision();
                count = 0;
            }

            else if (count % 7 == 0)
            {
                if (Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_ARROW_DOWN))
                {
                    active_piece.tryFall();
                    CheckBottomCollision();
                }

                if (Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_SPACE))
                {
                    soundEngine.fastPlace();
                    while (active_piece.didHitBin() != true && active_piece.didHit() != true)
                    {
                        active_piece.tryFall();
                    }
                    CheckBottomCollision();
                }

                else if (Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_ARROW_RIGHT))
                {

                    if (active_piece.getRightmostPoint() < Constants.GAME_MAX_X)
                    {
                        active_piece.moveRight();
                    }

                }
                else if (Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_ARROW_LEFT))
                {
                    if (active_piece.getLeftmostPoint() > Constants.GAME_MIN_X)
                    {
                        active_piece.moveLeft();
                    }
                }
                else if (Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_ARROW_UP))
                {
                    soundEngine.rotateSound();
                    active_piece.cycleOrientation();
                }
            }
            count++;

            //-----------------------------------------------------------
            // Input Test
            //-----------------------------------------------------------

            //InputTest.KeyboardTest();
            // InputTest.MouseTest();

            //-----------------------------------------------------------
            // Sound Experiments
            //-----------------------------------------------------------

            // Adjust music theme volume
            /*
			if (music.Volume > 0.30f)
            {
                vol_delta = -0.002f;
            }
            else if (music.Volume < 0.00f)
            {
                vol_delta = 0.002f;
            }
            */
            //music.Volume = 0.15f;

            //--------------------------------------------------------
            // Rotate Sprite
            //--------------------------------------------------------
            /*
            pRedBird.angle = pRedBird.angle + 0.01f;
            pRedBird.Update();
			*/
            //--------------------------------------------------------
            // Keyboard test
            //--------------------------------------------------------

            // Quick hack to have a one off call.
            // you need to release the keyboard between calls 
            /*
            if (Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_ENTER) && prevEnterKey == 0)
            {
                prevEnterKey = Azul.AZUL_KEY.KEY_ENTER;
                sndShoot = AudioEngine.Play2D(srcShoot, false, false, false);
            }
            else
            {
                if (!Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_ENTER))
                {
                    prevEnterKey = 0;
                }
            }
			*/
            //--------------------------------------------------------
            // Stats test
            //--------------------------------------------------------
            /*
            stats.setScore(stats.getScore() + 1);
            if (statsCount % 400 == 0)
            {
                stats.setLevelNum(stats.getLevelNum() + 1);
            }
            if (statsCount % 50 == 0)
            {
                stats.setLineCount(stats.getLineCount() + 1);
            }
            statsCount++;
			*/



            //****************************************************************************************************************************
            //!!!!!!!!!!!!!!!!We should really only be saving a piece to the grid once it has fallen as far as it can!!!!!!!!!!!!!!!!!!!!
            //****************************************************************************************************************************
            /*	
				//add active piece to grid
				//need to copy box data to grid for each box in active piece
				for (int i = 0; i < 4; i++)
				{
					//copy into the grid the coordinates and color of the ith box
					boxes[active_piece.boxes[i].x, active_piece.boxes[i].y].x = active_piece.boxes[i].x;
					boxes[active_piece.boxes[i].x, active_piece.boxes[i].y].y = active_piece.boxes[i].y;
					boxes[active_piece.boxes[i].x, active_piece.boxes[i].y].color = active_piece.boxes[i].color;
                boxes[active_piece.boxes[i].x, active_piece.boxes[i].y].draw();

                }
			*/

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

            // Update background
            SOM.drawBackground();
            SOM.drawStrings(stats);

            active_piece.draw();
            future_piece.draw();

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
            for(int j = Constants.GAME_MAX_Y; j > 0; j--)
            {
                // Iterate the row
                rowIsFull = true;
                for(int i = 0; i < Constants.GAME_MAX_X; i++)
                {
                    if(Grid.boxes[i,j] == null)
                    {
                        rowIsFull = false;
                        break;
                    }
                }
                
                if(rowIsFull)
                {
                    // Add row to list and increment:
                    completeRows[numRowsComplete] = j;
                    ++numRowsComplete;
                }
            }
            // Now we have the rows that are potentially complete.
            // Check if any are complete:
            if(completeRows[0] == -1)
            {
                // Nothing to do, exit.
                return 0;
            }

            // First pass through of the list: trigger animation:
            /*for(int i = 0; completeRows[i] != -1 && i < 4; i++)
            {
            // TODO: find method to trigger an animation for this, otherwise just play the sound.
            }*/
            return 0;
        }

    }
}