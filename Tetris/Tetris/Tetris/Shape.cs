using System;

namespace Tetris
{
    abstract class Shape
    {
        public Box[] boxes;
        public DrawColor.Shade color;
        public DrawColor.Shade originColor;
        public String name;
        public String direction;
        public Orientation orientation;
        public int coor_x;
        public int coor_y;


        public abstract void setCoordXY(int newcx, int newcy);
        public void setToPlay()
        {
            this.setCoordXY(5, Constants.GAME_MAX_Y);
        }
        public enum Orientation
        {
            ORIENT_0,
            ORIENT_1,
            ORIENT_2,
            ORIENT_3
        };

        public enum Direction
        {
            LEFT,
            RIGHT,
            DOWN
        };

        public void draw()
        {
            foreach (Box b in boxes)
            {
                if (b.x == coor_x && b.y == coor_y)
                    b.draw(originColor);
                else
                    b.draw();
            }
        }

        public Box[] getNextPositions(Direction dir)
        {
            Box[] nextPositions = new Box[boxes.Length];

            if (dir == Direction.LEFT)
            {
                for (int i = 0; i < boxes.Length; i++)
                {
                    Box currentBox = boxes[i];
                    Box newBox = new Box(currentBox.x - 1, currentBox.y);
                    nextPositions[i] = newBox;
                }
            }
            else if (dir == Direction.RIGHT)
            {
                for (int i = 0; i < boxes.Length; i++)
                {
                    Box currentBox = boxes[i];
                    Box newBox = new Box(currentBox.x + 1, currentBox.y);
                    nextPositions[i] = newBox;
                }
            }
            else if (dir == Direction.DOWN)
            {
                for (int i = 0; i < boxes.Length; i++)
                {
                    Box currentBox = boxes[i];
                    Box newBox = new Box(currentBox.x, currentBox.y - 1);
                    nextPositions[i] = newBox;
                }
            }

            return nextPositions;
        }

        public void moveRight()
        {
            if (tryMoveRight())
            {
                boxes[0].x++;
                boxes[1].x++;
                boxes[2].x++;
                boxes[3].x++;
                coor_x++;
            }
        }

        public Boolean tryMoveRight()
        {
            Box[] nextPoints = this.getNextPositions(Direction.RIGHT);
            foreach(Box b in nextPoints)
            {
                // Addition: Check that the piece is in bounds (was causing an error)
                if(b.y > Constants.GAME_MAX_Y)
                {
                    return false;
                }
                if(ZeroOneGrid.zeroGrid[b.y, b.x] == 1)
                {
                    return false;
                }
            }
            return true;
        }
        public void moveLeft()
        {
            if (tryMoveLeft())
            {
                boxes[0].x--;
                boxes[1].x--;
                boxes[2].x--;
                boxes[3].x--;
                coor_x--;
            }

        }

        public Boolean tryMoveLeft()
        {
            Box[] nextPoints = this.getNextPositions(Direction.LEFT);
            foreach (Box b in nextPoints)
            {
                // Same addition as in try move right
                if(b.y > Constants.GAME_MAX_Y)
                {
                    return false;
                }
                if (ZeroOneGrid.zeroGrid[b.y, b.x] == 1)
                {
                    return false;
                }
            }
            return true;
        }
        public abstract bool setUpNewOrientation(Orientation o);


        public bool areBoxesFree(Box[] temp)
        {
            bool isValid = true;

            foreach (Box box in temp)
            {
                if (ZeroOneGrid.zeroGrid[box.y, box.x] == 1)
                    isValid = false;
            }

            return isValid;
        }
       
