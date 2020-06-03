using System;
using System.Collections.Generic;

namespace Tetris
{
    static class SOM  // SpriteObjecManager i.e. SOM
    {
        static public void drawStrings(GameStats stats)
        {
            int levels = stats.getLevelNum();
            int lines = stats.getLineCount();
            int score = stats.getScore();

            SpriteFont LevelLabel = new SpriteFont("LEVEL " + levels, 280, 300);
            SpriteFont LineslLabel = new SpriteFont("LINES " + lines, 280, 275);
            SpriteFont ScoreLabel = new SpriteFont("SCORES " + score, 280, 250);

            LevelLabel.Draw();
            LineslLabel.Draw();
            ScoreLabel.Draw();
        }

        static public void drawMenu()
        {
            SpriteFont MenuLabel = new SpriteFont("Press 'p' to pause", 250, 215);
            SpriteFont MuteLabel = new SpriteFont("Press 'm' to mute", 250, 180);
            MenuLabel.Draw();
            MuteLabel.Draw();
        }

        static public void drawMutedMenu()
        {
            SpriteFont MenuLabel = new SpriteFont("Press 'p' to pause", 250, 215);
            SpriteFont MuteLabel = new SpriteFont("Press 'n' to unmute", 250, 180);
            MenuLabel.Draw();
            MuteLabel.Draw();
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

        static public void drawGameOver(GameStats stats)
        {
            drawPreviewWindow(120, 500, 200, 160, DrawColor.Shade.COLOR_DK_BLUE, DrawColor.Shade.COLOR_DK_YELLOW);
            SpriteFont GameOverLabel = new SpriteFont("Game Over ", 70, 550);
            SpriteFont RestartLabel = new SpriteFont("Press Space", 60, 500);
            SpriteFont RestartLabel1 = new SpriteFont("To Play Again", 40, 450);
            GameOverLabel.Draw();
            RestartLabel.Draw();
            RestartLabel1.Draw();

            // update top5 scores
            stats.updateTop5();

            // draw high scores after update
            drawHighScores(stats);
        }

        static public void drawStartTetris()
        {
            drawPreviewWindow(120, 500, 200, 160, DrawColor.Shade.COLOR_DK_BLUE, DrawColor.Shade.COLOR_DK_YELLOW);
            SpriteFont StartLabel = new SpriteFont("Tetris", 70, 550);
            SpriteFont RestartLabel = new SpriteFont("Press Space", 60, 500);
            SpriteFont RestartLabel1 = new SpriteFont("To Play", 60, 450);
            StartLabel.Draw();
            RestartLabel.Draw();
            RestartLabel1.Draw();
        }

        static public void drawPaused()
        {
            drawPreviewWindow(120, 500, 200, 160, DrawColor.Shade.COLOR_DK_BLUE, DrawColor.Shade.COLOR_DK_YELLOW);
            SpriteFont PausedLabel = new SpriteFont("Paused", 70, 550);
            SpriteFont PressLabel = new SpriteFont("Press:", 40, 520);
            SpriteFont ContinueLabel = new SpriteFont("'c' to Continue", 40, 500);
            SpriteFont QuitLabel = new SpriteFont("'q' to Quit", 40, 480);
            PausedLabel.Draw();
            PressLabel.Draw();
            ContinueLabel.Draw();
            QuitLabel.Draw();
        }

        static public void drawHighScores(GameStats stats)
        {
            int endX = 200;
            int startY = 320;
            List<String> highScores = stats.readTop5File();

            drawPreviewWindow(120, 240, 200, 220, DrawColor.Shade.COLOR_BLUE, DrawColor.Shade.COLOR_DK_YELLOW);
            SpriteFont highScoresLabel = new SpriteFont("High Scores", 55, startY);
            highScoresLabel.Draw();

            int scoresCount = 1;
            foreach (String score in highScores)
            {
                int drawX = endX - (score.Length * 15);
                int drawY = startY - (scoresCount * 30);
                SpriteFont scoresLabel = new SpriteFont(score, drawX, drawY);
                scoresLabel.Draw();

                scoresCount++;
            }
        }
    }
}
