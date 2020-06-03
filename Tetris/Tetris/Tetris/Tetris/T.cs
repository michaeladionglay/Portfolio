using System;

namespace Tetris
{
    class T: Shape
    {

		public T(int cx, int cy)
		{
			this.color = DrawColor.Shade.COLOR_YELLOW;
			this.coor_x = cx;
			this.coor_y = cy;
			this.name = "T";
			this.direction = "up";
			this.orientation = T.Orientation.ORIENT_0;
            this.originColor = DrawColor.Shade.COLOR_DK_YELLOW;
			Box origin = new Box(coor_x, coor_y, color);
			Box a = new Box(coor_x-1, coor_y, color);
			Box b = new Box(coor_x+1, coor_y, color);
			Box c = new Box(coor_x, coor_y - 1, color);
			this.boxes = new Box[] { origin, a, b, c };
			
		}

        public override void setCoordXY(int newcx, int newcy)
        {
            this.coor_x = newcx;
            this.coor_y = newcy;
            Box origin = new Box(coor_x, coor_y, color);
            Box a = new Box(coor_x - 1, coor_y, color);
            Box b = new Box(coor_x + 1, coor_y, color);
            Box c = new Box(coor_x, coor_y - 1, color);
            this.boxes = new Box[] { origin, a, b, c };
        }


        public override void setUpNewOrientation(Orientation orient)
        {
            Box a = new Box();
            Box origin = new Box();
            Box b = new Box();
            Box c = new Box();

            switch (orient)
            {
                case T.Orientation.ORIENT_0:
                    if (coor_x - 1 < Constants.GAME_MIN_X)
                        coor_x++;
                    if (coor_x + 1 > Constants.GAME_MAX_X)
                        coor_x--;
                    if (coor_y - 1 < Constants.GAME_MIN_Y)
                        coor_y++;
                    a = new Box(coor_x - 1, coor_y, color);
                    origin = new Box(coor_x, coor_y, color);
                    b = new Box(coor_x + 1, coor_y, color);
                    c = new Box(coor_x, coor_y - 1, color);
                    boxes = new Box[] { origin, a, b, c };
                    break;

                case T.Orientation.ORIENT_1:
                    if (coor_x - 1 < Constants.GAME_MIN_X)
                        coor_x++;
                    if (coor_y + 1 > Constants.GAME_MAX_Y)
                        coor_y--;
                    if (coor_y - 1 < Constants.GAME_MIN_Y)
                        coor_y++;
                    a = new Box(coor_x, coor_y + 1, color);
                    origin = new Box(coor_x, coor_y, color);
                    b = new Box(coor_x, coor_y - 1, color);
                    c = new Box(coor_x - 1, coor_y, color);
                    boxes = new Box[] { origin, a, b, c };
                    break;

                case T.Orientation.ORIENT_2:
                    if (coor_x - 1 < Constants.GAME_MIN_X)
                        coor_x++;
                    if (coor_x + 1 > Constants.GAME_MAX_X)
                        coor_x--;
                    if (coor_y + 1 > Constants.GAME_MAX_Y)
                        coor_y--;
                    a = new Box(coor_x, coor_y + 1, color);
                    origin = new Box(coor_x, coor_y, color);
                    b = new Box(coor_x - 1, coor_y, color);
                    c = new Box(coor_x + 1, coor_y, color);
                    boxes = new Box[] { origin, a, b, c };
                    break;


                case T.Orientation.ORIENT_3:
                    if (coor_x + 1 > Constants.GAME_MAX_X)
                        coor_x--;
                    if (coor_y + 1 > Constants.GAME_MAX_Y)
                        coor_y--;
                    if (coor_y - 1 < Constants.GAME_MIN_Y)
                        coor_y++;
                    a = new Box(coor_x + 1, coor_y, color);
                    origin = new Box(coor_x, coor_y, color);
                    b = new Box(coor_x, coor_y + 1, color);
                    c = new Box(coor_x, coor_y - 1, color);
                    boxes = new Box[] { origin, a, b, c };
                    break;

                default:
                    break;
            }
        }

		public void erase(Orientation orient)
		{
			switch (orient)
			{
				case T.Orientation.ORIENT_0:
					SOM.drawBox(coor_x - 1, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x + 0, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x + 1, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x + 0, coor_y - 1, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					break;

				case T.Orientation.ORIENT_1:
					SOM.drawBox(coor_x, coor_y + 1, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x, coor_y + 0, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x, coor_y - 1, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x - 1, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					break;

				case T.Orientation.ORIENT_2:
					SOM.drawBox(coor_x + 0, coor_y + 1, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x - 1, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x + 0, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x + 1, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					break;

				case T.Orientation.ORIENT_3:
					SOM.drawBox(coor_x + 1, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x, coor_y + 1, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x, coor_y + 0, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x, coor_y - 1, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					break;

				default:
					break;
			}
		}

 
    }
}
