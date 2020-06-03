using System;
using System.Diagnostics;

namespace Tetris
{
    class Program
    {
        static void Main(string[] args)
        {
            // Create the instance
            Tetris game = new Tetris();
            Debug.Assert(game != null);

            // Start the game
            game.Run();
        }
    }
}
