using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Tetris
{
    static class ZeroOneGrid
    {
        public static int[,] zeroGrid = new int[30,10];

        public static void initializeGrid()
        {
            for (int i = 0; i < 30; i++)
                for (int j = 0; j < 10; j++)
                    zeroGrid[i, j] = 0;
        }

        public static void printArray()
        {
            for (int i = 29; i > -1; i--)
            {
                for (int j = 9; j > -1; j--)
                {
                    Console.Write(zeroGrid[i, j]);
                }
                Console.WriteLine();
            }
                
        }

    }
}
