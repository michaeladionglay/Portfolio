using System;
using System.Collections.Generic;
using System.IO;

namespace Tetris
{
    class GameStats
    {
        // data
        private int levelNum;
        private int lineCount;
        private int gameScore;

        public GameStats()
        {
            this.levelNum = 1;
            this.lineCount = 0;
            this.gameScore = 0;
        }

        public int getLevelNum()
        {
            return this.levelNum;
        }

        public void setLevelNum(int level)
        {
            this.levelNum = level;
        }

        public int getLineCount()
        {
            return this.lineCount;
        }

        public void setLineCount(int line)
        {
            this.lineCount = line;
        }

        public int getScore()
        {
            return this.gameScore;
        }

        public void setScore(int score)
        {
            this.gameScore = score;
        }

        public void updateStats(int removedLines)
        {
            // update score
            switch (removedLines)
            {
                case 1:
                    this.gameScore += 40 * (levelNum);
                    break;
                case 2:
                    this.gameScore += 100 * (levelNum);
                    break;
                case 3:
                    this.gameScore += 300 * (levelNum);
                    break;
                case 4:
                    this.gameScore += 1200 * (levelNum);
                    break;
                default:
                    break;
            }

            // update line count
            this.lineCount += removedLines;

            // update level number
            levelNum = (lineCount / 10) + 1;
        }

        public List<String> readTop5File()
        {
            StreamReader sr = new StreamReader(@"HighScore.txt");
            string highScoresText = sr.ReadLine();
            sr.Close();

            String[] highScoresArr = highScoresText.Split(',');
            List<String> highScoresList = new List<string>();
            foreach (String score in highScoresArr)
            {
                if (score != "0")
                {
                    highScoresList.Add(score);
                }
            }

            return highScoresList;
        }

        public void updateTop5()
        {
            List<String> highScores = readTop5File();

            int curCapacity = highScores.Capacity;
            if (curCapacity < 5)
            {
                int remainCapacity = 5 - curCapacity;
                for (int i = 0; i <= remainCapacity; i++)
                {
                    highScores.Add("0");
                }
            }

            for (int i = 0; i < highScores.Count; i++)
            {
                if (gameScore > int.Parse(highScores[i]))
                {
                    for (int j = 4; j > i; j--)
                    {
                        highScores[j] = highScores[j - 1];
                    }
                    highScores[i] = gameScore.ToString();

                    StreamWriter sw = new StreamWriter(@"HighScore.txt", false);
                    String finalScores = String.Join(",", highScores);
                    sw.Write(finalScores);
                    sw.Flush();
                    sw.Close();

                    break;
                }
            }
        }
    }
}
