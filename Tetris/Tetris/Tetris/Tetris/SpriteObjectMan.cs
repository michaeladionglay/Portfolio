using System;

namespace Tetris
{
    static class SOM  // SpriteObjecManager i.e. SOM
    {
        static public void drawStrings(GameStats stats)
        {
            int levels = stats.getLevelNum();
            int lines = stats.getLineCount();
            int score = stats.getScore();

            SpriteFont LevelLabel = new SpriteFont("Level " + levels, 280, 300);
            SpriteFont LineslLabel = new SpriteFont("Lines " + lines, 280, 275);
            SpriteFont ScoreLabel = new SpriteFont("Score " + score, 280, 250);

            LevelLabel.Draw();
            LineslLabel.Draw();
            ScoreLabel.Draw();
        }

        static public void drawInternal(int xPos, int yPos, DrawColor.Shade inColor)
        {
            // This is draw in painted order
            // Draw the color big box first, then the inside..
            Azul.SpriteSolidBox smallBlock = new Azul.SpriteSolidBox(new Azul.Rect(xPos, yPos, Constants.BOX_SIZE - 4, Constants.BOX_SIZE - 4),
                                                                     DrawColor.getColor(inColor));
            smallBlock.Update();

            Azul.SpriteSolidBox bigBlock = new Azul.SpriteSolidBox(new Azul.Rect(xPos, yPos, Constants.BOX_SIZE, Constants.BOX_SIZE),
                                                                   DrawColor.getColor(DrawColor.Shade.COLOR_GREY));
            bigBlock.Update();

            // Draw
            bigBlock.Render();
            smallBlock.Render();
        }

        public static void drawBox(int xPos, int yPos, DrawColor.Shade inColor)
        {
            // This is draw in painted order
            // Draw the color big box first, then the inside.
            int x = (xPos + 1) * Constants.BOX_SIZE + Constants.BOX_SIZE_HALF;
            int y = (yPos + 1) * Constants.BOX_SIZE + Constants.BOX_SIZE_HALF;

            drawInternal(x, y, inColor);
        }

        static public void drawPreviewWindow(int xPos, int yPos, int sizeX, int sizeY, DrawColor.Shade inColor, DrawColor.Shade outColor)
        {
            // This is draw in painted order
            // Draw the color big box first, then the inside..

            Azul.SpriteSolidBox smallBlock = new Azul.SpriteSolidBox(new Azul.Rect(xPos, yPos, sizeX - 4, sizeY - 4),
                                                                     DrawColor.getColor(inColor));
            smallBlock.Update();

            Azul.SpriteSolidBox bigBlock = new Azul.SpriteSolidBox(new Azul.Rect(xPos, yPos, sizeX, sizeY),
                                                                   DrawColor.getColor(DrawColor.Shade.COLOR_GREY));
            bigBlock.Update();

            // draw
            bigBlock.Render();
            smallBlock.Render();
        }

        static public void drawBackground()
        {
            int i;

            // Draw the bottom Bar
            int start_x = Constants.BOX_SIZE_HALF;

            for (i = 0; i < 12; i++)
            {
                drawInternal(start_x + i * Constants.BOX_SIZE, Constants.BOX_SIZE_HALF, DrawColor.Shade.COLOR_DK_GREY);
            }

            // Draw the left and right bar
            start_x = 11 * Constants.BOX_SIZE + Constants.BOX_SIZE_HALF;

            for (i = 0; i < 31; i++)
            {
                drawInternal(start_x, Constants.BOX_SIZE_HALF + i * Constants.BOX_SIZE, DrawColor.Shade.COLOR_DK_GREY);
                drawInternal(Constants.BOX_SIZE_HALF, Constants.BOX_SIZE_HALF + i * Constants.BOX_SIZE, DrawColor.Shade.COLOR_DK_GREY);
            }

            // preview window
            drawPreviewWindow((Constants.PREVIEW_WINDOW_X + 1) * Constants.BOX_SIZE + Constants.BOX_SIZE_HALF,
                               (Constants.PREVIEW_WINDOW_Y + 1) * Constants.BOX_SIZE + Constants.BOX_SIZE_HALF,
                               9 * Constants.BOX_SIZE, 7 * Constants.BOX_SIZE,
                               DrawColor.Shade.COLOR_BACKGROUND_CUSTOM,
                               DrawColor.Shade.COLOR_DK_GREY);
        }
    }
}
