using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Tetris
{
    static class Grid
    {
        public static Box[,] boxes = new Box[Constants.GAME_MAX_X+ 1, Constants.GAME_MAX_Y + 1];

        public static void InitializeGrid()
        {
            Console.WriteLine("Initialized Grid");
            for (int i = 0; i < Constants.GAME_MAX_X + 1; i++)
            {
                for (int j = 0; j < Constants.GAME_MAX_Y + 1; j++)
                {
                    boxes[i, j] = null;
                }
            }
        }

        public static void  passGrid(Box[,] boxes)
        {
            Grid.boxes = boxes;
        }

        public static void addToGrid(int x,int y,Box box)
        {
            //if (boxes[x, y] == null)
            //{
                boxes[x, y] = box;
             //   Console.WriteLine("Added to " + x + " : " + y);
            //}

        }


    }
}
