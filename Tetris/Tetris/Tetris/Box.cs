using System;

namespace Tetris
{
     class Box
    {
		public int x;
		public int y;
		public DrawColor.Shade color;

        public Box()
        {

        }

		public Box(int dx, int dy)
		{
			x = dx;
			y = dy;
		}

		public Box(int dx, int dy, DrawColor.Shade c)
		{
			x = dx;
			y = dy;
			color = c;
		}

        public void draw()
        {
            SOM.drawBox(x, y, color);
        }

        public void draw(DrawColor.Shade color)
        {
            SOM.drawBox(x, y, color);
        }

        public void stringWrite()
        {
            Console.WriteLine(x+" : "+y);
        }
	}
}
