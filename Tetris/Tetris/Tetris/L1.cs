﻿using System;

namespace Tetris
{
    class L1: Shape
    {

		public L1(int cx, int cy)
		{
			this.coor_x = cx;
			this.coor_y = cy;
			this.name = "L1";
			this.direction = "up";
			this.orientation = L1.Orientation.ORIENT_0;
            this.color = DrawColor.Shade.COLOR_BLUE;
            this.originColor = DrawColor.Shade.COLOR_DK_BLUE;
            Box origin = new Box(coor_x, coor_y, color);
			Box a = new Box(coor_x + 1, coor_y, color);
			Box b = new Box(coor_x + 2, coor_y, color);
			Box c = new Box(coor_x, coor_y - 1, color);
			this.boxes = new Box[] { origin, a, b, c };
		}

        public override void setCoordXY(int newcx, int newcy)
        {
            this.coor_x = newcx;
            this.coor_y = newcy;
            Box origin = new Box(coor_x, coor_y, color);
            Box a = new Box(coor_x + 1, coor_y, color);
            Box b = new Box(coor_x + 2, coor_y, color);
            Box c = new Box(coor_x, coor_y - 1, color);
            this.boxes = new Box[] { origin, a, b, c };

        }

        public override bool setUpNewOrientation(Orientation orient)
        {
            Box a = new Box();
            Box b = new Box();
            Box c = new Box();
            Box origin = new Box();
            bool isValid = true;

            Box[] temp = new Box[4];

            switch (orient)
            {
                case Shape.Orientation.ORIENT_0:
                    if (coor_x + 2 > Constants.GAME_MAX_X || coor_x + 1 > Constants.GAME_MAX_X || coor_y - 1 < Constants.GAME_MIN_Y)
                    {
                        isValid = false;
                        break;
                    }
                    a = new Box(coor_x + 1, coor_y, color);
                    origin = new Box(coor_x, coor_y, color);
                    b = new Box(coor_x + 2, coor_y, color);
                    c = new Box(coor_x, coor_y - 1, color);

                    temp = new Box[] { origin, a, b, c };

                    isValid = areBoxesFree(temp);

                    if (isValid)
                    {
                        boxes = temp;
                    }

                    break;

                case Shape.Orientation.ORIENT_1:
                    if (coor_x - 1 < Constants.GAME_MIN_X || coor_y - 2 < Constants.GAME_MIN_Y || coor_y - 1 < Constants.GAME_MIN_Y)
                    {
                        isValid = false;
                        break;
                    }
                    a = new Box(coor_x - 1, coor_y, color);
                    origin = new Box(coor_x, coor_y, color);
                    b = new Box(coor_x, coor_y - 1, color);
                    c = new Box(coor_x, coor_y - 2, color);
                    temp = new Box[] { origin, a, b, c };

                    isValid = areBoxesFree(temp);


                    if (isValid)
                    {
                        boxes = temp;
                    }
                    break;

                case Shape.Orientation.ORIENT_2:
                    if (coor_x - 2 < Constants.GAME_MIN_X || coor_x - 1 < Constants.GAME_MIN_X || coor_y + 1 > Constants.GAME_MAX_Y)
                    {
                        isValid = false;
                        break;
                    }
                    a = new Box(coor_x - 2, coor_y, color);
                    origin = new Box(coor_x, coor_y, color);
                    b = new Box(coor_x - 1, coor_y, color);
                    c = new Box(coor_x, coor_y + 1, color);
                    temp = new Box[] { origin, a, b, c };

                    isValid = areBoxesFree(temp);


                    if (isValid)
                    {
                        boxes = temp;
                    }
                    break;

                case Shape.Orientation.ORIENT_3:
                    if (coor_x + 1 > Constants.GAME_MAX_X || coor_y + 2 > Constants.GAME_MAX_Y || coor_y + 1 > Constants.GAME_MAX_Y)
                    {
                        isValid = false;
                        break;
                    }
                    a = new Box(coor_x, coor_y + 2, color);
                    origin = new Box(coor_x, coor_y, color);
                    b = new Box(coor_x, coor_y + 1, color);
                    c = new Box(coor_x + 1, coor_y, color);
                    temp = new Box[] { origin, a, b, c };

                    isValid = areBoxesFree(temp);


                    if (isValid)
                    {
                        boxes = temp;
                    }
                    break;

                default:
                    isValid = false;
                    break;
            }
            return isValid;
        }

     

		public void erase(Orientation orient)
		{
			switch (orient)
			{
				case Shape.Orientation.ORIENT_0:
					SOM.drawBox(coor_x + 0, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x + 1, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x + 2, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x + 0, coor_y - 1, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					break;

				case Shape.Orientation.ORIENT_1:

					SOM.drawBox(coor_x - 1, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x + 0, coor_y + 0, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x + 0, coor_y - 1, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x + 0, coor_y - 2, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					break;

				case Shape.Orientation.ORIENT_2:
					SOM.drawBox(coor_x - 2, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x - 1, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x + 0, coor_y, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x + 0, coor_y + 1, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					break;

				case Shape.Orientation.ORIENT_3:
					SOM.drawBox(coor_x, coor_y + 2, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x, coor_y + 1, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x, coor_y + 0, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					SOM.drawBox(coor_x + 1, coor_y + 0, DrawColor.Shade.COLOR_BACKGROUND_CUSTOM);
					break;

				default:
					break;
			}
		}




    }
}
