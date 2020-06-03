using System;

namespace Tetris
{
    class Square: Shape
    {

		public Square(int cx, int cy)
		{
			this.coor_x = cx;
			this.coor_y = cy;
			this.name = "Square";
			this.direction = "up";
			this.orientation = Shape.Orientation.ORIENT_0;
            this.color = DrawColor.Shade.COLOR_RED;
            this.originColor = DrawColor.Shade.COLOR_DK_RED;
            Box origin = new Box(coor_x, coor_y, color);
			Box a = new Box(coor_x + 1, coor_y, color);
			Box b = new Box(coor_x + 1, coor_y - 1, color);
			Box c = new Box(coor_x, coor_y - 1, color);
			this.boxes = new Box[] { origin, a, b, c };
		}

        public override void setCoordXY(int newcx, int newcy)
        {
            this.coor_x = newcx;
            this.coor_y = newcy;
            Box origin = new Box(coor_x, coor_y, color);
            Box a = new Box(coor_x + 1, coor_y, color);
            Box b = new Box(coor_x + 1, coor_y - 1, color);
            Box c = new Box(coor_x, coor_y - 1, color);
            this.boxes = new Box[] { origin, a, b, c };
        }

        public override void cycleOrientation()
        {
            // Noo-op for rotation of squares
        }

    
        public override void setUpNewOrientation(Orientation o)
        {
            // it's hip to be square
        }
    }
}
