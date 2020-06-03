using System;

namespace Tetris
{
    static class DrawColor
    {
        public enum Shade
        {
            COLOR_ORANGE,
            COLOR_DK_ORANGE,
            COLOR_BLUE,
            COLOR_DK_BLUE,
            COLOR_PURPLE,
            COLOR_DK_PURPLE,
            COLOR_LT_GREEN,
            COLOR_DK_GREEN,
            COLOR_LT_BLUE,
            COLOR_YELLOW,
            COLOR_DK_YELLOW,
            COLOR_RED,
            COLOR_DK_RED,
            COLOR_CYAN,
            COLOR_DK_CYAN,
            COLOR_GREY,
            COLOR_DK_GREY,
            COLOR_BACKGROUND_CUSTOM
        };

        public static Azul.Color getColor(DrawColor.Shade color)
        {
            Azul.Color tmp;

            switch (color)
            {
                case DrawColor.Shade.COLOR_ORANGE:
                    tmp = new Azul.Color(1.0f, 0.6f, 0.0f);
                    break;

                case DrawColor.Shade.COLOR_DK_ORANGE:
                    tmp = new Azul.Color(0.85f, 0.45f, 0.0f);
                    break;

                case DrawColor.Shade.COLOR_BLUE:
                    tmp = new Azul.Color(0.0f, 0.0f, 1.0f);
                    break;

                case DrawColor.Shade.COLOR_DK_BLUE:
                    tmp = new Azul.Color(0.0f, 0.0f, 0.80f);
                    break;

                case DrawColor.Shade.COLOR_PURPLE:
                    tmp = new Azul.Color(0.95f, 0.0f, 0.9f);
                    break;

                case DrawColor.Shade.COLOR_DK_PURPLE:
                    tmp = new Azul.Color(0.8f, 0.0f, 0.7f);
                    break;

                case DrawColor.Shade.COLOR_LT_GREEN:
                    tmp = new Azul.Color(0.25f, 1.0f, 0.25f);
                    break;

                case DrawColor.Shade.COLOR_DK_GREEN:
                    tmp = new Azul.Color(0.0f, 0.75f, 0.0f);
                    break;

                case DrawColor.Shade.COLOR_LT_BLUE:
                    tmp = new Azul.Color(0.25f, 0.25f, 1.0f);
                    break;

                case DrawColor.Shade.COLOR_YELLOW:
                    tmp = new Azul.Color(1.0f, 1.0f, 0.0f);
                    break;

                case DrawColor.Shade.COLOR_DK_YELLOW:
                    tmp = new Azul.Color(0.75f, 0.75f, 0.0f);
                    break;

                case DrawColor.Shade.COLOR_RED:
                    tmp = new Azul.Color(1.0f, 0.2f, 0.2f);
                    break;

                case DrawColor.Shade.COLOR_DK_RED:
                    tmp = new Azul.Color(0.9f, 0.10f, 0.10f);
                    break;

                case DrawColor.Shade.COLOR_CYAN:
                    tmp = new Azul.Color(0.0f, 1.0f, 1.0f);
                    break;

                case DrawColor.Shade.COLOR_DK_CYAN:
                    tmp = new Azul.Color(0.0f, 0.75f, 0.75f);
                    break;

                case DrawColor.Shade.COLOR_GREY:
                    tmp = new Azul.Color(0.5f, 0.5f, 0.5f);
                    break;

                case DrawColor.Shade.COLOR_DK_GREY:
                    tmp = new Azul.Color(0.75f, 0.75f, 0.75f);
                    break;

                case DrawColor.Shade.COLOR_BACKGROUND_CUSTOM:
                    tmp = new Azul.Color(0.2f, 0.2f, 0.25f);
                    break;

                default:
                    tmp = new Azul.Color(1.0f, 1.0f, 1.0f);
                    break;
            }

            return tmp;
        }
    }
}