       public virtual void cycleOrientation()
        {
            switch (orientation)
            {
                case Shape.Orientation.ORIENT_0:
                    if (!setUpNewOrientation(Shape.Orientation.ORIENT_1))
                    {
                        orientation = Shape.Orientation.ORIENT_1;
                        cycleOrientation();
                    }
                    else
                    {
                        orientation = Shape.Orientation.ORIENT_1;
                    }
                    break;
                case Shape.Orientation.ORIENT_1:
                    if (!setUpNewOrientation(Shape.Orientation.ORIENT_2))
                    {
                        orientation = Shape.Orientation.ORIENT_2;
                        cycleOrientation();
                    }
                    else
                    {
                        orientation = Shape.Orientation.ORIENT_2;
                    }
                    break;
                case Shape.Orientation.ORIENT_2:
                    if (!setUpNewOrientation(Shape.Orientation.ORIENT_3))
                    {
                        orientation = Shape.Orientation.ORIENT_3;
                        cycleOrientation();
                    }
                    else
                    {
                        orientation = Shape.Orientation.ORIENT_3;
                    }
                    break;
                case Shape.Orientation.ORIENT_3:
                    if (!setUpNewOrientation(Shape.Orientation.ORIENT_0))
                    {
                        orientation = Shape.Orientation.ORIENT_0;
                        cycleOrientation();
                    }
                    else
                    {
                        orientation = Shape.Orientation.ORIENT_0;
                    }
                    break;
                default:
                    if (!setUpNewOrientation(Shape.Orientation.ORIENT_0))
                    {
                        orientation = Shape.Orientation.ORIENT_0;
                        cycleOrientation();
                    }
                    else
                    {
                        orientation = Shape.Orientation.ORIENT_0;
                    }
                    break;
            }
        }
    


        public void tryFall()
        {

            Box[] nextPoints = this.getNextPositions(Direction.DOWN);
            if(boxes[0].y != 0 && boxes[1].y != 0 && boxes[2].y != 0 && boxes[3].y != 0)
            {
                if (ZeroOneGrid.zeroGrid[boxes[0].y, boxes[0].x] == 0)
                {
                    boxes[0].y--;
                    boxes[1].y--;
                    boxes[2].y--;
                    boxes[3].y--;
                    coor_y--;
                }
            }
        }

        public int getLowestPoint()
        {
            int low = boxes[0].y;
            foreach (Box b in boxes)
            {
                if (b.y < low)
                {
                    low = b.y;
                }
            }
            return low;
        }

        public Boolean didHit()
        {
            Box[] nextBoxes = this.getNextPositions(Direction.DOWN);
             Console.WriteLine(nextBoxes[0].y + " " + nextBoxes[0].x);
             Console.WriteLine(nextBoxes[1].y + " " + nextBoxes[1].x);
             Console.WriteLine(nextBoxes[2].y + " " + nextBoxes[2].x);
             Console.WriteLine(nextBoxes[3].y + " " + nextBoxes[3].x); 
           
                if (ZeroOneGrid.zeroGrid[nextBoxes[0].y, nextBoxes[0].x] == 0 &&
                    ZeroOneGrid.zeroGrid[nextBoxes[1].y, nextBoxes[1].x] == 0 &&
                    ZeroOneGrid.zeroGrid[nextBoxes[2].y, nextBoxes[2].x] == 0 &&
                    ZeroOneGrid.zeroGrid[nextBoxes[3].y, nextBoxes[3].x] == 0)
                    return false;
            
            
            return true;
        }

        public Boolean didHitBin()
        {
            if (boxes[0].y != 0 && boxes[1].y != 0 && boxes[2].y != 0 && boxes[3].y != 0)
                return false;
            return true;
        }

        public int getLeftmostPoint()
        {
            int left = boxes[0].x;
            foreach (Box b in boxes)
            {
                if (b.x < left)
                {
                    left = b.x;
                }
            }
            return left;
        }

        public int getHighestPoint()
        {
            int high = boxes[0].y;
            foreach (Box b in boxes)
            {
                if (b.y > high)
                {
                    high = b.y;
                }
            }
            return high;

        }

        public int getRightmostPoint()
        {
            int right = boxes[0].x;
            foreach (Box b in boxes)
            {
                if (b.x > right)
                {
                    right = b.x;
                }
            }
            return right;
        }

        public void turn()
        {
            switch (orientation)
            {
                case Shape.Orientation.ORIENT_0:
                    orientation = Shape.Orientation.ORIENT_1;
                    break;
                case Shape.Orientation.ORIENT_1:
                    orientation = Shape.Orientation.ORIENT_2;
                    break;
                case Shape.Orientation.ORIENT_2:
                    orientation = Shape.Orientation.ORIENT_3;
                    break;
                case Shape.Orientation.ORIENT_3:
                    orientation = Shape.Orientation.ORIENT_0;
                    break;
            }
        }
    }
}
